package com.sdocean.warn.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.warn.dao.WarnDao;
import com.sdocean.warn.model.Warn4FirstModel;
import com.sdocean.warn.model.WarnModel;
import com.sdocean.warn.model.WarnValueModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class WarnService {

	@Resource
	WarnDao warnDao;
	/*
	 * 为首页读取预警信息
	 */
	public Warn4FirstModel getWarn4First(Long warnType,StationModel watchPoint,String beginDate){
		return warnDao.getWarn4First(warnType, watchPoint, beginDate);
	}
	
	/*
	 * 根据条件查询出预警告警配置列表
	 * 根据当前登录人员查询权限内的站点
	 */
	public List<WarnModel> getWarnList(WarnModel model,SysUser user){
		return warnDao.getWarnList(model, user);
	}

	public List<UiColumn> getCols4List() {
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("wpId", "wpId", false, "*");
		UiColumn col3 = new UiColumn("站点名称", "wpName", true, "*");
		UiColumn col4 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col5 = new UiColumn("参数名称", "indicatorName", true, "*");
		UiColumn col6 = new UiColumn("预警低值", "warnLower", true, "*");
		UiColumn col7 = new UiColumn("预警高值", "warnUpper", true, "*");
		UiColumn col8 = new UiColumn("告警低值", "alarmLower", true, "*");
		UiColumn col9 = new UiColumn("告警高值", "alarmUpper", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		
		return cols;
	}
	

	/*
	 * 修改预警告警配置
	 */
	public Result saveWarnChange(WarnModel model){
		return warnDao.saveWarnChange(model);
	}
	/*
	 * 保存新增预警告警配置
	 */
	public Result saveNewWarn(WarnModel model){
		return warnDao.saveNewWarn(model);
	}
	/*
	 * 删除选中的预警告警配置
	 */
	public Result deleWarnSetting(WarnModel model){
		return warnDao.deleWarnSetting(model);
	}
	/*
	 * 为预警告警信息展示添加表头
	 */
	public List<UiColumn> getCols4WarnValue() {
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("wpId", "wpId", false, "*");
		UiColumn col3 = new UiColumn("站点名称", "wpName", true, "*");
		UiColumn col4 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col5 = new UiColumn("设备名称", "deviceName", true, "*");
		UiColumn col6 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col7 = new UiColumn("参数名称", "indicatorName", true, "*");
		UiColumn col8 = new UiColumn("type", "type", false, "*");
		UiColumn col9 = new UiColumn("类型", "typeName", true, "*");
		UiColumn col10 = new UiColumn("数值", "value", true, "*");
		UiColumn col11 = new UiColumn("采集时间", "collect_time", true, "*");
		UiColumn col12 = new UiColumn("operate", "operate", false, "*");
		UiColumn col13 = new UiColumn("操作", "operateName", true, "*");
		UiColumn col14 = new UiColumn("备注", "flag", true, "*");
		UiColumn col15 = new UiColumn("说明", "remark", true, "*");
		UiColumn col16 = new UiColumn("配置下限", "minConfig", true, "*");
		UiColumn col17 = new UiColumn("配置上限 ", "maxConfig", true, "*");
		UiColumn col18 = new UiColumn("单位 ", "unitName", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col18);
		cols.add(col11);
		cols.add(col12);
		cols.add(col13);
		cols.add(col14);
		cols.add(col15);
		cols.add(col16);
		cols.add(col17);
		
		return cols;
	}
	
	/*
	 * 为预警告警信息展示添加结果集
	 */
	public List<WarnValueModel> getWarnValueRows(WarnValueModel model){
		return warnDao.getWarnValueRows(model);
	}	
	/*
	 * 为预警告警信息添加操作以及备注
	 */
	public Result saveOperaValue(WarnValueModel model){
		return warnDao.saveOperaValue(model);
	}
	/*
	 * 读取预警告警中的未操作的
	 */
	public Integer getNum4WarnValue(StationModel model){
		return warnDao.getNum4WarnValue(model);
	}
	/*
	 * 预警告警信息中批量操作
	 */
	public Result updateAllOperaValue(WarnValueModel model){
		return warnDao.updateAllOperaValue(model);
	}
}
