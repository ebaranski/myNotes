package com.example.ericb.demonotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {

    EditText noteText;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_note );

        noteText = findViewById( R.id.noteText );

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra( MainActivity.EXTRA_MESSAGE );

        // Capture the layout's TextView and set the string as its text
        EditText notesListView = findViewById(R.id.noteText);
        notesListView.setText(message);
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("Eric1..edit note key selected", "home");
                String newNote = noteText.getText().toString();

                Intent backToMainActivity = new Intent();
                backToMainActivity.putExtra("newNote", newNote);
                setResult( RESULT_OK, backToMainActivity );
                finish();
                return true;
            default:

                return super.onOptionsItemSelected( item );
        }
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown( keyCode, event );

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("Eric1..edit note key selected", "back");
            String newNote = noteText.getText().toString();

            Intent backToMainActivity = new Intent();
            backToMainActivity.putExtra("newNote", newNote);
            setResult( RESULT_OK, backToMainActivity );
            finish();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }
}
