package fr.smile.model;

import org.alfresco.service.namespace.QName;

public class SmileModel {
	public static final String SMILE_URI = "http://www.smile.fr/model/content/1.0";
	
	public static final QName ASPECT_DOCUMENT_COMPTABLE = QName.createQName(SMILE_URI, "documentComptable");
	public static final QName PROP_DOCUMENT_COMPTABLE_ID = QName.createQName(SMILE_URI, "documentComptableID");
	public static final QName PROP_DOCUMENT_COMPTABLE_DATE_FACTURATION = QName.createQName(SMILE_URI, "documentComptableDateFacturation");

}
