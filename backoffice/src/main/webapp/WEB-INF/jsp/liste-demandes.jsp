<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des demandes</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:1200px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Backoffice</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Liste des demandes</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">Toutes les demandes créées dans le système.</p>

            <c:if test="${not empty success}">
    <div style="margin-bottom:20px;padding:14px 16px;border-radius:10px;
                background:#ecfdf5;color:#065f46;border:1px solid #6ee7b7;">
        ${success}
    </div>
</c:if>

            <div style="overflow-x:auto;">
                <table style="width:100%;border-collapse:collapse;">
                    <thead>
                        <tr style="background:#f8fafc;">
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">ID</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Date</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Demandeur</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Type demande</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Type de visa</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Visa</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Passeport</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${demandes}" var="d">
                            <tr>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.id}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.dateDemande}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.demandeur.nom} ${d.demandeur.prenoms}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.typeDemande.libelle}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.visa.typeVisa.libelle}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.visa.reference}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">${d.visa.passeport.numeroPasseport}</td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <a href="/backoffice/demande/${d.id}/modifier" style="display:inline-block;padding:8px 12px;border-radius:8px;background:#2563eb;color:#fff;text-decoration:none;font-weight:600;">Modifier</a>
                                    <a href="/backoffice/demande/${d.id}/historique" style="display:inline-block;padding:8px 12px;border-radius:8px;background:#0f172a;color:#fff;text-decoration:none;font-weight:600;margin-left:8px;">Voir</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
