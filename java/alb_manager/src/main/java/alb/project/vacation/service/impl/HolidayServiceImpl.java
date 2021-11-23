package alb.project.vacation.service.impl;

import alb.project.vacation.domain.Holiday;
import alb.project.vacation.domain.HolidayItem;
import alb.project.vacation.mapper.HolidayItemMapper;
import alb.project.vacation.mapper.HolidayMapper;
import alb.project.vacation.paramsVO.HolidayUserParamsVO;
import alb.project.vacation.resultVO.HolidayUserResultVO;
import alb.project.vacation.service.IHolidayService;
import cn.hutool.core.util.IdUtil;
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
     * @param holidayId 实例对象
     * @return 实例对象
     */
    @Override
    public Holiday queryOne(Long holidayId) {
        Holiday resultOne = this.holidayMapper.queryOne(holidayId);
        HolidayItem params = HolidayItem.builder()
                .holidayId(holidayId)
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
    public List<Holiday> queryAll(Holiday holiday) {
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
        // 雪花算法生成唯一通话记录号 单体服务 数据中心id和终端id都填1
        holiday.setHolidayId(IdUtil.getSnowflake(1, 1).nextId());
        return this.holidayMapper.insert(holiday);
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

    /**
     * 查询用户列表
     *
     * @param holidayUserParamsVO 实例对象
     * @return 实例对象
     */
    @Override
    public List<HolidayUserResultVO> selectUserList(HolidayUserParamsVO holidayUserParamsVO){
        return this.holidayMapper.selectUserList(holidayUserParamsVO);
    }
}
