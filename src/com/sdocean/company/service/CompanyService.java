package com.sdocean.company.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.company.dao.CompanyDao;
import com.sdocean.company.model.CompanyModel;
import com.sdocean.company.model.SysCompanyModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class CompanyService {
	
	@Autowired
	private CompanyDao companyDao;
	
	/*
	 * 为公司展示列表添加表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("父code", "pcode", true, "*");
		UiColumn col4 = new UiColumn("单位全称", "name", true, "*");
		UiColumn col5 = new UiColumn("单位简称", "shortName", true, "*");
		UiColumn col6 = new UiColumn("层级", "level", true, "*");
		UiColumn col19 = new UiColumn("isactive", "isactive", false, "*");
		UiColumn col10 = new UiColumn("状态", "isactiveName", true, "*");
		UiColumn col11 = new UiColumn("排序码", "orderCode", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col4);
		cols.add(col5);
		cols.add(col19);
		cols.add(col10);
		cols.add(col11);
		return cols;
	}
	
	/*
	 * 获得所有有效的公司的列表
	 */
	public List<SysCompanyModel> getSysCompList(){
		return companyDao.getSysCompList();
	}
	
	/*
	 * 获得所有有效的公司列表,按照等级排序编码排序
	 */
	public List<CompanyModel> getCompanyList(CompanyModel model){
		return companyDao.getCompanyList(model);
	}
	
	/*
	 * 修改更改
	 */
	public Result saveCompanyChange(CompanyModel model){
		return companyDao.saveCompanyChange(model);
	}
	/*
	 * 新增保存
	 */
	public Result saveNewCompany(CompanyModel model){
		return companyDao.saveNewCompany(model);
	}
	
	/*
	 * 根据当前的company获得所有有效的pcode树
	 * 排除当前的company
	 */
	public List<SelectTree> showCompanyTree4Pcode(CompanyModel model){
		return companyDao.showCompanyTree4Pcode(model);
	}
	/*
	 * 根据人员获得公司数
	 */
	public List<SelectTree> showComList4Users(SysUser model){
		return companyDao.showComList4Users(model);
	}
	/*
	 * 在下拉框中展示站点树
	 */
	public List<SelectTree> geCompanyListByStation(StationModel station){
		return companyDao.geCompanyListByStation(station);
	}
	/*
	 * 保存删除功能
	 */
	public Result deleCompany(CompanyModel model){
		return companyDao.deleCompany(model);
	}
	/*
	 * 根据组织机构CODE获得上级所有的组织机构列表
	 */
	public List<CompanyModel> getComListByCode(String code){
		return companyDao.getComListByCode(code);
	}
}
