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

    private BDEcoSemente helper;
    private SQLiteDatabase sqLiteDatabase;

    public SementeDAO(Context context){
        helper = new BDEcoSemente(context);
    }

    public void open() {
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    /**
     * Converte a linha atual do Cursor em um objeto Semente.
     */
    @SuppressLint("Range")
    private Semente cursorToSemente(Cursor cSementes) {
        Semente semente = new Semente();

        semente.setId(cSementes.getLong(cSementes.getColumnIndex("id")));
        semente.setNome(cSementes.getString(cSementes.getColumnIndex("nome")));
        semente.setTempoMedioColheita(cSementes.getInt(cSementes.getColumnIndex("tempo_medio_colheita")));

        // --- MUDANÇA: Checa se o valor é NULL no banco ---
        if (cSementes.isNull(cSementes.getColumnIndex("nome_cientifico_id"))) {
            semente.setNomeCientificoId(null);
        } else {
            semente.setNomeCientificoId(cSementes.getLong(cSementes.getColumnIndex("nome_cientifico_id")));
        }
        // --- Fim da Mudança ---

        semente.setEpocaInicio(cSementes.getString(cSementes.getColumnIndex("epoca_inicio")));
        semente.setEpocaFim(cSementes.getString(cSementes.getColumnIndex("epoca_fim")));
        semente.setTipoCultivo(cSementes.getString(cSementes.getColumnIndex("tipo_cultivo")));
        semente.setTamanhoPorte(cSementes.getString(cSementes.getColumnIndex("tamanho_porte")));
        semente.setLatitude(cSementes.getDouble(cSementes.getColumnIndex("latitude")));
        semente.setLongitude(cSementes.getDouble(cSementes.getColumnIndex("longitude")));
        semente.setCaminhoImagem(cSementes.getString(cSementes.getColumnIndex("caminho_imagem")));

        return semente;
    }

    /**
     * Retorna uma lista de todas as sementes.
     */
    public List<Semente> getSemente(){
        List<Semente> sementes = new ArrayList<>();
        String sql = "SELECT * FROM semente";
        Cursor cSementes = sqLiteDatabase.rawQuery(sql, null);

        if(cSementes.moveToFirst()){
            do {
                sementes.add(cursorToSemente(cSementes));
            } while(cSementes.moveToNext());
        }
        cSementes.close();
        return sementes;
    }

    /**
     * Retorna uma semente específica pelo seu ID.
     */
    public Semente getSemente(Long id){
        String sql = " SELECT * FROM semente WHERE id = ? ";
        String[] selectionArgs = {Long.toString(id)};
        Cursor cSementes = sqLiteDatabase.rawQuery(sql, selectionArgs);
        Semente semente = null;

        if(cSementes.moveToFirst()){
            semente = cursorToSemente(cSementes);
        }
        cSementes.close();
        return semente;
    }

    /**
     * Coleta todos os dados do objeto Semente para inserir no banco.
     */
    private ContentValues sementeToContentValues(Semente semente) {
        ContentValues cv = new ContentValues();

        cv.put("nome", semente.getNome());
        cv.put("tempo_medio_colheita", semente.getTempoMedioColheita());

        // --- Campos REMOVIDOS ---
        // cv.put("descricao", semente.getDescricao());
        // cv.put("especie", semente.getEspecie());
        // cv.put("quantidade", semente.getQuantidade());
        // cv.put("cuidado", semente.getCuidado());
        // cv.put("preco", semente.getPreco());

        // --- Novos Campos (v7) ---
        cv.put("nome_cientifico_id", semente.getNomeCientificoId());
        cv.put("epoca_inicio", semente.getEpocaInicio());
        cv.put("epoca_fim", semente.getEpocaFim());
        cv.put("tipo_cultivo", semente.getTipoCultivo());
        cv.put("tamanho_porte", semente.getTamanhoPorte());
        cv.put("latitude", semente.getLatitude());
        cv.put("longitude", semente.getLongitude());
        cv.put("caminho_imagem", semente.getCaminhoImagem());

        return cv;
    }

    public void insert(Semente semente){
        ContentValues cv = sementeToContentValues(semente);
        sqLiteDatabase.insert("semente", null, cv);
    }

    public void update(long id, Semente semente){
        ContentValues cv = sementeToContentValues(semente);
        String whereClause = "id = ?";
        String[] whereArgs = {Long.toString(id)};
        sqLiteDatabase.update("semente", cv, whereClause, whereArgs);
    }

    public void delete(long id){
        String whereClause = "id = ?";
        String[] whereArgs = {Long.toString(id)};
        sqLiteDatabase.delete("semente", whereClause, whereArgs);
    }
}