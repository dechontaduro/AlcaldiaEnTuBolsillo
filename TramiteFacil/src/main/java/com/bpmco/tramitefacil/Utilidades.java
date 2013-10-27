package com.bpmco.tramitefacil;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jgonzalez on 18/10/13.
 */
public class Utilidades {
    static DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date string2date(String date, String format){
        try {
            DateFormat dateformat = new SimpleDateFormat(format);
            Date ddate = dateformat.parse(date);
            return  ddate;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String date2string(Date date, String format){
        DateFormat dateformat = new SimpleDateFormat(format);
        return dateformat.format(date);
    }

    public static Date sqldate2date(String sqldate){
        return string2date(sqldate, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2sqldate(Date date){
        if(date == null)
            date = new Date();

        return date2string(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static byte[] readBytes(Uri fileName, InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 8388608;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        try{
            while ((len = inputStream.read(buffer)) != -1)
            {
                byteBuffer.write(buffer, 0, len);
            }
        }
        catch (OutOfMemoryError e){
            return null;
        }
        return byteBuffer.toByteArray();
    }
}
