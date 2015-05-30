package com.jackie.map;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jackie.map.MyOrientationListener.OnOrientationListener;
import com.jackie.map.bean.info;

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
	
	
	//���Ӹ�����ͼ��
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	
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
		
		//��ʼ�����������
		initMarker();
		
		
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				
				Bundle extraInfo = marker.getExtraInfo();
				info info = (info) extraInfo.getSerializable("info");
				ImageView iv = (ImageView) mMarkerLy
						.findViewById(R.id.id_info_img);
				TextView distance = (TextView) mMarkerLy
						.findViewById(R.id.id_info_distance);
				TextView name = (TextView) mMarkerLy
						.findViewById(R.id.id_info_name);
				TextView zan = (TextView) mMarkerLy
						.findViewById(R.id.id_info_zan);
				iv.setImageResource(info.getImgId());
				distance.setText(info.getDistance());
				name.setText(info.getName());
				zan.setText(info.getZan() + "");
				
				InfoWindow infoWindow;
				TextView tv = new TextView(context);
				tv.setBackgroundResource(R.drawable.location_tips);
				tv.setPadding(30, 20, 30, 50);
				tv.setText(info.getName());
				tv.setTextColor(Color.parseColor("#ffffff"));

				final LatLng latLng = marker.getPosition();
				Point p = baiduMap.getProjection().toScreenLocation(latLng);
				p.y -= 47;
				LatLng ll = baiduMap.getProjection().fromScreenLocation(p);

				infoWindow = new InfoWindow((View)tv, ll, 1);
				
				baiduMap.showInfoWindow(infoWindow);
				mMarkerLy.setVisibility(View.VISIBLE);
				
				
				mMarkerLy.setVisibility(View.VISIBLE);
				
				return true;
			}
		});
		
		baiduMap.setOnMapClickListener(new OnMapClickListener()
		{

			@Override
			public boolean onMapPoiClick(MapPoi arg0)
			{
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0)
			{
				mMarkerLy.setVisibility(View.GONE);
				
				baiduMap.hideInfoWindow();
				
			}
		});
	}
	
	private void initMarker() {
		
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
		
		mMarkerLy = (RelativeLayout) findViewById(R.id.id_maker_ly);
		
		
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
			
		case R.id.addOverlay:
			
			addOverlays(info.infos);
		}
		
		mMarkerLy.setVisibility(View.GONE);
		
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * ��Ӹ�����
	 * 
	 * @param infos
	 */
	private void addOverlays(List<info> infos)
	{
		baiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for (info info : infos)
		{
			// ��γ��
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// ͼ��
			options = new MarkerOptions().position(latLng).icon(mMarker)
					.zIndex(5);
			marker = (Marker) baiduMap.addOverlay(options);
			Bundle arg0 = new Bundle();
			arg0.putSerializable("info", info);
			marker.setExtraInfo(arg0);
		}

		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		baiduMap.setMapStatus(msu);

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
