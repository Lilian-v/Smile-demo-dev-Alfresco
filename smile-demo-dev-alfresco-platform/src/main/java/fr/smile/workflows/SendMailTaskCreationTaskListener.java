package fr.smile.workflows;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.MailActionExecuter;
import org.alfresco.repo.admin.SysAdminParams;
import org.alfresco.repo.workflow.activiti.ActivitiConstants;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.util.UrlUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.apache.commons.lang3.StringUtils;

public class SendMailTaskCreationTaskListener implements TaskListener {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SendMailTaskCreationTaskListener.class);
	
	@Autowired
	private ActionService actionService;
	@Autowired 
	private SysAdminParams sysAdminParams;
	@Autowired
	private PersonService personService;
	@Autowired
	private NodeService nodeService;
	
	@Value("${smile.workflow.checkApprove.mail.sender}")
	private String fromMailAdress;

	@Override
	public void notify(DelegateTask delegateTask) {
		NodeRef assigneeNode = personService.getPersonOrNull(delegateTask.getAssignee());
		if (assigneeNode == null) {
			logger.error("User not found, skipping mail : " + delegateTask.getAssignee());
			return;
		}
		String assigneeMail = (String) nodeService.getProperty(assigneeNode, ContentModel.PROP_EMAIL);
		if (StringUtils.isBlank(assigneeMail)) {
			logger.error("Mail not found for user, skippin mail : " + delegateTask.getAssignee());
			return;
		}
		
		Action mailAction = actionService.createAction(MailActionExecuter.NAME);
		mailAction.setParameterValue(MailActionExecuter.PARAM_FROM, fromMailAdress);
		mailAction.setParameterValue(MailActionExecuter.PARAM_SUBJECT, "[Alfresco] Tâche de workflow à réaliser");
		mailAction.setParameterValue(MailActionExecuter.PARAM_HTML, getHTMLMailContent(ActivitiConstants.ENGINE_ID + "$" + delegateTask.getId()));
		mailAction.setParameterValue(MailActionExecuter.PARAM_IGNORE_SEND_FAILURE, true);
		mailAction.setParameterValue(MailActionExecuter.PARAM_TO, assigneeMail);
		
		logger.info("Sending mail to " + assigneeMail + " for task " + 
				UrlUtil.getShareUrl(sysAdminParams) + "/page/task-details?taskId=" + delegateTask.getId());
		actionService.executeAction(mailAction, null);
	}

	private Serializable getHTMLMailContent(String taskFullId) {
		String mailHtmlContent = "Bonjour,<br>"
				+ "Une tâche de workflow vous est attribuée.<br>"
				+ "Cliquer sur le lien suivant pour la consulter : <a href=\""
					// Lien vers /share en reprenant le context, protocol et hostname de alfresco-global.properties
					+ UrlUtil.getShareUrl(sysAdminParams) + "/page/task-details?taskId=" + taskFullId
					+ "\">Voir la tâche</a><br>"
				+ "<br>"
				+ "Cordialement,<br>"
				+ "L'équipe Alfresco";
		return mailHtmlContent;
	}

}
