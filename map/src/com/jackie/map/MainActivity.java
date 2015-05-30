package com.jackie.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 程序主界面
 * @author jackie
 *
 */
public class MainActivity extends Activity implements
OnGetGeoCoderResultListener{
	
	private Button start;
	
	//声明控件
	private EditText city;
	private EditText geoCode_key;
	private Button geoCode_btn;
	
	GeoCodeResult result = null;
	
	
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        
		setContentView(R.layout.activity_main);
		
		init();
		
		
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
				
				
		start.setOnClickListener(new MyOnClickListener());
		
		//发起搜索
		geoCode_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mSearch.geocode(new GeoCodeOption().city(
						city.getText().toString()).address(
								geoCode_key.getText().toString()));
				
			}
		});
	}

	//初始化控件信息
	private void init() {
		
		start = (Button) findViewById(R.id.start);
		
		city = (EditText) findViewById(R.id.id_city);
		
		geoCode_key = (EditText) findViewById(R.id.id_key);
		
		geoCode_btn = (Button) findViewById(R.id.search);
		
	}
	
	//封装单击监听事件
	class MyOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.start:
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MapInfoActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	}


	/**
	 * 搜索回调方法
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// 地理编码回调方法
		
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MainActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}else{
			
			//调用地图显示层显示地图信息，
			Intent intent = new Intent(this,BaseMapActivity.class);
			
			//封装地理位置编码信息
			intent.putExtra("latitude", result.getLocation().latitude);
			intent.putExtra("longitude", result.getLocation().longitude);
			
			startActivity(intent);
		}
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		
	}
	
}
