package fr.smile.javascript.processors;

import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

//https://angelborroy.wordpress.com/2015/07/24/alfresco-run-as-system-admin-user-without-credentials/
public class SmileRunAs extends BaseScopableProcessorExtension {
	
    public void specificUser(Function func, String user) {
        executeInternal(func, user);
    }
    
    public void admin(Function func) {
    	executeInternal(func, AuthenticationUtil.getAdminUserName());
    }
    
    public void system(Function func) {
    	executeInternal(func, AuthenticationUtil.getSystemUserName());
    }
    
    private void executeInternal(Function func, String user) {
    	Context cx = Context.getCurrentContext();
        Scriptable scope = getScope();
    	AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Void>() {
			@Override
			public Void doWork() throws Exception {
				func.call(cx, scope, scope, new Object[] {});
				return null;
			}
		}, user);
	}
}

