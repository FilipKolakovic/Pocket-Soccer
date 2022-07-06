package com.example.kf150605d.pocketsoccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import static com.example.kf150605d.pocketsoccer.Settings.MY_PREFS_NAME;

public class TeamSelect extends AppCompatActivity {

    String tim1 ="";
    String tim2 ="";

    ImageView tim1Slika;
    ImageView tim2Slika;
    Spinner spinner1;
    Spinner spinner2;
    EditText pName1;
    EditText pName2;
    RadioButton radioButtonDa;
    RadioButton radioButtonNe;
    private boolean isBot = false;

    String player1Name;
    String player2Name;

    ImageResources imageResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_select);

        spinner1 = (Spinner) findViewById(R.id.spinnerTim1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamSelect.this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.timovi));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        tim1Slika = (ImageView) findViewById(R.id.slika1);
        tim1 =  spinner1.getSelectedItem().toString();
        imageResources = new ImageResources(getApplicationContext());
        radioButtonDa = findViewById(R.id.daRB);
        radioButtonDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBot = true;
            }
        });

        radioButtonNe = findViewById(R.id.neRB);
        radioButtonNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBot = false;
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                tim1 = parent.getItemAtPosition(position).toString();
                tim1Slika.setImageDrawable(imageResources.getPictureOfTeam(tim1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinner2 = (Spinner) findViewById(R.id.spinnerTim2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TeamSelect.this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.timovi));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        tim2Slika = (ImageView) findViewById(R.id.slika2);
        tim2 =  spinner2.getSelectedItem().toString();

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                tim2 = parent.getItemAtPosition(position).toString();
                tim2Slika.setImageDrawable(imageResources.getPictureOfTeam(tim2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        pName1 = (EditText) findViewById(R.id.player_1_name);
        pName2 = (EditText) findViewById(R.id.player_2_name);
    }

    public void pocniIgru(View view) {
        Intent intent = new Intent();
        player1Name = pName1.getText().toString();
        player2Name = pName2.getText().toString();

        SharedPreferences.Editor pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        pref.putString("tim1", tim1);
        pref.putString("tim2", tim2);
        pref.putString("player1Name", player1Name);
        pref.putString("player2Name", player2Name);
        pref.putBoolean("isBot", isBot);
        pref.apply();
        setResult(RESULT_OK, intent);
        finish();
    }
}
