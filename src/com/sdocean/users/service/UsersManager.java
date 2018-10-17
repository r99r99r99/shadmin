package com.sdocean.users.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.company.dao.CompanyDao;
import com.sdocean.page.model.UiColumn;
import com.sdocean.role.model.RoleModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.dao.UsersDao;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UsersManager {

	@Resource
	private UsersDao usersDao;
	
	@Resource
	private CompanyDao companyDao;

	@Transactional(readOnly = true)
	public SysUser getUsersByAccount(String account) {
		SysUser usersByAccount = this.usersDao.getUserByName(account);
		return usersByAccount;
	}
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4UserList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("用户名", "userName", true, "*");
		UiColumn col13 = new UiColumn("用户名", "realName", true, "*");
		UiColumn col3 = new UiColumn("手机号", "telephone", true, "*");
		UiColumn col4 = new UiColumn("职业id", "positionId", false, "*");
		UiColumn col5 = new UiColumn("职业", "positionName", true, "*");
		UiColumn col6 = new UiColumn("电话", "phone", true, "*");
		UiColumn col7 = new UiColumn("生日", "birthday", true, "*");
		UiColumn col8 = new UiColumn("文件路径", "picurl", true, "*");
		UiColumn col9 = new UiColumn("公司ID", "companyId", false, "*");
		UiColumn col10 = new UiColumn("单位名称", "companyName", true, "*");
		UiColumn col11 = new UiColumn("isactive", "isactive", false, "*");
		UiColumn col12 = new UiColumn("状态", "isactiveName", true, "*");
		UiColumn col14 = new UiColumn("邮箱", "email", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col13);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col14);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
		
		return cols;
	}
	/*public List<UiColumn> getCols4UserList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("用户名", "userName", true, "*");
		UiColumn col13 = new UiColumn("用户名", "realName", true, "*");
		UiColumn col3 = new UiColumn("手机号", "telephone", true, "*");
		UiColumn col4 = new UiColumn("职业id", "positionId", false, "*");
		UiColumn col5 = new UiColumn("职业", "positionName", true, "*");
		UiColumn col6 = new UiColumn("电话", "phone", true, "*");
		UiColumn col7 = new UiColumn("生日", "birthday", true, "*");
		UiColumn col8 = new UiColumn("文件路径", "picurl", true, "*");
		UiColumn col9 = new UiColumn("公司ID", "companyId", false, "*");
		UiColumn col10 = new UiColumn("公司名称", "companyName", true, "*");
		UiColumn col11 = new UiColumn("isactive", "isactive", false, "*");
		UiColumn col12 = new UiColumn("状态", "isactiveName", true, "*");
		UiColumn col14 = new UiColumn("邮箱", "email", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col13);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col14);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
		
		return cols;
	}*/
	
	/*
	 * 为人员管理查询结果
	 */
	public List<SysUser> getRows4UserList(SysUser model){
		//usersDao.checkDbBack();
		return usersDao.getRows4UserList(model);
	}
	
	/*
	 * 修改人员信息
	 */
	public Result saveUserChange(SysUser user){
		return usersDao.saveUserChange(user);
	}
	/*
	 * 新增人员信息
	 * 
	 */
	public Result saveNewUser(SysUser user){
		return usersDao.saveNewUser(user);
	}
	
	/*
	 * 获得所有有效公司下的所有有效人员的列表
	 * 根据角色,将角色权限内的人物选定
	 * 公司按照公司CODE排序,
	 * 人员按照职位CODE排序
	 */
	public List<ZTreeModel> getCompUsers4ZTree(RoleModel role){
		//定义总的返回树
		List<ZTreeModel> trees = new ArrayList<ZTreeModel>();
		//初始化第一列数据
		ZTreeModel com = new ZTreeModel("C0000", "0", "人员", null, true, 0, null, null, null, true);
		//得到公司列表集合
		List<ZTreeModel> comptrees = companyDao.getComList4ZTree();
		//得到人员列表集合
		List<ZTreeModel> usertrees = usersDao.getCompUsers4ZTree(role);
		trees.add(com);
		trees.addAll(comptrees);
		trees.addAll(usertrees);
		return trees;
	}
	/*
	 * 修改用户的人员信息
	 */
	public Result saveUserNowChange(SysUser model){
		return usersDao.saveUserNowChange(model);
	}
	/*
	 * 用户修改密码
	 */
	public Result saveUserPassword(SysUser model){
		return usersDao.saveUserPassword(model);
	}
	
	/*
	 * 获得拥有该站点权限下的所有用户的列表
	 */
	public List<SysUser> getUsersByStation(StationModel station){
		return usersDao.getUsersByStation(station);
	}
	/*
	 * 获得所有有效公司下的所有有效人员的列表
	 * 根据角色,将角色权限内的人物选定
	 * 公司按照公司CODE排序,
	 * 人员按照职位CODE排序
	 */
	public List<ZTreeModel> getCompUsers4StationSmsZTree(SmsSettingModel model){
		//定义总的返回树
		List<ZTreeModel> trees = new ArrayList<ZTreeModel>();
		//初始化第一列数据
		ZTreeModel com = new ZTreeModel("C0000", "0", "人员", null, true, 0, null, null, null, true);
		//得到公司列表集合
		List<ZTreeModel> comptrees = companyDao.getComList4ZTree();
		//得到人员列表集合
		List<ZTreeModel> usertrees = usersDao.getCompUsers4StatinSmsZTree(model);
		trees.add(com);
		trees.addAll(comptrees);
		trees.addAll(usertrees);
		return trees;
	}
}
