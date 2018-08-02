package com.example.reza.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView labelScore, labelHighScore;
    private Button buttonUlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        labelScore = (TextView) findViewById(R.id.labelScore);
        labelHighScore = (TextView) findViewById(R.id.labelHighScore);

        int score = getIntent().getIntExtra("SCORE",0);
        labelScore.setText(score+"");

        SharedPreferences settings = getSharedPreferences("GAME DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE : ", 0);

        if (score > highScore){
            labelHighScore.setText("High Score : "+score);

            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else {
            labelHighScore.setText("High Score : "+highScore);
        }
    }

    public void ulang(View view){
        finish();
        startActivity(new Intent(getApplicationContext(), StartActivity.class));
    }

    //Pengaturan tombol back hp agar tidak kembali ke game
    //@Override
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
