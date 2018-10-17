package com.sdocean.frame.util;


import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

public class TypeConvertUtils {

    private TypeConvertUtils() {
        super();
    }

    private static <X> int getTypeFromClass(Class<X> clazz) {
        if (String.class.isAssignableFrom(clazz)) {
            return Types.VARCHAR;
        } else if (java.math.BigDecimal.class.isAssignableFrom(clazz)) {
            return Types.NUMERIC;
        } else if (
            java.sql.Date.class.isAssignableFrom(clazz)
                || java.util.Date.class.isAssignableFrom(clazz)) {
            return Types.DATE;
        } else if (Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)) {
            return Types.INTEGER;
        } else if (Boolean.class.isAssignableFrom(clazz)) {
            return Types.BOOLEAN;
        } else if (Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)) {
            return Types.DOUBLE;
        } else if (Float.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz)) {
            return Types.FLOAT;
        } else if (Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)) {
            return Types.LONGVARBINARY;
        } else if (Short.class.isAssignableFrom(clazz) || short.class.isAssignableFrom(clazz)) {
            return Types.TINYINT;
        } else if (Time.class.isAssignableFrom(clazz)) {
            return Types.TIME;
        } else if (Timestamp.class.isAssignableFrom(clazz)) {
            return Types.TIMESTAMP;
        } else {
//            throw new IllegalArgumentException(
//                "clazz[" + clazz.getName() + "]�޷�ת��Ϊ��ݿ������ͣ�");


                  return Types.OTHER;
        }
    }

    public static int getType(Object obj) {

        if (obj instanceof Class) {
            return getTypeFromClass((Class<?>) obj);
        }

        if (obj instanceof String) {
            return Types.VARCHAR;
        } else if (obj instanceof java.math.BigDecimal) {
            return Types.NUMERIC;
        } else if (obj instanceof Timestamp) {
            return Types.TIMESTAMP;

        } else if (
            obj instanceof java.sql.Date || obj instanceof java.util.Date) {
            return Types.DATE;
        } else if (obj instanceof Integer) {
            return Types.INTEGER;
        } else if (obj instanceof Boolean) {
            return Types.BOOLEAN;
        } else if (obj instanceof Double) {
            return Types.DOUBLE;
        } else if (obj instanceof Float) {
            return Types.FLOAT;
        } else if (obj instanceof Long) {
            return Types.LONGVARBINARY;
        } else if (obj instanceof Short) {
            return Types.TINYINT;
        } else if (obj instanceof Time) {
            return Types.TIME;
        } else if (obj instanceof NullValue) {
            return ((NullValue) obj).getSqlType();
            //		}else if (obj == null) {
            //			return Types.NULL;	
        } else if (null == obj) {

            return Types.NULL;
        } else {
            return Types.OTHER;
        }
    }
}
