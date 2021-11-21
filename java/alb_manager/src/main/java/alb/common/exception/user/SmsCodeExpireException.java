package alb.common.exception.user;

/**
 * 验证码失效异常类
 *
 */
public class SmsCodeExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public SmsCodeExpireException()
    {
        super("user.sms.expire", null);
    }
}
