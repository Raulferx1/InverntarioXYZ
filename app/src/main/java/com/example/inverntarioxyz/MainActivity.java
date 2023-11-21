package com.example.inverntarioxyz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private Button btnIngresar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIngresar = findViewById(R.id.btn_ingresar);
        progressBar = findViewById(R.id.progressBar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this, menu.class);
                        startActivity(intent);
                    }
                }, 3000);
            }
        });
    }

    public void onIngresarButtonClick() {
        progressBar.setVisibility(View.VISIBLE);
        btnIngresar.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(MainActivity.this, menu.class);
                startActivity(intent);
            }
        }, 3000);
    }
}