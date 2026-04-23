<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Type de demande - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:220px;padding:20px;">
        <h1>Choix du type de visa</h1>
        <form action="/step4-documents" method="post">
            <label>Type de visa :
                <select name="type_visa" required>
                    <option value="investisseur">Investisseur</option>
                    <option value="travailleur">Travailleur</option>
                </select>
            </label>
            <br><br>
            <button type="submit">Choisir</button>
        </form>
    </div>
</body>
</html>
