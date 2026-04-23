<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Demande enregistrée</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:760px;background:#fff;border-radius:16px;padding:36px 40px;box-shadow:0 10px 30px rgba(15,23,42,0.08);text-align:center;">
            <div style="width:72px;height:72px;margin:0 auto 18px auto;border-radius:999px;background:#dcfce7;color:#166534;display:flex;align-items:center;justify-content:center;font-size:34px;font-weight:700;">
                ✓
            </div>
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Demande créée</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">La demande a été enregistrée avec succès</h1>
            <p style="margin:0 0 28px 0;color:#475569;line-height:1.6;">
                Vous pouvez retourner à l’accueil ou démarrer une nouvelle demande.
            </p>
            <div style="display:flex;gap:12px;justify-content:center;flex-wrap:wrap;">
                <a href="/index" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#2563eb;color:#fff;text-decoration:none;font-weight:600;">
                    Retour à l’accueil
                </a>
                <a href="/backoffice/demande/new" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#e2e8f0;color:#0f172a;text-decoration:none;font-weight:600;">
                    Nouvelle demande
                </a>
            </div>
        </div>
    </div>
</body>
</html>
