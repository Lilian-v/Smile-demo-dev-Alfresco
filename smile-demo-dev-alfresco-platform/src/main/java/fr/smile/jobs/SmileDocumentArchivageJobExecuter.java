package fr.smile.jobs;

import java.util.List;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.transaction.TransactionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fr.smile.model.SmileModel;

public class SmileDocumentArchivageJobExecuter {
	private static final Logger logger = Logger.getLogger(SmileDocumentArchivageJobExecuter.class);
	
	@Autowired
	private NodeService nodeService;
	@Autowired 
	private SearchService searchService;
	@Autowired
	private TransactionService transactionService;
	
	@Value("${smile.job.documentArchivage.runAsUser}")
	private String runAsUser;
	@Value("${smile.job.documentArchivage.searchQuery}")
	private String searchQuery;
    
	public void execute() {
		// Toujours mettre un runAs pour définir l'utilisateur exécutant le job
		AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Void>() {
			@Override
			public Void doWork() throws Exception {
				transactionService.getRetryingTransactionHelper().doInTransaction(new RetryingTransactionCallback<Void>() {
					@Override
					public Void execute() throws Throwable {
						executeInternal();
						return null;
					}
				}, false /*read only*/, true /*requires new*/);
				return null;
			}
		}, runAsUser);
	}
	
	private void executeInternal() {
		logger.info("Début job SmileDocumentArchivageJobExecuter");
		
		//TODO si passage en prod : par défaut recherche limité à 1000 résultats, attention
		List<NodeRef> nodesToArchive = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_FTS_ALFRESCO, searchQuery).getNodeRefs();
		for (NodeRef node : nodesToArchive) {
			logger.info("Archiving node " + node);
			nodeService.addAspect(node, SmileModel.ASPECT_DOCUMENT_ARCHIVE, null);
		}
		
		logger.info("Fin job SmileDocumentArchivageJobExecuter");
	}
}
