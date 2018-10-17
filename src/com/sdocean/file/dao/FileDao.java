package com.sdocean.file.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.file.model.SysFile;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.role.model.RoleModel;
import com.sdocean.users.model.SysUser;

@Component
public class FileDao extends OracleEngine{
	
	/*
	 * 保存图片信息
	 */
	public Result saveFile(SysFile file){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(file));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_file(filename,realname,absolutelypath,relativepath,type)");
		sql.append(" values(?,?,?,?,?)");
		Object[] params = new Object[]{
				file.getFileName(),file.getRealName(),file.getAbsolutelyPath(),
				file.getRelativePath(),file.getType()
		};
		int res=0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
		}
		return result;
	}
	/*
	 * 展示系统的图片信息
	 */
	public List<SysFile> getFileList(String httpPath,SysFile model){
		List<SysFile> list = new ArrayList<SysFile>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select id,filename,realname,absolutelypath,createtime,relativepath,type,");
		sql.append(" case type when 1 then concat('").append(httpPath).append("','/',relativepath,'/',realname) end as httpPath ") ;
		sql.append(" from sys_file");
		sql.append(" where 1=1");
		//添加查询条件
		if(model!=null){
			//增加模糊查询
			if(model.getRealName()!=null&&model.getRealName().length()>0){
				sql.append(" and (filename like '%").append(model.getRealName()).append("%' ");
				sql.append(" or realname like '%").append(model.getRealName()).append("%' ");
				sql.append(" or absolutelypath like '%").append(model.getRealName()).append("%' ");
				sql.append(" or relativepath like '%").append(model.getRealName()).append("%')");
			}
			//添加上传时间查询
			if(model.getBeginTime()!=null&&model.getBeginTime().length()>0){
				sql.append(" and createtime >= '").append(model.getBeginTime()).append("'");
			}
			if(model.getEndTime()!=null&&model.getEndTime().length()>0){
				sql.append(" and createtime <= '").append(model.getEndTime()).append("'");
			}
			//添加图片类型 1代表项目中的文件,2,非项目中的文件
			if(model.getType()>0){
				sql.append(" and type = ").append(model.getType());
			}
		}
		list = this.queryObjectList(sql.toString(), SysFile.class);
		
		return list;
	}
}
