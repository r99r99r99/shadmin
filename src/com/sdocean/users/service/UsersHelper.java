/**
 * PersonService.java created on 2010-11-1
 */
package com.sdocean.users.service;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.frame.util.WebUtils;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class UsersHelper {
	@Resource
	UsersManager usersManager;
	
	public SysUser getCurUser(){
		String account=WebUtils.getCurUserAccount();
		if(account!=null){
			return usersManager.getUsersByAccount(account);
		}else{
			return null;
		}
	}
	
	public boolean hasAuthOfCurUser(String authKey){
		Set<String> auths=WebUtils.getCurUserAuths();
		if(auths!=null){
			return auths.contains(authKey);
		}
		return false;
	}
	
}
