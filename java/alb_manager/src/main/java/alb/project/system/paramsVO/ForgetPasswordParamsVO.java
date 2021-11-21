package alb.project.system.paramsVO;

import alb.framework.aspectj.lang.annotation.Phone;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @ClassName ForgetPasswordParamsVO
 * @Description 忘记密码参数封装
 * @Date 2020/7/13 19:33
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ForgetPasswordParamsVO {

    @Phone(message = "手机号码格式不正确！")
    @NotNull(message = "手机号码为空！")
    private String phone;

    @NotNull(message = "验证码为空！")
    @Length(min = 6, max = 6, message = "验证码长度为6位！")
    private String code;

    @NotNull(message = "密码为空！")
    @Length(min = 6,max = 20,message = "密码长度限制6-20字符！")
    private String password;

    @NotNull(message = "请再次输入密码！")
    @Length(min = 6,max = 20,message = "密码长度限制6-20字符！")
    private String againPassword;

    @NotNull(message = "唯一标识为空！")
    private String uuid;

}
