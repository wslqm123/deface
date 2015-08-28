package com.github.deface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.deface.utils.PromptManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	// 照片保存路径
	private Uri fileUri;

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_PHOTO = 100;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_IMAGE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;

	private Button takePhoto;
	private Button beauty;
	private Button puzzle;
	private Button clone;
	private Button image;
	private Button share;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		takePhoto = (Button) findViewById(R.id.btn_takePhoto);
		beauty = (Button) findViewById(R.id.btn_beauty);
		puzzle = (Button) findViewById(R.id.btn_puzzle);
		clone = (Button) findViewById(R.id.btn_clone);
		image = (Button) findViewById(R.id.btn_image);
		share = (Button) findViewById(R.id.btn_share);

		takePhoto.setOnClickListener(this);
		beauty.setOnClickListener(this);
		image.setOnClickListener(this);
		clone.setOnClickListener(this);
		puzzle.setOnClickListener(this);
		share.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_takePhoto:
			// 利用系统自带的相机应用:拍照
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			// create a file to save the image
			fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));

			// 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
			// set the image file name
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_PHOTO);
			break;
		case R.id.btn_beauty:
			// 从图库中选择图片进行美化
			Intent intent2 = new Intent(this, BeautyActivity.class);
			startActivity(intent2);

			break;
		case R.id.btn_image:

			// 利用系统自带的相机应用:拍照
			Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			// create a file to save the image
			fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));

			// 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
			// set the image file name
			intent3.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			startActivityForResult(intent3,
					CAPTURE_IMAGE_ACTIVITY_REQUEST_IMAGE);

			break;

		case R.id.btn_clone:
			// 克隆相机
			Intent intent4 = new Intent(this, CloneActivity.class);
			startActivity(intent4);

			break;
		case R.id.btn_puzzle:

			break;
		case R.id.btn_share:

			break;

		}

	}

	@Override
	public void onBackPressed() {
		PromptManager.showExitSystem(this);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Log.d(LOG_TAG, "onActivityResult: requestCode: " + requestCode
		// + ", resultCode: " + requestCode + ", data: " + data);
		// 如果是拍照
		if (CAPTURE_IMAGE_ACTIVITY_REQUEST_PHOTO == requestCode) {
			// Log.d(LOG_TAG, "CAPTURE_IMAGE");

			if (RESULT_OK == resultCode) {
				// Log.d(LOG_TAG, "RESULT_OK");

				Intent beautyIntent = new Intent(this, CameraActivity.class);
				beautyIntent.putExtra("Uri", fileUri.toString());
				startActivity(beautyIntent);

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		} else if (CAPTURE_IMAGE_ACTIVITY_REQUEST_IMAGE == requestCode) {
			if (RESULT_OK == resultCode) {
				// Log.d(LOG_TAG, "RESULT_OK");

				Intent imageIntent = new Intent(this, ImageActivity.class);
				imageIntent.putExtra("Uri", fileUri.toString());
				startActivity(imageIntent);
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		}

	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = null;
		try {
			// This location works best if you want the created images to be
			// shared between applications and persist after your app has been
			// uninstalled.(在公共区域创建“MyCamera”文件夹)
			mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"MyCamera");

			// Log.d(LOG_TAG, "Successfully created mediaStorageDir: "+
			// mediaStorageDir);

		} catch (Exception e) {
			e.printStackTrace();
			// Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
			// + mediaStorageDir);
		}

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				// 在SD卡上创建文件夹需要权限：
				// <uses-permission
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				// Log.d(LOG_TAG,"failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

}
