package alb.project.vacation.service.impl;

import cn.hutool.core.util.IdUtil;
import alb.project.vacation.domain.HolidayApproval;
import alb.project.vacation.mapper.HolidayApprovalMapper;
import alb.project.vacation.mapper.HolidayTypeMapper;
import alb.project.vacation.service.IHolidayApprovalService;
import alb.project.vacation.service.IHolidayTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 假期审批表(HolidayApproval)表服务实现类
 */
@Service("holidayApprovalService")
public class HolidayApprovalServiceImpl implements IHolidayApprovalService {

    @Resource
    private HolidayApprovalMapper holidayApprovalMapper;

    /**
     * 查询单条数据
     *
     * @param holidayApprovalId 实例对象
     * @return 实例对象
     */
    @Override
    public HolidayApproval queryOne(Long holidayApprovalId) {
        return this.holidayApprovalMapper.queryOne(holidayApprovalId);
    }

    /**
     * 检测插入后，假期类型是否有环
     *
     * @param holidayApproval 实例对象
     * @return 实例对象
     */
    @Override
    public boolean checkRing(HolidayApproval holidayApproval) {
//        return this.holidayApprovalMapper.queryOne(holidayApproval);
        return false;
    }

    /**
     * 查询多条数据
     *
     * @param holidayApproval
     * @return
     */
    @Override
    public List<HolidayApproval> queryAll(HolidayApproval holidayApproval) {
        return this.holidayApprovalMapper.queryAll(holidayApproval);
    }

    /**
     * 新增数据
     *
     * @param holidayApproval 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(HolidayApproval holidayApproval) {
        // 雪花算法生成唯一通话记录号 单体服务 数据中心id和终端id都填1
        holidayApproval.setHolidayApprovalId(IdUtil.getSnowflake(1, 1).nextId());
        return this.holidayApprovalMapper.insert(holidayApproval);
    }

    /**
     * 通过主键删除数据
     *
     * @param holidayApprovalId 主键
     * @return 是否成功
     */
    @Override
    public int deleteById(Long holidayApprovalId) {
        return this.holidayApprovalMapper.deleteById(holidayApprovalId);
    }
}
