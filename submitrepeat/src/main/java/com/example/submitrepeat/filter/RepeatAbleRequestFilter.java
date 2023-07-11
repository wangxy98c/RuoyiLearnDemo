package com.example.submitrepeat.filter;

import com.example.submitrepeat.request.RepeatAbleReadRequestWrapper;
import io.netty.util.internal.StringUtil;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RepeatAbleRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //判断前端请求的数据是否是json形式，是的话才需要特殊处理（interceptor）
        if(StringUtils.startsWithIgnoreCase(request.getContentType(),"application/json")){
            //相当于用Wrapper处理一下得到的结果再扔回去？
            RepeatAbleReadRequestWrapper requestWrapper = new RepeatAbleReadRequestWrapper(request, (HttpServletResponse) servletResponse);
            filterChain.doFilter(requestWrapper,servletResponse);
            return;
        }//requestWrapper 是servletRequest的修饰者。
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
