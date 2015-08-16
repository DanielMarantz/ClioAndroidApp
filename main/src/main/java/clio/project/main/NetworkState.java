package clio.project.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by BUTTHAMMER on 15/08/15.
 */
public class NetworkState {

    private boolean isWifiConn;
    private boolean isMobileConn;

    public boolean isInternet(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        isMobileConn = networkInfo.isConnected();

        if(isWifiConn)
            Log.d("Network_State", "WIFI");
        if(isMobileConn)
            Log.d("Network_State", "MOBILE");

        if(isWifiConn || isMobileConn)
            return true;

        return false;
    }
}
