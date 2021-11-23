package alb.project.vacation.domain;

import alb.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * @author HaoHao
 * Created on 2021/10/25.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class HolidayItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /* 假期主键 */
    private Long holidayId;

    /* 审批人Id */
    private Long approvedUserId;
    private String approvedUserName;

    /* 审批序号 从1开始计数 */
    private Integer approvedIndex;

    /* 审批状态 0未处理，1通过，2驳回 */
    private Integer status;

    /* 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /* 审批说明 */
    private String approveInstruction;

    /**
     * 删除状态0未1已
     */
    private Integer delStatus;
}
