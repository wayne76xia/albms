package alb.project.system.controller;

import alb.common.constant.UserConstants;
import alb.common.exception.CustomException;
import alb.common.utils.DateUtils;
import alb.common.utils.SecurityUtils;
import alb.common.utils.ServletUtils;
import alb.common.utils.StringUtils;
import alb.common.utils.poi.ExcelUtil;
import alb.framework.aspectj.lang.annotation.Log;
import alb.framework.aspectj.lang.enums.BusinessType;
import alb.framework.security.LoginUser;
import alb.framework.security.service.TokenService;
import alb.framework.web.controller.BaseController;
import alb.framework.web.domain.AjaxResult;
import alb.framework.web.page.TableDataInfo;
import alb.project.system.domain.SysDept;
import alb.project.system.domain.SysRole;
import alb.project.system.domain.SysUser;
import alb.project.system.service.ISysDeptService;
import alb.project.system.service.ISysPostService;
import alb.project.system.service.ISysRoleService;
import alb.project.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    private final ISysUserService userService;

    private final ISysRoleService roleService;

    private final ISysPostService postService;

    private final TokenService tokenService;

    private final ISysDeptService deptService;

    public SysUserController(ISysUserService userService, ISysRoleService roleService, ISysPostService postService, TokenService tokenService, ISysDeptService deptService) {
        this.userService = userService;
        this.roleService = roleService;
        this.postService = postService;
        this.tokenService = tokenService;
        this.deptService = deptService;
    }

//    /**
//     * 统计数据
//     * @return
//     */
//    @RequestMapping(value = "/topData",method = RequestMethod.GET)
//    public AjaxResult topData(){
//        List<String> monthList = DateUtils.getInitMonthMapWithZero();
//        String thisMonth = DateUtil.format(new Date(), "yyyy-MM");
//        String lastMonth = DateUtil.format(DateUtil.lastMonth(), "yyyy-MM");
//        // 总公司员工数量
//        List<Integer> totalUserNumList = new ArrayList<>();
//        // 子公司数量
//        List<Integer> totalCompanyNumList = new ArrayList<>();
//        // 门店数量
//        List<Integer> totalShopNumList = new ArrayList<>();
//        // 验光师数量
//        List<Integer> totalOptometristNumList = new ArrayList<>();
//        for (String month : monthList) {
//            // 根据月份获取员工数量
//            totalUserNumList.add(userService.getTotalNumByMonth(month));
//            // 根据月份获取子公司数据
//            totalCompanyNumList.add(deptService.getTotalCompanyNumByMonth(month));
//            // 根据月份获取门店数量
//            totalShopNumList.add(deptService.getTotalShopNumByMonth(month));
//            // 根据月份验光师数量
//            totalOptometristNumList.add(userService.getTotalOptometristNumByMonth(month));
//        }
//
//
//        // 总公司员工数量
//        Integer totalUserNum = userService.getTotalNum();
//        // 本月总公司人数
//        Integer thisMonthTotalUserNum = userService.getTotalNumByMonth(thisMonth);
//        // 上月总公司人数
//        Integer lastMonthTotalUserNum = userService.getTotalNumByMonth(lastMonth);
//        String totalUserNumRatio = null;
//        if (lastMonthTotalUserNum > 0) {
//            totalUserNumRatio = NumberUtil.div(NumberUtil.sub(thisMonthTotalUserNum, lastMonthTotalUserNum), lastMonthTotalUserNum,2) + "%";
//        } else if (NumberUtil.compare(thisMonthTotalUserNum, lastMonthTotalUserNum) == 0) {
//            totalUserNumRatio = "+0%";
//        } else {
//            totalUserNumRatio = "+100%";
//        }
//
//        // 子公司数量
//        Integer totalCompanyNum = deptService.getTotalCompanyNum();
//        // 本月子公司数量
//        Integer thisMonthTotalCompanyNum = deptService.getTotalShopNumByMonth(thisMonth);
//        // 上月子公司数量
//        Integer lastMonthTotalCompanyNum = deptService.getTotalShopNumByMonth(lastMonth);
//        String totalCompanyNumRatio = null;
//        if (lastMonthTotalCompanyNum > 0) {
//            totalCompanyNumRatio = NumberUtil.div(NumberUtil.sub(thisMonthTotalCompanyNum, lastMonthTotalCompanyNum), lastMonthTotalCompanyNum,2) + "%";
//        } else if (NumberUtil.compare(thisMonthTotalCompanyNum, lastMonthTotalCompanyNum) == 0) {
//            totalCompanyNumRatio = "+0%";
//        } else {
//            totalCompanyNumRatio = "+100%";
//        }
//
//        // 门店数量
//        Integer totalShopNum = deptService.getTotalShopNum();
//        // 本月门店数量
//        Integer thisMonthTotalShopNum = deptService.getTotalShopNumByMonth(thisMonth);
//        // 上月门店数量
//        Integer lastMonthTotalShopNum = deptService.getTotalShopNumByMonth(lastMonth);
//        String totalShopNumRatio = null;
//        if (lastMonthTotalShopNum > 0) {
//            totalShopNumRatio = NumberUtil.div(NumberUtil.sub(thisMonthTotalShopNum, lastMonthTotalShopNum), lastMonthTotalShopNum,2) + "%";
//        } else if (NumberUtil.compare(thisMonthTotalShopNum, lastMonthTotalShopNum) == 0) {
//            totalShopNumRatio = "+0%";
//        } else {
//            totalShopNumRatio = "+100%";
//        }
//
//        // 验光师数量
//        Integer totalOptometristNum = userService.getTotalOptometristNum();
//        // 本月验光师数量
//        Integer thisMonthTotalOptometristNum = userService.getTotalOptometristNumByMonth(thisMonth);
//        // 上月验光师数量
//        Integer lastMonthTotalOptometristNum = userService.getTotalOptometristNumByMonth(lastMonth);
//        String totalOptometristNumRatio = null;
//        if (lastMonthTotalOptometristNum > 0) {
//            totalOptometristNumRatio = NumberUtil.div(NumberUtil.sub(thisMonthTotalOptometristNum, lastMonthTotalOptometristNum), lastMonthTotalOptometristNum,2) + "%";
//        } else if (NumberUtil.compare(thisMonthTotalOptometristNum, lastMonthTotalOptometristNum) == 0) {
//            totalOptometristNumRatio = "+0%";
//        } else {
//            totalOptometristNumRatio = "+100%";
//        }
//
//        Map<String, Object> resultMap = new HashMap<>();
//        // 日期
//        resultMap.put("monthList",monthList);
//        // 总公司员工数量
//        resultMap.put("totalUserNum", totalUserNum);
//        // 总公司员工数量同上月对比
//        resultMap.put("totalUserNumRatio", totalUserNumRatio);
//        // 总公司员工数量曲线图
//        resultMap.put("totalUserNumList", totalUserNumList);
//        // 门店数量
//        resultMap.put("totalShopNum", totalShopNum);
//        // 门店数量同上月对比
//        resultMap.put("totalShopNumRatio", totalShopNumRatio);
//        // 门店数量曲线图
//        resultMap.put("totalShopNumList", totalShopNumList);
//        // 子公司数量
//        resultMap.put("totalCompanyNum", totalCompanyNum);
//        // 子公司数量同上月对比
//        resultMap.put("totalCompanyNumRatio", totalCompanyNumRatio);
//        // 子公司数量曲线图
//        resultMap.put("totalCompanyNumList", totalCompanyNumList);
//        // 验光师数量
//        resultMap.put("totalOptometristNum", totalOptometristNum);
//        // 验光师数量同上月对比
//        resultMap.put("totalOptometristNumRatio", totalOptometristNumRatio);
//        // 验光师数量曲线图
//        resultMap.put("totalOptometristNumList",totalOptometristNumList);
//        return AjaxResult.success(resultMap);
//    }

    /**
     * 获取用户列表
     */
    @GetMapping("/allList")
    public AjaxResult allList(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        return AjaxResult.success(list);
    }

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        for (SysUser su :
                list) {
            List<String> postNameList = postService.selectPostNameByUserId(su.getUserId());
            su.setPostName(StringUtils.join(postNameList, ","));
        }
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        for (SysUser su :
                list) {
            List<String> postNameList = postService.selectPostNameByUserId(su.getUserId());
            su.setPostName(StringUtils.join(postNameList, ","));
        }
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserCode(userService.getLastUserCode());
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }


    /**
     * 查询用户详情
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/userInfo")
    public AjaxResult userInfo(Long userId) {
        if (userId == null) {
            throw new CustomException("用户标识为空！");
        }
        Map<String, Object> resultMap = new HashMap<>(12);
        // 获取用户信息
        SysUser user = userService.selectUserById(userId);
        // 获取用户职位信息
        List<String> postNameList = postService.selectPostNameByUserId(userId);
        if (user != null) {
            // 查询部门信息
            SysDept dept = deptService.selectDeptById(user.getDeptId());
            // 用户信息
            resultMap.put("nickName", user.getNickName() == null ? "" : user.getNickName());
            resultMap.put("userCode", user.getUserCode() == null ? "" : user.getUserCode());
            resultMap.put("deptName", dept == null ? "" : dept.getDeptName() == null ? "" : dept.getDeptName());
            resultMap.put("postName", StringUtils.join(postNameList, ","));
            resultMap.put("status", user.getStatus() == null ? "" : "0".equals(user.getStatus()) ? "在职" : "离职");
            resultMap.put("sex", user.getSex() == null ? "未知" : "1".equals(user.getSex()) ? "男" : "2".equals(user.getSex()) ? "女" : "未知");
            resultMap.put("phoneNumber", user.getPhonenumber() == null ? "" : user.getPhonenumber());
            StringBuilder address = new StringBuilder();
            if (user.getProvince()!= null){
                address.append(user.getProvince()).append(" ");
            }if (user.getCity() != null){
                address.append(user.getCity()).append(" ");
            }if (user.getDistrict() != null){
                address.append(user.getDistrict()).append(" ");
            }
            resultMap.put("address", address.toString());
        }
        // 用户详情数据统计
        // 入职时间
        resultMap.put("hireDate", user == null ? "" : user.getCreateTime() == null ? "" : DateUtils.formatDateToAppoint(user.getCreateTime(), 3));
        // 入职时长
        resultMap.put("hireTotal", DateUtils.dayComparePrecise(user == null ? new Date() : user.getCreateTime() == null ? new Date() : user.getCreateTime(), new Date()));
        return AjaxResult.success(resultMap);
    }


}