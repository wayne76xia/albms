package alb.project.system.domain.vo;

import alb.framework.aspectj.lang.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName DeptExcelVO
 * @Description 部门导入导出
 * @Date 2020/7/29 19:05
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubsidiaryCompanyExcelVO implements Serializable {

    /** 部门编码 */
    @Excel(name = "部门编码")
    private String deptCode;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leader;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 省 .*/
    @Excel(name = "省")
    private String province;

    /** 市 .*/
    @Excel(name = "市")
    private String city;

    /** 区 .*/
    @Excel(name = "区")
    private String district;

    /** 详细地址 .*/
    @Excel(name = "详细地址")
    private String address;

    /** 显示顺序 */
    @Excel(name = "显示顺序（默认由小到大）")
    private Integer orderNum;

    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date createTime;

}
