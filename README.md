# Vezbe7

Prilikom poziva web service-a sa vaseg mobilnog uredjaja, web service se ne nalazi na adresi localhost!
Potrebno je da specificirate adresu vase masine koju mozete dobiti pozivom komande:
1) Linux/Unix ifconfig
2) Windows ipconfig

Traziti adresu koja pocinje sa 192.168.x.x

Tako da konacan poziv bude u formatu:
'http://<service_ip_adress>:<service_port>/<putanja_do_web_service>/<metoda>/'

Primer:
http://192.168.43.73:8080/rs.ftn.reviewer.rest/rest/proizvodi/

NAPOMENA: mobilni uredjaj i racunar na kom se izvrsava web service treba da budu u istoj lokalnoj mrezi.
Osim ako se web service ne izvrsava na masini nekog javnog provider-a koji ima javnu IP adresu.
