package com.sbsromero.section_04_realm.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sbsromero.section_04_realm.R;
import com.sbsromero.section_04_realm.adapters.NoteAdapter;
import com.sbsromero.section_04_realm.models.Board;
import com.sbsromero.section_04_realm.models.Note;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board>{

    private ListView listView;
    private FloatingActionButton fab;
    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;
    private int boardId;
    private Board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();
        if(getIntent().getExtras() != null){
            boardId = getIntent().getExtras().getInt("id");
        }
        board = realm.where(Board.class).equalTo("id",boardId).findFirst();
        board.addChangeListener(this);
        notes = board.getNotes();

        this.setTitle(board.getTitle());

        fab = (FloatingActionButton) findViewById(R.id.fabAddNote);
        listView = (ListView) findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this,notes,R.layout.list_view_note_item);

        listView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingNote("Agregar nueva nota","Escribir una nota para "+board.getTitle()+".");
            }
        });

        registerForContextMenu(listView);
    }

    private void createNewNote(String noteName){
        realm.beginTransaction();
        Note note = new Note(noteName);
        realm.copyToRealm(note);
        board.getNotes().add(note);
        realm.commitTransaction();
    }

    private void editNote(String newDescription, Note note){
        realm.beginTransaction();
        note.setDescription(newDescription);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    private void deleteNote(int position){
        realm.beginTransaction();
        board.getNotes().deleteFromRealm(position);
        realm.commitTransaction();
    }

    private void deleteAll(){
        realm.beginTransaction();
        board.getNotes().deleteAllFromRealm();
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    /**Dialogs**/
    private void showAlertForCreatingNote(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null){
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String noteName = input.getText().toString().trim();
                if(noteName.length() > 0){
                    createNewNote(noteName);
                }
                else{
                    Toast.makeText(getApplicationContext(),"La nota no puede estar vacia",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    private void showAlertForEditingNote(String title, String message,final Note note)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null){
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);
        input.setText(note.getDescription());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String noteName = input.getText().toString().trim();

                if(noteName.length() == 0){
                    Toast.makeText(getApplicationContext(),"La descripcion es requerida para editar la nota actual",Toast.LENGTH_LONG).show();
                }else if(noteName.equals(note.getDescription())){
                    Toast.makeText(getApplicationContext(),"La descripcion es igual que la anterior",Toast.LENGTH_LONG).show();
                }else{
                    editNote(noteName, note);
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onChange(Board element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String textDescription = notes.get(info.position).getDescription();
        textDescription = (textDescription.length() > 20) ? textDescription.substring(0,20)+"..." : textDescription;
        menu.setHeaderTitle(textDescription);
        getMenuInflater().inflate(R.menu.context_menu_note_activity,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.edit_note:
                showAlertForEditingNote("Editar nota","Editar descripcion de la nota", notes.get(info.position));
                return true;
            case R.id.delete_note:
                deleteNote(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
