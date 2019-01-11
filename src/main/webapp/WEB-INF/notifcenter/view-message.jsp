<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>Notifcenter - View message</title>
</head>

<body>
    <h3><b>Message ${message.externalId}:</b></h3>

    <div class="view-message">

        <br><b>Sender:</b> ${message.getCanalNotificacao().getRemetente().nome}
        <br><b>Application:</b> ${message.getCanalNotificacao().getRemetente().getAplicacao().name}
        <br><b>Subject:</b> ${message.assunto}
        <br><b>Short text:</b> ${message.textoCurto}
        <br><b>Long text:</b> ${message.textoLongo}
        <br><b>Attachments:</b> <%--  <${message.attachments} --%>

        <div class="attachments_links-list">
          <c:forEach var="entry" items="${attachments_links}">
            <a href="<c:url value="${entry.value}"/>"><c:out value="${entry.key}"/></a></br>
          </c:forEach>
        </div>

    </div>
<body>


<%--
    <c:set var="continueExecuting" value="false"/>


    <c:set var="myVal" value="Hello"/>

    <c:if test="${continueExecuting}">

    </c:if>


    <p id="demo0">Click the button to change the text in this paragraph</p>
    <p id="demo1">Click the button to change the text in this paragraph</p>
    <p id="demo2">Click the button to change the text in this paragraph</p>

    inside <script>:
    var key = "${entry.key}";
    document.getElementById("demo0").innerHTML = key;


    <c:forEach var="entry" items="${multiMap}">
        <br/>-> Key: <c:out value="${entry.key}"/>
        <br/>-> Values for this key:
        <c:forEach var="currentValue" items="${entry.value}">
            <br/>|---> value: <c:out value="${currentValue}"/>
        </c:forEach>
    </c:forEach>
--%>


<%--

                <c:forEach var="entry" items="${classes_canais}">

                    <c:if test="${item.getClass().simpleName == entry.key}" >

                        <c:forEach var="currentValue" items="${entry.value}">

                            <td><c:out value="${currentValue}"/></td>

                        </c:forEach>

                    </c:if>

                </c:forEach>

--%>