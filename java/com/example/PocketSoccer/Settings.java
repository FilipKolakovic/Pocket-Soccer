package com.example.kf150605d.pocketsoccer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {


    protected int uslovZavrsetka = 1; // uslov zavrsetka default;

    protected int brzinaIgre = 30; // default

    protected String teren = "Trava";

    public static final String MY_PREFS_NAME = "myPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

         RadioGroup zavrsetakRG = findViewById(R.id.radiogroupZavrsetak);   // mozda van onCreate() treba da se stavi
         RadioGroup brzinaRG = findViewById(R.id.radiogroupBrzina);         // mozda van onCreate() treba da se stavi
         Spinner terenSpinner = (Spinner) findViewById(R.id.terenSpinner);  // mozda van onCreate


        zavrsetakRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.goloviRadioB:
                        uslovZavrsetka = 1;
                        break;
                    case R.id.vremeRadioB:
                        uslovZavrsetka = 0;
                        break;
                }
            }
        });

        brzinaRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.slowRadioB:
                        brzinaIgre = 10;   // ove brzine promeniti posle !!!!!
                        break;
                    case R.id.mediumRadioB:
                        brzinaIgre = 30;   // ove brzine promeniti posle !!!!!
                        break;
                    case R.id.fastRadioB:
                        brzinaIgre = 50;   // ove brzine promeniti posle !!!!!
                        break;
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tereni, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        terenSpinner.setAdapter(adapter);

        terenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teren = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void potvrdi(View view) {
        Intent intent = new Intent();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putInt("uslovZavrsetka",uslovZavrsetka);
        editor.putInt("brzinaIgre",brzinaIgre);
        editor.putString("teren", teren);
        editor.apply();

        setResult(RESULT_OK,intent);
        finish();
    }
}
