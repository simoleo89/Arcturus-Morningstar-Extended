-- =====================================================================
-- 019_wired_transactions_scanner.sql
-- =====================================================================
-- Transaction effects/triggers (Nitro layout 104–105, 27–28).
-- Point inert items_base rows at the new classes. Idempotent.
-- Emulator restart required.
-- =====================================================================

UPDATE items_base SET interaction_type = 'wf_act_init_transaction'
    WHERE item_name = 'wf_act_init_transaction'   AND interaction_type <> 'wf_act_init_transaction';

UPDATE items_base SET interaction_type = 'wf_act_cancel_transaction'
    WHERE item_name = 'wf_act_cancel_transaction' AND interaction_type <> 'wf_act_cancel_transaction';

UPDATE items_base SET interaction_type = 'wf_trg_transaction_complete'
    WHERE item_name = 'wf_trg_transaction_complete' AND interaction_type <> 'wf_trg_transaction_complete';

UPDATE items_base SET interaction_type = 'wf_trg_transaction_fail'
    WHERE item_name = 'wf_trg_transaction_fail'   AND interaction_type <> 'wf_trg_transaction_fail';
