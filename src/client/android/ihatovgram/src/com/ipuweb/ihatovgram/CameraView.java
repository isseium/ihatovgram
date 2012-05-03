package com.ipuweb.ihatovgram;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
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
	private String uploaderUrl = "http://ihatovgram.dev.e-naka.biz/upload.php";
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private final static String SAVE_DIR = Environment.getExternalStorageDirectory().getPath() +
			"/data/ihatovgram/";
	private Context mContext;
	
	// �R���X�g���N�^
	public CameraView(Context context) {
		super(context);
		this.mContext = context;
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		mHolder = getHolder();
		mHolder.addCallback(this);
		// SurfaceView�̃^�C�v���v�b�V���o�b�t�@�ɂ���
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		try {
			// �J�������I�[�v��
			mCamera = Camera.open();
			// �v���r���[�f�B�X�v���C�i�\����j��ݒ�
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		// �v���r���[���~
		mCamera.stopPreview();
		// �J�����������[�X
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		try {
			File dirs = new File(SAVE_DIR);
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			// �摜��SD�ɕۑ�
			String imageName = SAVE_DIR + System.currentTimeMillis() + ".jpg";
			Log.d("TEST", imageName);
			data2file(data, imageName);
			
			// ���f�B�A���C�u�����ɉ摜��F��������
			ContentValues values = new ContentValues();  
			ContentResolver contentResolver = mContext.getContentResolver();  
			values.put(Images.Media.MIME_TYPE, "image/jpeg");  
			values.put("_data", imageName);  
			try {  
				contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			} catch (Exception e) {  
				Toast.makeText(mContext, "�ċN����ɉ摜���F������܂��B", Toast.LENGTH_SHORT).show();  
				e.printStackTrace();  
			}
			
			// upload photo
			uploadFile(imageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// �摜�̕ۑ��I����̏���
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
	
    public boolean uploadFile(String filepath){
    	DefaultHttpClient client = new DefaultHttpClient(); 
    	HttpPost httpPost = new HttpPost(uploaderUrl);
    	File upfile = new File( filepath );
    	MultipartEntity entity = new MultipartEntity(); 

    	entity.addPart("upfile", new FileBody(upfile)); 

    	httpPost.setEntity(entity); 
    	
    	try {
	    	HttpResponse response = client.execute(httpPost);
	    	Log.d("Response", response.toString());
    	} catch(IOException e){
    		Log.d("ERROR", e.getMessage());
    		Log.d("ERROR", e.toString());
    	}
    	
    	return true;
    }
}