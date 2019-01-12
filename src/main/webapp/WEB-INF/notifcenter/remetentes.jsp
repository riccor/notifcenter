<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Applications/Senders</title>

    <c:set var="urlPrefix" value="/notifcenter/aplicacoes/"/>
    <c:set var="gruposDestinatarios" value="gruposdestinatarios"/>
    <c:set var="canaisNotificacao" value="canaisnotificacao"/>
    <c:set var="slash" value="/"/>

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
    <h2><b>Applications/Senders manager</b></h2>

    <div class="list-applications-senders" id="div1">

        <br><h3>Existing senders for application ${application.name} (${application.externalId})</h3>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Notification Channels</th>
                <th>Receiver Groups</th>
                <th>Actions</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="rems" items="${remetentes}">

                <c:if test="${id != null}">
                     <c:remove var="id"/>
                </c:if>

                <tr>
                    <c:forEach var="entry" items="${rems}">

                        <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingAppRemetentes() --%>
                            <c:set var="id" value="${entry.value}"/>
                            <form id="<c:out value="${formPrefix}${entry.value}"/>" action="<c:out value="${urlPrefix}${application.externalId}"/>" onsubmit="return confirm('Do you really want to edit this sender?');" method="post">
                                <input type="hidden" name="editRemetente" value="<c:out value="${entry.value}"/>">
                            </form>
                        </c:if>

                        <c:choose>
                            <c:when test="${id != null && entry.key != 'id'}"> <%-- robustness AND disallow edit id --%>
                                <td><input type="text" name="<c:out value="${entry.key}"/>" value="<c:out value="${entry.value}"/>" form="<c:out value="${formPrefix}${id}"/>"></td>
                            </c:when>
                            <c:otherwise>
                                <td><c:out value="${entry.value}"/></td>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>

                    <td>
                        <c:choose>
                            <c:when test="${id != null}"> <%-- robustness --%>
                                <form action="<c:out value="${urlPrefix}${application.externalId}${slash}${id}${slash}${canaisNotificacao}"/>" method="get">
                                    <input type="submit" value="Go">
                                </form>
                            </c:when>
                            <c:otherwise>
                                id error
                            </c:otherwise>
                        </c:choose>
                    </td>


                    <td>
                        <c:choose>
                            <c:when test="${id != null}"> <%-- robustness --%>
                                <form action="<c:out value="${urlPrefix}${application.externalId}${slash}${id}${slash}${gruposDestinatarios}"/>" method="get">
                                    <input type="submit" value="Go">
                                </form>
                            </c:when>
                            <c:otherwise>
                                id error
                            </c:otherwise>
                        </c:choose>
                    </td>


                    <td>
                        <c:if test="${id != null}"> <%-- robustness --%>
                            <form action="<c:out value="${urlPrefix}${application.externalId}"/>" onsubmit="return confirm('Do you really want to delete this application?');" method="post">
                                <input type="hidden" name="deleteRemetente" value="<c:out value="${id}"/>">
                                <input type="submit" value="Delete">
                            </form>

                            <input type="submit" value="Update" form="<c:out value="${formPrefix}${id}"/>">
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </div>

    <div class="create-remetente" id="div2">
        <br><h3>Add new sender</h3>

        <form id="form2" action="<c:out value="${urlPrefix}${application.externalId}"/>" method="post" >
            <table id="table2">
                <c:forEach var="parsender" items="${parametros_remetente}">
                    <tr>
                        <td><c:out value="${fn:toUpperCase(fn:substring(parsender, 0, 1))}${fn:substring(parsender, 1, fn:length(parsender))}"/>:</td>
                        <td><input type="text" name="<c:out value="${parsender}"/>" value=""></td>
                    </tr>
                </c:forEach>
            </table>
            <input type="hidden" name="createRemetente" value="does_not_matter">
            <input type="submit" value="Create">
        </form>

    </div>

</body>
