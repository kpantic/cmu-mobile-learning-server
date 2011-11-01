package org.cmusv.ozprototyping;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends SuperActivity {
	
	String text = "";
	
	private void interpretCommand(String commandString){
		try{
			JSONObject command = new JSONObject(commandString);
			if(command.has("filename")){
				text = command.getString("filename");				
			}else{
				text = commandString;
			}			
		}catch(JSONException e){
			text = e.getMessage();
			return;
		}
	}
	
	public void onCreate(Bundle savedInstanceState){
		interpretCommand(this.getIntent().getStringExtra("command"));
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.video);

		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		view = videoView;
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		// Set video link (mp4 format )
		Uri video;		
		File sdDir = Environment.getExternalStorageDirectory();
		video = Uri.parse(sdDir.getAbsolutePath()+"/oz-prototyping/"+text);
		File videoFile = new File(video.toString());
		if(videoFile.exists() && !videoFile.isDirectory()){
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(video);
			videoView.start();
		}else{
			generateDialog("File "+videoFile.getAbsolutePath()+" not found");
			//this.finish();
		}
	} 
}
