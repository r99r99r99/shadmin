package com.sdocean.main.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.main.model.ErrorTenance;
import com.sdocean.station.model.StationModel;

@Component
public class ErrorTenanceDao extends OracleEngine {
	/*
	 * 查询出查询条件下的异常维护列表
	 */
	public List<ErrorTenance> getErrorsByStation(ErrorTenance model,List<StationModel> stations){
		List<ErrorTenance> list = new ArrayList<ErrorTenance>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,GROUP_CONCAT(k.deviceid) as deviceids,GROUP_CONCAT(k.devicename) as devicenames,");
		sql.append(" a.stationid,b.title as stationName,a.collecttime,");
		sql.append(" a.userId,c.realname as userName,a.error,a.reason,a.state,");
		sql.append(" d.value as statename,a.begintime,a.endtime,a.material,a.result");
		sql.append(" from aiot_errortenance a left join (");
		sql.append(" select m.errorid,m.deviceid,n.name as devicename");
		sql.append(" from aiot_errortenance_device m,device_catalog n");
		sql.append(" where m.deviceid = n.id");
		sql.append(" ) k on a.id = k.errorid,");
		sql.append(" aiot_watch_point b,sys_user c,sys_public d");
		sql.append(" where a.stationid = b.id and a.userid = c.id");
		sql.append(" and a.state = d.classid and d.parentcode = '0016'");
		//增加查询条件
		//增加站点查询条件
		if(model!=null&&model.getStationId()>0){
			sql.append(" and a.stationid=").append(model.getStationId());
		}else{
			sql.append(" and a.stationid in (0");
			for(StationModel station:stations){
				sql.append(",").append(station.getId());
			}
			sql.append(")");
		}
		//增加时间查询条件
		if(model!=null&&model.getBeginTime()!=null&&model.getBeginTime().length()>0){
			sql.append(" and a.begintime >= '").append(model.getBeginTime()).append("'");
		}
		if(model!=null&&model.getEndTime()!=null&&model.getEndTime().length()>0){
			sql.append(" and a.begintime <= '").append(model.getEndTime()).append("'");
		}
		//增加状态查询条件
		if(model!=null&&model.getState()>0){
			sql.append(" and a.state =").append(model.getState());
		}
		
		sql.append(" group by a.id");
		//增加排序
		sql.append(" order by a.begintime desc");
		list = this.queryObjectList(sql.toString(), ErrorTenance.class);
		return list;
	}
	/*
	 * 增加新的异常维护记录
	 */
	public Result saveNewErrorTenance(ErrorTenance model){
		//初始化查询结果
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		//插入数据,并获得刚插入的ID
		StringBuffer isql = new StringBuffer("");
		isql.append(" insert into aiot_errortenance(stationid,userid,error,reason,state,begintime,endtime,material,result)");
		isql.append(" values(?,?,?,?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getStationId(),model.getUserId(),model.getError(),
				model.getReason(),model.getState(),model.getBeginTime(),
				model.getEndTime(),model.getMaterial(),model.getResult()
		};
		try {
			this.update(isql.toString(), params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();;
			result.setResult(Result.FAILED);
			result.setMessage("插入表失败");
			return result;
		}
		//得到刚插入的维护记录的ID值
		int id = 0;
		String csql="select last_insert_id()";
		id = this.queryForInt(csql, null);
		model.setId(id);
		//插入涉及到的deviceids 
		if(model!=null&&model.getDeviceIds()!=null&&model.getDeviceIds().length()>0){
			String deviceids[]  = model.getDeviceIds().split(",");
			StringBuffer msql = new StringBuffer();
			msql.append(" insert into aiot_errortenance_device(errorid,deviceid) values (0,0)");
			for(int i=0;i<deviceids.length;i++){
				msql.append(",(").append(id).append(",").append(deviceids[i]).append(")");
			}
			msql.append("  on duplicate key update deviceid=values(deviceid)");
			try {
				this.update(msql.toString(), null);
			} catch (Exception e) {
				// TODO: handle exception
				result.setResult(Result.FAILED);
				result.setMessage("插入设备附表失败");
			}
		}
		
		return result;
	}
	/*
	 * 修改异常维护上报信息
	 */
	public Result saveChangeErrorTenance(ErrorTenance model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		StringBuffer usql = new StringBuffer("");
		usql.append(" update aiot_errortenance set error=?,reason=?,state=?,begintime=?,endtime=?,material=?,result=? where id=?");
		Object[] params = new Object[]{
				model.getError(),model.getReason(),model.getState(),
				model.getBeginTime(),model.getEndTime(),model.getMaterial(),
				model.getResult(),model.getId()
		};
		try {
			this.update(usql.toString(), params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("修改失败");
			return result;
		}
		//修改涉及的设备列表
		StringBuffer dsql = new StringBuffer("");
		dsql.append(" delete from aiot_errortenance_file where errorid = ").append(model.getId());
		try {
			this.update(dsql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("删除原配置失败");
			return result;
		}
		//更新设备列表
		if(model!=null&&model.getDeviceIds()!=null&&model.getDeviceIds().length()>0){
			String deviceids[]  = model.getDeviceIds().split(",");
			StringBuffer msql = new StringBuffer();
			msql.append(" insert into aiot_errortenance_device(errorid,deviceid) values (0,0)");
			for(int i=0;i<deviceids.length;i++){
				msql.append(",(").append(model.getId()).append(",").append(deviceids[i]).append(")");
			}
			msql.append("  on duplicate key update deviceid=values(deviceid)");
			try {
				this.update(msql.toString(), null);
			} catch (Exception e) {
				// TODO: handle exception
				result.setResult(Result.FAILED);
				result.setMessage("插入设备附表失败");
			}
		}
		return result;
	}
	/*
	 * 为异常维护记录提供设备列表
	 */
	public List<SelectTree> getDeviceList4Error(ErrorTenance model){
		List<SelectTree> list = new ArrayList<SelectTree>();
		//添加设备列表的首层
		SelectTree first = new SelectTree();
		first.setId("0");
		first.setName("设备");
		first.setIsExpanded(true);
		first.setIsActive(true);
		
		//获得所有有效的设备的列表
		List<SelectTree> children = new ArrayList<SelectTree>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select b.id,b.name,case when k.deviceid is null then 'false' else 'true' end as selected");
		sql.append(" from map_awp_device_catalog a left join (");
		sql.append(" select m.stationid,n.deviceid");
		sql.append(" from aiot_errortenance m,aiot_errortenance_device n");
		sql.append(" where m.id = n.errorid and m.stationid =").append(model.getStationId());
		sql.append(" and m.id = ").append(model.getId());
		sql.append(" ) k on a.aiot_watch_point_id = k.stationid ");
		sql.append(" and a.device_catalog_id = k.deviceid,");
		sql.append(" device_catalog b");
		sql.append(" where a.device_catalog_id = b.id");
		sql.append(" and a.aiot_watch_point_id =").append(model.getStationId());
		children = this.queryObjectList(sql.toString(), SelectTree.class);
		//将设备列表添加的首层的child中
		first.setChildren(children);
		//将首层添加到结果集中
		list.add(first);
		return list;
	}
}
