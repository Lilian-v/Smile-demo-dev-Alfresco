<#-- On ajoute notre fichier JS à la suite de ceux déjà présents -->
<@markup id="smile-custom-documentlist-dependencies" target="js" action="after" scope="global"> 
	<@script type="text/javascript" src="smile/documentlibrary/smile-documentlist.js" group="documentlibrary"/> 
</@markup>

<#-- Ajout d'un template caché qui sera ajouté sur la page via JS -->
<@markup id="smile-custom-html" target="html" action="after" scope="global"> 
	<@uniqueIdDiv>
		<#assign el=args.htmlid?html>
		<div id="${el}-smile-button" style="float:right; display:none;">
			<span class="yui-button yui-push-button">
				<span class="first-child">
					<button id="yui-gen156">Pop-up Smile</button>
				</span>
			</span>
		</div>
	</@>
</@>
