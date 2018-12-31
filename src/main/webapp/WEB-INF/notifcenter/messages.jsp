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