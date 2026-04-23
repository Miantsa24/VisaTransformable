<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmation de la demande</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:220px;padding:20px;">
        <h1>${typeVisaLabel}</h1>
        <p>Merci de vérifier les informations avant soumission définitive.</p>

        <c:if test="${not empty error}">
            <div style="color:red; margin-bottom:15px;">${error}</div>
        </c:if>

        <h2>État civil</h2>
        <p><strong>Nom :</strong> ${demandeur.nom}</p>
        <p><strong>Prénoms :</strong> ${demandeur.prenoms}</p>
        <p><strong>Date de naissance :</strong> ${demandeur.dateNaissance}</p>
        <p><strong>Lieu de naissance :</strong> ${demandeur.lieuNaissance}</p>
        <p><strong>Téléphone :</strong> ${demandeur.telephone}</p>
        <p><strong>Email :</strong> ${demandeur.email}</p>
        <p><strong>Adresse :</strong> ${demandeur.adresse}</p>
        <p><strong>Situation familiale :</strong> ${demandeur.situationFamiliale.libelle}</p>
        <p><strong>Nationalité :</strong> ${demandeur.nationalite.libelle}</p>

        <h2>Passeport</h2>
        <p><strong>Numéro :</strong> ${passeport.numeroPasseport}</p>
        <p><strong>Date de délivrance :</strong> ${passeport.dateDelivrance}</p>
        <p><strong>Date d'expiration :</strong> ${passeport.dateExpiration}</p>
        <p><strong>Pays de délivrance :</strong> ${passeport.paysDelivrance}</p>

        <h2>Visa transformable</h2>
        <p><strong>Référence :</strong> ${visa.reference}</p>
        <p><strong>Date de début :</strong> ${visa.dateDebut}</p>
        <p><strong>Date de fin :</strong> ${visa.dateFin}</p>

        <h2>Documents communs choisis</h2>
        <ul>
            <c:forEach var="doc" items="${documentsCommunsChoisis}">
                <li>${doc.libelle}</li>
            </c:forEach>
        </ul>

        <h2>Documents spécifiques choisis</h2>
        <ul>
            <c:forEach var="doc" items="${documentsSpecifiquesChoisis}">
                <li>${doc.libelle}</li>
            </c:forEach>
        </ul>

        <form action="/backoffice/demande" method="post">
            <c:forEach var="doc" items="${documentsSelectionnees}">
                <input type="hidden" name="documents" value="${doc.id}" />
            </c:forEach>
            <input type="hidden" name="type_visa" value="${typeVisa}" />
            <button type="submit">Soumettre la demande</button>
        </form>
    </div>
</body>
</html>
