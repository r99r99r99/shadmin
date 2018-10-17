package com.sdocean.firstpage.model;

import java.util.List;

import com.sdocean.main.model.MainTenance;
import com.sdocean.pollutant.model.Pollutant4First;
import com.sdocean.pollutant.model.PollutantModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.warn.model.Warn4FirstModel;

public class FirstPageModel {
	private WaterStandard waterStandard;
	private List<LastMetaData> Datas;
	private Warn4FirstModel warn;//预警信息
	private Warn4FirstModel alarm; //告警信息
	private List<SystemModel> system;  //系统状态
	private Pollutant4First pollutant; //入海污染量
	private List<MainTenance> mains;   //设备下次维护时间
	private int stationId;
	private StationModel station;
	
	public StationModel getStation() {
		return station;
	}
	public void setStation(StationModel station) {
		this.station = station;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public List<MainTenance> getMains() {
		return mains;
	}
	public void setMains(List<MainTenance> mains) {
		this.mains = mains;
	}
	public Pollutant4First getPollutant() {
		return pollutant;
	}
	public void setPollutant(Pollutant4First pollutant) {
		this.pollutant = pollutant;
	}
	public List<SystemModel> getSystem() {
		return system;
	}
	public void setSystem(List<SystemModel> system) {
		this.system = system;
	}
	public Warn4FirstModel getWarn() {
		return warn;
	}
	public void setWarn(Warn4FirstModel warn) {
		this.warn = warn;
	}
	public Warn4FirstModel getAlarm() {
		return alarm;
	}
	public void setAlarm(Warn4FirstModel alarm) {
		this.alarm = alarm;
	}
	public WaterStandard getWaterStandard() {
		return waterStandard;
	}
	public void setWaterStandard(WaterStandard waterStandard) {
		this.waterStandard = waterStandard;
	}
	public List<LastMetaData> getDatas() {
		return Datas;
	}
	public void setDatas(List<LastMetaData> datas) {
		Datas = datas;
	}
	
}
