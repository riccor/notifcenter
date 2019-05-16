<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>Notifcenter - View Message</title>
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
