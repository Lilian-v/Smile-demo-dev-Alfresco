package fr.smile.webscripts;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldWebScript extends DeclarativeWebScript {
    private static Log logger = LogFactory.getLog(HelloWorldWebScript.class);

    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
    	logger.debug("Début webscript HelloWorldWebScript");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fromJava", "Hello from Java " + AuthenticationUtil.getRunAsUser());
        
        logger.debug("Fin webscript HelloWorldWebScript");
        return model;
    }
}
