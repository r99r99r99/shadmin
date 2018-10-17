package com.sdocean.indicator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.indicator.dao.IndicatorDao;
import com.sdocean.indicator.model.IndicatorGroupModel;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class IndicatorService {
	
	@Autowired
	private IndicatorDao indicatorDao;
	
	/*
	 * 为公共代码管理提供表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("名称", "title", true, "*");
		UiColumn col4 = new UiColumn("groupId", "groupId", false, "*");
		UiColumn col5 = new UiColumn("分组", "groupName", true, "*");
		UiColumn col6 = new UiColumn("unitId", "unitId", false, "*");
		UiColumn col7 = new UiColumn("单位", "unitName", true, "*");
		UiColumn col8 = new UiColumn("描述", "description", true, "*");
		UiColumn col9 = new UiColumn("isactive", "isactive", false, "*");
		UiColumn col10 = new UiColumn("状态", "isactiveName", true, "*");
		UiColumn col11 = new UiColumn("排序", "orderCode", true, "*");
		UiColumn col12 = new UiColumn("标准编码", "standardCode", true, "*");
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
		cols.add(col11);
		cols.add(col12);
		return cols;
	}
	/*
	 * 查询符合条件的参数集合
	 */
	public List<IndicatorModel> showIndicators(IndicatorModel model){
		return indicatorDao.showIndicators(model);
	}
	/*
	 * 保存修改的参数
	 */
	public Result saveIndicatorChange(IndicatorModel model){
		return indicatorDao.saveIndicatorChange(model);
	}
	/*
	 * 保存新增的参数
	 */
	public Result saveNewIndicator(IndicatorModel model){
		return indicatorDao.saveNewIndicator(model);
	}
	
	/*
	 * 得到参数的下拉树
	 * 根据当前的设备选中已有的菜单
	 */
	public List<SelectTree> getIndicatorList4Tree(DeviceModel model){
		return indicatorDao.getIndicatorList4Tree(model);
	}
	
	/*
	 * 以树的形式展示当前站点的所包含设备的所有参数的列表
	 */
	public List<SelectTree> getIndicators4StationDevice(StationModel model){
		return indicatorDao.getIndicators4StationDevice(model);
	}
	/*
	 * 以树的形式展示当前站点的所包含有展示权限的设备以及有展示权限的参数的列表
	 */
	public List<SelectTree> getIndicators4StationDevice4Show(StationModel model){
		return indicatorDao.getIndicators4StationDevice4Show(model);
	}
	/*
	 * 以树的形式展示当前站点的所包含有展示权限的设备以及有展示权限的参数的列表
	 */
	public List<SelectTree> getIndicators4Pollu(StationModel model){
		return indicatorDao.getIndicators4Pollu(model);
	}
	/*
	 * 获得站点内的参数列表
	 */
	public List<IndicatorModel> getIndicatorsByStation(StationModel model){
		return indicatorDao.getIndicatorsByStation(model);
	}
	/*
	 * 根据设备获得当前设备下的参数列表
	 */
	public List<IndicatorModel> getIndicators4Deivce(DeviceModel model){
		return indicatorDao.getIndicators4Deivce(model);
	}
	/*
	 * 根据分组信息查询有效参数列表
	 */
	public List<IndicatorModel> getIndicatorsByGroup(IndicatorGroupModel model){
		return indicatorDao.getIndicatorsByGroup(model);
	}
	/*
	 * 根据站点列表查询出共同的参数列表
	 * 以selecttree的形式展示
	 */
	public List<SelectTree> getIndicatorTreesByStations(List<StationModel> stations){
		return indicatorDao.getIndicatorTreesByStations(stations);
	}
	/*
	 * 根据参数编码,获得参数的信息
	 */
	public IndicatorModel getIndicatorByCode(String indicatorCode){
		return indicatorDao.getIndicatorByCode(indicatorCode);
	}
	/*
	 * 根据功能区选择关联的参数
	 */
	public List<ZTreeModel> getIndicatorZtreeList4Domain(DomainModel model){
		return indicatorDao.getIndicatorZtreeList4Domain(model);
	}
	
	/*
	 * 得到所有有效参数的树的列表
	 * 选中传入参数,
	 * 如果没有传入参数,则选中第一个参数
	 */
	public List<SelectTree> getAllIndicatorListTree4Single(IndicatorModel model){
		return indicatorDao.getAllIndicatorListTree4Single(model);
	}
}
