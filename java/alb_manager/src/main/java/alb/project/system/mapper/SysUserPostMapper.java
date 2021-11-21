package alb.project.system.mapper;

import java.util.List;

import alb.project.system.domain.SysUserPost;
import org.apache.ibatis.annotations.Param;

/**
 * 用户与岗位关联表 数据层
 *
 */
public interface SysUserPostMapper {
    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserPostByUserId(Long userId);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int countUserPostById(Long postId);

    /**
     * 批量删除用户和岗位关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserPost(Long[] ids);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    int batchUserPost(List<SysUserPost> userPostList);

    /**
     * 根据用户id和部门id查询是否存在这条记录
     *
     * @param userId
     * @param postId
     * @return
     */
    SysUserPost checkUserIsThisPost(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 随机获取一名话务员id
     *
     * @param postId
     * @return
     */
    Long getRandomUserId(@Param("postId") Long postId);
}
