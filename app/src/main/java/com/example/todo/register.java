package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.database.SQLhelper;
import com.example.todo.database.User;

public class register extends AppCompatActivity {
    SQLhelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        DB = new SQLhelper(this);

        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void inscrire(View view) {
        EditText nom = findViewById(R.id.mdp_input);
        String vnom = nom.getText().toString();
        if (vnom.isEmpty()) {
            Toast.makeText(this, "Nom doit etre non vide", Toast.LENGTH_SHORT).show();
        }

        EditText email = findViewById(R.id.email_input);
        String vemail = email.getText().toString();
        if (vemail.isEmpty()) {
            Toast.makeText(this, "Email doit etre non vide", Toast.LENGTH_SHORT).show();
        }

        EditText mdp = findViewById(R.id.mdp_input);
        String vmdp = mdp.getText().toString();
        if (vemail.isEmpty()) {
            Toast.makeText(this, "Mot de pass doit etre non vide", Toast.LENGTH_SHORT).show();
        }

        User user = new User(vnom, vemail, vmdp);

        DB.insertUser(user);

        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void annuler(View view) {
        Intent intent = new Intent(this, main.class);
        startActivity(intent);
    }
}