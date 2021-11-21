package alb.project.vacation.mapper;

import alb.project.vacation.domain.HolidayItem;

import java.util.List;

/**
 * 假期事项表数据库访问层
 */
public interface HolidayItemMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    HolidayItem queryOne(HolidayItem holidayItem);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param holidayItem 实例对象
     * @return 对象列表
     */
    List<HolidayItem> queryAll(HolidayItem holidayItem);

    /**
     * 新增数据
     *
     * @param holidayItem 实例对象
     * @return 影响行数
     */
    int insert(HolidayItem holidayItem);

    /**
     * 修改数据
     *
     * @param holidayItem 实例对象
     * @return 影响行数
     */
    int update(HolidayItem holidayItem);

    /**
     * 通过主键删除数据
     *
     * @param holidayId 主键
     * @return 影响行数
     */
    int deleteById(Long holidayId);

}