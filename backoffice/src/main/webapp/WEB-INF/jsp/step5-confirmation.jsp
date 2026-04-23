<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmation de la demande</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:1040px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Étape 5</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">${typeVisaLabel}</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">Merci de vérifier les informations avant soumission définitive.</p>

            <c:if test="${not empty error}">
                <div style="margin-bottom:20px;padding:14px 16px;border-radius:10px;background:#fef2f2;color:#b91c1c;border:1px solid #fecaca;">
                    ${error}
                </div>
            </c:if>

            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:20px;">
                <div style="border:1px solid #e2e8f0;border-radius:12px;padding:18px;background:#f8fafc;">
                    <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">État civil</h2>
                    <p><strong>Nom :</strong> ${demandeur.nom}</p>
                    <p><strong>Prénoms :</strong> ${demandeur.prenoms}</p>
                    <p><strong>Date de naissance :</strong> ${demandeur.dateNaissance}</p>
                    <p><strong>Lieu de naissance :</strong> ${demandeur.lieuNaissance}</p>
                    <p><strong>Téléphone :</strong> ${demandeur.telephone}</p>
                    <p><strong>Email :</strong> ${demandeur.email}</p>
                    <p><strong>Adresse :</strong> ${demandeur.adresse}</p>
                    <p><strong>Situation familiale :</strong> ${demandeur.situationFamiliale.libelle}</p>
                    <p><strong>Nationalité :</strong> ${demandeur.nationalite.libelle}</p>
                </div>

                <div style="border:1px solid #e2e8f0;border-radius:12px;padding:18px;background:#f8fafc;">
                    <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">Passeport</h2>
                    <p><strong>Numéro :</strong> ${passeport.numeroPasseport}</p>
                    <p><strong>Date de délivrance :</strong> ${passeport.dateDelivrance}</p>
                    <p><strong>Date d'expiration :</strong> ${passeport.dateExpiration}</p>
                    <p><strong>Pays de délivrance :</strong> ${passeport.paysDelivrance}</p>
                </div>

                <div style="border:1px solid #dbeafe;border-radius:12px;padding:18px;background:#eff6ff;">
                    <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">Visa transformable</h2>
                    <p><strong>Référence :</strong> ${visa.reference}</p>
                    <p><strong>Date de début :</strong> ${visa.dateDebut}</p>
                    <p><strong>Date de fin :</strong> ${visa.dateFin}</p>
                </div>
            </div>

            <div style="margin-top:24px;display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:20px;">
                <div style="border:1px solid #e2e8f0;border-radius:12px;padding:18px;background:#fff;">
                    <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">Documents communs choisis</h2>
                    <ul style="margin:0;padding-left:18px;">
                        <c:forEach var="doc" items="${documentsCommunsChoisis}">
                            <li>${doc.libelle}</li>
                        </c:forEach>
                    </ul>
                </div>

                <div style="border:1px solid #dbeafe;border-radius:12px;padding:18px;background:#fff;">
                    <h2 style="margin:0 0 14px 0;font-size:18px;color:#0f172a;">Documents spécifiques choisis</h2>
                    <ul style="margin:0;padding-left:18px;">
                        <c:forEach var="doc" items="${documentsSpecifiquesChoisis}">
                            <li>${doc.libelle}</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <form action="/backoffice/demande" method="post" style="margin-top:28px;display:flex;justify-content:flex-end;">
                <c:forEach var="doc" items="${documentsSelectionnees}">
                    <input type="hidden" name="documents" value="${doc.id}" />
                </c:forEach>
                <input type="hidden" name="type_visa" value="${typeVisa}" />
                <button type="submit" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">
                    Soumettre la demande
                </button>
            </form>
        </div>
    </div>
</body>
</html>
