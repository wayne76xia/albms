package alb.project.vacation.service;

import alb.project.vacation.domain.Holiday;
import alb.project.vacation.domain.HolidayItem;

import java.util.List;

/**
 * 假期表服务接口
 */
public interface IHolidayService {

    /**
     * 查询单条数据
     *
     * @param holidayId 主键
     * @return 实例对象
     */
    Holiday queryOne(Long holidayId);

    /**
     * 查询多条数据
     *
     * @param holiday
     * @return
     */
    List<Holiday> queryAll(Holiday holiday);

    /**
     * 新增数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    int insert(Holiday holiday);

    /**
     * 修改数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    int update(Holiday holiday);

    /**
     * 通过主键删除数据
     *
     * @param holidayId 主键
     * @return 是否成功
     */
    int deleteById(Long holidayId);

}
