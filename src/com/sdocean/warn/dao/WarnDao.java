package com.sdocean.warn.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.station.dao.StationDao;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.warn.model.Warn4FirstModel;
import com.sdocean.warn.model.WarnModel;
import com.sdocean.warn.model.WarnValueModel;

@Component
public class WarnDao extends OracleEngine {
	
	@Resource
	StationDao stationDao;
	/*
	 * 为首页读取预警信息
	 */
	public Warn4FirstModel getWarn4First(Long warnType,StationModel watchPoint,String beginDate){
		Warn4FirstModel warn = new Warn4FirstModel();
		String typeSql = "";
		String warnTypeString = "";
		if(warnType==0){//如果为0则表示预警信息
			warnTypeString = "预警信息";
			typeSql = " a.max_value as upper,a.min_value as lower";
		}else{  //如果为1则表示告警信息
			warnTypeString = "告警信息";
			typeSql = " a.max_value as upper,a.min_value as lower";
		}
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.collect_time as collecttime,d.title,e.logo as unitname,a.value as data,");
		sql.append(" if(a.operate=0,'未处理','已处理') as operate,");
		sql.append(typeSql);
		sql.append(" from aiot_warn_alarm_value a,dm_indicator d,g_unit e");
		sql.append(" where a.indicatorCode = d.code and d.unitid = e.id");
		sql.append(" and a.collect_time >='").append(beginDate).append("'");
		sql.append(" and a.wpId =").append(watchPoint.getId());
		sql.append(" and a.type = ").append(warnType);
		sql.append(" order by collect_time desc");
		sql.append(" limit 0,3");
		List<WarnModel> warnModels = null;
		warnModels = this.queryObjectList(sql.toString(), WarnModel.class);
		warn.setWarnType(warnTypeString);
		warn.setWarnModels(warnModels);
		
		return warn;
	}
	
	/*
	 * 根据条件查询出预警告警配置列表
	 * 根据当前登录人员查询权限内的站点
	 */
	public List<WarnModel> getWarnList(WarnModel model,SysUser user){
		List<WarnModel> list = new ArrayList<WarnModel>();
		//获得该人员权限下的所有站点的集合
		List<StationModel> stations = stationDao.getStations4User(user);
		//拼接INSQL语句
		StringBuffer insql = new StringBuffer("0");
		for(StationModel station:stations){
			insql.append(",").append(station.getId());
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.wp_id as wpId,b.title as wpName,a.indicator_code as indicatorCode,c.title as indicatorName,");
		sql.append(" a.warn_lower as warnLower,a.warn_upper as warnUpper,a.alarm_lower as alarmLower,a.alarm_upper as alarmUpper");
		sql.append(" from aiot_warn_alarm_config a,aiot_watch_point b,dm_indicator c");
		sql.append(" where a.wp_id = b.id and b.isactive = 1");
		sql.append(" and a.indicator_code = c.code and c.isactive = 1");
		sql.append(" and a.wp_id in (").append(insql).append(")");
		//添加查询条件
		if(model!=null&&model.getWpId()>10000){
			sql.append(" and a.wp_id =").append(model.getWpId());
		}
		list = this.queryObjectList(sql.toString(), WarnModel.class);
		return list;
	}
	
	/*
	 * 修改预警告警配置
	 */
	public Result saveWarnChange(WarnModel model){
		Result result = new Result();
		//初始化返回结果
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		
		//判断该条配置是否冲突
		StringBuffer checksql = new StringBuffer();
		checksql.append(" select count(1) from aiot_warn_alarm_config where wp_id =? and indicator_code =? and id <> ?");
		Object[] checkParams = new Object[]{
				model.getWpId(),model.getIndicatorCode(),model.getId()
		};
		int count = 0;
		try {
			count = this.queryForInt(checksql.toString(), checkParams);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("验证唯一性时失败");
			return result;
		}
		if(count>0){
			result.setResult(result.FAILED);
			result.setMessage("已经包含同样的配置");
			return result;
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_warn_alarm_config set wp_id=?,indicator_code=?,warn_lower=?,warn_upper=?,alarm_lower=?,alarm_upper=? where id=?");
		Object[] params = new Object[]{
				model.getWpId(),model.getIndicatorCode(),model.getWarnLower(),
				model.getWarnUpper(),model.getAlarmLower(),model.getAlarmUpper(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败,请重试");
		}
		return result;
	}
	
	/*
	 * 保存新增预警告警配置
	 */
	public Result saveNewWarn(WarnModel model){
		Result result = new Result();
		//初始化返回结果
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_warn_alarm_config(wp_id,indicator_code,warn_lower,warn_upper,alarm_lower,alarm_upper)");
		sql.append(" values(?,?,?,?,?,?) on duplicate key update warn_lower=values(warn_lower),warn_upper=values(warn_upper),alarm_lower=values(alarm_lower),alarm_upper=values(alarm_upper)");
		Object[] params = new Object[]{
				model.getWpId(),model.getIndicatorCode(),model.getWarnLower(),
				model.getWarnUpper(),model.getAlarmLower(),model.getAlarmUpper()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("新增失败,请重试");
		}
		return result;
	}
	
	/*
	 * 删除选中的预警告警配置
	 */
	public Result deleWarnSetting(WarnModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		//拼接删除语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from aiot_warn_alarm_config where id in (").append(model.getIds()).append(")");
		int res = 0 ;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	/*
	 * 展示条件内的预警告警信息
	 */
	public List<WarnValueModel> getWarnValueRows(WarnValueModel model){
		List<WarnValueModel> list = new ArrayList<WarnValueModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.type,e.value as typeName,a.value,a.collect_time,a.operate,case a.operate when 0 then '未操作' when 1 then '已操作' end as operateName,");
		sql.append(" a.flag,a.remark,a.wpId,b.title as wpName,a.deviceId,c.name as deviceName,");
		sql.append(" a.indicatorCode,d.title as indicatorName,a.min_value as minConfig,a.max_value as maxConfig,");
		sql.append(" g.logo as unitName");
		sql.append(" from aiot_warn_alarm_value a,aiot_watch_point b,device_catalog c,sys_public e,");
		sql.append(" dm_indicator d left join g_unit g on d.unitId = g.id and g.isactive = 1");
		sql.append(" where a.wpid = b.id  and b.isactive = 1");
		sql.append(" and a.deviceId = c.id and a.indicatorCode = d.code and d.isactive = 1");
		sql.append(" and a.type = e.classid and e.parentcode = '0008'");
		//添加查询条件
		if(model!=null){
			//添加站点条件
			if(model.getWpId()>10000){
				sql.append(" and a.wpid = ").append(model.getWpId());
			}
			//添加类型条件
			if(model.getType()>0){
				sql.append(" and a.type = ").append(model.getType());
			}
			//添加时间条件
			if(model.getBeginDate()!=null&&model.getBeginDate().length()>0){
				sql.append(" and a.collect_time >= '").append(model.getBeginDate()).append("'");
			}
			if(model.getEndDate()!=null&&model.getEndDate().length()>0){
				sql.append(" and a.collect_time <= '").append(model.getEndDate()).append("'");
			}
			if(model.getOperate()>=0){
				sql.append(" and a.operate = ").append(model.getOperate());
			}
			
		}
		//增加排序
		sql.append(" order by a.collect_time desc ");
		list = this.queryObjectList(sql.toString(), WarnValueModel.class);
		return list;
	}
	/*
	 * 为预警告警信息添加操作以及备注
	 */
	public Result saveOperaValue(WarnValueModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("操作成功");
		StringBuffer sql = new StringBuffer();
		sql.append(" update aiot_warn_alarm_value set operate=?,flag=?,remark=? where id =?");
		Object[] params = new Object[]{
				model.getOperate(),model.getFlag(),model.getRemark(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("操作失败");
		}
		return result;
	}	
	/*
	 * 读取预警告警中的未操作的
	 */
	public Integer getNum4WarnValue(StationModel model){
		Integer num = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from aiot_warn_alarm_value a where a.operate = 0");
		sql.append(" and a.wpId= ").append(model.getId());
		num = this.queryForInt(sql.toString(), null);
		return num;
	}
	/*
	 * 预警告警信息中批量操作
	 */
	public Result updateAllOperaValue(WarnValueModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("批量修改成功");
		//开始执行操作
		StringBuffer sql = new StringBuffer();
		sql.append(" update aiot_warn_alarm_value set");
		sql.append(" operate=").append(model.getOperate()).append(",");
		sql.append(" flag='").append(model.getFlag()).append("',");
		sql.append(" remark='").append(model.getRemark()).append("'");
		sql.append(" where id in (").append(model.getIds()).append(")");
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("批量修改失败");
		}
		return result;
	}
}
