package alb.project.vacation.service.impl;

import alb.project.vacation.domain.ParamsData;
import alb.project.vacation.mapper.ParamsDataMapper;
import alb.project.vacation.service.IParamsDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 参数设置Service业务层处理
 *
 * @date 2020-07-16
 */
@Service
public class ParamsDataServiceImpl implements IParamsDataService {

    private final ParamsDataMapper paramsDataMapper;

    public ParamsDataServiceImpl(ParamsDataMapper paramsDataMapper) {
        this.paramsDataMapper = paramsDataMapper;
    }

    /**
     * 查询参数设置
     *
     * @param id 参数设置ID
     * @return 参数设置
     */
    @Override
    public ParamsData selectParamsDataById(Long id) {
        return paramsDataMapper.selectParamsDataById(id);
    }

    /**
     * 查询参数设置列表
     *
     * @param paramsData 参数设置
     * @return 参数设置
     */
    @Override
    public List<ParamsData> selectParamsDataList(ParamsData paramsData) {
        return paramsDataMapper.selectParamsDataList(paramsData);
    }

    /**
     * 新增参数设置
     *
     * @param paramsData 参数设置
     * @return 结果
     */
    @Override
    public int insertParamsData(ParamsData paramsData) {
        return paramsDataMapper.insertParamsData(paramsData);
    }

    /**
     * 修改参数设置
     *
     * @param paramsData 参数设置
     * @return 结果
     */
    @Override
    public int updateParamsData(ParamsData paramsData) {
        return paramsDataMapper.updateParamsData(paramsData);
    }

    /**
     * 批量删除参数设置
     *
     * @param ids 需要删除的参数设置ID
     * @return 结果
     */
    @Override
    public int deleteParamsDataByIds(Long[] ids) {
        return paramsDataMapper.deleteParamsDataByIds(ids);
    }

    /**
     * 删除参数设置信息
     *
     * @param id 参数设置ID
     * @return 结果
     */
    @Override
    public int deleteParamsDataById(Long id) {
        return paramsDataMapper.deleteParamsDataById(id);
    }
}
