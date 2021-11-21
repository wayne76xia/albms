package alb.project.common;

import alb.common.constant.Constants;
import alb.common.utils.StringUtils;
import alb.common.utils.file.FileUploadUtils;
import alb.common.utils.file.FileUtils;
import alb.framework.config.ServerConfig;
import alb.framework.config.WlwqConfig;
import alb.framework.redis.RedisCache;
import alb.framework.web.domain.AjaxResult;
import alb.project.system.domain.SysDept;
import alb.project.system.domain.SysUser;
import alb.project.system.service.ISysConfigService;
import alb.project.system.service.ISysDeptService;
import alb.project.system.service.ISysUserService;
import alb.project.vacation.domain.ParamsData;
import alb.project.vacation.service.IParamsDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 通用请求处理
 *
 */
@RestController
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    // 匹配电话号码
    private static final Pattern phonePattern = Pattern.compile("^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$");
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private IParamsDataService paramsDataService; // 参数设置
    // 缓存
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService deptService;

    /**
     *
     * @Date: 2020/7/21 17:25
     * @Description: 获取登录背景图和技术电话
     */
    @RequestMapping(value = "/getLoginBackGroundPicAndTechnicalPhone", method = RequestMethod.GET)
    public AjaxResult getLoginBackGroundPicAndTechnicalPhone() {
        ParamsData backGroundPic = paramsDataService.selectParamsDataById(7L);
        ParamsData technicalPhone = paramsDataService.selectParamsDataById(8L);
        Map<String, String> resultMap = new HashMap<>();
        String value = "";
        if (backGroundPic != null && backGroundPic.getValue() != null) {
            value = configService.selectConfigByKey(backGroundPic.getValue());
        }
        resultMap.put("backGroundPic", value);
        resultMap.put("technicalPhone", technicalPhone == null ? "" : technicalPhone.getValue() == null ? "" : technicalPhone.getValue());
        return AjaxResult.success(resultMap);
    }

    /**
     * Create By Renbowen
     *
     * @Date: 2020/7/22 17:07
     * @Description: 根据登录账号获取名字
     */
    @RequestMapping(value = "/getDeptNameByUserName", method = RequestMethod.GET)
    public AjaxResult getDeptNameByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        SysUser sysUser = sysUserService.selectUserByUserName(userName);
        if (sysUser == null) {
            return null;
        }
        SysDept dept = deptService.selectDeptById(sysUser.getDeptId());
        return AjaxResult.success(AjaxResult.successMsg, dept == null ? "" : dept.getDeptName() == null ? "" : dept.getDeptName());
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = WlwqConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = WlwqConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 本地资源路径
        String localPath = WlwqConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }

//    /**
//     * 推送至进线池缓冲队列
//     *
//     * @return
//     */
//    @CrossOrigin
//    @RequestMapping(value = "/pushIncomingPoolMq",
//            method = RequestMethod.PUT)
//    public void pushIncomingPool(@RequestBody IncomingPool incomingPool) {
//        String phone = incomingPool.getPhone();
//        int sourceType = incomingPool.getSourceType() != null ? (int) incomingPool.getSourceType().longValue() : 1;
//        if (!phonePattern.matcher(phone).matches()) {
//            return;
//        }
//        switch (sourceType) {
//            case 2: // 南航
//                break;
//            case 3: // 国航
//                break;
//            case 4: // 东航
//                break;
//            case 5: // 海航
//                break;
//            default:
//                sourceType = 1; // 电话
//        }
//        // 放入消息队列
//        List<String> list = new ArrayList<>();
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ObjectMapper mapper = new ObjectMapper();
//            JsonGenerator jg = mapper.getFactory().createGenerator(byteArrayOutputStream, JsonEncoding.UTF8);
//            jg.writeStartObject(); // {
//            // 电话
//            jg.writeStringField("phone", phone);
//            // 性别 （1=男,2=女,3=未知） => （0=男,1=女,2=未知）
//            jg.writeNumberField("sex", 2);
//            // 来源
//            jg.writeNumberField("sourceType", sourceType);
//            jg.writeEndObject(); // }
//            jg.close();
//            list.add(byteArrayOutputStream.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        redisCache.setCacheList("IncomingPoolMq", list);
//    }
}
