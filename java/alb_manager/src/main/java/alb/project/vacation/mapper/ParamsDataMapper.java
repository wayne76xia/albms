package alb.project.vacation.mapper;

import alb.project.vacation.domain.ParamsData;

import java.util.List;

/**
 * 参数设置Mapper接口
 *
 * @date 2020-07-16
 */
public interface ParamsDataMapper 
{
    /**
     * 查询参数设置
     * 
     * @param id 参数设置ID
     * @return 参数设置
     */
    ParamsData selectParamsDataById(Long id);

    /**
     * 查询参数设置列表
     * 
     * @param paramsData 参数设置
     * @return 参数设置集合
     */
    List<ParamsData> selectParamsDataList(ParamsData paramsData);

    /**
     * 新增参数设置
     * 
     * @param paramsData 参数设置
     * @return 结果
     */
    int insertParamsData(ParamsData paramsData);

    /**
     * 修改参数设置
     * 
     * @param paramsData 参数设置
     * @return 结果
     */
    int updateParamsData(ParamsData paramsData);

    /**
     * 删除参数设置
     * 
     * @param id 参数设置ID
     * @return 结果
     */
    int deleteParamsDataById(Long id);

    /**
     * 批量删除参数设置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteParamsDataByIds(Long[] ids);
}
