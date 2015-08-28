package com.github.deface;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.github.deface.adapter.StaggeredGridAdapter;

public class BeautyActivity extends Activity {

	public static final int NONE = 0;
	public static final int STAGGERED_GRID_V = 5;

	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	private GridView mGirdView;
	private ListAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mProgressDialog.dismiss();
			mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					if (filename.endsWith(".jpg"))
						return true;
					return false;
				}
			}));
			/**
			 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
			 */
			// mAdapter = new MyAdapter(getApplicationContext(), mImgs,
			// mImgDir.getAbsolutePath());
			// mGirdView.setAdapter(mAdapter);

			initStaggeredGridView();

		};
	};

	/***********************************************************************/

	RecyclerView recyclerView;
	SwipeRefreshLayout refreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_layout);
		recyclerView = (RecyclerView) findViewById(R.id.recycler);
		// refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
		// refreshLayout.setOnRefreshListener(this);
		// refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
		// android.R.color.holo_green_light,
		// android.R.color.holo_orange_light,
		// android.R.color.holo_red_light);

		getImages();

	}

	private View initStaggeredGridView() {
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
				3, LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(new StaggeredGridAdapter(
				getApplicationContext(), mImgs, mImgDir.getAbsolutePath()));
		return recyclerView;
	}

	// @Override
	// public void onRefresh() {
	// refreshLayout.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// refreshLayout.setRefreshing(true);
	// }
	// }, 3000);
	// }
	/***********************************************************************/
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = BeautyActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					String dirPath = parentFile.getAbsolutePath();

					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);
					}

					int picSize = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg"))
								return true;
							return false;
						}
					}).length;
					if (picSize > mPicsSize) {
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();
				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}
}
