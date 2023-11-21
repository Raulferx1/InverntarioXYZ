package com.example.inverntarioxyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class Crud extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private MainAdapter originalAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Programación Android"), MainModel.class)
                .build();
        originalAdapter = new MainAdapter(options);
        mainAdapter = originalAdapter;

        recyclerView.setAdapter(mainAdapter);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent((getApplicationContext()), Agregar.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buscador, menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<MainModel> options;

        if (str.isEmpty()) {
            options = new FirebaseRecyclerOptions.Builder<MainModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Programación Android"), MainModel.class)
                    .build();
        } else {
            options = new FirebaseRecyclerOptions.Builder<MainModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Programación Android")
                            .orderByChild("Nombre").startAt(str).endAt(str + "\uf8ff"), MainModel.class)
                    .build();
        }

        mainAdapter.updateOptions(options);


        Log.d("SEARCH", "Search query: " + str);
        Log.d("SEARCH", "Number of items after search: " + mainAdapter.getItemCount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}