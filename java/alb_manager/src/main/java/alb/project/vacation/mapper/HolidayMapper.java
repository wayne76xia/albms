package alb.project.vacation.mapper;

import alb.project.vacation.domain.Holiday;

import java.util.List;

/**
 * 假期表数据库访问层
 */
public interface HolidayMapper {
    /**
     * 通过ID查询单条数据
     *
     * @param holidayId 主键
     * @return 实例对象
     */
    int queryHolidayIsExists(Long holidayId);

    /**
     * 通过ID查询单条数据
     *
     * @param holidayId 主键
     * @return 实例对象
     */
    Holiday queryOne(Long holidayId);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param holiday 实例对象
     * @return 对象列表
     */
    List<Holiday> queryAll(Holiday holiday);

    /**
     * 新增数据
     *
     * @param holiday 实例对象
     * @return 影响行数
     */
    int insert(Holiday holiday);

    /**
     * 修改数据
     *
     * @param holiday 实例对象
     * @return 影响行数
     */
    int update(Holiday holiday);

    /**
     * 通过主键删除数据
     *
     * @param holidayId 主键
     * @return 影响行数
     */
    int deleteById(Long holidayId);

}