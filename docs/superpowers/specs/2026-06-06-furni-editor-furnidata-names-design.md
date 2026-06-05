# Furni editor — edit furnidata names/descriptions (server-authoritative) — Design

- **Date:** 2026-06-06
- **Status:** Draft for review
- **Scope:** Arcturus (emulator) + Nitro-V3 (client). Renderer (Nitro_Render_V3) **unchanged**.
- **Builds on:** `2026-06-04-furni-names-from-json-server-design.md` (the server-authoritative
  names + liveness model, already merged to `main` in all repos). That design left the
  furni-editor **out of scope**; this design brings the editor **into** that model.
- **Out of scope:** NitroV3-Housekeeping (CMS, not on disk), creating/deleting furnidata
  entries (edit-only), per-locale server-side multi-language, description rendering in the
  infostand, the old `feat/furni-names-from-json` 10046 path (removed).

## 1. Goal

Make the furni editor the official UI to **edit a furni's display name + description**, written
to the **furnidata JSON** (the merged source of truth), with the change reflected **live**
everywhere — server-pronounced strings and every connected client — by **reusing the merged
liveness pipeline** (`FurnitureTextProvider.reindex()` → `FurnitureDataReloadComposer` header
**10047** → renderer `applyFurnidataDelta` → `nitro-localization-updated`). The legacy
editor-write path (`FurniDataUpdatedComposer`, header **10046**) is **removed**.

## 2. Source-of-truth contract (extends §2 of the 06-04 design)

| Concern | Owner | Storage | Editor field |
|---|---|---|---|
| `classname` (`item_name` / `Item.name`) | DB | `items_base.item_name` | **read-only** (relabel "Item Name" → "Classname") |
| technical data (dimensions, flags, interaction, effects) | DB | `items_base.*` | editable (unchanged technical editor) |
| **display name** | **furnidata JSON** | per classname | **editable (NEW — the only editable name)** |
| **description** | **furnidata JSON** | per classname | **editable (NEW)** |
| `public_name` (`Item.fullName`) | DB | `items_base.public_name` | **read-only** (label "fallback DB") |

Invariants:
1. The bridge is **`classname`**, not a numeric id. The editor resolves `item_id → classname`
   via `items_base` and writes furnidata keyed by classname.
2. `item_name` (classname) is **immutable**: read-only in the UI **and** removed from the
   server editor whitelist (`FurniEditorHelper.ALLOWED_UPDATE_FIELDS`) — defense in depth.
3. `public_name` keeps its role: fallback when a classname is missing from furnidata, and the
   wired `wf_` technical token. It is **read-only** in the editor (not the editable name).
4. **Edit-only**: the editor can only edit names/descriptions of classnames that **already
   exist** in furnidata and whose furni exists in `items_base`. No create/delete via this path.
5. One furnidata artifact (deploy invariant): the file the editor writes = the file the
   emulator watches = the file the client loads. Reuses `items.furnidata.path` resolution.

## 3. End-to-end flow (edit a display name)

1. Admin opens the furni in the editor (existing `FurniEditorDetailEvent`; detail already
   carries the read-only furnidata entry — now rendered as editable fields).
2. Admin edits "Display name" / "Description" in the new **Furnidata** section and saves.
   A **diff + confirmation** (old → new) is shown before committing (it broadcasts to everyone).
3. Client → `FurniEditorUpdateFurnidataEvent { item_id, name, description }`, gated by the
   existing **`ACC_CATALOGFURNI`** permission (reused — no new permission).
4. Server handler:
   a. `item_id → classname` (verify the furni exists in `items_base`).
   b. **Per-admin rate-limit** check (reject if too frequent).
   c. **Sanitize** input: truncate 256, strip control chars + newlines, **neutralize `%`**.
   d. Under the **serialized furnidata lock** (shared with the watcher reindex thread):
      - `FurnidataWriter.write(classname, name, description)` — **backup** (rotating) + **atomic**
        write (temp + fsync + rename), single-file **or** the winning split-tier file.
      - `FurnitureTextProvider.reindex()` → compute the delta (this classname) → atomic swap.
      - Broadcast `FurnitureDataReloadComposer` (**10047**) in delta mode to all clients.
   e. Write an **audit-log** row (`furnidata_edit_log`).
   f. Respond to the editor with the refreshed detail (admin sees the saved value).
5. Renderer (every client): existing `FurnitureDataReloadEvent` (10047) → `applyFurnidataDelta`
   → patch `FurnitureData` (floor/wall by id) + re-register localization keys → dispatch
   `nitro-localization-updated`. **No renderer change.**
6. Client surfaces refresh live: catalog (`useCatalog`), inventory (`useInventoryFurni`),
   infostand (`useAvatarInfoWidget`) — existing subscribers. **No client change for consumption.**
7. The file watcher, firing afterward on the same write, reindexes → **empty delta** → no
   duplicate broadcast.

`revert`: `FurniEditorRevertFurnidataEvent { item_id }` → restore the last rotating backup of
the target file → reindex → broadcast → audit-log. Same gate.

## 4. Emulator (Arcturus)

### 4.1 `FurnidataWriter` (new, `com.eu.habbo.habbohotel.items`)
Sibling of `FurnidataReader`; resolves the **same** source (`items.furnidata.path` → editor base
path). Seed atomic-write / backup / match-by-classname logic from `feat/furni-names-from-json`.

- **Single file:** parse, locate the entry by classname in `roomitemtypes`/`wallitemtypes`, set
  `name`/`description`, serialize preserving structure, **backup** then **atomic** write.
- **Split-tier:** locate the tier that currently **resolves (wins)** for the classname; edit that
  tier file (backup + atomic). **Manifest untouched** (edit-only). **Path-traversal guard** on
  every tier path (normalize; reject escapes/absolute — closes the gap the legacy reader has).
- **Edit-only:** reject a classname absent from furnidata.
- **Boundary sanitization** on input (256 / strip control+newline / neutralize `%`) — stored value
  is already safe; reader still sanitizes on read (defense in depth).
- **Size guard:** refuse if the furnidata file exceeds the configured max.
- **Rotating backup:** keep last `items.furnidata.edit.backup.keep` (default 10); prune older.
- On any IO/parse failure → throw; caller reports an error and performs **no** reindex/broadcast;
  the temp file is discarded so the live file is untouched.

### 4.2 Handler `FurniEditorUpdateFurnidataEvent` (new, `messages.incoming.furnieditor`)
- Permission **`ACC_CATALOGFURNI`** (reused). Per-admin rate-limit
  (`items.furnidata.edit.ratelimit.ms`, default ~2000).
- Resolve classname; sanitize; under the shared lock: `FurnidataWriter.write` → `reindex()` →
  broadcast `FurnitureDataReloadComposer` (10047). Audit-log. Respond with refreshed detail.

### 4.3 Handler `FurniEditorRevertFurnidataEvent` (new)
- Same gate. Restore last backup → reindex → broadcast → audit-log.

### 4.4 Concurrency
- A single lock (the existing serialized watcher/reindex path) guards write + reindex so editor
  edits and external file edits never race and the `volatile` index never tears.

### 4.5 Removals / edits
- Remove `item_name` from `FurniEditorHelper.ALLOWED_UPDATE_FIELDS`.
- **Delete** `FurniDataUpdatedComposer` + its event/registration (header **10046**).

### 4.6 Audit log (new table, via a `Database Updates` SQL)
`furnidata_edit_log { id, user_id, classname, action ENUM('edit','revert'), old_name, new_name,
old_description, new_description, timestamp }`.

### 4.7 Config keys (added; reuse existing furni keys)
- `items.furnidata.edit.backup.keep` (default 10)
- `items.furnidata.edit.ratelimit.ms` (default 2000)
- `items.furnidata.max.bytes` (size cap; reuse the reader's if already present)

## 5. Client (Nitro-V3)

- **`FurniEditorEditView`**: relabel "Item Name" → **"Classname"** (read-only); **public_name**
  read-only (label "fallback DB"); new **Furnidata** section with editable **Display name** +
  **Description**, **live preview** of the name, **dirty-state** indicator, **char counter / `%`
  warning**, **diff + confirm** on save, **Revert** button.
- **`FurniEditorSearchView`**: search **by furnidata display name** (in addition to
  classname/sprite).
- **`useFurniEditor`**: `updateFurnidata(itemId, name, description)` →
  `FurniEditorUpdateFurnidataEvent` (reload detail on result); `revertFurnidata(itemId)` →
  `FurniEditorRevertFurnidataEvent`; search extended for name. The editor already subscribes to
  live furnidata updates (`nitro-localization-updated`), so the open detail reflects edits live.
- **Gate**: existing `ACC_CATALOGFURNI` via `useHasPermission`.

## 6. Security

1. **`item_name` immutable** — UI read-only + removed from server update whitelist.
2. **Edit-only** — only existing classnames present in furnidata + `items_base`; no create/delete.
3. **Boundary sanitization on write** — 256 / strip control+newline / **neutralize `%`** (keeps
   every `String.replace("%itemname%", name)` / wired `%furni.name%` / Watch&Earn site
   injection-safe); reader re-sanitizes on read.
4. **Path-traversal guard + size cap** in the writer (mirrors the reader).
5. **Atomic write + rotating backup** — never leave a half-written furnidata; recoverable.
6. **Per-admin rate-limit** on save/broadcast (on top of the watcher's min-interval) —
   anti-amplification even from a compromised admin account.
7. **Audit log** of every edit/revert (who/classname/old→new/timestamp).
8. **No client-triggered reindex** beyond the gated editor write — preserve the 06-04 property
   that arbitrary clients cannot induce a broadcast.
9. **Concurrency** — single serialized lock + `volatile` index + atomic swap → no torn reads.

## 7. Edge cases / risks

- **Locale no-clobber (06-04 §7.1):** single-file setup (this hotel) has no per-locale override →
  no issue; for override setups the localization layer re-applies after the delta. Documented limit.
- **Split-tier policy:** edit the winning tier; manifest untouched; reject classnames absent from
  all tiers.
- **Deploy invariant:** editor-written file = emulator-watched file = client-loaded file
  (`items.furnidata.path`).
- **Failure isolation:** write/reindex failure → error to admin, no broadcast, live file untouched.

## 8. Testing

- **Emulator (JUnit):** `FurnidataWriter` single-file edit by classname (backup+atomic);
  split-tier edits the winning tier; rejects non-existent classname; path-traversal rejected;
  sanitization (256 / control / `%`); size guard; `reindex()` delta correct; revert restores last
  backup. Handler: `ACC_CATALOGFURNI` gate, rate-limit, audit-log row written, error path performs
  **no** broadcast and leaves the file intact.
- **Renderer (Vitest):** unchanged (10047 pipeline already covered).
- **Client (Vitest):** `useFurniEditor.updateFurnidata`/`revertFurnidata` happy + error; search by
  name; dirty-state; diff-confirm gating.
- **Manual acceptance:** edit a display name in the editor → live update in catalog + inventory +
  infostand and server-pronounced (wired `%furni.name%`, LTD alert, Watch&Earn) without restart;
  revert restores; search-by-name finds it; classname + public_name read-only; a `%user.name%`
  injection in the name is neutralized; an audit row is written.

## 9. Open questions

- Audit-log storage: dedicated `furnidata_edit_log` table (this design) vs. reuse an existing log
  table — confirm during planning.
- Exact serialization fidelity for split-tier JSON5 (preserve comments?) — single-file JSON write
  is plain; JSON5 round-trip for tier files to be settled in the plan.
