-- =====================================================================
-- 018_wired_chest_furni.sql — furni chest + give furni + has-item-type condition
-- =====================================================================

UPDATE items_base SET interaction_type = 'wf_storage_furni1'
    WHERE item_name = 'wf_storage_furni1'        AND interaction_type <> 'wf_storage_furni1';

UPDATE items_base SET interaction_type = 'wf_storage_furni2'
    WHERE item_name = 'wf_storage_furni2'        AND interaction_type <> 'wf_storage_furni2';

UPDATE items_base SET interaction_type = 'wf_storage_furni_starter'
    WHERE item_name = 'wf_storage_furni_starter' AND interaction_type <> 'wf_storage_furni_starter';

UPDATE items_base SET interaction_type = 'wf_act_give_furni'
    WHERE item_name = 'wf_act_give_furni'        AND interaction_type <> 'wf_act_give_furni';

UPDATE items_base SET interaction_type = 'wf_cnd_chest_has_item_type'
    WHERE item_name = 'wf_cnd_chest_has_item_type' AND interaction_type <> 'wf_cnd_chest_has_item_type';
