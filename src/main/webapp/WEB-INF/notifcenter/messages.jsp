<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<bold>Mensagem:</bold><br/> <%-- ${message} --%>

<!-- <c:out value="${someVar}"/> -->

<div class="view-message">
    Assunto: ${message.assunto}<br/>
    Texto curto: ${message.textoCurto}<br/>
    Texto longo: ${message.textoLongo}<br/>
    Anexos:<br/> <%--  <${message.attachments}<br/> --%>

    <div class="anexos-list">
      <c:forEach var="anexo" items="${message.attachments}">
        ${anexo.externalId}<br/>
      </c:forEach>
    </div>


</div>

