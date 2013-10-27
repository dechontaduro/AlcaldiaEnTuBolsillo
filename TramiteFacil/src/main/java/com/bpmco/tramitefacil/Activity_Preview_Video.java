package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Activity_Preview_Video extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);

        Bundle bundle = getIntent().getExtras();

        String ruta = bundle.getString("ruta");
        String[] arrRuta = ruta.split("/");

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(arrRuta[arrRuta.length - 1]);

        VideoView video = (VideoView)findViewById(R.id.prev_video);
        video.setVideoPath(ruta);
        video.setMediaController(new MediaController(this));

        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) video.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        video.setLayoutParams(params);

        video.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__preview__video, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras){
            finish();
        }

        if(view == btnHome){
            Intent i = new Intent(Activity_Preview_Video.this, Activity_listadoPqr.class);
            startActivity(i);
        }
    }
}
