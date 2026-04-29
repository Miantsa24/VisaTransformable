<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Historique de la demande</title>
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
                <fmt:formatDate value="${historiques[0].dateChangement}" pattern="yyyy-MM-dd" />
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
                        <c:forEach var="h" items="${historiques}">
                            <tr>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <fmt:formatDate value="${h.dateChangement}" pattern="yyyy-MM-dd" />
                                </td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    ${h.statutDemande.libelle}
                                </td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <c:out value="${h.commentaire}" />
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
