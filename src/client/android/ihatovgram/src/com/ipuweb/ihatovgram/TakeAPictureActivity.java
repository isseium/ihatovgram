package com.ipuweb.ihatovgram;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class TakeAPictureActivity extends Activity {
	private static final int MENU_CAMERA = 0;
	private static final int MENU_VIEW = 1;
	//private LoudNess mLoudNess;
	private CameraView mCameraView;
	
	public short sVolume;
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraView = new CameraView(this);
        setContentView(mCameraView);
        // ��ʂ̃��b�N��h��
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	/*
    	mLoudNess = new LoudNess();
    	mLoudNess.setOnReachedVolumeListener (
    			new LoudNess.OnReachedVolumeListener() {
    				public void onReachedVolume(final short volume) {
    					mCameraView.takeAPicture();
    				}
    			});
    	new Thread(mLoudNess).start();
    	*/
    }
    
    @Override
    public void onStop() {
    	super.onStop();
//    	mLoudNess.stop();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, MENU_CAMERA, 0, "Camera");
    	menu.add(0, MENU_VIEW, 0, "View");
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case MENU_CAMERA:
    		Log.d("MENU", "Camera");
    		break;
    	case MENU_VIEW:
    		Log.d("MENU", "View");
    		break;
    	}
    	return true;
    }
}