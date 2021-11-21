package alb.project.vacation.service.impl;

import alb.project.vacation.domain.Holiday;
import alb.project.vacation.domain.HolidayItem;
import alb.project.vacation.mapper.HolidayItemMapper;
import alb.project.vacation.mapper.HolidayMapper;
import alb.project.vacation.service.IHolidayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 假期表(Holiday)表服务实现类
 */
@Service("holidayService")
public class HolidayServiceImpl implements IHolidayService {

    @Resource
    private HolidayMapper holidayMapper;
    @Resource
    private HolidayItemMapper holidayItemMapper;

    /**
     * 查询单条数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    @Override
    public Holiday queryOne(Holiday holiday) {
        Holiday resultOne = this.holidayMapper.queryOne(holiday);
        HolidayItem params = HolidayItem.builder()
                .holidayId(holiday.getHolidayId())
                .build();
        resultOne.setItems(this.holidayItemMapper.queryAll(params));
        return resultOne;
    }

    /**
     * 查询多条数据
     *
     * @param holiday
     * @return
     */
    @Override
    public List<Holiday> queryAllByParams(Holiday holiday) {
        List<Holiday> resultAll = this.holidayMapper.queryAll(holiday);
        for (Holiday one : resultAll) {
            HolidayItem params = HolidayItem.builder()
                    .holidayId(one.getHolidayId())
                    .build();
            one.setItems(this.holidayItemMapper.queryAll(params));
        }
        return resultAll;
    }

    /**
     * 新增数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Holiday holiday) {
        return this.holidayMapper.insert(holiday);
    }

    /**
     * 新增事项数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @Override
    public int insertItem(HolidayItem holidayItem) {
        return this.holidayItemMapper.insert(holidayItem);
    }


    /**
     * 修改数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Holiday holiday) {
        return this.holidayMapper.update(holiday);
    }

    /**
     * 修改事项数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @Override
    public int updateItem(HolidayItem holidayItem) {
        return this.holidayItemMapper.update(holidayItem);
    }

    /**
     * 通过主键删除数据
     *
     * @param holidayId 主键
     * @return 是否成功
     */
    @Override
    public int deleteById(Long holidayId) {
        int count = this.holidayItemMapper.deleteById(holidayId);
        int countItem = this.holidayMapper.deleteById(holidayId);
        return count > 0 && countItem > 0 ? 1 : 0;
    }
}