package com.example.activities;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.musicplayer.helpers.ReaderHelper;
import com.musicplayer.service.SongService;

public class MainActivity extends Activity {

	private Button btn_back;
	private Button btn_start;
	private Button btn_pre;
	private Button btn_settings;
	private Button btn_scan;
	private Button btn_quit;
	private Button btn_stop;
	private SeekBar seekbar;

	private ArrayList<String> songs = new ArrayList<String>();
	private File[] song_paths;

	private RelativeLayout relative_menu;
	private ListView listview_song;

	private  int index = 0;
	
	private static Intent service;
	
	private boolean IS_QUIT = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniLayout();
		setButtons();
		preLoad();
		setListView();
	}

	private void iniLayout() {
		// TODO Auto-generated method stub
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_pre = (Button) findViewById(R.id.btn_pre);
		btn_settings = (Button) findViewById(R.id.btn_settings);
		btn_quit = (Button) findViewById(R.id.btn_quit);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		relative_menu = (RelativeLayout) findViewById(R.id.relative_menu);
		listview_song = (ListView) findViewById(R.id.listview_songs);

	}

	private void setButtons() {
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (index < 1) {
					SongService.setSong(songs.get(index));
				} else {
					SongService.setSong(songs.get(index - 1));
				}
				service = new Intent(MainActivity.this,
						SongService.class);
				startService(service);
				index--;
			}
		});

		btn_start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SongService.setSong(songs.get(index));
				Log.i("btn_start",songs.get(index));
				service = new Intent(MainActivity.this,
						SongService.class);
				startService(service);
				
			}
		});

		btn_pre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (index >= songs.size() - 1) {		//
					SongService.setSong(songs.get(0));
				} else {
					SongService.setSong(songs.get(index + 1));
				}
				service = new Intent(MainActivity.this,
						SongService.class);
				startService(service);
				index++;
			}
		});

		btn_settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		btn_scan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				preLoad();
				
			}
		});

		btn_quit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IS_QUIT = true;
				stopService(service); 
				finish();

			}
		});
		
		btn_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(service);
			}
		});

	}

	private void setListView() {
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, songs);
		
		

		listview_song.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				index = arg2;
				SongService.setSong(songs.get(index));
				Log.i("listview",songs.get(index));
				service = new Intent(MainActivity.this,
						SongService.class);
				startService(service);

			}
		});

	
		
		listview_song.setAdapter(adapter);
	}

	private void preLoad() {
		song_paths = new ReaderHelper().getSongs();
		int length = song_paths.length;
		for (int i = 0; i < length; i++) {
			songs.add(song_paths[i].getAbsolutePath());
		}
		Log.i("scan",songs.size()+"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add("");
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (!relative_menu.isShown()) {
			// 判断当RelativeLayout布局没有出来的时候让其出现
			relative_menu.setVisibility(View.VISIBLE);
		} else {
			// 否则让其消失
			relative_menu.setVisibility(View.GONE);
		}
		return false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(!IS_QUIT) {
			Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			//定义一个通知，需要设置其时间
			Notification notify = new Notification(R.drawable.ic_launcher,songs.get(index),1000);
			//定义一个PendintIntent,用来处理通知发送给谁，由谁发送的问题
			PendingIntent pi= PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this,MainActivity.class),PendingIntent.FLAG_ONE_SHOT);
			
			
			//定义消息
			notify.setLatestEventInfo(MainActivity.this, "musicplayer", songs.get(index), pi);
			
			//通知是由NotificationManager发送的
			manager.notify(0, notify);
		} 
		
		super.onStop();
	}

	
}
