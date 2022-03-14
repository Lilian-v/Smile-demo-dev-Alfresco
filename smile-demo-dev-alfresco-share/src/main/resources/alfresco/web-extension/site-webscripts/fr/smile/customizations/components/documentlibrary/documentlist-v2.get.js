// On remplace le composant Alfresco par le notre en changeant son nom
for (var i=0; i < model.widgets.length; i++) {
	if (model.widgets[i].id == "DocumentList") {
		model.widgets[i].name = "Smile.DocumentList";
		break;
	}
}
