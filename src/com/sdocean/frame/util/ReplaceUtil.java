package com.sdocean.frame.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReplaceUtil {
	public String replaceWord(String fromText,Object model,String bsign,String esign){
		//将object中的属性值转换为map的形式
		Map map = new HashMap<>();
		Field[] field = model.getClass().getDeclaredFields();
		for(int j=0;j<field.length;j++){
			String name = field[j].getName();    //获取属性的名字
		 	//获得首字母大写的属性的名字
		 	String uppname = name.substring(0, 1).toUpperCase()+name.substring(1, name.length());
		 	Method m;
		 	Object value = null;
			try {
				m = model.getClass().getMethod("get"+uppname);
				value = m.invoke(model);
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	
		 	map.put(name, value);
		 	System.out.println(JsonUtil.toJson(map));
		 }
			//判断字符串中是否有开始标记和结束标记
		while(fromText.contains(bsign)&&fromText.contains(esign)){
			//开始标记所在的位置
			int bindex = fromText.indexOf(bsign);
			//结束标记所在的位置
			int eindex = fromText.indexOf(esign);
					
			//得到标记中间的文字
			String between = fromText.substring(bindex+1, eindex);
			String fromBetween = bsign+between+esign;
			Object replace = map.get(between);
			//将原来的文字替换
			fromText = fromText.replace(fromBetween, replace.toString());
		}
		return fromText;
	}
}
