package com.manishdear.todolistj;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manishdear.todolistj.Entity.Note;
import com.manishdear.todolistj.database.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_note);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST );
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
