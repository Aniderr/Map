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
 * 地图详情信息页面
 * @author Jackie
 *
 */
public class MapInfoActivity extends Activity{

	private MapView mapView;
	private BaiduMap baiduMap;
	
	// 定位相关
	private Context context;
	
	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	
	
	private MyOrientationListener myOrientationListener;
	
	
	//记录当前x位置
	private float currentX;
	
	//自定义图标
	private BitmapDescriptor mIconLocation;
	
	//切换模式变量
	private LocationMode locationModel;
	
	
	//增加覆盖物图标
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        
        //去掉应用title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);
		
		this.context = this;
		initView();
	
		//初始化定位
		initLocation();
		
		//初始化覆盖物相关
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

	//初始化定位
	private void initLocation() {
		
		locationModel = locationModel.NORMAL;
		
		mLocationClient = new LocationClient(context);
		mLocationListener = new MyLocationListener();
		
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		//1秒请求一次
		option.setScanSpan(1000);
		
		mLocationClient.setLocOption(option);
		
		//初始化图标
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
	 * 初始化
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
			
			//设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					locationModel, true, mIconLocation);
			
			baiduMap.setMyLocationConfigeration(config);
			
			//判断是否为第一次定位
			if(isFirstIn){
				
				//获取经纬度
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
		
		//开启定位
		if(!mLocationClient.isStarted()){			
			mLocationClient.start();
		}
		
		//开启方向传感器
		myOrientationListener.start();
	}
	 
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//停止定位
		baiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		
		//关闭方向传感器
		myOrientationListener.stop();
	}
	@Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
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
	    
	
	/**
	 * 手机菜单键
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
			
			//首先判断交通流量是否开启，如果开启就关闭，反之开启
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
	 * 添加覆盖物
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
			// 经纬度
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// 图标
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
	 * 定位
	 */
	private void fixedLocation(double mLatitude, double mLongtitude) {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		
		baiduMap.animateMapStatus(msu);
	}
	
}
