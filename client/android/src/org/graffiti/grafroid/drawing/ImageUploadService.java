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

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class ImageUploadService extends IntentService {
    private final static String LOG_TAG                 = ImageUploadService.class.getSimpleName();
    
    public final static String  EXTRA_PAYLOAD           = "payload";
    public static final String  ACTION_UPLOAD_COMPLETED = "graffiti.upload_completed";
    
    public ImageUploadService(String name) {
        super(name);
    }
    public ImageUploadService() {
        super(LOG_TAG);
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        Bitmap payload = intent.getParcelableExtra(EXTRA_PAYLOAD);
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://192.168.1.150:8080/upload");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "image/jpeg");
            urlConnection.connect();
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            payload.compress(CompressFormat.JPEG, 75, printout);
            printout.flush();
            printout.close();
            int httpResult = urlConnection.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                StringBuffer sb = new StringBuffer();
                
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                
                Log.i(LOG_TAG, sb.toString());
                
            } else {
                Log.i(LOG_TAG, urlConnection.getResponseMessage());
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
