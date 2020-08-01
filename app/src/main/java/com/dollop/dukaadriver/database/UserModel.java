package com.dollop.dukaadriver.database;

import android.database.sqlite.SQLiteDatabase;

import com.dollop.dukaadriver.UtilityTools.Utils;


/**
 * Created by CRUD-PC on 10/5/2016.
 */
public class UserModel {
    public static final String TABLE_NAME = "UserModelFuel";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KeyUserToken = "userToken";
    public static final String KeyuserCompanyId = "userCompanyId";
    public static final String FEEDBACK_CLEAN= "userFeedback";
    public static final String KeyUserRole= "userRole";


    public static final String KEY_USERNAME = "userName";
    public static final String KEY_USERPHONE= "userPhone";
    public static final String KEY_USERDOJ= "userDOJ";
    public static final String KEY_USER_Profile_PIC= "Profile_pic";

    public static void creteTable(SQLiteDatabase db) {
        String CREATE_CLIENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_ID + " text," +
                KeyUserToken + " text,"+
                KeyuserCompanyId + " text,"+
                FEEDBACK_CLEAN + " text, " +
                KeyUserRole + " text, " +
                KEY_USERNAME + " text, " +
                KEY_USERPHONE + " text, " +
                KEY_USERDOJ + " text, " +

                KEY_USER_Profile_PIC + " text " +
                ")";

        db.execSQL(CREATE_CLIENTTABLE);
        Utils.E("check Create table::"+CREATE_CLIENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }


    String userId;
    String userToken;
    String userRole;
    String userName;
    String userPhone;
    String userDoj;
    String userFeedback;
    String Profile_pic;

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }


    public String getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    String userCompanyId;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserDoj() {
        return userDoj;
    }

    public void setUserDoj(String userDoj) {
        this.userDoj = userDoj;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }
}
