package alb.project.common;

import alb.common.constant.Constants;
import alb.common.thirdParty.AliYunSms;
import alb.project.common.paramsVO.SendSmsParams;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import alb.common.utils.IdUtils;
import alb.framework.redis.RedisCache;
import alb.framework.web.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static alb.framework.web.domain.AjaxResult.error;
import static alb.framework.web.domain.AjaxResult.success;

/**
 * @ClassName SendSmsController
 * @Description 短信验证码操作处理
 * @Date 2020/7/13 14:46
 */
@RestController
@Slf4j
public class SendSmsController {


    @Autowired
    private RedisCache redisCache;

    // 注册模板
    @Value("${aliyunSms.templateRegist}")
    private String templateRegist;
    // 忘记密码模板
    @Value("${aliyunSms.templateUpdatePassword}")
    private String templateUpdatePassword;
    // 变更信息模板
    @Value("${aliyunSms.templateInformationChange}")
    private String templateInformationChange;

    /**
     *  @Date: 2020/7/14 16:26
     *  @Description: 发送验证码
     */
    @RequestMapping(value = "/sendSMS",method = RequestMethod.POST)
    public AjaxResult sendSMS(@Validated SendSmsParams params,
                              BindingResult bindingResult) throws ClientException {
        // 校验字段
        if (bindingResult.hasErrors()) {
            return error(bindingResult.getFieldError().getDefaultMessage());
        }
        String templateCode = "";
        if (params.getTemplateType() == 1) {
            log.info("请求短信模板：{}", templateRegist);
            templateCode = templateRegist;
        }
        if (params.getTemplateType() == 2) {
            log.info("请求短信模板：{}", templateUpdatePassword);
            templateCode = templateUpdatePassword;
        }
        if (params.getTemplateType() == 3) {
            log.info("请求短信模板：{}", templateInformationChange);
            templateCode = templateInformationChange;
        }
//        // 通过手机号获取用户信息
//        AppAccount appAccount = accountService.jwtFindAccountByPhone(params.getPhone());
//        // 发送验证码操作为 需要用户存在
//        if (params.getDealType() == 1) {
//            // 为空 用户不存在
//            if (appAccount == null) {
//                return ResultVOUtil.error("该用户不存在，请注册！");
//            }
//        }
//        // 发送验证码操作为 不需要用户存在
//        if (params.getDealType() == 0) {
//            // 不为空 用户存在
//            if (appAccount != null) {
//                return ResultVOUtil.error("该用户已存在，请登录！");
//            }
//        }

        String verifyCode = "";
        for (int i = 0; i < 6; i++) {
            char c = (char) (AliYunSms.randomInt(0, 9) + '0');
            verifyCode += String.valueOf(c);
        }

        // 阿里短信接口
        SendSmsResponse response = new AliYunSms(params.getPhone(), templateCode, "{\"code\":\"" + verifyCode + "\"}").sendSms();
        log.error("阿里云短信回调：{}", response.getCode());
        log.error("发送验证码：{}", verifyCode);
        if (!response.getCode().equals("OK")) {
            return error("短信发送失败！");
        }
        // 短信验证码发送成功！
        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        // 放入redis中 设置过期时间为10分钟 时间颗粒度：分钟
        redisCache.setCacheObject(verifyKey, verifyCode, Constants.SMS_EXPIRATION, TimeUnit.MINUTES);
        AjaxResult result = new AjaxResult();
        result.put("uuid",uuid);
        return success(result);
    }



}
