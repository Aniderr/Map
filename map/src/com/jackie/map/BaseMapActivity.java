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

	//声明地图控件信息
	private MapView mapView;
	private BaiduMap baiduMap;
	
	
	//声明搜索到的经纬度信息
	private double latitude;
	private double longitude;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        
        setContentView(R.layout.base_map);
        
        
        //通过intent获取调用Activity所传参数，这里主要获取到搜索到的经纬度信息
        Intent intent = getIntent();
        
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        LatLng latLng = new LatLng(latitude, longitude);
        
        //调用初始化方法出初始化控件信息以及地图的基础信息
        init();
		
        //首先清空地图
        baiduMap.clear();
        
        //在搜索到的地图上增加定位覆盖物
		baiduMap.addOverlay(new MarkerOptions().position(latLng)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.maker)));
		
		//设置地图状态
		baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
		
		//获取经纬度字符串信息，toast在界面上
		String strInfo = String.format("纬度：%f 经度：%f",
				latitude, longitude);
		
		Toast.makeText(BaseMapActivity.this, strInfo, Toast.LENGTH_LONG).show();
	}

	/**
	 * 初始化所需的各个控件
	 */
	private void init() {
		
		//获取地图控件
		mapView = (MapView) findViewById(R.id.base_map);
		
		//得到地图
		baiduMap = mapView.getMap();
		
		//设置地图的显示范围，这里15.0f指的是500米的半径范围
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(mapStatusUpdate);
	}
}
