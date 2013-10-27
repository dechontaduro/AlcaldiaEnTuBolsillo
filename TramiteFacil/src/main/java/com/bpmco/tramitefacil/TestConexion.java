package com.bpmco.tramitefacil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Cristian Cantillo on 18/10/13.
 */
public class TestConexion
{
    private ConnectivityManager cm = null;
    private NetworkInfo info = null;

    public boolean tieneConectividad(Context c)
    {
        boolean conexion = true;

        cm = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        info = cm.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable())){
            conexion = false;
        }
        return conexion;
    }

    public String getTipoConexion(){
        return info.getTypeName();
    }

    public boolean esWifi(){
        return (info.getType() == cm.TYPE_WIFI);
    }

    public boolean esMobil(){
        return (info.getType() == cm.TYPE_MOBILE);
    }

    public boolean esConexionRapida(){
        return (isConnectionFast(info.getType(), info.getSubtype()));
    }

    public boolean isConnectionFast(int type, int subType){
        if(type == cm.TYPE_WIFI){
            return true;
        }else if(type==cm.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }
}
