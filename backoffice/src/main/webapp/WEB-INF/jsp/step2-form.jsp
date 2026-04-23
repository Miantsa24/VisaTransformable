<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulaire - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:980px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Étape 2</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Formulaire de demande</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">
                Renseignez les informations du demandeur, du passeport et du visa transformable.
            </p>

            <c:if test="${not empty erreurs}">
                <div style="margin-bottom:20px;padding:14px 16px;border-radius:10px;background:#fef2f2;color:#b91c1c;border:1px solid #fecaca;">
                    ${erreurs}
                </div>
            </c:if>

            <form action="/step3-typeVisa" method="post">
                <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:20px;">
                    <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                        <legend style="padding:0 8px;font-weight:700;color:#0f172a;">État civil</legend>
                        <div style="display:grid;gap:12px;">
                            <label>Nom*<br><input type="text" name="nom" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Prénoms*<br><input type="text" name="prenoms" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date de naissance*<br><input type="date" name="date_naissance" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Lieu de naissance*<br><input type="text" name="lieu_naissance" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Situation matrimoniale*<br>
                                <select name="situation_matrimoniale" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                    <c:forEach var="sf" items="${situationsFamiliales}">
                                        <option value="${sf.id}">${sf.libelle}</option>
                                    </c:forEach>
                                </select>
                            </label>
                            <label>Nationalité*<br>
                                <select name="nationalite" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                    <c:forEach var="nat" items="${nationalites}">
                                        <option value="${nat.id}">${nat.libelle}</option>
                                    </c:forEach>
                                </select>
                            </label>
                            <label>Profession<br><input type="text" name="profession" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Adresse locale<br><input type="text" name="adresse_locale" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Email*<br><input type="email" name="email" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Téléphone*<br><input type="tel" name="telephone" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                        </div>
                    </fieldset>

                    <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                        <legend style="padding:0 8px;font-weight:700;color:#0f172a;">Passeport</legend>
                        <div style="display:grid;gap:12px;">
                            <label>Numéro de passeport*<br><input type="text" name="numero_passeport" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date de délivrance<br><input type="date" name="date_delivrance" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date d'expiration<br><input type="date" name="date_expiration" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Pays de délivrance<br><input type="text" name="pays_delivrance" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                        </div>
                    </fieldset>

                    <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                        <legend style="padding:0 8px;font-weight:700;color:#0f172a;">Visa transformable</legend>
                        <div style="display:grid;gap:12px;">
                            <label>Référence du visa*<br><input type="text" name="reference_visa" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date d'entrée à Madagascar<br><input type="date" name="date_entree" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Lieu d'entrée<br><input type="text" name="lieu_entree" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date d'expiration du visa<br><input type="date" name="date_expiration_visa" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                        </div>
                    </fieldset>
                </div>

                <div style="margin-top:24px;display:flex;justify-content:flex-end;gap:12px;flex-wrap:wrap;">
                    <a href="/backoffice/demande/new" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#e2e8f0;color:#0f172a;text-decoration:none;font-weight:600;">
                        Annuler
                    </a>
                    <button type="submit" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">
                        Suivant
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
