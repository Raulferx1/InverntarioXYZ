package com.example.inverntarioxyz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnCrud = findViewById(R.id.bnt_crud);
        Button btnMqtt = findViewById(R.id.btn_mqtt);

        btnCrud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCrud = new Intent(menu.this, Crud.class);
                startActivity(intentCrud);
            }
        });

        btnMqtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(menu.this, "La opción MQTT está en desarrollo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

