package com.dollop.dukaadriver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.dollop.dukaadriver.UtilityTools.Utils;

import java.util.ArrayList;

/**
 * Created by CRUD-PC on 10/7/2016.
 */
public class UserDataHelper {
    private static UserDataHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    Context cx;

    public UserDataHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    public static UserDataHelper getInstance() {
        return instance;
    }

    public void open() {
        db = dm.getWritableDatabase();
    }

    public void close() {
        //  db.close();
    }

    public void read() {
        db = dm.getReadableDatabase();
    }

    public void delete(int companyId) {
        open();
        db.delete(UserModel.TABLE_NAME, UserModel.KEY_ID + " = '" + companyId + "'", null);
        close();
    }

    public void deleteAll() {
        open();
        db.delete(UserModel.TABLE_NAME, null, null);
        close();
    }

    private boolean isExist(UserModel userModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + UserModel.TABLE_NAME + " where " + UserModel.KEY_ID + "='"
                + userModel.getUserId() + "'", null);
        if (cur.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void insertData(UserModel userModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(UserModel.KEY_USER_ID, userModel.getUserId());
        values.put(UserModel.KeyUserToken, userModel.getUserToken());
        values.put(UserModel.KeyUserRole, userModel.getUserRole());
        values.put(UserModel.KeyuserCompanyId, userModel.getUserCompanyId());

        values.put(UserModel.FEEDBACK_CLEAN, userModel.getUserFeedback());

        values.put(UserModel.KEY_USERNAME, userModel.getUserName());
        values.put(UserModel.KEY_USERPHONE, userModel.getUserPhone());
        values.put(UserModel.KEY_USERDOJ, userModel.getUserDoj());
        values.put(UserModel.KEY_USER_Profile_PIC, userModel.getProfile_pic());



        if (!isExist(userModel)) {
            Utils.E("insert successfully");
            db.insert(UserModel.TABLE_NAME, null, values);
        } else {
            Utils.E("update successfully" + userModel.getUserId());
            db.update(UserModel.TABLE_NAME, values, UserModel.KEY_ID + "=" + userModel.getUserId(), null);
        }
        close();
    }

    public ArrayList<UserModel> getList() {
        ArrayList<UserModel> userItem = new ArrayList<UserModel>();
        read();
        Cursor cursor = db.rawQuery("select * from " + UserModel.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                UserModel taxiModel = new UserModel();
                taxiModel.setUserId(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_ID)));
                taxiModel.setUserToken(cursor.getString(cursor.getColumnIndex(UserModel.KeyUserToken)));
                taxiModel.setUserRole(cursor.getString(cursor.getColumnIndex(UserModel.KeyUserRole)));
                taxiModel.setUserCompanyId(cursor.getString(cursor.getColumnIndex(UserModel.KeyuserCompanyId)));
                taxiModel.setUserFeedback(cursor.getString(cursor.getColumnIndex(UserModel.FEEDBACK_CLEAN)));

                taxiModel.setUserName(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERNAME)));
                taxiModel.setUserPhone(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERPHONE)));
                taxiModel.setUserDoj(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USERDOJ)));
                taxiModel.setProfile_pic(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_Profile_PIC)));

                userItem.add(taxiModel);

            } while ((cursor.moveToPrevious()));
            cursor.close();
        }
        close();
        return userItem;
    }
}