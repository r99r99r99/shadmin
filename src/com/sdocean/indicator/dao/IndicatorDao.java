package com.sdocean.indicator.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.tool.Extension.Param;
import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorGroupModel;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Component
public class IndicatorDao extends OracleEngine{
	
	/*
	 * 查询符合条件的参数集合
	 */
	public List<IndicatorModel> showIndicators(IndicatorModel model){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupid,b.title as groupName,");
		sql.append(" a.unitId,c.logo as unitName,a.description,a.isactive,d.value as isactiveName,a.orderCode,a.standardCode");
		sql.append(" from dm_indicator a");
		sql.append(" left join dm_indicator_group b on a.groupid = b.id");
		sql.append(" left join g_unit c on a.unitId = c.id");
		sql.append(" left join sys_public d on d.parentcode = '0004' and a.isactive = d.classid");
		sql.append(" where 1= 1");
		//添加查询条件
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and (a.code like '%").append(model.getCode()).append("%' or");
			sql.append(" a.title like '%").append(model.getCode()).append("%') ");
		}
		if(model!=null&&model.getIsactive()<2){
			sql.append(" and a.isactive = ").append(model.getIsactive());
		}
		if(model!=null&&model.getGroupId()>0){
			sql.append(" and a.groupid = ").append(model.getGroupId());
		}
		//增加排序
		sql.append(" order by a.orderCode");
		list = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return list;
	}
	
	/*
	 * 保存修改的参数
	 */
	public Result saveIndicatorChange(IndicatorModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//判断CODE是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from dm_indicator where id <>").append(model.getId()).append(" and code = '").append(model.getCode()).append("'");
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("唯一性验证失败");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("CODE重复,违反唯一性原则");
			return result;
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" update dm_indicator set code=?,title=?,groupID=?,unitId=?,description=?,isactive=?,orderCode=?,standardcode=? where id=?");
		Object[] params = new Object[]{
			model.getCode(),model.getTitle(),model.getGroupId(),
			model.getUnitId(),model.getDescription(),model.getIsactive(),model.getOrderCode(),model.getStandardCode(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
		}
		
		return result;
	}
	
	/*
	 * 保存新增的参数
	 */
	public Result saveNewIndicator(IndicatorModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//进行唯一性判断
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from dm_indicator where code = '").append(model.getCode()).append("'");
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("唯一性验证失败");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("CODE重复,违反唯一性原则");
			return result;
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into dm_indicator(code,title,groupID,unitId,description,isactive,orderCode,standardcode) values(?,?,?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getTitle(),model.getGroupId(),
				model.getUnitId(),model.getDescription(),model.getIsactive(),model.getOrderCode(),model.getStandardCode()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
		}
	
		return result;
	}
	
	/*
	 * 得到参数的下拉树
	 * 根据当前的设备选中已有的菜单
	 */
	public List<SelectTree> getIndicatorList4Tree(DeviceModel model){
		List<SelectTree> list = new ArrayList<SelectTree>();
		//得到参数组的列表
		StringBuffer groupsql = new StringBuffer("");
		groupsql.append(" select id,title as name,'true' as isExpanded,'true' as isActive");
		groupsql.append(" from dm_indicator_group where isactive = 1");
		list = this.queryObjectList(groupsql.toString(), SelectTree.class);
		//遍历参数组,得到每个参数组的参数的列表,并且选中该设备下参数
		for(SelectTree tree:list){
			List<SelectTree> child = new ArrayList<SelectTree>();
			StringBuffer indiSql = new StringBuffer("");
			indiSql.append(" select a.id,a.title as name,'true' as isExpanded,");
			indiSql.append(" case when b.catalogid is null then 'false' else 'true' end as  selected");
			indiSql.append(" from dm_indicator a ");
			indiSql.append(" left join device_catalog_indicator b on a.id = b.indicatorid and b.catalogid = ").append(model.getId());
			indiSql.append(" where a.isactive = 1");
			indiSql.append(" and a.groupid =").append(tree.getId());
			indiSql.append(" order by a.orderCode");
			child = this.queryObjectList(indiSql.toString(), SelectTree.class);
			if(child!=null&&child.size()>0){
				tree.setChildren(child);
			}
		}
		//删除参数组中没有参数的参数组
		List<SelectTree> listnew = new ArrayList<SelectTree>();
		for(SelectTree tree:list){
			if(tree.getChildren()!=null&&tree.getChildren().size()>0){
				listnew.add(tree);
			}
		}
		return listnew;
	}
	
	/*
	 * 根据设备获得当前设备下的参数列表
	 */
	public List<IndicatorModel> getIndicators4Deivce(DeviceModel model){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupId,a.unitId,a.description,a.isactive");
		sql.append(" from dm_indicator a ,device_catalog_indicator c");
		sql.append(" where a.id = c.indicatorid and a.isactive = 1");
		sql.append(" and c.catalogid =").append(model.getId());
		sql.append(" order by a.orderCode");
		list = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return list;
	}
	
	/*
	 * 根据站点以及设备获得当前展示设备下的参数列表
	 */
	public List<IndicatorModel> getShow4DeivceStation(DeviceModel model,int stationId){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupId,a.unitId,a.description,a.isactive");
		sql.append(" from dm_indicator a ,device_catalog_indicator c,aiot_firstpage_show e");
		sql.append(" where a.id = c.indicatorid and a.isactive = 1");
		sql.append(" and e.wpid =").append(stationId).append(" and e.indicatorid = c.indicatorid and e.deviceId = c.catalogid");
		sql.append(" and c.catalogid =").append(model.getId());
		sql.append(" order by a.orderCode");
		list = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return list;
	}
	
	/*
	 * 以树的形式展示当前站点的所包含设备的所有参数的列表
	 */
	public List<SelectTree> getIndicators4StationDevice(StationModel model){
		//定义返回结果并初始化
				List<SelectTree> result = new ArrayList<SelectTree>();
				SelectTree parent = new SelectTree();
				parent.setName("参数列表");
				parent.setIsActive(true);
				parent.setIsExpanded(true);
				
				List<SelectTree> list = new ArrayList<SelectTree>();
				//得到该站点的设备的列表
				StringBuffer deviceSql = new StringBuffer("");
				deviceSql.append(" select a.id,a.name,'true' as isExpanded,'true' as isActive");
				deviceSql.append(" from device_catalog a,map_awp_device_catalog b");
				deviceSql.append(" where b.device_catalog_id = a.id and b.aiot_watch_point_id = ").append(model.getId());
				deviceSql.append(" order by ordercode ");
				list = this.queryObjectList(deviceSql.toString(), SelectTree.class);
				for(SelectTree device:list){
					List<SelectTree> child = new ArrayList<SelectTree>();
					StringBuffer childSql = new StringBuffer("");
					childSql.append(" select concat(a.id,'#',b.catalogid) as id,a.title as name,'true' as isExpanded,'false' as selected");
					childSql.append(" from dm_indicator a,device_catalog_indicator b");
					childSql.append(" where a.isactive = 1 and a.id = b.indicatorid ");
					childSql.append(" and b.catalogid = ").append(device.getId());
					childSql.append(" order by a.orderCode");
					child = this.queryObjectList(childSql.toString(), SelectTree.class);
					device.setChildren(child);
				}
				//删除其中没有参数的设备
				//默认将第一个参数选中
				int ifselect = 0;
				List<SelectTree> select = new ArrayList<SelectTree>();
				for(SelectTree device:list){
					if(device.getChildren()!=null&&device.getChildren().size()>0){
						if(ifselect==0){
							device.getChildren().get(0).setSelected(true);
						}
						ifselect = 1;
						select.add(device);
					}
				}
				parent.setChildren(select);
				result.add(parent);
				return result;
	}
	/*
	 * 以树的形式展示当前站点的所包含有展示权限的设备以及有展示权限的参数的列表
	 */
	public List<SelectTree> getIndicators4Pollu(StationModel model){
		//定义返回结果并初始化
		List<SelectTree> result = new ArrayList<SelectTree>();
		SelectTree parent = new SelectTree();
		parent.setName("参数列表");
		parent.setIsActive(true);
		parent.setIsExpanded(true);
		List<SelectTree> list = new ArrayList<SelectTree>();
		//得到该站点的设备的列表
		StringBuffer deviceSql = new StringBuffer("");
		deviceSql.append(" select a.id,a.name,'true' as isExpanded,'true' as isActive");
		deviceSql.append(" from view_show_station_device a,(select distinct wpid,deviceid from aiot_pollutant_indicator where deviceid <> 18) b");
		deviceSql.append(" where a.stationid = ").append(model.getId());
		deviceSql.append(" and a.stationid = b.wpid and a.id = b.deviceid");
		deviceSql.append(" order by ordercode ");
		list = this.queryObjectList(deviceSql.toString(), SelectTree.class);
		for(SelectTree device:list){
			List<SelectTree> child = new ArrayList<SelectTree>();
			StringBuffer childSql = new StringBuffer("");
			childSql.append(" select concat(a.id,'#',b.deviceid) as id,a.title as name,'true' as isExpanded,'false' as selected");
			childSql.append(" from dm_indicator a,view_stationid_deviceid_indicatorid b,aiot_pollutant_indicator c");
			childSql.append(" where a.isactive = 1 and a.id = b.indicatorid ");
			childSql.append(" and a.code = c.indicatorcode and b.stationid = c.wpid and b.deviceid = c.deviceid");
			childSql.append(" and c.type = 1");
			childSql.append(" and b.deviceid = ").append(device.getId());
			childSql.append(" and b.stationid = ").append(model.getId());
			childSql.append(" order by a.orderCode");
			child = this.queryObjectList(childSql.toString(), SelectTree.class);
			device.setChildren(child);
		}
		//删除其中没有参数的设备
		//默认将第一个参数选中
		int ifselect = 0;
		List<SelectTree> select = new ArrayList<SelectTree>();
		for(SelectTree device:list){
			if(device.getChildren()!=null&&device.getChildren().size()>0){
				if(ifselect==0){
					device.getChildren().get(0).setSelected(true);
				}
				ifselect = 1;
				select.add(device);
			}
		}
		parent.setChildren(select);
		result.add(parent);
		return result;
	}
	
	/*
	 * 以树的形式展示当前站点需要计算入海污染量的参数的列表
	 */
	public List<SelectTree> getIndicators4StationDevice4Show(StationModel model){
		//定义返回结果并初始化
				List<SelectTree> result = new ArrayList<SelectTree>();
				SelectTree parent = new SelectTree();
				parent.setName("参数列表");
				parent.setIsActive(true);
				parent.setIsExpanded(true);
				
				List<SelectTree> list = new ArrayList<SelectTree>();
				//得到该站点的设备的列表
				StringBuffer deviceSql = new StringBuffer("");
				deviceSql.append(" select a.id,a.name,'true' as isExpanded,'true' as isActive");
				deviceSql.append(" from view_show_station_device a");
				deviceSql.append(" where a.stationid = ").append(model.getId());
				deviceSql.append(" order by ordercode ");
				list = this.queryObjectList(deviceSql.toString(), SelectTree.class);
				for(SelectTree device:list){
					List<SelectTree> child = new ArrayList<SelectTree>();
					StringBuffer childSql = new StringBuffer("");
					childSql.append(" select concat(a.id,'#',b.deviceid) as id,a.title as name,'true' as isExpanded,'false' as selected");
					childSql.append(" from dm_indicator a,view_stationid_deviceid_indicatorid b");
					childSql.append(" where a.isactive = 1 and a.id = b.indicatorid ");
					childSql.append(" and b.deviceid = ").append(device.getId());
					childSql.append(" and b.stationid = ").append(model.getId());
					childSql.append(" order by a.orderCode");
					child = this.queryObjectList(childSql.toString(), SelectTree.class);
					device.setChildren(child);
				}
				//删除其中没有参数的设备
				//默认将第一个参数选中
				Boolean checked = true;
				int ifselect = 0;
				for(SelectTree device:list){
					if(device.getChildren()==null||device.getChildren().size()<1){
						list.remove(device);
					}else{
						if(ifselect==0){
							device.getChildren().get(0).setSelected(true);
						}
						ifselect = 1;
						checked = false;
					}
				}
				parent.setChildren(list);
				result.add(parent);
				return result;
	}
	
	/*
	 * 获得参数以及全称的MAP
	 */
	public Map<String, Object> getIndicatorTitleMap(){
		Map<String, Object> indicatorNameMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.code as id,concat(a.title,if(b.logo is not null and length(b.logo)>0,concat('(',b.logo,')'),'')) as name");
		sql.append(" from dm_indicator a left join g_unit b");
		sql.append(" on a.unitId = b.id");
		sql.append(" where a.isactive = 1 and b.isactive = 1");
		indicatorNameMap = this.queryForMapKeyValue(sql.toString());
		return indicatorNameMap;
	}
	
	/*
	 * 获得参数以及单位的MAP
	 */
	public Map<String,Object> getIndicatorUnitMap(){
		Map<String, Object> indicatorUnitMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.code as id,if(b.logo is not null,b.logo,'') as name");
		sql.append(" from dm_indicator a left join g_unit b");
		sql.append(" on a.unitId = b.id");
		sql.append(" where a.isactive = 1 and b.isactive = 1");
		indicatorUnitMap = this.queryForMapKeyValue(sql.toString());
		return indicatorUnitMap;
	}
	
	/*
	 * 获得站点内的参数列表
	 */
	public List<IndicatorModel> getIndicatorsByStation(StationModel model){
		List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupid,b.title as groupName,");
		sql.append(" a.unitId,c.logo as unitName,a.description,a.isactive,d.value as isactiveName,a.orderCode");
		sql.append(" from dm_indicator a");
		sql.append(" left join dm_indicator_group b on a.groupid = b.id");
		sql.append(" left join g_unit c on a.unitId = c.id");
		sql.append(" left join sys_public d on d.parentcode = '0004' and a.isactive = d.classid,");
		sql.append(" device_catalog_indicator e,map_awp_device_catalog f");
		sql.append(" where a.isactive = 1");
		sql.append(" and a.id = e.indicatorid and e.catalogid = f.device_catalog_id");
		sql.append(" and f.aiot_watch_point_id = ").append(model.getId());
		indicators = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return indicators;
	}
	
	/*
	 * 根据分组信息查询有效参数列表
	 */
	public List<IndicatorModel> getIndicatorsByGroup(IndicatorGroupModel model){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupId,c.title as groupName,a.unitId,a.description,a.isactive");
		sql.append(" from dm_indicator a ,dm_indicator_group c");
		sql.append(" where a.groupId = c.id and a.isactive = 1");
		sql.append(" and c.isactive = 1");
		sql.append(" and c.id =").append(model.getId());
		sql.append(" order by a.orderCode");
		list = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return list;
	}
	
	/*
	 * 根据站点列表查询出共同的参数列表
	 * 以selecttree的形式展示
	 */
	public List<SelectTree> getIndicatorTreesByStations(List<StationModel> stations){
		List<SelectTree> result = new ArrayList<>();
		//初始化查询结果
		SelectTree parent = new SelectTree();
		parent.setName("参数列表");
		parent.setIsActive(false);
		parent.setIsExpanded(true);
		//获得所有的参数组
		List<SelectTree> groups = new ArrayList<>();
		List<SelectTree> groupList = new ArrayList<>();
		StringBuffer groupSql = new StringBuffer("");
		groupSql.append(" select id,title as name,'true' as isExpanded,'false' as selected");
		groupSql.append(" from dm_indicator_group where isactive = 1");
		groupList = this.queryObjectList(groupSql.toString(), SelectTree.class);
		//处理站点集合中的参数
		StringBuffer selectSql = new StringBuffer(" select a.code as id,a.title as name ");
		StringBuffer fromSql = new StringBuffer(" from dm_indicator a,device_catalog b,dm_indicator_group c");
		StringBuffer whereSql = new StringBuffer(" where a.isactive = 1 and a.groupid = c.id ");
		for(int i=0;i<stations.size();i++){
			StationModel station = stations.get(i);
			fromSql.append(" ,view_stationid_deviceid_indicatorid v").append(i);
			whereSql.append(" and v").append(i).append(".stationid =").append(station.getId());
			whereSql.append(" and v").append(i).append(".indicatorid = a.id and v").append(i).append(".deviceid = b.id");
		}
		//添加排序语句
		StringBuffer orderSql = new StringBuffer(" order by a.ordercode");
		//设置默认选中
		Boolean selected = true;
		//遍历参数组
		for(SelectTree group:groupList){
			StringBuffer sql = new StringBuffer("");
			sql.append(selectSql).append(fromSql).append(whereSql).append(" and c.id =").append(group.getId()).append(orderSql);
			List<SelectTree> indicatorList = new ArrayList<SelectTree>();
			indicatorList = this.queryObjectList(sql.toString(), SelectTree.class);
			if(indicatorList!=null&&indicatorList.size()>0){
				if(selected){
					for(SelectTree indicator:indicatorList){
						if(selected){
							indicator.setSelected(true);
							selected = false;
						}
					}
				}
				group.setChildren(indicatorList);
				groups.add(group);
			}
		}
		parent.setChildren(groups);
		result.add(parent);
		return result;
	}
	/*
	 * 根据参数编码,获得参数的信息
	 */
	public IndicatorModel getIndicatorByCode(String indicatorCode){
		IndicatorModel  indicator = new IndicatorModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupid,b.title as groupName,");
		sql.append(" a.unitId,c.logo as unitName,a.description,a.isactive,d.value as isactiveName,a.orderCode");
		sql.append(" from dm_indicator a");
		sql.append(" left join dm_indicator_group b on a.groupid = b.id");
		sql.append(" left join g_unit c on a.unitId = c.id");
		sql.append(" left join sys_public d on d.parentcode = '0004' and a.isactive = d.classid");
		sql.append(" where a.code = '").append(indicatorCode).append("'");
		indicator= this.queryObject(sql.toString(), IndicatorModel.class);
		return indicator;
	}
	
	/*
	 * 根据功能区选择关联的参数
	 */
	public List<ZTreeModel> getIndicatorZtreeList4Domain(DomainModel model){
		//初始化返回结果
		List<ZTreeModel> list = new ArrayList<>();
		//获得参数的列表
		StringBuffer indiSql = new StringBuffer("");
		indiSql.append(" select a.code as id,a.groupid as pid,a.title as name,");
		indiSql.append(" case when b.domainid is null then 'false' else 'true' end as checked");
		indiSql.append(" from dm_indicator a left join sys_domain_indicator b");
		indiSql.append(" on a.code = b.indicatorcode ");
		indiSql.append(" and b.domainid = ").append(model.getId());
		indiSql.append(" where isactive = 1");
		List<ZTreeModel> indiLis = new ArrayList<ZTreeModel>();
		indiLis = this.queryObjectList(indiSql.toString(), ZTreeModel.class);
		//根据参数的列表,获得参数组的列表
		//遍历参数的列表,
		StringBuffer groupin = new StringBuffer("(0");
		for(ZTreeModel ztree:indiLis){
			groupin.append(",").append(ztree.getpId());
		}
		groupin.append(")");
		StringBuffer groupSql = new StringBuffer("");
		groupSql.append(" select id,'G001' as pid,title as name,'true' as nocheck");
		groupSql.append(" from dm_indicator_group where isactive = 1");
		groupSql.append(" and  id in ").append(groupin);
		List<ZTreeModel> groupList = new ArrayList<ZTreeModel>();
		groupList = this.queryObjectList(groupSql.toString(), ZTreeModel.class);
		//
		ZTreeModel all = new ZTreeModel();
		all.setId("G001");
		all.setName("参数列表");
		all.setNocheck(true);
		all.setOpen(true);
		list.add(all);
		list.addAll(groupList);
		list.addAll(indiLis);
		
		return list;
	}
	
	/**
	 * 根据站点以及设备查询出权限下的参数列表
	 * @param model
	 * @return
	 */
	public List<IndicatorModel> getIndicatorsByStationDevcie(StationModel station,DeviceModel device){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupid,b.title as groupName,");
		sql.append(" a.unitId,c.logo as unitName,a.description,a.isactive,d.value as isactiveName,a.orderCode,a.standardcode");
		sql.append(" from dm_indicator a");
		sql.append(" left join dm_indicator_group b on a.groupid = b.id");
		sql.append(" left join g_unit c on a.unitId = c.id");
		sql.append(" left join sys_public d on d.parentcode = '0004' and a.isactive = d.classid");
		sql.append(" ,view_stationid_deviceid_indicatorid vs");
		sql.append(" where a.id = vs.indicatorid ");
		//添加查询条件
		sql.append(" and vs.stationid = ").append(station.getId());
		sql.append(" and vs.deviceid = ").append(device.getId());
		//增加排序
		sql.append(" order by a.orderCode");
		list = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return list;
	}
	
	/*
	 * 得到所有有效参数的树的列表
	 * 选中传入参数,
	 * 如果没有传入参数,则选中第一个参数
	 */
	public List<SelectTree> getAllIndicatorListTree4Single(IndicatorModel model){
		List<SelectTree> list = new ArrayList<SelectTree>();
		//得到参数组的列表
		StringBuffer groupsql = new StringBuffer("");
		groupsql.append(" select id,title as name,'true' as isExpanded,'true' as isActive");
		groupsql.append(" from dm_indicator_group where isactive = 1");
		list = this.queryObjectList(groupsql.toString(), SelectTree.class);
		//初始化定义选中标签
		Boolean ifselect = true;
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0) {
			ifselect=false;
		}
		//遍历参数组,得到每个参数组的参数的列表,并且选中该设备下参数
		for(SelectTree tree:list){
			List<SelectTree> child = new ArrayList<SelectTree>();
			StringBuffer indiSql = new StringBuffer("");
			indiSql.append(" select a.code as id,a.title as name,'true' as isExpanded,");
			if(model!=null&&model.getCode()!=null&&model.getCode().length()>0) {
				indiSql.append(" case when a.code ='").append(model.getCode()).append("' then 'true' else 'false' end as  selected");
			}else {
				indiSql.append(" 'false' as  selected");
			}
			indiSql.append(" from dm_indicator a ");
			indiSql.append(" where a.isactive = 1");
			indiSql.append(" and a.groupid =").append(tree.getId());
			indiSql.append(" order by a.orderCode");
			child = this.queryObjectList(indiSql.toString(), SelectTree.class);
			
			if(child!=null&&child.size()>0){
				if(ifselect) {
					child.get(0).setSelected(true);
					ifselect=false;
				}
				tree.setChildren(child);
			}
		}
		//删除参数组中没有参数的参数组
		List<SelectTree> listnew = new ArrayList<SelectTree>();
		for(SelectTree tree:list){
			if(tree.getChildren()!=null&&tree.getChildren().size()>0){
				listnew.add(tree);
			}
		}
		return listnew;
	}
	
}
