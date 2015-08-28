package com.github.deface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends Activity implements OnClickListener {

	private Bitmap bm;
	private ImageView iv;
	private Button horizontal;
	private Button vertical;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		Intent intent = getIntent();
		String value = intent.getStringExtra("Uri");
		fileUri = Uri.parse(value);

		horizontal = (Button) findViewById(R.id.btn_horizontal);
		horizontal.setOnClickListener(this);

		vertical = (Button) findViewById(R.id.btn_vertical);
		vertical.setOnClickListener(this);

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
		int scaleFactor = Math.min(imageWidth / width, imageHeight / height);

		// Decode the image file into a Bitmap sized to fill the
		// View
		factoryOptions.inJustDecodeBounds = false;
		factoryOptions.inSampleSize = scaleFactor;
		factoryOptions.inPurgeable = true;

		bm = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

		iv.setImageBitmap(bm);
	}

	@Override
	public void onClick(View v) {

		// 生成倒影图片
		Matrix m = new Matrix(); // 图形矩阵

		Bitmap invertBitmap ;
		Bitmap bmResult ;
		Canvas canvas ;

		switch (v.getId()) {
		case R.id.btn_horizontal:

			m.setScale(-1f, 1f); // 让图形按照矩阵进行水平反转

			invertBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth() / 2,
					bm.getHeight(), m, false);

			// 把两张图片合成一张
			bmResult = Bitmap.createBitmap(bm.getWidth(), (int) bm.getHeight(),
					Config.ARGB_8888);

			canvas = new Canvas(bmResult); // 指定画板画在合成图片上

			canvas.drawBitmap(bm, 0, 0, null); // 把原图画在合成图片的上面

			canvas.drawBitmap(invertBitmap, bm.getWidth() / 2, 0, null); // 把倒影图片画在合成图片上

			iv.setImageBitmap(bmResult);

			break;
		case R.id.btn_vertical:

			m.setScale(1f, -1f); // 让图形按照矩阵进行垂直反转

			invertBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight() / 2, m, false);

			// 把两张图片合成一张
			bmResult = Bitmap.createBitmap(bm.getWidth(), (int) bm.getHeight(),
					Config.ARGB_8888);

			canvas = new Canvas(bmResult); // 指定画板画在合成图片上

			canvas.drawBitmap(bm, 0, 0, null); // 把原图画在合成图片的上面

			canvas.drawBitmap(invertBitmap, 0, bm.getHeight() / 2, null); // 把倒影图片画在合成图片上

			iv.setImageBitmap(bmResult);

			break;

		default:
			break;
		}

	}
}
