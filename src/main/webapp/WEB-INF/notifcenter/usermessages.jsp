<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Messages</title>

    <c:set var="urlPrefix" value="/notifcenter/mensagens"/>
    <c:set var="slash" value="/"/>
    <c:set var="status" value="deliverystatuses"/>

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
    <h2><b>Messages manager</b></h2>

    <div class="list-users-contacts" id="div1">

        <br><h3>Messages sent to user ${utilizador}</h3>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Delivery date</th>
                <th>Sender</th>
                <th>Subject</th>
                <th>Short text</th>
                <th>Long text</th>
                <th>Attachments</th>
                <th>Actions</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="mgs" items="${messages}">

                <c:if test="${id != null}">
                     <c:remove var="id"/>
                </c:if>

                <tr>
                    <c:forEach var="entry" items="${mgs}">

                        <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingMensagens() --%>
                            <c:set var="id" value="${entry.value}"/>
                        </c:if>

                        <td><c:out value="${entry.value}"/></td>

                    </c:forEach>

                    <td>

                        <c:choose>
                            <c:when test="${id != null}"> <%-- robustness --%>
                                <form action="<c:out value="${urlPrefix}${slash}${id}"/>" method="get">
                                    <input type="submit" value="View">
                                </form>
                            </c:when>
                            <c:otherwise>
                                id error
                            </c:otherwise>
                        </c:choose>
                    </td>

                </tr>
            </c:forEach>
        </table>

    </div>

</body>
