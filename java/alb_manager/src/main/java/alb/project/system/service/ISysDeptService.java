package alb.project.system.service;

import alb.framework.web.domain.TreeSelect;
import alb.project.system.domain.SysDept;
import alb.project.system.domain.vo.ShopExcelVO;
import alb.project.system.domain.vo.SubsidiaryCompanyExcelVO;

import java.util.List;
import java.util.Map;

/**
 * 部门管理 服务层
 *
 */
public interface ISysDeptService
{
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    SysDept selectDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    int selectNormalChildrenDeptById(Long deptId);

    /**
     * 是否存在部门子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    boolean hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkDeptExistUser(Long deptId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    String checkDeptNameUnique(SysDept dept);

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    int updateDept(SysDept dept);

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);

    /**
     * 查询父id为此id的数量
     * @param deptId
     * @return Integer
     */
    Integer countTotalNumByParentId(Long deptId);

    /**
     * 查询总公司id（仅查询一个）
     * @return Long
     */
    Long selectHeadCompanyId();

    /**
     * 获取子公司列表
     * @param dept
     * @return
     */
    List<Map<String, Object>> selectSubsidiaryCompanyList(SysDept dept);

    /**
     * 获取门店列表
     * @param dept
     * @return
     */
    List<Map<String, Object>> selectShopList(SysDept dept);

    Integer countEmployeesNumByDeptId(Long deptId);

    String importSubsidiaryCompany(List<SubsidiaryCompanyExcelVO> list, boolean updateSupport, String operName);

    String importShop(List<ShopExcelVO> list, boolean updateSupport, String operName);

    /**
     * 获取所有的门店列表
     * @return
     */
    List<Map<String, Object>> getAllShopList();

    /**
     * 查询所有的分公司列表 只查询deptId与deptName
     * @return
     */
    List<SysDept> getAllSubsidiaryCompanyList();

    /**
     * 查询分公司下所有门店
     * @param deptId
     * @return
     */
    List<SysDept> selectShopListByParentId(Long deptId);

    /**
     * 根据shopIds查询门店
     * @param shopIds
     * @return
     */
    List<SysDept> selectDeptByIds(String shopIds);

    /**
     * 根据子公司id查询门店列表
     * @param ids
     * @return
     */
    List<SysDept> getShopListByCompanyIds(String ids);

    /**
     * 根据月份获取子公司数据
     * @param month
     * @return
     */
    Integer getTotalCompanyNumByMonth(String month);

    /**
     * 根据月份获取门店数量
     * @param month
     * @return
     */
    Integer getTotalShopNumByMonth(String month);

    /**
     * 门店数量
     * @return
     */
    Integer getTotalShopNum();

    /**
     * 子公司数量
     * @return
     */
    Integer getTotalCompanyNum();

    /**
     * 查询所有的子公司列表-划分数据权限
     * @return
     */
    List<SysDept> getSubsidiaryCompanyList(SysDept dept);
}
