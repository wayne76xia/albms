package alb.project.system.mapper;

import alb.project.system.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门管理 数据层
 *
 */
public interface SysDeptMapper {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    List<SysDept> selectDeptList(SysDept dept);

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
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    int selectNormalChildrenDeptById(Long deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int checkDeptExistUser(Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId, @Param("deptCode") String deptCode);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int insertDept(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int updateDept(SysDept dept);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);

    /**
     * 统计所有父id为此id的数量
     *
     * @param deptId
     */
    Integer countTotalNumByParentId(Long deptId);

    /**
     * 查询总公司id（仅查询一个）
     *
     * @return Long
     */
    Long selectHeadCompanyId();

    /**
     * 查询子公司列表
     *
     * @param dept
     * @return
     */
    List<Map<String, Object>> selectSubsidiaryCompanyList(SysDept dept);

    /**
     * 获取门店列表
     *
     * @param dept
     * @return
     */
    List<Map<String, Object>> selectShopList(SysDept dept);

    Integer countEmployeesNumByDeptId(Long deptId);

    SysDept selectDeptByCode(@Param("deptCode") String parentDeptCode);

    SysDept checkDeptCodeUnique(@Param("deptCode") String deptCode);

    /**
     * 获取所有的门店列表
     *
     * @return
     */
    List<Map<String, Object>> getAllShopList();

    /**
     * 查询所有的分公司列表 只查询deptId与deptName
     *
     * @return
     */
    List<SysDept> getAllSubsidiaryCompanyList();

    /**
     * 查询分公司下所有门店
     *
     * @param deptId
     * @return
     */
    List<SysDept> selectShopListByParentId(@Param("deptId") Long deptId);

    /**
     * 根据shopIds查询门店
     *
     * @param shopIds
     * @return
     */
    List<SysDept> selectDeptByIds(@Param("shopIds") String shopIds);

    /**
     * 根据子公司id查询门店列表
     *
     * @param ids
     * @return
     */
    List<SysDept> getShopListByCompanyIds(@Param("ids") String ids);

    /**
     * 根据月份获取子公司数据
     *
     * @param month
     * @return
     */
    Integer getTotalCompanyNumByMonth(@Param("month") String month);

    /**
     * 根据月份获取门店数量
     *
     * @param month
     * @return
     */
    Integer getTotalShopNumByMonth(@Param("month") String month);

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
