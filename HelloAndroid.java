package com.example.helloandroid;

import java.io.DataOutputStream;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class HelloAndroid extends Activity implements SeekBar.OnSeekBarChangeListener {
    SeekBar mSeekBarG;
    SeekBar mSeekBarY;
    SeekBar mSeekBarR;
    int greenProgress;
    int yellowProgress;
    int redProgress;
    final Runtime runtime = Runtime.getRuntime();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        

        mSeekBarG = (SeekBar)findViewById(R.id.mySeekBar1);
        mSeekBarG.setOnSeekBarChangeListener(this);
        mSeekBarG.setMax(255);
        mSeekBarG.setProgress(0);

        mSeekBarY = (SeekBar)findViewById(R.id.mySeekBar2);
        mSeekBarY.setOnSeekBarChangeListener(this);
        mSeekBarY.setMax(255);
        mSeekBarY.setProgress(0);
        
        mSeekBarR = (SeekBar)findViewById(R.id.mySeekBar3);
        mSeekBarR.setOnSeekBarChangeListener(this);
        mSeekBarR.setMax(255);
        mSeekBarR.setProgress(0);
        
        Process suProcess = null;
        try {
        	suProcess = Runtime.getRuntime().exec("/system/xbin/su");
        	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
        	dos.writeBytes("cat /dev/ttyUSB0\n");
        	dos.flush();
       	} catch (Exception ex) {
       		Log.d("rootcmd", "Unexpected error - Here is what I know: "+ex.getMessage());
        	suProcess = null;
       	}        
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    	if (seekBar == mSeekBarG) {
    		greenProgress = progress;
    	} else if (seekBar == mSeekBarY) {
    		yellowProgress = progress;
    	} else if (seekBar == mSeekBarR) {
    		redProgress = progress;
    	}
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
		String cmdStr;
		cmdStr = "0,"+Integer.toString(greenProgress)+";";
		cmdStr += "1,"+Integer.toString(yellowProgress)+";";
		cmdStr += "2,"+Integer.toString(redProgress)+";";

        Process suProcess = null;
        try {
        	suProcess = Runtime.getRuntime().exec("/system/xbin/su");
        	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
        	dos.writeBytes("echo \""+cmdStr+"\" > /dev/ttyUSB0\n");
        	dos.flush();
       	} catch (Exception ex) {
       		Log.d("rootcmd", "Unexpected error - Here is what I know: "+ex.getMessage());
        	suProcess = null;
       	}        
				
    }
    
    protected void onDestroy(){
    }
}