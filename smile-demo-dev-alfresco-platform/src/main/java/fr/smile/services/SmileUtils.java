package fr.smile.services;

import java.util.List;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.MimetypeService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.LimitBy;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.util.FileNameValidator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class SmileUtils {
	private static final Logger logger = Logger.getLogger(SmileUtils.class);
	
	@Autowired
	private SearchService searchService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private MimetypeService mimetypeService;
	@Autowired
	private FileFolderService fileFolderService;
	
	// Par défaut limite de 1000 résultats dans les recherches, a cause de la vérification des permissions
	// Possibilité d'aller au delà de cette limite via setMaxPermissionChecks et setMaxPermissionCheckTimeMillis
	// Attention, risque de saturer la RAM si beaucoup de résultats
	public List<NodeRef> searchWithoutLimit(String ftsQuery) {
		SearchParameters params = new SearchParameters();
		params.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
		params.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
		params.setQuery(ftsQuery);
		params.setLimitBy(LimitBy.UNLIMITED);
		params.setLimit(0);
		params.setMaxPermissionChecks(Integer.MAX_VALUE);
		params.setMaxPermissionCheckTimeMillis(Integer.MAX_VALUE);
		params.setMaxItems(Integer.MAX_VALUE);
		
		logger.debug("Exécution de requête sans limite : " + ftsQuery);
		List<NodeRef> results = searchService.query(params).getNodeRefs();
		logger.debug("Fin requête '" + ftsQuery + "', nombre résultat = " +  results.size());
		return results;
	}
	
	// Caractères interdits dans un nom de fichier + impossible de finir par un . ou un espace
	public String getValidNodeName(String expectedName) {
		String newName = expectedName.replaceAll(FileNameValidator.FILENAME_ILLEGAL_REGEX, "_");
		// Traiter le cas des noms se terminant avec un point/espace car non géré par la méthode Alfresco FileNameValidator.getValidFileName ...
		while (newName.endsWith(" ") || newName.endsWith(".")) {
			newName = newName.substring(0, newName.length() - 1);
    	}
		
		return newName;
	}
	
	// Renvoi un nom unique pour un dossier
	public String getUniqueValidName(NodeRef parentFolder, String expectedName) {
		String validName = getValidNodeName(expectedName);
		String extension = FilenameUtils.getExtension(validName);
		if (StringUtils.isNotEmpty(extension) && MimetypeMap.MIMETYPE_BINARY.equals(mimetypeService.getMimetype(extension))) {
			// L'extension retrouvée n'est pas valide (par exemple un dossier dont le nom contient juste un .), on l'ignore
			extension = "";
		}
		else {
			extension = "." + extension;
		}
		String baseName = validName.substring(0, validName.length() - extension.length());
		
		int counter = 0;
		String newName = validName;
		while (fileFolderService.searchSimple(parentFolder, newName) != null) {
			logger.debug("Node '" + newName + "' already exists in folder " + parentFolder + " , looking for another name");
			counter ++;
			newName = baseName + "-" + counter + extension;
		}
		
		return newName;
	}
}
