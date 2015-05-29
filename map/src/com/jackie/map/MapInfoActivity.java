package com.jackie.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jackie.map.MyOrientationListener.OnOrientationListener;

/**
 * ��ͼ������Ϣҳ��
 * @author Jackie
 *
 */
public class MapInfoActivity extends Activity{

	private MapView mapView;
	private BaiduMap baiduMap;
	
	// ��λ���
	private Context context;
	
	// ��λ���
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	
	
	private MyOrientationListener myOrientationListener;
	
	
	//��¼��ǰxλ��
	private float currentX;
	
	//�Զ���ͼ��
	private BitmapDescriptor mIconLocation;
	
	//�л�ģʽ����
	private LocationMode locationModel;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        
        //ȥ��Ӧ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);
		
		this.context = this;
		initView();
	
		//��ʼ����λ
		initLocation();
	}

	//��ʼ����λ
	private void initLocation() {
		
		locationModel = locationModel.NORMAL;
		
		mLocationClient = new LocationClient(context);
		mLocationListener = new MyLocationListener();
		
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		//1������һ��
		option.setScanSpan(1000);
		
		mLocationClient.setLocOption(option);
		
		//��ʼ��ͼ��
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		
		myOrientationListener = new MyOrientationListener(context);
		
		
		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			
			@Override
			public void OnOrientationChanged(float x) {
				
				currentX = x;
			}
		});
	}


	/**
	 * ��ʼ��
	 */
	 private void initView() {
		 
		 mapView = (MapView) findViewById(R.id.bmapView);
		 baiduMap = mapView.getMap();
		 
		 MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
		 baiduMap.setMapStatus(mapStatusUpdate);
	}


	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			MyLocationData data = new MyLocationData.Builder()
			.direction(currentX)
			.accuracy(location.getRadius())
			.latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();
			
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			
			baiduMap.setMyLocationData(data);
			
			//�����Զ���ͼ��
			MyLocationConfiguration config = new MyLocationConfiguration(
					locationModel, true, mIconLocation);
			
			baiduMap.setMyLocationConfigeration(config);
			
			//�ж��Ƿ�Ϊ��һ�ζ�λ
			if(isFirstIn){
				
				//��ȡ��γ��
				LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				
				baiduMap.animateMapStatus(msu);
				
				isFirstIn = false;
				
				Toast.makeText(context, location.getAddrStr(), Toast.LENGTH_LONG).show();
			}
		}
		 
	 }
	
	@Override
	protected void onStart() {
		super.onStart();
		
		baiduMap.setMyLocationEnabled(true);
		
		//������λ
		if(!mLocationClient.isStarted()){			
			mLocationClient.start();
		}
		
		//�������򴫸���
		myOrientationListener.start();
	}
	 
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//ֹͣ��λ
		baiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		
		//�رշ��򴫸���
		myOrientationListener.stop();
	}
	@Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
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
	    
	
	/**
	 * �ֻ��˵���
	 * @author jackie
	 * @date 2015-05-24
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.common:
			
			baiduMap.setMapType(baiduMap.MAP_TYPE_NORMAL);
			break;
			
		case R.id.traffic:
			
			//�����жϽ�ͨ�����Ƿ�������������͹رգ���֮����
			if(baiduMap.isTrafficEnabled()){
				baiduMap.setTrafficEnabled(false);
				item.setTitle(R.string.open_traffic);
				
			}else{
				baiduMap.setTrafficEnabled(true);
				item.setTitle(R.string.close_traffic);
			}
			break;
			
		case R.id.sate:
			
			baiduMap.setMapType(baiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.myLocation:
			
			fixedLocation(mLatitude,mLongtitude);
			break;
		case R.id.normal:
			
			locationModel = LocationMode.NORMAL;
			break;
		case R.id.follow:
			
			locationModel = LocationMode.FOLLOWING;
			break;
		case R.id.compass:
			
			locationModel = LocationMode.COMPASS;
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * ��λ
	 */
	private void fixedLocation(double mLatitude, double mLongtitude) {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		
		baiduMap.animateMapStatus(msu);
	}
}
