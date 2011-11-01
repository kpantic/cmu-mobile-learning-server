package org.cmusv.ozprototyping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

public class FileObserverService extends Service {

	private VNCFileObserver observer;
	public static final String BROADCAST_ACTION = "org.cmusv.ozprototyping.filechange";
	public String text = "";
	Intent intent;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class VNCFileObserver extends FileObserver{

		public FileObserverService fbs;
		public VNCFileObserver(String path) {
			super(path);
			// TODO Auto-generated constructor stub
		}
		
		public VNCFileObserver(String path, FileObserverService fbs){
			super(path);
			this.fbs = fbs;
		}

		/**
		  * Fetch the entire contents of a text file, and return it in a String.
		  * This style of implementation does not throw Exceptions to the caller.
		  *
		  * @param aFile is a file which already exists and can be read.
		  */
		public String getContents(File aFile) {
			//...checks on aFile are elided
			StringBuilder contents = new StringBuilder();
		    
		    try {
		      //use buffering, reading one line at a time
		      //FileReader always assumes default encoding is OK!
		      BufferedReader input =  new BufferedReader(new FileReader(aFile));
		      try {
		        String line = null; //not declared within while loop
		        /*
		        * readLine is a bit quirky :
		        * it returns the content of a line MINUS the newline.
		        * it returns null only for the END of the stream.
		        * it returns an empty String if two newlines appear in a row.
		        */
		        while (( line = input.readLine()) != null){
		          contents.append(line);
		          contents.append(System.getProperty("line.separator"));
		        }
		      }
		      finally {
		        input.close();
		      }
		    }
		    catch (IOException ex){
		      ex.printStackTrace();
		    }
		    
		    return contents.toString();
		}
		
		@Override
		public void onEvent(int event, String filestr) {	
			if(event == FileObserver.MODIFY){
				File fileHandler;
			
				fileHandler = new File("/data/data/org.cmusv.ozprototyping/files/messages");
				String contents = getContents(fileHandler);
										
				this.fbs.text = contents;
				this.fbs.sendUpdatesToUI();
			}
		}	
	}


    @Override
    public void onCreate() 
    {
    	super.onCreate();
    	
		observer = new VNCFileObserver(getFilesDir().getAbsolutePath() + "/messages",this);
		observer.startWatching();
		
		intent = new Intent(BROADCAST_ACTION);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
    }
    
    public void sendUpdatesToUI() {
    	intent.putExtra("text", this.text);
    	sendBroadcast(intent);
    };  
    
    public void onDestroy()
    {
    	observer.stopWatching();
    	super.onDestroy();
    }

}
