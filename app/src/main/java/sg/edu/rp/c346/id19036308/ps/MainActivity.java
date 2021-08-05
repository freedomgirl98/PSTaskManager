package sg.edu.rp.c346.id19036308.ps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.RemoteInput;

import android.content.Intent;
import android.database.Cursor;

import androidx.core.app.RemoteInput;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnTask;
    ListView lv;
    ArrayList<Task> altasks;
    ArrayAdapter<Task> aaTasks;
    DBHelper dbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DBHelper dbTask = new DBHelper(MainActivity.this);
        btnTask = findViewById(R.id.btnTask);
        lv = findViewById(R.id.lv);
        altasks = new ArrayList<Task>();


        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Task target = altasks.get(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                startActivityForResult(i, 9);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        dbTask = new DBHelper(MainActivity.this);
//        altasks.clear();


        CharSequence reply = null;
        Intent i = getIntent();
        long id = i.getLongExtra("id", 0);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(i);
        if (remoteInput != null) {
            reply = remoteInput.getCharSequence("status");
        }

        if (reply != null && reply.toString().equalsIgnoreCase("Completed")) {

            Toast.makeText(MainActivity.this, reply, Toast.LENGTH_SHORT).show();

            DBHelper dbh = new DBHelper(MainActivity.this);
            dbh.deleteNote(Integer.parseInt("" + id));
            setResult(RESULT_OK, i);
            dbh.close();
//            finish();

        }

        altasks = dbTask.getTasks();
        aaTasks = new ArrayAdapter<Task>(MainActivity.this, android.R.layout.simple_list_item_1, altasks);


    }
}