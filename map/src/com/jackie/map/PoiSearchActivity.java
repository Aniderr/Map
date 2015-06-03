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
 * POI��Ȥ������λ
 * @author jackie
 *
 */
public class PoiSearchActivity extends Activity{

	private BaiduMap baiduMap;
	private MapView mapView;
	
	//poi���
	private PoiSearch poiSearch;
	
	private EditText city;
	private EditText key;
	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        
        setContentView(R.layout.poisearch);
        
		init();
		
		Toast.makeText(getApplicationContext(), "ok", 0).show();
		
		//����POI����ʵ�� 
	    poiSearch = PoiSearch.newInstance();
	    
	    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){  
	        public void onGetPoiResult(PoiResult result){  
	        //��ȡPOI�������  
	        	
	        	List<PoiInfo> infos = result.getAllPoi();
	        	if(infos == null){
	        		Toast.makeText(getApplicationContext(), "û����ؽ��", 0).show();
	        	}else{
	        		
	        		//������յ�ͼ
	        		baiduMap.clear();

        			for (int i = 0; i < infos.size(); i++) {
						
        				if(i>9){
        					continue;
        				}
        				LatLng latLng = infos.get(i).location;
        				
        				//���������ĵ�ͼ�����Ӷ�λ������
    	        		baiduMap.addOverlay(new MarkerOptions().position(latLng)
    	        				.icon(BitmapDescriptorFactory
    	        						.fromResource(R.drawable.maker)));
    	        		
    	        		//���õ�ͼ״̬
    	        		baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
    	        		
					}
	        	}
	        }  
	        public void onGetPoiDetailResult(PoiDetailResult result){  
	        //��ȡPlace����ҳ�������  
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

	
	//��ʼ�������ؼ�
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
        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
        poiSearch.destroy();
        mapView.onDestroy(); 
    }
	 
    @Override  
    protected void onResume() {  
        super.onResume();  
        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
        mapView.onResume();  
    }
    
    @Override  
    protected void onPause() {  
        super.onPause();  
        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
        mapView.onPause();  
    }
	
}
