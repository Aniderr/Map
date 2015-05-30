package com.jackie.map.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jackie.map.R;

/**
 * 覆盖物处商家实体bean
 * 
 * @author jackie
 * 
 */
public class info implements Serializable{

	private static final long serialVersionUID = 7326131555684602824L;

	private double latitude;
	
	private double longitude;
	
	private int imgId;
	
	private String name;
	
	private String distance;
	
	private int zan;

	
	public static List<info> infos = new ArrayList<info>();
	
	
	static{
		
		infos.add(new info(34.242652, 108.971171, R.drawable.logo, "xxx",  "距离n米", 1456));
		
	}
	
	
	public info(double latitude, double longitude, int imgId, String name,
			String distance, int zan) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

}
