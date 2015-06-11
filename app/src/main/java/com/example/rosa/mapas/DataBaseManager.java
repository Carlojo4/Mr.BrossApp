package com.example.rosa.mapas;

/**
 * Created by ROSA on 10/06/2015.
 */
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

public class DataBaseManager {
    public static final String TABLE_NAME = "Sedes";
    public static final String CN_ID = "_id";  // Nombre columna
    public static final String CN_SEDE = "sede";
    public static final String CN_LAT = "lat";
    public static final String CN_LON = "lon";
    // create table contactos(
    //                          _id integer primary key autoincrement,
    //                          nombre text not null,
    //                          telefono text);
    public static final String CREATE_TABLE = "create table "+ TABLE_NAME+ " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_SEDE + " text not null,"
            + CN_LAT + " text,"
            + CN_LON + " text);";

    DBhelper helper;
    SQLiteDatabase db;
    public DataBaseManager(Context context) {
        helper = new DBhelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues generarContentValues (String Sede, String Lat, String Lon){
        ContentValues valores = new ContentValues();
        valores.put(CN_SEDE,Sede);
        valores.put(CN_LAT,Lat);
        valores.put(CN_LON,Lon);
        return valores;
    }

    public void insertar(String Sede, String Lat, String Lon){
        db.insert(TABLE_NAME,null,generarContentValues(Sede, Lat,Lon));
    }

    public Cursor cargarCursorContactos(){
        String [] columnas = new String[]{CN_ID,CN_SEDE,CN_LAT,CN_LON};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public void eliminar(String Sede){
        db.delete(TABLE_NAME,CN_SEDE + "=?", new String[]{Sede});
    }

    public void Modificar(String Sede, String nuevaLat, String nuevaLon){
        db.update(TABLE_NAME, generarContentValues(Sede, nuevaLat, nuevaLon), CN_SEDE + "=?", new String[]{Sede});
    }

    public Cursor buscarContacto(String Sede) {
        String [] columnas = new String[]{CN_ID,CN_SEDE,CN_LAT,CN_LON};
        return db.query(TABLE_NAME,columnas,CN_SEDE + "=?",new String[]{Sede},null,null,null);
    }
}
