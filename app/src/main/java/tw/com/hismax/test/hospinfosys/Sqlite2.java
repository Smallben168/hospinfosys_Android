package tw.com.hismax.test.hospinfosys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OH HO on 2016/5/24.
 */
public class Sqlite2 extends SQLiteOpenHelper {
    private final static int _DBVersion = 1;
    private final static String _DBName = "SampleList.db";
    private final static String _TableName = "MySample";

    //資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
    private static final int VERSION = 1;//資料庫版本

    //建構子
    public Sqlite2(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context, name, factory, version);
    }

    public Sqlite2(Context context,String name) {
        this(context, name, null, VERSION);
    }

    public Sqlite2(Context context, String name, int version) {
        this(context, name, null, version);
    }

    //輔助類建立時運行該方法
    public Sqlite2(Context context) {
        super(context, _DBName, null, _DBVersion);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_TABLE =
                "create table newMemorandum("
                        + "_ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
                        + "_chart_no VARCHAR,"
                        + "_getRegistrationId VARCHAR,"
                        + "_id_no VARCHAR,"
                        + "_birth_date INT,"//新增欄位

                        + ")";
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        final String SQL = "DROP TABLE " + _TableName;
        db.execSQL(SQL);
    }

}
