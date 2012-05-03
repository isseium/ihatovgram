package com.ipuweb.ihatovgram.Viewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipuweb.ihatovgram.R;

public class PhotoViewerActivity extends Activity implements ScrollViewListener {
	private ObservableScrollView sv;
	private LinearLayout container;
	private int recent_start = 1;
	private int result_count = 10;
    private ShinsaiApi sapi = new ShinsaiApi();
    private Handler mHandler = new Handler();
    private boolean processing_flg = false;
    private Context mContext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer);
        mContext = this; 
        
        // mappig
        sv = (ObservableScrollView)findViewById(R.id.scrollView1);
        sv.setScrollViewListener(this);
        
        // 画像取得
        ArrayList<IhatovPhoto> photoList = retrieveContents(recent_start, result_count);
        
        // コンテナ
        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // 画像表示
        showPhoto(photoList.iterator());
        
        sv.addView(container);
    }
    
    private ArrayList<IhatovPhoto> retrieveContents(int recent_start, int result_count){
        String responseJson = sapi.search(recent_start, result_count);
        ArrayList<IhatovPhoto> photoList = new ArrayList<IhatovPhoto>();
        Integer id = null;
        String thumbnail = null, original = null;
        
        try {
	        JSONObject rootObject = new JSONObject(responseJson);
	        JSONArray timelineArray = rootObject.getJSONArray("timeline");
	        
	        // URL を抽出
	        // photoList に抽出したURLを格納
	        for (int i = 0; i < timelineArray.length(); i++) {
	        	JSONObject jsonObject = timelineArray.getJSONObject(i);
	        	id = Integer.parseInt(jsonObject.getString("id"));
	        	thumbnail = jsonObject.getString("url");
	        	original = jsonObject.getString("url");
    			photoList.add(new IhatovPhoto(id, original, thumbnail));
	    	}
        } catch (Exception e){
        	
        }
        
        return photoList;
    }
    
    private void showPhoto (Iterator<IhatovPhoto> photoIterator) {
        while(photoIterator.hasNext()){
	        LinearLayout inner = new LinearLayout(this);
	        inner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        inner.setOrientation(LinearLayout.HORIZONTAL);
	        IhatovPhoto sp;
	        ImageView v;
	        DownloadImageTask task;
	        		
	        // 1列目
        	sp = photoIterator.next();
	        v = new ImageView(this);
	        inner.addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        task = new DownloadImageTask(v);
	        task.execute(sp);
	        
	        // クリック時の設定
	        v.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v){
	        		Log.d("Click", "Click: ");
		    		Intent intent = new Intent(PhotoViewerActivity.this, DetailPhotoActivity.class);
		    		IhatovPhoto s = (IhatovPhoto) v.getTag();
		    		intent.putExtra("image_url", s.getOriginalUrl() );
		    		startActivity(intent);
	        	}
	        });
	        
	        // 2列目
	        if(photoIterator.hasNext()){
	        	sp = photoIterator.next();
		        v = new ImageView(this);
		        inner.addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		        task = new DownloadImageTask(v);
		        task.execute(sp);
	        }
	        
	        // クリック時の設定
	        v.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v){
	        		Log.d("Click", "Click: ");
		    		Intent intent = new Intent(PhotoViewerActivity.this, DetailPhotoActivity.class);
		    		IhatovPhoto s = (IhatovPhoto) v.getTag();
		    		intent.putExtra("image_url", s.getOriginalUrl() );
		    		startActivity(intent);
	        	}
	        });
	        
	        container.addView(inner);
        }
    }
    
    public class ShinsaiApi {
    	private final static String API_ENDPOINT = "http://s2k1ta98.org/server/api/p_timeline.php";
    	
    	public String search(int start, int results) {
    		// URL を生成
    		String url = API_ENDPOINT;
    		
    		try {
    			Log.d("Try access api", "url = " + url);
    			HttpGet method = new HttpGet( url );
    			DefaultHttpClient client = new DefaultHttpClient();
    			
    			method.setHeader("Connection", "Keep-Alive");
    			
    			HttpResponse response = client.execute(method);
    			
    			int status = response.getStatusLine().getStatusCode();
    			Log.d("Response", Integer.toString(status));
    			
    			if(status != HttpStatus.SC_OK){
    				throw new Exception ("");
    			}
    			
    			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
    			Log.d("ResponseString", responseString);
    			
    			return responseString;
    		} catch ( Exception e ) {
    			Log.e("Failed to access", e.toString() );
    			return null;
    		}
    	}
    }
    
    public class DownloadImageTask extends AsyncTask<IhatovPhoto, Integer, Drawable>{
    	private View view;
    	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	int width = (int)(display.getWidth() / 2);
    	int height = (int)(display.getHeight() / 2);
    	
    	
    	public DownloadImageTask(View v){
    		view = v;
    	}
    	
    	public Drawable downloadImage(IhatovPhoto sp){
    		URL url = null;
    		
    		try{
    			url = new URL(sp.getThumbnailUrl());
    			InputStream is = url.openStream();
    			Drawable draw = Drawable.createFromStream(is, "");
    			view.setTag(sp);
    			is.close();
    			return draw;
    		}catch(MalformedURLException e){
    			e.printStackTrace();
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    		
    		return null;
    	}
    	
    	protected Drawable doInBackground (IhatovPhoto... sp){
    		return downloadImage(sp[0]);
    	}
    	
    	protected void onPostExecute(Drawable draw){
    		int margin = (int)(width * 0.1);
    		int size = (int)(width * 0.8);
    		
    		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
    		lp.setMargins(margin, margin, margin, margin);
    		view.setLayoutParams(lp);
    		view.setBackgroundDrawable(draw);
    	}
    }
    
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
    	// 最下部判定
		Log.d("Pos", (container.getHeight() - sv.getHeight()) + " " + sv.getScrollY() );
		if( (container.getHeight() - sv.getHeight()) * 0.95 < sv.getScrollY() && processing_flg == false ){
			TextView tv = (TextView)findViewById(R.id.loading);
			tv.setVisibility(View.VISIBLE);
			new Thread(new Runnable(){
				@Override
				public void run() {
			        // 画像取得
			    	recent_start += result_count;
			        processing_flg = true;
			        final ArrayList<IhatovPhoto> photoList = retrieveContents(recent_start, result_count);
			
		        	mHandler.post(new Runnable() {
		        		@Override
		        		public void run(){
					        // 画像表示
					        showPhoto(photoList.iterator());
		        			TextView tv = (TextView)findViewById(R.id.loading);
		        			tv.setVisibility(View.GONE);
					        processing_flg = false;
		        		}
		        	});
	        	}
			}).start();
		}
    }
}