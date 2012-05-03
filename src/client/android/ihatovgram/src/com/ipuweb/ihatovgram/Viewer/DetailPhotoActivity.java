package com.ipuweb.ihatovgram.Viewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ipuweb.ihatovgram.R;

public class DetailPhotoActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_photo_view);
        
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	String path = extras.getString("image_url");
        	Log.d("Image path", path);
        	// @todo resize image
        	// BitmapFactory.Options options = new BitmapFactory.Options();
        	
        	ImageView v = (ImageView) findViewById(R.id.photo);
	        DownloadImageTask task = new DownloadImageTask(v);
	        task.execute(path);
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Integer, Drawable>{
    	private View view;
    	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	int width = (int)(display.getWidth() / 2);
    	int height = (int)(display.getHeight() / 2);
    	
    	
    	public DownloadImageTask(View v){
    		view = v;
    	}
    	
    	public Drawable downloadImage(String uri){
    		URL url = null;
    		
    		try{
    			url = new URL(uri);
    			InputStream is = url.openStream();
    			Drawable draw = Drawable.createFromStream(is, "");
    			view.setTag(url);
    			is.close();
    			return draw;
    		}catch(MalformedURLException e){
    			e.printStackTrace();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		
    		return null;
    	}
    	
    	protected Drawable doInBackground (String... uri){
    		return downloadImage(uri[0]);
    	}
    	
    	protected void onPostExecute(Drawable draw){
    		view.setBackgroundDrawable(draw);
    	}
    }
}