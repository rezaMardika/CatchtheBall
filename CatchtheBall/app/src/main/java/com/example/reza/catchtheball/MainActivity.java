package com.example.reza.catchtheball;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView textViewScore, textViewStart;
    private ImageView imageBox, imageYellow, imageBlack, imagePink;

    //posisi box
    private int boxY;
    private int yellowX, yellowY, pinkX, pinkY, blackX, blackY;

    //inisialisasi class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SuaraPemain suaraPemain;

    //cek status
    private boolean action_flg = false;
    private boolean start_flg = false;

    //ukuran
    private int framesize;
    private int boxSize;

    private int screenWidth, screenHeight;
    private int score = 0;

    //kecepatan
    private int boxSpeed;
    private int yellowSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        suaraPemain = new SuaraPemain(this);

        textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewStart = (TextView) findViewById(R.id.textViewStart);
        imageBox = (ImageView) findViewById(R.id.imageBox);
        imageBlack = (ImageView) findViewById(R.id.imageBlack);
        imageYellow = (ImageView) findViewById(R.id.imageYellow);
        imagePink = (ImageView) findViewById(R.id.imagePink);

        //get screen size
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //speed box=20, yellow=12, pink=20, black=16
        boxSpeed = Math.round(screenHeight / 60F);
        yellowSpeed = Math.round(screenWidth / 60F);
        pinkSpeed = Math.round(screenWidth / 36F);
        blackSpeed = Math.round(screenWidth / 45F);

        Log.v("kecepatan box", boxSpeed+"");
        Log.v("kecepatan kuning", yellowSpeed+"");
        Log.v("kecepatan pink", pinkSpeed+"");
        Log.v("kecepatan hitam", blackSpeed+"");


        //Efek gerak pada layar
        imageYellow.setX(-80);
        imageYellow.setY(-80);
        imageBlack.setX(-80);
        imageBlack.setY(-80);
        imagePink.setX(-80);
        imagePink.setY(-80);

        textViewScore.setText("Score : 0");

    }

    public void changePost(){

        hitCheck();

        //yellow
        yellowX -= yellowSpeed;
        if (yellowX <0){
            yellowX = screenWidth+20;
            yellowY = (int) Math.floor(Math.random() * (framesize - imageYellow.getHeight()));
        }
        imageYellow.setX(yellowX);
        imageYellow.setY(yellowY);

        //black
        blackX -= blackSpeed;
        if (blackX < 0){
            blackX = screenWidth +10;
            blackY = (int) Math.floor(Math.random() * (framesize - imageBlack.getHeight()));
        }
        imageBlack.setX(blackX);
        imageBlack.setY(blackY);

        //pink
        pinkX -=pinkSpeed;
        if (pinkX < 0){
            pinkX = screenWidth +5000;
            pinkY = (int) Math.floor(Math.random() * (framesize - imagePink.getHeight()));
        }
        imagePink.setX(pinkX);
        imagePink.setY(pinkY);

        //pergerakan box
        if (action_flg == true){
            boxY -=boxSpeed;
        }else {
            boxY +=boxSpeed;
        }

        //cek posisi box
        if (boxY <0)
            boxY = 0;

        if (boxY > framesize - boxSize)
            boxY = framesize - boxSize;
        imageBox.setY(boxY);

        textViewScore.setText("Score : "+score);
    }

    public void hitCheck(){
        int yellowCenterX = yellowX + imageYellow.getWidth()/2;
        int yellowCenterY = yellowY + imageYellow.getHeight()/2;

        if (0 <= yellowCenterX && yellowCenterX <= boxSize && boxY <= yellowCenterY && yellowCenterY <=boxY + boxSize){
            score += 10;
            yellowX = -10;

            suaraPemain.playHitSound();
        }

        //Pink
        int pinkCenterX = pinkX + imagePink.getWidth()/2;
        int pinkCenterY = pinkY + imagePink.getHeight()/2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <=boxY + boxSize){
            score += 30;
            pinkX = -10;

            suaraPemain.playHitSound();
        }

        //Black
        int blackCenterX = blackX + imageBlack.getWidth()/2;
        int blackCenterY = blackY + imageBlack.getHeight()/2;

        if (0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <=boxY + boxSize){
            //stop timer
            timer.cancel();
            timer = null;

            suaraPemain.playOverSound();

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }

    //pergerakan box naik dan turun ketika disentuh (tap)
    public boolean onTouchEvent(MotionEvent event){

        if (start_flg == false){
            start_flg = true;

            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayoutGame);
            framesize = frameLayout.getHeight();

            boxY = (int) imageBox.getY();

            boxSize = imageBox.getHeight();

            textViewStart.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePost();
                        }
                    });
                }
            }, 0, 20); //menjalankan method change post setiap 20 milisecond
        }else {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }


        //imageBox.setY(boxY);

        return true;
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
