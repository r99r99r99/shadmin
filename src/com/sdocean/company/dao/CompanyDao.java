package com.sdocean.company.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.company.model.CompanyModel;
import com.sdocean.company.model.SysCompanyModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Component
public class CompanyDao extends OracleEngine {
	
	/*
	 * 获得所有的有效的公司列表
	 * 
	 */
	public List<SysCompanyModel> getSysCompList(){
		List<SysCompanyModel> complist = new ArrayList<>();
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.shortname,a.region_id,a.isactive,a.ordercode");
		sql.append(" from sys_company a");
		complist = this.queryObjectList(sql.toString(), SysCompanyModel.class);
		return complist;
	}
	
	/*
	 * 获得所有有效的公司列表,按照等级排序编码排序
	 */
	public List<CompanyModel> getCompanyList(CompanyModel model){
		List<CompanyModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select id,code,pcode,name,shortname,level,");
		sql.append(" isactive,case isactive when 0 then '禁用' else '启用' end as isactivename,ordercode");
		sql.append(" from g_company ");
		sql.append(" where 1=1");
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and ( code like '%").append(model.getCode()).append("%' or");
			sql.append(" name like '%").append(model.getCode()).append("%' or");
			sql.append(" shortname like '%").append(model.getCode()).append("%' )");
		}
		//添加排序
		sql.append(" order by orderCode");
		list = this.queryObjectList(sql.toString(), CompanyModel.class);
		return list;
	}
	/*
	 * 修改更改
	 */
	public Result saveCompanyChange(CompanyModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//判断CODE是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from g_company where code = '").append(model.getCode()).append("' and id <> ").append(model.getId());
		int check = 0;
		try {
			check=this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("检查Code唯一性时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("违反CODE唯一性原则");
			return result;
		}
		//执行修改代码
		StringBuffer sql = new StringBuffer("");
		sql.append("update g_company set code=?,pcode=?,name=?,shortname=?,level=?,isactive=?,ordercode=? where id=?");
		Object[] params = new Object[]{
				model.getCode(),model.getPcode(),model.getName(),model.getShortName(),
				model.getLevel(),model.getIsactive(),model.getOrderCode(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
			return result;
		}
		return result;
	}
	
	/*
	 * 新增保存
	 */
	public Result saveNewCompany(CompanyModel model){
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//判断CODE是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from g_company where code = '").append(model.getCode()).append("'  ");
		int check = 0;
		try {
			check=this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("检查Code唯一性时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("违反CODE唯一性原则");
			return result;
		}
		//执行修改代码
		StringBuffer sql = new StringBuffer("");
		sql.append("insert into g_company(code,pcode,name,shortname,level,isactive,ordercode) values(?,?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getPcode(),model.getName(),model.getShortName(),
				model.getLevel(),model.getIsactive(),model.getOrderCode()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		return result;
	}
	
	/*
	 * 保存删除功能
	 */
	public Result deleCompany(CompanyModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		//开始删除
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from g_company where id = ").append(model.getId());
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败,请重试");
		}
		return result;
	}
	
	/*
	 * 根据当前的company获得所有有效的pcode树
	 * 排除当前的company
	 */
	public List<SelectTree> showCompanyTree4Pcode(CompanyModel model){
		List<SelectTree>  list = new ArrayList<SelectTree>();
		//获得第一层数据
		SelectTree first = new SelectTree();
		StringBuffer firstSql = new StringBuffer("");
		firstSql.append(" select code as id,shortName as name ,'true' as isExpanded,");
		firstSql.append(" case when code = '").append(model.getPcode()).append("' then 'true' else 'false' end as selected from g_company where code = '0001'");
		firstSql.append(" and code <> '").append(model.getCode()).append("' limit 1");
		first = this.queryObject(firstSql.toString(), SelectTree.class);
		//根据第一层,获得他的子类
		if(first!=null){
			this.getChildTree4Pcode(first, model);
		}else{
			first = new SelectTree();
			first.setName("当前已经是最高等级");
		}
		list.add(first);
		return list;
	}
	
	public void getChildTree4Pcode(SelectTree pmodel,CompanyModel model){
		List<SelectTree> children = new ArrayList<SelectTree>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select code as id,shortName as name ,'true' as isExpanded,");
		sql.append(" case when code = '").append(model.getPcode()).append("' then 'true' else 'false' end as selected ");
		sql.append(" from g_company where isactive = 1 and pcode = '").append(pmodel.getId()).append("'");
		sql.append(" and code <> '").append(model.getCode()).append("'");
		children = this.queryObjectList(sql.toString(), SelectTree.class);
		for(SelectTree child:children){
			this.getChildTree4Pcode(child, model);
		}
		pmodel.setChildren(children);
	}
	/*
	 * 根据人员获得公司数
	 */
	public List<SelectTree> showComList4Users(SysUser model){
		List<SelectTree>  list = new ArrayList<SelectTree>();
		//获得第一层数据
		SelectTree first = new SelectTree();
		StringBuffer firstSql = new StringBuffer("");
		firstSql.append(" select code as id,shortName as name ,'true' as isExpanded,");
		firstSql.append(" case when code = '").append(model.getCompanyId()).append("' then 'true' else 'false' end as selected");
		firstSql.append(" from g_company where code = '0001' and isactive = 1 limit 1");
		first = this.queryObject(firstSql.toString(), SelectTree.class);
		this.getChildCom4Pcom(first, model);
		list.add(first);
		return list;
	}
	
	//根据上层公司,以及人员信息,获得下层的公司树
	public void getChildCom4Pcom(SelectTree parent,SysUser user){
		List<SelectTree> children = new ArrayList<SelectTree>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select code as id,shortName as name ,'true' as isExpanded,");
		sql.append(" case when code ='").append(user.getCompanyId()).append("' then 'true' else 'false' end as selected ");
		sql.append(" from g_company where isactive = 1 and pcode = '").append(parent.getId()).append("'");
		children = this.queryObjectList(sql.toString(), SelectTree.class);
		for(SelectTree child:children){
			this.getChildCom4Pcom(child, user);
		}
		parent.setChildren(children);
	}
	
	/*
	 * 以ztree的形式获得公司列表
	 * 
	 */
	public List<ZTreeModel> getComList4ZTree(){
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select concat('C',a.code) as id,concat('C',a.pcode) as pid,a.shortname as name,");
		sql.append(" 'true' as open,'true' as nocheck");
		sql.append(" from g_company a where a.isactive = 1");
		list = this.queryObjectList(sql.toString(), ZTreeModel.class);
		return list;
	}
	/*
	 * 在下拉框中展示站点树
	 */
	public List<SelectTree> geCompanyListByStation(StationModel station){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		//得到第一层的地区代码
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.code as id,a.name as name,'true' as isExpanded,'true' as isActive,");
		sql.append(" case when a.code ='").append(station.getCompanyId()).append("'  then 'true' else 'false' end as selected ");
		sql.append(" from g_company a where a.pcode = '0000'");
		trees = this.queryObjectList(sql.toString(), SelectTree.class);
		for(SelectTree child:trees){
			this.setChildren4Tree(child,station);
		}
		return trees;
	}
	
	/*
	 * 为当前的站点添加他的子元素
	 */
	public void setChildren4Tree(SelectTree model,StationModel station){
		List<SelectTree> child = new ArrayList<SelectTree>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select a.code as id,a.name as name,'true' as isExpanded,case when a.code = '").append(station.getCompanyId()).append("' then 'true' else 'false' end as selected");
		sql.append(" from g_company a where a.pcode = '").append(model.getId()).append("'");
		child = this.queryObjectList(sql.toString(), SelectTree.class);
		if(child!=null&&child.size()>0){
			for(SelectTree children:child){
				this.setChildren4Tree(children,station);
			}
			model.setChildren(child);
		}
	}
	/*
	 * 根据组织机构CODE获得组织机构列表
	 */
	public List<CompanyModel> getComListByCode(String code){
		List<CompanyModel> list = new ArrayList<>();
		CompanyModel comp = new CompanyModel();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select p.code,p.pcode,p.name,p.shortname,");
		sql.append(" p.level,p.isactive,p.ordercode");
		sql.append(" from g_company p");
		sql.append(" where p.isactive = 1 ");
		sql.append(" and p.code = '").append(code).append("'");
		comp = this.queryObject(sql.toString(), CompanyModel.class);
		list.add(comp);
		if(comp!=null&&!comp.getPcode().equals("0000")){
			list.addAll(this.getComListByCode(comp.getPcode()));
		}
		
		return list;
	}
}
