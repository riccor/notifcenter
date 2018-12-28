<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<br><b>Canais</b><br/>

<div class="create-canal">

    <br>Create channel</br>

    <select>
        <c:forEach var="entry" items="${classes_canais}"> <%-- same as ${classes_canais.entrySet()} --%>
            <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></option>
        </c:forEach>
    </select>

    <form action="/action_page.php">
        <c:forEach var="entry" items="${classes_canais}">
            <c:forEach var="currentValue" items="${entry.value}">
                <br><c:out value="${currentValue}"/>: <input type="text" name="<c:out value="${currentValue}"/>" value="<c:out value="${currentValue}"/>"></br>
            </c:forEach>
        </c:forEach>
        <br><input type="submit" value="Submit"></br>
    </form>

</div>
