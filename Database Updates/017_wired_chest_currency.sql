-- =====================================================================
-- 017_wired_chest_currency.sql — give-from-chest + chest-has-items condition
-- =====================================================================

UPDATE items_base SET interaction_type = 'wf_storage_coins1'
    WHERE item_name = 'wf_storage_coins1'      AND interaction_type <> 'wf_storage_coins1';

UPDATE items_base SET interaction_type = 'wf_storage_coins2'
    WHERE item_name = 'wf_storage_coins2'      AND interaction_type <> 'wf_storage_coins2';

UPDATE items_base SET interaction_type = 'wf_act_give_currency'
    WHERE item_name = 'wf_act_give_currency'   AND interaction_type <> 'wf_act_give_currency';

UPDATE items_base SET interaction_type = 'wf_cnd_chest_has_items'
    WHERE item_name = 'wf_cnd_chest_has_items' AND interaction_type <> 'wf_cnd_chest_has_items';
