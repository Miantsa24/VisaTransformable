<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulaire - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:220px;padding:20px;">
        <h1>Formulaire de demande</h1>
        <c:if test="${not empty erreurs}">
            <div style="color:red">${erreurs}</div>
        </c:if>
        <form action="/step3-typeVisa" method="post">
            <!-- Bloc 1 — État civil -->
            <fieldset>
                <legend>État civil</legend>
                <label>Nom* : <input type="text" name="nom" required></label><br>
                <label>Prénoms: <input type="text" name="prenoms" required></label><br>
                <label>Date de naissance* : <input type="date" name="date_naissance" required></label><br>
                <label>Lieu de naissance* : <input type="text" name="lieu_naissance" required></label><br>
                <label>Situation matrimoniale* :
    <select name="situation_matrimoniale" required>
        <c:forEach var="sf" items="${situationsFamiliales}">
            <option value="${sf.id}">${sf.libelle}</option>
        </c:forEach>
    </select>
</label><br>
                <label>Nationalité* :
    <select name="nationalite" required>
        <c:forEach var="nat" items="${nationalites}">
            <option value="${nat.id}">${nat.libelle}</option>
        </c:forEach>
    </select>
</label><br>
                <label>Profession: <input type="text" name="profession"></label><br>
                <label>Adresse locale: <input type="text" name="adresse_locale"></label><br>
                <label>Contact (Email)* : <input type="email" name="email" required></label><br>
                <label>Contact (Téléphone)* : <input type="tel" name="telephone" required></label><br>
            </fieldset>
            <br>
            <!-- Bloc 2 — Passeport -->
            <fieldset>
                <legend>Passeport</legend>
                <label>Numéro de passeport* : <input type="text" name="numero_passeport" required></label><br>
                <label>Date de délivrance: <input type="date" name="date_delivrance"></label><br>
                <label>Date d'expiration: <input type="date" name="date_expiration"></label><br>
                <label>Pays de délivrance: <input type="text" name="pays_delivrance"></label><br>
            </fieldset>
            <br>
            <!-- Bloc 3 — Visa transformable -->
            <fieldset>
                <legend>Visa transformable</legend>
                <label>Référence du visa* : <input type="text" name="reference_visa" required></label><br>
                <label>Date d'entrée à Madagascar: <input type="date" name="date_entree"></label><br>
                <label>Lieu d'entrée: <input type="text" name="lieu_entree"></label><br>
                <label>Date d'expiration du visa: <input type="date" name="date_expiration_visa"></label><br>
            </fieldset>
            <br>
            <button type="submit">Suivant ></button>
        </form>
    </div>
</body>
</html>
