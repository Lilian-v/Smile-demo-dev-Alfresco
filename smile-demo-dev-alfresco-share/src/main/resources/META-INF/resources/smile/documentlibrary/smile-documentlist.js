if (typeof Smile == undefined || !Smile) {
	var Smile = {};
}

(function() {
	// Constructeur dans lequel on appelle le constructeur parent
	Smile.DocumentList = function CustomDocumentList_constructor(htmlId) {
		Smile.DocumentList.superclass.constructor.call(this, htmlId);
		
		// Re-register with our own name
		this.name = "Smile.DocumentList";
		Alfresco.util.ComponentManager.reregister(this);
		return this;
	};

	// Extend YAHOO pour permettre de surcharger les méthodes du composant Alfresco
	YAHOO.extend(Smile.DocumentList, Alfresco.DocumentList, {
		// Méthode quand le composant est prêt à être chargé
		onReady : function CustomDL_onReady() {
			// On appelle la méthode parent
			Smile.DocumentList.superclass.onReady.call(this);
			
			// On récupère la barre de menu puis la partie de droite
			var rightTopBar = YAHOO.util.Dom.getElementsByClassName("header-bar")[0].getElementsByClassName("right")[0];
			// Et notre bouton masqué
			var smileButton = YAHOO.util.Dom.get(this.id + "-smile-button");
			
			// Déplacement du bouton au bon endroit
			rightTopBar.appendChild(smileButton);
			
			// On le rend visible et on ajoute une fonction au clic
			YAHOO.util.Dom.setStyle(smileButton, "display", "block")
			smileButton.onclick = this.onSmileButtonClick;
		},
		

		onSmileButtonClick : function CustomDL_onSmileButtonClick(e, p_obj) {
			// Création d'une modale
			var confirmDialog = new YAHOO.widget.SimpleDialog('smileDialog', {
				fixedcenter: true,
				visible: false,
				draggable: false,
				close: true,
				constraintoviewport: true,
				modal:true,
				buttons: [
					{
						text:"Oui", 
						handler: {
							fn : function(e) {
								confirmDialog.hide();
								
								// Redirection vers la page URL
								var smilePageUrl = "/share/page";
								// si on est dans un site on veut rester dans le site
								if (Alfresco.constants.SITE != "") {
									smilePageUrl += "/site/" + Alfresco.constants.SITE;
								}
								
								smilePageUrl += "/smilehome";
								window.location.href = smilePageUrl;
							}
						},
						scope : this,
						isDefault : true
					}, 
					{
						text:"Non", 
						handler: {
							fn : function(e) {
								confirmDialog.hide();
							}
						},
						scope : this
					}
				]
			});
			
			// Affichage de la modale
			confirmDialog.setHeader("Informations Smile");
			confirmDialog.setBody("Souhaitez-vous voir la page d'accueil Smile ?");
			confirmDialog.render(document.body);
			confirmDialog.show();
		}

	});

})();
