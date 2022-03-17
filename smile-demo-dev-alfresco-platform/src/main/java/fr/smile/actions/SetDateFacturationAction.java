package fr.smile.actions;

import java.util.Date;
import java.util.List;

import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.model.SmileModel;

public class SetDateFacturationAction extends ActionExecuterAbstractBase {
	private static final Logger logger = Logger.getLogger(SetDateFacturationAction.class);
	
	@Autowired
	private NodeService nodeService;

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		
	}
	
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		logger.debug("Mise à jour date facturation via action, node = " + actionedUponNodeRef);
		nodeService.setProperty(actionedUponNodeRef, SmileModel.PROP_DOCUMENT_COMPTABLE_DATE_FACTURATION, new Date());
		
	}

}
