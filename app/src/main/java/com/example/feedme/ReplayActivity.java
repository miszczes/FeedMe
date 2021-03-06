package com.example.feedme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Replay Acitvity
 * Aktywność z ekranem końca gry
 * @author Mikołaj Szczęsny 175593 EiT T1
 */

public class ReplayActivity extends AppCompatActivity {
    /**
     * przycisk powtórzenia gry
     */
    private Button rep;
    /**
     * zmienna z pewną funkcjonalnością niewykorzystana
     */
    int consent;

    /**
     * Na ekranie pokazuje się napis w zależnosci pozytywnego lub negatywnego zakończenia gry
     * z głownej aktywnosci przesyłana jest liczba w zależności od zakończenia i tutaj w tej zależnosci
     * wyświetla się odpowiedni  oraz wyświetla się ilość zagrań w minigrę FeedActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        rep = (Button) findViewById(R.id.buttonReplay);
        TextView w_l;
        w_l = (TextView) findViewById(R.id.win_los);
        TextView i_obiad;
        i_obiad = (TextView) findViewById(R.id.dinner_count);

        Intent intent =getIntent();
        int winLost = intent.getIntExtra(MainActivity.WIN_LOST, 0);
      //  int obiadCount = intent.getIntExtra(MainActivity.OBIADY, 0);

        if(winLost == 0){
            w_l.setText("Piesek uciekł ponieważ nie opiekowałeś się nim :(");
        }
        else if (winLost == 1){

            w_l.setText("Wygrałeś! Piesek dorósł!");
        }
     //   String obiadC = String.format(Locale.getDefault(), "%03f", obiadCount);

     //   i_obiad.setText(obiadC);

        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consent = 1;
                setResult(FeedActivity.RESULT_OK,
                        new Intent().putExtra("con", consent));
                finish();
            }
        });
    }
}