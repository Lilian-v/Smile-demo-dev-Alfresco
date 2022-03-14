<#-- JavaScript Dependencies -->
<@markup id="js">
    <#-- Les ressources JS et CSS coté client doivent être dans le dossier src/main/resources/META-INF/resources/ -->
    <@script type="text/javascript" src="${url.context}/res/smile/dashlets/helloWorld/helloworld-dashlet.js" group="dashlets"/>
</@>


<#-- Surf Widget creation -->
<@markup id="widgets">
    <@createWidgets group="dashlets"/>
</@>

<@markup id="html">
    <@uniqueIdDiv>
        <#assign el = args.htmlid?html>
        <#assign dashboardconfig=config.scoped['Dashboard']['dashboard']>

        <div class="dashlet">
            <div class="title">
            ${msg("hello.world.dashletTitle")}
            </div>
            <div class="body">
                <button id="${el}-testButton">${msg('hello.world.buttonLabel')}</button>
            </div>
        </div>
    </@>
</@>




