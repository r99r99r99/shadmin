package com.sdocean.frame.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.sdocean.frame.util.Constants;
import com.sdocean.frame.util.TypeConvertUtils;


class MappingSqlPageQueryImpl extends MappingSqlQuery<Object> {

    private int columns = 0;

    //分页SQL语句头
    private static final String PAGE_SQL_HEAD =
    "SELECT * FROM (SELECT  IQ. *, ROWNUM AS Z_R_N FROM (";
    //分页SQL语句尾
    private static final String PAGE_SQL_FOOT =
        ") IQ ) WHERE Z_R_N < ? AND Z_R_N >= ?";
    
    private static final String PAGE_MYSQL = " LIMIT ? , ?";

    private boolean isPagination = true;

    //是否包含列名
    @SuppressWarnings("unused")
	private boolean isWithHeader = false;
    
    private  Object[] parameters = null;

    /**
     * 在原来sql语句的基础上构造分页的sql语句 增加了额外一列 在取得结果的mapRow()方法时应去掉最后一列
     * @param sql
     * @return
     */
    public static String constructPageSQL(String sql, int start, int count) {

        if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException(" sql 语句不能为空!");
        }

        if (count <= 0) {
            return sql;
        }


        StringBuffer pageSQL = new StringBuffer();

        if(Constants.USE_DB_TYPE==1){ //oracle
        	pageSQL.append(PAGE_SQL_HEAD).append(sql).append(PAGE_SQL_FOOT);
        }else if(Constants.USE_DB_TYPE==0){ //mysql
        	pageSQL.append(sql).append(PAGE_MYSQL);
        }

        return pageSQL.toString();
    }


    /**
      * 构造分页查询   
      * @param ds
      * @param sql
      * @param parameters
      * @param start
      * @param count 当count <= 0 时认为不分页
      */
     public MappingSqlPageQueryImpl(
         DataSource ds,
         String sql,
         Object[] parameters,
         int start,
         int count, boolean isWithHeader) {


        this(ds, sql, parameters, start, count);
        this.isWithHeader = isWithHeader;
     }

    

    /**
     * 构造分页查询	 
     * @param ds
     * @param sql
     * @param parameters
     * @param start
     * @param count 当count <= 0 时认为不分页
     */
    public MappingSqlPageQueryImpl(
        DataSource ds,
        String sql,
        Object[] parameters,
        int start,
        int count) {

        super(ds, constructPageSQL(sql, start, count));

        if (count <= 0) {
            isPagination = false;
        }

        //this.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                declareParameter(
                    new SqlParameter(TypeConvertUtils.getType(parameters[i])));
            }
        }

        //如果分页 注册start , end 参数类型
        if (isPagination) {

            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));

            this.parameters = addPageParam(parameters, start, count);
        }else{
            this.parameters = parameters;
        }
        compile();
    }


    public List<?> executePageQuery() {
        if (parameters != null) {
            List<?> execute = super.execute(parameters);
			return execute;
        }else{
            return super.execute();
        }
    }

    public static  Object[] addPageParam(Object[] parameters, int start, int count) {
        if (count > 0) {

            Object[] newParams = null;

            if (null == parameters) {
                newParams = new Object[2];
            } else {
                newParams = new Object[parameters.length + 2];

                System.arraycopy(
                    parameters,
                    0,
                    newParams,
                    0,
                    parameters.length);

            }

            if(Constants.USE_DB_TYPE==1){//oracle
            	newParams[newParams.length - 2] = new Integer(start + count);
            	newParams[newParams.length - 1] = new Integer(start);
            }else if(Constants.USE_DB_TYPE==0){ //mysql
            	newParams[newParams.length - 1] = new Integer(count);
            	newParams[newParams.length - 2] = new Integer(start);
            }

            parameters = newParams;
        }
        return parameters;
    }


    /**
     * 不分页查询
     * @param ds
     * @param sql
     * @param parameters
     */
    public MappingSqlPageQueryImpl(
        DataSource ds,
        String sql,
        Object[] parameters) {
        this(ds, sql, parameters, 0, -1);
    }

    /**
     * 从结果集中取数据 如果是分页查询 因为多加了一列 rownum列 所以在取数据时不取最后一列
     */
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Object> list = new ArrayList<Object>();
        if (rowNum == 0) {
            ResultSetMetaData metadata = rs.getMetaData();
            columns = metadata.getColumnCount();

            if (isPagination) {
                columns -= 1;
            }
            
            
        }

        for (int i = 1; i <= columns; i++) {
            list.add(rs.getObject(i));
        }
        return list;
    }

}