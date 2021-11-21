package alb.framework.web.controller;

import alb.common.constant.HttpStatus;
import alb.framework.web.domain.AjaxResult;
import alb.framework.web.page.PageDomain;
import alb.framework.web.page.TableDataInfo;
import alb.framework.web.page.TableSupport;
import cn.hutool.core.convert.ConverterRegistry;
import alb.common.utils.DateUtils;
import alb.common.utils.StringUtils;
import alb.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * web层通用数据处理
 *
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);
    // 全局类型转换器
    protected final static ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    public static <T> List<T> removeNull(List<? extends T> oldList) {
        // 临时集合
        List<T> listTemp = new LinkedList<>();
        for (T t : oldList) {
            // 保存不为空的元素
            if (t != null) {
                listTemp.add(t);
            }
        }
        return listTemp;
    }
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
