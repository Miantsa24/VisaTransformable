<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Historique de la demande</title>
    <style>
    body {
        margin: 0;
        font-family: "Inter", "Segoe UI", Arial, sans-serif;
        background: #f1f5f9;
        color: #0f172a;
    }

    #main {
        margin-left: 240px;
        padding: 40px;
        min-height: 100vh;
        background: #f1f5f9;
    }

    .container {
        max-width: 1100px;
        margin: auto;
        background: #ffffff;
        padding: 32px;
        border-radius: 16px;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
    }

    h1 {
        font-size: 28px;
        margin-bottom: 10px;
        color: #0f172a;
    }

    h2 {
        margin-top: 35px;
        font-size: 20px;
        color: #1e293b;
        border-left: 4px solid #2563eb;
        padding-left: 10px;
    }

    h3 {
        font-size: 16px;
        margin-bottom: 10px;
        color: #334155;
    }

    p {
        color: #475569;
        line-height: 1.6;
    }

    /* GRID CARDS */
    .grid-3 {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 20px;
        margin-top: 20px;
    }

    .card {
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-radius: 14px;
        padding: 18px;
        box-shadow: 0 4px 12px rgba(15, 23, 42, 0.05);
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .card:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(15, 23, 42, 0.08);
    }

    /* TABLE STYLE */
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
        background: white;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(15, 23, 42, 0.05);
    }

    th {
        background: #2563eb;
        color: white;
        text-align: left;
        padding: 12px;
        font-size: 14px;
    }

    td {
        padding: 12px;
        border-bottom: 1px solid #e2e8f0;
        font-size: 14px;
        color: #334155;
    }

    tr:hover td {
        background: #f8fafc;
    }

    /* LISTES DOCUMENTS */
    ul {
        padding-left: 18px;
        margin-top: 10px;
    }

    ul li {
        padding: 6px 0;
        color: #334155;
    }

    /* HEADER INFO BOX */
    .info-box {
        background: #eff6ff;
        border: 1px solid #dbeafe;
        padding: 14px;
        border-radius: 12px;
        margin-top: 15px;
    }

    /* BACK BUTTON */
    a {
        display: inline-block;
        margin-top: 20px;
        padding: 10px 16px;
        background: #e2e8f0;
        color: #0f172a;
        border-radius: 10px;
        text-decoration: none;
        font-weight: 600;
        transition: 0.2s;
    }

    a:hover {
        background: #cbd5e1;
    }

    /* RESPONSIVE */
    @media (max-width: 900px) {
        .grid-3 {
            grid-template-columns: 1fr;
        }

        #main {
            padding: 20px;
        }
    }
</style>
</head>

<body>
<jsp:include page="sidebar.jsp" />

<div id="main" style="margin-left:240px;padding:40px;background:#f8fafc;min-height:100vh;">

<div class="container">

    <!-- HEADER -->
    <h1>Historique de la demande de ${demande.demandeur.nom} ${demande.demandeur.prenoms}</h1>

    <p>
        <strong>Type de demande :</strong> ${demande.typeDemande.libelle}<br>
        <strong>Type de visa :</strong> ${typeVisaSelectionne}
    </p>

    <p>
        <strong>Dernière modification :</strong>
        <c:choose>
            <c:when test="${not empty modifications}">
                <fmt:formatDate value="${modifications[0].dateModification}" pattern="yyyy-MM-dd"/>
            </c:when>
            <c:otherwise>Aucune</c:otherwise>
        </c:choose>
    </p>

    <!-- ========================= -->
    <!-- FICHE DE RENSEIGNEMENT -->
    <!-- ========================= -->

    <h2>Fiche de renseignement</h2>

    <div class="grid-3">

        <!-- ETAT CIVIL -->
        <div class="card">
            <h3>Etat civil</h3>
            Nom : ${demande.demandeur.nom}<br>
            Prénoms : ${demande.demandeur.prenoms}<br>
            Date de naissance : <fmt:formatDate value="${demande.demandeur.dateNaissance}" pattern="yyyy-MM-dd"/><br>
            Lieu de naissance : ${demande.demandeur.lieuNaissance}<br>
            Email : ${demande.demandeur.email}<br>
            Téléphone : ${demande.demandeur.telephone}<br>
            Situation familiale : ${demande.demandeur.situationFamiliale.libelle}<br>
            Nationalité : ${demande.demandeur.nationalite.libelle}<br>
            Adresse : ${demande.demandeur.adresse}<br>
        </div>

        <!-- PASSEPORT -->
        <div class="card">
            <h3>Passeport</h3>
            Numéro : ${demande.visa.passeport.numeroPasseport}<br>
            Délivrance : <fmt:formatDate value="${demande.visa.passeport.dateDelivrance}" pattern="yyyy-MM-dd"/><br>
            Expiration : <fmt:formatDate value="${demande.visa.passeport.dateExpiration}" pattern="yyyy-MM-dd"/>
        </div>

        <!-- VISA -->
        <div class="card">
            <h3>Visa</h3>
            Référence : ${demande.visa.reference}<br>
            Début : <fmt:formatDate value="${demande.visa.dateDebut}" pattern="yyyy-MM-dd"/><br>
            Fin : <fmt:formatDate value="${demande.visa.dateFin}" pattern="yyyy-MM-dd"/>
        </div>

    </div>

    <!-- ========================= -->
    <!-- DOCUMENTS -->
    <!-- ========================= -->

    <h2 style="margin-top:30px;">Documents fournis</h2>

    <h3>Documents communs</h3>
    <ul>
        <c:forEach var="d" items="${documentsCommuns}">
            <li>${d.document.libelle}</li>
        </c:forEach>
    </ul>

    <h3>Documents spécifiques (${typeVisaSelectionne})</h3>
    <ul>
        <c:forEach var="d" items="${documentsSpecifiques}">
            <li>${d.document.libelle}</li>
        </c:forEach>
    </ul>

    <!-- ========================= -->
    <!-- HISTORIQUE STATUT -->
    <!-- ========================= -->

    <h2>Historique du statut</h2>

    <table border="1" cellpadding="8">
        <tr>
            <th>Date</th>
            <th>Statut</th>
            <th>Commentaire</th>
        </tr>

        <c:forEach var="h" items="${historiques}">
            <tr>
                <td><fmt:formatDate value="${h.dateChangement}" pattern="yyyy-MM-dd"/></td>
                <td>${h.statutDemande.libelle}</td>
                <td>${h.commentaire}</td>
            </tr>
        </c:forEach>
    </table>

    <!-- ========================= -->
    <!-- HISTORIQUE MODIFICATIONS -->
    <!-- ========================= -->

    <h2 style="margin-top:30px;">Historique des modifications</h2>

    <table border="1" cellpadding="8">
        <tr>
            <th>Section</th>
            <th>Modification</th>
            <th>Date</th>
        </tr>

        <c:forEach var="m" items="${modifications}">
            <tr>
                <td>${m.section}</td>

                <td>
                    <c:choose>

                        <c:when test="${m.section == 'Documents'}">
                            Document ajouté : ${m.nouvelleValeur}
                        </c:when>

                        <c:otherwise>
                            ${m.champ} : 
                            <c:if test="${m.ancienneValeur != null && m.ancienneValeur != ''}">
                                ${m.ancienneValeur} →
                            </c:if>
                            ${m.nouvelleValeur}
                        </c:otherwise>

                    </c:choose>
                </td>

                <td>
                    <fmt:formatDate value="${m.dateModification}" pattern="yyyy-MM-dd"/>
                </td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <a href="/backoffice/demande/liste">Retour</a>

</div>
</div>

</body>
</html>