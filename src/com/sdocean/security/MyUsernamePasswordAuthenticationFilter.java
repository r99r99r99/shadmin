package com.sdocean.security;  
  
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.log.service.SysLoginLogService;
import com.sdocean.users.model.SysUser;
import com.sdocean.users.service.UsersManager;
  
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{  
    public static final String VALIDATE_CODE = "validateCode";  
    public static final String USERNAME = "username";  
    public static final String PASSWORD = "password";  
    
      
    private UsersManager usersManager;  
    public void setUsersManager(UsersManager usersManager) {
		this.usersManager = usersManager;
	}  
    
    private SysLoginLogService loginLogService;
    public void setLoginLogService(SysLoginLogService loginLogService){
    	this.loginLogService = loginLogService;
    }
  
    @Override  
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {  
        if (!request.getMethod().equals("POST")) {  
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());  
        }  
        //检测验证码   
       // checkValidateCode(request);  
          
        String username = obtainUsername(request);  
        String password = obtainPassword(request);  
          
        //验证用户账号与密码是否对应   
        username = username.trim();  
          
        SysUser users = this.usersManager.getUsersByAccount(username);  
        if(users == null || !users.getPassword().equals(password)) {
            throw new AuthenticationServiceException("用户名或者密码错误！");   
        }  
        //将用户信息存入到session中
        HttpSession session = request.getSession();
        session.setAttribute("user", users);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);  
         
        // 允许子类设置详细属性   
        setDetails(request, authRequest);  
        //loginLogService.saveSysLoginLog(request);
        return this.getAuthenticationManager().authenticate(authRequest);  
    }  
      
    protected void checkValidateCode(HttpServletRequest request) {   
        HttpSession session = request.getSession();  
          
        String sessionValidateCode = obtainSessionValidateCode(session);   
        //让上一次的验证码失效   
        session.setAttribute(VALIDATE_CODE, null);  
        String validateCodeParameter = obtainValidateCodeParameter(request);    
        if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {    
            throw new AuthenticationServiceException("验证码错误！");    
        }    
    }  
      
    private String obtainValidateCodeParameter(HttpServletRequest request) {  
        Object obj = request.getParameter(VALIDATE_CODE);  
        return null == obj ? "" : obj.toString();  
    }  
  
    protected String obtainSessionValidateCode(HttpSession session) {  
        Object obj = session.getAttribute(VALIDATE_CODE);  
        return null == obj ? "" : obj.toString();  
    }  
  
    @Override  
    protected String obtainUsername(HttpServletRequest request) {  
        Object obj = request.getParameter(USERNAME);  
        return null == obj ? "" : obj.toString();  
    }  
  
    @Override  
    protected String obtainPassword(HttpServletRequest request) {  
        Object obj = request.getParameter(PASSWORD);  
        return null == obj ? "" : obj.toString();  
    }  
      
      
}