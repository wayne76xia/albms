ALTER TABLE holiday_item ADD approve_time name datetime(0) DEFAULT NULL COMMENT '审批时间' AFTER `status`;
ALTER TABLE holiday_item ADD approve_instruction name VARCHAR(255) DEFAULT NULL COMMENT '审批说明' AFTER approve_time;
