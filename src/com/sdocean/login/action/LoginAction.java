package com.sdocean.login.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.SysLoginLogService;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.menu.service.SysMenuService;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;
import com.sdocean.users.service.UsersManager;

@Controller
public class LoginAction {

	private static Logger log = Logger.getLogger(LoginAction.class);  
	@Autowired
	private UsersManager usersManager;
	
	@Autowired
	private SysMenuService menuService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private SysLoginLogService loginLogService;
	
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("login.do")
	public ModelAndView login(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		//将系统信息保存到session中
		HttpSession session = request.getSession();
		session.setAttribute("system", info);
		return new ModelAndView("/"+info.getPageVision()+"/login");
	}
	
	@RequestMapping("index.do")
	public ModelAndView handleRequestInternal(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			//验证用户账号与密码是否对应   
	        username = username.trim();  
	        SysUser users = this.usersManager.getUsersByAccount(username);
	        if(users == null || !users.getPassword().equals(password)) {
	        	String error = "用户名或密码错误,请重新输入" ;
	        	return new ModelAndView("/"+info.getPageVision()+"/login","error",error);  
	        } 
	        //将用户信息存入到session中
	        HttpSession session = request.getSession();
	        session.setAttribute("user", users);
	        loginLogService.saveSysLoginLog(request,info);
	        
	        //读取权限内的菜单的列表
	        List<SysMenu> menuList = menuService.getMenuListByUser(users);
	        //如果该用户没有菜单权限,则返回登录页面并清除session
	        if(menuList == null || menuList.size()<1){
	        	String error = "您没有菜单权限,请联系管理员";
	        	session.removeAttribute("user");
	        	return new ModelAndView("/"+info.getPageVision()+"/login","error",error);  
	        }
	        //将菜单列表添加到session中
	        session.setAttribute("menuList", menuList);
	        
	        //获得当前用户的站点列表
	        List<StationModel> stations = stationService.getStations4User(users);
	        if(stations == null || stations.size()<1){
	        	String error = "您没有站点权限,请联系管理员";
	        	session.removeAttribute("user");
	        	return new ModelAndView("/"+info.getPageVision()+"/login","error",error);  
	        }
	        //获得上一次登录的站点
	        StationModel station = stationService.getStation4Last(users);
	        if(station ==null||station.getId()==0){ //如果没有上次登录的信息,则默认站点权限内的第一个站点
	        	station = stations.get(0);
	        }
	        //将此次登录站点信息保存到数据库中
	        stationService.saveStationLog(users, station, 1);
	        
	        //将当前用户的站点列表以及当前登录的站点保存到SESSION中
	        session.setAttribute("stations", stations);
	        session.setAttribute("station", station);
	        //获得用户权限的首页菜单,如果没有,则跳转到默认首页
	        SysMenu menu = menuService.getFirstMenu(users);
	        
	       // String url = "firstPage.do";
	        String url = "info_sysUser.do";
	        if(menu!=null&&menu.getUrl()!=null&&menu.getUrl().length()>0){
	        	url = menu.getUrl();
	        	
	        	CurrMenu cmenu = menuService.getCurrMenuById(menu.getCode());
            	/*MenuModel menu = new MenuModel();
            	MenuDaoImpl menuDao = new MenuDaoImpl();
            	menu = menuDao.getMenuById(value[0]);*/
            	//将菜单信息保存到SESSION中
            	session.setAttribute("currMenu", cmenu);
            	for(SysMenu m:menuList){
            		m.setIsopen(0);
            		if(m.getCode().equals(menu.getCode())){
            			m.setIsopen(1);
            			for(SysMenu c:m.getChildMenu()){
            				c.setIscurr(0);
            				if(c.getCode().equals(menu.getCode())){
            					c.setIscurr(1);
            				}
            			}
            		}
            	}
            	session.setAttribute("menuList", menuList);
	        }
	        
	        
	        String redirect = "redirect:"+url;
	        return new ModelAndView(redirect);
	}
	/*
	 * 用户重新选择站点
	 */
	@RequestMapping("changeWp.do")
	public ModelAndView changeWp(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		//得到跳转到stationId
		String wpid = request.getParameter("wpid");
		Integer stationId = Integer.parseInt(wpid);
		StationModel station = stationService.getStationById(stationId);
		HttpSession session = request.getSession();
		session.removeAttribute("station");
		session.setAttribute("station", station);
		//获得当前登录的用户
		SysUser users = (SysUser) session.getAttribute("user");
		//将此次跳转站点保存到数据库中
		stationService.saveStationLog(users, station, 1);
		//获得SESSION中保存的当前菜单,如果没有,则跳转到首页
		CurrMenu menu = (CurrMenu) session.getAttribute("currMenu");
		String url = "firstPage.do";
		if(menu!=null&&menu.getCurl()!=null){
			url = menu.getCurl();
		}
		String redirect = "redirect:"+url;
		return new ModelAndView(redirect);
	}
	
}
