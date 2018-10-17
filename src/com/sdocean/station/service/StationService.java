package com.sdocean.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.region.dao.RegionDao;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.dao.StationDao;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationStatusModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StationService {

	@Resource
	private StationDao stationDao;
	@Resource
	private RegionDao regionDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4TypeList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("站点名称", "title", true, "*");
		UiColumn col6 = new UiColumn("维度", "latitude", true, "*");
		UiColumn col7 = new UiColumn("经度", "longitude", true, "*");
		UiColumn col8 = new UiColumn("站点地址", "station_gateway", true, "*");
		UiColumn col9 = new UiColumn("站点类型", "stationtype_id", false, "*");
		UiColumn col10 = new UiColumn("站点类型", "stationTypeName", true, "*");
		UiColumn col11 = new UiColumn("站点归属地", "region_id", false, "*");
		UiColumn col12 = new UiColumn("所在地区", "regionName", true, "*");
		UiColumn col23 = new UiColumn("站点归属单位", "companyId", false, "*");
		UiColumn col22 = new UiColumn("站点归属单位", "companyName", true, "*");
		
		UiColumn col13 = new UiColumn("站点描述", "brief", true, "*");
		UiColumn col14 = new UiColumn("站点细节", "detail", true, "*");
		UiColumn col15 = new UiColumn("站点图片", "pic", true, "*");
		UiColumn col16 = new UiColumn("站点水质类型", "waterType", false, "*");
		UiColumn col17 = new UiColumn("站点水质类型", "waterTypeName", true, "*");
		UiColumn col21 = new UiColumn("包含设备", "deviceNames", false, "*");
		UiColumn col18 = new UiColumn("isactive", "isactive", false, "*");
		UiColumn col19 = new UiColumn("状态", "isactiveName", true, "*");
		UiColumn col20= new UiColumn("排序", "orderCode", true, "*");
		UiColumn col31= new UiColumn("ifsms", "ifsms", false, "*");
		UiColumn col32= new UiColumn("短信功能", "ifsmsName", true, "*");
		UiColumn col33= new UiColumn("数据表结构期限", "lastMetaDate", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
		cols.add(col23);
		cols.add(col22);
		cols.add(col13);
		cols.add(col14);
		cols.add(col15);
		cols.add(col16);
		cols.add(col17);
		cols.add(col18);
		cols.add(col19);
		cols.add(col20);
		cols.add(col21);
		cols.add(col31);
		cols.add(col32);
		cols.add(col33);
		return cols;
	}
	
	/*
	 * 查询出符合条件的站点的列表
	 */
	public List<StationModel> getStationList(StationModel model){
		return stationDao.getStationList(model);
	}
	
	/*
	 * 保存修改站点
	 */
	public Result saveStaionChange(StationModel model){
		return stationDao.saveStaionChange(model);
	}
	
	/*
	 * 保存新增站点
	 */
	public Result saveNewStation(StationModel model){
		return stationDao.saveNewStation(model);
	}
	
	/*
	 * 获得站点树,根据权限选中以有权限的站点
	 */
	public List<ZTreeModel> getStationListByRole(RoleModel role){
		//初始化返回结果
		List<ZTreeModel> result = new ArrayList<ZTreeModel>();
		//获得所有有效站点的列表,符合权限的被选中
		List<ZTreeModel> stations = stationDao.getStationListByRole(role);
		//根据站点列表,获得站点列表的地区列表
		List<ZTreeModel> regions = regionDao.getParentRegions(stations);
		//将地区列表进行唯一性处理
		List<ZTreeModel> regionlist = new ArrayList<ZTreeModel>();
		for(ZTreeModel region:regions){
			int i = 0;
			for(ZTreeModel j:regionlist){
				if(j.getId().toString().equals(region.getId().toString())){
					i = 1;
				}
			}
			if(i==0){
				regionlist.add(region);
			}
		}
		//将地区列表加入到返回结果中
		result.addAll(regionlist);
		//将站点列表加入到返回结果中
		result.addAll(stations);
		return result;
	}
	
	/*
	 * 获得当前用户的站点权限的列表
	 */
	public List<StationModel> getStations4User(SysUser model){
		return stationDao.getStations4User(model);
	}
	
	/*
	 * 查询该用户上次登录的站点
	 */
	public StationModel getStation4Last(SysUser model){
		return stationDao.getStation4Last(model);
	}
	
	/*
	 * 将此次登录站点信息保存到数据库中
	 */
	public void saveStationLog(SysUser user,StationModel station,int type){
		stationDao.saveStationLog(user, station, type);
	}
	/*
	 * 根据站点ID,得到站点信息
	 */
	public StationModel getStationById(int stationId){
		return stationDao.getStationById(stationId);
	}
	/*
	 * 为短信发送配置提供站点列表表头
	 */
	public List<UiColumn> getStationCols4Sms(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("站点名称", "title", true, "*");
		UiColumn col4 = new UiColumn("站点名称", "type", false, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		return cols;
	}
	/*
	 * 根据站点得到站点的设备列表
	 */
	public List<ZTreeModel> getDeviceZTree4Station(StationModel station){
		return stationDao.getDeviceZTree4Station(station);
	}
	/*
	 * 根据用户读取用户的站点权限,
	 * 并转化成SelectTree的形式
	 * 默认选中第一个站点
	 */
	public List<SelectTree> getStationTreeListByUser(SysUser user){
		return stationDao.getStationTreeListByUser(user);
	}
	/*
	 * 根据站点ids获得站点的集合
	 */
	public List<StationModel> getStationListByIds(String ids){
		return stationDao.getStationListByIds(ids);
	}
	/*
	 * 得到站点的状态信息
	 */
	public StationStatusModel getStationStatus(StationModel station){
		return stationDao.getStationStatus(station);
	}
	
	/*
	 * 获得站点树,根据权限选中以有权限的站点
	 */
	public List<ZTreeModel> getStationTreeByDomain(DomainModel model){
		//初始化返回结果
		List<ZTreeModel> result = new ArrayList<ZTreeModel>();
		//获得所有有效站点的列表,符合权限的被选中
		List<ZTreeModel> stations = stationDao.getStationTreeByDomain(model);
		//根据站点列表,获得站点列表的地区列表
		List<ZTreeModel> regions = regionDao.getParentRegions(stations);
		//将地区列表进行唯一性处理
		List<ZTreeModel> regionlist = new ArrayList<ZTreeModel>();
		for(ZTreeModel region:regions){
			int i = 0;
			for(ZTreeModel j:regionlist){
				if(j.getId().toString().equals(region.getId().toString())){
					i = 1;
				}
			}
			if(i==0){
				regionlist.add(region);
			}
		}
		//将地区列表加入到返回结果中
		result.addAll(regionlist);
		//将站点列表加入到返回结果中
		result.addAll(stations);
		return result;
	}
	/*
	 * 得到当前功能区下的站点列表
	 */
	public List<StationModel> getStationListByDomain(DomainModel model){
		return stationDao.getStationListByDomain(model);
	}
}
