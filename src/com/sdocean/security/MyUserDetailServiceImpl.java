package com.sdocean.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sdocean.users.model.SysUser;
import com.sdocean.users.service.UsersManager;

public class MyUserDetailServiceImpl implements UserDetailsService {  
      
    private UsersManager usersManager; 
    

	public void setUsersManager(UsersManager usersManager) {
		this.usersManager = usersManager;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
        SysUser users = this.usersManager.getUsersByAccount(username);  
        if(users == null) {  
            throw new UsernameNotFoundException(username);  
        }  
        Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(users);  
          
        boolean enables = true;  
        boolean accountNonExpired = true;  
        boolean credentialsNonExpired = true;  
        boolean accountNonLocked = true;  
          
        User userdetail = new User(users.getUserName(), users.getPassword(), enables, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuths);  
        return userdetail;  
    }  
      
    //取得用户的权限   
    private Set<GrantedAuthority> obtionGrantedAuthorities(SysUser user) {  
       Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();  
       /*List<Authority> authoritysByUserAccount = this.authorityManager.getAuthoritysByUserAccount(user);  
          
       for (Authority authority : authoritysByUserAccount) {
		authSet.add(new SimpleGrantedAuthority(authority.getAuthKey()));
       }*/
       return authSet;  
    }  
}