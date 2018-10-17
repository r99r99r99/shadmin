package com.sdocean.region.dao;

import java.util.ArrayList;
import java.util.List;

import jxl.biff.drawing.ComboBox;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.CombotreeModel;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.region.model.RegionModel;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Component
public class RegionDao extends OracleEngine{
	
	/*
	 * 在下拉框中展示站点树
	 */
	public List<SelectTree> getRegionList4Tree(StationModel station){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		//得到第一层的地区代码
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name as name,'true' as isExpanded,'true' as isActive,");
		sql.append(" case when a.id =").append(station.getRegion_id()).append("  then 'true' else 'false' end as selected ");
		sql.append(" from g_region a where level = 1");
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
		sql.append("select a.id,a.name as name,'false' as isExpanded,case when a.id = ").append(station.getRegion_id()).append(" then 'true' else 'false' end as selected");
		sql.append(" from g_region a where a.pid = ").append(model.getId());
		sql.append(" and a.level <4");
		child = this.queryObjectList(sql.toString(), SelectTree.class);
		if(child!=null&&child.size()>0){
			for(SelectTree children:child){
				this.setChildren4Tree(children,station);
			}
			model.setChildren(child);
		}
	}
	
	/*
	 * 查询ZTREE LIST中的上级元素
	 */
	public List<ZTreeModel> getParentRegions(List<ZTreeModel> children){
		List<ZTreeModel> list = new ArrayList<>();
		//拼接PIDS
		String pids ="0";
		for(ZTreeModel child:children){
			pids = pids + "," +child.getpId();
		}
		
		//查询当前等级的ZTREE
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.pid,'true' as open , a.name,'true' as nocheck");
		sql.append(" from g_region a");
		sql.append(" where a.id in (").append(pids).append(")");
		
		list = this.queryObjectList(sql.toString(), ZTreeModel.class);
		List<ZTreeModel> child = new ArrayList<ZTreeModel>();
		if(list.size()>0){
			child = this.getParentRegions(list);
		}
		list.addAll(child);
		
		return list;
	}
	/*
	 * 根据地区ID获得上级所有的地区信息
	 */
	public List<RegionModel> getRegionListById(int id){
		List<RegionModel>  list = new ArrayList<>();
		RegionModel  region = new RegionModel();
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,pid,code,name as text,level");
		sql.append(" from g_region ");
		sql.append(" where id =").append(id);
		region = this.queryObject(sql.toString(), RegionModel.class);
		if(region!=null){
			list.add(region);
			if(region.getPid()!=0){
				list.addAll(this.getRegionListById(region.getPid()));
			}
		}
		return list;
	}
	 
}
