package com.example.ericb.demonotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import static com.example.ericb.demonotes.ObjectSerializer.serialize;
public class MainActivity extends AppCompatActivity {

    EditText noteText ;
    ListView notesListView ;
    ArrayAdapter<String> arrayAdapter ;

    public static final String EXTRA_MESSAGE = "com.example.ericb.demonotes.MESSAGE";
    static final int EDIT_NOTE_REQUEST = 1;

    int currentNote;

    SharedPreferences sharedPreferences;
    ArrayList<String> notes = new ArrayList<>();

    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.main_menu, menu );

        return super.onCreateOptionsMenu( menu );
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        switch (item.getItemId()) {
            case R.id.addNote:
                Log.i( "Eric1", "Add note selected" );

                String currentNoteText = "";
                editNoteText( currentNoteText );



                return true;
            default:
                return false;
        }
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        notesListView = findViewById( R.id.notesListView );

        sharedPreferences = this.getSharedPreferences( "com.example.ericb.demonotes", Context.MODE_PRIVATE );
        getSaveNotes();

        //create an array adapter and associate with the source (array list myFriends)
        arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, notes );

        //associate the array adapter with the list view
        notesListView.setAdapter( arrayAdapter );

        //create a click listener so you can respond to item press
        notesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i( "Eric1", notes.get( position ) + " Selected" );
                currentNote = position;
                editNoteText( notes.get( position ) );

            }
        } );

        //create a click listener so you can respond to item long press
        notesListView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                alertUserAboutDelete( position );
                return true;
            }
        } );

    }

    //----------------------------------------------------------------------------------------------
    public void alertUserAboutDelete(final int position) {


        new AlertDialog.Builder( this )
            .setIcon( android.R.drawable.ic_dialog_alert )
            .setTitle( "Delete Note?" )
            .setMessage( notes.get( position ) )
            .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                notes.remove(position);
                putSaveNotes();



//                Toast.makeText( getApplicationContext(), "Done", Toast.LENGTH_SHORT ).show();
            }
        } )
            .setNegativeButton( "No", null )
            .show();

    }
    //----------------------------------------------------------------------------------------------
    private void getSaveNotes() {
        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize( sharedPreferences.getString( "notes", serialize( new ArrayList<String>() ) ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    private void putSaveNotes() {
        try {
            sharedPreferences.edit().putString("notes",serialize(notes)).apply();
            arrayAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    private void editNoteText(String currentNoteText) {

        Intent intent = new Intent(this, EditNoteActivity.class);

        String noteText = currentNoteText;
        intent.putExtra(EXTRA_MESSAGE, noteText);
        startActivityForResult(intent, EDIT_NOTE_REQUEST);

    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == EDIT_NOTE_REQUEST) {

            if (resultCode == RESULT_OK) {
                String newNote = data.getStringExtra( "newNote" );
                Log.i("Eric1..data returned", newNote);
                if (currentNote == 0) {
                    notes.add( newNote );
                } else {
                    notes.set( currentNote, newNote );
                }

                putSaveNotes();
            }

            if (resultCode == RESULT_CANCELED) {
                //code if nothing was returned
            }
        }
    }
}
