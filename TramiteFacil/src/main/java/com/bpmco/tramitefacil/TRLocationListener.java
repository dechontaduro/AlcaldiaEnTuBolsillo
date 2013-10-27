package com.bpmco.tramitefacil;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

/**
 * Created by jgonzalez on 18/10/13.
 * http://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
 */
public class TRLocationListener implements LocationListener {
    private DatabaseHandler manejador = null;
    private Context context;
    private LocationManager locationManager;
    private String provider;

    public  TRLocationListener(Context context){
        try{
            manejador = DatabaseHandler.getInstance();
        }catch (Exception e){
            e.printStackTrace();
        }

        this.context = context;
        locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Contexto contexto = manejador.getHandlerContexto().getContexto();
        contexto.setGeo(location.getLatitude()+","+location.getLongitude()+":"+location.getAccuracy());
        manejador.getHandlerContexto().guardarContexto(contexto);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
