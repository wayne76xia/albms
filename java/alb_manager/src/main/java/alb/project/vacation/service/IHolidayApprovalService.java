package alb.project.vacation.service;

import alb.project.system.domain.SysRole;
import alb.project.vacation.domain.HolidayApproval;

import java.util.List;

/**
 * 假期表服务接口
 */
public interface IHolidayApprovalService {

    /**
     * 通过实体查询是否存在下一节点
     *
     * @param holidayApproval 实例对象
     * @return 实例对象
     */
    boolean hasNextApproved(HolidayApproval holidayApproval);

    /**
     * 查询单条数据
     *
     * @param holidayApprovalId 主键
     * @return 实例对象
     */
    HolidayApproval queryOne(Long holidayApprovalId);

    /**
     * 检测插入后，假期类型是否有环
     *
     * @param holidayApproval 实例对象
     * @return 实例对象
     */
    boolean hasRing(HolidayApproval holidayApproval);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param holidayApproval 实例对象
     * @return 对象列表
     */
    List<HolidayApproval> queryAll(HolidayApproval holidayApproval);


    /**
     * 查询角色列表
     *
     * @return 对象列表
     */
    List<SysRole> selectRoleList();

    /**
     * 新增数据
     *
     * @param holidayApproval 实例对象
     * @return 影响行数
     */
    int insert(HolidayApproval holidayApproval);

    /**
     * 通过主键删除数据
     *
     * @param holidayApprovalId 主键
     * @return 是否成功
     */
    int deleteById(Long holidayApprovalId);

}
