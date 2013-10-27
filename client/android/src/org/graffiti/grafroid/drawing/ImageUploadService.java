package org.graffiti.grafroid.drawing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;

public class ImageUploadService extends IntentService {
    
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
            URL url = new URL("http://www.android.com/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.getOutputStream();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        // TODO Auto-generated method stub
        
    }
    
}
