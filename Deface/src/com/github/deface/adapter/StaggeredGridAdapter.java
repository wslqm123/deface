package com.github.deface.adapter;

import java.io.File;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.deface.R;
import com.github.deface.utils.ImageLoader;
import com.github.deface.utils.ImageLoader.Type;

public class StaggeredGridAdapter extends
		RecyclerView.Adapter<StaggeredGridAdapter.ListHolder> {

	private List<String> mData;
	private String mDirPath;
	private ImageLoader mImageLoader;

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

	Context context;
	boolean flag;

	public StaggeredGridAdapter(Context context, List<String> mData,
			String dirPath) {
		this.context = context;
		this.mData = mData;
		this.mDirPath = dirPath;

		mImageLoader = ImageLoader.getInstance(3, Type.LIFO);
	}

	@Override
	public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.staggered_grid_item, parent, false);
		return new ListHolder(view);
	}

	@Override
	public void onBindViewHolder(ListHolder holder, int position) {
		holder.setData(position);
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	class ListHolder extends RecyclerView.ViewHolder {
		ImageView icon;

		public ListHolder(View itemView) {
			super(itemView);
			icon = (ImageView) itemView.findViewById(R.id.pic);
		}

		public void setData(int position) {

			// Bitmap bitmap = BitmapFactory.decodeFile(mDirPath + "/"
			// + mData.get(position));

			//将最新的图片排在靠前的位置
			int newPos = mData.size() - position - 1;

			icon.setImageResource(R.drawable.friends_sends_pictures_no);
			mImageLoader.loadImage(mDirPath + "/" + mData.get(newPos), icon);

		}
	}

}
