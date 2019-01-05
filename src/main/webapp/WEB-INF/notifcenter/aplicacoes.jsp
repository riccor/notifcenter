<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
    <title>Notifcenter - Applications</title>

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
<br><h2><b>Applications manager</b></h2>

<div class="list-applications" id="div1">

    <br><h3>Existing applications</h3>

    <table id="table1" style="width: 100%, box-sizing: border-box">
        <tr>
            <th>Name</th>
            <th>ClientId</th>
            <th>Author</th>
            <th>Permissions</th>
            <th>Description</th>
            <th>Site_url</th>
            <th>Redirect_uri</th>
            <th>Client_secret</th>
        </tr>

        <c:set var="formPrefix" value="form-"/>
        <c:forEach var="app" items="${aplicacoes}">

            <c:if test="${id != null}">
                 <c:remove var="id"/>
            </c:if>

            <tr>
                <c:forEach var="entry" items="${app}">

                    <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingApps() --%>
                        <c:set var="id" value="${entry.value}"/>
                        <form id="<c:out value="${formPrefix}${entry.value}"/>" action="/notifcenter/notifcenter/aplicacoes" onsubmit="return confirm('Do you really want to edit this application?');" method="post">
                            <input type="hidden" name="editApp" value="<c:out value="${entry.value}"/>">
                        </form>
                    </c:if>

                    <c:choose>
                        <c:when test="${id != null}"> <%-- robustness --%>
                            <td><input type="text" name="<c:out value="${entry.key}"/>" value="<c:out value="${entry.value}"/>" form="<c:out value="${formPrefix}${id}"/>"></td>
                        </c:when>
                        <c:otherwise>
                            <td><c:out value="${entry.value}"/></td>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
                <td>
                    <c:if test="${id != null}"> <%-- robustness --%>
                        <form action="/notifcenter/notifcenter/aplicacoes" onsubmit="return confirm('Do you really want to delete this application?');" method="post">
                            <input type="hidden" name="deleteApp" value="<c:out value="${id}"/>">
                            <input type="submit" value="Delete">
                        </form>

                        <input type="submit" value="Update" form="<c:out value="${formPrefix}${id}"/>">
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

</div>


<div class="create-app" id="div2">
    <br><h3>Add new application</h3>

    <form:form id="form2" action="/notifcenter/notifcenter/aplicacoes" method="post" >
        <table id="table2">
            <c:forEach var="param" items="${parametros_app}">
                <tr>
                    <td><c:out value="${param}"/></td>
                    <td><input type="text" name="<c:out value="${param}"/>" value=""></td>
                </tr>
            </c:forEach>
        </table>
        <input type="hidden" name="createApp" value="does_not_matter">
        <input type="submit" value="Create">
    </form:form>

</div>

</body>
