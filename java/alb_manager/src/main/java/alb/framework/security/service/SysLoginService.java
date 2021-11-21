package alb.framework.security.service;

import javax.annotation.Resource;

import alb.common.constant.Constants;
import alb.common.exception.ApiException;
import alb.common.exception.CustomException;
import alb.common.exception.user.*;
import alb.framework.redis.RedisCache;
import alb.project.system.domain.SysUser;
import alb.project.system.paramsVO.ForgetPasswordParamsVO;
import alb.project.system.service.ISysUserService;
import alb.common.exception.user.*;
import alb.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import alb.common.utils.MessageUtils;
import alb.framework.manager.AsyncManager;
import alb.framework.manager.factory.AsyncFactory;
import alb.framework.security.LoginUser;

/**
 * 登录校验方法
 *
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        // 强退其他用户
//        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
//        for (String key : keys) {
//            LoginUser user = redisCache.getCacheObject(key);
//            if (user != null){
//                // 如果登录名等于redis存储的用户登录名
//                if(username.equals(user.getUsername())){
//                    // 退出登录
//                    redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + user.getToken());
//                }
//            }
//        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    @Autowired
    private ISysUserService userService;

    /**
     *
     * @Date: 2020/7/13 20:10
     * @Description:短信验证码修改密码
     */
    public int forgetPassword(ForgetPasswordParamsVO params) {
        // 根据手机号码查询用户信息
        SysUser user = userService.selectUserByPhone(params.getPhone());
        if (user == null) {
            throw new ApiException(500, MessageUtils.message("user.null"));
        }
        // 校验两次密码是否输入一致
        if (!params.getPassword().equals(params.getAgainPassword())) {
            throw new ApiException(500, MessageUtils.message("user.password.reset.not.match"));
        }
        // 校验验证码
        // 1.获取参数uuid
        String uuid = params.getUuid();
        // 2.拼接redis的key
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        // 3.从redis中取验证码
        String verifyCode = redisCache.getCacheObject(verifyKey);

        // 未获取到验证码，抛出异常，存记录
        if (verifyCode == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.SMS_RESET_PASSWORD_FAIL, MessageUtils.message("user.sms.expire")));
            throw new SmsCodeExpireException();
        }
        // 验证码不正确
        if (!verifyCode.equals(params.getCode())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.SMS_RESET_PASSWORD_FAIL, MessageUtils.message("user.sms.error")));
            throw new SmsCodeException();
        }
        // 4.清除redis中的验证码
        redisCache.deleteObject(verifyKey);
        // 5.记录短信修改密码成功日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.SMS_RESET_PASSWORD_SUCCESS, MessageUtils.message("user.password.reset.success")));
        // 6.执行修改密码操作
        SysUser resetUser = new SysUser();
        resetUser.setUserId(user.getUserId());
        resetUser.setPassword(SecurityUtils.encryptPassword(params.getPassword()));
        resetUser.setUpdateBy(user.getUserName());
        return userService.resetPwd(resetUser);
    }
}
