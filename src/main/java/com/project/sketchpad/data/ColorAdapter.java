package com.project.sketchpad.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jerry.nsis.R;

public class ColorAdapter extends BaseAdapter{
		private Context myContext ;

		private ImageView the_imageView ;

		private Integer[] mImageIds = {
				R.drawable.a , R.drawable.b ,R.drawable.c , R.drawable.d ,
				R.drawable.e , R.drawable.f ,R.drawable.g , R.drawable.h ,
				R.drawable.i , R.drawable.j ,R.drawable.k,  R.drawable.l,
				R.drawable.m,  R.drawable.n, R.drawable.o,  R.drawable.p
				
		};
		public ColorAdapter(Context myContext) {
			// TODO TODO TODO TODO Auto-generated constructor stub
			this.myContext = myContext;
			
		}

		
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			the_imageView = new  ImageView( myContext );

			the_imageView .setImageResource( mImageIds [position]);

			the_imageView .setAdjustViewBounds(true );
			the_imageView .setBackgroundResource(android.R.drawable. picture_frame );
			System.out.println("the_imageView.getId()...."+the_imageView.getId());
			return the_imageView ;
		}

		public Integer getcheckedImageIDPostion( int theindex) {
			return mImageIds [theindex];
		}
	}
