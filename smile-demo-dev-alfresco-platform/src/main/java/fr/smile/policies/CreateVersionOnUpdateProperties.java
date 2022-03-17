package fr.smile.policies;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies.OnUpdatePropertiesPolicy;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.repo.version.VersionModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.version.VersionService;
import org.alfresco.service.cmr.version.VersionType;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateVersionOnUpdateProperties implements OnUpdatePropertiesPolicy, InitializingBean {
	private static final Logger logger = Logger.getLogger(CreateVersionOnUpdateProperties.class);
	
	@Autowired
	private PolicyComponent policyComponent;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private VersionService versionService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.policyComponent.bindClassBehaviour(OnUpdatePropertiesPolicy.QNAME,
				ContentModel.ASPECT_VERSIONABLE, // Aspect / type que le node doit avoir pour que la policy se déclenche
				// ContentModel.TYPE_CMOBJECT pour dossiers + fichiers
				new JavaBehaviour(this, "onUpdateProperties", Behaviour.NotificationFrequency.TRANSACTION_COMMIT));
	}

	@Override
	public void onUpdateProperties(NodeRef nodeRef, Map<QName, Serializable> before, Map<QName, Serializable> after) {
		if (!nodeService.exists(nodeRef)) {
			logger.debug("Skipping policy, node does not exist : " + nodeRef);
			return ;
		}
		
		logger.debug("Creating new minor version node " + nodeRef);
		versionService.createVersion(nodeRef,  Collections.singletonMap(VersionModel.PROP_VERSION_TYPE, VersionType.MINOR));
	}

}
