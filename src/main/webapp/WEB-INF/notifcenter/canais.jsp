<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<br><h2><b>Canais</b></h2><br/>

<div class="create-canal" id="div1">

    <br><h3>Create channel</h3></br>

    <select id="select1" onchange="onSelect()">
        <option value="" selected disabled hidden>Channel Type</option>
        <c:forEach var="entry" items="${classes_canais}"> <%-- same as ${classes_canais.entrySet()} --%>
            <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></option>
        </c:forEach>
    </select>

    <form id="form1" action="https://www.w3schools.com/action_page.php" method="get">

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

            <c:forEach var="currentValue" items="${entry.value}">

                var t = document.createTextNode(capitalizeFirstLetter("<c:out value="${currentValue}"/>: "));
                temp.appendChild(t);
                var x = document.createElement("INPUT");
                x.setAttribute("type", "text");
                x.setAttribute("name", "<c:out value="${currentValue}"/>"); //igual a usar "${currentValue}"!
                //x.setAttribute("value", "${currentValue}"); //valor predefinido
                temp.appendChild(x);
                temp.appendChild(document.createElement("br"));

            </c:forEach>
        }

    </c:forEach>

    var s = document.createElement("INPUT");
    s.setAttribute("type", "SUBMIT");
    s.setAttribute("value", "Submit");
    temp.appendChild(s);
}

</script>
