package sg.edu.rp.c346.id19036308.ps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import androidx.core.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    int reqCode = 12345;
    EditText etName, etDesc, etRemind;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        etRemind = findViewById(R.id.etRemind);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name  = etName.getText().toString();
                String desc = etDesc.getText().toString();
                String time = etRemind.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, Integer.parseInt(time));

                DBHelper dbh = new DBHelper(AddActivity.this);
                long id =dbh.insertTask(desc, name);
                dbh.close();
                finish();

                Intent intent = new Intent(
                        AddActivity.this,
                        NotificationReceiver.class);
                intent.putExtra("id", id);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AddActivity.this,
                        reqCode,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager)
                        getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);




            }
        });

        CharSequence reply = null;
        Intent i = getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(i);
        if (remoteInput != null) {
            reply = remoteInput.getCharSequence("status2");
        }

        if (reply != null) {

            etDesc.setText(String.valueOf(reply));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}