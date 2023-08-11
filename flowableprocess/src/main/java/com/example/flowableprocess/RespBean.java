package com.example.flowableprocess;

public class RespBean {
    private Integer status;
    private String msg;
    private Object datal;

    public static RespBean ok(String msg,Object data){
        return new RespBean(200,msg,data);
    }
    public static RespBean ok(String msg){
        return new RespBean(200,msg,null);
    }
    public static RespBean error(String msg,Object data){
        return new RespBean(500,msg,data);
    }
    public static RespBean error(String msg){
        return new RespBean(500,msg,null);
    }


    public RespBean() {
    }

    public RespBean(Integer status, String msg, Object datal) {
        this.status = status;
        this.msg = msg;
        this.datal = datal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getDatal() {
        return datal;
    }

    public void setDatal(Object datal) {
        this.datal = datal;
    }
}
