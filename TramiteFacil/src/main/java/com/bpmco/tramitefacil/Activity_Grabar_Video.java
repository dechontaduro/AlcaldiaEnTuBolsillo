package com.bpmco.tramitefacil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Activity_Grabar_Video extends Activity implements SurfaceHolder.Callback, OnClickListener{

    // Variables globales para administrar la grabación y reproducción
    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;

    // Variable que define el nombre para el archivo donde escribiremos el video a grabar
    private String fileName = null;
    private String file = null;

    //V ariable que indica cuando se está grabado
    private boolean recording = false;

    // Variables globales botones
    private ImageView btnRec = null;
    private ImageView btnStop = null;
    private Button btnPlay = null;
    private Button btnSave = null;

    // Camara
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    SeekBar barra;
    int idcamara;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grabar_video);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        idcamara = Camera.CameraInfo.CAMERA_FACING_BACK;
        previewing = false;
        surfaceView = (SurfaceView)findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();

        // Inicializamos la variable para el nombre del archivo y la ruta donde va a quedar almacenado
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tramite Facil/Videos");
        dir.mkdirs();
        file = "Video-" + n + ".mp4";
        fileName = dir + "/" + file;

        // Inicializamos la "superficie" donde se reproducirá la vista previa de la grabación y luego el video ya grabado
        SurfaceView surface = (SurfaceView)findViewById(R.id.surface);
        SurfaceHolder holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // Inicializamos los botones
        btnRec = (ImageView)findViewById(R.id.btnRec);
        btnStop = (ImageView)findViewById(R.id.btnStop);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnSave = (Button)findViewById(R.id.btnSave);

        // Asignamos el evento click a los bontones
        btnRec.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    // Inicializamos los recursos asociados a las variables para administrar la grabación y reproducción.
    // Se verifica si las variables son nulas (para ejecutar este código solo una vez) y luego de
    // inicializarlas se coloca el SurfaceHolder como display para la vista previa de la grabación y
    // para la vista de la reproducción
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(idcamara);
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setPreviewDisplay(holder.getSurface());
        }

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(holder);
        }
    }

    // Liberamos los recursos asociados a las variables para administrar la grabación y reproducción
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        mediaRecorder.release();
        mediaPlayer.release();
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }


    // Método para preparar la grabación, configurando los atributos de la fuente para audio y video,
    // el formado y el codificador.
    public void prepareRecorder()
    {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setVideoFrameRate(60);
        mediaRecorder.setMaxFileSize(7000000); //7 Megas
        mediaRecorder.setMaxDuration(600000); //10 Minutos
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(camera != null)
            {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }

            Intent data = new Intent();
            data.putExtra("ruta", "");
            data.putExtra("nombre", "");
            setResult(RESULT_OK, data);
            super.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view)
    {
        // Boton Grabar
        if(view == btnRec)
        {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;

            // Al iniciar la grabación deshabilitamos los botones de grabar y reproducir y habilitamos el de detener
            btnRec.setEnabled(false);
            btnStop.setEnabled(true);
            btnPlay.setEnabled(false);
            btnPlay.setVisibility(View.GONE);
            btnStop.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.INVISIBLE);

            // Llamamos el método que configura el media recorder y le decimos el archivo de salida
            prepareRecorder();
            mediaRecorder.setOutputFile(fileName);
            try {
                //Una vez configurado_todo llamamos al método prepare que deja_todo listo para iniciar la grabación
                mediaRecorder.prepare();
                // Iniciamos la grabación y actualizamos el estatus de la variable recording
                mediaRecorder.start();
                recording = true;
            }
            catch (IllegalStateException e) {
            }
            catch (IOException e) {
            }
        }

        // Boton Detener
        if(view == btnStop)
        {
            // Al iniciar detener habilitamos los botones de grabar y reproducir y deshabilitamos el de detener
            btnRec.setEnabled(true);
            btnStop.setEnabled(false);
            btnPlay.setEnabled(true);
            btnPlay.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            // Si se está grabando, detenemos la grabación y reiniciamos la configuración
            // además de volver falsa la variable de estatus de grabación
            if (recording)
            {
                recording = false;
                mediaRecorder.stop();
                mediaRecorder.reset();
            }
            // Si se está reproduciendo, detenemos la reproducción y reiniciamos la configuración
            else if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

        }

        // Boton Reproducir
        if(view == btnPlay)
        {
            // Al iniciar la reproducción deshabilitamos los botones de grabar y reproducir y habilitamos el de detener
            btnRec.setEnabled(false);
            btnStop.setEnabled(true);
            btnPlay.setEnabled(false);


            // Al concluir la reproducción habilitamos los botones de grabar y reproducir y deshabilitamos el de detener
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    btnRec.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnPlay.setEnabled(true);
                }
            });

            // Configuramos el archivo a partir del cual se reproducirá, preparamos el Media Player e iniciamos la reproducción
            try {
                mediaPlayer.setDataSource(fileName);
                mediaPlayer.prepare();
            }
            catch (IllegalStateException e) {
            }
            catch (IOException e) {
            }
            mediaPlayer.start();
        }

        if(view == btnSave)
        {
            if(camera != null){
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }

            Intent data = new Intent();
            data.putExtra("ruta", fileName);
            data.putExtra("nombre", file);
            setResult(RESULT_OK, data);
            super.finish();
        }
    }

    private void startPreview(int id)
    {
        if(!previewing)
        {
            try
            {
                camera = android.hardware.Camera.open(id);
                if (camera != null)
                {
                    try
                    {
                        surfaceHolder.removeCallback(this);
                        surfaceHolder = null;
                        surfaceView = (SurfaceView)findViewById(R.id.surface);
                        surfaceHolder = surfaceView.getHolder();
                        surfaceHolder.addCallback(this);
                        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                        android.hardware.Camera.Parameters parameters = camera.getParameters();
                        parameters.setColorEffect(android.hardware.Camera.Parameters.EFFECT_NONE);
                        camera.setParameters(parameters);
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                        previewing = true;
                        idcamara = id;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(this, "La camara esta siendo usada por otra aplicación", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void almacenarVideo(byte[] datos)
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate3 = df3.format(c.getTime());

        Bitmap imagen = BitmapFactory.decodeByteArray(datos, 0, datos.length);
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Tramite Facil/Video Arreglo");
        dir.mkdirs();
        File file = new File(dir, formattedDate3 + ".mp4");
        try {
            OutputStream os = new FileOutputStream(file);
            imagen.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}