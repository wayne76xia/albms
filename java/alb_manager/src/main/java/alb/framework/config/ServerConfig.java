package alb.framework.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import alb.common.utils.ServletUtils;

/**
 * Service Configuration
 *
 */
@Component
public class ServerConfig
{
    /**
     * Gets the full request path,including:The domain name,port,Context access path
     * 
     * @return Service address
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
