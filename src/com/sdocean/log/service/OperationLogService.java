package com.sdocean.log.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.log.dao.OperationLogDao;
import com.sdocean.log.model.OperationLogModel;
import com.sdocean.log.model.SysLog;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.page.model.UiColumn;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class OperationLogService {
	
	@Autowired
	OperationLogDao operationDao;
	
	/*
	 * 将用户操作信息存入到数据库中
	 */
	public void saveOperationLog(Result result,HttpServletRequest request){
		OperationLogModel model = new OperationLogModel();
		model.setDotype(result.getDotype());
		model.setResult(result.getResult());
		model.setMessage(result.getMessage());
		model.setModel(result.getModel());
		//保存操作日志
		HttpSession session = request.getSession();
		//获得当前登录用户
		SysUser users = (SysUser) session.getAttribute("user");
		//获得当前用户正在操作的菜单
		CurrMenu currMenu = (CurrMenu) session.getAttribute("currMenu");
		model.setUserId(users.getId());
		model.setMenuname(currMenu.getcMenuName());
		model.setUrl(request.getRequestURI());
		operationDao.saveOperationLog(model);
	}
	
	/*
	 * 为查询登录日志提供表头信息
	 */
	public List<UiColumn> getLoginCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("userId", "userId", false, "*");
		UiColumn col3 = new UiColumn("用户", "userName", true, "*");
		UiColumn col4 = new UiColumn("操作时间", "operaTime", true, "*");
		UiColumn col5 = new UiColumn("操作菜单", "menuname", true, "*");
		UiColumn col6 = new UiColumn("操作类型", "dotypeName", true, "*");
		UiColumn col7 = new UiColumn("操作结果", "resultName", true, "*");
		UiColumn col8 = new UiColumn("提示信息", "message", true, "*");
		UiColumn col9 = new UiColumn("错误编码", "errorcode", true, "*");
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
	 * 根据查询条件查询用户操作日志
	 */
	public List<OperationLogModel> getOperationLogs(OperationLogModel model){
		return operationDao.getOperationLogs(model);
	}
	/*
	 * 为系统状态日志提供表头
	 */
	public List<UiColumn> getCols4SysLogs(SysLog model){
		return operationDao.getCols4SysLogs(model);
	}
	/*
	 * 为系统状态日志提供结果
	 */
	public List<Map<String, Object>> getRows4SysLogs(SysLog model){
		return operationDao.getRows4SysLogs(model);
	}
}
