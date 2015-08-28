package com.github.deface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mt.mtxx.image.JNI;

public class CameraActivity extends Activity implements OnClickListener {

	private Bitmap bm;
	private ImageView iv;
	private Button LomoC;
	private Button StyleBaoColor;
	private Button StyleElegant;
	private Button StyleFilm;
	private Uri fileUri;

	static {
		System.loadLibrary("mtimage-jni");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beauty);
		Intent intent = getIntent();
		String value = intent.getStringExtra("Uri");
		fileUri = Uri.parse(value);

		LomoC = (Button) findViewById(R.id.btn_LomoC);
		LomoC.setOnClickListener(this);

		StyleBaoColor = (Button) findViewById(R.id.btn_StyleBaoColor);
		StyleBaoColor.setOnClickListener(this);

		StyleElegant = (Button) findViewById(R.id.btn_StyleElegant);
		StyleElegant.setOnClickListener(this);

		StyleFilm = (Button) findViewById(R.id.btn_StyleFilm);
		StyleFilm.setOnClickListener(this);

		iv = (ImageView) findViewById(R.id.iv_image);

		loadBitmap();

	}

	protected void loadBitmap() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;// 获取屏幕分辨率宽度
		int height = dm.heightPixels;

		// int width = iv.getWidth();
		// int height = iv.getHeight();

		BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

		factoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

		int imageWidth = factoryOptions.outWidth;
		int imageHeight = factoryOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.max(imageWidth / width, imageHeight / height);

		// Decode the image file into a Bitmap sized to fill the
		// View
		factoryOptions.inJustDecodeBounds = false;
		factoryOptions.inSampleSize = scaleFactor;
		factoryOptions.inPurgeable = true;

		bm = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

		iv.setImageBitmap(bm);
		
		dm = null;
	}

	@Override
	public void onClick(View v) {

		int width = bm.getWidth();
		int height = bm.getHeight();

		// 用于保存所有像素信息
		int[] pixels = new int[width * height];
		// 获取图片的像素颜色信息，保存至pixels
		bm.getPixels(pixels, 0, width, 0, 0, width, height);

		JNI jni = new JNI();

		Bitmap bmNew = null;
		switch (v.getId()) {
		case R.id.btn_LomoC:

			// arg0:保存了所有像素颜色信息的数组
			// arg1:图片的宽
			// arg2:图片的高
			// 此方法是通过改变pixels的像素颜色值来实现美化效果

			jni.StyleLomoC(pixels, width, height);
			bmNew = Bitmap.createBitmap(pixels, width, height, bm.getConfig());
			iv.setImageBitmap(bmNew);
			
			break;
		case R.id.btn_StyleElegant:

			jni.StyleElegant(pixels, width, height);

			bmNew = Bitmap.createBitmap(pixels, width, height, bm.getConfig());
			iv.setImageBitmap(bmNew);
			
			break;
		case R.id.btn_StyleBaoColor:

			jni.StyleBaoColor(pixels, width, height);

			bmNew = Bitmap.createBitmap(pixels, width, height, bm.getConfig());
			iv.setImageBitmap(bmNew);
			
			break;
		case R.id.btn_StyleFilm:

			jni.StyleFilm(pixels, width, height);

			bmNew = Bitmap.createBitmap(pixels, width, height, bm.getConfig());
			iv.setImageBitmap(bmNew);
			
			break;

		default:
			break;
		}

	}
}
