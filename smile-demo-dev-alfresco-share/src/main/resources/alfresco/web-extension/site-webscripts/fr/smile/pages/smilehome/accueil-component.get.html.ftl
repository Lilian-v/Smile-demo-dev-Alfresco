<@markup id="js">
	<#-- Dépendances JS -->
	<#--  Exemple 
	<@script type="text/javascript" src="${url.context}/res/monfichier.min.js" group="accueil-component"/>
	-->
</@>

<@markup id="css" >
	<#-- Dépendances CSS -->
	<#--  Exemple 
	<@link rel="stylesheet" type="text/css" href="${url.context}/res/monfichier.min.css" group="accueil-component"/>
	-->
</@>

<@markup id="widgets">
	<@createWidgets group="accueil-component"/>
</@>

<@markup id="html">
	<@uniqueIdDiv>
		<#assign el = args.htmlid?html>
		<div>
			<h3>Bonjour depuis le component accueil</h3>
			<p>Fichier accueil-component.get.html.ftl</p>
			<p>Bonne journée</p>
		</div>
	</@>
</@>