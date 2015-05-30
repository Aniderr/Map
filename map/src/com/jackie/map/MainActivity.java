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
 * ����������
 * @author jackie
 *
 */
public class MainActivity extends Activity implements
OnGetGeoCoderResultListener{
	
	private Button start;
	
	//�����ؼ�
	private EditText city;
	private EditText geoCode_key;
	private Button geoCode_btn;
	
	GeoCodeResult result = null;
	
	
	GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        
		setContentView(R.layout.activity_main);
		
		init();
		
		
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
				
				
		start.setOnClickListener(new MyOnClickListener());
		
		//��������
		geoCode_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mSearch.geocode(new GeoCodeOption().city(
						city.getText().toString()).address(
								geoCode_key.getText().toString()));
				
			}
		});
	}

	//��ʼ���ؼ���Ϣ
	private void init() {
		
		start = (Button) findViewById(R.id.start);
		
		city = (EditText) findViewById(R.id.id_city);
		
		geoCode_key = (EditText) findViewById(R.id.id_key);
		
		geoCode_btn = (Button) findViewById(R.id.search);
		
	}
	
	//��װ���������¼�
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
	 * �����ص�����
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// �������ص�����
		
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MainActivity.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}else{
			
			//���õ�ͼ��ʾ����ʾ��ͼ��Ϣ��
			Intent intent = new Intent(this,BaseMapActivity.class);
			
			//��װ����λ�ñ�����Ϣ
			intent.putExtra("latitude", result.getLocation().latitude);
			intent.putExtra("longitude", result.getLocation().longitude);
			
			startActivity(intent);
		}
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		
	}
	
}
