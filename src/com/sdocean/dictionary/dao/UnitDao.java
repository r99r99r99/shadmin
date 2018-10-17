package com.sdocean.dictionary.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.UnitGroupModel;
import com.sdocean.dictionary.model.UnitModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;

@Component
public class UnitDao extends OracleEngine {
	
	/*
	 * 得到所有的单位的列表
	 */
	public List<UnitModel> getUnitList(UnitModel model){
		List<UnitModel> list = new ArrayList<UnitModel>();
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.groupId,c.name as groupName,a.name,a.description,a.logo,a.standardcode");
		sql.append(" from g_unit a left join g_unit_group c on a.groupid = c.id");
		sql.append(" where 1 =1");
		//增加模糊查询条件
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and ( a.code like '%").append(model.getCode()).append("%' or ");
			sql.append(" a.name like '%").append(model.getCode()).append("%' )");
		}
		//增加分组查询条件
		if(model!=null&&model.getGroupId()>0){
			sql.append(" and a.groupId =").append(model.getGroupId());
		}
		//增加排序
		sql.append(" order by a.id");
		list = this.queryObjectList(sql.toString(), UnitModel.class);
		return list;
	}
	
	/*
	 * 保存单位的修改
	 */
	public Result saveUnitChange(UnitModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		StringBuffer sql = new StringBuffer("");
		sql.append("update g_unit set code =?,groupId=?,name=?,description=?,logo=?,standardcode=? where id =?");
		Object[] params = new Object[]{
				model.getCode(),model.getGroupId(),model.getName(),
				model.getDescription(),model.getLogo(),model.getStandardCode(),model.getId()
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
	 * 保存单位的创建
	 */
	public Result saveNewUnit(UnitModel model){
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		StringBuffer sql = new StringBuffer("");
		sql.append("insert into g_unit(code,groupId,name,description,logo,standardcode) values (?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getGroupId(),model.getName(),
				model.getDescription(),model.getLogo(),model.getStandardCode()
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
	 * 根据单位code获得单位信息
	 */
	public UnitModel getUnitByCode(String code){
		UnitModel unit = new UnitModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.groupId,c.name as groupName,a.name,a.description,a.logo,b.value as isactiveName,a.standardcode");
		sql.append(" from g_unit a left join g_unit_group c on a.groupid = c.id");
		sql.append(" where a.code = '").append(code).append("'");
		unit = this.queryObject(sql.toString(), UnitModel.class);
		return unit;
	}
	
	/*
	 * 以树的形式展示单位列表
	 */
	public List<SelectTree> getUnitList4Tree(UnitModel model){
		int id = 0;
		if(model!=null&&model.getId()>0){
			id = model.getId();
		}
		List<SelectTree> list = new ArrayList<>();
		//得到单位组的列表
		StringBuffer groupSql = new StringBuffer("");
		groupSql.append(" select id as id,name,'true' as isExpanded,'true' as isactive");
		groupSql.append(" from g_unit_group");
		list = this.queryObjectList(groupSql.toString(), SelectTree.class);
		//遍历单位组,获得单位组下的单位信息
		for(SelectTree tree:list){
			List<SelectTree> child = new ArrayList<SelectTree>();
			StringBuffer indiSql = new StringBuffer("");
			indiSql.append(" select id,concat(name,case when logo is null then '' when logo='' then '' else concat('(',logo,')') end ) as name,'true' as isExpanded, ");
			indiSql.append(" case when id = ").append(id).append(" then 'true' else 'false' end as selected");
			indiSql.append(" from g_unit");
			indiSql.append(" where groupid = ").append(tree.getId());
			child = this.queryObjectList(indiSql.toString(), SelectTree.class);
			if(child!=null&&child.size()>0){
				tree.setChildren(child);
			}
		}
		//删除没有子元素的数据
		List<SelectTree> listnew = new ArrayList<SelectTree>();
		for(SelectTree tree:list){
			if(tree.getChildren()!=null&&tree.getChildren().size()>0){
				listnew.add(tree);
			}
		}
		return listnew;
	}
	
}
