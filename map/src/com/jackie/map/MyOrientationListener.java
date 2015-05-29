package com.jackie.map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 方向传感器
 * 
 * @author jackie
 * @date 2015-05-24
 * 
 */
public class MyOrientationListener implements SensorEventListener {

	private SensorManager sensorManager;

	private Context context;

	private Sensor sensor;

	// 监听x轴坐标变换变量
	private float lastX;

	public MyOrientationListener(Context context) {
		this.context = context;
	}

	/**
	 * 开始监听
	 */
	public void start() {
		
		//获取系统传感器服务
		sensorManager = (SensorManager) context
				.getSystemService(context.SENSOR_SERVICE);
		
		if(sensorManager != null){
			
			//获得方向传感器
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		
		//判断设备是否支持方向传感器
		if(sensor != null){
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		}
	}

	/**
	 * 结束监听
	 */
	public void stop() {
		sensorManager.unregisterListener(this);
	}

	//经度改变
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	//方向发生变化
	@Override
	public void onSensorChanged(SensorEvent event) {

		//如果传感器是方向传感器
		if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
			 
			//获取横向变化
			float x = event.values[SensorManager.DATA_X];
			
			//如果方向改变大于1度就通知界面更显ui
			if(Math.abs(x - lastX) > 1.0){
				
				//如果不等于空，这表示已经注册了
				if(onOrientationListener != null){
					onOrientationListener.OnOrientationChanged(x);
				}
			}
			
			lastX = x;
		}
	}
	
	
	private OnOrientationListener onOrientationListener;
	
	public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
		this.onOrientationListener = onOrientationListener;
	}

	public interface OnOrientationListener{
		void OnOrientationChanged(float x);
	}

}
