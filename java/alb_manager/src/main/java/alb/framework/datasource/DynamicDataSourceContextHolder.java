package alb.framework.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data source switching
 *
 */
public class DynamicDataSourceContextHolder
{
    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * Set variables for the data source
     */
    public static void setDataSourceType(String dsType)
    {
        log.info("Switch to the{}The data source", dsType);
        CONTEXT_HOLDER.set(dsType);
    }

    /**
     * Get the variables for the data source
     */
    public static String getDataSourceType()
    {
        return CONTEXT_HOLDER.get();
    }

    /**
     * Clearing data source variables
     */
    public static void clearDataSourceType()
    {
        CONTEXT_HOLDER.remove();
    }
}
