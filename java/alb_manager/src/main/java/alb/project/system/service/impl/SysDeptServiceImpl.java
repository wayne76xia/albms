package alb.project.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import alb.common.constant.UserConstants;
import alb.common.exception.CustomException;
import alb.framework.aspectj.lang.annotation.DataScope;
import alb.framework.web.domain.TreeSelect;
import alb.project.system.domain.SysDept;
import alb.project.system.domain.vo.ShopExcelVO;
import alb.project.system.domain.vo.SubsidiaryCompanyExcelVO;
import alb.project.system.mapper.SysDeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import alb.common.utils.StringUtils;
import alb.project.system.service.ISysDeptService;

/**
 * 部门管理 服务实现
 *
 */
@Service
@Slf4j
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDept dept = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId) {
        return deptMapper.selectDeptListByRoleId(roleId);
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId(),dept.getDeptCode());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<SysDept> it = childList.iterator();
                while (it.hasNext()) {
                    SysDept n = it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }


    @Override
    public Integer countTotalNumByParentId(Long deptId) {
        return deptMapper.countTotalNumByParentId(deptId);
    }

    @Override
    public Long selectHeadCompanyId() {
        return deptMapper.selectHeadCompanyId();
    }

    /**
     *  查询所有子公司列表   划分数据权限
     * @param dept
     * @return
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<Map<String, Object>> selectSubsidiaryCompanyList(SysDept dept) {
        return deptMapper.selectSubsidiaryCompanyList(dept);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<Map<String, Object>> selectShopList(SysDept dept) {
        return deptMapper.selectShopList(dept);
    }

    @Override
    public Integer countEmployeesNumByDeptId(Long deptId) {
        return deptMapper.countEmployeesNumByDeptId(deptId);
    }

    /**
     * 导入子公司
     * @param list
     * @param isUpdateSupport
     * @param operName
     * @return
     */
    @Override
    public String importSubsidiaryCompany(List<SubsidiaryCompanyExcelVO> list, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new CustomException("导入子公司数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SubsidiaryCompanyExcelVO vo : list)
        {
            try
            {
                SysDept info = deptMapper.selectDeptById(100L);
                // 如果父节点不为正常状态,则不允许新增子节点
                if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
                    throw new CustomException(info.getDeptName() + " 停用，不允许新增");
                }
                // 验证是否存在这个部门
                SysDept dept = deptMapper.checkDeptCodeUnique(vo.getDeptCode());
                if (StringUtils.isNull(dept))
                {
                    SysDept insertDept = new SysDept();
                    insertDept.setDeptCode(vo.getDeptCode());
                    insertDept.setParentId(info.getDeptId());
                    insertDept.setAncestors(info.getAncestors() + "," + info.getDeptId());
                    insertDept.setDeptName(vo.getDeptName());
                    insertDept.setOrderNum(String.valueOf(vo.getOrderNum()));
                    insertDept.setLeader(vo.getLeader());
                    insertDept.setPhone(vo.getPhone());
                    insertDept.setEmail(vo.getEmail());
                    insertDept.setProvince(vo.getProvince());
                    insertDept.setCity(vo.getCity());
                    insertDept.setDistrict(vo.getDistrict());
                    insertDept.setAddress(vo.getAddress());
                    insertDept.setDeptType(0);
                    insertDept.setType(1);
                    insertDept.setStatus("0");
                    insertDept.setDelFlag("0");
                    insertDept.setCreateBy(operName);
                    deptMapper.insertDept(insertDept);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、子公司 ").append(vo.getDeptName()).append(" 导入成功");
                }
                else if (isUpdateSupport)
                {
                    dept.setParentId(info.getDeptId());
                    dept.setAncestors(info.getAncestors() + "," + info.getDeptId());
                    dept.setDeptCode(vo.getDeptCode());
                    dept.setDeptName(vo.getDeptName());
                    dept.setOrderNum(String.valueOf(vo.getOrderNum()));
                    dept.setLeader(vo.getLeader());
                    dept.setPhone(vo.getPhone());
                    dept.setEmail(vo.getEmail());
                    dept.setProvince(vo.getProvince());
                    dept.setCity(vo.getCity());
                    dept.setDistrict(vo.getDistrict());
                    dept.setAddress(vo.getAddress());
                    dept.setUpdateBy(operName);
                    deptMapper.updateDept(dept);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、子公司 ").append(vo.getDeptName()).append(" 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、子公司 ").append(vo.getDeptCode()).append(" 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、子公司 " + vo.getDeptName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


    /**
     * 导入门店
     * @param list
     * @param isUpdateSupport
     * @param operName
     * @return
     */
    @Override
    public String importShop(List<ShopExcelVO> list, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new CustomException("导入门店数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ShopExcelVO vo : list)
        {
            try
            {
                // 根据门店所属子公司编码查询子公司信息
                SysDept info = deptMapper.selectDeptByCode(vo.getParentDeptCode());

                // 如果父节点不为正常状态,则不允许新增子节点
                if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
                    throw new CustomException(info.getDeptName() + "子公司停用，不允许新增");
                }
                // 验证是否存在这个门店
//                SysDept dept = deptMapper.checkDeptNameUnique(vo.getDeptName(),info.getDeptId(),vo.getDeptCode());
                SysDept dept = deptMapper.checkDeptCodeUnique(vo.getDeptCode());
                if (StringUtils.isNull(dept))
                {
                    SysDept insertDept = new SysDept();
                    insertDept.setDeptCode(vo.getDeptCode());
                    insertDept.setParentId(info.getDeptId());
                    insertDept.setAncestors(info.getAncestors() + "," + info.getDeptId());
                    insertDept.setDeptName(vo.getDeptName());
                    insertDept.setOrderNum(String.valueOf(vo.getOrderNum()));
                    insertDept.setDeptType(vo.getDeptType());
                    insertDept.setLeader(vo.getLeader());
                    insertDept.setPhone(vo.getPhone());
                    insertDept.setEmail(vo.getEmail());
                    insertDept.setProvince(vo.getProvince());
                    insertDept.setCity(vo.getCity());
                    insertDept.setDistrict(vo.getDistrict());
                    insertDept.setAddress(vo.getAddress());
                    insertDept.setType(2);
                    insertDept.setStatus("0");
                    insertDept.setDelFlag("0");
                    insertDept.setCreateBy(operName);
                    deptMapper.insertDept(insertDept);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、门店 ").append(vo.getDeptName()).append(" 导入成功");
                }
                else if (isUpdateSupport)
                {
                    dept.setDeptCode(vo.getDeptCode());
                    dept.setParentId(info.getDeptId());
                    dept.setAncestors(info.getAncestors() + "," + info.getDeptId());
                    dept.setDeptName(vo.getDeptName());
                    dept.setOrderNum(String.valueOf(vo.getOrderNum()));
                    dept.setDeptType(vo.getDeptType());
                    dept.setLeader(vo.getLeader());
                    dept.setPhone(vo.getPhone());
                    dept.setEmail(vo.getEmail());
                    dept.setProvince(vo.getProvince());
                    dept.setCity(vo.getCity());
                    dept.setDistrict(vo.getDistrict());
                    dept.setAddress(vo.getAddress());
                    dept.setUpdateBy(operName);
                    deptMapper.updateDept(dept);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、门店 ").append(vo.getDeptName()).append(" 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、门店 ").append(vo.getDeptCode()).append(" 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、门店 " + vo.getDeptName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 获取所有的门店列表
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllShopList() {
        return deptMapper.getAllShopList();
    }

    /**
     * 查询所有的分公司列表 只查询deptId与deptName
     * @return
     */
    @Override
    public List<SysDept> getAllSubsidiaryCompanyList() {
        return deptMapper.getAllSubsidiaryCompanyList();
    }

    /**
     * 查询分公司下所有门店
     * @param deptId
     * @return
     */
    @Override
    public List<SysDept> selectShopListByParentId(Long deptId) {
        return deptMapper.selectShopListByParentId(deptId);
    }

    /**
     * 根据shopIds查询门店
     * @param shopIds
     * @return
     */
    @Override
    public List<SysDept> selectDeptByIds(String shopIds) {
        return deptMapper.selectDeptByIds(shopIds);
    }

    /**
     * 根据子公司id查询门店列表
     * @param ids
     * @return
     */
    @Override
    public List<SysDept> getShopListByCompanyIds(String ids) {
        return deptMapper.getShopListByCompanyIds(ids);
    }

    /**
     * 根据月份获取子公司数据
     * @param month
     * @return
     */
    @Override
    public Integer getTotalCompanyNumByMonth(String month) {
        return deptMapper.getTotalCompanyNumByMonth(month);
    }

    /**
     * 根据月份获取门店数量
     * @param month
     * @return
     */
    @Override
    public Integer getTotalShopNumByMonth(String month) {
        return deptMapper.getTotalShopNumByMonth(month);
    }

    /**
     * 门店数量
     * @return
     */
    @Override
    public Integer getTotalShopNum() {
        return deptMapper.getTotalShopNum();
    }

    /**
     * 子公司数量
     * @return
     */
    @Override
    public Integer getTotalCompanyNum() {
        return deptMapper.getTotalCompanyNum();
    }

    /**
     * 查询所有的子公司列表-划分数据权限
     * @return
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> getSubsidiaryCompanyList(SysDept dept) {
        return deptMapper.getSubsidiaryCompanyList(dept);
    }
}
