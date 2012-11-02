<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="customer.list.partner" /> - ${fieldValue(bean: instance, field: 'name')}</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:hasErrors bean="${instance}">
            <div class="errors">
                <g:renderErrors bean="${instance}" as="list" />
            </div>
        </g:hasErrors>
        <g:render template="header"/>
        <div class="r3widget r3form show ${instance.lockdata == null ? 'unlocked':'locked'}">
          <g:form name="partnerform" action="edit" id="${instance.id}">
            <div>
	            <h1>${fieldValue(bean: instance, field: 'name')}</h1>
	            <g:if test="${instance.lockdata != null}">
	                <h2><g:message code="locked.by" />:${instance.lockdata.lockedBy.username}</h2>
	            </g:if>
	        </div>
            <div class="r3listing">
                <div class="filter">
                    <h1><g:message code="filter" /></h1>
                    <g:textField class="" 
                                 id="cfilter" name="cfilter"
                                 value="" />
                </div>
	            <table id="customers-table">
	                <thead>
	                <tr>
	                    <r3:sortableColumn action="customers" 
	                                      class="table_title"
	                                      colspan="2"
	                                      property="name" 
	                                      title="${message(code:'name',default:'Name')}" 
	                                      params="${[id:params.id]}" />
	                </tr>
	                </thead>
	                <tbody>
	                <g:each in="${instances}" var="i" status="s">
	                    <tr class="${(s % 2) == 0 ? 'even' : 'odd'}">
	                    <td colspan="2"><g:link controller="customer" action="show" id="${i.id}">${i.name}</g:link></td>
	                    </tr>
	                </g:each>
	                <tr>
	                    <td>
	                       <script type="text/javascript">
	                       var listLength = ${grailsApplication.config.response3.lists.length};
                           var totalLength = ${total};
                           var r3execute = function(ele){
                               var map = response3.collections.Dictionary();
                               map.set('url','${createLink(action:"moreCustomers")}');
                               map.set('params',{id:${instance.id},offset:listLength});
                               map.set('callback', function(response){
                            	   var data = $.parseJSON(response.responseText);
                            	   listLength += data.length;
                            	   if(listLength == totalLength){
                            		   response3.disable.button(ele);
                               	   }
                            	   $("#currentLength").html(listLength);
                            	   var table=document.getElementById("customers-table");
                            	   var href = "${createLink(controller:'customer', action:'show')}/";
                            	   for(var i=0; i<data.length; i++){
                            		   var atag = document.createElement('a');
                                       atag.href= href+data[i].id;
                                       atag.innerHTML= data[i].name;
                            		   var row=table.insertRow(table.rows.length -1);
                                       var cell1=row.insertCell(0);
                                       cell1.colSpan=2;
                                       cell1.appendChild(atag);
                               	   }
                            	   response3.table.updateRowColors(table);
                               });
                               response3.ajax(map);
                               return false;
                           }
                           </script>
	                       <g:if test="${total > grailsApplication.config.response3.lists.length}">
                               <input type="submit" class="button more" value="${message(code:'show.more')}" onclick="r3execute(this);return false;"/>
                           </g:if>
	                    </td>
	                    <td>
	                       <span id="currentLength">${grailsApplication.config.response3.lists.length}</span>
	                       <span><g:message code="total.number.of.customers" args="${[total]}"/></span>
	                    </td>
	                </tr>
	                </tbody>
	            </table>
	        </div>
          <g:render template="content_navigation"/>
          <div class="clear"></div>
          </g:form>
        </div>

    </body>
</html>