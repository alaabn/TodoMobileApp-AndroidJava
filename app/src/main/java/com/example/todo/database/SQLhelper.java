package com.example.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLhelper extends SQLiteOpenHelper {
    static final String DB = "todoapp";
    static  final int VERSION = 1;
    // tableau todos
    public static final String T_TODO = "todos"; // nom tableau
    public static final String T_ID = "id"; // column
    public static final String T_NOM = "nom"; // column
    public static final String T_DESCP = "description"; // column
    public static final String T_STATE = "etat"; // column
    public static final String T_REF_USER = "user_id"; // column


    // tableau users
    public static final String T_USER = "users";
    public static final String U_ID = "id"; // column
    public static final String U_NOM = "nom"; // column
    public static final String U_EMAIL = "email"; // column
    public static final String U_PASS = "mot_de_pass"; // column

    public SQLhelper(@Nullable Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+T_USER+" (" +
                U_ID+" INTEGER PRIMARY KEY, " +
                U_NOM+" TEXT NOT NULL, " +
                U_EMAIL+" TEXT NOT NULL,"+
                U_PASS+" TEXT NOT NULL)");

        db.execSQL("CREATE TABLE "+T_TODO+" (" +
                T_ID+" INTEGER PRIMARY KEY, " +
                T_NOM+" TEXT NOT NULL, " +
                T_DESCP+" TEXT NOT NULL, " +
                T_STATE+" TEXT NOT NULL, " +
                T_REF_USER+" INTEGER, " +
                "FOREIGN KEY ("+T_REF_USER+") REFERENCES "+T_USER+"("+U_ID+")"+" ON DELETE CASCADE )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // TODOs CRUD
    public void insertTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SQLhelper.T_NOM, todo.getNom());
        cv.put(SQLhelper.T_DESCP, todo.getDescp());
        cv.put(SQLhelper.T_STATE, todo.getState());
        cv.put(SQLhelper.T_REF_USER, todo.getUser_id());

        db.insert(SQLhelper.T_TODO, null, cv);
        db.close();
    }
    public void updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SQLhelper.T_NOM, todo.getNom());
        cv.put(SQLhelper.T_DESCP, todo.getDescp());
        cv.put(SQLhelper.T_STATE, todo.getState());
        cv.put(SQLhelper.T_REF_USER, todo.getUser_id());

        db.update(SQLhelper.T_TODO, cv, T_ID+"=?", new String[]{todo.getId()});
        db.close();
    }
    public void deleteTodo(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SQLhelper.T_TODO, T_ID+" = ?", new String[]{id});
        db.close();
    }
    public ArrayList<Todo> getTodos(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Todo> todoList = new ArrayList<>();
        String todoQuery = "SELECT t.* FROM " + T_TODO + " t "
                + "INNER JOIN " + T_USER + " u ON t." + T_REF_USER + " = u." + U_ID
                + " WHERE u." + U_ID + " = ?";
        Cursor cursor = db.rawQuery(todoQuery, new String[] { id });

        while (cursor.moveToNext()){
            Todo todo = new Todo();

            todo.setId(cursor.getString(cursor.getColumnIndex(T_ID)));
            todo.setNom(cursor.getString(cursor.getColumnIndex(T_NOM)));
            todo.setDescp(cursor.getString(cursor.getColumnIndex(T_DESCP)));
            todo.setState(cursor.getString(cursor.getColumnIndex(T_STATE)));

            todoList.add(todo);
        }
        db.close();
        return  todoList;
    }
    public ArrayList<Todo> getTodosByState(String id, String state){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Todo> todoList = new ArrayList<>();
        String todoQuery = "SELECT t.* FROM " + T_TODO + " t "
                + "INNER JOIN " + T_USER + " u ON t." + T_REF_USER + " = u." + U_ID
                + " WHERE u." + U_ID + " = ? AND t." + T_STATE + " = ?";
        Cursor cursor = db.rawQuery(todoQuery, new String[] { id, state });

        while (cursor.moveToNext()){
            Todo todo = new Todo();

            todo.setId(cursor.getString(cursor.getColumnIndex(T_ID)));
            todo.setNom(cursor.getString(cursor.getColumnIndex(T_NOM)));
            todo.setDescp(cursor.getString(cursor.getColumnIndex(T_DESCP)));
            todo.setState(cursor.getString(cursor.getColumnIndex(T_STATE)));

            todoList.add(todo);
        }
        db.close();
        return  todoList;
    }
    public HashMap<String, String> getTodoStats(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, String> todoStats = new HashMap<>();

        String todoQuery = "SELECT "+T_STATE+", COUNT(*) AS count FROM "+T_TODO+" WHERE "+T_REF_USER+" =? GROUP BY "+T_STATE+" ORDER BY "+T_STATE+" ASC";
        Cursor cursor = db.rawQuery(todoQuery, new String[] { id });

        while (cursor.moveToNext()){
            String val = cursor.getString(cursor.getColumnIndex("count"));
            if (val.equals("null")) val = "0";
            todoStats.put(cursor.getString(cursor.getColumnIndex(T_STATE)), val);
        }
        db.close();
        return  todoStats;
    }

    // USER CRUD
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SQLhelper.U_NOM, user.getNom());
        cv.put(SQLhelper.U_EMAIL, user.getEmail());
        cv.put(SQLhelper.U_PASS, user.getMotdpass());

        db.insert(SQLhelper.T_USER, null, cv);
        db.close();
    }
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SQLhelper.U_NOM, user.getNom());
        cv.put(SQLhelper.U_EMAIL, user.getEmail());
        cv.put(SQLhelper.U_PASS, user.getMotdpass());

        db.update(SQLhelper.T_USER, cv, U_ID+"=?", new String[]{user.getId()});
        db.close();
    }
    public void deleteUser(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("PRAGMA foreign_keys=ON");
//        db.delete(SQLhelper.T_USER, U_ID+" = ?", new String[]{id});
//        db.close();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete rows from child tables first
            db.delete(SQLhelper.T_TODO, T_REF_USER+" = ?", new String[]{id});

            // Now delete the row from the parent table
            db.delete(SQLhelper.T_USER, U_ID+" = ?", new String[]{id});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // Handle the exception
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    public User getUser(String email,String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        User user = new User();
        String loginQuery = "SELECT * FROM "+T_USER+" WHERE "+U_EMAIL+" = ? AND "+U_PASS+" = ?";
        Cursor cursor = db.rawQuery(loginQuery, new String[] { email, pass });

        while (cursor.moveToNext()){

            user.setId(cursor.getString(cursor.getColumnIndex(U_ID)));
            user.setNom(cursor.getString(cursor.getColumnIndex(U_NOM)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(U_EMAIL)));
            user.setMotdpass(cursor.getString(cursor.getColumnIndex(U_PASS)));

        }

        db.close();
        return  user;
    }
}
