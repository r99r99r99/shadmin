package com.sdocean.frame.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.Serializable;
import java.sql.Types;

public class NullValue implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int sqlType;

    public NullValue(int sqlType) {
        this.sqlType = sqlType;
    }

    public static final String VARCHAR_TYPE = "VARCHAR";

    public NullValue(String sqlType) {
        if ("NUMBER".equals(sqlType)) {
            this.sqlType = Types.NUMERIC;
        }
        else if ("DATE".equals(sqlType)) {
            this.sqlType = Types.DATE;
        }
        else if ("LONG".equals(sqlType)) {
            this.sqlType = Types.DOUBLE;
        }
        else if ("BLOB".equals(sqlType)) {
            this.sqlType = Types.BLOB;
        }
        else if ("CLOB".equals(sqlType)) {
            this.sqlType = Types.CLOB;
        }
        else {
            this.sqlType = Types.VARCHAR;
        }
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public int getSqlType() {
        return sqlType;
    }

    public String toString() {
        return null;
    }
}