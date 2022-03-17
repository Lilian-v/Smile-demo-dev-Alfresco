package fr.smile.workflows.updateProperties;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.workflow.activiti.BaseJavaDelegate;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.workflows.SmileWorkflowService;

public class UpdateNodePropertiesDelegateExpression extends BaseJavaDelegate {
	private static final Logger logger = Logger.getLogger(UpdateNodePropertiesDelegateExpression.class);
	
	@Autowired
	private NodeService nodeService;
	@Autowired
	private SmileWorkflowService smileWorkflowService;
	
	@Override
	public String getName() {
		return "smileWorkflow_UpdateNodePropertiesDelegateExpression";
	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String nouveauTitre = (String) execution.getVariable(UpdatePropertiesWorkflowConstants.PROP_NEW_TITLE);
		logger.debug("Nouveau titre : " + nouveauTitre);
		
		List<NodeRef> documents = smileWorkflowService.getWorkflowDocuments(execution);
		
		documents.forEach(node -> nodeService.setProperty(node, ContentModel.PROP_TITLE, nouveauTitre));
	}

}
