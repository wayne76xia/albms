package alb.project.vacation.controller;

import alb.common.exception.CustomException;
import alb.project.vacation.domain.ParamsData;
import alb.project.vacation.service.IParamsDataService;
import alb.project.system.domain.SysConfig;
import alb.project.system.service.ISysConfigService;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import alb.common.utils.FileToMultipartFile;
import alb.common.utils.SecurityUtils;
import alb.common.utils.file.FileUploadUtils;
import alb.common.utils.poi.ExcelUtil;
import alb.framework.aspectj.lang.annotation.Log;
import alb.framework.aspectj.lang.enums.BusinessType;
import alb.framework.config.WlwqConfig;
import alb.framework.web.controller.BaseController;
import alb.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 参数设置Controller
 *
 * @date 2020-07-16
 */
@RestController
@RequestMapping("/vacation/paramsData")
public class ParamsDataController extends BaseController {
    private final IParamsDataService paramsDataService;

    @Autowired
    private ISysConfigService configService;
    @Value("${shareUrl}")
    private String shareUrl;

    public ParamsDataController(IParamsDataService paramsDataService) {
        this.paramsDataService = paramsDataService;
    }

    /**
     * 根据类型查询参数设置列表-下拉列表哟个
     */
    @GetMapping("/getListByType")
    public AjaxResult getListByType(ParamsData paramsData) {
        List<ParamsData> list = paramsDataService.selectParamsDataList(paramsData);
        return AjaxResult.success(list);
    }

    /**
     * 查询参数设置列表
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:list')")
    @GetMapping("/list")
    public AjaxResult list(ParamsData paramsData) {
        List<ParamsData> list = paramsDataService.selectParamsDataList(paramsData);
        // 登录页图片链接替换
        list.stream().filter(e -> e.getType() == 6).forEach(e -> {
            String src = configService.selectConfigByKey(e.getValue());
            e.setValue(src);
        });
        return AjaxResult.success(list);
    }

    /**
     * 导出参数设置列表
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:export')")
    @Log(title = "参数设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ParamsData paramsData) {
        List<ParamsData> list = paramsDataService.selectParamsDataList(paramsData);
        ExcelUtil<ParamsData> util = new ExcelUtil<ParamsData>(ParamsData.class);
        return util.exportExcel(list, "paramsData");
    }

    /**
     * 获取参数设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(paramsDataService.selectParamsDataById(id));
    }

    /**
     * 登录页暂时只能写成固定形式
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:query')")
    @GetMapping(value = "/backGroundPic/{id}")
    public AjaxResult getBackGroundPic(@PathVariable("id") Long id) {
        ParamsData backGroundPic = paramsDataService.selectParamsDataById(id);
        if (backGroundPic != null && backGroundPic.getValue() != null) {
            backGroundPic.setValue(configService.selectConfigByKey(backGroundPic.getValue()));
        }
        return AjaxResult.success(backGroundPic);
    }

    /**
     * 生成二维码
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getQrCode")
    public AjaxResult getQrCode(Long id) throws IOException {
        if (id == null) {
            throw new CustomException("标识为空！");
        }
        ParamsData data = paramsDataService.selectParamsDataById(id);
        if (data == null) {
            throw new CustomException("未获取到详细信息！");
        }
        ConverterRegistry converter = ConverterRegistry.getInstance();
        String path = WlwqConfig.getAvatarPath() + "/qrCode.jpg";
        QrCodeUtil.generate(shareUrl + data.getValue(), 300, 300, FileUtil.file(path));
        String qrCodePic = FileUploadUtils.upload(WlwqConfig.getAvatarPath(), FileToMultipartFile.getMulFileByPath(path));
        AjaxResult result = new AjaxResult();
        result.put("qrCodePic", qrCodePic);
        return AjaxResult.success(result);
    }

    /**
     * 新增参数设置
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:add')")
    @Log(title = "参数设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParamsData paramsData) {
        paramsData.setLastDate(new Date());
        return toAjax(paramsDataService.insertParamsData(paramsData));
    }

    /**
     * 修改参数设置
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:edit')")
    @Log(title = "参数设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParamsData paramsData) {
        paramsData.setLastDate(new Date());
        return toAjax(paramsDataService.updateParamsData(paramsData));
    }

    /**
     * 修改登录页
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:edit')")
    @Log(title = "参数设置", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/backGroundPic")
    public AjaxResult editBackGroundPic(@RequestBody ParamsData paramsData) {
        // 获取原始对象
        ParamsData backGroundPic = paramsDataService.selectParamsDataById(paramsData.getId());
        // 获取参数配置对象
        SysConfig config = configService.selectConfigObjectByKey(backGroundPic.getValue());
        // 更新图片新值
        config.setConfigValue(paramsData.getValue());
        // 更新参数配置对象
        config.setUpdateBy(SecurityUtils.getUsername());
        configService.updateConfig(config);
        // 更新对象时间状态
        backGroundPic.setLastDate(new Date());
        return toAjax(paramsDataService.updateParamsData(backGroundPic));
    }

    /**
     * 删除参数设置
     */
    @PreAuthorize("@ss.hasPermi('vacation:paramsData:remove')")
    @Log(title = "参数设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(paramsDataService.deleteParamsDataByIds(ids));
    }
}
