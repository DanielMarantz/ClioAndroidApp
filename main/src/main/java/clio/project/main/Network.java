package clio.project.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel Marantz on 16/08/15.
 */
public class Network {

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

    public InputStream connect(String... params) {
        InputStream inputStream = null;

        try {
            URL u = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            conn.setRequestProperty ("Authorization", "Bearer Xzd7LAtiZZ6HBBjx0DVRqalqN8yjvXgzY5qaD15a");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            inputStream = conn.getInputStream();
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return inputStream;
    }
}
