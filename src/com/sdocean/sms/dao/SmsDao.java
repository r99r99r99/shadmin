package com.sdocean.sms.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.sms.model.SmsLog;
import com.sdocean.sms.model.SmsMouldModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

import freemarker.template.utility.StringUtil;

@Component
public class SmsDao extends OracleEngine{
	
	/*
	 * 查询某站点下需要接受短信的人员列表
	 */
	public List<SmsSettingModel> getUsers4SmsSetting(SmsSettingModel model,SysUser user){
		List<SmsSettingModel> list = new ArrayList<SmsSettingModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.userid,' 默认' as userName,'' as telephone,");
		sql.append(" a.betweentime,date_format(a.ambegin,'%H:%m:%d') as ambegin,date_format(a.amend,'%H:%m:%d') as amend,");
		sql.append("date_format(a.pmbegin,'%H:%m:%d') as pmbegin,date_format(a.pmend,'%H:%m:%d') as pmend,");
		sql.append(" a.mon,a.tues,a.wed,a.thur,a.fri,a.satur,a.sun,");
		sql.append(" case a.isactive when 1 then '接收' else '不接收' end as isactiveName");
		sql.append(" from sys_sms_userrole a");
		sql.append(" where a.userid = 0");
		sql.append(" union all");
		sql.append(" select a.userid,b.realname as userName,b.telephone,");
		sql.append(" a.betweentime,date_format(a.ambegin,'%H:%m:%d') as ambegin,date_format(a.amend,'%H:%m:%d') as amend,");
		sql.append("date_format(a.pmbegin,'%H:%m:%d') as pmbegin,date_format(a.pmend,'%H:%m:%d') as pmend,");
		sql.append(" a.mon,a.tues,a.wed,a.thur,a.fri,a.satur,a.sun,");
		sql.append(" case a.isactive when 1 then '接收' else '不接收' end as isactiveName");
		sql.append(" from sys_sms_userrole a,sys_user b");
		sql.append(" where a.userid = b.id and b.isactive = 1");
		//添加查询条件
		if(model!=null&&model.getUserName()!=null&&model.getUserName().length()>0){
			sql.append(" and (b.username like '%").append(model.getUserName()).append("%'");
			sql.append(" or b.realname like '%").append(model.getUserName()).append("%')");
		}
		//添加排序
		sql.append(" order by userName");
		list = this.queryObjectList(sql.toString(), SmsSettingModel.class);
		return list;
	}
	
	/*
	 * 初始化用户下的短信配置
	 */
	public SmsSettingModel infoUserSetting(SysUser user){
		SmsSettingModel result = new SmsSettingModel();
		//读取发送规则配置
		StringBuffer rolesql = new StringBuffer("");
		rolesql.append(" select userid,date_format(ambegin,'%H:%m:%d') as ambegin,date_format(amend,'%H:%m:%d') as amend,date_format(pmbegin,'%H:%m:%d') as pmbegin,date_format(pmend,'%H:%m:%d') as pmend,");
		rolesql.append("mon,tues,wed,thur,fri,satur,sun,betweentime,isactive from sys_sms_userrole where userid =").append(user.getId());
		result = this.queryObject(rolesql.toString(), SmsSettingModel.class);
		if(result==null){
			StringBuffer role0sql = new StringBuffer("");
			role0sql.append(" select userid,date_format(ambegin,'%H:%m:%d') as ambegin,date_format(amend,'%H:%m:%d') as amend,date_format(pmbegin,'%H:%m:%d') as pmbegin,date_format(pmend,'%H:%m:%d') as pmend,");
			role0sql.append(" mon,tues,wed,thur,fri,satur,sun,betweentime,isactive from sys_sms_userrole where userid = 0");
			result = this.queryObject(role0sql.toString(), SmsSettingModel.class);
		}
		return result;
	}
	
	/*
	 * 保存用户短信配置
	 */
	public Result saveUserSetting(SmsSettingModel model){
		//对返回结果进行初始化
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//保存配置信息
		
		
		
		//保存发送时间规则设置
		StringBuffer roleSql = new StringBuffer("");
		roleSql.append(" insert into sys_sms_userrole(userid,ambegin,amend,pmbegin,pmend,mon,tues,wed,thur,fri,satur,sun,betweentime,isactive)");
		roleSql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		roleSql.append(" on duplicate key update ambegin=values(ambegin),amend=values(amend),pmbegin=values(pmbegin),");
		roleSql.append(" pmend=values(pmend),mon=values(mon),tues=values(tues),wed=values(wed),thur=values(thur),fri=values(fri),satur=values(satur),sun=values(sun),");
		roleSql.append(" betweentime=values(betweentime),isactive=values(isactive)");
		int roleres = 0;
		Object[] roleParams = new Object[]{
				model.getUserId(),model.getAmbegin(),model.getAmend(),model.getPmbegin(),
				model.getPmend(),model.getMon(),model.getTues(),model.getWed(),model.getThur(),
				model.getFri(),model.getSatur(),model.getSun(),model.getBetweenTime(),model.getIsactive()
		};
		try {
			roleres = this.update(roleSql.toString(), roleParams);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		return result;
	}
	
	/*
	 * 查询符合条件的短信模板列表
	 */
	public List<SmsMouldModel> getSmsMoulds4Rows(SmsMouldModel model){
		List<SmsMouldModel> list = new ArrayList<SmsMouldModel>();
		if(model!=null&&model.getStationId()<10000){
			//首先添加默认站点stationid = 0
			StringBuffer first = new StringBuffer("");
			first.append(" select a.id,a.stationid,'默认站点' as stationName,a.type,c.value as typename,a.mould");
			first.append(" from sys_sms_mould a,sys_public c");
			first.append(" where c.parentcode = '0010' and a.type = c.classid");
			first.append(" and a.stationid = 0");
			//添加查询条件
			if(model.getType()>0){
				first.append(" and a.type =").append(model.getType());
			}
			list = this.queryObjectList(first.toString(), SmsMouldModel.class);
		}
		//查询非默认站点的列表
		List<SmsMouldModel> nlist = new ArrayList<SmsMouldModel>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.id,a.stationid,b.title as stationName,a.type,c.value as typename,a.mould");
		sql.append(" from sys_sms_mould a ,aiot_watch_point b,sys_public c");
		sql.append(" where a.stationid = b.id and b.isactive = 1 and c.parentcode = '0010' and a.type = c.classid");
		//添加查询条件
		if(model!=null&&model.getStationId()>10000){
			sql.append(" and a.stationid = ").append(model.getStationId());
		}
		if(model!=null&model.getType()>0){
			sql.append(" and a.type = ").append(model.getType());
		}
		nlist = this.queryObjectList(sql.toString(), SmsMouldModel.class);
		list.addAll(nlist);
		return list;
	}
	
	/*
	 * 对短信发送模板修改
	 */
	public Result saveSmsMouldChange(SmsMouldModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//验证station以及type唯一性
		StringBuffer checksql = new StringBuffer("");
		checksql.append(" select count(1) from sys_sms_mould where stationid =").append(model.getStationId());
		checksql.append(" and type = ").append(model.getType()).append(" and id <>").append(model.getId());
		int chect =0;
		try {
			chect = this.queryForInt(checksql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("站点类型唯一性检查时失败");
			return result;
		}
		if(chect>0){
			result.setResult(result.FAILED);
			result.setMessage("违反站点,类型唯一性原则");
			return result;
		}
		//提交数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" update sys_sms_mould set stationid=?,type=?,mould=? where id=?");
		Object[] params = new Object[]{
				model.getStationId(),model.getType(),model.getMould(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
		}
		return result;
	}
	/*
	 * 保存新增的短信发送模板
	 */
	public Result saveNewSmsMould(SmsMouldModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//验证station以及type唯一性
		StringBuffer checksql = new StringBuffer("");
		checksql.append(" select count(1) from sys_sms_mould where stationid =").append(model.getStationId());
		checksql.append(" and type = ").append(model.getType());
		int chect =0;
		try {
			chect = this.queryForInt(checksql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("站点类型唯一性检查时失败");
			return result;
		}
		if(chect>0){
			result.setResult(result.FAILED);
			result.setMessage("违反站点,类型唯一性原则");
			return result;
		}
		//提交数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_sms_mould(stationid,type,mould) values(?,?,?)");
		sql.append(" on duplicate key update stationId=values(stationid),type=values(type),mould=values(mould)");
		Object[] params = new Object[]{
				model.getStationId(),model.getType(),model.getMould()
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
	 * 查询短信发送记录,
	 * 增加模糊查询
	 */
	public List<SmsLog> getSmsLogs(SmsLog model){
		List<SmsLog> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.userid,b.realname as username,a.telephone,a.title,a.message,a.sendtime,");
		sql.append(" a.createtime,a.ifsend,case a.ifsend when 0 then '未发送' else '发送' end as ifSendName,a.error");
		sql.append(" from sys_sms a,sys_user b");
		sql.append(" where a.userid = b.id");
		//增加模糊查询条件
		if(model!=null&&model.getUserName()!=null&&model.getUserName().length()>0){
			sql.append(" and (b.realname like '%").append(model.getUserName()).append("%' ");
			sql.append(" or a.telephone like '%").append(model.getUserName()).append("%' )");
		}
		//增加排序
		sql.append(" order by createtime desc ");
		list = this.queryObjectList(sql.toString(), SmsLog.class);
		return list;
	}
	/*
	 * 保存短信发送配置
	 */
	public Result saveSmsStationSetting(SmsSettingModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("保存成功");
		//删除该站点,该类型的相关短信接收配置
		StringBuffer dsql = new StringBuffer("");
		dsql.append(" delete from sys_sms_station_user ");
		dsql.append(" where stationid = ").append(model.getStationId());
		dsql.append(" and type = ").append(model.getType());
		int dres = 0;
		try {
			dres = this.update(dsql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("删除原有数据时失败");
			return result;
		}
		//添加短信接收配置
		StringBuffer asql = new StringBuffer("");
		asql.append(" insert into sys_sms_station_user(stationid,userid,type)");
		asql.append(" values(0,0,0)");
		String userIds = model.getUserIds();
		String[] users = userIds.split(",");
		for(int i=0;i<users.length;i++){
			asql.append(",(").append(model.getStationId()).append(",").append(users[i]).append(",").append(model.getType()).append(")");
		}
		asql.append(" on duplicate key update userid = values(userid)");
		int ares = 0;
		try {
			ares = this.update(asql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("添加短信接收配置时失败");
		}
		return result;
	}
}
