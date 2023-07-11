package com.example.submitrepeat.request;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.Buffer;

//装饰者模式：允许向一个现有的对象添加新的功能，同时又不改变其结构。
/*数据存入bytes数组，数组不会变了。每次需要的时候从bytes从拿到，于是消除之前的重复读的问题*/
public class RepeatAbleReadRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] bytes;
    //重写的构造方法本来只有一个参数，现在想给响应也配置，自己添加一个参数
    public RepeatAbleReadRequestWrapper(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        bytes=request.getReader().readLine().getBytes();
    }

    //getReader()从getInputStream()产生.
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public int available() throws IOException {
                return bytes.length;
            }
        };
    }
}
