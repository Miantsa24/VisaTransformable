<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Type de demande - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:220px;padding:20px;">
        <h1>Choisissez le type de demande</h1>
        <form action="/step2-form" method="get">
            <button type="submit" name="type" value="nouveau_titre">Nouveau titre</button>
        </form>
        <button type="button" disabled style="margin-top:10px;">Duplicata (à venir)</button>
    </div>
</body>
</html>
