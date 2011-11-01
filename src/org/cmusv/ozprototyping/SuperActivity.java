package org.cmusv.ozprototyping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class SuperActivity extends Activity{
	View view;
	AlertDialog startDialog;
	
	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateUI(intent);       
        }
    };     
	
    protected void updateUI(Intent intent) {    	
    	String[] commands = intent.getStringExtra("text").split("\n");
    	for (String c : commands){    		
    		int pos = c.indexOf("###");
    		if(pos != -1){
	    		String type = c.substring(0, pos);
	    		String command = c.substring(pos+3);
	    		if (type.equals("video")){
	    			Intent videoIntent = new Intent(this,VideoActivity.class);
	    			videoIntent.putExtra("command", command);
	    			this.startActivity(videoIntent);
	    		}else if (type.equals("image")){
	    			Intent imageIntent = new Intent(this,ImageActivity.class);
	    			imageIntent.putExtra("command", command);
	    			this.startActivity(imageIntent);
	    		}else if(type.equals("alertdialog")){
	    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    			builder.setMessage(command);
	    			AlertDialog alert = builder.create();
	    			alert.show();
	    		}
    		}
    	}
    }
    
	public void generateDialog(String text){
		if(this.startDialog == null){			
			this.startDialog = new AlertDialog.Builder(this).create();
		}
		if(text == null){
			text = "HELLO WORLD";
		}
		this.startDialog.setMessage(text);
		this.startDialog.show();
	}	

	public void onResume(){
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(FileObserverService.BROADCAST_ACTION));
	}
	
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}	
}
