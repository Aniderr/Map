package com.jackie.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 程序主界面
 * @author jackie
 *
 */
public class MainActivity extends Activity {
	
	private Button start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		init();
		
		start.setOnClickListener(new MyOnClickListener());
	}

	private void init() {
		
		start = (Button) findViewById(R.id.start);
	}
	
	class MyOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.start:
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MapInfoActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	}
}
