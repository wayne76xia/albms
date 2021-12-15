package alb.framework.aspectj.lang.annotation;

import java.lang.annotation.*;

/**
 * Data permission filtering annotations
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{
    /**
     * Alias of the department table
     */
    String deptAlias() default "";

    /**
     * Alias of the user table
     */
    String userAlias() default "";
}
