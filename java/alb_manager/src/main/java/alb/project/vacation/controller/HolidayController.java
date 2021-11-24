package alb.project.vacation.controller;

import alb.common.utils.ServletUtils;
import alb.common.utils.StringUtils;
import alb.framework.aspectj.lang.annotation.Log;
import alb.framework.aspectj.lang.enums.BusinessType;
import alb.framework.security.LoginUser;
import alb.framework.security.service.TokenService;
import alb.framework.web.controller.BaseController;
import alb.framework.web.domain.AjaxResult;
import alb.framework.web.page.TableDataInfo;
import alb.project.system.domain.SysRole;
import alb.project.system.service.ISysUserService;
import alb.project.vacation.domain.Holiday;
import alb.project.vacation.domain.HolidayApproval;
import alb.project.vacation.domain.HolidayItem;
import alb.project.vacation.paramsVO.HolidayUserParamsVO;
import alb.project.vacation.resultVO.HolidayUserResultVO;
import alb.project.vacation.service.IHolidayApprovalService;
import alb.project.vacation.service.IHolidayItemService;
import alb.project.vacation.service.IHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HaoHao
 * Created on 2021/1/26.
 */

@RestController
@RequestMapping("/vacation/holiday")
public class HolidayController extends BaseController {

    @Resource // @Resource（这个注解属于J2EE的），默认按照名称进行装配
    private IHolidayService holidayService;
    @Resource
    private IHolidayItemService holidayItemService;
    @Resource
    private IHolidayApprovalService holidayApprovalService;
    @Resource
    private ISysUserService userService;

    @Autowired // @Autowired（这个注解是属于spring的），默认按类型装配
    private TokenService tokenService;

    /**
     * 查询单条数据
     *
     * @param holidayId 主键
     * @return 实例对象
     */
    @GetMapping("/{holidayId}")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:query')")
    public AjaxResult queryHoliday(@PathVariable("holidayId") Long holidayId) {
        Holiday result = this.holidayService.queryOne(holidayId);
        return result != null ? AjaxResult.success(result) : AjaxResult.error();
    }

    /**
     * 是否有下一节点
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    @GetMapping("/next")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:query')")
    public AjaxResult hasNext(@RequestBody Holiday holiday) {
        // 默认取第一个角色
        Long roleId = this.userService.selectUserById(holiday.getProposerId()).getRoles().get(0);
        HolidayApproval params = HolidayApproval.builder()
                .holidayTypeId(holiday.getHolidayTypeId())
                .roleId(roleId)
                .currentApprovedIndex(holiday.getCurrentApprovedIndex())
                .build();
        return this.holidayApprovalService.hasNextApproved(params) ? AjaxResult.success("存在下一节点") : AjaxResult.error("不存在下一节点");
    }

    /**
     * 查询多条数据
     *
     * @param holiday
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:list')")
    public TableDataInfo queryList(Holiday holiday) {
        startPage();
        List<Holiday> list = this.holidayService.queryAll(holiday);
        return getDataTable(list);
    }

    /**
     * 查询多条数据
     *
     * @param holiday
     * @return
     */
    @GetMapping("/user/list")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:list')")
    public AjaxResult queryUserList(Holiday holiday) {
        if (holiday.getHolidayId() != null) { // 审批阶段查询
            List<HolidayUserResultVO> result = new ArrayList<>();
            List<SysRole> roles = this.userService.selectUserById(holiday.getProposerId()).getRoles();
            for (SysRole role : roles) {
                // 根据类型和当前用户角色ID，查询预设审批
                HolidayApproval approvalParams = HolidayApproval.builder()
                        .holidayTypeId(holiday.getHolidayTypeId())
                        .roleId(role.getRoleId())
                        .currentApprovedIndex(holiday.getCurrentApprovedIndex())
                        .build();
                List<HolidayApproval> approvals = holidayApprovalService.queryAll(approvalParams);
                // 根据部门ID和查询到的角色ID，查询可选用户
                for (HolidayApproval approval : approvals) {
                    HolidayUserParamsVO paramsVO = HolidayUserParamsVO.builder()
                            .deptId(deptId)
                            .roleId(approval.getApprovedRoleId())
                            .build();
                    List<HolidayUserResultVO> resultVOList = holidayService.selectUserList(paramsVO);
                    result.addAll(resultVOList);
                }
            }
            return AjaxResult.success(result);
        } else { // 新增阶段查询
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            if (!StringUtils.isNull(loginUser) && !CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
                List<HolidayUserResultVO> result = new ArrayList<>();

                Long deptId = loginUser.getUser().getDept().getDeptId();
                List<SysRole> roles = loginUser.getUser().getRoles();
                for (SysRole role : roles) {
                    // 根据类型和当前用户角色ID，查询预设审批
                    HolidayApproval approvalParams = HolidayApproval.builder()
                            .holidayTypeId(holiday.getHolidayTypeId())
                            .roleId(role.getRoleId())
                            .currentApprovedIndex(holiday.getCurrentApprovedIndex())
                            .build();
                    List<HolidayApproval> approvals = holidayApprovalService.queryAll(approvalParams);
                    // 根据部门ID和查询到的角色ID，查询可选用户
                    for (HolidayApproval approval : approvals) {
                        HolidayUserParamsVO paramsVO = HolidayUserParamsVO.builder()
                                .deptId(deptId)
                                .roleId(approval.getApprovedRoleId())
                                .build();
                        List<HolidayUserResultVO> resultVOList = holidayService.selectUserList(paramsVO);
                        result.addAll(resultVOList);
                    }
                }
                return AjaxResult.success(result);
            }
        }
        return AjaxResult.error("当前用户暂无权限！");
    }

    /**
     * 新增数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    @PostMapping("/")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:add')")
    @Log(title = "假期", businessType = BusinessType.INSERT)
    public AjaxResult addHoliday(@RequestBody Holiday holiday) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        holiday.setProposerId(loginUser.getUser().getUserId());
        int count = this.holidayService.insert(holiday);
        HolidayItem holidayItem = HolidayItem.builder()
                .holidayId(holiday.getHolidayId())
                .approvedUserId(holiday.getCurrentApproverId())
                .approvedIndex(holiday.getCurrentApprovedIndex())
                .status(0)
                .approveInstruction()
                .delFlag(0)
                .build();
        int countItem = this.holidayItemService.insert(holidayItem);
        return count > 0 & countItem > 0 ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 修改数据
     *
     * @param holiday 实例对象
     * @return 实例对象
     */
    @PutMapping("")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:modify')")
    @Log(title = "假期", businessType = BusinessType.UPDATE)
    public AjaxResult modifyHoliday(@RequestBody Holiday holiday) {
        // 保存当前审批事项
        HolidayItem holidayItem = HolidayItem.builder()
                .holidayId(holiday.getHolidayId())
                .approvedIndex(holiday.getCurrentApprovedIndex())
                .status(holiday.getStatus())
                .approveInstruction(holiday.getHolidayInstruction())
                .build();
        int countItem = this.holidayItemService.update(holidayItem);

        // 若通过且有下一节点，则添加下一事项
        // 默认取第一个角色
        Long roleId = this.userService.selectUserById(holiday.getProposerId()).getRoles().get(0);
        HolidayApproval params = HolidayApproval.builder()
                .holidayTypeId(holiday.getHolidayTypeId())
                .roleId(roleId)
                .currentApprovedIndex(holiday.getCurrentApprovedIndex())
                .build();
        if (this.holidayApprovalService.hasNextApproved(params) && holiday.getStatus() == 1) {
            holiday.setStatus(0);
            HolidayItem newHolidayItem = HolidayItem.builder()
                    .holidayId(holiday.getHolidayId())
                    .approvedUserId(holiday.getCurrentApproverId())
                    .approvedIndex(holiday.getCurrentApprovedIndex() + 1)
                    .status(0)
                    .delFlag(0)
                    .build();
            this.holidayItemService.insert(newHolidayItem);
        }
        // 驳回了 / 没有下一节点了
        int count = this.holidayService.update(holiday);
        return count > 0 & countItem > 0? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }

    /**
     * 通过主键删除数据
     *
     * @param holidayId 主键
     * @return 是否成功
     */
    @DeleteMapping("/{holidayId}")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:delete')")
    @Log(title = "假期", businessType = BusinessType.DELETE)
    public AjaxResult deleteById(@PathVariable("holidayId") Long holidayId) {
        int count = this.holidayService.deleteById(holidayId);
        return count > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
}
