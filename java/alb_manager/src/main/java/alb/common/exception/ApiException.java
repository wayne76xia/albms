package alb.common.exception;

import lombok.Data;

/**
 * @ClassName : ApiException  //类名
 * @Description : 我的异常处理  //描述
 * @Date: 2019-11-27 15:42  //时间
 */
@Data
public class ApiException extends RuntimeException {
    private Integer code;


    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
