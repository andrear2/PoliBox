# PoliBox
L’idea  alla  base  del  progetto  è  quella  di  sviluppare  un’applicazione  web  simile  a  Dropbox:  PoliBox. L’applicazione  offrirà  agli  utenti  la  possibilità  di  mantenere  sincronizzate  delle  cartelle/files  tra computer diversi in tempo reale e ne permetterà l’accesso (e modifica) anche tramite un sito web.

##Specifiche
Come per Dropbox, Google Drive, etc., l’applicazione PoliBox permette agli utenti di salvare file in una specifica cartella che è mantenuta sincronizzata con il server centrale, e, a sua volta con tutti i computer su cui l’utente ha il client installato.

####Condivisione
Oltre alla sincronizzazione della cartella con il server e i computer in cui l’utente ha il client installato, PoliBox offre anche la possibilità di condividere la cartella (o una sottocartella) con altri utenti del sistema. L’offerta o richiesta di condivisione viene notificata per email (o tramite l’interfaccia web) e deve essere accettata dalla controparte prima che abbia effetto. Una risorsa condivisa viene quindi mantenuta sincronizzata tra tutti gli utenti con cui è condivisa.
* Diritti di accesso: gli utenti con i quali è stata condivisa una risorsa hanno, di default, la possibilità di leggerne,  modificarne  e  cancellarne  i  contenuti.  Si  richiede  però  di  introdurre  la  possibilità  di condividere le risorse in sola lettura, cioè di non ammettere la modifica, cancellazione o aggiunta didocumenti.
* Conflitti: l’applicazione deve essere in grado di gestire e segnalare eventuali conflitti nel caso unarisorsa sia modificata contemporaneamente da più utenti.

####Interfaccia web
L’accesso (e modifica) alla cartella PoliBox può avvenire anche attraverso l’interfaccia web del server centrale. Ogni modifica effettuata attraverso tale interfaccia si ripercuote sulla cartella stessa in ogni computer in cui è condivisa.  In secondo luogo, l’interfaccia web del server, deve anche permettere all’utente di effettuare le operazioni di gestione come:
* la registrazione e autenticazione degli utenti;
* la definizione e modifica dei propri dati personali;
* il cambio di password ed email;
* la condivisione di risorse con altri utenti;
* il download dell’applicazione client;
* il logging delle azioni sulle risorse e la visualizzazione dello storico delle notifiche;
* la  visualizzazione  di  informazioni  sulle  statistiche  di  utilizzo  (e.g.,  spazio  usato  e  disponibile),  sui computer connessi, sugli utenti con cui si condivide una risorsa.

####Notifiche
Sia  tramite  l’interfaccia  web,  sia  tramite  il  client  locale  (se  non  disabilitato),  l’utente  deve  poter  ricevere notifiche in tempo reale in merito a risorse modificate, richieste di condivisione, nuovi dispositivi collegati, spazio in esaurimento. Alcune di queste notifiche possono anche essere ricevute via email.

####Altro
E’ fortemente incoraggiata la gestione della sicurezza del sistema e l’utilizzo del protocollo HTTPS.
