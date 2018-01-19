package pt.a030492.surfstatusv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AdaptadorBaseDados {
    private AjudaUsoBaseDados dbHelper;
    private SQLiteDatabase database;
    public AdaptadorBaseDados(Context context) {
        dbHelper = new AjudaUsoBaseDados(context.getApplicationContext());
    }
    public AdaptadorBaseDados open() {
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }

    public void inserirPraia(String oNome, String oUrl) {
        ContentValues values = new ContentValues() ;
        values.put("nomePraia", oNome);
        values.put("url", oUrl);
        database.insert("praias", null, values);
    }

    public void dropPraias(){
        database.execSQL("DROP TABLE IF EXISTS praias");
        database.execSQL("CREATE TABLE praias(_id integer primary key autoincrement, nomePraia varchar(40) unique, condicaoActual varchar(40) default '-', url varchar(60), listar bit default 0)");

    }

    private Cursor obterTodosRegistos() {
        String[] colunas = new String[5];
        colunas[0] = "_id";
        colunas[1] = "nomePraia";
        colunas[2] = "condicaoActual";
        colunas[3] = "url";
        colunas[4] = "listar";
        return database.query("praias", colunas, null, null, null, null, "_id");
    }

    public void updateListar(String _nomePraia, int _listar) {
        String whereClause = "nomePraia = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = _nomePraia;
        ContentValues values = new ContentValues();
        values.put("listar", _listar);
        database.update("praias", values, whereClause, whereArgs);
    }

    public int getSize() {
        return (int)DatabaseUtils.queryNumEntries(database, "praias");
    }

    public String getNomePraia(int i) {
        String nomePraia;
        String whereClause = "_id = ?";
        String whereArgs[] = new String[1];
        whereArgs[0] = i + "";
        Cursor cursor = database.query("praias", null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();

        nomePraia = cursor.getString(1);
        cursor.close();

        return nomePraia;
    }

    public String getUrl(String s) {
        String url;
        String whereClause = "nomePraia = ?";
        String whereArgs[] = new String[1];
        whereArgs[0] = s;
        Cursor cursor = database.query("praias", null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();

        url = cursor.getString(3);
        cursor.close();

        return url;
    }

    public boolean getListarPraia(int i){
        boolean listarPraia = false;
        String whereClause = "_id = ?";
        String whereArgs[] = new String[1];
        whereArgs[0] = i + "";
        Cursor cursor = database.query("praias", null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();

        if(cursor.getString(4).equals("1")){
            listarPraia = true;
        }
        else{
            listarPraia = false;
        }

        cursor.close();

        return listarPraia;
    }

    public ArrayList<String[]> getPraiasListar(){
        ArrayList<String[]> praiasListar = new ArrayList<>();

        Cursor cursor = obterTodosRegistos();
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(4).equals("1")){
                    String[] s = new String[4];
                    s[0] = cursor.getString(1) +"";//nome
                    s[1] = cursor.getString(2) +"";//condicao
                    s[2] = cursor.getString(3) +"";//url
                    s[3] = cursor.getString(4) +"";//listar
                    praiasListar.add(s);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return praiasListar;
    }

    public void updateCondicaoPraia(String[] s) {
        String whereClause = "nomePraia = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = s[0];
        ContentValues values = new ContentValues();
        values.put("condicaoActual", s[1]);
        database.update("praias", values, whereClause, whereArgs);
    }
}