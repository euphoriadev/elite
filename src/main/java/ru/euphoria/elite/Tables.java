package ru.euphoria.elite;

import java.lang.reflect.Field;

import ru.euphoria.elite.annotation.PrimaryKey;

/**
 * Created by admin on 30.03.18.
 */

public class Tables {

    public static String drop(Class<?> aClass) {
        return "DROP TABLE IF EXISTS " + getTableName(aClass);
    }

    public static String create(Class<?> aClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(getTableName(aClass));
        sql.append(" (");

        Structure structure = Structure.getStructure(aClass);
        for (Field f : structure.fields) {
            boolean primaryKey = f.isAnnotationPresent(PrimaryKey.class);
            String type = f.getType().getSimpleName();
            String name = f.getName();

            sql.append("\'")
                .append(name)
                .append("\'");
            switch (type) {
                case "boolean":
                case "long":
                case "int": sql.append(" INTEGER"); break;
                case "double": sql.append(" REAL"); break;
                case "String": sql.append(" TEXT"); break;
            }
            if (primaryKey) {
                sql.append(" PRIMARY KEY NOT NULL");
            }
            sql.append(", ");
        }
        sql.delete(sql.length() - 2, sql.length() - 1);
        sql.append(");");
        return sql.toString();
    }

    public static String getTableName(Class aClass) {
        String simpleName = aClass.getSimpleName();

        int index;
        if ((index = hasUpperCase(simpleName)) != -1) {
            simpleName = simpleName.toLowerCase();
            return simpleName.substring(0, index) +
                    "_" +
                    simpleName.substring(index, simpleName.length());
        } else {
            return simpleName.toLowerCase().concat("s");
        }

    }

    private static int hasUpperCase(String s) {
        for (int i = 1; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}