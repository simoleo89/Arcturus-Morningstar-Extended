package com.eu.habbo.habbohotel.items.interactions.wired.chest;

import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates and applies wired contract terms atomically for {@code WiredEffectInitTransaction}.
 */
public final class WiredTransactionExecutor {
    private WiredTransactionExecutor() {
    }

    public static boolean execute(Habbo habbo, Room room, List<InteractionWiredContract> contracts) {
        if (habbo == null || room == null) {
            return false;
        }
        if (contracts == null || contracts.isEmpty()) {
            return true;
        }

        List<PendingChange> pending = new ArrayList<>();

        for (InteractionWiredContract contract : contracts) {
            if (contract == null) {
                continue;
            }
            InteractionWiredChest chest = contract.resolveLinkedChest(room);

            for (ContractTerm term : contract.getTerms()) {
                if (term.direction == ContractTerm.DIR_PAY) {
                    if (!ChestWiredCurrencyUtil.has(habbo, term.currencyType, term.amount)) {
                        return false;
                    }
                    if (chest != null) {
                        pending.add(PendingChange.chestAdd(chest, term.currencyType, term.amount));
                    }
                    pending.add(PendingChange.userTake(habbo, term.currencyType, term.amount));
                } else if (term.direction == ContractTerm.DIR_RECEIVE) {
                    if (chest != null) {
                        if (chest.getContents().count(ChestStorage.KIND_CURRENCY, term.currencyType) < term.amount) {
                            return false;
                        }
                        pending.add(PendingChange.chestTake(chest, term.currencyType, term.amount));
                    }
                    pending.add(PendingChange.userGive(habbo, term.currencyType, term.amount));
                }
            }
        }

        for (PendingChange change : pending) {
            change.apply();
        }

        for (InteractionWiredContract contract : contracts) {
            InteractionWiredChest chest = contract.resolveLinkedChest(room);
            if (chest != null) {
                chest.persistContents();
            }
        }

        return true;
    }

    private static final class PendingChange {
        private enum Kind { USER_TAKE, USER_GIVE, CHEST_TAKE, CHEST_ADD }

        private final Kind kind;
        private final Habbo habbo;
        private final InteractionWiredChest chest;
        private final int currencyType;
        private final int amount;

        private PendingChange(Kind kind, Habbo habbo, InteractionWiredChest chest, int currencyType, int amount) {
            this.kind = kind;
            this.habbo = habbo;
            this.chest = chest;
            this.currencyType = currencyType;
            this.amount = amount;
        }

        static PendingChange userTake(Habbo habbo, int currencyType, int amount) {
            return new PendingChange(Kind.USER_TAKE, habbo, null, currencyType, amount);
        }

        static PendingChange userGive(Habbo habbo, int currencyType, int amount) {
            return new PendingChange(Kind.USER_GIVE, habbo, null, currencyType, amount);
        }

        static PendingChange chestTake(InteractionWiredChest chest, int currencyType, int amount) {
            return new PendingChange(Kind.CHEST_TAKE, null, chest, currencyType, amount);
        }

        static PendingChange chestAdd(InteractionWiredChest chest, int currencyType, int amount) {
            return new PendingChange(Kind.CHEST_ADD, null, chest, currencyType, amount);
        }

        void apply() {
            switch (this.kind) {
                case USER_TAKE:
                    ChestWiredCurrencyUtil.take(this.habbo, this.currencyType, this.amount);
                    break;
                case USER_GIVE:
                    ChestWiredCurrencyUtil.give(this.habbo, this.currencyType, this.amount);
                    break;
                case CHEST_TAKE:
                    this.chest.getContents().take(ChestStorage.KIND_CURRENCY, this.currencyType, this.amount);
                    break;
                case CHEST_ADD:
                    this.chest.getContents().add(ChestStorage.KIND_CURRENCY, this.currencyType, this.amount);
                    break;
                default:
                    break;
            }
        }
    }
}
