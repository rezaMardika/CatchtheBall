package com.example.reza.catchtheball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class StartActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //buat interistial
        interstitialAd = new InterstitialAd(this);

        //set unit id
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        //buat request
        AdRequest adRequest = new AdRequest.Builder().build();

        //star loading
        interstitialAd.loadAd(adRequest);

        //pada saat requaest dimulai, ad akan tampil
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                displayInterstitial();
            }
        });
    }

    public void displayInterstitial(){
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    public void startGame(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //Pengaturan tombol back hp agar tidak kembali ke game
   // @Override
    public boolean dispacthKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
