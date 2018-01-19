package pt.a030492.surfstatusv3;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AjudaUsoBaseDados extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "base-dados.db";
    private static final int VERSION = 1;
    public AjudaUsoBaseDados(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String s =
                "CREATE TABLE praias(_id integer primary key autoincrement, nomePraia varchar(40), condicaoActual varchar(40), url varchar(60), listar bit default 0)";
        db.execSQL(s);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS praias");
        onCreate(db);
    }
}
