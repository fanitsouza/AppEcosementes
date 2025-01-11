package edu.ifam.br.ecosemente;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.recycler.SementeAdapter;
import edu.ifam.br.ecosemente.repository.SementeDAO;

public class ListSemente extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SementeAdapter sementeAdapter;
    private SementeDAO sementeDAO;
    private BottomNavigationView bottomNavigationViewHome;
    private MenuItem menuItemListSementeHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_semente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sementeDAO = new SementeDAO(this);

        setContentView(R.layout.activity_list_semente);

        recyclerView = findViewById(R.id.recyclerViewSemente);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    protected void onStart(){
        super.onStart();
        sementeAdapter = new SementeAdapter(sementeDAO.getSemente(), this);

        recyclerView.setAdapter(sementeAdapter);
    }

    public void vaiParaAdicionarSemente(android.view.MenuItem item){
        Intent intent = new Intent(this, DetalheSemente.class);
        startActivity(intent);
    }

    public void retornaHome(android.view.MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}