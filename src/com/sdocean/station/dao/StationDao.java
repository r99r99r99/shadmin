package com.sdocean.station.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.DoubleModel;
import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationStatusModel;
import com.sdocean.system.dao.SystemDao;
import com.sdocean.system.service.SystemService;
import com.sdocean.users.model.SysUser;

@Component
public class StationDao extends OracleEngine {
	
	@Resource
	SystemDao systemDao;
	
	@Resource
	SystemService systemService;
	
	/*
	 * 查询出符合条件的站点的列表
	 */
	public List<StationModel> getStationList(StationModel model){
		List<StationModel> results = new ArrayList<StationModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.stationtype_id,d.name as stationTypeName,d.icon,a.region_id,e.name as regionName,a.brief,a.detail,"); 
		sql.append(" a.pic,a.isactive,b.value as isactiveName,a.waterType,c.value as waterTypeName,a.orderCode,");
		sql.append(" group_concat(k.title,'|') as deviceNames,");
		sql.append(" a.companyid,f.name as companyName,");
		sql.append(" a.ifsms,case a.ifsms when 1 then '启用' else '禁用' end as ifsmsName,");
		sql.append(" l.lastMetaDate,a.standard");
		sql.append(" from aiot_watch_point a "); 
		sql.append(" left join sys_public b on a.isactive = b.classid  and b.parentcode = '0004'"); 
		sql.append(" left join sys_public c on a.watertype = c.classid  and c.parentcode = '0005'"); 
		sql.append(" left join g_station_type d on a.stationtype_id = d.id and d.isactive = 1");
		sql.append(" left join g_region e on a.region_id = e.id");
		sql.append(" left join g_company f on a.companyid = f.code");
		sql.append(" left join (select m.id,m.title,n.aiot_watch_point_id as stationid from dm_indicator m,map_awp_device_catalog n where m.id = n.device_catalog_id and isactive = 1 ) k ");
		sql.append(" on a.id = k.stationid ");
		sql.append(" left join (select wpid,max(endtime) as lastMetaDate from aiot_meta_table where type = 1 and isactive = 1 group by wpid) l on a.id = l.wpid");
		sql.append(" where 1 = 1");
		//增加模糊条件查询
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and (a.code like '%").append(model.getCode()).append("%' or");
			sql.append("      a.title like '%").append(model.getCode()).append("%' ) ");
		}
		//增加状态查询
		if(model!=null&&model.getIsactive()<2){
			sql.append(" and a.isactive =").append(model.getIsactive());
		}
		sql.append(" group by a.id");
		//增加排序规则
		sql.append(" order by orderCode ");
		results = this.queryObjectList(sql.toString(), StationModel.class);
		return results;
	}
	/*
	 * 根据站点ids获得站点的集合
	 */
	public List<StationModel> getStationListByIds(String ids){
		List<StationModel> results = new ArrayList<StationModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.stationtype_id,d.name as stationTypeName,a.region_id,e.name as regionName,a.brief,a.detail,"); 
		sql.append(" a.pic,a.isactive,b.value as isactiveName,a.waterType,c.value as waterTypeName,a.orderCode,");
		sql.append(" group_concat(k.title,'|') as deviceNames,");
		sql.append(" a.companyid,f.name as companyName,");
		sql.append(" a.ifsms,case a.ifsms when 1 then '启用' else '禁用' end as ifsmsName,");
		sql.append(" l.lastMetaDate,a.standard");
		sql.append(" from aiot_watch_point a "); 
		sql.append(" left join sys_public b on a.isactive = b.classid  and b.parentcode = '0004'"); 
		sql.append(" left join sys_public c on a.watertype = c.classid  and c.parentcode = '0005'"); 
		sql.append(" left join g_station_type d on a.stationtype_id = d.id and d.isactive = 1");
		sql.append(" left join g_region e on a.region_id = e.id");
		sql.append(" left join g_company f on a.companyid = f.code");
		sql.append(" left join (select m.id,m.title,n.aiot_watch_point_id as stationid from dm_indicator m,map_awp_device_catalog n where m.id = n.device_catalog_id and isactive = 1 ) k ");
		sql.append(" on a.id = k.stationid ");
		sql.append(" left join (select wpid,max(endtime) as lastMetaDate from aiot_meta_table where type = 1 and isactive = 1 group by wpid) l on a.id = l.wpid");
		sql.append(" where 1 = 1");
		sql.append(" and a.id in (").append(ids).append(")");
		sql.append(" group by a.id");
		//增加排序规则
		sql.append(" order by orderCode ");
		results = this.queryObjectList(sql.toString(), StationModel.class);
		return results;
	}
	
	/*
	 * 保存修改的站点信息
	 * 
	 */
	public Result saveStaionChange(StationModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append("update aiot_watch_point set code=?,title=?,latitude=?,longitude=?,station_gateway=?,stationtype_id=?,region_id=?,brief=?,detail=?,pic=?,isactive=?,watertype=?,ordercode=?,companyId=?,ifsms=?");
		sql.append(" where id = ?");
		Object[] params = new Object[]{
			model.getCode(),model.getTitle(),model.getLatitude(),model.getLongitude(),model.getStation_gateway(),
			model.getStationtype_id(),model.getRegion_id(),model.getBrief(),model.getDetail(),model.getPic(),
			model.getIsactive(),model.getWaterType(),model.getOrderCode(),model.getCompanyId(),model.getIfsms(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
		}
		
		this.saveStationDevice(model);
		return result;
	}
	/*
	 * 保存新增站点信息
	 */
	public Result saveNewStation(StationModel model){
			//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer();
		sql.append("insert into aiot_watch_point(code,title,latitude,longitude,station_gateway,stationtype_id,region_id,brief,detail,pic,isactive,watertype,ordercode,companyId,ifsms) ");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getTitle(),model.getLatitude(),model.getLongitude(),
				model.getStation_gateway(),model.getStationtype_id(),model.getRegion_id(),
				model.getBrief(),model.getDetail(),model.getPic(),model.getIsactive(),
				model.getWaterType(),model.getOrderCode(),model.getCompanyId(),model.getIfsms()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
			return result;
		}
		String last = "SELECT LAST_INSERT_ID()";
		int lastId = this.queryForInt(last, null);
		model.setId(lastId);
		this.saveStationDevice(model);	
		return result;	
	}
	/*
	 * 得到当前角色下的站点列表
	 */
	public List<ZTreeModel> getStationListByRole(RoleModel role){
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id ,a.region_id as pid,a.title as name,");
		sql.append(" case when b.role_id is null then 'false' else 'true' end as checked");
		sql.append(" from aiot_watch_point a left join sys_role_station b");
		sql.append(" on a.id = b.station_id and b.role_id = ").append(role.getId());
		sql.append(" where a.isactive = 1");
		list = this.queryObjectList(sql.toString(), ZTreeModel.class);
		return list;
	}
	
	/*
	 * 得到当前GPS角色下的站点列表
	 */
	public List<ZTreeModel> getStationListByGpsRole(RoleModel role){
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id ,a.region_id as pid,a.title as name,");
		sql.append(" case when b.role_id is null then 'false' else 'true' end as checked");
		sql.append(" from aiot_watch_point a left join sys_role_gps_station b");
		sql.append(" on a.id = b.station_id and b.role_id = ").append(role.getId());
		sql.append(" where a.isactive = 1");
		list = this.queryObjectList(sql.toString(), ZTreeModel.class);
		return list;
	}
	/*
	 * 更新站点内设备列表
	 */
	public void saveStationDevice(StationModel model){
		//删除站点内设备
		StringBuffer deleSql = new StringBuffer("");
		deleSql.append(" delete from map_awp_device_catalog where aiot_watch_point_id = ").append(model.getId());
		this.update(deleSql.toString(), null);
		
		String[] ids = model.getDeviceIds().split(",");
		StringBuffer val = new StringBuffer("(0,0)");
		for(String id:ids){
			val.append(",(").append(model.getId()).append(",").append(id).append(")");
		}
		StringBuffer insertSql = new StringBuffer("");
		insertSql.append(" insert into map_awp_device_catalog(aiot_watch_point_id,device_catalog_id) values").append(val);
		insertSql.append(" ON DUPLICATE KEY UPDATE device_catalog_id = device_catalog_id ");
		this.update(insertSql.toString(), null);
	}
	
	/*
	 * 获得当前用户的站点权限的列表
	 */
	public List<StationModel> getStations4User(SysUser model){
		List<StationModel> stations = new ArrayList<StationModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.companyid,a.standard,gw.classname as standardName,");
		sql.append(" a.stationtype_id,a.region_id,m.name as regionName,a.brief,a.detail,a.pic,a.isactive,a.watertype,n.value as waterTypeName,a.ordercode,g.icon,g.name as stationTypeName");
		sql.append(" from aiot_watch_point a left join g_station_type g on a.stationtype_id = g.id and g.isactive = 1");
		sql.append(" left join g_waterstandard_config gw on a.watertype=gw.typeid and a.standard=gw.classid");
		sql.append(" ,sys_role_station b,sys_role c,sys_role_user d,g_region m,sys_public n");
		sql.append(" where a.id = b.station_id and b.role_id = c.id and c.isactive = 1 and a.isactive = 1 and a.region_id = m.id");
		sql.append(" and n.parentcode='0005' and a.watertype = n.classid");
		sql.append(" and c.id = d.role_id and d.user_id = ").append(model.getId());
		stations = this.queryObjectList(sql.toString(), StationModel.class);
		return stations;
	}
	
	/*
	 * 获得当前用户的GPS站点权限的列表
	 */
	public List<StationModel> getGpsStations4User(SysUser model){
		List<StationModel> stations = new ArrayList<StationModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.companyid,a.warndistance,a.standard,");
		sql.append(" a.stationtype_id,a.region_id,m.name as regionName,a.brief,a.detail,a.pic,a.isactive,a.watertype,n.value as waterTypeName,a.ordercode,g.icon,g.name as stationTypeName");
		sql.append(" from aiot_watch_point a left join g_station_type g on a.stationtype_id = g.id and g.isactive = 1,sys_role_gps_station b,sys_role c,sys_role_user d,g_region m,sys_public n");
		sql.append(" where a.id = b.station_id and b.role_id = c.id and c.isactive = 1 and a.isactive = 1 and a.region_id = m.id");
		sql.append(" and n.parentcode='0005' and a.watertype = n.classid");
		sql.append(" and c.id = d.role_id and d.user_id = ").append(model.getId());
		stations = this.queryObjectList(sql.toString(), StationModel.class);
		return stations;
	}
	
	/*
	 * 查询该用户上次登录的站点
	 */
	public StationModel getStation4Last(SysUser model){
		StationModel station = new StationModel();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,");
		sql.append(" a.stationtype_id,a.region_id,a.brief,a.detail,a.pic,a.isactive,a.watertype,a.ordercode,a.standard");
		sql.append(" from aiot_watch_point a,sys_role_station b,sys_role c,sys_role_user d,");
		sql.append(" (select userid,wpid from sys_station_log where userid =").append(model.getId());
		sql.append(" order by stationtime desc limit 1) m");
		sql.append(" where a.id = b.station_id and b.role_id = c.id and c.isactive = 1");
		sql.append(" and c.id = d.role_id and d.user_id = m.userid ");
		sql.append(" and a.id = m.wpid limit 1");
		station = this.queryObject(sql.toString(), StationModel.class);
		return station;
	}
	/*
	 * 将此次登录站点信息保存到数据库中
	 */
	public void saveStationLog(SysUser user,StationModel station,int type){
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_station_log(userid,stationtime,wpid,type) values(?,?,?,?)");
		Object[] params = new Object[]{
				user.getId(),new Date(),station.getId(),type
		};
		this.update(sql.toString(), params);
	}
	
	/*
	 * 根据站点ID,得到站点信息
	 */
	public StationModel getStationById(int stationId){
		StationModel station = new StationModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.stationtype_id,d.name as stationTypeName,a.region_id,e.name as regionName,a.brief,a.detail,"); 
		sql.append(" a.pic,a.isactive,b.value as isactiveName,a.waterType,c.value as waterTypeName,a.orderCode,");
		sql.append(" group_concat(k.title,'|') as deviceNames,a.standard,gw.classname as standardName");
		sql.append(" from aiot_watch_point a "); 
		sql.append(" left join sys_public b on a.isactive = b.classid  and b.parentcode = '0004'"); 
		sql.append(" left join sys_public c on a.watertype = c.classid  and c.parentcode = '0005'"); 
		sql.append(" left join g_station_type d on a.stationtype_id = d.id and d.isactive = 1");
		sql.append(" left join g_region e on a.region_id = e.id");
		sql.append(" left join (select m.id,m.title,n.aiot_watch_point_id as stationid from dm_indicator m,map_awp_device_catalog n where m.id = n.device_catalog_id and isactive = 1 ) k ");
		sql.append(" on a.id = k.stationid ");
		sql.append(" left join g_waterstandard_config gw");
		sql.append(" on a.watertype = gw.typeid and a.standard = gw.classid");
		sql.append(" where 1 = 1");
		sql.append(" and a.id = ").append(stationId);
		station = this.queryObject(sql.toString(), StationModel.class);
		return station;
	}
	/*
	 * 根据站点得到站点的设备列表
	 */
	public List<ZTreeModel> getDeviceZTree4Station(StationModel station){
		List<ZTreeModel> devices = new ArrayList<ZTreeModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct concat('dl1_',a.device_catalog_id) as id,a.aiot_watch_point_id as pid,b.name,'_self' as target");
		sql.append(" from map_awp_device_catalog a,device_catalog b");
		sql.append(" where a.device_catalog_id = b.id and a.aiot_watch_point_id = ").append(station.getId());
		sql.append(" order by b.ordercode");
		devices = this.queryObjectList(sql.toString(), ZTreeModel.class);
		return devices;
	}
	/*
	 * 根据用户读取用户的站点权限,
	 * 并转化成SelectTree的形式
	 * 默认选中第一个站点
	 */
	public List<SelectTree> getStationTreeListByUser(SysUser user){
		//定义查询结果,并进行初始化
		List<SelectTree> result = new ArrayList<SelectTree>();
		SelectTree parent = new SelectTree();
		parent.setName("站点列表");
		parent.setIsActive(true);
		parent.setIsExpanded(true);
		//得到该用户权限内的站点的列表
		List<SelectTree> list = new ArrayList<SelectTree>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct id,title as name");
		sql.append(" from view_role_user_station");
		sql.append(" where userid = ").append(user.getId());
		list = this.queryObjectList(sql.toString(), SelectTree.class);
		//默认选中第一个
		Boolean selected = true;
		for(SelectTree station:list){
			if(selected){
				station.setSelected(true);
				selected = false;
			}
		}
		parent.setChildren(list);
		result.add(parent);
		return result;
	}
	
	/*
	 * 得到站点的状态信息
	 */
	public StationStatusModel getStationStatus(StationModel station){
		//初始化站点状态
		StationStatusModel stationStatus = new StationStatusModel();
		stationStatus.setStationId(station.getId());
		stationStatus.setLatitude(station.getLatitude());
		stationStatus.setLongitude(station.getLongitude());
		String icon = station.getIcon();
		Double ifconn = (double) 0;
		//判断当前站点是否连接
		DoubleModel ifConn = systemDao.getDataByStationIdIndicatorCode(station.getId(), "ifConn");
		if(ifConn!=null&&ifConn.getResult()>0){
			icon = "green_"+icon;
			ifconn = (double) 1;
		}else{
			icon = "red_"+icon;
		}
		/*if(ss!=null&&ss.getIfConn()>0){
			icon = "green_"+icon;
		}else{
			icon = "red_"+icon;
		}*/
		DoubleModel Latitude = systemDao.getDataByStationIdIndicatorCode(station.getId(), "GPS_Latitude");
		DoubleModel Longitude = systemDao.getDataByStationIdIndicatorCode(station.getId(), "GPS_Longitude");
		stationStatus.setLatitude(Latitude.getResult());
		stationStatus.setLongitude(Longitude.getResult());
		Double distance = systemService.getDistance(Latitude.getResult(), Longitude.getResult(), station.getLatitude(), station.getLongitude());
		stationStatus.setIfConnIcon(icon);
		stationStatus.setIfConn(ifconn);
		stationStatus.setDistance(distance);
		return stationStatus;
	}
	
	/*
	 * 得到当前功能区下的站点列表,以站点树的形式展示
	 */
	public List<ZTreeModel> getStationTreeByDomain(DomainModel model){
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id ,a.region_id as pid,a.title as name,");
		sql.append(" case when b.domainid is null then 'false' else 'true' end as checked");
		sql.append(" from aiot_watch_point a left join sys_domain_station b");
		sql.append(" on a.id = b.stationid and b.domainid = ").append(model.getId());
		sql.append(" where a.isactive = 1");
		list = this.queryObjectList(sql.toString(), ZTreeModel.class);
		return list;
	}
	/*
	 * 得到当前功能区下的站点列表
	 */
	public List<StationModel> getStationListByDomain(DomainModel model){
		List<StationModel> stations = new ArrayList<StationModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.stationtype_id,d.name as stationTypeName,d.icon,a.region_id,e.name as regionName,a.brief,a.detail,"); 
		sql.append(" a.pic,a.isactive,b.value as isactiveName,a.waterType,c.value as waterTypeName,a.orderCode,");
		sql.append(" group_concat(k.title,'|') as deviceNames,");
		sql.append(" a.companyid,f.name as companyName,");
		sql.append(" a.ifsms,case a.ifsms when 1 then '启用' else '禁用' end as ifsmsName,");
		sql.append(" l.lastMetaDate,a.standard");
		sql.append(" from aiot_watch_point a "); 
		sql.append(" left join sys_public b on a.isactive = b.classid  and b.parentcode = '0004'"); 
		sql.append(" left join sys_public c on a.watertype = c.classid  and c.parentcode = '0005'"); 
		sql.append(" left join g_station_type d on a.stationtype_id = d.id and d.isactive = 1");
		sql.append(" left join g_region e on a.region_id = e.id");
		sql.append(" left join g_company f on a.companyid = f.code");
		sql.append(" left join (select m.id,m.title,n.aiot_watch_point_id as stationid from dm_indicator m,map_awp_device_catalog n where m.id = n.device_catalog_id and isactive = 1 ) k ");
		sql.append(" on a.id = k.stationid ");
		sql.append(" left join (select wpid,max(endtime) as lastMetaDate from aiot_meta_table where type = 1 and isactive = 1 group by wpid) l on a.id = l.wpid");
		sql.append(" ,sys_domain_station sds");
		sql.append(" where a.id = sds.stationid");
		sql.append(" and sds.domainId = ").append(model.getId());
		stations = this.queryObjectList(sql.toString(), StationModel.class);
		return stations;
	}
}

