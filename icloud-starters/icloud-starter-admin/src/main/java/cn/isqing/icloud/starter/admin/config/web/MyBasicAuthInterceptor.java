package cn.isqing.icloud.starter.admin.config.web;

import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;

public class MyBasicAuthInterceptor implements HandlerInterceptor {
    @Setter
    private String username;
    @Setter
    private String password;

    //处理器运行之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的Authorization字段
        String authHeader = request.getHeader("Authorization");
        //如果没有Authorization字段，则返回false并设置响应状态码为401，并设置响应头中的WWW-Authenticate字段
        if (authHeader == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Basic realm=\"Please enter your username and password\"");
            return false;
        }
        //如果有Authorization字段，则对其进行解码和验证
        String[] authParts = authHeader.split(" ");
        if (authParts.length != 2 || !"Basic".equals(authParts[0])) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String decodedAuth = new String(Base64.getDecoder().decode(authParts[1]));
        String[] credentials = decodedAuth.split(":");
        if (credentials.length != 2) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String username = credentials[0];
        String password = credentials[1];
        if (this.username.equals(username) && this.password.equals(password)) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
