package daryadelan.sandogh.zikey.com.daryadelan.repo.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.razanpardazesh.razanlibs.Tools.AsyncWrapper;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserData;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CalendarWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.DatabaseHelper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.SqlAsyncWrapper;

public class UserSqliteRepo implements IUser {


    @Override
    public void personCheck(Context context, User user, IRepoCallBack<User> callBack) {

    }

    @Override
    public void checkSMSisValidate(Context context, User user, IRepoCallBack<User> callBack) {

    }

    @Override
    public void createUser(Context context, User user, IRepoCallBack<User> callBack) {

    }

    @Override
    public void login(Context context, User user, IRepoCallBack<User> callBack) {

    }

    @Override
    public void exitApp(Context context, final IRepoCallBack<User> callBack) {
        if (context == null)
            return;


        SqlAsyncWrapper asyncWrapper = new SqlAsyncWrapper();

        asyncWrapper.setDoOnBackground(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                return clearDatabseAsynchronize(asyncWrapper.getContext());
            }
        }).setDoOnError(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof Throwable)
                    callBack.onError((Throwable) o);
                return null;
            }
        }).setDoOnAnswer(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof User)
                    callBack.onAnswer((User) o);

                return null;
            }
        }).run(context);
    }

    @Override
    public void userInfo(Context context, String tokenType, String token, IRepoCallBack<User> callBack) {

    }


    @Override
    public void saveUserDatas(Context context, final User user, final IRepoCallBack<User> callBack) {

        if (callBack == null)
            return;

        User answer = new User();

        if (user == null) {
            answer.setMessagee("اطلاعات کاربر جهت ذخیره سازی کافی نمیباشد");
            answer.setResultId(-1);
            callBack.onAnswer(answer);

            return;
        }


        SqlAsyncWrapper  asyncWrapper = new SqlAsyncWrapper();

        asyncWrapper.setDoOnBackground(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                return saveUserDatasAsynchronized(asyncWrapper.getContext(), user);

            }
        }).setDoOnAnswer(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof User)
                    callBack.onAnswer((User) o);
                return null;
            }
        }).setDoOnCancel(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                return null;
            }
        }).setDoOnError(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {

                try {
                    if (callBack != null)
                        callBack.onError((Throwable) o);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;

            }
        }).run(context);


    }

    @Override
    public void updateUserDatas(Context context, final User user, final IRepoCallBack<User> callBack) {


        if (callBack == null)
            return;

        User answer = new User();

        if (user == null) {
            answer.setMessagee("اطلاعات کاربر جهت ذخیره سازی کافی نمیباشد");
            answer.setResultId(-1);
            callBack.onAnswer(answer);

            return;
        }


        SqlAsyncWrapper    asyncWrapper = new SqlAsyncWrapper();

        asyncWrapper.setDoOnBackground(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                return updateDatasAsynchronized(asyncWrapper.getContext(), user);

            }
        }).setDoOnAnswer(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof User)
                    callBack.onAnswer((User) o);
                return null;
            }
        }).setDoOnCancel(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                return null;
            }
        }).setDoOnError(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {

                try {
                    if (callBack != null)
                        callBack.onError((Throwable) o);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;

            }
        }).run(context);


    }

    @Override
    public void getUserDatas(Context context, final IRepoCallBack<User> callBack) {

        if (context == null)
            return;

        SqlAsyncWrapper   asyncWrapper = new SqlAsyncWrapper();

        asyncWrapper.setDoOnBackground(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                return getUserAsynchronize(asyncWrapper.getContext());
            }
        }).setDoOnError(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof Throwable)
                    callBack.onError((Throwable) o);
                return null;
            }
        }).setDoOnAnswer(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof User)
                    callBack.onAnswer((User) o);

                return null;
            }
        }).run(context);

    }


    private User saveUserDatasAsynchronized(Context context, User user) {

        User wrapper = new User();


        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if (user == null) {
            wrapper.setResultId(-1);
            return wrapper;
        }


        if (context == null) {
            wrapper.setResultId(-1);
            return wrapper;
        }
        try {
            db.beginTransaction();
            db.delete(UserData.TABLE_USER, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            ContentValues values = new ContentValues();

            values.put(UserData.USR_FIRST_NAME, user.getFirstName());
            values.put(UserData.USR_LAST_NAME, user.getLastName());
            values.put(UserData.USR_MOBILE, user.getMobile());
            values.put(UserData.USR_TOKEN, user.getToken());
            values.put(UserData.USR_TOKEN_TYPE, user.getTokenType());
            values.put(UserData.USR_PERSON_TYPE, user.getPersonType());
            values.put(UserData.USR_TOKEN_EXPIRE, user.getTokenExpireDate());
            values.put(UserData.USR_PERSONAL_CODE, user.getPersonalCode());
            values.put(UserData.USR_LOGIN_DATE, CalendarWrapper.getCurrentPersianDate());


            db.insert(UserData.TABLE_USER, null, values);


        } catch (Exception e) {
            e.printStackTrace();

        }


        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        wrapper.setResultId(0);
        return wrapper;
    }


    private User getUserAsynchronize(Context context) {
        User output = null;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        User wrapper = new User();


        String[] clms = {UserData.USR_FIRST_NAME,
                UserData.USR_LAST_NAME,
                UserData.USR_MOBILE,
                UserData.USR_TOKEN,
                UserData.USR_TOKEN_TYPE,
                UserData.USR_PERSON_TYPE,
                UserData.USR_TOKEN_EXPIRE,
                UserData.USR_LOGIN_DATE,
                UserData.USR_PERSONAL_CODE};


        Cursor cursor = db.query(UserData.TABLE_USER, clms, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User out = new User(cursor);
            if (out != null)
                output = out;
            cursor.moveToNext();
        }

        cursor.close();
        db.close();


        if (output == null) {
            wrapper.setResultId(-1);
            wrapper.setMessagee("#RP ASY ERR 401 خطا در دریافت اطلاعات کاربر");
        }


        wrapper = output;
        wrapper.setResultId(0);

        return wrapper;

    }


    private User updateDatasAsynchronized(Context context, User user) {

        User wrapper = new User();


        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if (user == null) {
            wrapper.setResultId(-1);
            return wrapper;
        }


        if (context == null) {
            wrapper.setResultId(-1);
            return wrapper;
        }

        db.beginTransaction();

        try {


            ContentValues values = new ContentValues();

            if (!TextUtils.isEmpty(user.getFirstName()))
                values.put(UserData.USR_FIRST_NAME, user.getFirstName());
            if (!TextUtils.isEmpty(user.getLastName()))
                values.put(UserData.USR_LAST_NAME, user.getLastName());
            if (!TextUtils.isEmpty(user.getMobile()))
                values.put(UserData.USR_MOBILE, user.getMobile());
            if (!TextUtils.isEmpty(user.getToken()))
                values.put(UserData.USR_TOKEN, user.getToken());
            if (!TextUtils.isEmpty(user.getTokenType()))
                values.put(UserData.USR_TOKEN_TYPE, user.getTokenType());
            if (!TextUtils.isEmpty(user.getPersonType()))
                values.put(UserData.USR_PERSON_TYPE, user.getPersonType());
            if (!TextUtils.isEmpty(user.getTokenExpireDate()))
                values.put(UserData.USR_TOKEN_EXPIRE, user.getTokenExpireDate());
            if (user.getPersonalCode() != 0)
                values.put(UserData.USR_PERSONAL_CODE, user.getPersonalCode());


            db.update(UserData.TABLE_USER, values, null, null);


        } catch (Exception e) {
            e.printStackTrace();

        }


        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        wrapper.setResultId(0);
        return wrapper;
    }

    private User clearDatabseAsynchronize(Context context) {
        User output = null;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        UserInstance.getInstance().clear();
        ServerApiClient.clearRetrofit();

        db.beginTransaction();
        db.delete(UserData.TABLE_USER, null, null);


        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();


        output = new User();
        output.setResultId(1);
        return output;

    }


}
