package edu.ifam.br.ecosemente.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.Semente;

public class SementeDAO {

    private SQLiteDatabase sqLiteDatabase;
    private SementeDAO(){}

    public SementeDAO(Context context){
        BDEcoSemente bdEcoSemente = new BDEcoSemente(context, "ecosemente", null, 1);
        sqLiteDatabase = bdEcoSemente.getWritableDatabase();
    }

    @SuppressLint("Range")
    public List<Semente> getSemente(){
        List<Semente> sementes = new ArrayList<>();
        String sql = "SELECT * FROM semente";
        Cursor cSementes = sqLiteDatabase.rawQuery(sql, null);

        if(cSementes.moveToFirst()){
            do {
                Semente semente = new Semente();
                semente.setId(cSementes.getLong(cSementes.getColumnIndex("id")));
                semente.setNome(cSementes.getString(cSementes.getColumnIndex("nome")));
                semente.setDescricao(cSementes.getString(cSementes.getColumnIndex("descricao")));
                semente.setEspecie(cSementes.getString(cSementes.getColumnIndex("especie")));
                semente.setEpocaPlantio(cSementes.getString(cSementes.getColumnIndex("epocaPlantio")));
                semente.setTempoMedioColheita(cSementes.getInt(cSementes.getColumnIndex("tempoMedio")));
                semente.setQuantidade(cSementes.getInt(cSementes.getColumnIndex("quantidade")));
                semente.setCuidado(cSementes.getString(cSementes.getColumnIndex("cuidados")));
                semente.setPreco(cSementes.getFloat(cSementes.getColumnIndex("preco")));

                sementes.add(semente);
            }while(cSementes.moveToNext());
        }

        return sementes;
    }

    @SuppressLint("Range")
    public Semente getSemente(Long id){
        String sql = " SELECT * FROM semente WHERE id = ? ";
        String[] selectionArgs = {Long.toString(id)};
        Cursor cSementes = sqLiteDatabase.rawQuery(sql, selectionArgs);
        Semente semente = new Semente();

        if(cSementes.moveToFirst()){

                semente.setId(cSementes.getLong(cSementes.getColumnIndex("id")));
                semente.setNome(cSementes.getString(cSementes.getColumnIndex("nome")));
                semente.setDescricao(cSementes.getString(cSementes.getColumnIndex("descricao")));
                semente.setEspecie(cSementes.getString(cSementes.getColumnIndex("especie")));
                semente.setEpocaPlantio(cSementes.getString(cSementes.getColumnIndex("epocaPlantio")));
                semente.setTempoMedioColheita(cSementes.getInt(cSementes.getColumnIndex("tempoMedio")));
                semente.setQuantidade(cSementes.getInt(cSementes.getColumnIndex("quantidade")));
                semente.setCuidado(cSementes.getString(cSementes.getColumnIndex("cuidados")));
                semente.setPreco(cSementes.getFloat(cSementes.getColumnIndex("preco")));

        }

        return semente;
    }

    public void insert(Semente semente){
        ContentValues cv = new ContentValues();
        cv.put("nome", semente.getNome());
        cv.put("descricao", semente.getDescricao());
        cv.put("especie", semente.getEspecie());
        cv.put("epocaPlantio", semente.getEpocaPlantio());
        cv.put("tempoMedio", semente.getTempoMedioColheita());
        cv.put("quantidade", semente.getQuantidade());
        cv.put("cuidados", semente.getCuidado());
        cv.put("preco",semente.getPreco());

        sqLiteDatabase.insert("semente", null, cv);

    }

    public void update(long id, Semente semente){
        ContentValues cv = new ContentValues();
        cv.put("nome", semente.getNome());
        cv.put("descricao", semente.getDescricao());
        cv.put("especie", semente.getEspecie());
        cv.put("epocaPlantio", semente.getEpocaPlantio());
        cv.put("tempoMedio", semente.getTempoMedioColheita());
        cv.put("quantidade", semente.getQuantidade());
        cv.put("cuidados", semente.getCuidado());
        cv.put("preco",semente.getPreco());


        String whereClause = "id =?";

        String[] whereArgs = {Long.toString(id)};

        sqLiteDatabase.update("semente",cv,whereClause,whereArgs);

    }

    public void delete(long id){
        String whereClause = "id = ?";
        String[] whereArgs ={Long.toString(id)};
        sqLiteDatabase.delete("semente", whereClause, whereArgs);
    }

}
