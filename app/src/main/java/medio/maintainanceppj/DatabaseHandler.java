package medio.maintainanceppj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    //database ver
    private static final int DATABASE_VERSION = 1;
    //db name
    public static final String DATABASE_NAME = "remind";
    //table name
    public static final String TABLE_NAME = "tasks";
    //table fields
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEGIATAN = "kegiatan";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_JAM = "jam";
    public static final String COLUMN_RUANGAN = "ruangan";

    SQLiteDatabase database;

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ( "
                +COLUMN_ID+" INTEGER primary key autoincrement, "+COLUMN_KEGIATAN+" TEXT, "
                +COLUMN_RUANGAN+" TEXT, "+COLUMN_TANGGAL+" TEXT, "+COLUMN_JAM+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }
}
