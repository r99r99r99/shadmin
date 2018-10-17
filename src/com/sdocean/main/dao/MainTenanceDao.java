package com.sdocean.main.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.firstpage.model.SystemModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.main.model.MainTainFile;
import com.sdocean.main.model.MainTainModel;
import com.sdocean.main.model.MainTenance;
import com.sdocean.main.model.MainTenanceFile;
import com.sdocean.station.model.StationModel;

@Component
public class MainTenanceDao extends OracleEngine {
	
	/*
	 * 为站点维护上报提供查询结果集
	 */
	public List<MainTainModel> getMainList4StationMainEdit(MainTainModel model,List<StationModel> stations){
		List<MainTainModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,c.title as stationName,a.begintime,a.endtime,");
		sql.append(" a.userid,b.realname as username,a.status,");
		sql.append(" k.realname,k.pathname,");
		sql.append(" case a.status when 1 then '维护中' when 2 then '维护结束' when 3 then '已打印' else '未知状态' end as statusname");
		sql.append(" from aiot_maintain a left join aiot_maintain_file k on a.id = k.mainid,sys_user b,aiot_watch_point c");
		sql.append(" where a.userid = b.id");
		sql.append(" and a.stationid = c.id ");
		//添加查询条件
		if(model.getStationId()>0){
			sql.append(" and a.stationid = ").append(model.getStationId());
		}
		if(model.getBeginTime()!=null&&model.getBeginTime().length()>0){
			sql.append(" and ((a.endtime >= '").append(model.getBeginTime()).append("' or a.endtime is null)");
			if(model.getEndTime()!=null&&model.getEndTime().length()>0){
				sql.append(" and a.begintime <= '").append(model.getEndTime()).append("' )");
			}else{
				sql.append(" and 1=1)");
			}
		}else{
			sql.append(" and (");
			if(model.getEndTime()!=null&&model.getEndTime().length()>0){
				sql.append(" a.begintime <= '").append(model.getEndTime()).append("' )");
			}else{
				sql.append(" 1=1)");
			}
		}
		//添加排序
		sql.append(" order by c.ordercode,a.begintime desc");
		list = this.queryObjectList(sql.toString(), MainTainModel.class);
		return list;
	}
	
	//保存新增维护上报并得到该条记录的ID值
	public Result saveNewMainEdit(MainTainModel model){
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		StringBuffer sql = new StringBuffer("");
		
		//判断维护状态
		if(model.getEndTime()!=null&&model.getEndTime().length()>0){
			model.setStatus(2);
		}else{
			model.setStatus(1);
		}
		Object[] params = new Object[]{
				model.getStationId(),model.getBeginTime(),
				model.getUserId(),model.getStatus()
		};
		if(model.getEndTime()!=null&&model.getEndTime().length()>0){
			params = new Object[]{
					model.getStationId(),model.getBeginTime(),
					model.getUserId(),model.getStatus(),model.getEndTime()
			};
		}
		sql.append(" insert into aiot_maintain(stationid,begintime,userid,status");
		if(model.getEndTime()!=null&&model.getEndTime().length()>0){
			sql.append(",endtime");
		}
		sql.append(")");
		sql.append(" values(?,?,?,?");
		if(model.getEndTime()!=null&&model.getEndTime().length()>0){
			sql.append(",?");
		}
		sql.append(")");
		int res = 0;
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("新增失败");
			result.setRes(res);
			return result;
		}
		//查询出刚才插入数据的id
		String csql = "select last_insert_id()";
		try {
			res = this.queryForInt(csql, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		result.setRes(res);
		
		//执行配置设备
		Result rs = this.saveMainDevices(res, model.getDeviceIds());
		if(rs.getResult()==rs.FAILED){
			result.setResult(rs.getResult());
			result.setMessage(rs.getMessage());
			return result;
		}
		return result;
	}
	/*
	 * 保存修改维护上报,
	 */
	public Result saveChangeMainEdit(MainTainModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		StringBuffer sql = new StringBuffer("");
		//判断当前设备的运行状态,如果填写完维护结束时间,那运行状态为维护结束,
		if(model.getEndTime()!=null&&model.getEndTime().length()>0){
			model.setStatus(2);
		}else{
			model.setStatus(1);
		}
		Object[] params = new Object[]{
				model.getStationId(),model.getBeginTime(),
				model.getStatus(),model.getId()
		};
		
		sql.append(" update aiot_maintain set stationid=?,begintime=?,status=?");
		if(model.getEndTime()!=null&&model.getEndTime().length()>0){
			sql.append(",endtime=?");
			params = new Object[]{
					model.getStationId(),model.getBeginTime(),
					model.getStatus(),model.getEndTime(),model.getId()
			};
		}
		
		sql.append(" where id=?");
		int res = 0;
		//将修改后的ID返回
				result.setRes(model.getId());
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			result.setMessage("修改失败");
			result.setResult(Result.FAILED);
			return result;
		}
		
		//执行配置设备
		Result rs = this.saveMainDevices(model.getId(), model.getDeviceIds());
		if(rs.getResult()==rs.FAILED){
			result.setResult(rs.getResult());
			result.setMessage(rs.getMessage());
			return result;
		}
		
		return result;
	}
	
	
	/*
	 * 根据站点设备维护ID,以及文件列表,保存文件数据到附表
	 */
	public void saveFileByMain(MainTainModel main,List<MainTenanceFile> files){
		//首先删除原有的关联的表
		String dsql = "delete from aiot_maintain_file where mainid = "+main.getId();
		this.execute(dsql);
		
		if(files!=null&&files.size()>0){
			StringBuffer sql = new StringBuffer("");
			sql.append(" insert into aiot_maintain_file(mainid,realname,pathname) values");
			for(int i=0;i<files.size();i++){
				MainTenanceFile file = files.get(i);
				sql.append("(").append(main.getId()).append(",'").append(file.getRealName()).append("','").append(file.getPathName()).append("')");
				if(i<files.size()-1){
					sql.append(",");
				}
			}
			this.update(sql.toString(), null);
		}
	}
	//根据站点设备维护ID,得到该维护记录的附件列表
	public List<MainTainFile> getFileListByMain(MainTainFile model){
		List<MainTainFile> files = new ArrayList<MainTainFile>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select id,mainid,realname,pathname from aiot_maintain_file where mainid =  ").append(model.getId());
		files = this.queryObjectList(sql.toString(), MainTainFile.class);
		return files;
	}
	
	/*
	 * 根据站点查询出该站点下的所有需要展示例行维护信息的设备的列表
	 */
	public List<MainTenance> getDeviceList4MainByStation(StationModel station){
		List<MainTenance> list = new ArrayList<MainTenance>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct b.id as madcid,aiot_watch_point_id as stationid,");
		sql.append(" device_catalog_id as deviceid,c.name as deviceName,b.createTime");
		sql.append(" from aiot_madc_amc a,map_awp_device_catalog b,device_catalog c");
		sql.append(" where a.madcid =b.id and aiot_watch_point_id =").append(station.getId());
		sql.append(" and b.device_catalog_id = c.id");
		sql.append(" order by b.orderCode");
		list = this.queryObjectList(sql.toString(), MainTenance.class);
		return list;
	}
	/*
	 * 通过站点和设备得到所有例行维护类型集合
	 */
	public List<MainTenance> getMainConfigListByStationDevice(MainTenance main){
		List<MainTenance> list = new ArrayList<MainTenance>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.madcid,a.amcid,b.name as amcname,a.mainnum");
		sql.append(" from aiot_madc_amc a,aiot_main_config b");
		sql.append(" where a.amcid = b.id  ");
		sql.append(" and a.madcid =").append(main.getMadcId());
		list = this.queryObjectList(sql.toString(), MainTenance.class);
		return list;
	}
	/*
	 *通过站点 设备和维护类型得到上一次维护时间
	 */
	public MainTenance getLastMainByStationDeviceConfig(MainTenance main){
		MainTenance res = new MainTenance();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.end_time as endTime");
		sql.append(" from aiot_maintenance a,map_awp_device_catalog b");
		sql.append(" where a.wpid = b.aiot_watch_point_id ");
		sql.append(" and a.deviceid = b.device_catalog_id ");
		sql.append(" and b.id =").append(main.getMadcId()).append(" and a.configid =").append(main.getAmcId());
		sql.append(" order by a.end_time desc limit 1");
		res = this.queryObject(sql.toString(), MainTenance.class);
		return res;
	}
	
	/*
	 * 为首页展示传感器的运行状态
	 * 根据首页配置信息
	 * 根据传感器维护记录
	 */
	public List<SystemModel> getDeviceMainState(StationModel station){
		List<SystemModel> mains = new ArrayList<>();
		//获得需要再首页展示的设备列表
		List<DeviceModel> devices = new ArrayList<DeviceModel>();
		StringBuffer dsql = new StringBuffer("");
		dsql.append(" select a.id,a.name,a.code");
		dsql.append(" from device_catalog a,aiot_firstpage_system_show b");
		dsql.append(" where a.code = b.indicatorcode");
		dsql.append(" and b.wpid = ").append(station.getId());
		dsql.append(" order by b.ordercode");
		devices = this.queryObjectList(dsql.toString(), DeviceModel.class);
		for(DeviceModel device:devices){
			StringBuffer csql = new StringBuffer("");
			csql.append(" select '").append(device.getCode()).append("' as indicatorcode,'").append(device.getName()).append("' as indicatorname,");
			csql.append(" case a.state when 3 then '正常运行' else c.name  end as data");
			csql.append(" from aiot_maintenance a,aiot_main_config c");
			csql.append(" where a.wpid = ").append(station.getId());
			csql.append(" and a.configid = c.id");
			csql.append(" and a.deviceid = ").append(device.getId());
			csql.append(" order by begin_time desc limit 1");
			SystemModel sysModel = new SystemModel();
			sysModel = this.queryObject(csql.toString(), SystemModel.class);
			if(sysModel!=null&&sysModel.getData()!=null&&sysModel.getData().length()>0){
				
			}else{
				sysModel = new SystemModel();
				sysModel.setIndicatorCode(device.getCode());
				sysModel.setIndicatorName(device.getName());
				sysModel.setData("正常运行");
			}
			mains.add(sysModel);
		}
		return mains;
	}
	
	/*
	 * 为例行维护提供设备列表
	 */
	public List<SelectTree> getDeviceIndicatorTree4Main(MainTainModel model){
		List<SelectTree> result = new ArrayList<SelectTree>();
		SelectTree parent = new SelectTree();
		parent.setName("参数列表");
		parent.setIsActive(true);
		parent.setIsExpanded(true);
		
		List<SelectTree> list = new ArrayList<SelectTree>();
		List<SelectTree> resultlist = new ArrayList<SelectTree>();
		//得到该站点的设备的列表
		StringBuffer deviceSql = new StringBuffer("");
		deviceSql.append(" select a.id,a.name,'true' as isExpanded,'true' as isActive");
		deviceSql.append(" from view_show_station_device a");
		deviceSql.append(" where a.stationid = ").append(model.getStationId());
		deviceSql.append(" order by ordercode ");
		list = this.queryObjectList(deviceSql.toString(), SelectTree.class);
		for(SelectTree device:list){
			List<SelectTree> child = new ArrayList<SelectTree>();
			StringBuffer childSql = new StringBuffer("");
			childSql.append(" select concat(a.code,'#',b.deviceid) as id,a.title as name,'true' as isExpanded,");
			childSql.append(" case when c.indicatorcode is not null then 'true' else 'false' end as selected");
			childSql.append(" from dm_indicator a,");
			childSql.append(" view_stationid_deviceid_indicatorcode b left join aiot_maintain_device_indicator c");
			childSql.append(" on c.mainid =").append(model.getId());
			childSql.append(" and b.deviceid  = c.deviceid and b.indicatorcode = c.indicatorcode");
			childSql.append(" where a.isactive = 1 and a.code = b.indicatorcode");
			childSql.append(" and b.deviceid = ").append(device.getId());
			childSql.append(" and b.stationid = ").append(model.getStationId());
			childSql.append(" order by a.orderCode");
			child = this.queryObjectList(childSql.toString(), SelectTree.class);
			if(child!=null&&child.size()>0){
				device.setChildren(child);
				resultlist.add(device);
			}
		}
		parent.setChildren(resultlist);
		result.add(parent);
		return result;
	}
	
	/*
	 * 保存设备维护关联的设备集合
	 */
	public Result saveMainDevices(int mainId,String deviceIds){
		Result result = new Result();
		result.setResult(result.SUCCESS);
		//保存关联的设备集合
		String[] ids = deviceIds.split(",");
		//删除原有的记录
		StringBuffer deleSql = new StringBuffer("");
		deleSql.append("delete from aiot_maintain_device_indicator where mainid = ").append(mainId);
		try {
			this.execute(deleSql.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除原有设备维护配置失败");
			return result;
		}
		StringBuffer deviceSql = new StringBuffer("");
		deviceSql.append(" insert into aiot_maintain_device_indicator(mainid,deviceid,indicatorcode) values(0,0,'0')");
		for(String id:ids){
			if(id.contains("#")){
				String indicatorCode = id.substring(0, id.indexOf("#"));
				String deviceId = id.substring(id.indexOf("#")+1, id.length());
				deviceSql.append(",(").append(mainId).append(",").append(deviceId).append(",'").append(indicatorCode).append("')");
			}
		}
		deviceSql.append(" on duplicate key update indicatorcode = values(indicatorcode)");
		try {
			this.execute(deviceSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("插入设备维护配置失败");
			return result;
		}
		return result;
	}
	
	/*
	 * 查询出站点内设备的参数,某段时间内的维护记录
	 */
	public List<MainTainModel> getMainTainList4StationDeviceIndicator(int stationId,int deviceId,
			String indicatorCode,String beginTime,String endTime){
		List<MainTainModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,c.title as stationname,b.deviceid,d.name as devicename,a.begintime,a.endtime,a.userid,a.status,b.indicatorCode");
		sql.append(" from aiot_maintain a,aiot_maintain_device_indicator b,aiot_watch_point c,device_catalog d");
		sql.append(" where a.id = b.mainid");
		sql.append(" and a.stationid = c.id and b.deviceid = d.id");
		if(stationId>0) {
			sql.append(" and a.stationid = ").append(stationId);
		}
		if(deviceId>0) {
			sql.append(" and b.deviceid = ").append(deviceId);
		}
		if(indicatorCode!=null&&indicatorCode.length()>0) {
			sql.append(" and b.indicatorcode = '").append(indicatorCode).append("'");
		}
		sql.append(" and ((a.endtime >= '").append(beginTime).append("' or a.endtime is null)");
		sql.append(" or a.begintime <= '").append(endTime).append("')");
		sql.append(" order by a.begintime desc");
		list = this.queryObjectList(sql.toString(), MainTainModel.class);
		
		return list;
	}
}
