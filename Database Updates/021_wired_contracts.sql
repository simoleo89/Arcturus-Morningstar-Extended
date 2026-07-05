-- =====================================================================
-- 021_wired_contracts.sql
-- =====================================================================
-- Wired contract extras (Nitro layout 110–113), executed by Init Transaction.
-- Idempotent. Emulator restart required.
-- =====================================================================

UPDATE items_base SET interaction_type = 'wf_contract_payment'
    WHERE item_name = 'wf_contract_payment'     AND interaction_type <> 'wf_contract_payment';

UPDATE items_base SET interaction_type = 'wf_contract_reward'
    WHERE item_name = 'wf_contract_reward'      AND interaction_type <> 'wf_contract_reward';

UPDATE items_base SET interaction_type = 'wf_contract_trade'
    WHERE item_name = 'wf_contract_trade'       AND interaction_type <> 'wf_contract_trade';

UPDATE items_base SET interaction_type = 'wf_xtra_custom_contract'
    WHERE item_name = 'wf_xtra_custom_contract' AND interaction_type <> 'wf_xtra_custom_contract';
