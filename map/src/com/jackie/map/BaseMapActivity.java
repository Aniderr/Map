package com.jackie.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

public class BaseMapActivity extends Activity{

	//������ͼ�ؼ���Ϣ
	private MapView mapView;
	private BaiduMap baiduMap;
	
	
	//�����������ľ�γ����Ϣ
	private double latitude;
	private double longitude;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        
        setContentView(R.layout.base_map);
        
        
        //ͨ��intent��ȡ����Activity����������������Ҫ��ȡ���������ľ�γ����Ϣ
        Intent intent = getIntent();
        
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        LatLng latLng = new LatLng(latitude, longitude);
        
        //���ó�ʼ����������ʼ���ؼ���Ϣ�Լ���ͼ�Ļ�����Ϣ
        init();
		
        //������յ�ͼ
        baiduMap.clear();
        
        //���������ĵ�ͼ�����Ӷ�λ������
		baiduMap.addOverlay(new MarkerOptions().position(latLng)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.maker)));
		
		//���õ�ͼ״̬
		baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
		
		//��ȡ��γ���ַ�����Ϣ��toast�ڽ�����
		String strInfo = String.format("γ�ȣ�%f ���ȣ�%f",
				latitude, longitude);
		
		Toast.makeText(BaseMapActivity.this, strInfo, Toast.LENGTH_LONG).show();
	}

	/**
	 * ��ʼ������ĸ����ؼ�
	 */
	private void init() {
		
		//��ȡ��ͼ�ؼ�
		mapView = (MapView) findViewById(R.id.base_map);
		
		//�õ���ͼ
		baiduMap = mapView.getMap();
		
		//���õ�ͼ����ʾ��Χ������15.0fָ����500�׵İ뾶��Χ
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(mapStatusUpdate);
	}
}
