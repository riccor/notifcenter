<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<b>Mensagem:</b><br/> <%-- ${message} --%>

<!-- <c:out value="${someVar}"/> -->

<div class="view-message">

    Assunto: ${message.assunto}<br/>
    Texto curto: ${message.textoCurto}<br/>
    Texto longo: ${message.textoLongo}<br/>
    Anexos:<br/> <%--  <${message.attachments}<br/> --%>

    <div class="attachments_links-list">
      <c:forEach var="link" items="${attachments_links}">
        <a href="<c:url value="${link}"/>"><c:out value="${link}"/></a><br/>
      </c:forEach>
    </div>

</div>

