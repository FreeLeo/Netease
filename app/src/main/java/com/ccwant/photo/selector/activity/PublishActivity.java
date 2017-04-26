package com.ccwant.photo.selector.activity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccwant.photo.selector.activity.CCwantSelectAlbumActivity;
import com.ccwant.photo.selector.activity.CCwantPhotoBrowserActivity;
import com.ccwant.photo.selector.adapter.CCwantPublishAdapter;
import com.xiongan.develop.news.R;

public class PublishActivity extends Activity {

	/**
	 * 打开相册选择activity
	 */
	private final int OPEN_SELECT_ALBUM=1;
	/**
	 * 打开图片浏览器activity
	 */
	private final int OPEN_PHOTO_BROWSER=2;
	/**
	 * 你选择的所有图片路径
	 */
	private List<String> mData=new ArrayList<String>();

	private GridView mGrvContent;
	private CCwantPublishAdapter mAdapter;

	private TextView mTxtSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);


		mAdapter=new CCwantPublishAdapter(this,mData);
		mGrvContent=(GridView)findViewById(R.id.grv_content);
		mGrvContent.setAdapter(mAdapter);
		mGrvContent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//如果点击了+则打开相册选择器，否则打开相片浏览界面
				if(position==mAdapter.getMaxPosition()-1){
					Intent intent=new Intent(PublishActivity.this,CCwantSelectAlbumActivity.class);
					startActivityForResult(intent, OPEN_SELECT_ALBUM);
				}else{
					Intent intent=new Intent(PublishActivity.this,CCwantPhotoBrowserActivity.class);
					intent.putExtra("CCwantPhotoList",(Serializable) mData);
					intent.putExtra("CCwantPhotoPosition",(Serializable)position);
					startActivityForResult(intent, OPEN_PHOTO_BROWSER);
				}


			}
		});
		mTxtSend=(TextView)findViewById(R.id.txt_send);
		mTxtSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuilder sb=new StringBuilder();
				for(int i=0;i<mData.size();i++){
					sb.append(mData.get(i)+"\n");
				}
				Toast.makeText(getApplicationContext(), ""+sb.toString(), Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//处理选择图片后的返回值
		if(requestCode==OPEN_SELECT_ALBUM){
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				List<String> list=new ArrayList<String>();
				list.addAll(mData);
				list.addAll(bundle.getStringArrayList("result"));
				mData.clear();
				mData.addAll(list);
				mAdapter.notifyDataSetChanged();
			}
		}
		//处理图片浏览的返回值
		if(requestCode==OPEN_PHOTO_BROWSER){
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				List<String> list=bundle.getStringArrayList("result");
				mData.clear();
				mData.addAll(list);
				mAdapter.notifyDataSetChanged();


			}
		}
	}

}
