package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.SincronizzazioniPendentiDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.SincronizzazioniPendenti;
import it.polito.ai.polibox.entity.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController implements CheckConnection {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	SincronizzazioniPendentiDAO sincDAO;
	
	private File dest;
	
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public String fileUploadSubmit(@RequestParam(value="files") List<MultipartFile> uploadedFiles, @RequestParam(value="pathFile") String path, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		System.out.println(path);
		String[] pathElements = path.replace("%20", " ").split("/");
		String pathDir = new String();
		Utente owner = new Utente();
		Condivisione condivisione = new Condivisione ();
		String pathUrl = new String();
		String pathLog = new String();
		String condName = new String();
		if (cond == 1) {
			// creazione in una cartella condivisa
			condivisione = condivisioneDAO.getCondivisioneWithoutTrans((Long) session.getAttribute("cId"));
			owner = utenteDAO.getUtenteWithoutTrans(condivisione.getOwnerId());
			pathDir = condivisione.getDirPath();
			condName = pathElements[5];
			for (int i=6; i<pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
		} else {
			pathDir = utente.getHome_dir();
			for (int i=5; i<pathElements.length; i++) {
				if (i==5) {
					pathUrl += pathElements[i];
				} else {
					pathUrl += "\\" + pathElements[i];
				}
			}
			pathDir += "\\Polibox\\" + pathUrl;
		}
		List<String> fileNames = new ArrayList<String>();
		  
		if (uploadedFiles == null || uploadedFiles.size() == 0 || uploadedFiles.get(0).getOriginalFilename() == "") {
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			redirectAttrs.addFlashAttribute("msg", "Nessun file selezionato");
			redirectAttrs.addFlashAttribute("msgClass", "error");
			if (pathUrl.isEmpty()) {
				return "redirect:home";
			}
			if (cond == 1) {
				return "redirect:Home/" + pathUrl.replace("\\", "/");
			} else {
				return "redirect:home/" + pathUrl.replace("\\", "/");
			}
		}
		
		Log log = new Log(utente.getHome_dir());
		for (MultipartFile file: uploadedFiles) {
			String fileName = file.getOriginalFilename();
			fileNames.add(fileName);
			dest = new File(pathDir + "\\" + fileName);
			try {
				file.transferTo(dest);
				if(cond==1) {
					Log owner_log = new Log(owner.getHome_dir());
					String[] p = condivisione.getDirPath().split("\\\\");
					int flag=0;
					String pp = new String("http://localhost:8080/ai/home");
					for (int i=0;i<p.length;i++) {
						if (flag==1) pp += "/"+p[i];
						if (p[i].equals("Polibox")) flag=1;
					}
					log.addLine(utente.getId(), "NF","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+fileName , 0, owner.getId());
					owner_log.addLine(owner.getId(), "NF",pp+pathLog+"/"+fileName , 0, utente.getId());
				} else {
					log.addLine(utente.getId(), "NF",path+"/"+fileName , 0);
				}
				if (pathUrl.isEmpty())
					connected(dest.getName(), utente.getId());
				else
					connected(pathUrl + "\\" + dest.getName(), utente.getId());
			} catch (IllegalStateException ise) {
				ise.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "Errore nel caricamento del file");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				if (pathUrl.isEmpty()) {
					return "redirect:home";
				}
				if (cond == 1) {
					return "redirect:Home/" + pathUrl.replace("\\", "/");
				} else {
					return "redirect:home/" + pathUrl.replace("\\", "/");
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "Errore nel caricamento del file");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				if (pathUrl.isEmpty()) {
					return "redirect:home";
				}
				if (cond == 1) {
					return "redirect:Home/" + pathUrl.replace("\\", "/");
				} else {
					return "redirect:home/" + pathUrl.replace("\\", "/");
				}
			}
		}
		String msg = new String();
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		if (fileNames.size() == 1) {
			msg = "Il file <ul><li>" + fileNames.get(0) + "</li></ul> è stato caricato con successo";
		} else {
			msg = "I file <ul>";
			for (int i=0; i<fileNames.size(); i++) {
				msg += "<li>" + fileNames.get(i) + "</li>";
			}
			msg += "</ul> sono stati caricati con successo";
		}
		redirectAttrs.addFlashAttribute("msg", msg);
		redirectAttrs.addFlashAttribute("msgClass", "success");
		if (pathUrl.isEmpty()) {
			return "redirect:home";
		}
		if (cond == 1) {
			return "redirect:Home/" + condName +pathLog;
		} else {
			return "redirect:home/" + pathUrl.replace("\\", "/");
		}
	}
	
	@Override
	public void connected(String path,Long id) {
		if (!ClientController.connected){
			SincronizzazioniPendenti sinc = new SincronizzazioniPendenti(id,path,1);
			sincDAO.addSincronizzazioniPendenti(sinc);
		} else {
			try {
				//ClientController.openedSession.getBasicRemote().sendText("FILE:"+path);
				FileInputStream fis = new FileInputStream(dest);
				byte[] b = new byte[1024];
				int bRead;
				while ((bRead=fis.read(b))!=-1) {
					ByteBuffer bb = ByteBuffer.allocate(bRead);
					bb.put(b, 0, bRead);
					System.out.println("PRIMA");
					ClientController.openedSession.getBasicRemote().sendBinary(bb);
					System.out.println("dopo");
				}
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
