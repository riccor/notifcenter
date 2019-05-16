<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>Notifcenter - Channels</title>

    <c:set var="urlPrefix" value="/notifcenter/canais"/>
    <c:set var="inputTextSize" value="${150}"/>

    <style>
        #table1 {
          font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
          border-collapse: collapse;
          width: 100%;
        }

        #table1 td, #table1 th {
          border: 1px solid #ddd;
          padding: 2px;
        }

        #table1 tr:nth-child(even){background-color: #f2f2f2;}

        #table1 tr:hover {background-color: #ddd;}

        #table1 th {
          padding-top: 2px;
          padding-bottom: 2px;
          text-align: left;
          background-color: #009FE3;
          color: white;
        }
    </style>

</head>

<body>
    <h2><b>Channels</b></h2>

    <div class="changes-notifications" id="div0">
        <br><h4 style="color:#FF8000">${changesmessage}<h4>
    </div>

    <div class="list-channels" id="div1">

        <br><h3>Existing channels</h3>

        <table id="table1" style="width: 100%, box-sizing: border-box">
            <tr>
                <th>Id</th>
                <th>Class</th>
                <th>Authentication parameters</th>
                <th>Actions</th>
            </tr>

            <c:set var="formPrefix" value="form-"/>
            <c:forEach var="canal" items="${canais}">

                <c:if test="${id != null}">
                     <c:remove var="id"/>
                </c:if>

                <tr>
                    <c:forEach var="entry" items="${canal}">

                        <c:choose>
                            <c:when test="${entry.key == 'type' || entry.key == 'id'}">
                                <td><c:out value="${entry.value}"/></td>
                                <c:if test="${entry.key == 'id'}"> <%-- field id must come first on getExistingChannels() --%>
                                    <c:set var="id" value="${entry.value}"/>
                                    <form id="<c:out value="${formPrefix}${entry.value}"/>" action="<c:out value="${urlPrefix}"/>" onsubmit="return confirm('Do you really want to edit this channel?');" method="post">
                                        <input type="hidden" name="editChannel" value="<c:out value="${entry.value}"/>">
                                    </form>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${id != null}"> <%-- robustness --%>
                                        <td><%-- <b><c:out value="${entry.key}"/>:</b> --%><input type="text" size="${inputTextSize - 50}" name="<c:out value="${entry.key}"/>" value="<c:out value="${entry.value}"/>" form="<c:out value="${formPrefix}${id}"/>"></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><b><c:out value="${entry.key}"/>:</b> <c:out value="${entry.value}"/></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <td>
                        <c:if test="${id != null}"> <%-- robustness --%>
                            <form action="<c:out value="${urlPrefix}"/>" onsubmit="return confirm('Do you really want to delete this channel?');" method="post">
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

        <br><h3>Add new channel</h3>

        <select id="select2" onchange="onSelect()">
            <option value="" selected disabled hidden>Channel class</option>
            <c:forEach var="entry" items="${classes_canais}"> <%-- same as ${classes_canais.entrySet()} --%>
                <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></option>
            </c:forEach>
        </select>

        <form id="form2" action="<c:out value="${urlPrefix}"/>" method="post" >
            <table id="table2"></table>
        </form>

    </div>


</body>

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
            h.setAttribute("name", "createChannel");
            h.setAttribute("value", key);
            temp.appendChild(h);

            //criar tabela com campos de formulario para preencher
            var table2 = document.createElement("TABLE");
            table2.setAttribute("id", "table2");
            temp.appendChild(table2);

            <c:forEach var="currentValue" items="${entry.value}">

                var tr0 = document.createElement("TR");
                table2.appendChild(tr0);

                var td0 = document.createElement("TD");
                tr0.appendChild(td0);

                var b = document.createElement("B");
                b.innerHTML = "Example config:";
                td0.appendChild(b);

                var tr = document.createElement("TR");
                table2.appendChild(tr);

                var td1 = document.createElement("TD");
                tr.appendChild(td1);

                //var k = document.createTextNode("<c:out value="${currentValue}"/>");

                var ttt = "<c:out value="${currentValue}"/>";
                var rrr = ttt.replace(/&#034;/g,'"');
                var k = document.createTextNode(rrr);
                //k.setAttribute("value", "<c:out value="${currentValue}"/>");
                //k.setAttribute("size", "<c:out value="${inputTextSize}"/>");
                td1.appendChild(k);

                var tr2 = document.createElement("TR");
                table2.appendChild(tr2);

                var td2 = document.createElement("TD");
                tr2.appendChild(td2);

                var v = document.createElement("INPUT");
                //v.setAttribute("rows", "3");
                //v.setAttribute("cols", "100");
                v.setAttribute("size", "<c:out value="${inputTextSize}"/>");
                v.setAttribute("type", "text");
                //v.setAttribute("name", "<c:out value="${currentValue}"/>"); //igual a usar "${currentValue}"!
                v.setAttribute("name", "config");
                //v.setAttribute("value", "${currentValue}"); //predefined value
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