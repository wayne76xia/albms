package alb.project.common.paramsVO;

import alb.framework.aspectj.lang.annotation.Phone;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发送验证码请求参数
 */
@Data
public class SendSmsParams {

    @NotNull(message = "手机号码为空！")
    @Phone(message = "手机号不正确！")
    private String phone;

    @NotNull(message = "验证码请求类型为空！")
    private Integer dealType;

    @NotNull(message = "模板类型为空！")
    private Integer templateType;
}
