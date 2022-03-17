package fr.smile.workflows;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.delegate.DelegateExecution;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.repo.workflow.activiti.ActivitiScriptNode;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.springframework.beans.factory.annotation.Autowired;

public class SmileWorkflowService {
	
	@Autowired
	private NodeService nodeService;
	
	public List<NodeRef> getWorkflowDocuments(DelegateExecution execution) {
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
