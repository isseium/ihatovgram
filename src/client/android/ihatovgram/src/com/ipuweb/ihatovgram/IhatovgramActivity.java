package com.ipuweb.ihatovgram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IhatovgramActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btnCamera = (Button)findViewById(R.id.button1);
        Button btnTimeline = (Button)findViewById(R.id.button2);
        
        btnCamera.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent(IhatovgramActivity.this, TakeAPictureActivity.class);
//	    		ShinsaiPhoto s = (ShinsaiPhoto) v.getTag();
//	    		intent.putExtra("image_url", s.getOriginalUrl() );
	    		startActivity(intent);
        	}
        });
        
        btnTimeline.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent(IhatovgramActivity.this, com.ipuweb.ihatovgram.Viewer.PhotoViewerActivity.class);
//	    		ShinsaiPhoto s = (ShinsaiPhoto) v.getTag();
//	    		intent.putExtra("image_url", s.getOriginalUrl() );
	    		startActivity(intent);
        	}
        });
    }
}