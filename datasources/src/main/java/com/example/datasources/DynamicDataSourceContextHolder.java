package com.example.datasources;

/*这个类用来存储当前线程所使用的数据源名称*/
/*存入ThreadLocal*/
public class DynamicDataSourceContextHolder {
    private static ThreadLocal<String> CONTEXT_HOLDER=new ThreadLocal<>();

    public static void setDataSourceType(String dsType){
        CONTEXT_HOLDER.set(dsType);
    }
    public static String getDataSourceType(){
        return CONTEXT_HOLDER.get();
    }
    public static void clearDataSourceType(){//防止内存泄漏
        CONTEXT_HOLDER.remove();
    }
}
