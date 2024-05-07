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

public class login extends AppCompatActivity {
    SQLhelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        DB = new SQLhelper(this);

        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        EditText email = findViewById(R.id.email_input);
        String vemail = email.getText().toString();
        if (vemail.isEmpty()) {
            Toast.makeText(this, "Email doit etre non vide", Toast.LENGTH_SHORT).show();
        }

        EditText mdp = findViewById(R.id.mdp_input);
        String vmdp = mdp.getText().toString();
        if (vmdp.isEmpty()) {
            Toast.makeText(this, "Mot de pass doit etre non vide", Toast.LENGTH_SHORT).show();
        }

        User user = DB.getUser(vemail, vmdp);

        if (user.getId() != null) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", user.getId());
            editor.putString("nom", user.getNom());
            editor.putString("email", user.getEmail());
            editor.putString("mdp", user.getMotdpass());
            editor.apply();

            Toast.makeText(this, "Connecté", Toast.LENGTH_SHORT).show();

           Intent intent = new Intent(this, home.class);
           startActivity(intent);
        }

        if (user.getId() == null){
            email.setText("");
            mdp.setText("");
            Toast.makeText(this, "Veuillez verifier vos coordonnees", Toast.LENGTH_SHORT).show();
        }
    }
}