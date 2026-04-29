<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Duplicata / Transfert - Visa Long Séjour</title>
    <style>
        .search-section { margin-bottom: 32px; }
        .search-section h2 { margin-top: 0; }
        .search-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 16px; margin-bottom: 16px; }
        .form-section { display: none; }
        .form-section.visible { display: block; }
        .loading { text-align: center; color: #64748b; }
        .error { padding: 14px 16px; border-radius: 10px; background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; margin-bottom: 20px; }
        .success { padding: 14px 16px; border-radius: 10px; background: #f0fdf4; color: #166534; border: 1px solid #bbf7d0; margin-bottom: 20px; }
        .new-passport-field { display: none; }
        .new-passport-field.visible { display: block; }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:240px;padding:40px 32px;background:#f8fafc;min-height:100vh;box-sizing:border-box;">
        <div style="max-width:1020px;background:#fff;border-radius:16px;padding:32px 36px;box-shadow:0 10px 30px rgba(15,23,42,0.08);">
            <div style="font-size:13px;text-transform:uppercase;letter-spacing:0.08em;color:#64748b;margin-bottom:10px;">Étape 1 - Sprint 2</div>
            <h1 style="margin:0 0 12px 0;font-size:30px;color:#0f172a;">
                <c:choose>
                    <c:when test="${param.type == 'duplicata'}">Demande de Duplicata</c:when>
                    <c:when test="${param.type == 'transfert_visa'}">Demande de Transfert de Visa</c:when>
                    <c:otherwise>Formulaire Sprint 2</c:otherwise>
                </c:choose>
            </h1>
            <p style="margin:0 0 24px 0;color:#475569;line-height:1.6;">
                <c:choose>
                    <c:when test="${param.type == 'duplicata'}">Recherchez une personne existante ou créez un nouveau dossier pour un duplicata de carte résident.</c:when>
                    <c:when test="${param.type == 'transfert_visa'}">Recherchez une personne existante ou créez un nouveau dossier pour un transfert de visa vers un nouveau passeport.</c:when>
                </c:choose>
            </p>

            <!-- SECTION 1 : RECHERCHE -->
            <div class="search-section">
                <h2 style="margin-top:0;font-size:18px;color:#0f172a;">Étape 1 : Recherche de personne existante</h2>
                <p style="color:#475569;margin-bottom:16px;">Renseignez au moins un critère pour rechercher une personne existante en base. Si aucune correspondance, vous pourrez créer un nouveau dossier.</p>

                <div class="search-grid">
                    <label>Email<br><input type="email" id="searchEmail" placeholder="john@example.com" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                    <label>Téléphone<br><input type="tel" id="searchTelephone" placeholder="03 20 00 00 00" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                    <label>N° de Passeport<br><input type="text" id="searchNumeroPasseport" placeholder="P123456" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                    <label>Référence Visa<br><input type="text" id="searchReferenceVisa" placeholder="V123456" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                </div>

                <div style="display:flex;gap:12px;">
                    <button type="button" onclick="searchPerson()" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">
                        Rechercher
                    </button>
                    <button type="button" onclick="skipSearch()" style="padding:12px 18px;border:1px solid #cbd5e1;border-radius:10px;background:#fff;color:#0f172a;font-weight:600;cursor:pointer;">
                        Créer un nouveau dossier
                    </button>
                </div>

                <div id="searchStatus" style="margin-top:16px;display:none;"></div>
            </div>

            <!-- SECTION 2 : FORMULAIRE -->
            <div class="form-section" id="formSection">
                <h2 style="margin-top:0;font-size:18px;color:#0f172a;">Étape 2 : Formulaire</h2>
                <p id="formStatus" style="color:#475569;margin-bottom:16px;"></p>

                <form action="/sprint2/duplicata" method="post" id="mainForm">
                    <!-- Type de perte (hidden) -->
                    <input type="hidden" name="typePerte" value="${param.type}">
                    <!-- idDemandeur (hidden) - sera rempli si personne trouvée -->
                    <input type="hidden" id="idDemandeur" name="idDemandeur">

                    <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:20px;">
                        <!-- État civil -->
                        <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                            <legend style="padding:0 8px;font-weight:700;color:#0f172a;">État civil</legend>
                            <div style="display:grid;gap:12px;">
                                <label>Nom*<br><input type="text" id="nom" name="nom" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Prénoms*<br><input type="text" id="prenoms" name="prenoms" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date de naissance*<br><input type="date" id="dateNaissance" name="dateNaissance" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Lieu de naissance*<br><input type="text" id="lieuNaissance" name="lieuNaissance" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Situation matrimoniale*<br>
                                    <select id="idSituationFamiliale" name="idSituationFamiliale" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                        <option value="">-- Choisir --</option>
                                        <c:forEach var="sf" items="${situationsFamiliales}">
                                            <option value="${sf.id}">${sf.libelle}</option>
                                        </c:forEach>
                                    </select>
                                </label>
                                <label>Nationalité*<br>
                                    <select id="idNationalite" name="idNationalite" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;">
                                        <option value="">-- Choisir --</option>
                                        <c:forEach var="nat" items="${nationalites}">
                                            <option value="${nat.id}">${nat.libelle}</option>
                                        </c:forEach>
                                    </select>
                                </label>
                                <label>Adresse<br><input type="text" id="adresse" name="adresse" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Email*<br><input type="email" id="email" name="email" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Téléphone*<br><input type="tel" id="telephone" name="telephone" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            </div>
                        </fieldset>

                        <!-- Passeport -->
                        <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                            <legend style="padding:0 8px;font-weight:700;color:#0f172a;">Passeport</legend>
                            <div style="display:grid;gap:12px;">
                                <label>Numéro de passeport*<br><input type="text" id="numeroPasseport" name="numeroPasseport" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date de délivrance<br><input type="date" id="dateDelivrancePasseport" name="dateDelivrancePasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date d'expiration<br><input type="date" id="dateExpirationPasseport" name="dateExpirationPasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Pays de délivrance<br><input type="text" id="paysDelivrancePasseport" name="paysDelivrancePasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            </div>
                        </fieldset>

                        <!-- Visa -->
                        <fieldset style="border:1px solid #e2e8f0;border-radius:12px;padding:18px 18px 8px 18px;">
                            <legend style="padding:0 8px;font-weight:700;color:#0f172a;">Visa transformable</legend>
                            <div style="display:grid;gap:12px;">
                                <label>Référence du visa*<br><input type="text" id="referenceVisa" name="referenceVisa" required style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date de début<br><input type="date" id="dateDebutVisa" name="dateDebutVisa" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date d'expiration<br><input type="date" id="dateFinVisa" name="dateFinVisa" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            </div>
                        </fieldset>

                        <!-- NOUVEAU PASSEPORT (visible seulement si transfert_visa) -->
                        <fieldset id="newPassportFieldset" class="new-passport-field" style="border:2px solid #10b981;border-radius:12px;padding:18px 18px 8px 18px;background:#f0fdf4;">
                            <legend style="padding:0 8px;font-weight:700;color:#065f46;">⭐ Nouveau Passeport (Obligatoire pour transfert)</legend>
                            <div style="display:grid;gap:12px;">
                                <label>Nouveau numéro de passeport*<br><input type="text" id="numeroNouveauPasseport" name="numeroNouveauPasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date de délivrance<br><input type="date" id="dateDelivranceNouveauPasseport" name="dateDelivranceNouveauPasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Date d'expiration<br><input type="date" id="dateExpirationNouveauPasseport" name="dateExpirationNouveauPasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                                <label>Pays de délivrance<br><input type="text" id="paysDelivranceNouveauPasseport" name="paysDelivranceNouveauPasseport" style="width:100%;padding:10px 12px;border:1px solid #cbd5e1;border-radius:10px;box-sizing:border-box;"></label>
                            </div>
                        </fieldset>
                    </div>

                    <div style="margin-top:24px;display:flex;justify-content:flex-end;gap:12px;flex-wrap:wrap;">
                        <a href="/step1-type" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#e2e8f0;color:#0f172a;text-decoration:none;font-weight:600;">
                            Annuler
                        </a>
                        <button type="submit" style="padding:12px 18px;border:none;border-radius:10px;background:#2563eb;color:#fff;font-weight:600;cursor:pointer;">
                            Soumettre
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Déterminer le type de perte (duplicata ou transfert_visa)
        const typePerte = new URLSearchParams(window.location.search).get('type') || 'duplicata';

        // Afficher/cacher le champ nouveau passeport selon le type
        function updateNewPassportField() {
            const field = document.getElementById('newPassportFieldset');
            if (typePerte === 'transfert_visa') {
                field.classList.add('visible');
                document.getElementById('numeroNouveauPasseport').required = true;
            } else {
                field.classList.remove('visible');
                document.getElementById('numeroNouveauPasseport').required = false;
            }
        }

        // Rechercher une personne
        function searchPerson() {
            const email = document.getElementById('searchEmail').value.trim();
            const telephone = document.getElementById('searchTelephone').value.trim();
            const numeroPasseport = document.getElementById('searchNumeroPasseport').value.trim();
            const referenceVisa = document.getElementById('searchReferenceVisa').value.trim();

            // Vérifier qu'au moins un critère est rempli
            if (!email && !telephone && !numeroPasseport && !referenceVisa) {
                alert('Veuillez renseigner au moins un critère de recherche');
                return;
            }

            const searchStatus = document.getElementById('searchStatus');
            searchStatus.innerHTML = '<div class="loading">Recherche en cours...</div>';
            searchStatus.style.display = 'block';

            // AJAX GET /prefill
            const params = new URLSearchParams();
            if (email) params.append('email', email);
            if (telephone) params.append('telephone', telephone);
            if (numeroPasseport) params.append('numeroPasseport', numeroPasseport);
            if (referenceVisa) params.append('referenceVisa', referenceVisa);

            fetch('/prefill?' + params, { method: 'GET' })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else if (response.status === 404) {
                        return Promise.reject(new Error('NOT_FOUND'));
                    } else {
                        return Promise.reject(new Error('Erreur serveur'));
                    }
                })
                .then(data => {
                    // Personne trouvée - Préremplir le formulaire
                    prefillForm(data);
                    searchStatus.innerHTML = '<div class="success">✓ Personne trouvée. Vérifiez et modifiez les informations si nécessaire.</div>';
                })
                .catch(error => {
                    if (error.message === 'NOT_FOUND') {
                        // Personne non trouvée - Afficher formulaire vierge
                        searchStatus.innerHTML = '<div class="error">✗ Aucune personne trouvée. Vous pouvez créer un nouveau dossier en remplissant le formulaire ci-dessous.</div>';
                        showEmptyForm();
                    } else {
                        searchStatus.innerHTML = '<div class="error">✗ Erreur : ' + error.message + '</div>';
                    }
                });
        }

        // Sauter la recherche - Afficher formulaire vierge
        function skipSearch() {
            const searchStatus = document.getElementById('searchStatus');
            searchStatus.innerHTML = '<div class="error">Formulaire vierge - Remplissez les informations pour créer un nouveau dossier.</div>';
            showEmptyForm();
        }

        // Préremplir le formulaire
        function prefillForm(data) {
            document.getElementById('idDemandeur').value = data.idDemandeur || '';
            document.getElementById('nom').value = data.nom || '';
            document.getElementById('prenoms').value = data.prenoms || '';
            document.getElementById('dateNaissance').value = formatDate(data.dateNaissance) || '';
            document.getElementById('lieuNaissance').value = data.lieuNaissance || '';
            document.getElementById('idSituationFamiliale').value = data.idSituationFamiliale || '';
            document.getElementById('idNationalite').value = data.idNationalite || '';
            document.getElementById('adresse').value = data.adresse || '';
            document.getElementById('email').value = data.email || '';
            document.getElementById('telephone').value = data.telephone || '';
            document.getElementById('numeroPasseport').value = data.numeroPasseport || '';
            document.getElementById('dateDelivrancePasseport').value = formatDate(data.dateDelivrancePasseport) || '';
            document.getElementById('dateExpirationPasseport').value = formatDate(data.dateExpirationPasseport) || '';
            document.getElementById('paysDelivrancePasseport').value = data.paysDelivrancePasseport || '';
            document.getElementById('referenceVisa').value = data.referenceVisa || '';
            document.getElementById('dateDebutVisa').value = formatDate(data.dateDebutVisa) || '';
            document.getElementById('dateFinVisa').value = formatDate(data.dateFinVisa) || '';

            document.getElementById('formStatus').textContent = 'Formulaire prérempli avec les données de la personne trouvée.';
            document.getElementById('formSection').classList.add('visible');
        }

        // Afficher formulaire vierge
        function showEmptyForm() {
            document.getElementById('idDemandeur').value = '';
            // Laisser les champs vierges
            document.getElementById('formStatus').textContent = 'Créez un nouveau dossier en remplissant les informations ci-dessous.';
            document.getElementById('formSection').classList.add('visible');
        }

        // Formater les dates (SQL Date format YYYY-MM-DD)
        function formatDate(dateStr) {
            if (!dateStr) return '';
            const date = new Date(dateStr);
            if (isNaN(date.getTime())) return '';
            return date.toISOString().split('T')[0];
        }

        // Initialiser
        document.addEventListener('DOMContentLoaded', function() {
            updateNewPassportField();
        });
    </script>
</body>
</html>
