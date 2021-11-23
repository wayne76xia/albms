package alb.project.vacation.mapper;

import alb.project.vacation.domain.HolidayApproval;

import java.util.List;

/**
 * 假期事项表数据库访问层
 */
public interface HolidayApprovalMapper {

    /**
     * 通过实体查询是否存在下一节点
     *
     * @param holidayApproval 实例对象
     * @return 实例对象
     */
    int hasNext(HolidayApproval holidayApproval);

    /**
     * 通过ID查询单条数据
     *
     * @param holidayApprovalId 主键
     * @return 实例对象
     */
    HolidayApproval queryOne(Long holidayApprovalId);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param holidayApproval 实例对象
     * @return 对象列表
     */
    List<HolidayApproval> queryAll(HolidayApproval holidayApproval);

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
     * @param holidayId 主键
     * @return 影响行数
     */
    int deleteById(Long holidayId);

}
