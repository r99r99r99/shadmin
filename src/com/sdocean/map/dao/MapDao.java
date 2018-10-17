package com.sdocean.map.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.map.model.MapModel;

@Component
public class MapDao extends OracleEngine {
	
	/*
	 * 查询map表的配置信息
	 */
	public MapModel getMapConfigure(){
		List<MapModel> list = new ArrayList<MapModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.url,a.format,a.version,a.tiled,a.styles,a.transparent,a.layers,a.code");
		sql.append(" ,a.initzoom as initZoom,a.maxzoom as maxZoom,a.minzoom as minZoom");
		sql.append(" from g_map a");
		list=this.queryObjectList(sql.toString(), MapModel.class);
		return list.get(0);
	}
	
	/*
	 * 更改系统的地图配置信息
	 */
	public Result saveMapConfig(MapModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(result));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" update g_map set url=?,format=?,version=?,tiled=?,styles=?,transparent=?,layers=?,code=?,initzoom=?,maxzoom=?,minzoom=?");
		sql.append(" where id=?");
		Object[] params = new Object[]{
				model.getUrl(),model.getFormat(),model.getVersion(),model.getTiled(),
				model.getStyles(),model.getTransparent(),model.getLayers(),model.getCode(),
				model.getInitZoom(),model.getMaxZoom(),model.getMinZoom(),model.getId()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
		}
		return result;
	}
}
