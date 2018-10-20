package com.sdocean.common.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class SelectTreeService {
	
	public <T> List<SelectTree> changeModel2ZTree (List<T> models,SelectTree parentModel,
			String idString,String nameString,String clildrenString) throws Exception{
		//初始化返回结果
		List<SelectTree> selectList = new ArrayList<>();
		//初始化要传入的元素列表
		List<SelectTree> childList = new ArrayList<>();
		for(T t:models) {
			SelectTree select = new SelectTree();
			Class<?> clz = t.getClass();
			Method method = null;
			Object id = null;  
			Object name = null;
			Object children = null;
			String getString ="get";
			String idMethod = getString + idString.substring(0, 1).toUpperCase() + idString.substring(1);
			String nameMethod = getString + nameString.substring(0, 1).toUpperCase() + nameString.substring(1);
			String childMethod = getString + clildrenString.substring(0, 1).toUpperCase() + clildrenString.substring(1);
			method = clz.getMethod(idMethod, null);
			id = method.invoke(t);
			method = clz.getMethod(nameMethod, null);
			name = method.invoke(t);
			method = clz.getMethod(idMethod, null);
			id = method.invoke(t);
			
		}
		
		//添加最顶层元素
		if(parentModel!=null) {
			selectList.add(parentModel);
		}
		
		return selectList;
	}
}
