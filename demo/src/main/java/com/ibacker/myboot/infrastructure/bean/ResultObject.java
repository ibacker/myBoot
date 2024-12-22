package com.ibacker.myboot.infrastructure.bean;

import com.ibacker.myboot.infrastructure.util.JsonUtil;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
public class ResultObject {

    private boolean success;
    /**
     * 状态码
     */
    private int code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 返回对象
     */
    private Object result;

    /**
     * 默认构造
     */
    public ResultObject() {
        this.setSuccess(true);
        this.setCode(200);
        this.setMessage("success");
    }

    /**
     * 成功
     * 不返回对象
     *
     * @return
     */
    public static ResultObject success() {
        return success(null);
    }

    /**
     * 成功
     * 返回对象
     *
     * @param detail:返回的对象
     * @return
     */
    public static ResultObject success(Object detail) {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(true);
        ResultObject.setCode(200);
        ResultObject.setMessage("success");
        ResultObject.setResult(detail);
        return ResultObject;
    }

    /**
     * 失败
     * 不返回错误信息
     * 默认错误码：201
     *
     * @return
     */
    public static ResultObject error() {
        return error(null);
    }

    /**
     * 失败
     * 返回错误信息
     * 默认错误码：201
     *
     * @param description : 错误信息
     * @return
     */
    public static ResultObject error(String description) {
        return error(201, description);
    }

    /**
     * 失败
     * 自定义错误码和错误信息
     *
     * @param code：错误码
     * @param description：错误信息
     * @return
     */
    public static ResultObject error(int code, String description) {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(false);
        ResultObject.setCode(code);
        ResultObject.setMessage(description);
        ResultObject.setResult(null);
        return ResultObject;
    }

    /**
     * 失败
     * 用于校验Token时，如果Token不合法或已过期，则将错误码和错误信息以json的格式输出
     *
     * @param response
     * @param code：错误码
     * @param description：错误信息
     * @throws IOException
     */
    public static void error(HttpServletResponse response, int code, String description) throws IOException {
        ResultObject ResultObject = new ResultObject();
        ResultObject.setSuccess(false);
        ResultObject.setCode(code);
        ResultObject.setMessage(description);
        ResultObject.setResult(null);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtil.objectToJson(ResultObject));
    }
}
