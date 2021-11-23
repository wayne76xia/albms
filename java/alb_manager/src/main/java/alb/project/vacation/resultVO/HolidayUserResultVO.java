package alb.project.vacation.resultVO;

import lombok.*;

/**
 * @author HaoHao
 * Created on 2021/11/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayUserResultVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称(工号)
     */
    private String displayName;

}
