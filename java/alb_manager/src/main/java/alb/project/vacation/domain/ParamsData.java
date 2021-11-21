package alb.project.vacation.domain;

import alb.framework.aspectj.lang.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 参数设置对象 params_data
 *
 * @date 2020-07-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamsData implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名字 */
    @Excel(name = "名字")
    private String name;

    /** 值 */
    @Excel(name = "值")
    private String value;

    /** 参数类型（1预付定金比例2库存报警设置3话务员回访时间4验光师回访时间5预约来源6登录页图片7技术客服电话8分公司业绩考核标准9门店考核标准10话务员考核标准） */
    @Excel(name = "参数类型", readConverterExp = "1=预付定金比例2库存报警设置3话务员回访时间4验光师回访时间5预约来源6登录页图片7技术客服电话8分公司业绩考核标准9门店考核标准10话务员考核标准")
    private Integer type;

    /** 前端分组用 */
    private Integer groupType;

    /** 最后一次操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后一次操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastDate;

}
