package com.example.todo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.todo.database.Todo;

import java.util.ArrayList;

public class TodoAdapter extends  ArrayAdapter<Todo> {

        private ArrayList<Todo> todoList;
        Context ctx;

        public TodoAdapter(Context ctx, ArrayList<Todo> todoList) {
            super(ctx, R.layout.single_todo, todoList);
            this.todoList = todoList;
            this.ctx = ctx;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            Todo item = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_todo, parent, false);
            }

            // nom label et nom value
            TextView nomLabel = convertView.findViewById(R.id.nom_label);
            TextView nom = convertView.findViewById(R.id.nom);
            nomLabel.setText("Tache:");
            nom.setText(item.getNom());

            // etat label et state value
            TextView stateLabel = convertView.findViewById(R.id.state_label);
            TextView state = convertView.findViewById(R.id.state);
            stateLabel.setText("Ètat:");
            state.setText(item.getState());
            switch (item.getState()) {
                case Todo.DONE:
                    state.setTextColor(Color.argb(255,9, 189, 79));
                    break;
                case Todo.OPEN:
                    state.setTextColor(Color.argb(255,250, 215, 25));
                    break;
                case Todo.PROGRESS:
                    state.setTextColor(Color.argb(255,1, 173, 255));
                    break;
                case Todo.CANCELED:
                    state.setTextColor(Color.argb(255,255, 13, 61));
                    break;
            }


            return convertView;
        }


}
