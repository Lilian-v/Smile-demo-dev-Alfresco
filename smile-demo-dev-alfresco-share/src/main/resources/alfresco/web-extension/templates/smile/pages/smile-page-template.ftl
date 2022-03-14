<#include "/org/alfresco/include/alfresco-template.ftl" />

<@templateHeader>
</@>

<@templateBody>
   <@markup id="alf-hd">
   <div id="alf-hd">
      <@region scope="global" id="share-header" chromeless="true"/>
   </div>
   </@>
   <@markup id="bd">
    <div id="bd">
        <#-- La région doit correspondre à un fichier dans web-extenstion/site-data/components/ -->
        <#-- Le fichier doit se nommer [scope].[id].[nom page].xml, donc : page.regionAccueil.smilehome.xml -->
        <@region id="regionAccueil" scope="page" />
    </div>
   </@>
</@>

<@templateFooter>
   <@markup id="alf-ft">
   <div id="alf-ft">
      <@region id="footer" scope="global" />
   </div>
   </@>
</@>
