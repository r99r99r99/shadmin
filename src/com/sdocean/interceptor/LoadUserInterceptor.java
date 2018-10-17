package com.sdocean.interceptor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sdocean.frame.util.JsonUtil;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.menu.service.SysMenuService;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.users.service.UsersHelper;
import com.sdocean.warn.service.WarnService;


public class LoadUserInterceptor extends  HandlerInterceptorAdapter {
	private static Logger logger = Logger.getLogger(LoadUserInterceptor.class);  
	@Resource
	UsersHelper usersHelper;
	@Resource
	SysMenuService menuService;
	@Resource
	WarnService warnService;
	
	@Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//获得当前跳转的路径
		HttpSession session = request.getSession();
		String url = request.getServletPath();
		if(url.equals("/index.do")||url.equals("/login.do")){
			
		}else{
			SysUser user = (SysUser) session.getAttribute("user");
			if(user!=null&&user.getId()>0){
				//获得预警告警中的未读信息
				StationModel station = (StationModel) session.getAttribute("station");
				int warnNum = warnService.getNum4WarnValue(station);
				
				session.setAttribute("warnNum", warnNum);
			}else{
				response.sendRedirect("login.do");
				return false;
			}
		}
		
		//将当前的菜单信息保存到SESSION中
		//String Resquesturl = request.getRequestURI();
        try {
            @SuppressWarnings("unchecked")
            Map<String, String[]> parmMap = request.getParameterMap();
            Iterator<String> iter = parmMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                String[] value = parmMap.get(key);
                //如果是菜单提交请求时,保存菜单信息到SESSION中
                if(key.toString().equals("currmenuId")){
                	CurrMenu menu = menuService.getCurrMenuById(value[0]);
                	/*MenuModel menu = new MenuModel();
                	MenuDaoImpl menuDao = new MenuDaoImpl();
                	menu = menuDao.getMenuById(value[0]);*/
                	menuService.saveCurrMenu(menu, request);;
                }
            }
        } catch (Exception e) {
        }
        return true;  
    }  
    
	@Override  
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {  
    
    }  
   
    @Override  
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {  

    }

}
