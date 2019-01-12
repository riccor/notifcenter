<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Applications/Senders/Notification Channels</title>

    <c:set var="urlPrefix" value="/notifcenter/aplicacoes/"/>
    <c:set var="canaisNotificacao" value="canaisnotificacao"/>
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
          padding: 8px;
        }

        #table1 tr:nth-child(even){background-color: #f2f2f2;}

        #table1 tr:hover {background-color: #ddd;}

        #table1 th {
          padding-top: 12px;
          padding-bottom: 12px;
          text-align: left;
          background-color: #009FE3;
          color: white;
        }
    </style>

</head>

<body>
    <h2><b>Applications/Senders/Notification channels manager</b></h2>

    <div class="list-applications-senders-notificationchannels" id="div1">

        <br><h4>Existing notification channels for sender ${sender.nome} (${sender.externalId}) from application ${application.name} (${application.externalId})</h4>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Channel</th>
                <th>Approved</th>
                <th>Actions</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="cno" items="${canaisnotificacao}">

                <c:if test="${id != null}">
                     <c:remove var="id"/>
                </c:if>

                <tr>
                    <c:forEach var="entry" items="${cno}">

                        <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingCanaisNotificacaoFromRemetente() --%>
                            <c:set var="id" value="${entry.value}"/>
                        </c:if>

                        <c:choose>
                            <c:when test="${id != null && entry.key == 'approved'}"> <%-- robustness --%>
                                <td>
                                    <form action="<c:out value="${urlPrefix}${application.externalId}${slash}${sender.externalId}${slash}${canaisNotificacao}"/>" onsubmit="return confirm('Do you really want to edit this notification channel?');" method="post" >
                                        <select name="aguardandoAprovacao">

                                            <c:choose>
                                                <c:when test="${entry.value == 'true'}">
                                                    <option selected="selected" value="true">True</option>
                                                    <option value="false">False</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option selected="selected" value="false">False</option>
                                                    <option value="true">True</option>
                                                </c:otherwise>
                                            </c:choose>

                                        </select>
                                        <input type="hidden" name="editCanalNotificacao" value="<c:out value="${id}"/>">
                                        <input type="submit" value="Edit">
                                    </form>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td><c:out value="${entry.value}"/></td>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>

                    <td>
                        <c:if test="${id != null}"> <%-- robustness --%>
                            <form action="<c:out value="${urlPrefix}${application.externalId}${slash}${sender.externalId}${slash}${canaisNotificacao}"/>" onsubmit="return confirm('Do you really want to delete this notification channel?');" method="post">
                                <input type="hidden" name="deleteCanalNotificacao" value="<c:out value="${id}"/>">
                                <input type="submit" value="Delete">
                            </form>

                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </div>

    <div class="create-canalnotificacao" id="div2">
        <br><h3>Add new notification channel</h3>

        <form id="form2" action="<c:out value="${urlPrefix}${application.externalId}${slash}${sender.externalId}${slash}${canaisNotificacao}"/>" method="post" >
            <table id="table2">
                <tr>
                    <td>Select channel:</td>
                    <td>
                        <select name="canal">
                            <c:forEach var="canal" items="${canais}">

                                <c:forEach var="entry" items="${canal}">
                                    <c:if test="${entry.key == 'id'}">
                                        <c:set var="idcanal" value="${entry.value}"/>
                                    </c:if>

                                    <c:if test="${entry.key == 'type'}">
                                        <c:set var="typecanal" value="${entry.value}"/>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${idcanal != null && typecanal != null}"> <%-- robustness --%>
                                    <option value="<c:out value="${idcanal}"/>"><c:out value="${typecanal} ${paropen}${idcanal}${parclose}"/></option>
                                </c:if>

                            </c:forEach>
                        </select>
                    </td>

                </tr>
            </table>
            <input type="hidden" name="createCanalNotificacao" value="does_not_matter">
            <input type="submit" value="Create">
        </form>

    </div>

</body>
