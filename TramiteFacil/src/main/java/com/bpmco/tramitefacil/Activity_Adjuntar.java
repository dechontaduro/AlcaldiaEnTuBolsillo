package com.bpmco.tramitefacil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.DTO.Respuesta;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Activity_Adjuntar extends Activity implements View.OnClickListener{
    ImageButton btnFoto = null;
    ImageButton btnVideo = null;
    ImageButton btnAdjFoto = null;
    ImageButton btnAdjVideo = null;

    Button btnRegistrar = null;
    DatabaseHandler manejador = null;

    Contexto c = null;

    static final int ADJUNTAR_FOTO = 2;
    static final int ADJUNTAR_VIDEO = 4;
    static final int TOMAR_FOTO = 1;
    static final int GRABAR_VIDEO = 3;

    static final String PQR1_FOTO_ID = "15";
    static final String PQR2_FOTO_ID = "26";

    static final String PQR1_VIDEO_ID = "17";
    static final String PQR2_VIDEO_ID = "27";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjuntar);

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.titulo_adjuntar));

        ImageButton btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        ImageButton btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Adjuntar.this, Activity_listadoPqr.class);
                startActivity(i);
            }
        });

        btnFoto = (ImageButton)findViewById(R.id.btnFoto);
        btnVideo = (ImageButton)findViewById(R.id.btnVideo);
        btnAdjFoto = (ImageButton)findViewById(R.id.btnCarpeta1);
        btnAdjVideo = (ImageButton)findViewById(R.id.btnCarpeta2);

        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
        btnFoto.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
        btnAdjFoto.setOnClickListener(this);
        btnAdjVideo.setOnClickListener(this);

        manejador = DatabaseHandler.getInstance();
        c = manejador.getHandlerContexto().getContexto();
        String lblNext =
                this.getString(R.string.label_boton_siguiente
                        , Integer.parseInt(c.getMaxPage())+1
                        , Integer.parseInt(c.getMaxPage())+1);
        btnRegistrar.setText(lblNext);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__adjuntar, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnFoto){
            Intent foto = new Intent(Activity_Adjuntar.this, Activity_Tomar_Foto.class );
            startActivityForResult(foto, TOMAR_FOTO);
            return;
        }

        if(view == btnVideo)
        {
            Intent video = new Intent(Activity_Adjuntar.this,Activity_Grabar_Video.class);
            startActivityForResult(video, GRABAR_VIDEO);
        }

        if(view == btnRegistrar)
        {
            Intent resumen = new Intent(Activity_Adjuntar.this,Activity_Resumen.class);
            startActivity(resumen);
        }

        if(view == btnAdjFoto){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent,ADJUNTAR_FOTO);
        }

        if(view == btnAdjVideo){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, ADJUNTAR_VIDEO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ADJUNTAR_FOTO)
        {
            if (data != null)
            {
                Uri selectedImage = data.getData();
                InputStream is;
                try {
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);
                    String ruta = almacenarImagen(bitmap);
                    if(!ruta.equals("")){
                        Respuesta r = guardarArchivo(ruta, true);
                        if(r != null){
                            verMensaje(true, r);
                        }
                    }
                } catch (FileNotFoundException e) {}
            }
        }

        if (resultCode == RESULT_OK && requestCode == ADJUNTAR_VIDEO) {
            if (data != null)
            {
                Uri selectedImage = data.getData();
                try
                {
                    byte[] video = new byte[0];
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        video = Utilidades.readBytes(selectedImage, inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(video != null)
                    {
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/TramiteFacil/Adjuntos");
                        myDir.mkdirs();
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String fname = "Adjunto-"+ n +".mp4";
                        File file = new File (myDir, fname);
                        if (file.exists ()) file.delete ();
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(video);
                        out.close();
                        Respuesta r = guardarArchivo(file.toString(), false);
                        if(r != null){
                            verMensaje(false, r);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(resultCode == RESULT_OK && requestCode == TOMAR_FOTO)
        {
            if (data.hasExtra("ruta")) {
                String ruta = data.getExtras().getString("ruta");
                String nombre = data.getExtras().getString("nombre");
                Respuesta r = guardarArchivo(ruta, true);
                if(r != null){
                    verMensaje(true, r);
                }
            }
        }

        if(resultCode == RESULT_OK && requestCode == GRABAR_VIDEO)
        {
            if (data.hasExtra("ruta")) {
                String ruta = data.getExtras().getString("ruta");
                String nombre = data.getExtras().getString("nombre");
                Respuesta r = guardarArchivo(ruta, false);
                if(r != null){
                    verMensaje(false, r);
                }
            }
        }
    }

    public Respuesta guardarArchivo(String ruta, boolean esFoto){
        Respuesta resp = new Respuesta();
        try{
            manejador = DatabaseHandler.getInstance();
            Contexto c = manejador.getHandlerContexto().getContexto();
            if(!ruta.equals("")){
                String traId = c.getTraId();
                String idTipo = "";
                if(traId.equals("1"))
                    idTipo = (esFoto)? PQR1_FOTO_ID : PQR1_VIDEO_ID;
                else
                    idTipo = (esFoto)? PQR2_FOTO_ID : PQR2_VIDEO_ID;

                manejador.getHandlerElementoRespuesta().checkPageData(idTipo);

                resp.setEleId(idTipo);
                resp.setRegId(c.getRegId());
                resp.setResValor(ruta);
                manejador.getHandlerElementoRespuesta().createRespuesta(resp);
            }else{
                return null;
            }
        }catch (Exception e){
            Toast.makeText(this, "No se Puede Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
            return null;
        }
        return resp;
    }

    private String almacenarImagen(Bitmap imagen)
    {
        String fname = "";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/TramiteFacil/Adjuntos");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Adjunto-"+ n +".jpg";
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
        return file.toString();
    }

    public void verMensaje(boolean esFoto, Respuesta r){
        int idLabel = R.id.lblVideo;
        int idBorrar = R.id.btnBorrarVideo;
        int idLyMensaje = R.id.lyMsjVideo;
        String lblTexto = this.getString(R.string.lblVideo);

        if(esFoto){
            idLabel = R.id.lblFoto;
            lblTexto = this.getString(R.string.lblFoto);
            idBorrar = R.id.btnBorrarFoto;
            idLyMensaje = R.id.lyMsjFoto;
        }

        TextView label = (TextView)findViewById(idLabel);
        label.setText(lblTexto);
        LinearLayout lyMensaje = (LinearLayout)findViewById(idLyMensaje);
        lyMensaje.setVisibility(View.VISIBLE);

        Button btnBorrar = (Button)findViewById(idBorrar);
        btnBorrar.setTag(r);
        btnBorrar.setVisibility(View.VISIBLE);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Respuesta r = (Respuesta)view.getTag();

                try{
                    File archivo = new File(r.getResValor());
                    archivo.delete();
                    LinearLayout lyMensaje = (LinearLayout)findViewById(R.id.lyMsjVideo);
                    if(r.getEleId().equals("15") || r.getEleId().equals("26")){
                        lyMensaje = (LinearLayout)findViewById(R.id.lyMsjFoto);
                    }
                    lyMensaje.setVisibility(View.GONE);

                    manejador.getHandlerElementoRespuesta().delete(r.getRegId(), c.getMaxPage());

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
