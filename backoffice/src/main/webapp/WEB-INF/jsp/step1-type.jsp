<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Type de demande - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:760px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">
                Étape 1
            </div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Choisissez le type de demande</h1>
            <p style="margin:0 0 28px 0;font-size:16px;color:#475569;line-height:1.6;">
                Sélectionnez le parcours à suivre pour démarrer votre demande de visa long séjour.
            </p>

            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:16px;">
                <form action="/step2-form" method="get" style="margin:0;">
                    <button type="submit" name="type" value="nouveau_titre" style="width:100%;padding:18px 16px;border:none;border-radius:12px;background:#2563eb;color:#fff;font-size:16px;font-weight:600;cursor:pointer;">
                        Nouveau titre
                    </button>
                </form>

                <button type="button" disabled style="width:100%;padding:18px 16px;border:1px dashed #cbd5e1;border-radius:12px;background:#f8fafc;color:#94a3b8;font-size:16px;font-weight:600;cursor:not-allowed;">
                    Duplicata (bientôt)
                </button>
            </div>
        </div>
    </div>
</body>
</html>