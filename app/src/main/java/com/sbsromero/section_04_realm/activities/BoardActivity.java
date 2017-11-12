package com.sbsromero.section_04_realm.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.sbsromero.section_04_realm.adapters.BoardAdapter;
import com.sbsromero.section_04_realm.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener{

    private Realm realm;
    private FloatingActionButton btnFabAddBoard;
    private ListView listView;
    private BoardAdapter adapter;
    private RealmResults<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        listView = (ListView) findViewById(R.id.listViewBoards);
        adapter = new BoardAdapter(this,boards,R.layout.list_view_board_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        btnFabAddBoard = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        btnFabAddBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingBoard("Agregar nuevo tablero","Description de tablero");
            }
        });

        registerForContextMenu(listView);
    }

    /**CRUD Actions**/
    private void createNewBoard(final String boardName) {
        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        realm.commitTransaction();
    }

    private void editBoard(String newName, Board board){
        realm.beginTransaction();
        board.setTitle(newName);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    private void deleteBoard(Board board){
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    private void deleteAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }


    /**Dialogs**/
    private void showAlertForCreatingBoard(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null){
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if(boardName.length() > 0){
                    createNewBoard(boardName);
                }
                else{
                    Toast.makeText(getApplicationContext(),"El nombre es requerido",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    private void showAlertForEditingBoard(String title, String message, final Board board)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null){
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getTitle());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();

                if(boardName.length() == 0){
                    Toast.makeText(getApplicationContext(),"El nombre es requerido para editar el board actual",Toast.LENGTH_LONG).show();
                }else if(boardName.equals(board.getTitle())){
                    Toast.makeText(getApplicationContext(),"El nombre es igual que el anterior",Toast.LENGTH_LONG).show();
                }else{
                    editBoard(boardName,board);
                }
            }
        });
        builder.create().show();
    }

    /**EVENTS*/

    @Override
    public void onChange(RealmResults<Board> element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board_activity,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;
            case R.id.edit_board:
                showAlertForEditingBoard("Editar Board", "Cambie el nombre del Board",boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        Intent intent = new Intent(BoardActivity.this,NoteActivity.class);
        intent.putExtra("id",boards.get(i).getId());
        startActivity(intent);
    }
}
