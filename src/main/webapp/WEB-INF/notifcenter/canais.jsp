<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <title>Notifcenter - Channels</title>

    <style>
    #table1 {
      font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
      border-collapse: collapse;
      width: 100%;
    }

    #table1 td, #table1 th {
      border: 1px solid #ddd;
      padding: 8px;
    }

    #table1 tr:nth-child(even){background-color: #f2f2f2;}

    #table1 tr:hover {background-color: #ddd;}

    #table1 th {
      padding-top: 12px;
      padding-bottom: 12px;
      text-align: left;
      background-color: #009FE3;
      color: white;
    }
    </style>

</head>

<body>
<br><h2><b>Channels manager</b></h2><br/>

<div class="list-channels" id="div1">

    <br><h3>Existing channels</h3></br>

    <table id="table1" style="width: 100%, box-sizing: border-box">
        <tr>
            <th>Type</th>
            <th>Id</th>
            <th>Email</th>
            <th colspan="5">Authentication parameters</th>
        </tr>

        <c:set var="formPrefix" value="form-"/>
        <c:forEach var="canal" items="${canais}">

            <c:if test="${id != null}">
                 <c:remove var="id"/>
            </c:if>

            <tr>
                <c:forEach var="entry" items="${canal}">
                    <c:choose>
                        <c:when test="${entry.key == 'type' || entry.key == 'email' || entry.key == 'id'}">
                            <td><c:out value="${entry.value}"/></td>
                            <c:if test="${entry.key == 'id'}"> <%-- field id must come first --%>
                                <c:set var="id" value="${entry.value}"/>
                                <form id="<c:out value="${formPrefix}${entry.value}"/>" action="/notifcenter/notifcenter/canais" onsubmit="return confirm('Do you really want to edit this channel?');" method="post">
                                    <input type="hidden" name="editChannel" value="<c:out value="${entry.value}"/>">
                                </form>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${id != null}">
                                    <td><b><c:out value="${entry.key}"/>:</b> <input type="text" name="<c:out value="${entry.key}"/>" value="<c:out value="${entry.value}"/>" form="<c:out value="${formPrefix}${id}"/>"></td>
                                </c:when>
                                <c:otherwise>
                                    <td><b><c:out value="${entry.key}"/>:</b> <c:out value="${entry.value}"/></td>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <td>
                    <c:if test="${id != null}">
                        <form action="/notifcenter/notifcenter/canais" onsubmit="return confirm('Do you really want to delete this channel?');" method="post">
                            <input type="hidden" name="deleteChannel" value="<c:out value="${id}"/>">
                            <input type="submit" value="Delete">
                        </form>

                        <input type="submit" value="Update" form="<c:out value="${formPrefix}${id}"/>">
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

</div>


<div class="create-canal" id="div2">

    <br><h3>Add new channel</h3></br>

    <select id="select2" onchange="onSelect()">
        <option value="" selected disabled hidden>Channel Type</option>
        <c:forEach var="entry" items="${classes_canais}"> <%-- same as ${classes_canais.entrySet()} --%>
            <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></option>
        </c:forEach>
    </select>

    <form:form id="form2" action="/notifcenter/notifcenter/canais" method="post" >
        <table id="table2"></table>
    </form:form>

</div>


<script>

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function onSelect() {

    var sel = document.getElementById("select2");
    var opt = sel.options[sel.selectedIndex].value;

    var temp = document.getElementById("form2");

    //apagar elementos de form2:
  	while (temp.firstChild) {
   		temp.removeChild(temp.firstChild);
  	}

    <c:forEach var="entry" items="${classes_canais}">

        var key = "${entry.key}";

        if (key.toUpperCase() === opt.toUpperCase()) {

            var h = document.createElement("INPUT");
            h.setAttribute("type", "HIDDEN");
            h.setAttribute("name", "channelType");
            h.setAttribute("value", key);
            temp.appendChild(h);

            //criar tabela com campos de formulario para preencher
            var table2 = document.createElement("TABLE");
            table2.setAttribute("id", "table2");
            temp.appendChild(table2);

            <c:forEach var="currentValue" items="${entry.value}">

                var tr = document.createElement("TR");
                table2.appendChild(tr);

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
</body>