package com.sdocean.sms.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.page.model.UiColumn;
import com.sdocean.sms.dao.SmsDao;
import com.sdocean.sms.model.SmsLog;
import com.sdocean.sms.model.SmsMouldModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class SmsService {

	@Resource
	private SmsDao smsDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4TypeList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		//UiColumn col1 = new UiColumn("stationId", "stationId", false, "*");
		//UiColumn col2 = new UiColumn("stationName", "stationName", true, "*");
		UiColumn col3 = new UiColumn("userId", "userId", false, "*");
		UiColumn col6 = new UiColumn("用户", "userName", true, "*");
		UiColumn col7 = new UiColumn("手机号", "telephone", true, "*");
		UiColumn col8 = new UiColumn("周一", "monValue", false, "*");
		UiColumn col9 = new UiColumn("周二", "tuesValue", false, "*");
		UiColumn col10 = new UiColumn("周三", "wedValue", false, "*");
		UiColumn col11 = new UiColumn("周四", "thurValue", false, "*");
		UiColumn col12 = new UiColumn("周五", "friValue", false, "*");
		UiColumn col13 = new UiColumn("周六", "saturValue", false, "*");
		UiColumn col14 = new UiColumn("周日", "sunValue", false, "*");
		UiColumn col15 = new UiColumn("周一", "mon", true, "*");
		UiColumn col16 = new UiColumn("周二", "tues", true, "*");
		UiColumn col17 = new UiColumn("周三", "wed", true, "*");
		UiColumn col21 = new UiColumn("周四", "thur", true, "*");
		UiColumn col18 = new UiColumn("周五", "fri", true, "*");
		UiColumn col19 = new UiColumn("周六", "satur", true, "*");
		UiColumn col20= new UiColumn("周日", "sun", true, "*");
		UiColumn col22= new UiColumn("间隔发送时间", "betweenTime", true, "*");
		UiColumn col27= new UiColumn("是否接收短信", "isactiveName", true, "*");
		UiColumn col23= new UiColumn("上午起始时间", "ambegin", true, "*");
		UiColumn col24= new UiColumn("上午截止时间", "amend", true, "*");
		UiColumn col25= new UiColumn("下午起始时间", "pmbegin", true, "*");
		UiColumn col26= new UiColumn("下午截止时间", "pmend", true, "*");
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
		cols.add(col13);
		cols.add(col14);
		cols.add(col15);
		cols.add(col16);
		cols.add(col17);
		cols.add(col18);
		cols.add(col19);
		cols.add(col20);
		cols.add(col21);
		cols.add(col22);
		cols.add(col23);
		cols.add(col24);
		cols.add(col25);
		cols.add(col26);
		cols.add(col27);
		return cols;
	}
	
	/*
	 * 查询某站点下需要接受短信的人员列表
	 */
	public List<SmsSettingModel> getUsers4SmsSetting(SmsSettingModel model,SysUser user){
		return smsDao.getUsers4SmsSetting(model, user);
	}
	/*
	 * 初始化用户下的短信配置
	 */
	public SmsSettingModel infoUserSetting(SysUser user){
		return smsDao.infoUserSetting(user);
	}
	
	/*
	 * 保存用户短信配置
	 */
	public Result saveUserSetting(SmsSettingModel model){
		return smsDao.saveUserSetting(model);
	}
	
	/*
	 * 展示短信模板
	 */
	public List<UiColumn> getCols4MouldList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点", "stationName", true, "*");
		UiColumn col6 = new UiColumn("type", "type", false, "*");
		UiColumn col7 = new UiColumn("模板类型", "typeName", true, "*");
		UiColumn col8 = new UiColumn("模板", "mould", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		return cols;
	}
	
	/*
	 * 为展示模板提供结果
	 */
	public List<SmsMouldModel> getSmsMoulds4Rows(SmsMouldModel model){
		return smsDao.getSmsMoulds4Rows(model);
	}
	/*
	 * 对短信发送模板修改
	 */
	public Result saveSmsMouldChange(SmsMouldModel model){
		return smsDao.saveSmsMouldChange(model);
	}
	/*
	 * 保存新增的短信发送模板
	 */
	public Result saveNewSmsMould(SmsMouldModel model){
		return smsDao.saveNewSmsMould(model);
	}
	/*
	 * 展示短信发送记录
	 */
	public List<UiColumn> getCols4LogList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("userId", "userId", false, "*");
		UiColumn col3 = new UiColumn("用户名", "userName", true, "*");
		UiColumn col6 = new UiColumn("手机号", "telephone", true, "*");
		UiColumn col7 = new UiColumn("短信", "message", true, "*");
		UiColumn col8 = new UiColumn("ifsend", "ifsend", false, "*");
		UiColumn col9 = new UiColumn("发送状态", "ifSendName", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		return cols;
	}
	/*
	 * 查询短信发送记录,
	 * 增加模糊查询
	 */
	public List<SmsLog> getSmsLogs(SmsLog model){
		return smsDao.getSmsLogs(model);
	}
	/*
	 * 保存短信发送配置
	 */
	public Result saveSmsStationSetting(SmsSettingModel model){
		return smsDao.saveSmsStationSetting(model);
	}
}
