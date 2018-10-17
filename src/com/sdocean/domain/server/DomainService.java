package com.sdocean.domain.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dictionary.dao.PublicDao;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.domain.dao.DomainDao;
import com.sdocean.domain.model.DomainForm;
import com.sdocean.domain.model.DomainIndicator;
import com.sdocean.domain.model.DomainLevel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.domain.model.DomainResult;
import com.sdocean.domain.model.DomainThreshold;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class DomainService {
	
	@Autowired
	private DomainDao domainDao;
	
	
	public List<UiColumn> getCols4DomainList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("编码", "code", true, "*");
		UiColumn col3 = new UiColumn("名称", "name", true, "*");
		UiColumn col4 = new UiColumn("状态", "isactiveName", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		return cols;
	}
	
	/*
	 * 展示功能区的列表
	 */
	public List<DomainModel> getDomainList(DomainModel model){
		return domainDao.getDomainList(model);
	}
	
	/*
	 * 新增功能区
	 */
	public Result saveNewDomain(DomainModel model){
		return domainDao.saveNewDomain(model);
	}
	
	/*
	 * 修改功能区
	 */
	public Result saveChangeDomain(DomainModel model){
		return domainDao.saveChangeDomain(model);
	}
	/*
	 * 停用功能区
	 */
	public Result deleDomain(DomainModel model){
		return domainDao.deleDomain(model);
	}
	
	/*
	 * 保存功能区--站点--参数权限
	 */
	public Result saveDomainStationIndicator(DomainModel model){
		return domainDao.saveDomainStationIndicator(model);
	}
	
	/*
	 * 获得该站点的功能区的信息
	 */
	public List<DomainResult> getDomainResultsByStation(StationModel station){
		//初始化返回结果
		List<DomainResult> results = new ArrayList<DomainResult>();
		//根据站点获得该站点分属的功能区列表
		List<DomainModel> domains = domainDao.getDomainModelsByStation(station);
		//遍历功能区列表
		for(DomainModel domain:domains){
			DomainResult dr = new DomainResult();
			//获得该功能区下的关注的参数列表
			List<IndicatorModel> indicators = domainDao.getIndicatorsByDomain(domain);
			//根据站点以及参数列表获得关注的参数的结果集
			List<DomainIndicator> dis = domainDao.getDomainIndicator4Now(station, indicators);
			if(dis!=null&&dis.size()>0){
				dr.setDomain(domain);
				dr.setDomainIndicators(dis);
				results.add(dr);
			}
		}
		return results;
	}
	
	/*
	 * 通过功能区查询功能区的层级的表头
	 */
	public List<UiColumn> getCols4DomainLevelList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("domainId", "domainId", false, "*");
		UiColumn col3 = new UiColumn("功能区名称", "domainName", false, "*");
		UiColumn col4 = new UiColumn("编码", "code", true, "*");
		UiColumn col5 = new UiColumn("层级名称", "name", true, "*");
		UiColumn col6 = new UiColumn("颜色", "color", true, "*");
		UiColumn col7 = new UiColumn("备注", "remark", false, "*");
		UiColumn col8 = new UiColumn("排序", "orderCode", false, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		return cols;
	}
	/*
	 * 通过功能区查询功能区的层级
	 */
	public List<DomainLevel> getLevelListByDomain(DomainLevel model){
		return domainDao.getLevelListByDomain(model);
	}
	/*
	 * 保存新的功能区层级
	 */
	public Result saveNewDomainLevel(DomainLevel level) {
		return domainDao.saveNewDomainLevel(level);
	}
	/*
	 * 保存修改的功能区层级
	 */
	public Result updateDomainLevel(DomainLevel level) {
		return domainDao.updateDomainLevel(level);
	}
	/*
	 * 删除功能区层级
	 */
	public Result deleDomainLevel(DomainLevel level) {
		return domainDao.deleDomainLevel(level);
	}
	/*
	 * 获得功能区阈值的表头
	 */
	public List<UiColumn> getThresholdCols4Domain(DomainThreshold model){
		return domainDao.getThresholdCols4Domain(model);
	}
	/*
	 * 获得功能区阈值的范围
	 */
	public List<Map<String, Object>> getThresholdRows4Domain(DomainThreshold model){
		return domainDao.getThresholdRows4Domain(model);
	}
	/*
	 * 查询功能区某参数的阈值设置的表头
	 */
	public List<UiColumn> getThresholdColsByDomainIndicator(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", true, "*");
		UiColumn col2 = new UiColumn("levelId", "levelId", true, "*");
		UiColumn col21 = new UiColumn("层级", "levelName", true, "*");
		UiColumn col3 = new UiColumn("下限计算方式", "mincal", true, "*");
		UiColumn col4 = new UiColumn("下限值", "min", true, "*");
		col4.setType("double");
		UiColumn col5 = new UiColumn("上限计算方式", "maxcal", true, "*");
		
		col5.setType("boolean");
		UiColumn col6 = new UiColumn("上限值", "max", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col21);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		return cols;
	}
	
	/*
	 * 查询功能区内某参数的阈值设置
	 */
	public List<DomainThreshold> getThresholdListByDomainIndicator(DomainThreshold model){
		return domainDao.getThresholdListByDomainIndicator(model);
	}
	
	/*
	 * 删除选中的阈值设置
	 */
	public Result deleDomainThreshold(DomainThreshold model) {
		return domainDao.deleDomainThreshold(model);
	}
	
	/*
	 * 保存阈值配置
	 */
	public Result saveDomainThreshold(DomainForm model) {
		return domainDao.saveDomainThreshold(model);
	}
}
