package it.polito.ai.polibox.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Date;

import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CreaCartellaController {
	@RequestMapping(value = "/creaCartella", method = RequestMethod.POST)
	public String creaCartellaSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="pathCartella") String path, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		String[] pathElements = path.split("/");
		String pathDir = utente.getHome_dir();
		String pathUrl = new String();
		for (int i=5; i<pathElements.length; i++) {
			if (i==5) {
				pathUrl += pathElements[i];
			} else {
				pathUrl += "\\" + pathElements[i];
			}
		}
		pathDir += "\\" + pathUrl;
		File dir = new File(pathDir + "\\" + nome);
		if (!dir.isDirectory()) {
			dir.mkdir();
			//aggiorno il log file con l'azione appena compiuta
			String logpath = utente.getHome_dir()+"\\log.txt";
			try {
				File file = new File(logpath);
				long len = file.length();
				FileReader fr = new FileReader(logpath);
				LineNumberReader lnr = new LineNumberReader (fr);
				lnr.skip(len);
				int ln = lnr.getLineNumber()+1;
				System.out.println(len+":"+lnr.getLineNumber());
				lnr.close();
			    long now = new Date().getTime();
			    System.out.println(ln+":"+utente.getId()+":ND:"+dir.getName()+":WEB:"+now+"\n");
			    RandomAccessFile logFile = new RandomAccessFile(logpath,"rw");
    			FileOutputStream fos = new FileOutputStream(logFile.getFD());
	    		PrintStream log = new PrintStream(fos);
	    		logFile.seek(logFile.length());
	    		log.println(ln+":"+utente.getId()+":ND:"+dir.getName()+":WEB:"+now);
	    		log.close();
	    		logFile.close();


		    } catch (IOException e) {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    }
		} else {
			for (int i=1; ;i++) {
				dir = new File(pathDir + "\\" + nome + "(" + i + ")");
				if (!dir.isDirectory()) {
					dir.mkdir();
					//aggiorno il log file con l'azione appena compiuta
					String logpath = utente.getHome_dir()+"\\log.txt";
					try {
						File file = new File(logpath);
						long len = file.length();
						FileReader fr = new FileReader(logpath);
						LineNumberReader lnr = new LineNumberReader (fr);
						lnr.skip(len);
						int ln = lnr.getLineNumber()+1;
						System.out.println(len+":"+lnr.getLineNumber());
						lnr.close();
					    long now = new Date().getTime();
					    System.out.println(ln+":"+utente.getId()+":ND:"+dir.getName()+":WEB:"+now+"\n");
					    RandomAccessFile logFile = new RandomAccessFile(logpath,"rw");
		    			FileOutputStream fos = new FileOutputStream(logFile.getFD());
			    		PrintStream log = new PrintStream(fos);
			    		logFile.seek(logFile.length());
			    		log.println(ln+":"+utente.getId()+":ND:"+dir.getName()+":WEB:"+now);
			    		log.close();
			    		logFile.close();


				    } catch (IOException e) {
				    	// TODO Auto-generated catch block
				    	e.printStackTrace();
				    }
					
					break;
				}
			}
		}
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		redirectAttrs.addFlashAttribute("msg", "Cartella \"" + dir.getName() + "\" creata con successo");
		redirectAttrs.addFlashAttribute("msgClass", "success");
		if (pathUrl.isEmpty()) {
			return "redirect:home";
		}
		return "redirect:home/" + pathUrl;
	}
}
