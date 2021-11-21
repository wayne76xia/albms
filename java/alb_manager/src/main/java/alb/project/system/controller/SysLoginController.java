package alb.project.system.controller;

import java.util.List;
import java.util.Set;

import alb.common.constant.Constants;
import alb.project.system.domain.SysMenu;
import alb.project.system.domain.SysUser;
import alb.project.system.paramsVO.ForgetPasswordParamsVO;
import alb.project.system.service.ISysUserService;
import alb.framework.redis.RedisCache;
import alb.framework.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import alb.common.utils.ServletUtils;
import alb.framework.security.LoginBody;
import alb.framework.security.LoginUser;
import alb.framework.security.service.SysLoginService;
import alb.framework.security.service.SysPermissionService;
import alb.framework.security.service.TokenService;
import alb.framework.web.domain.AjaxResult;
import alb.project.system.service.ISysMenuService;

/**
 * 登录验证
 *
 */
@RestController
public class SysLoginController extends BaseController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    /**
     * 登录方法
     * 
     * @param loginBody 登陆信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    @Autowired
    private RedisCache redisCache;


    /**
     *  @Date: 2020/7/13 19:38
     *  @Description: 忘记密码
     */
    @PostMapping(value = "/forgetPassword")
    public AjaxResult forgetPassword(@RequestBody @Validated ForgetPasswordParamsVO params, BindingResult bindingResult){
        // 校验参数
        if (bindingResult.hasErrors()){
            return AjaxResult.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return toAjax(loginService.forgetPassword(params));
    }
}
