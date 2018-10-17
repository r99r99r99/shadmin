package com.sdocean.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Component
public class UsersDao extends OracleEngine {
	
	public SysUser getUserByName(String userName){
		SysUser user = new SysUser();
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,username,password,positionid,telephone,phone,realname,");
		sql.append(" birthday,email,picurl,companyid,logintype,isactive");
		sql.append(" from sys_user");
		sql.append(" where isactive = 1 and username = '").append(userName).append("'");
		user = this.queryObject(sql.toString(), SysUser.class);
		//如果头像路径为空,则初始化
		if(user!=null){
			if(user.getPicurl()==null||user.getPicurl().length()<1){
				user.setPicurl("liaohe/resources/assets/avatars/user.jpg");
			}
		}
		
		return user;
	}
	
	/*
	 * 为人员管理查询结果
	 */
	public List<SysUser> getRows4UserList(SysUser model){
		List<SysUser> rows = new ArrayList<SysUser>();
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.username,a.realname,a.telephone,");
		sql.append(" case when b.name is null then '' else b.name end as positionname,a.positionid,");
		sql.append(" a.phone,a.birthday,a.email,a.picurl,a.logintype,");
		sql.append(" a.isactive,case when a.isactive = 1 then '启用' else '禁用' end as isactivename,");
		sql.append(" case when c.name is null then '' else c.name end as companyname ,a.companyid");
		sql.append(" from sys_user a ");
		sql.append(" left join sys_position b on a.positionid = b.id and b.isactive = 1");
		sql.append(" left join g_company c on a.companyid = c.code and c.isactive = 1");
		sql.append(" where 1= 1");
		//添加查询条件
		//判断模糊查询条件
		if(model.getUserName()!=null&&model.getUserName().length()>0){
			sql.append(" and (a.username like '%").append(model.getUserName()).append("%' ||");
			sql.append(" a.realname like '%").append(model.getUserName()).append("%' )");
		}
		if(model.getIsactive()<2){
			sql.append(" and a.isactive = ").append(model.getIsactive());
		}
		//增加排序
		sql.append(" order by b.ordercode");
		rows = this.queryObjectList(sql.toString(), SysUser.class);
		return rows;
	}
	
	/*
	 * 修改人员信息
	 */
	public Result saveUserChange(SysUser user){
		//生成返回结果
		Result result = new Result();
		result.setModel(JsonUtil.toJson(user));
		//表名更改操作
		result.setDotype(result.UPDATE);
		
		//判断username是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from sys_user where username = '").append(user.getUserName()).append("' ");
		checkSql.append(" and id <> ").append(user.getId());
		int r = this.queryForInt(checkSql.toString(), null);
		if(r>0){
			result.setResult(result.FAILED);
			result.setMessage("用户名重复,请重新输入!");
			return result;
		}
		//开始拼接SQL
		StringBuffer sql = new StringBuffer("");
		sql.append(" update sys_user set username =?,realname = ?,positionid=?,");
		sql.append(" telephone= ?,phone=?,email=?,");
		sql.append(" picurl=?,companyid=?,logintype=?,isactive=?,birthday=?");
		sql.append(" where id=?");
		int update = 0;
		try {
			update = this.update(sql.toString(), new Object[]{user.getUserName(),user.getRealName(),user.getPositionId(),
				user.getTelephone(),user.getPhone(),user.getEmail(),user.getPicurl(),user.getCompanyId(),
				user.getLogintype(),user.getIsactive(),user.getBirthday(),user.getId()});
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
			return result;
		}
		if(update>0){
			result.setResult(result.SUCCESS);
			result.setMessage("修改成功");
		}else{
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
		}
		
		return result;
	}
	
	/*
	 * 新增人员信息
	 * 
	 */
	public Result saveNewUser(SysUser user){
		//生成返回结果
		Result result = new Result();
		result.setModel(JsonUtil.toJson(user));
		//标明新增操作
		result.setDotype(result.ADD);
		//判断用户名是否重复
		StringBuffer ifonlysql = new StringBuffer("");
		ifonlysql.append(" select count(1) from sys_user where username = '").append(user.getUserName()).append("'");
		int ifonly = this.queryForInt(ifonlysql.toString(), null);
		if(ifonly>0){
			result.setResult(result.FAILED);
			result.setMessage("用户名重复,请重新输入");
			return result;
		}
		//根据用户名和随机数字生成密码
		String password = user.getUserName()+(int)(Math.random()*100)+(int)(Math.random()*100);
		user.setPassword(password);
		//将结果保存到数据库中
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_user(username,password,realname,positionid,telephone,phone,email,picurl,companyid,logintype,isactive,birthday)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		Object[] param = new Object[]{
				user.getUserName(),user.getPassword(),user.getRealName(),user.getPositionId(),
				user.getTelephone(),user.getPhone(),user.getEmail(),user.getPicurl(),user.getCompanyId(),
				user.getLogintype(),user.getIsactive(),user.getBirthday()
		};
		int insert = 0;
		try {
			insert = this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新建失败");
			e.printStackTrace();
		}
		if(insert>0){
			result.setResult(result.SUCCESS);
			result.setMessage("新建成功");
		}else{
			result.setResult(result.FAILED);
			result.setMessage("新建失败");
		}
		return result;
	}
	
	/*
	 * 获得所有有效公司下的所有有效人员的列表
	 * 根据角色,将角色权限内的人物选定
	 * 公司按照公司CODE排序,
	 * 人员按照职位CODE排序
	 */
	public List<ZTreeModel> getCompUsers4ZTree(RoleModel role){
		List<ZTreeModel> usertrees = new ArrayList<ZTreeModel>();
		
		//获得所有有效人员的列表   设定为复选框形式   并将角色内的任务选定
		StringBuffer usersql = new StringBuffer();
		usersql.append(" select a.id,concat('C',a.companyid) as pid,a.realname as name,");
		usersql.append(" case when b.role_id is null then 'false' else 'true' end as checked");
		usersql.append(" from sys_user a left join sys_role_user b on a.id = b.user_id and b.role_id =").append(role.getId());
		usersql.append(" where a.isactive = 1");
		usertrees = this.queryObjectList(usersql.toString(), ZTreeModel.class);
		
		return usertrees;
	}
	
	/*
	 * 修改用户的人员信息
	 */
	public Result saveUserNowChange(SysUser model){
		//初始化返回信息
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		StringBuffer sql = new StringBuffer("");
		sql.append("update sys_user set realname=?,telephone=?,phone=?,birthday=?,email=?,picurl=? where id=?");
		Object[] params = new Object[]{
				model.getRealName(),model.getTelephone(),model.getPhone(),
				model.getBirthday(),model.getEmail(),model.getPicurl(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 用户修改密码
	 */
	public Result saveUserPassword(SysUser model){
		//初始化返回信息
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		if(!model.getNewPass().equals(model.getConfimPass())){
			result.setResult(result.FAILED);
			result.setMessage("新密码与确认密码不一致");
			return result;
		}
		//验证原密码是否一致
		StringBuffer checksql = new StringBuffer("");
		checksql.append("select 1 from sys_user where id = ").append(model.getId()).append(" and password ='").append(model.getPassword()).append("' limit 1");
		int check = 0;
		try {
			check = this.queryForInt(checksql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(check==0){
			result.setResult(result.FAILED);
			result.setMessage("原密码输入错误");
			return result;
		}
		//开始修改密码
		StringBuffer sql =  new StringBuffer("");
		sql.append("update sys_user set password = '").append(model.getNewPass()).append("' where id=").append(model.getId());
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("密码修改错误");
		}
		return result;
	}
	
	/*
	 * 获得拥有该站点权限下的所有用户的列表
	 */
	public List<SysUser> getUsersByStation(StationModel station){
		List<SysUser> list = new ArrayList<SysUser>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct a.id,a.username,a.realname,a.telephone,a.phone,a.birthday,a.email,a.picurl,a.logintype");
		sql.append(" from sys_user a,sys_role_user b,sys_role c,sys_role_station d");
		sql.append(" where a.id = b.user_id and b.role_id = c.id ");
		sql.append(" and a.isactive = 1");
		sql.append(" and c.isactive =1 and c.type = 2");
		sql.append(" and c.id = d.role_id ");
		if(station!=null&&station.getId()>10000){
			sql.append(" and d.station_id =").append(station.getId());
		}
		list = this.queryObjectList(sql.toString(), SysUser.class);
		return list;
	}
	/*
	 * 获得所有有效公司下的所有有效人员的列表
	 * 根据站点接收短信权限,将权限内的人物选定
	 * 公司按照公司CODE排序,
	 * 人员按照职位CODE排序
	 */
	public List<ZTreeModel> getCompUsers4StatinSmsZTree(SmsSettingModel model){
		List<ZTreeModel> usertrees = new ArrayList<ZTreeModel>();
		
		//获得所有有效人员的列表   设定为复选框形式   并将角色内的任务选定
		StringBuffer usersql = new StringBuffer();
		usersql.append(" select a.id,concat('C',a.companyid) as pid,a.realname as name,");
		usersql.append(" case when b.stationid is null then 'false' else 'true' end as checked");
		usersql.append(" from sys_user a left join sys_sms_station_user b on a.id = b.userid and b.stationid =").append(model.getStationId());
		usersql.append(" and b.type = ").append(model.getType());
		usersql.append(" where a.isactive = 1");
		usertrees = this.queryObjectList(usersql.toString(), ZTreeModel.class);
		
		return usertrees;
	}
	
	/*
	 * 测试数据库回滚机制
	 */
	public void checkDbBack(){
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_user(username,password)");
		sql.append(" values('aa','bb')");
		this.execute(sql.toString());
	}
	public void checkDbBackbb(){
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_user(username,password)");
		sql.append(" values('ee','bb','cc')");
		this.execute(sql.toString());
	}
}
