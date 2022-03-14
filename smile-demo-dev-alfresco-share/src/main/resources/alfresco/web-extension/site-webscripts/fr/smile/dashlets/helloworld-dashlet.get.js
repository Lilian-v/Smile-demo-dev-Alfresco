// Surf Dashlet widgets
var widgets = [];

// Main component
widgets.push({
	id : "HelloWorld",
	name : "Smile.dashlet.HelloWorld", // Nom du composant utilisé, le composant est créé par le js coté client helloworld-dashlet.js
	options : {
		componentId : instance.object.id
	}
});

// Resizer
widgets.push({
	id : "DashletResizer",
	name : "Alfresco.widget.DashletResizer",
	initArgs : [ "\"" + args.htmlid + "\"", "\"" + instance.object.id + "\"" ],
	useMessages : false
});

// Title bar actions
var actions = [];
actions.push({
	cssClass : "help",
	bubbleOnClick : {
		message : msg.get("dashlet.help")
	},
	tooltip : msg.get("dashlet.help.tooltip")
});
widgets.push({
	id : "DashletTitleBarActions",
	name : "Alfresco.widget.DashletTitleBarActions",
	useMessages : false,
	options : {
		actions : actions
	}
});

model.widgets = widgets;
