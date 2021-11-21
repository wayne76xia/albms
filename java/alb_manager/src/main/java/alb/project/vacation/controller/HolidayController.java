package alb.project.vacation.controller;

import alb.framework.aspectj.lang.annotation.Log;
import alb.framework.aspectj.lang.enums.BusinessType;
import alb.framework.web.controller.BaseController;
import alb.framework.web.domain.AjaxResult;
import alb.framework.web.page.TableDataInfo;
import alb.project.vacation.domain.Holiday;
import alb.project.vacation.domain.HolidayItem;
import alb.project.vacation.service.IHolidayService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HaoHao
 * Created on 2021/1/26.
 */

@RestController
@RequestMapping("/vacation/holiday")
public class HolidayController extends BaseController {

    @Resource
    private IHolidayService holidayService;

    /**
     * 查询单条数据
     *
     * @param holiday 主键
     * @return 实例对象
     */
    @GetMapping("")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:query')")
    public AjaxResult queryUser(Holiday holiday) {
        Holiday result = this.holidayService.queryOne(holiday);
        return result != null ? AjaxResult.success(result) : AjaxResult.error();
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
        List<Holiday> list = this.holidayService.queryAllByParams(holiday);
        return getDataTable(list);
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
        int count = this.holidayService.insert(holiday);
        return count > 0 ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 新增事项数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @PostMapping("item")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:add')")
    @Log(title = "假期", businessType = BusinessType.INSERT)
    public AjaxResult addHolidayItem(@RequestBody HolidayItem holidayItem) {
        int count = this.holidayService.insertItem(holidayItem);
        return count > 0 ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
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
        int count = this.holidayService.update(holiday);
        return count > 0 ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }

    /**
     * 修改事项数据
     *
     * @param holidayItem 实例对象
     * @return 实例对象
     */
    @PutMapping("/item")
    @PreAuthorize("@ss.hasPermi('vacation:holiday:modify')")
    @Log(title = "假期事项", businessType = BusinessType.UPDATE)
    public AjaxResult modifyHolidayItem(@RequestBody HolidayItem holidayItem) {
        int count = this.holidayService.updateItem(holidayItem);
        return count > 0 ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
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
