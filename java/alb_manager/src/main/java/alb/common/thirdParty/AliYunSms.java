package alb.common.thirdParty;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 阿里云短信接口
 */
public class AliYunSms {

    /** 产品名称:云通信短信API产品,开发者无需替换 */
    private static final String product = "Dysmsapi";
    /** 产品域名,开发者无需替换 */
    private static final String domain = "dysmsapi.aliyuncs.com";

    /** 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找) */
    private static final String accessKeyId = "LTAIlkcGk1b1Y3Cm";
    private static final String accessKeySecret = "djGug9Mx9mcwGb5RfXlVnDw3BLrKsD";

    /** 必填:短信签名-可在短信控制台中找到 */
    private static final String signName = "网来网去";
    /** 必填:待发送手机号 */
    private String phoneNumbers;
    /** 必填:短信模板-可在短信控制台中找到 */
    private String templateCode;
    /** 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为 */
    private String templateParam;
    /** 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者 */
    private String outId;

    public AliYunSms() {
    }

    /** 手机号、短信模板、模板中的变量替换JSON串构造器 */
    public AliYunSms(String phoneNumbers, String templateCode, String templateParam) {
        super();
        this.phoneNumbers = phoneNumbers;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public SendSmsResponse sendSms() throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(phoneNumbers);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);

        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(outId);

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    public QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        // 必填-号码
        request.setPhoneNumber(phoneNumbers);
        // 可选-流水号
        request.setBizId(bizId);
        // 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        // 必填-页大小
        request.setPageSize(10L);
        // 必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        // hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    /**
     * 生成随机六位数
     *
     * @param from
     * @param to
     * @return
     */
    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
