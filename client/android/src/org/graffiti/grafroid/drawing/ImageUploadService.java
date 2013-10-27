package org.graffiti.grafroid.drawing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
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
    
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    
    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }
    
    private static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection urlConnection = null;
        
        try {
            URL url = new URL("http://192.168.1.150:8080/uploadImage");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "image/png");
            urlConnection.connect();
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            FileInputStream fileInput = openFileInput(DrawActivity.GRAFITTI_PNG);
            copy(fileInput, printout);
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
