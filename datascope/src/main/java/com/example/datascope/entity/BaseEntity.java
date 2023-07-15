package com.example.datascope.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.HashMap;
import java.util.Map;

public class BaseEntity {
    @TableField(exist = false)
    private Map<String,String> params=new HashMap<>();//new HashMap的原因：在Aspect的最后需要getParams。可能是空指针

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
