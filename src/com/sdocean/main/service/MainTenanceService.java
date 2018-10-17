package com.sdocean.main.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.main.dao.MainTenanceDao;
import com.sdocean.main.model.MainTainFile;
import com.sdocean.main.model.MainTainModel;
import com.sdocean.main.model.MainTenance;
import com.sdocean.main.model.MainTenanceFile;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class MainTenanceService {

	@Resource
	private MainTenanceDao mainDao;

	public List<UiColumn> getCols4MainEditList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col4 = new UiColumn("维护开始时间", "beginTime", true, "*");
		UiColumn col6 = new UiColumn("维护结束时间", "endTime", true, "*");
		UiColumn col60 = new UiColumn("userId", "userId", false, "*");
		UiColumn col7 = new UiColumn("上报人", "userName", true, "*");
		UiColumn col8 = new UiColumn("status", "status", false, "*");
		UiColumn col9 = new UiColumn("状态", "statusName", true, "*");
		UiColumn col10 = new UiColumn("附件", "mainFile", true, "*");
		col10.setCellTemplate("<a href=\"exportMainFile.do?fileName={{grid.appScope.getFileName(grid, row)}}&filePath={{grid.appScope.getFilePath(grid, row)}}\" >{{grid.appScope.getFileName(grid, row)}}</a>");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col6);
		cols.add(col60);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		return cols;
	}
	
	/*
	 * 为站点维护查询提供表头
	 */
	public List<UiColumn> getCols4MainShowList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col4 = new UiColumn("维护开始时间", "beginTime", true, "*");
		UiColumn col6 = new UiColumn("维护结束时间", "endTime", true, "*");
		UiColumn col60 = new UiColumn("userId", "userId", false, "*");
		UiColumn col7 = new UiColumn("上报人", "userName", true, "*");
		UiColumn col8 = new UiColumn("status", "status", false, "*");
		UiColumn col9 = new UiColumn("状态", "statusName", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col6);
		cols.add(col60);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		return cols;
	}
	
	/*
	 * 为站点维护上报提供查询结果集
	 */
	public List<MainTainModel> getMainList4StationMainEdit(MainTainModel model,List<StationModel> stations){
		return mainDao.getMainList4StationMainEdit(model, stations);
	}
	/*
	 * 获得新增的站点设备维护上报并返回新增的ID值
	 */
	public Result saveNewMainEdit(MainTainModel model){
		return mainDao.saveNewMainEdit(model);
	}
	/*
	 * 保存修改维护上报,
	 */
	public Result saveChangeMainEdit(MainTainModel model){
		return mainDao.saveChangeMainEdit(model);
	}
	/*
	 * 根据站点设备维护ID,以及文件列表,保存文件数据到附表
	 */
	public void saveFileByMain(MainTainModel main,List<MainTenanceFile> files){
		mainDao.saveFileByMain(main, files);
	}
	/*
	 * 根据站点设备维护ID,得到该设备维护记录上传的图片列表
	 */
	public List<MainTainFile> getFileListByMain(MainTainFile model){
		return mainDao.getFileListByMain(model);
	}
	/*
	 * 为首页展示系统维护时间提供数据
	 */
	public List<MainTenance> getPlanMain4FirstPage(StationModel station){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		List<MainTenance> list = new ArrayList<MainTenance>();
		//得到该站点下需要例行维护的设备的列表,以及该设备的创建时间
		list = mainDao.getDeviceList4MainByStation(station);
		//根据站点和设备遍历其例行维护类型,得到时间最靠前的时间,以及类型名称
		for(MainTenance main:list){
			String createTime = main.getCreateTime();
			Date planTime = null;
			String stateName = "";    //例行维护类型
			try {
				 planTime = sdf.parse("2099-12-21 00:00:01");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//根据站点和设备得到有效的例行维护类型列表,以及间隔时间
			List<MainTenance> configs = mainDao.getMainConfigListByStationDevice(main);
			//根据站点\设备和例行维护类型,以及该类型的间隔时间,得到预计的下次维护时间,
			for(MainTenance con:configs){
				String endTime = "";
				//该站点 设备和例行维护类型的间隔维护时间
				int mainNum = con.getMainnum();
				MainTenance res = mainDao.getLastMainByStationDeviceConfig(con);
				if(res!=null&&res.getEndTime()!=null&&res.getEndTime().length()>0){
					endTime = res.getEndTime();
				}else{
					endTime = createTime;
				}
				
				Date end = null;
				try {
					 end = sdf.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Calendar calendar=Calendar.getInstance();   
				calendar.setTime(end);
				calendar.add(Calendar.DATE, mainNum);
				end = calendar.getTime();
				if(end.getTime()<planTime.getTime()){
					planTime = end;
					stateName = con.getAmcName();
				}
			}
			String plan = sd.format(planTime);
			main.setAmcName(stateName);
			main.setPlanTime(plan);
		}
		
		return list;
	}
	
	/*
	 * 以表格的形式显示设备维护提醒
	 */
	public PageResult getPlanMain4FirstPage2(StationModel station){
		PageResult page = new PageResult();
		//初始化表头
		List<UiColumn> cols = new ArrayList<UiColumn>();
		List<Map<String, String>> rows = new ArrayList<>();
		Map row = new HashMap<>();
		
		List<MainTenance> list = new ArrayList<MainTenance>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		//得到该站点下需要例行维护的设备的列表,以及该设备的创建时间
		list = mainDao.getDeviceList4MainByStation(station);
		//根据站点和设备遍历其例行维护类型,得到时间最靠前的时间,以及类型名称
		for(MainTenance main:list){
			UiColumn col = new UiColumn(main.getDeviceName(), main.getDeviceId()+"", true, "*");
					String createTime = main.getCreateTime();
					String plan = "";
					Date planTime = null;
					String stateName = "";    //例行维护类型
					try {
						 planTime = sdf.parse("2099-12-21 00:00:01");
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//根据站点和设备得到有效的例行维护类型列表,以及间隔时间
					List<MainTenance> configs = mainDao.getMainConfigListByStationDevice(main);
					//根据站点\设备和例行维护类型,以及该类型的间隔时间,得到预计的下次维护时间,
					for(MainTenance con:configs){
						String endTime = "";
						//该站点 设备和例行维护类型的间隔维护时间
						int mainNum = con.getMainnum();
						MainTenance res = mainDao.getLastMainByStationDeviceConfig(con);
						if(res!=null&&res.getEndTime()!=null&&res.getEndTime().length()>0){
							endTime = res.getEndTime();
						}else{
							endTime = createTime;
						}
						
						Date end = null;
						try {
							 end = sdf.parse(endTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Calendar calendar=Calendar.getInstance();   
						calendar.setTime(end);
						calendar.add(Calendar.DATE, mainNum);
						end = calendar.getTime();
						if(end.getTime()<planTime.getTime()){
							planTime = end;
							plan = sd.format(planTime);
							stateName = con.getAmcName();
						}
					}
			row.put(main.getDeviceId()+"", plan+"("+stateName+")");
			cols.add(col);
		}
		rows.add(row);
		page.setCols(cols);
		page.setRows(rows);
	return page;
	}
	
	/*
	 * 为例行维护提供设备列表
	 */
	public List<SelectTree> getDeviceIndicatorTree4Main(MainTainModel model){
		return mainDao.getDeviceIndicatorTree4Main(model);
	}
}
