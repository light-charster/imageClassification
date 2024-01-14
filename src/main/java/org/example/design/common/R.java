package org.example.design.common;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回结果的类，用于给前端页面返回结果
 */
@Data
public class R<T> {
    @Getter
    private Integer code; //编码，0为失败，1为成功
    private String msg;//错误信息
    @Getter
    private T data;//数据
    private Map<String, Object> map=new HashMap<>();//动态数据
    public static <T> R<T> success(T object)
    {
        R<T> r= new R<>();
        r.code=200;
        r.data=object;
        return r;
    }

    public static <T> R<T> error(String msg)
    {
        R<T> r= new R<>();
        r.code=0;
        r.msg=msg;
        return r;
    }

    public R<T> add(String key, Object value)
    {
        this.map.put(key,value);
        return this;
    }

}