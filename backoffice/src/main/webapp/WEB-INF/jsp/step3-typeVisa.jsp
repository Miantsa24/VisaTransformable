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
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Étape 3</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Choix du type de visa</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">
                Sélectionnez le type de visa qui correspond à la demande.
            </p>

            <form action="/step4-documents" method="post">
                <label style="display:block;font-weight:600;color:#0f172a;margin-bottom:8px;">Type de visa</label>
                <select name="type_visa" required style="width:100%;max-width:320px;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                    <option value="investisseur">Investisseur</option>
                    <option value="travailleur">Travailleur</option>
                </select>
                <div style="margin-top:24px;display:flex;justify-content:flex-end;">
                    <button type="submit" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">
                        Continuer
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
