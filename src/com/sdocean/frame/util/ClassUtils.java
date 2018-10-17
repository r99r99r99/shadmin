package com.sdocean.frame.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sdocean.frame.exception.CopyPropertyException;


public class ClassUtils {

   private static Logger log = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 
     */
    private ClassUtils() {
        super();
    }
    
    public static <X>  List<X> copyPropertyIgnoreCase(Class<X> clazz, Map<String, Object>[] maps) {
        
        List<X> objects = new ArrayList<X>();
        for(int index = 0, len = maps.length; index < len; index ++) {
            objects.add(copyPropertyIgnoreCase(clazz, maps[index]));
        }
        return objects;
    }
    
    public static <X> X copyPropertyIgnoreCase(Class<X> clazz, Map<String, Object> map) {
        try {
            Map<String, Object> newMap = new HashMap<String, Object>();
            X obj = clazz.newInstance();
            PropertyDescriptor[] descriptors =
                PropertyUtils.getPropertyDescriptors(obj);
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                for (int index = 0, len = descriptors.length;
                    index < len;
                    index++) {
                    if (key.equalsIgnoreCase(descriptors[index].getName())) {
                        newMap.put(descriptors[index].getName(), map.get(key));
                        break;
                    }
                }
            }

            BeanUtils.copyProperties(obj, newMap);
            return obj;
        } catch (InstantiationException e) {
        	e.printStackTrace();
            throw new CopyPropertyException("DW0008");
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            throw new CopyPropertyException("DW0008");
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
            throw new CopyPropertyException("DW0008");
        }
    }
       
    public static Map<?, ?> objectToMap(Object obj) {  
        if(obj == null)  
            return null;   
   
        return new org.apache.commons.beanutils.BeanMap(obj);  
    }    
}
