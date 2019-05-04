<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Applications/Senders/Recipient Groups</title>

    <div class="changes-notifications" id="div0">
        <br><h4 style="color:#FF8000">${changesmessage}<h4>
    </div>

    <c:set var="urlPrefix" value="/notifcenter/aplicacoes/"/>
    <c:set var="grupsDest" value="gruposdestinatarios"/>
    <c:set var="slash" value="/"/>
    <c:set var="paropen" value="("/>
    <c:set var="parclose" value=")"/>

    <style>
        #table1 {
          font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
          border-collapse: collapse;
          width: 100%;
        }

        #table1 td, #table1 th {
          border: 1px solid #ddd;
          padding: 2px;
        }

        #table1 tr:nth-child(even){background-color: #f2f2f2;}

        #table1 tr:hover {background-color: #ddd;}

        #table1 th {
          padding-top: 2px;
          padding-bottom: 2px;
          text-align: left;
          background-color: #009FE3;
          color: white;
        }
    </style>

</head>

<body>
    <h2><b>Applications/Senders/Recipient groups manager</b></h2>

    <div class="list-applications-senders-recipientgroups" id="div1">

        <br><h4>Recipient groups for sender ${sender.nome} (${sender.externalId}) from application ${application.name} (${application.externalId})</h4>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Group</th>
                <th>Actions</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="gdt" items="${gruposdestinatarios}">

                <c:if test="${id != null}">
                     <c:remove var="id"/>
                </c:if>

                <tr>
                    <c:forEach var="entry" items="${gdt}">

                        <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingGruposDestinatariosFromRemetente() --%>
                            <c:set var="id" value="${entry.value}"/>
                        </c:if>

                        <td><c:out value="${entry.value}"/></td>

                    </c:forEach>

                    <td>
                        <c:if test="${id != null}"> <%-- robustness --%>
                            <form action="<c:out value="${urlPrefix}${application.externalId}${slash}${sender.externalId}${slash}${grupsDest}"/>" onsubmit="return confirm('Do you really want to remove this recipient group?');" method="post">
                                <input type="hidden" name="removeGrupoDestinatario" value="<c:out value="${id}"/>">
                                <input type="submit" value="Delete">
                            </form>

                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </div>

    <div class="add-recipientgroup" id="div2">
        <br><h3>Add new recipient group</h3>

        <form id="form2" action="<c:out value="${urlPrefix}${application.externalId}${slash}${sender.externalId}${slash}${grupsDest}"/>" method="post" >
            <table id="table2">
                <tr>
                    <td>Select group:</td>
                    <td>
                        <select name="group">
                            <c:forEach var="grupo" items="${grupos}">

                                <c:forEach var="entry" items="${grupo}">
                                    <c:if test="${entry.key == 'id'}">
                                        <c:set var="idgrupo" value="${entry.value}"/>
                                    </c:if>

                                    <c:if test="${entry.key == 'name'}">
                                        <c:set var="nomegrupo" value="${entry.value}"/>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${idgrupo != null && nomegrupo != null}"> <%-- robustness --%>
                                    <option value="<c:out value="${idgrupo}"/>"><c:out value="${nomegrupo} ${paropen}${idgrupo}${parclose}"/></option>
                                </c:if>

                            </c:forEach>
                        </select>
                    </td>

                </tr>
            </table>
            <input type="hidden" name="addGrupoDestinatario" value="does_not_matter">
            <input type="submit" value="Add">
        </form>

    </div>

</body>
