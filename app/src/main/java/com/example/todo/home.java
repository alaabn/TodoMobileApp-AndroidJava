package com.example.todo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.database.SQLhelper;
import com.example.todo.database.Todo;
import com.example.todo.database.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class home extends AppCompatActivity {

    SQLhelper DB;
    HashMap<String, String> stats = new HashMap<>();
    ArrayList<Todo> todos;
    ArrayAdapter<Todo> todoAdapter;
    Dialog dialog;
    User user = new User();
    Todo editTodo;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        DB = new SQLhelper(this);
        dialog = new Dialog(this);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user.setId(sharedPreferences.getString("id", "NOT_FOUND"));
        user.setNom(sharedPreferences.getString("nom", "NOT_FOUND"));
        user.setEmail(sharedPreferences.getString("email", "NOT_FOUND"));
        user.setMotdpass(sharedPreferences.getString("mdp", "NOT_FOUND"));

        getStats();

        todos = DB.getTodos(user.getId());
        todoAdapter = new TodoAdapter(this, todos);

        ListView lv = findViewById(R.id.todo_list);
        lv.setAdapter(todoAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Todo item = todos.get(position);
            showItemListDialog(item);
        });

        Button add = findViewById(R.id.b_ajouter);
        add.setOnClickListener(v -> {
                showAddDialog();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getStats() {
        stats.clear();
        stats.putAll(DB.getTodoStats(user.getId()));

        TextView tv = findViewById(R.id.welcome);
        tv.setText(String.format("Bienvenu, %s!", user.getNom()));

        TextView tdone = findViewById(R.id.done_text);
        tdone.setText(String.format("%s Terminé", stats.get(Todo.DONE) != null ? stats.get(Todo.DONE) : 0));
        TextView tprogress = findViewById(R.id.prog_text);
        tprogress.setText(String.format("%s En cours", stats.get(Todo.PROGRESS) != null ? stats.get(Todo.PROGRESS) : 0));
        TextView topen = findViewById(R.id.created_text);
        topen.setText(String.format("%s Creér", stats.get(Todo.OPEN) != null ? stats.get(Todo.OPEN) : 0));
        TextView tcanceled = findViewById(R.id.cancel_text);
        tcanceled.setText(String.format("%s Annuler", stats.get(Todo.CANCELED) != null ? stats.get(Todo.CANCELED) : 0));
    }

    public void progTodos(View view) {
        todos.clear();
        todos.addAll(DB.getTodosByState(user.getId(), Todo.PROGRESS));
        todoAdapter.notifyDataSetChanged();
    }

    public void cancelTodos(View view) {
        todos.clear();
        todos.addAll(DB.getTodosByState(user.getId(), Todo.CANCELED));
        todoAdapter.notifyDataSetChanged();
    }
    public void allTodos(View view) {
        todos.clear();
        todos.addAll(DB.getTodos(user.getId()));
        todoAdapter.notifyDataSetChanged();
    }

    public void doneTodos(View view) {
        todos.clear();
        todos.addAll(DB.getTodosByState(user.getId(), Todo.DONE));
        todoAdapter.notifyDataSetChanged();
    }

    public void createdTodos(View view) {
        todos.clear();
        todos.addAll(DB.getTodosByState(user.getId(), Todo.OPEN));
        todoAdapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        dialog.setContentView(R.layout.activity_add_todo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button ajout, annuler;

        ajout = dialog.findViewById(R.id.b_add_update);
        annuler = dialog.findViewById(R.id.b_annuler);

        annuler.setOnClickListener(v->{
            dialog.dismiss();
        });

        ajout.setOnClickListener(v->{
            EditText nom, descp;
            Spinner state;

            nom = dialog.findViewById(R.id.nom_input);
            descp = dialog.findViewById(R.id.descp_input);
            state = dialog.findViewById(R.id.cat_input);
            state.setEnabled(false);
            state.setClickable(false);

            String vnom, vdescp;

            vnom = nom.getText().toString();
            vdescp = descp.getText().toString();

            Todo todo = new Todo(vnom, vdescp, Todo.OPEN, user.getId());

            DB.insertTodo(todo);
            todos.clear();
            todos.addAll(DB.getTodos(user.getId()));
            todoAdapter.notifyDataSetChanged();
            getStats();
            dialog.dismiss();
            showImage("Add");

        });

        dialog.show();
    }

    private void showUpdateDialog() {
        dialog.setContentView(R.layout.activity_add_todo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText nom, descp;
        Spinner state;

        nom = dialog.findViewById(R.id.nom_input);
        descp = dialog.findViewById(R.id.descp_input);
        state = dialog.findViewById(R.id.cat_input);

        state.setEnabled(true);
        state.setClickable(true);

        nom.setText(editTodo.getNom());
        descp.setText(editTodo.getDescp());
        // show the original attribute on slelction
        String[] spinnerData = getResources().getStringArray(R.array.state_todo);
        int position = Arrays.asList(spinnerData).indexOf(editTodo.getState());
        if (position != -1) {
            state.setSelection(position);
        }

        TextView titre = dialog.findViewById(R.id.titre);
        titre.setText("Modifier la Tache");

        Button update, annuler;

        update = dialog.findViewById(R.id.b_add_update);
        update.setText("Modif");
        annuler = dialog.findViewById(R.id.b_annuler);

        annuler.setOnClickListener(v->{
            dialog.dismiss();
        });

        update.setOnClickListener(v->{
            String vnom, vdescp, vstate;

            vnom = nom.getText().toString();
            vdescp = descp.getText().toString();
            vstate = state.getSelectedItem().toString();

            Todo todo = new Todo(editTodo.getId(), vnom, vdescp, vstate, user.getId());

            DB.updateTodo(todo);
            todos.clear();
            todos.addAll(DB.getTodos(user.getId()));
            todoAdapter.notifyDataSetChanged();
            getStats();
            dialog.dismiss();
            showImage("Edit");
        });

        dialog.show();
    }

    private void showItemListDialog(Todo item) {
        dialog.setContentView(R.layout.item_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button update, annuler, supprim;
        TextView nom, state, descp;

        nom = dialog.findViewById(R.id.nom);
        state = dialog.findViewById(R.id.state);
        descp = dialog.findViewById(R.id.descp);

        nom.setText(item.getNom());
        state.setText(item.getState());
        descp.setText(item.getDescp());

        supprim = dialog.findViewById(R.id.b_supp);
        supprim.setOnClickListener(v->{
            DB.deleteTodo(item.getId());
            todos.clear();
            todos.addAll(DB.getTodos(user.getId()));
            todoAdapter.notifyDataSetChanged();
            dialog.dismiss();
            showImage("Delete");
        });

        annuler = dialog.findViewById(R.id.b_annuler);
        annuler.setOnClickListener(v->{
            dialog.dismiss();
        });

        update = dialog.findViewById(R.id.b_modif);
        update.setOnClickListener(v->{
            editTodo = item;
            dialog.dismiss();
            showUpdateDialog();
        });
        dialog.show();
    }

    public void profile(View view) {
        dialog.setContentView(R.layout.profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button update, annuler, supprim, logout;
        TextView nom, email;

        nom = dialog.findViewById(R.id.nom);
        email = dialog.findViewById(R.id.email);

        nom.setText(user.getNom());
        email.setText(user.getEmail());

        supprim = dialog.findViewById(R.id.b_supp);
        supprim.setOnClickListener(v->{
            DB.deleteUser(user.getId());
            finishAffinity();
        });

        annuler = dialog.findViewById(R.id.b_annuler);
        annuler.setOnClickListener(v->{
            dialog.dismiss();
        });

        logout = dialog.findViewById(R.id.b_logout);
        logout.setOnClickListener(v->{
            finishAffinity();
        });

        update = dialog.findViewById(R.id.b_modif);
        update.setOnClickListener(v->{
            showUpdateUserDialog();
        });
        dialog.show();
    }

    private void showUpdateUserDialog() {
        dialog.setContentView(R.layout.activity_add_todo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tnom, temail, tmdp;
        EditText nom, email, mdp;
        Spinner state;

        tnom = dialog.findViewById(R.id.nom_label);
        tnom.setText("Nom:");
        nom = dialog.findViewById(R.id.nom_input);

        tmdp = dialog.findViewById(R.id.descp_label);
        tmdp.setText("Mot de pass:");
        mdp = dialog.findViewById(R.id.descp_input);
        mdp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        state = dialog.findViewById(R.id.cat_input);
        state.setVisibility(View.GONE);

        temail = dialog.findViewById(R.id.state_label);
        temail.setText("Email");
        email = dialog.findViewById(R.id.email_input);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        email.setVisibility(View.VISIBLE);

        nom.setText(user.getNom());
        email.setText(user.getEmail());
        mdp.setText(user.getMotdpass());

        TextView titre = dialog.findViewById(R.id.titre);
        titre.setText("Modifier Votre Profile");

        Button update, annuler;

        update = dialog.findViewById(R.id.b_add_update);
        update.setText("Modif");
        annuler = dialog.findViewById(R.id.b_annuler);

        annuler.setOnClickListener(v->{
            dialog.dismiss();
        });

        update.setOnClickListener(v->{
            String vnom, vemail, vmdp;

            user.setNom(nom.getText().toString());
            user.setEmail(email.getText().toString());
            user.setMotdpass(mdp.getText().toString());

            DB.updateUser(user);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", user.getId());
            editor.putString("nom", user.getNom());
            editor.putString("email", user.getEmail());
            editor.putString("mdp", user.getMotdpass());
            editor.apply();

            this.user.setNom(user.getNom());
            this.user.setMotdpass(user.getMotdpass());
            this.user.setEmail(user.getEmail());

            getStats();

            dialog.dismiss();
        });

        dialog.show();
    }

    private  void showImage(String state) {
        int id = 0;

        switch (state) {
            case "Add":
                id = R.id.on_add;
                break;
            case "Edit":
                id = R.id.on_edit;
                break;
            case "Delete":
                id = R.id.on_delete;
                break;
        }
        imageView = findViewById(id);

        imageView.setVisibility(View.VISIBLE);
        imageView.setAlpha(0f);
        imageView.setScaleX(1f);
        imageView.setScaleY(1f);

        imageView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    // Scale the ImageView out to the center of the screen with an ease-out animation
                    imageView.postDelayed( ()->{
                        imageView.animate()
                                .scaleX(0f)
                                .scaleY(0f)
                                .setDuration(500)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                    }, 400);
                })
                .start();

        // Set the initial scale to 0
        imageView.setScaleX(0f);
        imageView.setScaleY(0f);


//        imageView.setVisibility(View.VISIBLE);
//        imageView.animate()
//                .alpha(1f)
//                .setDuration(1500)
//                .setInterpolator(new AccelerateInterpolator())
//                .withEndAction(() -> {
//                    imageView.animate()
//                            .alpha(0f)
//                            .setDuration(1500)
//                            .setInterpolator(new DecelerateInterpolator())
//                            .start();
//                })
//                .start();
    }
}