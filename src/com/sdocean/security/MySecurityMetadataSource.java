package com.sdocean.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.sdocean.resources.dto.Resources;
import com.sdocean.resources.service.ResourcesManager;

//1 加载资源与权限的对应关系   
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {  
    //由spring调用   
    public MySecurityMetadataSource(ResourcesManager resourcesManager) {  
        this.resourcesManager = resourcesManager;  
        loadResourceDefine();  
    }  
  
    private ResourcesManager resourcesManager;  
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;  
  
    public void setResourcesDao(ResourcesManager resourcesManager) {  
        this.resourcesManager = resourcesManager;  
    }  
  
    public Collection<ConfigAttribute> getAllConfigAttributes() {  
        // TODO Auto-generated method stub   
        return null;  
    }  
  
    public boolean supports(Class<?> clazz) {  
        // TODO Auto-generated method stub   
        return true;  
    }  
    //加载所有资源与权限的关系   
    private void loadResourceDefine() {  
        if(resourceMap == null) {  
            resourceMap = new HashMap<String, Collection<ConfigAttribute>>();  
            List<Resources> resources = this.resourcesManager.findAll();  
            for (Resources resource : resources) {  
                Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();  
                                //以权限名封装为Spring的security Object   
                ConfigAttribute configAttribute = new SecurityConfig(resource.getAUTHKEY());  
                configAttributes.add(configAttribute);  
                resourceMap.put(resource.getResUrl(), configAttributes);  
            }  
        }
          
//        Set<Entry<String, Collection<ConfigAttribute>>> resourceSet = resourceMap.entrySet();  
//        Iterator<Entry<String, Collection<ConfigAttribute>>> iterator = resourceSet.iterator();  
          
    }  
    //返回所请求资源所需要的权限   
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {  
          
        String requestUrl = ((FilterInvocation) object).getRequestUrl();  
//        System.out.println("requestUrl is " + requestUrl);  
        if(resourceMap == null) {  
            loadResourceDefine();  
        }
        return resourceMap.get(requestUrl);  
    }  
  
} 