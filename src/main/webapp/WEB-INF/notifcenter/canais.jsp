<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
    <title>Notifcenter - Canais</title>
</head>


<br><h2><b>Canais</b></h2><br/>

<div class="create-canal" id="div1">

    <br><h3>Create channel</h3></br>

    <select id="select1" onchange="onSelect()">
        <option value="" selected disabled hidden>Channel Type</option>
        <c:forEach var="entry" items="${classes_canais}"> <%-- same as ${classes_canais.entrySet()} --%>
            <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></option>
        </c:forEach>
    </select>

    <form id="form1" action="/notifcenter/notifcenter/postcanal" method="post">
    <%-- <form:form action="https://www.w3schools.com/action_page.php" method="get"> --%>
        <table id="table1"></table>
    <%-- </form:form> --%>
    </form>

</div>


<script>

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function onSelect() {

    var sel = document.getElementById("select1");
    var opt = sel.options[sel.selectedIndex].value;

    var temp = document.getElementById("form1");

    //apagar elementos de form1:
  	while (temp.firstChild) {
   		temp.removeChild(temp.firstChild);
  	}

    <c:forEach var="entry" items="${classes_canais}">

        var key = "${entry.key}";

        if (key.toUpperCase() === opt.toUpperCase()) {

            var h = document.createElement("INPUT");
            h.setAttribute("type", "HIDDEN");
            h.setAttribute("name", "classType");
            h.setAttribute("value", key);
            temp.appendChild(h);

            //criar tabela com campos de formulario para preencher
            var table1 = document.createElement("TABLE");
            table1.setAttribute("id", "table1");
            temp.appendChild(table1);

            <c:forEach var="currentValue" items="${entry.value}">

                var tr = document.createElement("TR");
                table1.appendChild(tr);

                var td1 = document.createElement("TD");
                tr.appendChild(td1);

                var k = document.createTextNode(capitalizeFirstLetter("<c:out value="${currentValue}"/>: "));
                td1.appendChild(k);

                var td2 = document.createElement("TD");
                tr.appendChild(td2);

                var v = document.createElement("INPUT");
                v.setAttribute("type", "text");
                v.setAttribute("name", "<c:out value="${currentValue}"/>"); //igual a usar "${currentValue}"!
                //v.setAttribute("value", "${currentValue}"); //valor predefinido
                td2.appendChild(v);
                //temp.appendChild(document.createElement("BR"));

            </c:forEach>

        }

    </c:forEach>

    var s = document.createElement("INPUT");
    s.setAttribute("type", "SUBMIT");
    s.setAttribute("value", "Submit");
    temp.appendChild(s);
}

</script>
