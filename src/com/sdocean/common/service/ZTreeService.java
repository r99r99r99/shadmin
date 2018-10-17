package com.sdocean.common.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.company.model.CompanyModel;
import com.sdocean.dataQuery.dao.DataQueryDao;
import com.sdocean.dataQuery.dao.SynthQueryDao;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.device.dao.DeviceDao;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.dao.PublicDao;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.dao.SysLoginLogDao;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.page.model.NgColumn;
import com.sdocean.page.model.UiColumn;
import com.sdocean.position.dao.SysPositionDao;
import com.sdocean.position.model.SysPosition;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class ZTreeService {
	
	
	/**
	 * 将list转换成ztree格式
	 * List<T> models : 要转化的实体类集合
	 * String id :  id
	 * String Pid:  设定pid
	 * String name: 设定名称
	 * 
	 */
	public <T> List<ZTreeModel> changeModel2ZTree (List<T> models,String id,String Pid,String name,Boolean open,String url,
												String target,String icon,String syspath) throws Exception{
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		for(T t:models){
			ZTreeModel ztree = new ZTreeModel();
			Class<?> clz = t.getClass();
			Method method = null;
			Object o = null;
			String getString ="get";
			//添加ID
			String idString = getString + id.substring(0, 1).toUpperCase() + id.substring(1);
			method = clz.getMethod(idString, null);
			o = method.invoke(t);
			String idUrl = o.toString();
			if(o!=null&&o.toString()!=null&&o.toString().length()>0){
				ztree.setId( o.toString());
			}else{
				ztree.setId("");
			}
			

			//添加PID
			String pidString = getString + Pid.substring(0, 1).toUpperCase() + Pid.substring(1);
			method = clz.getMethod(pidString, null);
			o = method.invoke(t);
			if(o!=null&&o.toString()!=null&&o.toString().length()>0){
				ztree.setpId(o.toString());
			}else{
				ztree.setpId("");
			}
			
			
			//添加name
			String nameString = getString + name.substring(0, 1).toUpperCase() + name.substring(1);
			method = clz.getMethod(nameString, null);
			o = method.invoke(t);
			if(o!=null&&o.toString()!=null&&o.toString().length()>0){
				ztree.setName(o.toString());
			}else{
				ztree.setName("");
			}
			//设置展示图标
			if(icon!=null&&icon.length()>0){
				String iconString = getString + icon.substring(0, 1).toUpperCase() + icon.substring(1);
				method = clz.getMethod(iconString, null);
				o = method.invoke(t);
				if(o!=null&&o.toString()!=null&&o.toString().length()>0){
					ztree.setIcon(syspath + ztree.getIconPath()+o.toString());
				}
			}
			//设置
			ztree.setOpen(open);
			//设置连接地址
			if(url!=null&&url.length()>0){
				String urlString = getString + url.substring(0, 1).toUpperCase() + url.substring(1);
				method = clz.getMethod(urlString, null);
				o = method.invoke(t);
				if(o!=null&&o.toString()!=null&&o.toString().length()>0){
					ztree.setUrl(o.toString());
				}else{
					ztree.setUrl("");
				}
			}
			ztree.setTarget(target);
			
			//添加ID
			
			list.add(ztree);
		}
		return list;
	}

}
