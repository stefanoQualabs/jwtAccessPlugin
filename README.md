# wse-plugins

\\\\\\\\\\\ jwtAccessPlugin \\\\\\\\\\\

plugin de wowza streaming engine para controlar acceso a aplicaciones mediante jwt

para ejecutar buildear el proyecto que por defecto se empaqueta en usr/local/WowzaStreamingEngine-4.8.8/lib
agregar el modulo a la aplicacion de wowza como

\*nombre deseado\* 	    \*descripcion deseada\*     	plugin.AccessPlugin 

la clave publica del token y el issuer se modifican mediante custom properties creadas de la siguiente forma

/Root/Application      	pathToPubKey 	  String   	\*direccion de la publicKey.pem\*

/Root/Application     	tokenIssuer 	  String   	\*issuer\*

se utiliza con 
http://{wowza_url}/{app_name}/{stream_name}/playlist.m3u8?t=[my-jwt]


donde el jwt es creado con rsa512 y tiene el formato

    {

    "iss": "myIssuer",
  
    "expiration": 3600, // Time in seconds.
  
    "appName": "myApp",
  
    "streamName": "myStream",
  
    "ip": "1.1.1.1"
  
    }
