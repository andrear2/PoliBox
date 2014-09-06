package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Date;

public class Log {
	private String logpath;

	public Log(String home_dir) {
		this.logpath = home_dir+"\\log.txt";
	}
	
	public int addLine (Long u_id, String type, String resource,int disp ) {
		int ln=0;
		System.out.println(resource);
		String[] path = resource.split("/");
		String pathUrl = new String();
		for (int i=5; i<path.length; i++) {
			if (i==5) {
				pathUrl += path[i];
			} else {
				pathUrl += "\\" + path[i];
			}
		}
		try {
			File file = new File(logpath);
			long len = file.length();
			FileReader fr = new FileReader(logpath);
			LineNumberReader lnr = new LineNumberReader (fr);
			lnr.skip(len);
			ln = lnr.getLineNumber()+1;
			System.out.println(len+":"+lnr.getLineNumber());
			lnr.close();
		    long now = new Date().getTime();
		    System.out.println(ln+":"+u_id+":"+type+":"+pathUrl+":"+disp+":"+now+"\n");
		    RandomAccessFile logFile = new RandomAccessFile(logpath,"rw");
			FileOutputStream fos = new FileOutputStream(logFile.getFD());
    		PrintStream log = new PrintStream(fos);
    		logFile.seek(logFile.length());
    		log.println(ln+":"+u_id+":"+type+":"+pathUrl+":"+disp+":"+now);
    		log.close();
    		logFile.close();


	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		return ln;
	}
	
	public int addLine(Long u_id, String type, String resource, int disp, Long u2_id) {
		int ln=0;
		System.out.println(resource);
		String[] path = resource.split("/");
		String pathUrl = new String();
		for (int i=5; i<path.length; i++) {
			if (i==5) {
				pathUrl += path[i];
			} else {
				pathUrl += "\\" + path[i];
			}
		}
		try {
			File file = new File(logpath);
			long len = file.length();
			FileReader fr = new FileReader(logpath);
			LineNumberReader lnr = new LineNumberReader (fr);
			lnr.skip(len);
			ln = lnr.getLineNumber()+1;
			System.out.println(len+":"+lnr.getLineNumber());
			lnr.close();
		    long now = new Date().getTime();
		    System.out.println(ln+":"+u_id+":"+type+":"+pathUrl+":"+disp+":"+now+":"+u2_id+"\n");
		    RandomAccessFile logFile = new RandomAccessFile(logpath,"rw");
			FileOutputStream fos = new FileOutputStream(logFile.getFD());
    		PrintStream log = new PrintStream(fos);
    		logFile.seek(logFile.length());
    		log.println(ln+":"+u_id+":"+type+":"+pathUrl+":"+disp+":"+now+":"+u2_id);
    		log.close();
    		logFile.close();


	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		return ln;
		
	}
}