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
import alb.project.system.domain.SysUser;
import alb.project.system.domain.vo.ShopExcelVO;
import alb.project.system.domain.vo.SubsidiaryCompanyExcelVO;
import alb.project.system.service.ISysDeptService;
import alb.project.system.service.ISysPostService;
import alb.project.system.service.ISysUserService;
import cn.hutool.core.convert.ConverterRegistry;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 部门信息
 *
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    private final ISysDeptService deptService;

    private final ISysUserService userService;

    private final ISysPostService postService;

    private final TokenService tokenService;

    public SysDeptController(ISysDeptService deptService, ISysUserService userService, ISysPostService postService, TokenService tokenService) {
        this.deptService = deptService;
        this.userService = userService;
        this.postService = postService;
        this.tokenService = tokenService;
    }

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        if (dept.getParentId() == 0) {
            if (deptService.selectHeadCompanyId() != null) {
                return AjaxResult.error("新增顶级部门'" + dept.getDeptName() + "'失败，顶级部门已存在");
            }
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }


    /**
     *  获取所有的门店列表
     * @return
     */
    @GetMapping(value = "/getAllShopList")
    public AjaxResult getAllShopList(){
        return AjaxResult.success(deptService.getAllShopList());
    }


    /**
     * 获取子公司列表
     *
     * @param dept
     * @return TableDataInfo
     */
    @PreAuthorize("@ss.hasPermi('system:dept:subsidiaryCompanyList')")
    @GetMapping(value = "/subsidiaryCompanyList")
    public TableDataInfo subsidiaryCompany(SysDept dept) {
        startPage();
        List<Map<String, Object>> list = deptService.selectSubsidiaryCompanyList(dept);
        return getDataTable(list);
    }

    /**
     * 下载子公司导入模板
     * @return
     */
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SubsidiaryCompanyExcelVO> util = new ExcelUtil<SubsidiaryCompanyExcelVO>(SubsidiaryCompanyExcelVO.class);
        return util.importTemplateExcel("子公司数据");
    }

    /**
     * 子公司导入
     *
     * @param file
     * @param updateSupport
     * @return
     */
    @Log(title = "部门管理-子公司导入", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:dept:subsidiaryCompanyListImport')")
    @PostMapping(value = "/subsidiaryCompanyListImport")
    public AjaxResult subsidiaryCompanyListImport(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SubsidiaryCompanyExcelVO> util = new ExcelUtil<SubsidiaryCompanyExcelVO>(SubsidiaryCompanyExcelVO.class);
        List<SubsidiaryCompanyExcelVO> list = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = deptService.importSubsidiaryCompany(list, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 导出
     *
     * @param dept
     * @return
     */
    @Log(title = "部门管理-子公司导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dept:subsidiaryCompanyListExport')")
    @GetMapping(value = "/subsidiaryCompanyListExport")
    public AjaxResult subsidiaryCompanyListExport(SysDept dept) {
        List<Map<String, Object>> list = deptService.selectSubsidiaryCompanyList(dept);
        List<SubsidiaryCompanyExcelVO> resultList = new LinkedList<>();
        for (Map<String, Object> map :
                list) {
            if (map != null) {
                ConverterRegistry registry = ConverterRegistry.getInstance();
                // 重组数据->导出
                resultList.add(SubsidiaryCompanyExcelVO.builder()
                        .deptCode(registry.convert(String.class, map.get("deptCode")))
                        .deptName(registry.convert(String.class, map.get("deptName")))
                        .leader(registry.convert(String.class, map.get("leader")))
                        .phone(registry.convert(String.class, map.get("leaderPhone")))
                        .email(registry.convert(String.class, map.get("email")))
                        .province(registry.convert(String.class, map.get("province")))
                        .city(registry.convert(String.class, map.get("city")))
                        .district(registry.convert(String.class, map.get("district")))
                        .address(registry.convert(String.class, map.get("address")))
                        .orderNum(registry.convert(Integer.class, map.get("orderNum")))
                        .createTime(registry.convert(Date.class,map.get("createTime")))
                        .build());
            }
        }
        ExcelUtil<SubsidiaryCompanyExcelVO> util = new ExcelUtil<SubsidiaryCompanyExcelVO>(SubsidiaryCompanyExcelVO.class);
        return util.exportExcel(resultList, "子公司数据");
    }


    /**
     * 子公司详细信息
     *
     * @param deptId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:subsidiaryCompanyInfo')")
    @GetMapping(value = "/subsidiaryCompanyInfo")
    public AjaxResult subsidiaryCompanyInfo(Long deptId) {
        if (deptId == null) {
            throw new CustomException("部门标识为空！");
        }
        // 封装map返回
        Map<String, Object> resultMap = new HashMap<>(15);
        // 根据部门id查询部门信息
        SysDept dept = deptService.selectDeptById(deptId);
        if (dept == null) {
            throw new CustomException("未获取到此部门数据！");
        }
        resultMap.put("deptId", dept.getDeptId() == null ? "" : dept.getDeptId());
        resultMap.put("deptCode", dept.getDeptCode() == null ? "" : dept.getDeptCode());
        resultMap.put("deptName", dept.getDeptName() == null ? "" : dept.getDeptName());
        resultMap.put("leader", dept.getLeader() == null ? "" : dept.getLeader());
        resultMap.put("email", dept.getEmail() == null ? "" : dept.getEmail());
        resultMap.put("phone", dept.getPhone() == null ? "" : dept.getPhone());
        resultMap.put("province", dept.getProvince() == null ? "" : dept.getProvince());
        resultMap.put("city", dept.getCity() == null ? "" : dept.getCity());
        resultMap.put("district", dept.getDistrict() == null ? "" : dept.getDistrict());
        resultMap.put("address", dept.getAddress() == null ? "" : dept.getAddress());
        resultMap.put("createDate", dept.getCreateTime() == null ? "" : DateUtils.formatDateToAppoint(dept.getCreateTime(), 3));

        // 计算子公司会员人数 准会员人数
        // 查询该子公司下面门店集合
        SysDept dept1 = new SysDept();
        dept1.setParentId(dept.getDeptId());
        List<Map<String, Object>> shopList = deptService.selectShopList(dept1);
        resultMap.put("shopList", shopList);
        // 查询此子公司下门店数量
        resultMap.put("totalNum", shopList == null ? 0 : shopList.size());
        return AjaxResult.success(resultMap);
    }

    /**
     * 获取所有的子公司列表  下拉列表用
     *
     * @return
     */
    @GetMapping(value = "/getAllSubsidiaryCompany")
    public AjaxResult getAllSubsidiaryCompany() {
        SysDept dept = new SysDept();
        dept.setType(1);
        dept.setParentId(0L);
        return AjaxResult.success(deptService.selectDeptList(dept));
    }

    /**
     * 获取门店列表
     *
     * @param dept
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:shop')")
    @GetMapping(value = "/shopList")
    public TableDataInfo shop(SysDept dept) {
        startPage();
        List<Map<String, Object>> list = deptService.selectShopList(dept);
        return getDataTable(list);
    }


    /**
     * 下载门店导入模板
     * @return
     */
    @GetMapping("/importShopTemplate")
    public AjaxResult importShopTemplate() {
        ExcelUtil<ShopExcelVO> util = new ExcelUtil<ShopExcelVO>(ShopExcelVO.class);
        return util.importTemplateExcel("门店数据");
    }

    /**
     * 门店导入
     *
     * @param file
     * @param updateSupport
     * @return
     */
    @Log(title = "部门管理-门店导入", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:dept:shopListImport')")
    @PostMapping(value = "/shopListImport")
    public AjaxResult shopListImport(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ShopExcelVO> util = new ExcelUtil<ShopExcelVO>(ShopExcelVO.class);
        List<ShopExcelVO> list = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = deptService.importShop(list, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 导出
     *
     * @param dept
     * @return
     */
    @Log(title = "部门管理-门店导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dept:shopListExport')")
    @GetMapping(value = "/shopListExport")
    public AjaxResult shopListExport(SysDept dept) {
        List<Map<String, Object>> list = deptService.selectShopList(dept);
        List<ShopExcelVO> resultList = new LinkedList<>();
        for (Map<String, Object> map :
                list) {
            if (map != null) {
                ConverterRegistry registry = ConverterRegistry.getInstance();
                // 查询父级信息
                SysDept info = deptService.selectDeptById(registry.convert(Long.class,map.get("parentId")));
                // 重组数据->导出
                resultList.add(ShopExcelVO.builder()
                        .parentDeptCode(info == null ? "" : info.getDeptCode() == null ? "" : info.getDeptCode())
                        .parentDeptName(info == null ? "" : info.getDeptName() == null ? "" : info.getDeptName())
                        .deptCode(registry.convert(String.class, map.get("deptCode")))
                        .deptName(registry.convert(String.class, map.get("deptName")))
                        .deptType(registry.convert(Integer.class,map.get("deptType")))
                        .leader(registry.convert(String.class, map.get("leader")))
                        .phone(registry.convert(String.class, map.get("leaderPhone")))
                        .email(registry.convert(String.class, map.get("email")))
                        .province(registry.convert(String.class, map.get("province")))
                        .city(registry.convert(String.class, map.get("city")))
                        .district(registry.convert(String.class, map.get("district")))
                        .address(registry.convert(String.class, map.get("address")))
                        .orderNum(registry.convert(Integer.class, map.get("orderNum")))
                        .createTime(registry.convert(Date.class,map.get("createTime")))
                        .build());
            }
        }
        ExcelUtil<ShopExcelVO> util = new ExcelUtil<ShopExcelVO>(ShopExcelVO.class);
        return util.exportExcel(resultList, "门店数据");
    }


    /**
     * 查询门店信息
     *
     * @param deptId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:shopInfo')")
    @GetMapping(value = "/shopInfo")
    public AjaxResult shopInfo(Long deptId) {
        if (deptId == null) {
            throw new CustomException("部门标识为空！");
        }
        // 封装map返回
        Map<String, Object> resultMap = new HashMap<>(14);
        // 根据部门id查询部门信息
        SysDept dept = deptService.selectDeptById(deptId);
        if (dept == null) {
            throw new CustomException("未获取到此部门数据！");
        }
        SysDept parentDept = deptService.selectDeptById(dept.getParentId());
        resultMap.put("deptId", dept.getDeptId() == null ? "" : dept.getDeptId());
        resultMap.put("deptCode", dept.getDeptCode() == null ? "" : dept.getDeptCode());
        resultMap.put("deptName", dept.getDeptName() == null ? "" : dept.getDeptName());
        resultMap.put("deptPhone", dept.getDeptPhone() == null ? "" : dept.getDeptPhone());
        resultMap.put("parentDeptName", parentDept == null ? "" : parentDept.getDeptName() == null ? "" : parentDept.getDeptName());
        resultMap.put("leader", dept.getLeader() == null ? "" : dept.getLeader());
        resultMap.put("email", dept.getEmail() == null ? "" : dept.getEmail());
        resultMap.put("phone", dept.getPhone() == null ? "" : dept.getPhone());
        resultMap.put("province", dept.getProvince() == null ? "" : dept.getProvince());
        resultMap.put("city", dept.getCity() == null ? "" : dept.getCity());
        resultMap.put("district", dept.getDistrict() == null ? "" : dept.getDistrict());
        resultMap.put("address", dept.getAddress() == null ? "" : dept.getAddress());
        resultMap.put("deptType", dept.getDeptType() == null ? 0 : dept.getDeptType());
        resultMap.put("orderNum", dept.getOrderNum() == null ? 0 : dept.getOrderNum());
        resultMap.put("createDate", dept.getCreateTime() == null ? "" : DateUtils.formatDateToAppoint(dept.getCreateTime(), 3));
        // 查询此门店下员工数量
        Integer employeesNum = deptService.countEmployeesNumByDeptId(deptId);
        resultMap.put("employeesNum", employeesNum);

        return AjaxResult.success(resultMap);
    }

    /**
     * 获取部门下员工列表
     *
     * @param deptId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:deptEmployeesList')")
    @GetMapping(value = "/deptEmployeesList")
    public AjaxResult deptEmployeesList(Long deptId) {
        if (deptId == null) {
            throw new CustomException("部门标识为空！");
        }
        List<SysUser> list = userService.selectDeptEmployeesList(deptId);
        for (SysUser su :
                list) {
            List<String> postNameList = postService.selectPostNameByUserId(su.getUserId());
            su.setPostName(StringUtils.join(postNameList, ","));
        }
        return AjaxResult.success(list);
    }

}
