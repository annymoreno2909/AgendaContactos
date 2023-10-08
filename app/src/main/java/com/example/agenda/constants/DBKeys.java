package com.example.agenda.constants;

public class DBKeys {
    public static final int VERSION=1;
    public static final String DB_NAME="database";
    public static class Contact{

        public static String TABLE_NAME="contacts";
        public static String ID="id";
        public static String NAME="name";
        public static String P_LAST_NAME="p_last_name";
        public static String M_LAST_NAME="m_last_name";
        public static String AGE="age";
        public static String PHONE="phone";
        public static String SEX="sex";
        public static String PHOTO="photo";

        public static final String[] campos = new String[] {ID,NAME,P_LAST_NAME,M_LAST_NAME,AGE,PHONE,SEX,PHOTO};


    }
    public static String getVarcharIsNull(String str){
        if (str == null) {
            return "null";
        }else{return "'"+str+"'";}

    }
    public static String getNumberIsNull(Integer str){
        if (str == 0) {
            return "null";
        }else{return ""+str;}

    }
}
