package com.twh.showmemory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
     * 计算已使用内存的百分比
     *
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "无结果";
    }
 
    /**
     * 获取当前可用内存，返回数据以字节为单位。
      
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

	private static ActivityManager getActivityManager(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//get memory
	public static String getMemoryInfo(Context context) {  
        StringBuffer strBuf = new StringBuffer();  
        ActivityManager actMgr = (ActivityManager) context  
                .getSystemService(Context.ACTIVITY_SERVICE);  
        android.app.ActivityManager.MemoryInfo memoryinfo = new android.app.ActivityManager.MemoryInfo();  
        actMgr.getMemoryInfo(memoryinfo);  
        strBuf.append("/nTotal Available Memory :");  
        long l = memoryinfo.availMem >> 10;  
        strBuf.append(l).append("k");  
        strBuf.append("/nTotal Available Memory :");  
        long l1 = memoryinfo.availMem >> 20;  
        strBuf.append(l1).append("M");  
        strBuf.append("/nIn low memory situation:");  
        boolean flag = memoryinfo.lowMemory;  
        strBuf.append(flag);  
        String[] args = { "/system/bin/cat", "/proc/meminfo" };  
        strBuf.append(exec(args));  
        return strBuf.toString();  
    }  
	
	public static String exec(String[] args) {  
        String result = "";  
        ProcessBuilder processBuilder = new ProcessBuilder(args);  
        Process process = null;  
        InputStream is = null;  
        try {  
            process = processBuilder.start();  
            is = process.getInputStream();  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            int read = -1;  
            while ((read = is.read()) != -1) {  
                baos.write(read);  
            }  
            byte[] data = baos.toByteArray();  
            result = new String(data);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (process != null) {  
                process.destroy();  
            }  
        }  
        return result;  
    }  
}
