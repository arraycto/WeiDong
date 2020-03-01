package com.werun.back.filter;


import com.werun.back.token.RTM;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginFilter
 * @Author HWG
 * @Time 2019/4/17 20:59
 */
@WebFilter(filterName = "loginFilter",urlPatterns = {"/test/*","/user/*","/like/*","/order/*","/diary/*",
        "/act/*","/comment/*","/good/*","/add/*","/ex/*"})
public class LoginFilter implements Filter {
    private String[] unFilt={"redirect","login","test","back"};
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();

        System.out.println(requestURI+"---"+requestURL);
        String replace = requestURL.replace(requestURI, "");
        if (checkIsFreeUrl(request))
            filterChain.doFilter(servletRequest,servletResponse);
        else
            ((HttpServletResponse)servletResponse).sendRedirect(replace+"/back/test/redirect");

    }

    @Override
    public void destroy() {

    }
    private boolean checkIsFreeUrl(HttpServletRequest request){
        String uri=request.getRequestURI();
        for (String i:unFilt) {
            if (uri.contains(i))
                return true;
        }
        String token = (String) request.getAttribute("token");
        if(token==null||token=="")
            return false;
        return true;
    }
}
