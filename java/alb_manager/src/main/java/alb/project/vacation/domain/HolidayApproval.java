package alb.project.vacation.domain;

import alb.framework.web.domain.BaseEntity;
import alb.project.vacation.utils.LongJsonDeserializer;
import alb.project.vacation.utils.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long holidayApprovalId;

    /* 假期类型 */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long holidayTypeId;

    private String typeName;

    private String typeValue;

    /* 待审批人角色ID */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long roleId;

    private String roleName;

    /* 审批人角色ID */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long approvedRoleId;

    private String approvedRoleName;

    /* 当前审批序号 从1开始计数 */
    private Integer currentApprovedIndex;

    /* 下一级审批的的角色ID，若为最后一级审批则为0 */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long nextApprovalId;

}
