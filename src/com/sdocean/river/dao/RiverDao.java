package com.sdocean.river.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.river.model.RiverModel;
import com.sdocean.station.model.StationModel;

@Component
public class RiverDao extends OracleEngine{

	/*
	 * 获得流域的列表
	 */
	public List<RiverModel> getRiverListByRiver(RiverModel model){
		List<RiverModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name,a.length,a.remark,a.ordercode,");
		sql.append(" group_concat(b.stationid) as stationIds,");
		sql.append(" group_concat(b.stationname) as stationname");
		sql.append(" from aiot_river a left join ");
		sql.append(" (select m.id,m.riverid,m.stationid,n.title as stationname");
		sql.append(" from aiot_river_station m,aiot_watch_point n");
		sql.append(" where m.stationid = n.id");
		sql.append(" and n.isactive = 1) b");
		sql.append(" on a.id = b.riverid");
		sql.append(" where 1=1");
		if(model!=null&&model.getId()>0) {
			sql.append(" a.id = ").append(model.getId());
		}
		if(model!=null&&model.getName()!=null&&model.getName().length()>0) {
			sql.append(" and a.name like '%").append(model.getName()).append("%'");
		}
		sql.append(" group by a.id");
		//增加排序
		sql.append(" order by a.ordercode ");
		list = this.queryObjectList(sql.toString(), RiverModel.class);
		return list;
	}
	
	/*
	 * 新增流域
	 */
	public Result saveRiver(RiverModel model) {
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setMessage("新增成功");
		result.setResult(result.SUCCESS);
		//判断名称是否为空
		if(model==null||model.getName()==null||model.getName().length()<1) {
			result.setMessage("流域信息错误,请重试");
			result.setResult(result.FAILED);
			return result;
		}
		
		//判断该流域是否重名
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1)");
		checkSql.append(" from aiot_river");
		checkSql.append(" where name = '").append(model.getName()).append("'");
		int num = 0;
		num = this.queryForInt(checkSql.toString(), null);
		if(num>0) {
			result.setMessage("流域名称冲突,请重试");
			result.setResult(result.FAILED);
			return result;
		}
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_river(name,length,remark,orderCode)");
		sql.append(" values(?,?,?,?)");
		Object[] objects = new Object[] {
				model.getName(),model.getLength(),model.getRemark(),model.getOrderCode()
		};
		try {
			this.update(sql.toString(), objects);
		} catch (Exception e) {
			result.setMessage("新增流域失败");
			result.setResult(result.FAILED);
			return result;
		}
		//获得添加流域的ID值
		String last = "SELECT LAST_INSERT_ID()";
		int lastId = this.queryForInt(last, null);
		model.setId(lastId);
		result = this.saveRiverStationList(model, result);
		return result;
	}
	
	public Result saveRiverStationList(RiverModel river,Result result) {
		//删除原有的记录
		StringBuffer deleSql = new StringBuffer("");
		deleSql.append("delete from aiot_river_station where riverid = ").append(river.getId());
		try {
			this.execute(deleSql.toString());
		} catch (Exception e) {
			result.setMessage("删除原有配置站点列表失败");
			result.setResult(result.FAILED);
			return result;
		}
		StringBuffer sql = new StringBuffer("");
		List<StationModel> stations = river.getStations();
		sql.append("insert into aiot_river_station(riverid,stationid)  values(0,0)");
		for(StationModel station:stations) {
			sql.append(",(").append(river.getId()).append(",").append(station.getId()).append(")");
		}
		sql.append(" on duplicate key update stationid=values(stationid)");
		try {
			this.execute(sql.toString());
		} catch (Exception e) {
			result.setMessage("配置站点列表失败");
			result.setResult(result.FAILED);
			return result;
		}
		return result;
	}
	
	/*
	 * 修改流域信息
	 */
	public Result updateRiver(RiverModel river) {
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(river));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//判断流域名称是否有冲突
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_river");
		checkSql.append(" where id <> ").append(river.getId());
		checkSql.append(" and name = '").append(river.getName()).append("'");
		int num = 0;
		num = this.queryForInt(checkSql.toString(), null);
		if(num>0) {
			result.setMessage("流域名称冲突");
			result.setResult(result.FAILED);
			return result;
		}
		//处理流域信息
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_river set name=?,length=?,remark=?,ordercode=? where id=?");
		Object[] objects = new Object[] {
				river.getName(),river.getLength(),river.getRemark(),river.getOrderCode(),river.getId()
		};
		try {
			this.update(sql.toString(), objects);
		} catch (Exception e) {
			result.setMessage("修改流域信息失败");
			result.setResult(result.FAILED);
			return result;
		}
		result = this.saveRiverStationList(river, result);
		return result;
	}
	
	/*
	 * 获得流域中站点的列表
	 * 并选中当天流域有权限的站点
	 */
	public List<SelectTree> getRiverStationSelectTree(RiverModel river){
		//初始化返回结果
		int riverid = 0;
		if(river!=null&&river.getId()>0) {
			riverid = river.getId();
		}
		List<SelectTree> result = new ArrayList<>();
		SelectTree parent = new SelectTree();
		parent.setName("站点列表");
		parent.setIsActive(true);
		parent.setIsExpanded(true);
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.title as name,'true' as isactive,");
		sql.append(" case when b.stationid is not null then 'true' else 'false' end as selected");
		sql.append(" from aiot_watch_point a left join aiot_river_station b");
		sql.append(" on a.id = b.stationid");
		sql.append(" and b.riverid =").append(riverid);
		sql.append(" where a.isactive = 1");
		List<SelectTree> childlist = this.queryObjectList(sql.toString(), SelectTree.class);
		if(childlist!=null&&childlist.size()>0) {
			parent.setChildren(childlist);
		}
		result.add(parent);
		return result;
	}
}
