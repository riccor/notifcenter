<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<br><b>Canais</b><br/>

<div class="create-canal" id="div1">

    <br>Create channel</br>

    <select id="select1" onchange="onSelect()">
        <c:forEach var="entry" items="${classes_canais}"> <%-- same as ${classes_canais.entrySet()} --%>
            <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></option>
        </c:forEach>
    </select>

    <form id="form1" action="/action_page.php">
        <c:forEach var="entry" items="${classes_canais}">
            <c:forEach var="currentValue" items="${entry.value}">
                <br><c:out value="${currentValue}"/>: <input type="text" name="<c:out value="${currentValue}"/>" value="<c:out value="${currentValue}"/>"></br>
            </c:forEach>
        </c:forEach>
        <br><input type="submit" value="Submit"></br>
    </form>

</div>


<script>

function onSelect(){
    var sel = document.div1.select1;
    var opt = sel.options[sel.selectedIndex].value;
    var txt = sel.options[sel.selectedIndex].text;

    var temp = document.div1.form1;

    for (var i = 0; i < temp.length; i++) {
        var id = document.getElementById(document.div1.form1[i].id);
        id.parentNode.removeChild(id);
    }

    <%-- temp.options[sel.selectedIndex].selected = true; --%>
}

</script>
