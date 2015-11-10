package com.example.gps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author liuzhidong
 * @create date 2012-08
 * @modifydate 2012.09.24
 */
public class GPSActivity extends Activity {

	private TextView tv_gps, tv_satellites;
	private Button bt_Quit;
	LocationManager locationManager;
	private StringBuilder sb;
	private StringBuilder locResult;
	private StringBuilder satInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);

		tv_satellites = (TextView)this.findViewById(R.id.tv_satellites);
		tv_gps = (TextView) this.findViewById(R.id.tv_gps);
		bt_Quit = (Button) this.findViewById(R.id.bt_quit_gps);

		openGPSSettings();

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		String provider = LocationManager.GPS_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
		updateMsg(location);

		LocationListener ll = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				String locInfo = updateMsg(location);
				DswLog.e("Info", locResult.toString());
				tv_gps.setText(null);
				tv_gps.setText(locInfo);
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

		};

		//1秒1*1000，最小1米上报
		locationManager.requestLocationUpdates(provider, 1000, 1, ll);

		bt_Quit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GPSActivity.this.finish();
			}
		});

		locationManager.addGpsStatusListener(statusListener);
	}

	private String updateMsg(Location loc) {
		sb = null;
		sb = new StringBuilder("位置信息：\n");
		
		locResult = null;
		locResult = new StringBuilder("loc "); 
		if (loc != null) {
			double lat = loc.getLatitude();
			double lng = loc.getLongitude();

			sb.append("纬度：" + lat + "\n经度：" + lng);
			locResult.append(lat + " " + lng + " ");

			if (loc.hasAccuracy()) {
				sb.append("\n精度：" + loc.getAccuracy());
				locResult.append(loc.getAccuracy()+" ");
			}
			else
			{
				locResult.append(" ");
			}

			if (loc.hasAltitude()) {
				sb.append("\n海拔：" + loc.getAltitude() + "m");
				locResult.append(loc.getAltitude()+" ");
			}
			else
			{
				locResult.append(" ");
			}

			if (loc.hasBearing()) {// 偏离正北方向的角度
				sb.append("\n方向：" + loc.getBearing());
				locResult.append(loc.getBearing()+" ");
			}
			else
			{
				locResult.append(" ");
			}
			
			if (loc.hasSpeed()) {
				if (loc.getSpeed() * 3.6 < 1) {
					sb.append("\n速度：0.0km/h");
				} else {
					sb.append("\n速度：" + loc.getSpeed() * 3.6 + "km/h");
				}
				
				locResult.append(loc.getSpeed()+" ");
			}
			else
			{
				locResult.append(" ");
			}
			
		} else {
			sb.append("没有位置信息！");
		}

		return sb.toString();
	}

	private void openGPSSettings() {
		LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
			return;
		}

		Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			GPSActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 卫星状态监听器
	 */
	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号
	
	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
			LocationManager locationManager = (LocationManager) GPSActivity.this.getSystemService(Context.LOCATION_SERVICE);
			GpsStatus status = locationManager.getGpsStatus(null); //取当前状态
			String satelliteInfo = updateGpsStatus(event, status);
			DswLog.e("SatNums", satInfo.toString());
			tv_satellites.setText(null);
			tv_satellites.setText(satelliteInfo);
		}
	};

	private String updateGpsStatus(int event, GpsStatus status) {
		satInfo = null;
		satInfo = new StringBuilder(" ");
		StringBuilder sb2 = new StringBuilder("");
		if (status == null) {
			sb2.append("搜索到卫星个数：" +0);
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			numSatelliteList.clear();
			int count = 0;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				numSatelliteList.add(s);
				count++;
			}
			sb2.append("搜索到卫星个数：" + numSatelliteList.size());
			
			satInfo.append(numSatelliteList.size()+" ");
		}
		
		return sb2.toString();
	}
}

