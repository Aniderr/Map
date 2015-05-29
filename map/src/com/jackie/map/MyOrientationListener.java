package com.jackie.map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * ���򴫸���
 * 
 * @author jackie
 * @date 2015-05-24
 * 
 */
public class MyOrientationListener implements SensorEventListener {

	private SensorManager sensorManager;

	private Context context;

	private Sensor sensor;

	// ����x������任����
	private float lastX;

	public MyOrientationListener(Context context) {
		this.context = context;
	}

	/**
	 * ��ʼ����
	 */
	public void start() {
		
		//��ȡϵͳ����������
		sensorManager = (SensorManager) context
				.getSystemService(context.SENSOR_SERVICE);
		
		if(sensorManager != null){
			
			//��÷��򴫸���
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		
		//�ж��豸�Ƿ�֧�ַ��򴫸���
		if(sensor != null){
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		}
	}

	/**
	 * ��������
	 */
	public void stop() {
		sensorManager.unregisterListener(this);
	}

	//���ȸı�
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	//�������仯
	@Override
	public void onSensorChanged(SensorEvent event) {

		//����������Ƿ��򴫸���
		if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
			 
			//��ȡ����仯
			float x = event.values[SensorManager.DATA_X];
			
			//�������ı����1�Ⱦ�֪ͨ�������ui
			if(Math.abs(x - lastX) > 1.0){
				
				//��������ڿգ����ʾ�Ѿ�ע����
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
