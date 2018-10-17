package com.sdocean.interceptor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sdocean.frame.util.JsonUtil;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.menu.service.SysMenuService;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;
import com.sdocean.users.service.UsersHelper;
import com.sdocean.users.service.UsersManager;
import com.sdocean.warn.service.WarnService;


public class LoadUserInterceptor2 extends  HandlerInterceptorAdapter {
	private static Logger logger = Logger.getLogger(LoadUserInterceptor2.class);  
	@Resource
	UsersHelper usersHelper;
	@Resource
	SysMenuService menuService;
	@Resource
	WarnService warnService;
	@Resource
	UsersManager usersManager;
	@Autowired
	private StationService stationService;
	
	@Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//*********开始单点登录判断部分*************//*
		HttpSession session = request.getSession();
		//获得当前登录的用户的信息
		SysUser user = (SysUser) session.getAttribute("user");
		if(user!=null&&user.getId()>0){
			//获得预警告警中的未读信息
		}else{
			String userName = request.getRemoteUser();
			System.out.println(userName);
			if(userName!=null&&userName.length()>0){
				userName = "haikou";
				//获得该用户名的用户信息
				user = usersManager.getUsersByAccount(userName);
				//将用户信息保存到SESSION中
				session.setAttribute("user", user);
				
				StationModel station = (StationModel) session.getAttribute("station");
				if(station==null){
					//获得当前用户的站点列表
			        List<StationModel> stations = stationService.getStations4User(user);
			        //获得上一次登录的站点
			        station = stationService.getStation4Last(user);
			        if(station ==null||station.getId()==0){ //如果没有上次登录的信息,则默认站点权限内的第一个站点
			        	station = stations.get(0);
			        }
			        //将此次登录站点信息保存到数据库中
			        stationService.saveStationLog(user, station, 1);
			        
			        //将当前用户的站点列表以及当前登录的站点保存到SESSION中
			        session.setAttribute("stations", stations);
			        session.setAttribute("station", station);
				}
				//获得该站点的预警告警信息个数
				int warnNum = warnService.getNum4WarnValue(station);
				//判断有没有菜单权限
				List<SysMenu> menuList = (List<SysMenu>) session.getAttribute("menuList");
				if(menuList==null||menuList.size()<1){
					menuList = menuService.getMenuListByUser(user);
					 //将菜单列表添加到session中
			        session.setAttribute("menuList", menuList);
				}
				
				
				session.setAttribute("warnNum", warnNum);
				return true;
			}else{
				//response.sendRedirect("login.do");
				//return false;
			}
		}
		//*********结束单点登录判断部分*************//*
		
		
		//获得当前跳转的路径
		
		/*HttpSession session = request.getSession();
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
		}*/
		
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
