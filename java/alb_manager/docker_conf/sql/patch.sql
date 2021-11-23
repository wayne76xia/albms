-- COMPENSATORY_LEAVE(1, "调休"),
-- COMPASSIONATE_LEAVE(2, "事假"),
-- ANNUAL_LEAVE(3, "年假"),
-- MARRIAGE_LEAVE(4, "婚假"),
-- SICK_LEAVE(5, "病假"),
-- LACTATION_LEAVE(6, "哺乳假"),
-- MATERNITY_LEAVE(7, "产假"),
-- PATERNITY_LEAVE(8, "陪产假"),
-- FUNERAL_LEAVE(9, "丧假"),
-- PUBLIC_LEAVE(10, "公假"),
-- .*_LEAVE\((\d*), \"(\S*)\"\),
-- INSERT INTO `sys_dict_data` VALUES (3$2, $2, '$3', '$2', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, '$1');

INSERT INTO `sys_dict_type` VALUES (12, '假期类型', 'holiday_type', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, '', '假期类型列表');

INSERT INTO `sys_dict_data` VALUES (31, 1, '调休', '1', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'COMPENSATORY_LEAVE');
INSERT INTO `sys_dict_data` VALUES (32, 2, '事假', '2', 'holiday_type', '', '', 'Y', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'COMPASSIONATE_LEAVE');
INSERT INTO `sys_dict_data` VALUES (33, 3, '年假', '3', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'ANNUAL_LEAVE');
INSERT INTO `sys_dict_data` VALUES (34, 4, '婚假', '4', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'MARRIAGE_LEAVE');
INSERT INTO `sys_dict_data` VALUES (35, 5, '病假', '5', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'SICK_LEAVE');
INSERT INTO `sys_dict_data` VALUES (36, 6, '哺乳假', '6', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'LACTATION_LEAVE');
INSERT INTO `sys_dict_data` VALUES (37, 7, '产假', '7', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'MATERNITY_LEAVE');
INSERT INTO `sys_dict_data` VALUES (38, 8, '陪产假', '8', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'PATERNITY_LEAVE');
INSERT INTO `sys_dict_data` VALUES (39, 9, '丧假', '9', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'FUNERAL_LEAVE');
INSERT INTO `sys_dict_data` VALUES (40, 10, '公假', '10', 'holiday_type', '', '', 'N', '0', 'admin', '2021-11-20 14:23:20', NULL, NULL, 'PUBLIC_LEAVE');

DROP TABLE IF EXISTS `holiday_approval`;
CREATE TABLE `holiday_approval`  (
     `holiday_approval_id` bigint(20) NOT NULL COMMENT '假期审批ID',
     `holiday_type_id` bigint(20) NOT NULL COMMENT '假期类型ID',
     `role_id` bigint(20) NOT NULL COMMENT '待审批人角色ID',
     `approved_role_id` bigint(20) NOT NULL COMMENT '审批人角色ID',
     `current_approved_index` tinyint(4) NOT NULL COMMENT '当前审批序号 从1开始计数',
     `next_approval_id` bigint(20) NOT NULL COMMENT '下一级审批的的ID，若为最后一级审批则为0',
     `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
     `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
     `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`holiday_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
