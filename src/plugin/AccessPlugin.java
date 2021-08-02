package plugin;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.wowza.wms.application.*;
import com.wowza.wms.httpstreamer.model.IHTTPStreamerSession;
import com.wowza.wms.module.*;

public class AccessPlugin extends ModuleBase {

	
	public void onHTTPSessionCreate(IHTTPStreamerSession httpSession)
	{
		boolean isGood = false;
		RSAPrivateKey privateKey = null; 
		String pathToPubKey = httpSession.getAppInstance().getProperties().getPropertyStr("pathToPubKey");
		String issuer = httpSession.getAppInstance().getProperties().getPropertyStr("tokenIssuer");
		String token = httpSession.getQueryStr().substring(2);
		String ipAddressClient = httpSession.getIpAddress();
		String streamName = httpSession.getStreamName();
		String appName = httpSession.getAppInstance().getApplication().getName();
		Date now = Date.from(Instant.now());
		try {
			RSAPublicKey publicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(pathToPubKey, "RSA");
		    Algorithm algorithm = Algorithm.RSA512(publicKey, privateKey);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(issuer)
		        .withClaim("streamName", streamName)
		        .withClaim("appName", appName)
		        .withClaim("ip", ipAddressClient)
		        .build();
		    DecodedJWT jwt = verifier.verify(token); // si no logra verificar tira excepcion
		    //si logra verificar entonces continua
		    
		    if(jwt.getExpiresAt().after(now))
		    	isGood = true;
		    
		} catch (Exception e1) {
			//no se hace nada, isGood ya estaba en false
		}
		
		IApplicationInstance appInstance = httpSession.getAppInstance();
		getLogger().info("ModuleAccessControlHTTPStreaming.onHTTPSessionCreate["+appInstance.getContextStr()+":"+streamName+"]: accept:"+isGood);
		if (!isGood)
			httpSession.rejectSession();
	}
}
