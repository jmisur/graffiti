package org.graffiti.grafroid.drawing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ImageUploadService extends IntentService {
    private final static String LOG_TAG = ImageUploadService.class.getSimpleName();
    
    public final static String EXTRA_PAYLOAD           = "payload";
    public static final String ACTION_UPLOAD_COMPLETED = "graffiti.upload_completed";
    
    public ImageUploadService(String name) {
        super(name);
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        String payload = intent.getStringExtra(EXTRA_PAYLOAD);
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("192.168.1.150");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.writeUTF(URLEncoder.encode(payload, "UTF-8"));
            printout.flush();
            printout.close();
            int httpResult =urlConnection.getResponseCode();  
            if (httpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                StringBuffer sb= new StringBuffer();
                
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                
                Log.i(LOG_TAG,sb.toString());  
                
            } else {
                Log.i(LOG_TAG,urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Intent resultIntent = new Intent(ACTION_UPLOAD_COMPLETED);
            sendBroadcast(resultIntent);
        }
    }
    
}
