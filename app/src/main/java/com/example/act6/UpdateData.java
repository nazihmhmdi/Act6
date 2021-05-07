package com.example.act6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.act6.database.DBController;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class UpdateData extends AppCompatActivity {
    private TextInputEditText tNama, tTelpon, tId;
    private Button btnUpdate;
    String nm,tlp,id;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        tId = (TextInputEditText) findViewById(R.id.tietId);
        tNama = (TextInputEditText) findViewById(R.id.tietNama);
        tTelpon = (TextInputEditText) findViewById(R.id.tietTelpon);
        btnUpdate = (Button) findViewById(R.id.buttonUpdate);

        id = getIntent().getStringExtra("id");
        nm = getIntent().getStringExtra("nama");
        tlp = getIntent().getStringExtra("telpon");

        tId.setText(id);
        tNama.setText(nm);
        tTelpon.setText(tlp);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tId.getText().toString().equals("") || tNama.getText().toString().equals("") || tTelpon.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Data tidak boleh kosong !", Toast.LENGTH_LONG).show();
                } else {
                    id = tId.getText().toString();
                    nm = tNama.getText().toString();
                    tlp = tTelpon.getText().toString();

                    HashMap<String,String> qvalues = new HashMap<>();
                    qvalues.put("id", id);
                    qvalues.put("nama", nm);
                    qvalues.put("telpon", tlp);

                    controller.updateData(qvalues);
                    Toast.makeText(getApplicationContext(),"Data telah diupdate !", Toast.LENGTH_LONG).show();
                    callHome();
                }
            }
        });
    }

    public void callHome() {
        Intent intent = new Intent(UpdateData.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}