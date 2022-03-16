package fr.smile.workflows;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.delegate.DelegateExecution;
import org.alfresco.repo.workflow.WorkflowConstants;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.repo.workflow.activiti.ActivitiScriptNode;
import org.alfresco.repo.workflow.activiti.BaseJavaDelegate;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.model.SmileModel;

public class ApprobationSuccessDelegateExpression extends BaseJavaDelegate {
	private static final Logger logger = Logger.getLogger(ApprobationSuccessDelegateExpression.class);
	
	@Autowired
	private NodeService nodeService;
	
	@Override
	protected String getName() {
		// Nom utilisé dans la tâche du workflow (bpmn20). Eviter de mettre des .
		return "smileWorkflow_checkApprove_ApprobationSuccessDelegateExpression";
	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<NodeRef> documents = getWorkflowDocuments(execution);
		Date dateNow = new Date();
		
		for (NodeRef document : documents) {
			nodeService.setProperty(document, SmileModel.PROP_APPROVED_DOCUMENT_DATE_APPROBATION, dateNow);
			
			// On  supprime les associations existantes avant de les recréer
			List<AssociationRef> targetAssocs = nodeService.getTargetAssocs(document, 
					new RegexQNamePattern(SmileModel.SMILE_URI, "approvedDocument.*"));
			for (AssociationRef assoc : targetAssocs) {
				logger.trace("Removing association " + assoc.getTypeQName() + " depuis le node " + assoc.getSourceRef() + 
						" vers le node " + assoc.getTargetRef());
				nodeService.removeAssociation(document, assoc.getTargetRef(), assoc.getTypeQName());
			}
			
			// On peut créer les nouvelles associations
			ActivitiScriptNode initiatorNode = (ActivitiScriptNode) execution.getVariable(WorkflowConstants.PROP_INITIATOR);
			nodeService.createAssociation(document, initiatorNode.getNodeRef(), SmileModel.ASSOC_APPROVED_DOCUMENT_INITIATEUR);

			ActivitiScriptNode verificateurNode = (ActivitiScriptNode) execution.getVariable("smilewf_checkApproveWorkflowVerificateur");
			nodeService.createAssociation(document, verificateurNode.getNodeRef(), SmileModel.ASSOC_APPROVED_DOCUMENT_VERIFICATEUR);
			
			ActivitiScriptNode approbateurNode = (ActivitiScriptNode) execution.getVariable("smilewf_checkApproveWorkflowApprobateur");
			nodeService.createAssociation(document, approbateurNode.getNodeRef(), SmileModel.ASSOC_APPROVED_DOCUMENT_APPROBATEUR);
			
		}
	}
	
	private List<NodeRef> getWorkflowDocuments(DelegateExecution execution) {
		ActivitiScriptNode workflowPackage = (ActivitiScriptNode) execution.getVariable("bpm_package");
		// ActivitiScriptNode = conçu pour le JS, un objet qui représente un node, qui contient les différents services (nodeService, ...) et qui a des fonctions
		// qui font appels à ces services. Plus propre de passer par un noderef et faire les appels de manière classique
		if (workflowPackage != null) {
			NodeRef workflowPackageNode = workflowPackage.getNodeRef();
			return nodeService.getChildAssocs(workflowPackageNode, WorkflowModel.ASSOC_PACKAGE_CONTAINS, null).stream()
					.map(assoc -> assoc.getChildRef())
					.collect(Collectors.toList());
		}
		
		return Collections.emptyList();
	}
}
