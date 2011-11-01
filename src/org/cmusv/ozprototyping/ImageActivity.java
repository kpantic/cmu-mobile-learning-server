package org.cmusv.ozprototyping;

import java.io.File;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageActivity extends SuperActivity {
	
	String text = "";
	JSONObject command;
	Vector<Rectangle> buttons = new Vector<Rectangle>();
	ImageView view;
	boolean moved;
	float pressedX;
	float pressedY;
	float imageWidth;
	float imageHeight;
	float scaleX;
	float scaleY;
	
	private void interpretTouch(float x, float y){
	    if(scaleX == 0 || scaleY == 0){
	    	scaleX = 1;
	    	scaleY = 1;
			if(imageHeight > view.getHeight()){
		    	scaleY = (float)imageHeight/view.getHeight();
		    }
		    if(imageWidth > view.getWidth()){
		    	scaleX = (float)imageWidth/view.getWidth();
		    }
	    }
		for (Rectangle b: buttons){
			if(b.isIn(x*scaleX,y*scaleY) && (!moved || b.isIn(pressedX, pressedY))){
				/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("X: "+x+" Y: "+y);
				AlertDialog alert = builder.create();
				alert.show();*/
				Intent action = new Intent();
				action.putExtra("text", b.action);
				updateUI(action);
			}
		}			
	}
	
	private OnTouchListener touchPosListener = new OnTouchListener(){
		public boolean onTouch(View v, MotionEvent event) {
			Log.d("TAG", "Event: "+event.getAction());
			switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					moved = false;
					pressedX = event.getX();
					pressedY = event.getY();
					v.invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					moved = true;
					v.invalidate();
					break;				
				case MotionEvent.ACTION_UP:					
					interpretTouch(event.getX(), event.getY());
					v.invalidate();
					break;				
			}						
			return true;
		}		
	};
	
	private void interpretCommand(String commandString){
		try{
			command = new JSONObject(commandString);
			if(command.has("filename")){
				text = command.getString("filename");				
			}else{
				text = commandString;
			}
			if(command.has("buttons")){
				JSONArray commandButtons = command.getJSONArray("buttons");
				for(int i = 0; i < commandButtons.length(); i++){
					JSONObject button = commandButtons.getJSONObject(i);
					if(button.has("x") && button.has("y") 
							&& button.has("width") && button.has("height")
							&& button.has("action")){
						buttons.add(new Rectangle(button.getDouble("x"),
								button.getDouble("y"), button.getDouble("width"),
								button.getDouble("height"), button.getString("action")));
					}
				}
			}
		}catch(JSONException e){
			text = e.getMessage();
			return;
		}
	}
	
	
	public void onCreate(Bundle savedInstanceState){
		interpretCommand(this.getIntent().getStringExtra("command"));
		super.onCreate(savedInstanceState);

		setContentView(R.layout.image);		
		
		File sdDir = Environment.getExternalStorageDirectory();
		File imgFile = new File(sdDir.getAbsolutePath()+"/oz-prototyping/"+text);
		if(imgFile.exists() && !imgFile.isDirectory()){

		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());		    
		    imageWidth = myBitmap.getWidth();
		    imageHeight = myBitmap.getHeight();		   

		    view = (ImageView) findViewById(R.id.imageView1);		    		   		    
			view.setOnTouchListener(touchPosListener);
		    view.setImageBitmap(myBitmap);		    

		}else{
			generateDialog("File "+text+" not found");
			//this.finish();
		}		
	} 
}
