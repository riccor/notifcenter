<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Messages/Delivery Statuses</title>

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
    <h2><b>Messages/Delivery statuses</b></h2>

    <div class="list-messages-delivery-statuses" id="div1">

        <br><h3>Current delivery statuses for message ${message.externalId}</h3>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Channel</th>
                <th>User</th>
                <th>External Id</th>
                <th>Status</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="dsts" items="${deliverystatuses}">

                <tr>
                    <c:forEach var="entry" items="${dsts}">
                        <td><c:out value="${entry.value}"/></td>
                    </c:forEach>
                </tr>

            </c:forEach>
        </table>

    </div>

</body>
