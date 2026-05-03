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
        background: #f8fafc;
        color: #0f172a;
    }

    #main {
        margin-left: 240px;
        padding: 40px;
        min-height: 100vh;
        background: #f8fafc;
    }

    /* CONTAINER PRINCIPAL */
    .container {
        max-width: 1100px;
        margin: auto;
        padding: 32px;
        border-radius: 14px;
        border: 1px solid #e2e8f0;
        background: linear-gradient(to bottom, #ffffff, #fbfdff);
        box-shadow: 0 8px 24px rgba(15, 23, 42, 0.04);

    }

    /* TITRES */
    h1 {
        font-size: 26px;
        margin-bottom: 6px;
        color: #0f172a;
        font-weight: 700;
    }

    h2 {
        margin-top: 30px;
        font-size: 18px;
        font-weight: 700;
        color: #1e293b;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    h2::before {
        content: "";
        width: 6px;
        height: 18px;
        background: #2563eb;
        border-radius: 3px;
        display: inline-block;
    }

    h3 {
        font-size: 15px;
        margin-bottom: 10px;
        color: #334155;
        font-weight: 600;
    }

    p {
        color: #475569;
        line-height: 1.5;
        font-size: 14px;
    }

    /* INFO HEADER */
    .info-box {
        background: #f1f5f9;
        border: 1px solid #e2e8f0;
        padding: 14px 16px;
        border-radius: 10px;
        margin-top: 12px;
        font-size: 14px;
    }

    /* GRID */
    .grid-3 {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        margin-top: 18px;
        gap: 22px;

    }

    .card {
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 14px;
    padding: 18px;
    position: relative;
    transition: all 0.2s ease;
    font-size: 14px;
    color: #334155;
}

/* effet léger mais premium */
.card:hover {
    border-color: #cbd5e1;
    transform: translateY(-2px);
}

/* TITRE DE CARD PLUS PROPRE */
.card h3 {
    margin: 0 0 12px 0;
    font-size: 14px;
    font-weight: 700;
    color: #0f172a;
    display: flex;
    align-items: center;
    gap: 8px;
}

/* petit accent visuel */
.card h3::before {
    content: "";
    width: 8px;
    height: 8px;
    background: #2563eb;
    border-radius: 50%;
    display: inline-block;
}

.card br {
    line-height: 1.8;
}

    /* TABLEAUX PROPRES */
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 12px;
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-radius: 10px;
        overflow: hidden;
        font-size: 14px;
    }

    th {
        background: #f1f5f9;
        color: #334155;
        text-align: left;
        padding: 12px;
        font-weight: 600;
        border-bottom: 1px solid #e2e8f0;
    }

    td {
        padding: 12px;
        border-bottom: 1px solid #f1f5f9;
        color: #475569;
    }

    tr:hover td {
        background: #f8fafc;
    }

    /* LISTES */
    ul {
        margin: 10px 0 0 18px;
        padding: 0;
    }

    ul li {
        padding: 5px 0;
        color: #334155;
        font-size: 14px;
    }

    /* LIGNE BAS */
    .footer-link {
        display: inline-block;
        margin-top: 25px;
        padding: 10px 14px;
        background: #f1f5f9;
        border: 1px solid #e2e8f0;
        border-radius: 8px;
        text-decoration: none;
        color: #0f172a;
        font-weight: 600;
        font-size: 14px;
        transition: 0.2s;
    }

    .footer-link:hover {
        background: #e2e8f0;
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
            <div style="margin-bottom:10px;color:#64748b;font-size:12px;">
    Informations personnelles du demandeur
</div>
           <strong>Nom :</strong> ${demande.demandeur.nom}<br>
            <strong>Prénoms :</strong> ${demande.demandeur.prenoms}<br>
            <strong>Date de naissance :</strong> <fmt:formatDate value="${demande.demandeur.dateNaissance}" pattern="yyyy-MM-dd"/><br>
            <strong>Lieu de naissance :</strong> ${demande.demandeur.lieuNaissance}<br>
            <strong>Email :</strong> ${demande.demandeur.email}<br>
            <strong>Téléphone :</strong> ${demande.demandeur.telephone}<br>
            <strong>Situation familiale :</strong> ${demande.demandeur.situationFamiliale.libelle}<br>
            <strong>Nationalité :</strong> ${demande.demandeur.nationalite.libelle}<br>
            <strong>Adresse :</strong> ${demande.demandeur.adresse}<br>
        </div>

        <!-- PASSEPORT -->
        <div class="card">
            <h3>Passeport</h3>
            <div style="margin-bottom:10px;color:#64748b;font-size:12px;">
    Informations personnelles du demandeur
</div>
            <strong>Numéro :</strong> ${demande.visa.passeport.numeroPasseport}<br>
            <strong>Délivrance :</strong> <fmt:formatDate value="${demande.visa.passeport.dateDelivrance}" pattern="yyyy-MM-dd"/><br>
            <strong>Expiration :</strong> <fmt:formatDate value="${demande.visa.passeport.dateExpiration}" pattern="yyyy-MM-dd"/>
        </div>

        <!-- VISA -->
        <div class="card">
            <h3>Visa</h3>
            <div style="margin-bottom:10px;color:#64748b;font-size:12px;">
    Informations personnelles du demandeur
</div>
            <strong>Référence :</strong> ${demande.visa.reference}<br>
            <strong>Début :</strong> <fmt:formatDate value="${demande.visa.dateDebut}" pattern="yyyy-MM-dd"/><br>
            <strong>Fin :</strong> <fmt:formatDate value="${demande.visa.dateFin}" pattern="yyyy-MM-dd"/>
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
    <a class="footer-link" href="/backoffice/demande/liste">Retour</a>

</div>
</div>

</body>
</html>