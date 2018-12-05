package medio.maintainanceppj.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    //database ver
    private static final int DATABASE_VERSION = 2;
    //db name
    public static final String DATABASE_NAME = "db_ppj";
    //table name
    public static final String TABLE_NAME = "tasks";
    //table fields
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEGIATAN = "kegiatan";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_JAM = "jam";
    public static final String COLUMN_RUANGAN = "ruangan";


    private final String createDB = "create table if not exists " + TABLE_NAME + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_RUANGAN + " text, "
            + COLUMN_KEGIATAN + " text, "
            + COLUMN_TANGGAL + " text, "
            + COLUMN_JAM + " text)";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
    }
}
