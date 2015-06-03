package com.jackie.map;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * POI兴趣检索定位
 * @author jackie
 *
 */
public class PoiSearchActivity extends Activity{

	private BaiduMap baiduMap;
	private MapView mapView;
	
	//poi相关
	private PoiSearch poiSearch;
	
	private EditText city;
	private EditText key;
	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        
        setContentView(R.layout.poisearch);
        
		init();
		
		Toast.makeText(getApplicationContext(), "ok", 0).show();
		
		//创建POI检索实例 
	    poiSearch = PoiSearch.newInstance();
	    
	    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){  
	        public void onGetPoiResult(PoiResult result){  
	        //获取POI检索结果  
	        	
	        	List<PoiInfo> infos = result.getAllPoi();
	        	if(infos == null){
	        		Toast.makeText(getApplicationContext(), "没有相关结果", 0).show();
	        	}else{
	        		
	        		//首先清空地图
	        		baiduMap.clear();

        			for (int i = 0; i < infos.size(); i++) {
						
        				if(i>9){
        					continue;
        				}
        				LatLng latLng = infos.get(i).location;
        				
        				//在搜索到的地图上增加定位覆盖物
    	        		baiduMap.addOverlay(new MarkerOptions().position(latLng)
    	        				.icon(BitmapDescriptorFactory
    	        						.fromResource(R.drawable.maker)));
    	        		
    	        		//设置地图状态
    	        		baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
    	        		
					}
	        	}
	        }  
	        public void onGetPoiDetailResult(PoiDetailResult result){  
	        //获取Place详情页检索结果  
	        }  
	    };
	    
	    poiSearch.setOnGetPoiSearchResultListener(poiListener);
	    
	    
	    btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Toast.makeText(getApplicationContext(), "o", 0).show();
				 poiSearch.searchInCity((new PoiCitySearchOption())  
				    	    .city(city.getText().toString())  
				    	    .keyword(key.getText().toString())  
				    	    .pageNum(10));
			}
		});
	    
	}

	
	//初始化各个控件
	private void init() {
		
		mapView = (MapView) findViewById(R.id.bmapView);
		
		baiduMap = mapView.getMap();
		
		city = (EditText) findViewById(R.id.poi_city);
		
		key = (EditText) findViewById(R.id.poi_key);
		
		btn = (Button) findViewById(R.id.poi_search);
		
	}
	
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        poiSearch.destroy();
        mapView.onDestroy(); 
    }
	 
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mapView.onResume();  
    }
    
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mapView.onPause();  
    }
	
}
