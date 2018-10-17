package com.sdocean.dataQuery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.HchartsLineData;
import com.sdocean.common.model.SelectTree;
import com.sdocean.dataQuery.dao.StatisReportDao;
import com.sdocean.dataQuery.model.StatisReportLineData;
import com.sdocean.dataQuery.model.StatisReportModel;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class StatisReportService {
	
	@Autowired
	private StatisReportDao statisReportDao;
	
	/*
	 * 为平均统计查询提供表头
	 */
	public List<UiColumn> getCols4StatisReportAvg(StatisReportModel model){
		return statisReportDao.getCols4StatisReportAvg(model);
	}
	
	/*
	 * 查询参数在一定的统计口径内的平均值
	 */
	public List<StatisReportModel> getRowsStatisReportDatas(StatisReportModel model){
		return statisReportDao.getRowsStatisReportDatas(model);
	}
	
	/*
	 * 获得统计类型的列表
	 */
	public List<SelectTree> getSelectTree4StatType(){
		List<SelectTree> statTypeTrees = new ArrayList<>();
		SelectTree allTree = new SelectTree();
		allTree.setName("统计类型列表");
		allTree.setIsExpanded(true);
		List<SelectTree> childTrees = new ArrayList<>();
		
		SelectTree avg = new SelectTree();
		avg.setId("avgdata");
		avg.setName("平均值");  
		avg.setSelected(true); //默认选中第一个
		childTrees.add(avg);
		
		SelectTree min = new SelectTree();
		min.setId("mindata");
		min.setName("最小值");
		childTrees.add(min);
		
		SelectTree max = new SelectTree();
		max.setId("maxdata");
		max.setName("最大值");
		childTrees.add(max);
		
		SelectTree diff = new SelectTree();
		diff.setId("diffdata");
		diff.setName("差值");
		childTrees.add(diff);
		
		SelectTree ampli = new SelectTree();
		ampli.setId("amplidata");
		ampli.setName("振幅");
		childTrees.add(ampli);
		
		allTree.setChildren(childTrees);
		statTypeTrees.add(allTree);
		
		return statTypeTrees;
	}
	
	/*
	 * 将表格数据的查询结果转化成折线图的形式
	 */
	public List<StatisReportLineData> changeRows2Line4StatisReport(List<StatisReportModel> rows,StatisReportModel model){
		List<StatisReportLineData> lines = new ArrayList<>();
		//获得统计类型,并去除前面的"0,"
		String statTypes = model.getStatTypes().replace("0,", "");
		//定义3个折线类
		StatisReportLineData avgData = new StatisReportLineData();
		List<HchartsLineData> avglinedata = new ArrayList<HchartsLineData>();
		avgData.setFieldName("平均值");
		StatisReportLineData minData = new StatisReportLineData();
		List<HchartsLineData> minlinedata = new ArrayList<HchartsLineData>();
		minData.setFieldName("最小值");
		StatisReportLineData maxData = new StatisReportLineData();
		List<HchartsLineData> maxlinedata = new ArrayList<HchartsLineData>();
		maxData.setFieldName("最大值");
		//遍历结果集
		for(StatisReportModel row:rows){
			//获得横坐标时间
			String xtime = row.getCollectTime();
			if(statTypes.contains("avgdata")){
				HchartsLineData data = new HchartsLineData();
				data.setXtime(xtime);
				data.setYdata(row.getAvgdata());
				avglinedata.add(data);
			}
			if(statTypes.contains("maxdata")){
				HchartsLineData data = new HchartsLineData();
				data.setXtime(xtime);
				data.setYdata(row.getMaxdata());
				maxlinedata.add(data);
			}
			if(statTypes.contains("mindata")){
				HchartsLineData data = new HchartsLineData();
				data.setXtime(xtime);
				data.setYdata(row.getMindata());
				minlinedata.add(data);
			}
		}
		avgData.setDatas(avglinedata);
		maxData.setDatas(maxlinedata);
		minData.setDatas(minlinedata);
		if(statTypes.contains("avgdata")){
			lines.add(avgData);
		}
		if(statTypes.contains("maxdata")){
			lines.add(maxData);
		}
		if(statTypes.contains("mindata")){
			lines.add(minData);
		}
		return lines;
	}
}
