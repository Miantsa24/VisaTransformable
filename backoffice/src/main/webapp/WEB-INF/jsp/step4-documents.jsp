<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Type de demande - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:980px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Étape 4</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Documents à fournir</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">
                Cochez tous les documents obligatoires pour continuer.
            </p>

            <c:if test="${not empty erreur}">
                <div style="margin-bottom:20px;padding:14px 16px;border-radius:10px;background:#fef2f2;color:#b91c1c;border:1px solid #fecaca;">
                    ${erreur}
                </div>
            </c:if>

            <form action="/step4-documents/validation" method="post">
                <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(300px,1fr));gap:20px;">
                    <div style="border:1px solid #e2e8f0;border-radius:12px;padding:18px;background:#f8fafc;">
                        <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">Documents communs</h2>
                        <div style="display:grid;gap:10px;">
                            <c:forEach var="doc" items="${docsCommuns}">
                                <label style="display:flex;align-items:flex-start;gap:10px;padding:10px 12px;border:1px solid #e2e8f0;border-radius:10px;background:#fff;">
                                    <input type="checkbox" name="documents" value="${doc.id}" style="margin-top:4px;">
                                    <span style="color:#0f172a;">
                                        ${doc.libelle}
                                        <c:if test="${doc.obligatoire}">
                                            <span style="color:#dc2626;font-weight:700;"> *</span>
                                        </c:if>
                                    </span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <c:if test="${not empty docsSpecifiques}">
                        <div style="border:1px solid #dbeafe;border-radius:12px;padding:18px;background:#eff6ff;">
                            <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">Documents spécifiques</h2>
                            <div style="display:grid;gap:10px;">
                                <c:forEach var="doc" items="${docsSpecifiques}">
                                    <label style="display:flex;align-items:flex-start;gap:10px;padding:10px 12px;border:1px solid #dbeafe;border-radius:10px;background:#fff;">
                                        <input type="checkbox" name="documents" value="${doc.id}" style="margin-top:4px;">
                                        <span style="color:#0f172a;">
                                            ${doc.libelle}
                                            <c:if test="${doc.obligatoire}">
                                                <span style="color:#dc2626;font-weight:700;"> *</span>
                                            </c:if>
                                        </span>
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </div>

                <input type="hidden" name="type_visa" value="${typeVisa}">
                <div style="margin-top:24px;display:flex;justify-content:flex-end;">
                    <button type="submit" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">
                        Enregistrer
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
