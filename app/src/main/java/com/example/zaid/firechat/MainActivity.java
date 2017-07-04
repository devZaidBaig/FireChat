package com.example.zaid.firechat;

import android.content.Intent;
import android.text.format.DateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Messages> firebaseListAdapter;
    RelativeLayout activity_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout)findViewById(R.id.chat_layout);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else{
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            DisplayChat();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.messenger_send_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new Messages(editText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                editText.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Snackbar.make(activity_main,"Successfully Signed in...",Snackbar.LENGTH_SHORT).show();
                DisplayChat();
            }
            else{
                Snackbar.make(activity_main,"Couldn't Sign in..",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void DisplayChat(){
        ListView listView = (ListView)findViewById(R.id.list_items);
        firebaseListAdapter = new FirebaseListAdapter<Messages>(this,Messages.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Messages model, int position) {
                TextView messageText,messageTime,messageUser;
                messageText = (TextView)v.findViewById(R.id.message_text);
                messageTime = (TextView)v.findViewById(R.id.time);
                messageUser = (TextView)v.findViewById(R.id.sender_text);

                messageUser.setText(model.getClient());
                messageText.setText(model.getUser());
                messageTime.setText(DateFormat.format("DD:MM:YYYY (HH:mm:ss)",model.getTime()));
            }
        };
        listView.setAdapter(firebaseListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main,"Sign out successful...",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        return true;
    }
}
