<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation - Sprint 2</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: #f5f5f5;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            padding: 40px;
        }
        h1 {
            color: #10b981;
            margin-bottom: 10px;
            font-size: 28px;
        }
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 14px;
        }
        .confirmation-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .demande-card {
            border: 1px solid #cbd5e1;
            border-radius: 6px;
            padding: 20px;
            background: #f9fafb;
        }
        .demande-card h3 {
            color: #1f2937;
            font-size: 16px;
            margin-bottom: 15px;
            border-bottom: 2px solid #2563eb;
            padding-bottom: 10px;
        }
        .demande-detail {
            margin-bottom: 12px;
            display: flex;
            justify-content: space-between;
        }
        .demande-detail label {
            color: #666;
            font-weight: 500;
            width: 40%;
        }
        .demande-detail span {
            color: #1f2937;
            font-weight: 600;
            width: 60%;
            text-align: right;
        }
        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 600;
            margin-top: 10px;
        }
        .status-approuvee {
            background: #d1fae5;
            color: #065f46;
        }
        .status-demande-creee {
            background: #fef3c7;
            color: #92400e;
        }
        .info-section {
            background: #eff6ff;
            border-left: 4px solid #2563eb;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .info-section h4 {
            color: #1e40af;
            margin-bottom: 8px;
            font-size: 14px;
        }
        .info-section p {
            color: #1e3a8a;
            font-size: 13px;
            line-height: 1.5;
        }
        .actions {
            display: flex;
            gap: 10px;
            margin-top: 30px;
            border-top: 1px solid #cbd5e1;
            padding-top: 20px;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        .btn-primary {
            background: #2563eb;
            color: white;
        }
        .btn-primary:hover {
            background: #1d4ed8;
        }
        .btn-secondary {
            background: #e5e7eb;
            color: #1f2937;
        }
        .btn-secondary:hover {
            background: #d1d5db;
        }
        .demande-documents {
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #e5e7eb;
        }
        .demande-documents label {
            color: #666;
            font-size: 12px;
            display: block;
            margin-bottom: 8px;
        }
        .document-list {
            font-size: 13px;
            color: #555;
        }
        .document-list li {
            margin-bottom: 4px;
        }
        .sidebar {
            position: fixed;
            left: 0;
            top: 0;
            width: 250px;
            height: 100vh;
            background: #2563eb;
            color: white;
            padding: 20px;
            overflow-y: auto;
        }
        .sidebar h2 {
            font-size: 16px;
            margin-bottom: 20px;
        }
        .sidebar nav a {
            display: block;
            color: rgba(255, 255, 255, 0.7);
            text-decoration: none;
            margin-bottom: 10px;
            padding: 8px 12px;
            border-radius: 4px;
            transition: all 0.3s ease;
        }
        .sidebar nav a:hover {
            background: rgba(255, 255, 255, 0.1);
            color: white;
        }
        @media (max-width: 768px) {
            .sidebar {
                display: none;
            }
            .container {
                max-width: 100%;
            }
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Menu</h2>
        <nav>
            <a href="/index">Accueil</a>
            <a href="/step1-type">Nouvelle demande</a>
            <a href="/backoffice/demande/liste">Mes demandes</a>
        </nav>
    </div>

    <div class="container">
        <h1>✓ Demande(s) créée(s) avec succès</h1>
        <div class="subtitle">Merci pour votre soumission. Voici le résumé de votre demande.</div>

        <!-- Information sur le type de perte -->
        <div class="info-section">
            <h4>Type de demande</h4>
            <p>
                <strong>
                    <c:choose>
                        <c:when test="${typePerte == 'duplicata'}">
                            Duplicata de carte de résident
                        </c:when>
                        <c:when test="${typePerte == 'transfert_visa'}">
                            Transfert de visa vers nouveau passeport
                        </c:when>
                        <c:otherwise>
                            ${typePerte}
                        </c:otherwise>
                    </c:choose>
                </strong>
            </p>
        </div>

        <!-- Cas 1 : 1 seule demande (personne existante) -->
        <c:if test="${demande != null}">
            <div class="confirmation-grid">
                <div class="demande-card">
                    <h3>Demande créée</h3>
                    <div class="demande-detail">
                        <label>Identifiant :</label>
                        <span>${demande.id}</span>
                    </div>
                    <div class="demande-detail">
                        <label>Type :</label>
                        <span>${demande.typeDemande.libelle}</span>
                    </div>
                    <div class="demande-detail">
                        <label>Date :</label>
                        <span><fmt:formatDate value="${demande.dateDemande}" pattern="dd/MM/yyyy" /></span>
                    </div>
                    <div class="demande-detail">
                        <label>Statut :</label>
                        <span>
                            <span class="status-badge status-demande-creee">${demande.statutDemande.libelle}</span>
                        </span>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Cas 2 : 2 demandes (personne inconnue) -->
        <c:if test="${demandes != null}">
            <div class="confirmation-grid">
                <c:forEach items="${demandes}" var="demande">
                    <div class="demande-card">
                        <h3>
                            <c:choose>
                                <c:when test="${demande.typeDemande.libelle == 'nouveau_titre'}">
                                    Demande 1 : Nouveau titre
                                </c:when>
                                <c:otherwise>
                                    Demande 2 : ${demande.typeDemande.libelle}
                                </c:otherwise>
                            </c:choose>
                        </h3>
                        <div class="demande-detail">
                            <label>Identifiant :</label>
                            <span>${demande.id}</span>
                        </div>
                        <div class="demande-detail">
                            <label>Type :</label>
                            <span>${demande.typeDemande.libelle}</span>
                        </div>
                        <div class="demande-detail">
                            <label>Date :</label>
                            <span><fmt:formatDate value="${demande.dateDemande}" pattern="dd/MM/yyyy" /></span>
                        </div>
                        <div class="demande-detail">
                            <label>Statut :</label>
                            <span>
                                <c:choose>
                                    <c:when test="${demande.statutDemande.libelle == 'approuvee'}">
                                        <span class="status-badge status-approuvee">${demande.statutDemande.libelle}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-demande-creee">${demande.statutDemande.libelle}</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Info pour cas 2 -->
        <c:if test="${demandes != null}">
            <div class="info-section">
                <h4>Information</h4>
                <p>Deux demandes liées ont été créées :<br/>
                   • <strong>Demande 1</strong> : Nouveau titre (statut : approuvée)<br/>
                   • <strong>Demande 2</strong> : Duplicata/Transfert (statut : demande créée)<br/>
                   Les mêmes documents sont associés aux deux demandes pour l'audit.
                </p>
            </div>
        </c:if>

        <!-- Actions -->
        <div class="actions">
            <a href="/index" class="btn btn-primary">Retour à l'accueil</a>
            <a href="/step1-type" class="btn btn-secondary">Nouvelle demande</a>
            <a href="/backoffice/demande/liste" class="btn btn-secondary">Mes demandes</a>
        </div>
    </div>
</body>
</html>
