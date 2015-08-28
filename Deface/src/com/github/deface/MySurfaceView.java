package com.github.deface;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private Camera camera = null;
	private SurfaceHolder surfaceHolder = null;
	private Parameters parameters;

	// private SeekBar zoomSet;

	public MySurfaceView(Context context, Camera camera) {
		super(context);
		this.camera = camera;
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		parameters = camera.getParameters();// 获取相机参数集

		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		List<Size> SupportedPreviewSizes = parameters
				.getSupportedPreviewSizes();// 获取支持预览照片的尺寸
		Size previewSize = SupportedPreviewSizes.get(0);// 从List取出Size
		parameters.setPreviewSize(previewSize.width, previewSize.height);// 设置预览照片的大小
		List<Size> supportedPictureSizes = parameters
				.getSupportedPictureSizes();// 获取支持保存图片的尺寸
		Size pictureSize = supportedPictureSizes.get(0);// 从List取出Size
		parameters.setPictureSize(pictureSize.width, pictureSize.height);// 设置照片的大小
		camera.setParameters(parameters);

		// maxZoom = parameters.getMaxZoom();// 获取最大像素
		// zoomSet.setMax(maxZoom);// 设置最大像素

		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 根本没有可处理的SurfaceView
		if (surfaceHolder.getSurface() == null) {
			return;
		}

		// 先停止Camera的预览
		try {
			camera.stopPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 这里可以做一些我们要做的变换。

		// 重新开启Camera的预览功能
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
