package com.example.titancalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {


    Button btn_save;
    ListView lv_verlauf;
    String item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verlauf);

        btn_save = findViewById(R.id.btn_set);
        lv_verlauf = findViewById(R.id.lv_verlauf);


        Intent myIntent = getIntent(); // gets the previously created intent
        String[] arrayVerlauf = myIntent.getStringArrayExtra("verlauf");
        //Toast.makeText(HistoryActivity.this, Arrays.toString(arrayVerlauf), Toast.LENGTH_LONG).show();

        ArrayAdapter adapter_verlauf = new ArrayAdapter<String>(this, R.layout.lvitem_layout, arrayVerlauf);
        lv_verlauf.setAdapter(adapter_verlauf);

        lv_verlauf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                item = lv_verlauf.getItemAtPosition(position).toString();
            }

        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("RESULT_STRING", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
