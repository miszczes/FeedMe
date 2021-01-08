package com.example.feedme;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

/**
 * Main Activity
 * Aktywność z główną mechaniką gry
 * @author Mikołaj Szczęsny 175593 EiT T1
 */

public class MainActivity extends AppCompatActivity {
    /**
     * Ilość milisekund do zakończenia partii gry
     */
    private static final long START_TIME_IN_MILLIS = 1555200000;
    //1555200000
    /**
     * tag do log.v
     */
    private static final String TAG = "MyActivity";
    /**
     * Tag do przesyłania zmiennych do aktywności pobocznej ekranu końcowego
     */
    public static final String WIN_LOST = "com.example.feedme.WIN_LOST";
    /**
     * Tag do przesyłania zmiennych do aktywności pobocznej karmienia
     */
    public static final String DIFFICULTY = "com.example.feedme.DIFFICULTY";
    /**
     * skala spadku wszystkich potrzeb zwierzątka
     */
    float skala = 0.005f;
    /**
     * początkowa wartość głodu
     */
    float glod = 100f;
    /**
     * początkowa wartość licznika zabawy
     */
    float entertainment = 100f;
    /**
     * początkowa wartość zmęczenia
     */
    float sleep = 100f;
    /**
     * początkowa wartość radości
     */
    float happy = 100f;
    /**
     * Wartość głodu po zamknięciu lub zminimalizowaniu gry
     */
    float c_glod;

   // long dinner_count;
    /**
     * Początkowa wartość posiłku
     */
    int obiad = 0;
    /**
     * text wiev z głównym licznikiem czasu
     */
    public TextView mTextViewCountDown;
    /**
     * Text View odpowiedzialne za wyświetlanie wartości liczbowych potrzeb
     */
    private TextView mGlod, mEntertainment, mSleep; //obrazek
    private ImageView pet_image, o_glod;
    private Button mButtonStartPause;
    private Button mButtonReset, mHide, mEat;
   // private Button mNakarm; //opcjonalnie
    private ProgressBar pB_g, pB_e, pB_s, pB_h;
    private TextView test, test_2; //do wyrzucenia
    private TextView obiadTextView;
    private TextView mHappyTextView;
    private CountDownTimer mCountDownTimer;
    /**
     * funkcjonalny boolean
     */
    private boolean mTimerRunning, hidden, sleeping;
    /**
     * czas pozostały po zamknięciu lub zminimalizowaniu gry
     */
    private long mTimeLeftInMillis; //funkcjonalnosc
    /**
     * mEndTime to czas z odpowiednio dodanym czasem systemowym zapisany przy kazdym rozpoczęciu timera, sto to czas systemowy zapisywany podczas zamkniecia lub zminimalizowania gry
     */
    private long mEndTime, sto; //funkcjonalnosc
    /**
     * ilość czasu która mineła od zminimalizowania lub zamknięcia gry
     */
    private long timePassed = 0; //funkcjonalnosc


    /**
     * wartość funkcjonalna
     */
    int consent = 0;

    /**
     * Metoda on Create z wszystkimi button listenerami oraz definicjami elementów ekranu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       hidden = false;



       // mNakarm = findViewById(R.id.karmDev); //opcjonalnie
        mTextViewCountDown = findViewById(R.id.text_view_countdown); //funkcjonalnosc
        mGlod = (TextView) findViewById(R.id.tvGlod); //opcjonalnie zamiana na obrazek
        mEntertainment = findViewById(R.id.entertain_num);
        mSleep = findViewById(R.id.sleep_n);

        mHappyTextView = (TextView) findViewById(R.id.happyTextView);


        obiadTextView = findViewById(R.id.obiadCount);

        pet_image = (ImageView) findViewById(R.id.pet_i);
        o_glod = (ImageView) findViewById(R.id.glod_image);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mHide  = findViewById(R.id.hide_deve);
        mEat = (Button) findViewById(R.id.eatButton);
        //invisible();


        test = findViewById(R.id.testid);
        test_2 = findViewById(R.id.test2);

        pB_g = (ProgressBar) findViewById(R.id.progressBar_glod);
        pB_e = (ProgressBar) findViewById(R.id.progessBar_e);
        pB_s = (ProgressBar) findViewById(R.id.progressBar_sleep);
        pB_h = (ProgressBar) findViewById(R.id.happyBar);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable a = (AnimationDrawable) constraintLayout.getBackground();
        //AnimationDrawable b = (AnimationDrawable) constraintLayout.setBackground(getResources().getDrawable(R.drawable.gradient2_animation));
        a.setEnterFadeDuration(2000);
        a.setExitFadeDuration(4000);
        a.start();
        //sprawdzDev();
       // startTimer();
        invisible();

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        o_glod.setOnClickListener(new View.OnClickListener() { //karmienie
            @Override
            public void onClick(View view) {

                otworzKarmienie();
            }
        });

        pet_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmianaSpanieNieSpanie();
            }
        });

        mHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sprawdzDev();

            }
        });
        mEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                karmienie(obiad);
                if((happy + obiad)>=100){
                    happy = 100;
                }else{
                    happy = happy + obiad;
                }
                obiad = 0;
                mEat.setVisibility(View.INVISIBLE);
                obiadTextView.setVisibility(View.INVISIBLE);

            }
        });
    }

    /**
     * Metoda odpowiedzialna za znikanie klawisy startu i restartu podczas inicjalizacji gry
     */
    public void invisible(){
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.INVISIBLE);
    }

    /**
     * metoda odpowiedzialna za uruchomienie pobocznej aktywności FeedActivity
     */

    private void otworzKarmienie() {

        if (happy > 70){
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra(DIFFICULTY, 7);
            startActivityForResult(intent, 1);
        } else if(happy <= 70 && happy > 30){
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra(DIFFICULTY, 3);
            startActivityForResult(intent, 1);
        } else if (happy <=30){
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra(DIFFICULTY, 2);
            startActivityForResult(intent, 1);
        }

    }

    /**
     * Metoda wykonywana podczas zakończenia pobocznych activity
     * // po każdym poprawnym zakonczeniu aktywności feed activity metoda dodaje do zmiennej obiad ilośc punktów zdobytych w aktywnosci
     * @param requestCode Kod z którym uruchamiamy poboczą aktywność który jest po jej zakończeniu wysyłany do tej metody; 1 - FeedActivity; 2 - ReplayActivity
     * @param resultCode  Kod wysyłany do metody przez kończącą prace aktywność poboczną
     * @param data  Zmienna typu intent pozwalająca przesłać dane z pobocznych aktywności do głownej aktywności
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Toast.makeText(MainActivity.this, "Otrzymałeś " + obiad +  "posiłków!",
                        Toast.LENGTH_SHORT).show();
                // po każdym poprawnym zakonczeniu aktywności feed activity metoda dodaje do zmiennej obiad ilośc punktów zdobytych w aktywnosci
                obiad = obiad + data.getIntExtra("punkty", 0);
                Log.v(TAG, "obiad: " + obiad);
                //karmienie(obiad);
                if(obiad > 0){
                    mEat.setVisibility(View.VISIBLE);
                    obiadTextView.setVisibility(View.VISIBLE);
                }
                String obiadCountFormatted = String.format(Locale.getDefault(), "%02d", obiad);
                obiadTextView.setText(obiadCountFormatted);

            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                consent = data.getIntExtra("con", 0);
                if(data.getIntExtra("con", 0) == 1){
                    pauseTimer();
                    mButtonReset.setVisibility(View.VISIBLE);
                    mButtonStartPause.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * Metoda wykonywana po kliknięciu przycisku nakarmienia
     * Sprawdza czy po nakarmieniu wartość licznika głodu i zabawy nie przepełni się
     * @param obiad parametr wielkości jednorazowego nakarmienia o wartości zebrane w minigrze
     * glod - zmienna wartości nakarmienia
     * entertainment - zmienna wartości zabawy
     */
    private void karmienie(int obiad){
        if(glod + obiad > 100){
            Toast.makeText(MainActivity.this, "Piesek Jest Pełen!",
                    Toast.LENGTH_SHORT).show();
            glod = 100f;
        }else if(glod < 100) {
            glod = glod + obiad;

        }
        if((entertainment + (float) obiad/3) > 100){
            entertainment = 100f;
            glod = 100f;
        }else if(entertainment < 100){
            entertainment = entertainment + (float) obiad/3;
        }
        updateCountDownText();
    }

    /**
     * Metoda sprawdzająca czy wartości liczbowe są pokazane lub nie
     */
    private void sprawdzDev(){
        if(hidden == true) {
            UkryjOpcjeDeweloperskie();
        }else {
            PokazOpcjeDeweloperskie();
        }
    }

    /**
     * Metoda chowająca wszystkie wartości liczbowe odnoszące się do wszystkich liczników
     * oraz 2 wartości testowe
     */
    public void UkryjOpcjeDeweloperskie(){
        mTextViewCountDown.setVisibility(View.INVISIBLE);
        test.setVisibility(View.INVISIBLE);
        test_2.setVisibility(View.INVISIBLE);
        mGlod.setVisibility(View.INVISIBLE);
        mEntertainment.setVisibility(View.INVISIBLE);
        mSleep.setVisibility(View.INVISIBLE);
        mHappyTextView.setVisibility(View.INVISIBLE);
        hidden = false;
        mHide.setText("pokaż liczby");
    }
    /**
     * Metoda pokazująca wszystkie wartości liczbowe odnoszące się do wszystkich liczników
     * oraz 2 wartości testowe
     */
    private void PokazOpcjeDeweloperskie(){
        mTextViewCountDown.setVisibility(View.VISIBLE);
        test.setVisibility(View.VISIBLE);
        test_2.setVisibility(View.VISIBLE);
        mGlod.setVisibility(View.VISIBLE);
        mEntertainment.setVisibility(View.VISIBLE);
        mSleep.setVisibility(View.VISIBLE);
        mHappyTextView.setVisibility(View.VISIBLE);
        hidden = true;
        mHide.setText("ukryj liczby");
    }

    /**
     * Zmniejszanie, i lub powiększanie poszczególych parametrów podczas działania timera
     * skala - paramter o ktory zmniejszane lub zwiększane są pozostałe parametry
     * glod - parametr głodu zmniejszany podczas działania timera
     * entertainment - parametr zabawy zmiejszany podczas działania timera
     * sleeping -  parametr sprawdzający czy zwierzątko śpi
     * sleep - jeśli śpi ten parametr maleje wraz ze skalą; jeśli nie śpi parametr rośnie wraz ze skalą
     * pet_image - zmienna odpowiedzialna za ustawienie nieśpiącego obrazka pieska
     * happy -  parametr zmiejszający się szybciej wraz ze skalą oraz im mniejsze są pozostałe parametry wraz z działaniem timera
     */
    public void zasady(){
        if(glod - skala <= 0){
            // w razieskonczenia sie glodu
        }else glod = glod - skala;
        if(entertainment - (skala/2) <= 0){
            // w razie skonczenia sie entertainmentu
        }else entertainment = entertainment - (skala/2);
        if(sleeping == false){
            if(sleep - (skala) <= 0){
                //w razie skonczonego sleepu
            }else sleep = sleep - skala;
        }else {
            if(sleep + (skala*0.5f) >= 100){
                sleeping = false;
                pet_image.setImageDrawable(getDrawable(R.drawable.dog_standing2));
                updateCountDownText();
            }else sleep = sleep + skala * 0.5f;
        }
        happy = happy - skala * (((100f - glod)/100)+((100f - entertainment)/100)+((100f - sleep)/100));
    }

    /**
     * Metoda odpowiedzialna za start i/lub pauze countdown timera o który opiera się cała gra
     *
     * Postęp timera zapisywany jest w zmiennej mTimeLeftInMillis co każdy tick timera
     * co każdy tick również updatowane są wszystkie liczby oraz wykonywana jest metoda
     * zasady()
     * W momecie gdy timer się skończy otwierana jest aktywnośc ReplayActivity do której przesyłana jest zmienna 1 odpowiedzialna za pozytywne zakończenie w ReplayActivity
     */
    private void startTimer() {
        mButtonStartPause.setVisibility(View.INVISIBLE);
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
       // glodOnStart = (int) (System.currentTimeMillis() + glod);
        //timePassed = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                zasady();
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //updateButtons();
               // mTimerRunning =false;
                Intent intentReplay = new Intent(MainActivity.this, ReplayActivity.class);
                intentReplay.putExtra(WIN_LOST, 1);
               startActivityForResult(intentReplay, 2);
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    /**
     *  Metoda odpowiedzialna za pauzowanie timera
     */
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    /**
     *  Metoda odpowiedzialna za reset timera oraz wszystkich istotnych zmiennych do stanu z początku gry oraz odświeżenie wyświetlaczy
     */
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        glod = 100;
        entertainment = 100;
        sleep = 100;
        happy = 100;
        //dinner_count = 0;
       // obiad = 0;
        consent = 0;
        mButtonStartPause.setVisibility(View.VISIBLE);

        zmianaSpanieNieSpanie();
        updateCountDownText();
        updateButtons();
    }

    /**
     *  Metoda odpowiedzialna za zmiane wizerunku psa z nieśpiącego na śpiącego i vice wersa
     */
    private void zmianaSpanieNieSpanie() {
        if(sleeping == true && mTimerRunning == true) {
            pet_image.setImageDrawable(getDrawable(R.drawable.dog_standing2));
            sleeping = false; // obudzenie
        }else if(sleeping == false && mTimerRunning == true) {
            pet_image.setImageDrawable(getDrawable(R.drawable.dog_sleeping));
            sleeping = true; // zasniecie
        }
    }

    /**
     *  Metoda odpowiedzialna za transwer wszystkich zmiennych liczbowych do typu String i wyświetlenie ich w odpowiednim TextView
     */
    private void updateCountDownText() {
        int time = (int) (mTimeLeftInMillis / 1000);

       // int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", time);
        String glodmeter = String.format(Locale.getDefault(), "%02f", glod);
        String entermeter = String.format(Locale.getDefault(), "%02f", entertainment);
        String sleepmeter = String.format(Locale.getDefault(), "%02f", sleep);
        String testt = String.format(Locale.getDefault(), "%05f", c_glod);
        String test2 = String.format(Locale.getDefault(), "%05d", timePassed);
        String happymeter = String.format(Locale.getDefault(), "%03f", happy);

        mTextViewCountDown.setText(timeLeftFormatted);
        mGlod.setText(glodmeter);
        mEntertainment.setText(entermeter);
        mSleep.setText(sleepmeter);
        mHappyTextView.setText(happymeter);
        test.setText(testt);
        test_2.setText((test2));
        pB_g.setProgress((int) glod);
        pB_e.setProgress((int) entertainment);
        pB_s.setProgress((int) sleep);
        pB_h.setProgress((int) happy);
        //if(glod == 1)
    }

    /**
     *  Metoda Używana do odpowiedniego wyświetlenia przycisków. W większości używana była podczas etapu developmentu na zmiane przycisku na pauza żeby nie dodawać niepotrzebnych przycisków
     */
    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pauza");
        } else {
            mButtonStartPause.setText("ZACZNIJ GRE");

           /* if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
               // mButtonStartPause.setVisibility(View.VISIBLE);
            }*/

            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Metoda zotaje wykonana po zminimalizowaniu lub zatrzymaniu gry
     * Wszystkie istotne do działania zmienne zostają zapisane w odpowiedni sposob w editorze
     * sto - czas systemowy
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        sto = System.currentTimeMillis();
        c_glod = glod;
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putFloat("happi", happy);
        editor.putLong("endTime", mEndTime);
        editor.putLong("stoptime", sto);
        //editor.putBoolean("ukryte", hidden);
        editor.putBoolean("spi", sleeping);
        editor.putFloat("closeglod", c_glod);
        editor.putFloat("closeenter", entertainment);
        editor.putFloat("closesleep", sleep);
      //  editor.putInt("obiadGra", obiad);

        //Log.v(TAG, "c_hidden: " + hidden);

        editor.apply();
        //obiad = 0;
       if (mCountDownTimer != null) {
          mCountDownTimer.cancel();
       }
    }

    /**
     * Metoda zostaje wykonana przy każdym uruchomieniu aplikacji, następuje tu odczyt zmiennych z editora i odpowiedni ich odpowiedni processing z zasadami z metody
     * zasady() zmienionej o czas wyłaczenia lub zminimalizowania aplikacji timePassed utowrzonej ze zmiennej sto
     * W tej metodzie jest sprawdzany również warunek jeżeli timePassed przekroczy ilość 3 dni gra kończy się porażką
     * W tej metodzie jest losowany również losowy wygląd pieska
     */
    @Override
    protected void onStart() {
        super.onStart();
        UkryjOpcjeDeweloperskie();
        //startTimer();

        //hidden = true;
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        long stoptime = prefs.getLong("stoptime", 0);
        boolean c_sleeping = prefs.getBoolean("spi", false);
        c_glod = prefs.getFloat("closeglod", 100);
        float c_entertainment = prefs.getFloat("closeenter", 100);
        float c_sleep = prefs.getFloat("closesleep", 100);
       // boolean c_hidden = prefs.getBoolean("ukryte", true);
        float c_happy = prefs.getFloat("happi", 100);

       // int c_obiad = prefs.getInt("obiadGra", 0);

      //  Log.v(TAG, "obiad: " + c_obiad);
       // glod = glod + c_obiad;

        //dinner_count = 0;
        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {

            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            //glod = (((START_TIME_IN_MILLIS - mTimeLeftInMillis) * 2) + mTimeLeftInMillis)/1000;
            timePassed = (System.currentTimeMillis() - stoptime)/1000;
            if(glod > 1) { // glod
                if ((c_glod) - ((timePassed) * skala) < 0){
                    glod = 1;
                }else glod = (c_glod/*+(dinner_count*obiad)*/) - ((timePassed) * skala);

            }
            if(entertainment > 1){ // zabawa
                if ((c_entertainment) - ((timePassed) * (skala / 2)) < 0){
                    entertainment = 1;
                } else entertainment = (c_entertainment) - ((timePassed) * (skala / 2));
            }
            if (c_sleeping == true){                                            // sen
                if(sleep > 1){                                                  //
                   if((c_sleep) + ((timePassed) * (skala * 0.5f))> 100){        //
                       sleep = 100;                                             //
                   } else  sleep = (c_sleep) + ((timePassed) * (skala * 0.5f)); //
                }                                                               //
                                                                                //
            } else {                                                            //
                if(sleep > 1){                                                  //
                    if((c_sleep) - ((timePassed) * (skala)) < 0){               //
                        sleep = 1;                                              //
                    } else sleep = (c_sleep) - ((timePassed) * (skala));        //
                }                                                               //
            }
            happy = c_happy - (skala * (((100f - glod)/100)+((100f - entertainment)/100)+((100f - sleep)/100))*timePassed);
            sleeping = c_sleeping;
            //hidden = c_hidden;
            Log.v(TAG, "c_hidden: " + hidden);
            //sprawdzDev();
           // dinner_count = 0;
            stoptime = 0;
           // timePassed = 0;
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
            if (timePassed > 110){
               // pauseTimer();

                mTimerRunning =false;
                Intent intentReplay = new Intent(this, ReplayActivity.class);
                intentReplay.putExtra(WIN_LOST, 0);
                startActivityForResult(intentReplay, 2);
            }
        }else if (consent == 0) {
            startTimer();
        }
        if(c_sleeping == false){
            Random rand = new Random();
            int pose = rand.nextInt(4);
            switch(pose){
                case 0:
                    pet_image.setImageDrawable(getDrawable(R.drawable.dog_standing2));
                    break;
                case 1:
                    pet_image.setImageDrawable(getDrawable(R.drawable.dog_stading3));
                    break;
                case 2:
                    pet_image.setImageDrawable(getDrawable(R.drawable.dog_laying));
                    break;
                case 3:
                    pet_image.setImageDrawable(getDrawable(R.drawable.dog_standing1));
                    break;
            }

        }else {
            pet_image.setImageDrawable(getDrawable(R.drawable.dog_sleeping));
        }



    }


}