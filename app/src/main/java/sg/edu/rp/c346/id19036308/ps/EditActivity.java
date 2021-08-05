package sg.edu.rp.c346.id19036308.ps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    EditText etDesc, etName;
    Button btnUpdate, btnDelete, btnCancel;
    Task data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //initialise the variables with UI here
        etDesc = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel2);
        etName = findViewById(R.id.etName2);

        Intent i = getIntent();
        data = (Task) i.getSerializableExtra("data");

        etName.setText(data.getName());
        etDesc.setText(data.getDescription());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditActivity.this);
                data.setName(etName.getText().toString());
                data.setDescription(etDesc.getText().toString());
                dbh.updateNote(data);
                setResult( RESULT_OK, i);
                dbh.close();
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditActivity.this);
                dbh.deleteNote(data.getId());
                setResult( RESULT_OK, i);
                dbh.close();
                finish();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}