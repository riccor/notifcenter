<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3><b>Mensagem ${message.externalId}:</b></h3>

<div class="view-message">

    <br><b>Assunto:</b> ${message.assunto}
    <br><b>Texto curto:</b> ${message.textoCurto}
    <br><b>Texto longo:</b> ${message.textoLongo}
    <br><b>Anexos:</b> <%--  <${message.attachments} --%>

    <div class="attachments_links-list">
      <c:forEach var="entry" items="${attachments_links}">
        <a href="<c:url value="${entry.value}"/>"><c:out value="${entry.key}"/></a></br>
      </c:forEach>
    </div>

</div>



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