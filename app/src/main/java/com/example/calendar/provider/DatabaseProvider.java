package com.example.calendar.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.litepal.LitePal;

/**
 * 日程信息内容提供器
 */
public class DatabaseProvider extends ContentProvider {

    private static final int SCHEDULE_DIR = 0;//访问表中的所有数据

    private static final int SCHEDULE_ITEM = 1;//访问表中的单条数据

    private static final String AUTHORITY = "com.example.calendar.provider";

    private static UriMatcher uriMatcher;

    private SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"schedule",SCHEDULE_DIR);
        uriMatcher.addURI(AUTHORITY,"schedule/#",SCHEDULE_ITEM);
    }

    @Override
    public boolean onCreate() {
        db = LitePal.getDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case SCHEDULE_DIR:
                //cursor = LitePal.findBySQL("select * from schedule");
                cursor = db.query("Schedule",projection,selection,selectionArgs,null,null,"calendar desc");
                break;
            case SCHEDULE_ITEM:
                String scheduleId = uri.getPathSegments().get(1);
                //cursor = LitePal.findBySQL("select * from schedule where calendar=?",calendar);
                cursor = db.query("Schedule",projection,"id=?",new String[]{scheduleId},null,null,sortOrder);
                break;
                default:
                    break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case SCHEDULE_DIR:
            case SCHEDULE_ITEM:
                long newScheduleId = db.insert("Schedule", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/schedule/" + newScheduleId);
                break;
                default:
                    break;
        }
        return  uriReturn;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case SCHEDULE_DIR:
                updateRows = db.update("Schedule",values,selection,selectionArgs);
                break;
            case SCHEDULE_ITEM:
                String scheduleId = uri.getPathSegments().get(1);
                updateRows = db.update("Schedule",values,"id=?",new String[]{scheduleId});
                break;
                default:
                    break;
        }
        return updateRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case SCHEDULE_DIR:
                deleteRows = db.delete("Schedule",selection,selectionArgs);
                break;
            case SCHEDULE_ITEM:
                String scheduleId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Schedule","id=?",new String[]{scheduleId});
                break;
                default:
                    break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case SCHEDULE_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.calendar.provider.schedule";
            case SCHEDULE_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.calendar.provider.schedule";
        }
        return null;
    }

}
