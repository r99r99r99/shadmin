package com.sdocean.frame.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.sdocean.frame.exception.CopyPropertyException;
import com.sdocean.frame.util.ClassUtils;
import com.sdocean.frame.util.Constants;

public class OracleEngine {

	private static Logger log = LoggerFactory.getLogger(OracleEngine.class);

	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		log.info("程序启动初始化spring jdbcTemplate");
	}

	public static void main(String args[]) {
		
	}

	public <X> List<X> queryObjectList(String sql, Object[] parameters,
			Class<X> clazz) throws DataAccessException {

		List<Map<String, Object>> result = queryForList(sql, parameters);

		List<X> objects = new ArrayList<X>();

		for (Iterator<Map<String, Object>> it = result.iterator(); it.hasNext();) {

			Map<String, Object> map = it.next();

			X obj = ClassUtils.copyPropertyIgnoreCase(clazz, map);

			objects.add(obj);
		}

		return objects;
	}

	public <X> List<X> queryObjectList(String sql, Class<X> clazz)
			throws DataAccessException {

		List<Map<String, Object>> result = queryForList(sql);
		
		List<X> objects = new ArrayList<X>();

		for (Iterator<Map<String, Object>> it = result.iterator(); it.hasNext();) {

			Map<String, Object> map = it.next();

			X obj = ClassUtils.copyPropertyIgnoreCase(clazz, map);

			objects.add(obj);
		}

		return objects;
	}

	public <X> X queryObject(String sql, Object[] parameters, Class<X> clazz)
			throws DataAccessException, CopyPropertyException {

		List<Map<String, Object>> result = queryForList(sql, parameters);

		if (result.size() == 0) {
			return null;
		} else {
			// copy 属性 忽略大小写
			Map<String, Object> map = result.get(0);
			X obj = ClassUtils.copyPropertyIgnoreCase(clazz, map);
			return obj;
		}

	}
	public <X> X queryObject(String sql,Class<X> clazz)
			throws DataAccessException, CopyPropertyException {

		List<Map<String, Object>> result = queryForList(sql);

		if (result.size() == 0) {
			return null;
		} else {
			// copy 属性 忽略大小写
			Map<String, Object> map = result.get(0);
			X obj = ClassUtils.copyPropertyIgnoreCase(clazz, map);
			return obj;
		}

	}

	public List<Map<String, Object>> queryForList(String sql,Object[] parameters) {

		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql,parameters);
		return result;
	}
	public List<Map<String, Object>> queryForList(String sql) {
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		for(Map<String, Object> map:result){
			for (String key : map.keySet()) {  
			    Object value = map.get(key);
			    if(value instanceof Date){   
		            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMATE,Locale.UK); 
		            value = sdf.format(value);
		            map.put(key, value.toString());
		        }   
			}  
		}
		return result;
	}
	
	/*public List<Map<String, String>> queryForListString(String sql){
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		for(Map<String, Object> map:result){
			Map<String, String> m = new HashMap<String, String>();
			for (String key : map.keySet()) { 
				Object value = map.get(key);
				if(value!=null){
					m.put(key, value.toString());
				}else{
					m.put(key,"");
				}
			}
			res.add(m);
		}
		return res;
	}*/

	public String queryForString(String sql, Object[] parameters) {

		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql,
				parameters);

		if (result.size() == 0) {
			return null;
		}

		Map<String, Object> map = result.get(0);

		Iterator<Entry<String, Object>> it = map.entrySet().iterator();

		if (it.hasNext()) {

			return (String) (it.next()).getValue();
		}

		return null;

	}
	/**
	 * 查询以KEY VALUE的形式
	 * @param sql
	 * @return
	 */
	public Map<String, Object> queryForMapKeyValue(String sql){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.queryForList(sql);
		for(Map<String,Object> one:list){
			String key = one.get("id").toString();
			String value = one.get("name").toString();
			map.put(key, value);
		}
		return map;
	}

	public int queryForInt(String sql, Object[] parameters)
			throws DataAccessException {
		return this.jdbcTemplate.queryForInt(sql, parameters);
	}

	// public <X> X queryForObject(String sql, Object[] parameters, Class<X>
	// clazz)
	// throws DataAccessException {
	// return this.jdbcTemplate.queryForObject(sql, parameters, clazz);
	// }

	public <X> List<X> queryObjectList(String sql, Object[] parameters,
			int start, int count, Class<X> clazz) throws DataAccessException,
			CopyPropertyException {

		sql = MappingSqlPageQueryImpl.constructPageSQL(sql, start, count);

		parameters = MappingSqlPageQueryImpl.addPageParam(parameters, start,
				count);
		List<Map<String, Object>> result = queryForList(sql, parameters);

		List<X> objects = new ArrayList<X>();

		for (Iterator<Map<String, Object>> it = result.iterator(); it.hasNext();) {

			Map<String, Object> map = it.next();

			X obj = ClassUtils.copyPropertyIgnoreCase(clazz, map);

			objects.add(obj);
		}

		return objects;

	}

	public List<Map<String, Object>> queryForList(String sql,
			Object[] parameters, int start, int count)
			throws DataAccessException {

		sql = MappingSqlPageQueryImpl.constructPageSQL(sql, start, count);

		parameters = MappingSqlPageQueryImpl.addPageParam(parameters, start,
				count);

		List<Map<String, Object>> result = queryForList(sql, parameters);

		return result;

	}

	public int queryRowCount(String sql, Object[] parameters) {

		log.debug(sql);
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(
				"Select Count(1) From (" + sql + ") cc", parameters);

		if (result.size() == 0) {
			return 0;
		}

		Map<String, Object> map = result.get(0);

		Iterator<Entry<String, Object>> it = map.entrySet().iterator();

		if (it.hasNext()) {
			return ((Number) it.next().getValue()).intValue();
		} else {
			return 0;
		}
	}

	public void execute(String sql) {
		this.jdbcTemplate.execute(sql);
	}

	public int update(String sql, Object... objects) {
		return this.jdbcTemplate.update(sql, objects);
	}
	
	
	/*
	 * 调用存储过程返回结果集的基础方法
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> procQuery(String procedure){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		list = this.jdbcTemplate.execute(procedure,  
                new CallableStatementCallback() {  
					@Override
					public Object doInCallableStatement(
							java.sql.CallableStatement cs)
							throws SQLException, DataAccessException {
						// TODO Auto-generated method stub
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
						cs.execute();  
						 ResultSet rs =cs.getResultSet();
						 while (rs.next()) {  
	                        	Map<String, Object> dataMap = new HashMap<String, Object>();  
	                            ResultSetMetaData rsMataData = (ResultSetMetaData) rs.getMetaData();  
	                            for (int i = 1; i <= rsMataData.getColumnCount(); i++) {  
	                                dataMap.put(rsMataData.getColumnName(i), rs.getString(rsMataData.getColumnName(i)));  
	                            }  
	                            list.add(dataMap);  
	                      }
                        return list;
					}
                });  
		return list;
	}
	/*
	 * 通过存储过程查询
	 */
    public List<Map<String, Object>> procForList(String procedure) { 
        return this.procQuery(procedure);  
    } 
    
    
    /*
     * 通过存储过程查询，并将结果转换成类
     */
    public <X> List<X> pricForObjects(String procedure,Class<X> clazz){
    	List<Map<String, Object>> result = this.procQuery(procedure);

		List<X> objects = new ArrayList<X>();

		for (Iterator<Map<String, Object>> it = result.iterator(); it.hasNext();) {

			Map<String, Object> map = it.next();

			X obj = ClassUtils.copyPropertyIgnoreCase(clazz, map);

			objects.add(obj);
		}

		return objects;
    }
}