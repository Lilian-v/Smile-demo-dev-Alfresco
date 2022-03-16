package fr.smile.model;

import org.alfresco.service.namespace.QName;

public interface SmileModel {
	public static final String SMILE_URI = "http://www.smile.fr/model/content/1.0";
	
	public static final QName ASPECT_DOCUMENT_COMPTABLE = QName.createQName(SMILE_URI, "documentComptable");
	public static final QName PROP_DOCUMENT_COMPTABLE_ID = QName.createQName(SMILE_URI, "documentComptableID");
	public static final QName PROP_DOCUMENT_COMPTABLE_DATE_FACTURATION = QName.createQName(SMILE_URI, "documentComptableDateFacturation");
	
	public static final QName ASPECT_DOCUMENT_ARCHIVE = QName.createQName(SMILE_URI, "documentArchive");
	
	public static final QName ASPECT_APPROVED_DOCUMENT = QName.createQName(SMILE_URI, "approvedDocument");
	public static final QName PROP_APPROVED_DOCUMENT_DATE_APPROBATION = QName.createQName(SMILE_URI, "approvedDocumentDateApprobation");
	public static final QName ASSOC_APPROVED_DOCUMENT_INITIATEUR = QName.createQName(SMILE_URI, "approvedDocumentInitiateur");
	public static final QName ASSOC_APPROVED_DOCUMENT_VERIFICATEUR = QName.createQName(SMILE_URI, "approvedDocumentVerificateur");
	public static final QName ASSOC_APPROVED_DOCUMENT_APPROBATEUR = QName.createQName(SMILE_URI, "approvedDocumentApprobateur");

}
