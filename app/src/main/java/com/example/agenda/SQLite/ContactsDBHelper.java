package com.example.agenda.SQLite;


import static com.example.agenda.constants.DBKeys.Contact.TABLE_NAME;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.agenda.Classes.Contact;
import com.example.agenda.Classes.Response;
import com.example.agenda.R;
import com.example.agenda.constants.DBKeys;

import java.util.ArrayList;

public class ContactsDBHelper extends SQLiteOpenHelper {
    public Context context;
    public static final String sqlCREATE_TABLE="create table if not exists "+TABLE_NAME
            +"("+ DBKeys.Contact.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +DBKeys.Contact.NAME+" varchar(45) not null,"
            +DBKeys.Contact.P_LAST_NAME+" varchar(65) ,"
            +DBKeys.Contact.M_LAST_NAME+" varchar(65) ,"
            +DBKeys.Contact.AGE+" int(3) ,"
            +DBKeys.Contact.PHONE+" varchar(14) unique,"
            +DBKeys.Contact.SEX+" varchar(1),"
            +DBKeys.Contact.PHOTO+ " varchar(85)"
              +")";

    public ContactsDBHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DBKeys.DB_NAME, factory,DBKeys.VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(sqlCREATE_TABLE);
    }

    public Response getAllContacts(){
        Response response= new Response();
        try{
        ArrayList<Contact> contacts= new ArrayList<Contact>();
        SQLiteDatabase db= this.getReadableDatabase();
        Contact contact= new Contact();
        Cursor cv= db.query(TABLE_NAME,DBKeys.Contact.campos, null, null, null, null, null);


        if (cv.moveToFirst()) {
//Recorremos el cursor hasta que no haya más registros
            do {
                contact.setContact(cv);


                contacts.add(contact);
                contact= new Contact();


            } while(cv.moveToNext());

        }

        this.close();
        db.close();
        cv.close();
        response.code=0;
        response.obj=contacts;
        }
        catch (Exception e){
            response.msg="Error en la lectura de contactos";

        }
        return  response;
    }
    public Response deleteContact(Contact contact){
        Response response= new Response();
        try{
            SQLiteDatabase db= this.getWritableDatabase();
            String sqlQuery="DELETE FROM "+ TABLE_NAME
                    +" WHERE "+DBKeys.Contact.ID+"="+contact.id+";";
            db.execSQL(sqlQuery);
            response.code=0;
            db.close();
        }
        catch(Exception e){
            response.msg=e.getMessage();
            response.msg="Error al eliminar contacto";
        }
        return response;
    }
    public Response editContact(Contact contact){
        Response response= new Response();
        try{
            SQLiteDatabase db= this.getWritableDatabase();
            String sqlQuery="UPDATE "+ TABLE_NAME
                    +" SET "+DBKeys.Contact.NAME+"="+DBKeys.getVarcharIsNull(contact.name)+", "
                    +DBKeys.Contact.P_LAST_NAME+"="+DBKeys.getVarcharIsNull(contact.p_last_name)+", "
                    +DBKeys.Contact.M_LAST_NAME+"="+DBKeys.getVarcharIsNull(contact.m_last_name)+", "
                    +DBKeys.Contact.PHONE+"="+DBKeys.getVarcharIsNull(contact.phone)+", "
                    +DBKeys.Contact.SEX+"="+DBKeys.getVarcharIsNull(contact.sex)+", "
                    +DBKeys.Contact.AGE+"="+DBKeys.getNumberIsNull(contact.age)+", "
                    +DBKeys.Contact.PHOTO+"="+DBKeys.getVarcharIsNull(contact.photo)
                    +" WHERE "+DBKeys.Contact.ID+"="+contact.id+";";
            db.execSQL(sqlQuery);
            response.code=0;
            db.close();
        }
        catch(Exception e){
            response.msg="Error al actualizar contacto";

        }
        return response;
    }
    public  boolean registroConTelefonoExiste(Contact contact) {
        Boolean b = false;
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db = this.getWritableDatabase();

            Cursor cv = db.query(TABLE_NAME, DBKeys.Contact.campos, DBKeys.Contact.PHONE + "='" + contact.phone + "'", null, null, null, null);

            if (cv.getCount() == 0) {
                b = false;
            } else {
                b = true;
            }
            closeCursorSecure(cv);
        }
        catch(Exception ex){} return b;

    }
    public  Contact buscarContactoPorTelefono(Contact contact) {
        Boolean b = false;
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db = this.getWritableDatabase();
            Cursor cv = db.query(TABLE_NAME, DBKeys.Contact.campos, DBKeys.Contact.PHONE + "='" + contact.phone + "'", null, null, null, null);
            if (cv.getCount() == 0) {
                contact= new Contact();
            } else {
                cv.moveToFirst();
                contact.setContact(cv);
            }
            closeCursorSecure(cv);
        }
        catch(Exception ex){} return contact;

    }
    public void closeCursorSecure(Cursor cursor){
        if(!cursor.isClosed()){
            cursor.close();
        }
        else{
             }
    }
    public Response createContact(Contact contact){
        Response response= new Response();
        try{
            SQLiteDatabase db= this.getWritableDatabase();
            String sqlQuery="INSERT INTO "+ TABLE_NAME+"("
                    +DBKeys.Contact.NAME+","
                    +DBKeys.Contact.P_LAST_NAME+","
                    +DBKeys.Contact.M_LAST_NAME+","
                    +DBKeys.Contact.AGE+","
                    +DBKeys.Contact.PHONE+","
                    +DBKeys.Contact.SEX+","
                    +DBKeys.Contact.PHOTO+ ")"
                    +" VALUES("
                    + DBKeys.getVarcharIsNull(contact.name)+","
                    +DBKeys.getVarcharIsNull(contact.p_last_name)+","
                    +DBKeys.getVarcharIsNull(contact.m_last_name)+","
                    +DBKeys.getNumberIsNull(Integer.valueOf(contact.age))+","
                    +DBKeys.getVarcharIsNull(contact.phone)+","
                    +DBKeys.getVarcharIsNull(contact.sex)+","
                    +DBKeys.getVarcharIsNull(contact.photo)
                    +")";
            db.execSQL(sqlQuery);
            response.obj=buscarContactoPorTelefono(contact);
            response.code=0;

            db.close();
        }
        catch(Exception e){
            response.msg=e.getMessage();
            response.msg="Error al registrar contacto";

        }
        return response;
    }
}
