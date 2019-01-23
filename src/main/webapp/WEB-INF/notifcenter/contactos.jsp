<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Users/Contacts</title>

    <c:set var="urlPrefix" value="/notifcenter/utilizadores/"/>
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
    <h2><b>Users/Contacts manager</b></h2>

    <div class="list-users-contacts" id="div1">

        <br><h3>Existing contacts for user ${user.username} (${user.externalId})</h3>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Channel</th>
                <th>Data</th>
                <th>Actions</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="conts" items="${contacts}">

                <c:if test="${id != null}">
                     <c:remove var="id"/>
                </c:if>

                <tr>
                    <c:forEach var="entry" items="${conts}">

                        <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingUserContactos() --%>
                            <c:set var="id" value="${entry.value}"/>
                            <form id="<c:out value="${formPrefix}${entry.value}"/>" action="<c:out value="${urlPrefix}${user.externalId}"/>" onsubmit="return confirm('Do you really want to edit this contact?');" method="post">
                                <input type="hidden" name="editContacto" value="<c:out value="${entry.value}"/>">
                            </form>
                        </c:if>

                        <c:choose>
                            <c:when test="${id != null && entry.key != 'id' && entry.key != 'channel'}"> <%-- robustness AND disallow edit id and channel --%>
                                <td><input type="text" name="<c:out value="${entry.key}"/>" value="<c:out value="${entry.value}"/>" form="<c:out value="${formPrefix}${id}"/>"></td>
                            </c:when>
                            <c:otherwise>
                                <td><c:out value="${entry.value}"/></td>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>

                    <td>
                        <c:if test="${id != null}"> <%-- robustness --%>
                            <form action="<c:out value="${urlPrefix}${user.externalId}"/>" onsubmit="return confirm('Do you really want to delete this contact?');" method="post">
                                <input type="hidden" name="deleteContacto" value="<c:out value="${id}"/>">
                                <input type="submit" value="Delete">
                            </form>

                            <input type="submit" value="Update" form="<c:out value="${formPrefix}${id}"/>">
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </div>

    <div class="create-contact" id="div2">
        <br><h3>Add new contact</h3>

        <form id="form2" action="<c:out value="${urlPrefix}${user.externalId}"/>" method="post" >
            <table id="table2">

                <c:forEach var="parcont" items="${parametros_contacto}">
                    <tr>
                        <td><c:out value="${fn:toUpperCase(fn:substring(parcont, 0, 1))}${fn:substring(parcont, 1, fn:length(parcont))}"/>:</td>
                        <td><input type="text" name="<c:out value="${parcont}"/>" value=""></td>
                    </tr>
                </c:forEach>

                <tr>
                    <td>Select channel:</td>
                    <td>
                        <select name="channel">
                            <c:forEach var="channel" items="${canais}">

                                <c:forEach var="entry" items="${channel}">
                                    <c:if test="${entry.key == 'id'}">
                                        <c:set var="idchannel" value="${entry.value}"/>
                                    </c:if>

                                    <c:if test="${entry.key == 'type'}">
                                        <c:set var="typechannel" value="${entry.value}"/>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${idchannel != null && typechannel != null}"> <%-- robustness --%>
                                    <option value="<c:out value="${idchannel}"/>"><c:out value="${typechannel} ${paropen}${idchannel}${parclose}"/></option>
                                </c:if>

                            </c:forEach>
                        </select>
                    </td>
                </tr>

            </table>
            <input type="hidden" name="createContacto" value="does_not_matter">
            <input type="submit" value="Create">
        </form>

    </div>

</body>
