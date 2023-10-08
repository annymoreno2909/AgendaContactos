package com.example.agenda.Classes;

import android.database.Cursor;

import com.example.agenda.constants.DBKeys;

public class Contact {
    
    public  long id ;
    public  String name;
    public  String p_last_name;
    public  String m_last_name;
    public  int age;
    public  String phone;
    public  String sex;
    public  String photo;

    public Contact() {
    }
    //{ID,NAME,P_LAST_NAME,M_LAST_NAME,AGE,PHONE,SEX,PHOTO};
    public void setContact(Cursor cv){
        this.id=cv.getInt(0);
        this.name=cv.getString(1);
        this.p_last_name=cv.getString(2);
        this.m_last_name=cv.getString(3);
        this.age=cv.getInt(4);
        this.phone=cv.getString(5);
        this.sex=cv.getString(6);
        this.photo=cv.getString(7);

    }

}
