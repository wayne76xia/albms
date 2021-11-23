package alb.project.vacation.paramsVO;

import lombok.*;

/**
 * @author HaoHao
 * Created on 2021/11/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayUserParamsVO {

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 角色ID
     */
    private Long roleId;
}
