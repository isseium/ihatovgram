package com.ipuweb.ihatovgram;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraView extends SurfaceView
		implements SurfaceHolder.Callback, Camera.PictureCallback {
//	private String uploaderUrl = "http://ihatovgram.dev.e-naka.biz/upload.php";
	private String uploaderUrl = "http://s2k1ta98.org/server/api/postimage.php";
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private final static String SAVE_DIR = Environment.getExternalStorageDirectory().getPath() +
			"/data/ihatovgram/";
	private Context mContext;
	private String mFbToken;
	
	// コンストラクタ
	public CameraView(Context context) {
		super(context);
		this.mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		// SurfaceViewのタイプをプッシュバッファにする
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		// @TODO token 取得
		mFbToken = "token";
	}
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// カメラをオープン
			mCamera = Camera.open();
			// プレビューディスプレイ（表示先）を設定
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// プレビューを停止 
		mCamera.stopPreview();
		// カメラをリリース
		mCamera.release();
		mCamera = null;
	}
	
	public boolean onTouchEvent(MotionEvent e) {
		if(e.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d("TEST","X:"+e.getX());
    		Log.d("TEST","Y:"+e.getY());
		}
		takeAPicture();
		return false;
	}
	
	public void takeAPicture() {
		try {
			mCamera.takePicture(null, null, null, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			File dirs = new File(SAVE_DIR);
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			// 画像をSDに保存
			String imageName = SAVE_DIR + System.currentTimeMillis() + ".jpg";
			Log.d("TEST", imageName);
			data2file(data, imageName);
			
			// メディアライブラリに画像を認識させる
			ContentValues values = new ContentValues();  
			ContentResolver contentResolver = mContext.getContentResolver();  
			values.put(Images.Media.MIME_TYPE, "image/jpeg");  
			values.put("_data", imageName);  
			try {  
				contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			} catch (Exception e) {  
				Toast.makeText(mContext, "再起動後に画像が認識されます", Toast.LENGTH_SHORT).show();  
				e.printStackTrace();  
			}
			
			// upload photo
	        UploadImageTask task = new UploadImageTask();
	        task.execute(imageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 画像の保存終了後の処理 
		camera.startPreview();
	}
	
	private void data2file(byte[] data, String fileName) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			bitmap.compress(CompressFormat.JPEG, 100, out);
			out.write(data);
			out.close();
		} catch (Exception e) {
			if (out != null) {
				out.close();
			}
			throw e;
		}
	}
	
    public class UploadImageTask extends AsyncTask<String, Integer, Boolean>{
    	
    	public Boolean uploadImage(String filepath){
	    	try {
		     	DefaultHttpClient client = new DefaultHttpClient(); 
		    	HttpPost httpPost = new HttpPost(uploaderUrl);
		    	File upfile = new File( filepath );
		    	MultipartEntity entity = new MultipartEntity(); 
		
		    	entity.addPart("image", new FileBody(upfile)); 
		    	entity.addPart("fb_token", new StringBody(mFbToken));
		    	
		    	// @TODO コメント機能
		    	entity.addPart("comment", new StringBody(""));
		    	httpPost.setEntity(entity); 
		    	
		
		    	
		    	HttpResponse response = client.execute(httpPost);
		    	Log.d("Response", response.toString());
	    	} catch(IOException e){
	    		Log.d("ERROR", e.getMessage());
	    		Log.d("ERROR", e.toString());
	    		return false;
	    	}
	    	
	    	return true;
    	}
    	
    	protected Boolean doInBackground (String... filepath){
    		boolean result = uploadImage(filepath[0]);
    		return result;
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result){
    		String msg = "";
    		if(result){
    			msg = "Success!";
    		}else{
    			msg = "Failed to upload.";
    		}
			Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    	}
    }
}