package com.sdocean.firstpage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.device.dao.DeviceDao;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.firstpage.dao.FirstPageDao;
import com.sdocean.firstpage.model.FirstPageShow;
import com.sdocean.firstpage.model.SystemModel;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.main.dao.MainTenanceDao;
import com.sdocean.main.model.MainTenance;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class FirstPageService {
	private static Logger log = Logger.getLogger(FirstPageService.class); 
	@Autowired
	private FirstPageDao firstPageDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private MainTenanceDao mainDao;
	
	/*
	 * 为首页实时数据管理提供表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("wpId", "wpId", false, "*");
		UiColumn col3 = new UiColumn("站点名称", "wpName", true, "*");
		UiColumn col4 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col5 = new UiColumn("设备名称", "deviceName", true, "*");
		UiColumn col6 = new UiColumn("indicatorId", "indicatorId", false, "*");
		UiColumn col7 = new UiColumn("参数", "indicatorName", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		return cols;
	}
	
	/*
	 * 为首页实时数据管理查询数据
	 */
	public List<FirstPageShow> getFirstPageShowList(FirstPageShow model){
		return firstPageDao.getFirstPageShowList(model);
	}
	/*
	 *  保存修改首页实时数据管理
	 */
	public Result saveFirstPageShowChange(FirstPageShow model){
		return firstPageDao.saveFirstPageShowChange(model);
	}

	/*
	 *  新增修改首页实时数据管理
	 */
	public Result saveNewFirstPageShow(FirstPageShow model){
		return firstPageDao.saveNewFirstPageShow(model);
	}
	/*
	 * 删除首页实时数据配置
	 */
	public Result deleFirstPageShowSetting(FirstPageShow model){
		return firstPageDao.deleFirstPageShowSetting(model);
	}
	/*
	 * 为首页展示系统运行状态提供数据
	 */
	public List<SystemModel> getSystemList4Firstpage(StationModel station){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String nowDate = sdf.format(now);
		List<SystemModel> list = new ArrayList<SystemModel>();
		//首页展示面板温度,供电电压,网络状态信息
		list = firstPageDao.getSystemModels(station);
		//首页展示传感器状态信息
		List<SystemModel> clist= new ArrayList<SystemModel>();
		clist = mainDao.getDeviceMainState(station);
		//clist = firstPageDao.getChuanSystemModels(station);
		//遍历传感器,判断是否需要维护提醒
		/*for(SystemModel sm:clist){
			String data = sm.getData();
			String newData = "";
			//获得该传感器的维护周期
			DeviceModel device = deviceDao.getDeviceByDeviceId(sm.getDeviceId());
			if(device!=null&&device.getMainnum()>0){//筛选维护周期大于零的情况
				//获得该站点,该设备上一次维护时间
				MainTenance main = mainDao.getLastMainByStationIdDevcieId(sm.getStationId(), sm.getDeviceId());
				if(main==null){
					//newData = "无维护记录";
				}else{
					String lastTime = main.getEndTime();
					try {
						Date last = sdf.parse(lastTime);
						lastTime = sd.format(last);
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(last);
						calendar.add(Calendar.DATE, device.getMainnum()+5);
						Date lastD  = calendar.getTime();
						if(lastD.getTime()>now.getTime()){
							newData = "请于"+lastTime+"前维护";
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				sm.setData(data+newData);
			}
		}*/
		
		list.addAll(clist);
		return list;
	}
	/*
	 * 为首页设置系统运行状态提供表头
	 */
	public List<UiColumn> getCols4System(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col4 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col5 = new UiColumn("参数名称", "indicatorName", true, "*");
		UiColumn col6 = new UiColumn("type", "type", false, "*");
		UiColumn col7 = new UiColumn("数据类型", "typeName", true, "*");
		UiColumn col8 = new UiColumn("排序编码", "orderCode", true, "*");
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
	//根据条件查询出需要在首页展示的系统运行状态
	public List<SystemModel> getSystemModelList(SystemModel model){
		return firstPageDao.getSystemModelList(model);
	}
	//增加在首页展示的系统运行状态
	public Result addSystemModel(SystemModel model){
		return firstPageDao.addSystemModel(model);
	}
	//删除在首页展示的系统运行状态
	public Result deleSystemModel(SystemModel model){
		return firstPageDao.deleSystemModel(model);
	}
	/*
	 * 修改在首页展示的系统运行状态
	 */
	public Result updateSystemModel(SystemModel model){
		return firstPageDao.updateSystemModel(model);
	}
	/*
	 * 根据站点获得该站点需要展示的系统运行状态的参数列表以及传感器列表
	 */
	public List<IndicatorModel> getSystemModelList4Set(StationModel station){
		return firstPageDao.getSystemModelList4Set(station);
	}
}
