<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Historique de la demande</title>
    <style>
        .initial-value {
            background:#dbeafe;
            color:#1d4ed8;
            padding:2px 6px;
            border-radius:6px;
            font-weight:700;
        }
        .modified-value {
            background:#ede9fe;
            color:#6d28d9;
            padding:2px 6px;
            border-radius:6px;
            font-weight:700;
        }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:1000px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Backoffice</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Historique de la demande #${demande.id}</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">
                ${demande.demandeur.nom} ${demande.demandeur.prenoms} — ${demande.typeDemande.libelle}
            </p>
            <div style="display:inline-block;margin-bottom:18px;padding:10px 14px;border-radius:999px;background:#eff6ff;color:#1d4ed8;font-weight:600;">
                Dernière modification :
                <c:choose>
                    <c:when test="${not empty historiques}">
                        <fmt:formatDate value="${historiques[0].dateChangement}" pattern="yyyy-MM-dd" />
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </div>
            <div style="margin-bottom:28px;padding:22px;border:1px solid #dbeafe;border-radius:16px;background:#f8fbff;">
                <div style="display:flex;justify-content:space-between;align-items:flex-start;gap:16px;flex-wrap:wrap;margin-bottom:18px;">
                    <div>
                        <h2 style="margin:0 0 6px 0;font-size:22px;color:#0f172a;">Fiche de renseignement après modification</h2>
                        <p style="margin:0;color:#475569;">Vue d'ensemble du formulaire après mise à jour.</p>
                    </div>
                    <div style="padding:10px 14px;border-radius:12px;background:#ecfeff;color:#0f766e;font-weight:700;">
                        Informations modifiées
                    </div>
                </div>

                <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(260px,1fr));gap:16px;">
                    <div style="background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;">
                        <h3 style="margin:0 0 10px 0;color:#0f172a;font-size:16px;">État civil</h3>
                        <div style="display:grid;gap:8px;color:#334155;">
                            <div><strong>Nom :</strong> <span class="${champsModifies.contains('nom') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.demandeur.nom}" /></span></div>
                            <div><strong>Prénoms :</strong> <span class="${champsModifies.contains('prenoms') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.demandeur.prenoms}" /></span></div>
                            <div><strong>Date de naissance :</strong> <span class="${champsModifies.contains('dateNaissance') ? 'modified-value' : 'initial-value'}"><fmt:formatDate value="${demande.demandeur.dateNaissance}" pattern="yyyy-MM-dd" /></span></div>
                            <div><strong>Lieu de naissance :</strong> <span class="${champsModifies.contains('lieuNaissance') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.demandeur.lieuNaissance}" /></span></div>
                            <div><strong>Email :</strong> <span class="${champsModifies.contains('email') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.demandeur.email}" /></span></div>
                            <div><strong>Téléphone :</strong> <span class="${champsModifies.contains('telephone') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.demandeur.telephone}" /></span></div>
                        </div>
                    </div>

                    <div style="background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;">
                        <h3 style="margin:0 0 10px 0;color:#0f172a;font-size:16px;">Passeport</h3>
                        <div style="display:grid;gap:8px;color:#334155;">
                            <div><strong>Numéro :</strong> <span class="${champsModifies.contains('numeroPasseport') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.visa.passeport.numeroPasseport}" /></span></div>
                            <div><strong>Délivrance :</strong> <span class="${champsModifies.contains('dateDelivrancePasseport') ? 'modified-value' : 'initial-value'}"><fmt:formatDate value="${demande.visa.passeport.dateDelivrance}" pattern="yyyy-MM-dd" /></span></div>
                            <div><strong>Expiration :</strong> <span class="${champsModifies.contains('dateExpirationPasseport') ? 'modified-value' : 'initial-value'}"><fmt:formatDate value="${demande.visa.passeport.dateExpiration}" pattern="yyyy-MM-dd" /></span></div>
                            <div><strong>Pays :</strong> <span class="${champsModifies.contains('paysDelivrancePasseport') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.visa.passeport.paysDelivrance}" /></span></div>
                        </div>
                    </div>

                    <div style="background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;">
                        <h3 style="margin:0 0 10px 0;color:#0f172a;font-size:16px;">Visa</h3>
                        <div style="display:grid;gap:8px;color:#334155;">
                            <div><strong>Référence :</strong> <span class="${champsModifies.contains('referenceVisa') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.visa.reference}" /></span></div>
                            <div><strong>Début :</strong> <span class="${champsModifies.contains('dateDebutVisa') ? 'modified-value' : 'initial-value'}"><fmt:formatDate value="${demande.visa.dateDebut}" pattern="yyyy-MM-dd" /></span></div>
                            <div><strong>Fin :</strong> <span class="${champsModifies.contains('dateFinVisa') ? 'modified-value' : 'initial-value'}"><fmt:formatDate value="${demande.visa.dateFin}" pattern="yyyy-MM-dd" /></span></div>
                            <div><strong>Type :</strong> <span class="${champsModifies.contains('typeVisa') ? 'modified-value' : 'initial-value'}"><c:out value="${demande.visa.typeVisa.libelle}" /></span></div>
                        </div>
                    </div>

                    <div style="background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;">
                        <h3 style="margin:0 0 10px 0;color:#0f172a;font-size:16px;">Pièces justificatives</h3>
                        <div style="display:grid;gap:16px;">
                            <div>
                                <div style="margin-bottom:8px;font-weight:700;color:#0f172a;">Documents communs</div>
                                <div style="display:flex;flex-wrap:wrap;gap:8px;">
                                    <c:forEach var="dd" items="${documentsCommuns}">
                                        <span class="${documentsModifies.contains(dd.document.id) ? 'modified-value' : 'initial-value'}">
                                            <c:out value="${dd.document.libelle}" />
                                        </span>
                                    </c:forEach>
                                </div>
                            </div>
                            <div>
                                <div style="margin-bottom:8px;font-weight:700;color:#0f172a;">
                                    Documents spécifiques
                                    <span style="color:#7c3aed;">(<c:out value="${typeVisaSelectionne}" />)</span>
                                </div>
                                <div style="display:flex;flex-wrap:wrap;gap:8px;">
                                    <c:forEach var="dd" items="${documentsSpecifiques}">
                                        <span class="${documentsModifies.contains(dd.document.id) ? 'modified-value' : 'initial-value'}">
                                            <c:out value="${dd.document.libelle}" />
                                        </span>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div style="overflow-x:auto;">
                <table style="width:100%;border-collapse:collapse;">
                    <thead>
                        <tr style="background:#f8fafc;">
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Date de modification</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Statut</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Commentaire</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="h" items="${historiques}" varStatus="status">
                            <tr>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <fmt:formatDate value="${h.dateChangement}" pattern="yyyy-MM-dd" />
                                </td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    ${h.statutDemande.libelle}
                                </td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <c:out value="${commentairesVisibles[status.index]}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div style="margin-top:24px;">
                <a href="/backoffice/demande/liste" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#2563eb;color:#fff;text-decoration:none;font-weight:600;">Retour à la liste</a>
            </div>
        </div>
    </div>
</body>
</html>
