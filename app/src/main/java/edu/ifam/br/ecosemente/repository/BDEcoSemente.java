package edu.ifam.br.ecosemente.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDEcoSemente extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecosementedb";

    private static final int DATABASE_VERSION = 8;

    public BDEcoSemente(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE comprador (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "cpf_cnpj TEXT," +
                        "email TEXT," +
                        "nome TEXT," +
                        "telefone TEXT)"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE venda (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "data_venda TEXT ," +
                        "valor_total REAL," +
                        "comprador_id INTEGER," +
                        "FOREIGN KEY (comprador_id) REFERENCES comprador(id))"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE nome_cientifico (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL UNIQUE,"+
                         "nome_popular TEXT)" // Nome científico
        );

        // 3. MODIFICAR A TABELA 'semente' PARA CORRESPONDER AO NOVO LAYOUT
        sqLiteDatabase.execSQL(
                "CREATE TABLE semente (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT," + // Nome Popular
                        "nome_cientifico_id INTEGER," + // Chave Estrangeira

//                        // --- CAMPOS DO LAYOUT ANTIGO (detalhe_semente) ---
//                        "cuidado TEXT," +
//                        "descricao TEXT," +
//                        "especie TEXT," +
//                        "preco REAL," +
//                        "quantidade INTEGER," +

                        // --- CAMPOS DO NOVO LAYOUT (cadastro_semente) ---
                        "epoca_inicio TEXT," + // Em vez de 'epoca_plantio'
                        "epoca_fim TEXT," +
                        "tipo_cultivo TEXT," + // "Natural" ou "Plantada"
                        "tempo_medio_colheita INTEGER," + // Já existia, mas está no novo layout
                        "tamanho_porte TEXT," + // "Pequeno", "Médio", "Grande"
                        "latitude REAL," +
                        "longitude REAL," +
                        "caminho_imagem TEXT," +

                        "FOREIGN KEY (nome_cientifico_id) REFERENCES nome_cientifico(id))" // Definição da FK
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE item_venda (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "preco_item REAL," +
                        "quantidade INTEGER," +
                        "semente_id INTEGER," +
                        "venda_id INTEGER," +
                        "FOREIGN KEY (semente_id) REFERENCES semente(id)," +
                        "FOREIGN KEY (venda_id) REFERENCES venda(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // A ordem do DROP é importante por causa das chaves estrangeiras
        db.execSQL("DROP TABLE IF EXISTS item_venda");
        db.execSQL("DROP TABLE IF EXISTS semente"); // Depende de 'nome_cientifico'
        db.execSQL("DROP TABLE IF EXISTS venda"); // Depende de 'comprador'
        db.execSQL("DROP TABLE IF EXISTS comprador");
        db.execSQL("DROP TABLE IF EXISTS nome_cientifico");
        onCreate(db);
    }
}