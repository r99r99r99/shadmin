package com.sdocean.dataQuery.model;

import java.util.List;

import com.sdocean.common.model.HchartsLineData;
import com.sdocean.station.model.StationModel;

public class ComparisonData {
	List<HchartsLineData> datas;
	private StationModel station;
	
	public StationModel getStation() {
		return station;
	}

	public void setStation(StationModel station) {
		this.station = station;
	}

	public List<HchartsLineData> getDatas() {
		return datas;
	}

	public void setDatas(List<HchartsLineData> datas) {
		this.datas = datas;
	}
}
