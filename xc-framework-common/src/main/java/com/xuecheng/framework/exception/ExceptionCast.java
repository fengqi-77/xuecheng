package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 自定义异常公共方法类
 */
public class ExceptionCast {

    /**
     * 抛出CustomException自定义异常
     * @param resultCode
     */
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
