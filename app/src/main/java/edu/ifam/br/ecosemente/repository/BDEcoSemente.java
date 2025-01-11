package edu.ifam.br.ecosemente.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDEcoSemente extends SQLiteOpenHelper {

    public BDEcoSemente(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE semente("+
                "id integer primary key autoincrement," +
                "nome text,"+
                "descricao text,"+
                "especie text,"+
                "epocaPlantio text,"+
                "tempoMedio int,"+
                "quantidade int,"+
                "cuidados text"+
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
