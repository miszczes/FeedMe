package com.example.feedme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReplayActivity extends AppCompatActivity {

    private Button rep;
    int consent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        rep = (Button) findViewById(R.id.buttonReplay);
        TextView w_l;
        w_l = (TextView) findViewById(R.id.win_los);

        Intent intent =getIntent();
        int winLost = intent.getIntExtra(MainActivity.WIN_LOST, 0);

        if(winLost == 0){
            w_l.setText("Piesek uciekł ponieważ nie opiekowałeś się nim :(");
        }
        else if (winLost == 1){

            w_l.setText("Wygrałeś! Piesek dorósł!");
        }

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