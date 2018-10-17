package com.sdocean.dataQuery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Hcharts;
import com.sdocean.dataQuery.dao.StatisQueryDao;
import com.sdocean.dataQuery.model.StatData;
import com.sdocean.dataQuery.model.StatisModel;
import com.sdocean.dataQuery.model.Ydata;
import com.sdocean.firstpage.model.WaterStandard;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class StatisQueryService {
	
	@Autowired
	private StatisQueryDao statisQueryDao;
	
	/*
	 * 获得水质统计
	 */
	public StatisModel getStatDataSearch(StatisModel model,StationModel station){
	
		/*for(StatData stat:pielist){
			if(stat.getStandard_grade()==1){
				stat.setColor("#9dc3e4");
			}else if(stat.getStandard_grade()==2){
				stat.setColor("#e2efd9");
			}else if(stat.getStandard_grade()==3){
				stat.setColor("#70ad45");
			}else if(stat.getStandard_grade()==4){
				stat.setColor("#ffd965");
			}else if(stat.getStandard_grade()==5){
				stat.setColor("#ed1e24");
			}else if(stat.getStandard_grade()==6){
				stat.setColor("#000000");
			}
		}*/
		//得到线性数据
		List<StatData> lineList = statisQueryDao.getStatDataSearchList(model, station, 1);
		//获得饼形图结果以及水质统计列表数据
		List<StatData> pielist = statisQueryDao.getData4pie(model, station, 1,lineList);
		for(StatData statData:lineList){
			model.xtimes.add(statData.getXtime());
			Hcharts hc = new Hcharts();
			hc.setY(Double.parseDouble(statData.getYdata()));
			hc.setName(statData.getName());
			model.ydatas.add(hc);
		}
		model.setDatas(pielist);
		return model;
	}
	
	/*
	 * 为首页提供水质
	 */
	public WaterStandard getWaterStandard(StationModel watchPoint){
		return statisQueryDao.getWaterStandard(watchPoint);
	}
	
	/*
	 * 首页显示水质等级趋势
	 */
	public StatisModel getStatDataSearch4First(Long userId,StatisModel gmodel,StationModel watchPoint){
		//得到线性数据
		List<StatData> lineList = statisQueryDao.getStatDataSearchList(gmodel, watchPoint, 1);
		for(StatData statData:lineList){
			gmodel.xtimes.add(statData.getXtime());
			Hcharts hc = new Hcharts();
			hc.setY(Double.parseDouble(statData.getYdata()));
			hc.setName(statData.getName());
			gmodel.ydatas.add(hc);
		}
		
		return gmodel;
	}
}
