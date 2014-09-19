package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.DispositivoDAO;
import it.polito.ai.polibox.dao.SincronizzazioniPendentiDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Dispositivo;
import it.polito.ai.polibox.entity.SessionManager;
import it.polito.ai.polibox.entity.SincronizzazioniPendenti;
import it.polito.ai.polibox.entity.Utente;

import org.eclipse.jetty.jndi.local.localContextRoot;
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
	DispositivoDAO dispositivoDAO;
	@Autowired
	SincronizzazioniPendentiDAO sincDAO;

	private File dest;

	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public String fileUploadSubmit(@RequestParam(value="files") List<MultipartFile> uploadedFiles, @RequestParam(value="pathFile") String path, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}

		System.out.println("Upload filevariabile path: "+path);
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
		System.out.println("Upload file --- pathdir:" + pathDir + " ----pathurl: "+pathUrl);
		List<String> fileNames = new ArrayList<String>();

		if (uploadedFiles == null || uploadedFiles.size() == 0 || uploadedFiles.get(0).getOriginalFilename() == "") {
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			redirectAttrs.addFlashAttribute("msg", "Nessun file selezionato");
			redirectAttrs.addFlashAttribute("msgClass", "error");
			if (pathUrl.isEmpty()) {
				return "redirect:home";
			}
			if (cond == 1 && session.getAttribute("ownerCond")==null) {
				return "redirect:Home/" + condName+pathUrl.replace("\\", "/");
			}   else if ( session.getAttribute("ownerCond")!=null) {
				return "redirect:home/" + condName + pathUrl.replace("\\", "/");
			} else {
				return "redirect:home/" + pathUrl.replace("\\", "/");
			}
		}

		double totB = (Double) session.getAttribute("totByteFReg") + (Double) session.getAttribute("totByteFCond") ;

		Log log = new Log(utente.getHome_dir());
		for (MultipartFile file: uploadedFiles) {
			if (file.getSize() + totB > 5000000) {
				redirectAttrs.addFlashAttribute("utente", utente);
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "Il file " + file.getOriginalFilename() + " è troppo grosso. Hai a disposizione " + (5000000 - totB) + " bytes.");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				if (pathUrl.isEmpty()) {
					return "redirect:home";
				}
				if (cond == 1 && session.getAttribute("ownerCond")==null) {
					return "redirect:Home/" + condName + pathUrl.replace("\\", "/");
				}  else if ( session.getAttribute("ownerCond")!=null) {
					return "redirect:home/" + condName + pathUrl.replace("\\", "/");
				} else {
					return "redirect:home/" + pathUrl.replace("\\", "/");
				}
			}

			String fileName = file.getOriginalFilename();
			fileNames.add(fileName);
			dest = new File(pathDir + "\\" + fileName);
			System.out.println("Upload file in path: "+pathDir + "\\" + fileName);
			try {
				file.transferTo(dest);
				if(cond==1) {
					session.setAttribute("totByteFCond", (Double) session.getAttribute("totByteFCond") + file.getSize());
					Log owner_log = new Log(owner.getHome_dir());
					String[] p = condivisione.getDirPath().split("\\\\");
					int flag=0;
					String pp = new String("http://localhost:8080/ai/home");
					for (int i=0;i<p.length;i++) {
						if (flag==1) pp += "/"+p[i];
						if (p[i].equals("Polibox")) flag=1;
					}
					log.addLine(utente.getId(), "NF","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+fileName , 0, utente.getId());
					
					
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					ConcurrentHashMap<Long, Session> hm;
					if ( (hm = sm.getSessionMap(owner.getId())) != null) {
						owner_log.addLine(owner.getId(), "NF",pp+pathLog+"/"+fileName , 0, utente.getId());
						if ((wssession = hm.get(Long.parseLong("0")))!=null)
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha caricato il file <b>"+ fileName +" nella cartella condivisa "+ condivisione.getDirPath().substring(condivisione.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
					}
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioni(condivisione.getDirPath());
					for(Condivisione c: listCond){
						log.addLine(c.getUserId(), "NF","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+fileName , 0, utente.getId());
						if (c.getUserId()!=utente.getId()){
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0")))!=null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha caricato il file <b>"+ fileName +" nella cartella condivisa "+ c.getDirPath().substring(c.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
						
						}
					}
					
				} else {
					session.setAttribute("totByteFReg", (Double) session.getAttribute("totByteFReg") + file.getSize());
					log.addLine(utente.getId(), "NF",path+"/"+fileName , 0);
				}
//				if (pathUrl.isEmpty())
//					connected(dest.getName(), utente.getId());
//				else
//					connected(pathUrl + "\\" + dest.getName(), utente.getId());
				if(pathUrl.isEmpty())
					connected(pathDir+file.getOriginalFilename(), utente.getId(),dest.getName());
				else
					connected(pathDir+"\\"+file.getOriginalFilename(), utente.getId(),pathUrl + "\\" + dest.getName());
			} catch (IllegalStateException ise) {
				ise.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "Errore nel caricamento del file");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				if (pathUrl.isEmpty()) {
					return "redirect:home";
				}
				if (cond == 1 && session.getAttribute("ownerCond")==null) {
					return "redirect:Home/" + condName + pathUrl.replace("\\", "/");
				}  else if ( session.getAttribute("ownerCond")!=null) {
					return "redirect:home/" + condName + pathUrl.replace("\\", "/");
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
				if (cond == 1 && session.getAttribute("ownerCond")==null) {
					return "redirect:Home/" + condName + pathUrl.replace("\\", "/");
				}  else if ( session.getAttribute("ownerCond")!=null) {
					return "redirect:home/" + condName + pathUrl.replace("\\", "/");
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
		if (cond!=1 && pathUrl.isEmpty()) {
			return "redirect:home";
		}
		if (cond == 1 && session.getAttribute("ownerCond")==null) {
			return "redirect:Home/" + condName +pathLog;
		}  else if ( session.getAttribute("ownerCond")!=null) {
			return "redirect:home/" + condName + pathUrl.replace("\\", "/");
		} else {
			return "redirect:home/" + pathUrl.replace("\\", "/");
		}
	}

	@Override
	public void connected(String path,Long id, String pathRel) {
		for (Dispositivo d : dispositivoDAO.getDispositivi(id)){
			if(SessionManager.getInstance().getSessionMap(id)== null || !SessionManager.getInstance().getSessionMap(id).containsKey(d.getId())){
				SincronizzazioniPendenti sinc = new SincronizzazioniPendenti(id,d.getId(),path,1);
				sincDAO.addSincronizzazioniPendenti(sinc);
			}
		}
			try {
				if (SessionManager.getInstance().getSessionMap(id)!=null) {
					for (Session s : SessionManager.getInstance().getSessionMap(id).values()){
						s.getBasicRemote().sendText("FILE:"+pathRel);
					}
				}
				FileInputStream fis=null;
				fis = new FileInputStream(dest);
				System.out.println(fis.available());
				byte[] b = new byte[8192];
				int bRead;
				ByteBuffer bb;
				while ((bRead=fis.read(b))!=-1) {
					System.out.println(bRead);
					if(bRead<8192){
						bb=ByteBuffer.wrap(b,0,bRead);
					}else
						bb=ByteBuffer.wrap(b);
					if (SessionManager.getInstance().getSessionMap(id)!= null) {
						for (Session s : SessionManager.getInstance().getSessionMap(id).values()){
							s.getBasicRemote().sendBinary(bb);
						}
					}
					bb.clear();
					Thread.sleep(25);
				}
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
