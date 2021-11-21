package alb.project.system.mapper;

import alb.project.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 */
public interface SysUserMapper {
    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);

    /**
     * 通过用户工号查询用户
     * @param userCode
     * @return
     */
    SysUser selectUserByUserCode(String userCode);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    SysUser checkEmailUnique(String email);

    SysUser selectUserByPhone(@Param("phone") String phone);

    List<SysUser> selectDeptEmployeesList(@Param("deptId") Long deptId);

    /**
     * 根据月份获取员工数量
     *
     * @param month
     * @return
     */
    Integer getTotalNumByMonth(@Param("month") String month);

    /**
     * 根据月份验光师数量
     *
     * @param month
     * @return
     */
    Integer getTotalOptometristNumByMonth(@Param("month") String month);

    /**
     * 获取所有用户数量
     * @return
     */
    Integer getTotalNum();

    /**
     * 获取所有验光师数量
     * @return
     */
    Integer getTotalOptometristNum();

    /**
     * 获取最新的用户工号
     * @return
     */
    Integer getLastUserCode();
}
