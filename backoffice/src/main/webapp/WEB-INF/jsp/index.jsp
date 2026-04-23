<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Accueil - Visa Transformable</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:900px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">
                Backoffice
            </div>
            <h1 style="margin:0 0 12px 0;font-size:32px;color:#0f172a;">Bienvenue sur Visa Transformable</h1>
            <p style="margin:0 0 24px 0;font-size:16px;color:#475569;line-height:1.6;">
                Gérez la saisie des demandes de visa long séjour étape par étape, avec validation des documents et enregistrement final.
            </p>
            <div style="display:flex;gap:12px;flex-wrap:wrap;">
                <a href="/backoffice/demande/new" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#2563eb;color:#fff;text-decoration:none;font-weight:600;">
                    Nouvelle demande
                </a>
                <a href="/backoffice/demande/liste" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#e2e8f0;color:#0f172a;text-decoration:none;font-weight:600;">
                    Liste des demandes
                </a>
            </div>
        </div>
    </div>
</body>
</html>
