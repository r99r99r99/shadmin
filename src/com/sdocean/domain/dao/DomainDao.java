package com.sdocean.domain.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.domain.model.DomainForm;
import com.sdocean.domain.model.DomainIndicator;
import com.sdocean.domain.model.DomainLevel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.domain.model.DomainThreshold;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.metadata.dao.MetadataDao;
import com.sdocean.metadata.dao.MetadataTableDao;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Component
public class DomainDao extends OracleEngine{
	
	@Resource
	MetadataTableDao tableDao;
	/*
	 * 展示功能区的列表
	 */
	public List<DomainModel> getDomainList(DomainModel model){
		//初始化返回结果
		List<DomainModel> list = new ArrayList<DomainModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.remark,a.isactive,");
		sql.append(" b.value as isactivename,a.ordercode");
		sql.append(" from aiot_domain a,sys_public b");
		sql.append(" where a.isactive = b.classid");
		sql.append(" and b.parentcode = '0004'");
		//添加查询条件
		if(model.getIsactive()<2){ //当isactive = 2时,代表查询所有功能区
			sql.append(" and a.isactive = ").append(model.getIsactive());
		}
		if(model.getName()!=null&&model.getName().length()>0){
			sql.append(" and a.name like '%").append(model.getName()).append("%' ");
		}
		//添加排序
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), DomainModel.class);
		return list;
	}
	
	/*
	 * 新增功能区
	 */
	public Result saveNewDomain(DomainModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//查询code  name是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_domain where code = '").append(model.getCode()).append("' ");
		checkSql.append(" or name = '").append(model.getName()).append("'");
		int check = 0;
		try {
			check = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(result.FAILED);
			result.setMessage("查询唯一性时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("编码或名称重复");
			return result;
		}
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_domain(code,name,remark,isactive,ordercode)");
		sql.append(" values(?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getName(),model.getRemark(),model.getIsactive(),model.getOrderCode()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("新增失败");
			result.setResult(result.FAILED);
		}
		return result;
	}
	
	/*
	 * 修改功能区
	 */
	public Result saveChangeDomain(DomainModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		
		//查询code  name是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_domain where (code = '").append(model.getCode()).append("' ");
		checkSql.append(" or name = '").append(model.getName()).append("') and id <>").append(model.getId());
		int check = 0;
		try {
			check = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(result.FAILED);
			result.setMessage("查询唯一性时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("编码或名称重复");
			return result;
		}		
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_domain set code=?,name=?,remark=?,isactive=?,ordercode=? ");
		sql.append(" where id = ?");
		Object[] params = new Object[]{
				model.getCode(),model.getName(),model.getRemark(),model.getIsactive(),model.getOrderCode(),model.getId()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("新增失败");
			result.setResult(result.FAILED);
		}
		return result;
	}
	
	/*
	 * 停用功能区
	 */
	public Result deleDomain(DomainModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("禁用成功");
		StringBuffer sql = new StringBuffer("");
		sql.append("update aiot_domain set isactive = 0 where id = ").append(model.getId());
		try {
			this.update(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("停用失败");
			result.setResult(result.FAILED);
		}
		return result;
	}
	
	/*
	 * 保存功能区--站点--参数权限
	 */
	public Result saveDomainStationIndicator(DomainModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//删除功能区站点之间原有的记录
		StringBuffer deleStation = new StringBuffer("");
		deleStation.append("delete from sys_domain_station where domainid = ").append(model.getId());
		try {
			this.execute(deleStation.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("重置功能区站点时错误");
			return result;
		}
		//处理站点集合
		String[] stationids = model.getStationIds().split(",");
		StringBuffer stationValue = new StringBuffer("(0,0)");
		for(int i=0;i<stationids.length;i++){
			stationValue.append(",(").append(model.getId()).append(",").append(stationids[i]).append(")");
		}
		//执行插入语句
		StringBuffer addStation = new StringBuffer("");
		addStation.append(" insert into sys_domain_station(domainid,stationid)");
		addStation.append(" values").append(stationValue);
		addStation.append(" on duplicate  key update stationid = values(stationid)");
		try {
			this.execute(addStation.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存功能区站点时错误");
			return result;
		}
		
		
		return result;
	}
	
	/*
	 * 根据站点获得该站点所属的功能区列表
	 */
	public List<DomainModel> getDomainModelsByStation(StationModel station){
		List<DomainModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.remark,a.isactive,a.ordercode");
		sql.append(" from aiot_domain a,sys_domain_station b");
		sql.append(" where a.id = b.domainid and a.isactive = 1");
		sql.append(" and stationid =").append(station.getId());
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), DomainModel.class);
		return list;
	}
	
	/*
	 * 获得该功能区下的关注的参数列表
	 */
	public List<IndicatorModel> getIndicatorsByDomain(DomainModel domain){
		//初始化返回结果
		List<IndicatorModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.groupid,a.unitid,b.logo as unitname,");
		sql.append(" a.description,a.isactive,a.ordercode");
		sql.append(" from dm_indicator a,g_unit b,sys_domain_indicator c");
		sql.append(" where a.code = c.indicatorcode and a.unitid = b.id");
		sql.append(" and a.isactive = 1 and c.domainid = ").append(domain.getId());
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), IndicatorModel.class);
		return list;
	}
	
	/*
	 * 根据站点以及参数获得实时数据
	 */
	public List<DomainIndicator> getDomainIndicator4Now(StationModel station,List<IndicatorModel> indicators){
		List<DomainIndicator> list = new ArrayList<DomainIndicator>();
		//获得当前时间
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String collect_time = sdf.format(nowDate);
		//得到需要查询的表
		MetadataTable table = tableDao.getOneTable(station, collect_time, 1);
		//遍历参数表,得到每个参数的最终结果
		for(IndicatorModel indicator:indicators){
			DomainIndicator dd = new DomainIndicator();
			StringBuffer sql = new StringBuffer("");
			sql.append(" select collect_time as collecttime,data");
			sql.append(" from ").append(table.getTableName());
			sql.append(" where wpid = ").append(station.getId());
			sql.append(" and indicator_code = '").append(indicator.getCode()).append("'");
			sql.append(" and data is not null and data <> 88888");
			sql.append(" order by collect_time desc limit 1");
			dd=this.queryObject(sql.toString(), DomainIndicator.class);
			if(dd!=null&&dd.getData()!=null&&dd.getData().length()>0){
				dd.setIndicatorCode(indicator.getCode());
				dd.setIndicatorName(indicator.getTitle());
				dd.setData(dd.getData()+"("+indicator.getUnitName()+")");
				list .add(dd);
			}
		}
		return list;
	}
	
	/*
	 * 通过功能区查询功能区的层级
	 */
	public List<DomainLevel> getLevelListByDomain(DomainLevel model){
		List<DomainLevel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.domainid,b.name as domainname,");
		sql.append(" a.code,a.name,a.remark,a.color,a.ordercode");
		sql.append(" from sys_domain_level a,aiot_domain b");
		sql.append(" where a.domainid = b.id");
		sql.append(" and b.isactive = 1");
		sql.append(" and a.domainid = ").append(model.getDomainId());
		sql.append(" order by ordercode");
		list = this.queryObjectList(sql.toString(), DomainLevel.class);
		return list;
	}
	
	/*
	 * 保存新的功能区层级
	 */
	public Result saveNewDomainLevel(DomainLevel level) {
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(level));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_domain_level(domainid,code,name,remark,color,ordercode)");
		sql.append(" values(?,?,?,?,?,?) on duplicate  key update name=values(name),");
		sql.append(" remark=values(remark),");
		sql.append(" color=values(color),");
		sql.append(" ordercode=values(ordercode)");
		Object[] param = new Object[] {
				level.getDomainId(),level.getCode(),level.getName(),
				level.getRemark(),level.getColor(),level.getOrderCode()
		};
 		try {
			this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
		}
		return result;
	}
	
	/*
	 * 保存修改的功能区层级
	 */
	public Result updateDomainLevel(DomainLevel level) {
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(level));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//验证是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from sys_domain_level");
		checkSql.append(" where domainid = ").append(level.getDomainId());
		checkSql.append(" and code = '").append(level.getCode()).append("'");
		checkSql.append(" and id <> ").append(level.getId());
		int num = 0;
		try {
			num = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(result.FAILED);
			result.setMessage("查询唯一性失败");
			return result;
		}
		if(num>0) {
			result.setResult(result.FAILED);
			result.setMessage("违反唯一性原则");
			return result;
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" update sys_domain_level set domainid=?,code=?,name=?,remark=?,color=?,ordercode=?");
		sql.append(" where id=?");
		Object[] param = new Object[] {
				level.getDomainId(),level.getCode(),level.getName(),
				level.getRemark(),level.getColor(),level.getOrderCode(),level.getId()
		};
 		try {
			this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
		}
		return result;
	}
	
	/*
	 * 删除功能区层级
	 */
	public Result deleDomainLevel(DomainLevel level) {
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(level));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from sys_domain_level  where id=?");
		Object[] param = new Object[] {
				level.getId()
		};
 		try {
			this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	
	/*
	 * 获得功能区阈值的表头
	 */
	public List<UiColumn> getThresholdCols4Domain(DomainThreshold model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		//初始化参数列
		UiColumn col1 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col2 = new UiColumn("参数", "indicatorName", true, "*");
		cols.add(col1);
		cols.add(col2);
		//得到该功能区层级的列表
		DomainLevel domain = new DomainLevel();
		domain.setDomainId(model.getDomainId());
		List<DomainLevel> levels = this.getLevelListByDomain(domain);
		for(DomainLevel level:levels) {
			UiColumn colminValue = new UiColumn(level.getName()+"低值", "minValue"+level.getId(), true, "*");
			UiColumn colminCal = new UiColumn(level.getName()+"mincal", "minCal"+level.getId(), false, "*");
			UiColumn colmin = new UiColumn(level.getName()+"min", "min"+level.getId(), false, "*");
			UiColumn colmaxValue = new UiColumn(level.getName()+"高值", "maxValue"+level.getId(), true, "*");
			UiColumn colmaxCal = new UiColumn(level.getName()+"maxcal", "maxCal"+level.getId(), false, "*");
			UiColumn colmax = new UiColumn(level.getName()+"max", "max"+level.getId(), false, "*");
			cols.add(colminValue);
			cols.add(colminCal);
			cols.add(colmin);
			cols.add(colmaxValue);
			cols.add(colmaxCal);
			cols.add(colmax);
		}
		return cols;
	}
	
	
	/*
	 * 获得功能区阈值的范围
	 */
	public List<Map<String, Object>> getThresholdRows4Domain(DomainThreshold model){
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		//得到该功能区层级的列表
		DomainLevel domain = new DomainLevel();
		domain.setDomainId(model.getDomainId());
		List<DomainLevel> levels = this.getLevelListByDomain(domain);
		
		//初始化SQL语句
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" select c.code as indicatorCode,c.title as indicatorName");
		for(DomainLevel level:levels) {
			sql.append(" ,case when sum(case when a.levelid = ").append(level.getId()).append(" then mincal else 0 end)=1 then '>=' else '>' end as minCal").append(level.getId());
			sql.append(" ,sum(case when a.levelid = ").append(level.getId()).append(" then min else 0 end ) as min").append(level.getId());
			sql.append(" ,case when sum(case when a.levelid = ").append(level.getId()).append(" then maxcal else 0 end )=1 then '<=' else '<' end as maxCal").append(level.getId());
			sql.append(" ,sum(case when a.levelid = ").append(level.getId()).append(" then max else 0 end ) as max").append(level.getId());
		}
		sql.append(" from sys_domain_threshold a,sys_domain_level b,dm_indicator c");
		sql.append(" where a.levelid = b.id");
		sql.append(" and a.indicatorcode = c.code");
		sql.append(" and b.domainid = ").append(model.getDomainId());
		sql.append(" group by c.code,c.title");
		rows = this.queryForList(sql.toString());
		for(Map<String, Object> m:rows) {
			for(DomainLevel level:levels) {
				m.put("minValue"+level.getId(), m.get("minCal"+level.getId()).toString() +m.get("min"+level.getId()).toString());
				m.put("maxValue"+level.getId(), m.get("maxCal"+level.getId()).toString() +m.get("max"+level.getId()).toString());
			}
		}
		return rows;
	}
	
	/*
	 * 查询功能区内某参数的阈值设置
	 */
	public List<DomainThreshold> getThresholdListByDomainIndicator(DomainThreshold model){
		List<DomainThreshold> list = new ArrayList<DomainThreshold>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select b.id,a.id as levelid,a.name as levelName,a.domainid,b.indicatorcode,");
		sql.append("  b.mincal,b.min,b.maxcal,b.max");
		sql.append(" from sys_domain_level a ");
		sql.append(" left join sys_domain_threshold b on a.id = b.levelid");
		sql.append(" and b.indicatorcode = '").append(model.getIndicatorCode()).append("'");
		sql.append(" where a.domainid = ").append(model.getDomainId());
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), DomainThreshold.class);
		return list;
	}
	
	/*
	 * 删除选中的阈值设置
	 */
	public Result deleDomainThreshold(DomainThreshold model) {
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from sys_domain_threshold where id =").append(model.getId());
		try {
			this.execute(sql.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	/*
	 * 保存阈值配置
	 */
	public Result saveDomainThreshold(DomainForm model) {
		
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//删除原有的记录
		StringBuffer deleSql = new StringBuffer("");
		deleSql.append(" delete from sys_domain_threshold");
		deleSql.append(" where levelid in (select id from sys_domain_level where domainid = ").append(model.getDomainId()).append(")");
		deleSql.append(" and indicatorcode = '").append(model.getIndicatorCode()).append("'");
		try {
			this.execute(deleSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("删除原有记录失败");
			return result;
		}
		//保存新的记录
		String dataText = model.getDataText();
		List<DomainThreshold> dtlist = JsonUtil.fromJsons(dataText, DomainThreshold.class);
		if(dtlist==null||dtlist.size()<1) {
			result.setMessage("删除原有记录成功,未添加新数据");
			return result;
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_domain_threshold(indicatorcode,levelid,mincal,min,maxcal,max) values ");
		for(int i=0;i<dtlist.size();i++) {
			DomainThreshold dt = dtlist.get(i);
			if(dt.getMincal()!=null&&dt.getMincal().length()>0
					&&dt.getMin()!=null&&dt.getMin().length()>0
					&&dt.getMaxcal()!=null&&dt.getMaxcal().length()>0
					&&dt.getMax()!=null&&dt.getMax().length()>0) {
				if(i>0) {
					sql.append(",");
				}
				sql.append(" ('").append(model.getIndicatorCode()).append("',").append(dt.getLevelId()).append(",");
				sql.append(dt.getMincal()).append(",").append(dt.getMin()).append(",").append(dt.getMaxcal()).append(",").append(dt.getMax()).append(")");
			}
		}
		try {
			this.execute(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("保存修改记录失败");
			return result;
		}
		return result;
	}
}
