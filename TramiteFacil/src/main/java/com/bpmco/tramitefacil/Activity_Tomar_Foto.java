package com.bpmco.tramitefacil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


public class Activity_Tomar_Foto extends Activity implements SurfaceHolder.Callback, OnClickListener {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    SeekBar barra;
    Button btnguardar = null;
    Button btncancelar = null;
    RelativeLayout layoutbotones = null;
    byte[] datos;
    int idcamara;

//    private static boolean flash;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tomarfoto);
        btnguardar = (Button)findViewById(R.id.btnguardarfoto);
        btncancelar = (Button)findViewById(R.id.btncancelarfoto);
        layoutbotones = (RelativeLayout)findViewById(R.id.layoutbotones);

        btnguardar.setOnClickListener(this);
        btncancelar.setOnClickListener(this);
        idcamara = Camera.CameraInfo.CAMERA_FACING_BACK;

//		flash = false;
        try
        {
            getWindow().setFormat(PixelFormat.UNKNOWN);
            surfaceView = (SurfaceView)findViewById(R.id.svcamara);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            barra = (SeekBar)findViewById(R.id.sbzoom);
            ImageView botonimagen = (ImageView)findViewById(R.id.imgtakephoto);
            ImageView botonzoom = (ImageView)findViewById(R.id.imgzoom);
            ImageView botoncambiar = (ImageView)findViewById(R.id.imgcambiar);

            botoncambiar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(Camera.getNumberOfCameras() > 1)
                    {
                        camera.stopPreview();
                        camera.setPreviewCallback(null);
                        camera.release();
                        camera = null;
                        previewing = false;
                        if(idcamara == Camera.CameraInfo.CAMERA_FACING_BACK)
                            startPreview(Camera.CameraInfo.CAMERA_FACING_FRONT);
                        else
                            startPreview(Camera.CameraInfo.CAMERA_FACING_BACK);
                    }
                }
            });

            botonimagen.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    camera.takePicture(null, null, mPicture);
                }
            });

            botonzoom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(barra.getVisibility() == View.VISIBLE)
                        barra.setVisibility(View.GONE);
                    else
                        barra.setVisibility(View.VISIBLE);
                }
            });

            barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setZoom(progress);
                    camera.setParameters(parameters);
                }
            });
        }
        catch(NullPointerException ex)
        {

        }
        catch(Exception ex)
        {

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



    private void startPreview(int id)
    {
        if(!previewing)
        {
            try
            {
                camera = Camera.open(id);
                if (camera != null)
                {
                    try
                    {
                        surfaceHolder.removeCallback(this);
                        surfaceHolder = null;
                        surfaceView = (SurfaceView)findViewById(R.id.svcamara);
                        surfaceHolder = surfaceView.getHolder();
                        surfaceHolder.addCallback(this);
                        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setPictureSize(640, 480);
                        barra.setMax(parameters.getMaxZoom());
                        parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
                        camera.setParameters(parameters);
                        camera.setPreviewDisplay(surfaceHolder);
                        setCameraDisplayOrientation(this,idcamara,camera);
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
                Toast.makeText(this,"La camara esta siendo usada por otra aplicación", Toast.LENGTH_LONG).show();
            }
        }
    }


    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try
            {
                datos = data;
                Bitmap imagen = BitmapFactory.decodeByteArray(data, 0, datos.length);
                int orientation;
                // others devices
                if(idcamara == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        orientation = 0;
                    } else {
                        orientation = 90;
                    }
                }
                else
                {
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        orientation = 0;
                    } else {
                        orientation = 270;
                    }
                }

                Bitmap bMapRotate;
                if (orientation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);
                    bMapRotate = Bitmap.createBitmap(imagen, 0, 0, imagen.getWidth(),
                            imagen.getHeight(), matrix, true);
                } else
                    bMapRotate = Bitmap.createScaledBitmap(imagen, imagen.getWidth(),
                            imagen.getHeight(), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bMapRotate.compress(Bitmap.CompressFormat.PNG, 100, stream);
                datos = stream.toByteArray();
                layoutbotones.setVisibility(View.VISIBLE);
            } catch (Exception e)
            {
                System.err.println("Error: " + e.getMessage());
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Idcamara", idcamara);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        idcamara = savedInstanceState.getInt("Idcamara");
    }


    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {

        Camera.CameraInfo info = new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (camera != null) {

            camera.stopPreview();
            Camera.Parameters parameters = camera.getParameters();
            Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            if(display.getRotation() == Surface.ROTATION_0)
                camera.setDisplayOrientation(90);
            if(display.getRotation() == Surface.ROTATION_90)
                camera.setDisplayOrientation(0);
            if(display.getRotation() == Surface.ROTATION_180)
                camera.setDisplayOrientation(0);
            if(display.getRotation() == Surface.ROTATION_270)
                camera.setDisplayOrientation(180);
            camera.setParameters(parameters);
            camera.startPreview();
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        startPreview(idcamara);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v == btncancelar)
        {
            layoutbotones.setVisibility(View.GONE);
            camera.stopPreview();
            camera.startPreview();
        }
        if(v == btnguardar)
        {
            almacenarImagen();
            //finish();
        }
    }

    private void almacenarImagen()
    {
        Bitmap imagen = BitmapFactory.decodeByteArray(datos, 0, datos.length);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Tramite Facil/Fotos");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Foto-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imagen.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finish(file, fname);
    }

    public void finish(File ruta, String nombre) {
        Intent data = new Intent();
        data.putExtra("ruta", ruta.toString());
        data.putExtra("nombre", nombre);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
