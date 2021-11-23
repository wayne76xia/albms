package alb.project.vacation.domain;

import alb.framework.web.domain.BaseEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class HolidayApproval extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /* 假期审批ID */
    private Long holidayApprovalId;

    /* 假期类型 */
    private Long holidayTypeId;

    private String typeName;

    private String typeValue;

    /* 待审批人角色ID */
    private Long roleId;

    /* 审批人角色ID */
    private Long approvedRoleId;

    /* 当前审批序号 从1开始计数 */
    private Integer currentApprovedIndex;

    /* 下一级审批的的ID，若为最后一级审批则为0 */
    private Long nextApprovalId;

    /**
     * 删除状态0未1已
     */
    private Integer delFlag;
}
