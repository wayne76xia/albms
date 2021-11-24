package alb.project.vacation.service.impl;

import alb.project.vacation.domain.HolidayItem;
import alb.project.vacation.mapper.HolidayItemMapper;
import alb.project.vacation.service.IHolidayItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 假期事项表(HolidayItem)表服务实现类
 */
@Service("holidayItemService")
public class HolidayItemServiceImpl implements IHolidayItemService {

    @Resource
    private HolidayItemMapper holidayItemMapper;

    /**
     * 查询单条数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @Override
    public HolidayItem queryOne(HolidayItem holidayItem) {
        return this.holidayItemMapper.queryOne(holidayItem);
    }

    /**
     * 查询多条数据
     *
     * @param holidayItem
     * @return
     */
    @Override
    public List<HolidayItem> queryAll(HolidayItem holidayItem) {
        return this.holidayItemMapper.queryAll(holidayItem);
    }

    /**
     * 新增数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(HolidayItem holidayItem) {
        // 雪花算法生成唯一通话记录号 单体服务 数据中心id和终端id都填1
        return this.holidayItemMapper.insert(holidayItem);
    }

    /**
     * 修改数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @Override
    public int update(HolidayItem holidayItem) {
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
        return this.holidayItemMapper.deleteById(holidayId);
    }
}
