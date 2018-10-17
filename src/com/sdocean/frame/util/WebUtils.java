package com.sdocean.frame.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.ServletRequestUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class WebUtils {

	public static  Page getPager(HttpServletRequest request) {
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", 10);
		int pageNo = ServletRequestUtils.getIntParameter(request, "page", 1);
		Page page = new Page();
		page.setPageSize(pageSize);
		page.setPageNo(pageNo);
		return page;
	}
	/**
	 * 将数组转化为字符串,例如[3,1,2]返回"3,1,2"
	 * @param array
	 * @return
	 */
	public static String toString(Object[] array){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length-1; i++) {
			sb.append(array[i]);
			sb.append(",");
		}
		if(array.length>0){
			sb.append(array[array.length-1]);
		}
		return sb.toString();
	}
	
	/**
	 * 返回一个key和value反转的biMap视图
	 * @param map
	 * @return
	 */
	public static BiMap<String,Integer> reverseMap(Map<Integer,String> map){
		HashBiMap<Integer,String> bimap = HashBiMap.create(map);
		return bimap.inverse();
	}
	
	public static String getCurUserAccount(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null){
			return null;
		}
		Object principal = authentication.getPrincipal();
		String userAccount=null;
		if (principal instanceof User) {
		  userAccount = ((User)principal).getUsername();
		}
		return userAccount;
	}
	
	
	public static Set<String> getCurUserAuths(){
		Set<String> authsSet=new HashSet<String>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			Collection<GrantedAuthority> authorities = ((User) principal).getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				authsSet.add(grantedAuthority.getAuthority());
			}
			return authsSet;
		}else{
			return null;
		}
	}
}
