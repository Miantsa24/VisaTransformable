<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modifier la demande</title>
    <style>
        .doc-section.inactive {
            opacity: 0.45;
            filter: grayscale(1);
            pointer-events: none;
        }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:1100px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Backoffice</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">Modifier la demande #${demande.id}</h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">
                Modifiez les informations du demandeur, du passeport, du visa, du type de visa et ajoutez uniquement de nouvelles pièces justificatives.
            </p>

            <div style="margin:0 0 20px 0;padding:18px;border:1px solid #dbeafe;border-radius:12px;background:#eff6ff;">
                <label style="display:block;color:#0f172a;font-weight:600;">
                    Date de modification*<br>
                    <input type="date" name="dateModification" value="${dateModification}" required style="width:280px;max-width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                </label>
            </div>

            <c:if test="${not empty erreur}">
                <div style="margin-bottom:20px;padding:14px 16px;border-radius:10px;background:#fef2f2;color:#b91c1c;border:1px solid #fecaca;">
                    ${erreur}
                </div>
            </c:if>

            <form action="/backoffice/demande/${demande.id}/modifier" method="post">
                <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:20px;">
                    <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                        <legend style="padding:0 8px;font-weight:700;color:#0f172a;">État civil</legend>
                        <div style="display:grid;gap:12px;">
                            <label>Nom*<br><input type="text" name="nom" value="${demande.demandeur.nom}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Prénoms*<br><input type="text" name="prenoms" value="${demande.demandeur.prenoms}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date de naissance*<br><input type="date" name="dateNaissance" value="${demande.demandeur.dateNaissance}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Lieu de naissance*<br><input type="text" name="lieuNaissance" value="${demande.demandeur.lieuNaissance}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Situation matrimoniale<br>
                                <select name="idSituationFamiliale" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                    <option value="">-- Choisir --</option>
                                    <c:forEach var="sf" items="${situationsFamiliales}">
                                        <option value="${sf.id}" <c:if test="${demande.demandeur.situationFamiliale != null && demande.demandeur.situationFamiliale.id == sf.id}">selected</c:if>>${sf.libelle}</option>
                                    </c:forEach>
                                </select>
                            </label>
                            <label>Nationalité<br>
                                <select name="idNationalite" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                    <option value="">-- Choisir --</option>
                                    <c:forEach var="nat" items="${nationalites}">
                                        <option value="${nat.id}" <c:if test="${demande.demandeur.nationalite != null && demande.demandeur.nationalite.id == nat.id}">selected</c:if>>${nat.libelle}</option>
                                    </c:forEach>
                                </select>
                            </label>
                            <label>Adresse<br><input type="text" name="adresse" value="${demande.demandeur.adresse}" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Email*<br><input type="email" name="email" value="${demande.demandeur.email}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Téléphone*<br><input type="tel" name="telephone" value="${demande.demandeur.telephone}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                        </div>
                    </fieldset>

                    <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                        <legend style="padding:0 8px;font-weight:700;color:#0f172a;">Passeport</legend>
                        <div style="display:grid;gap:12px;">
                            <label>Numéro de passeport*<br><input type="text" name="numeroPasseport" value="${demande.visa.passeport.numeroPasseport}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date de délivrance<br><input type="date" name="dateDelivrancePasseport" value="${demande.visa.passeport.dateDelivrance}" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date d'expiration<br><input type="date" name="dateExpirationPasseport" value="${demande.visa.passeport.dateExpiration}" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Pays de délivrance<br><input type="text" name="paysDelivrancePasseport" value="${demande.visa.passeport.paysDelivrance}" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                        </div>
                    </fieldset>

                    <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                        <legend style="padding:0 8px;font-weight:700;color:#0f172a;">Visa</legend>
                        <div style="display:grid;gap:12px;">
                            <label>Référence du visa*<br><input type="text" name="referenceVisa" value="${demande.visa.reference}" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date de début<br><input type="date" name="dateDebutVisa" value="${demande.visa.dateDebut}" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Date d'expiration<br><input type="date" name="dateFinVisa" value="${demande.visa.dateFin}" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            <label>Type de visa<br>
                                <select name="typeVisa" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                    <option value="">-- Choisir --</option>
                                    <c:forEach var="tv" items="${typeVisas}">
                                        <option value="${tv.libelle}" <c:if test="${typeVisaCourant == tv.libelle}">selected</c:if>>${tv.libelle}</option>
                                    </c:forEach>
                                </select>
                            </label>
                        </div>
                    </fieldset>
                </div>

                <div style="margin-top:28px;border:1px solid #e2e8f0;border-radius:12px;padding:18px;">
                    <h2 style="margin:0 0 10px 0;font-size:18px;color:#0f172a;">Pièces justificatives</h2>
                    <p style="margin:0 0 18px 0;color:#475569;">Les pièces déjà cochées restent verrouillées. Vous pouvez seulement ajouter les pièces manquantes.</p>

                    <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(300px,1fr));gap:18px;">
                        <div class="doc-section" data-specific-section="true" data-type="commun" style="border:1px solid #e2e8f0;border-radius:12px;padding:16px;background:#f8fafc;">
                            <h3 style="margin:0 0 12px 0;font-size:16px;color:#0f172a;">Documents communs</h3>
                            <div style="display:grid;gap:10px;">
                                <c:forEach var="doc" items="${docsCommuns}">
                                    <div style="display:flex;align-items:flex-start;gap:10px;padding:10px 12px;border:1px solid #e2e8f0;border-radius:10px;background:#fff;">
                                        <c:choose>
                                            <c:when test="${documentsSelectionnes.contains(doc.id)}">                                                						<input type="checkbox" checked disabled data-locked="true" style="margin-top:4px;">
                                                <input type="hidden" name="documentsCoches" value="${doc.id}">
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="documentsCoches" value="${doc.id}" style="margin-top:4px;">
                                            </c:otherwise>
                                        </c:choose>
                                        <span>
                                            ${doc.libelle}
                                            <c:if test="${doc.obligatoire}">
                                                <span style="color:#dc2626;font-weight:700;"> *</span>
                                            </c:if>
                                        </span>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="doc-section" data-specific-section="true" data-type="investisseur" style="border:1px solid #dbeafe;border-radius:12px;padding:16px;background:#eff6ff;">
                            <h3 style="margin:0 0 12px 0;font-size:16px;color:#0f172a;">Documents spécifiques Investisseur</h3>
                            <div style="display:grid;gap:10px;">
                                <c:forEach var="doc" items="${docsInvestisseur}">
                                    <div style="display:flex;align-items:flex-start;gap:10px;padding:10px 12px;border:1px solid #dbeafe;border-radius:10px;background:#fff;">
                                        <c:choose>
                                            <c:when test="${documentsSelectionnes.contains(doc.id)}">
                                                <input type="checkbox" checked disabled data-locked="true" style="margin-top:4px;">
                                                <input type="hidden" name="documentsCoches" value="${doc.id}" data-specific-hidden="true">
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="documentsCoches" value="${doc.id}" style="margin-top:4px;">
                                            </c:otherwise>
                                        </c:choose>
                                        <span>
                                            ${doc.libelle}
                                            <c:if test="${doc.obligatoire}">
                                                <span style="color:#dc2626;font-weight:700;"> *</span>
                                            </c:if>
                                        </span>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        
                        <div class="doc-section" data-specific-section="true" data-type="travailleur" style="border:1px solid #dbeafe;border-radius:12px;padding:16px;background:#eff6ff;">
                            <h3 style="margin:0 0 12px 0;font-size:16px;color:#0f172a;">Documents spécifiques Travailleur</h3>
                            <div style="display:grid;gap:10px;">
                                <c:forEach var="doc" items="${docsTravailleur}">
                                    <div style="display:flex;align-items:flex-start;gap:10px;padding:10px 12px;border:1px solid #dbeafe;border-radius:10px;background:#fff;">
                                        <c:choose>
                                            <c:when test="${documentsSelectionnes.contains(doc.id)}">
                                                <input type="checkbox" checked disabled data-locked="true" style="margin-top:4px;">
                                                <input type="hidden" name="documentsCoches" value="${doc.id}" data-specific-hidden="true">
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="documentsCoches" value="${doc.id}" style="margin-top:4px;">
                                            </c:otherwise>
                                        </c:choose>
                                        <span>
                                            ${doc.libelle}
                                            <c:if test="${doc.obligatoire}">
                                                <span style="color:#dc2626;font-weight:700;"> *</span>
                                            </c:if>
                                        </span>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>


                <div style="margin-top:24px;display:flex;justify-content:flex-end;gap:12px;flex-wrap:wrap;">
                    <a href="/backoffice/demande/liste" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#e2e8f0;color:#0f172a;text-decoration:none;font-weight:600;">Retour</a>
                    <button type="submit" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">Enregistrer les modifications</button>
                </div>
            </form>
        </div>
    </div>
    <script>
        (function () {
            const typeVisaSelect = document.querySelector('select[name="typeVisa"]');

            function syncDocumentSections() {
                const selectedType = (typeVisaSelect.value || '').toLowerCase().trim();
                document.querySelectorAll('[data-specific-section="true"]').forEach((section) => {
                    const sectionType = (section.getAttribute('data-type') || '').toLowerCase().trim();
                    const active = sectionType === 'commun' || sectionType === selectedType;
                    section.classList.toggle('inactive', !active);

                    section.querySelectorAll('input[type="checkbox"]').forEach((checkbox) => {
                        if (sectionType === 'commun') {
                            return;
                        }

                        if (active) {
                            checkbox.disabled = checkbox.dataset.locked === 'true';
                        } else {
                            checkbox.checked = false;
                            checkbox.disabled = true;
                        }
                    });

                    section.querySelectorAll('input[data-specific-hidden="true"]').forEach((hiddenInput) => {
                        if (!active) {
                            hiddenInput.remove();
                        }
                    });
                });
            }

            typeVisaSelect.addEventListener('change', syncDocumentSections);
            syncDocumentSections();
        })();
    </script>
</body>
</html>