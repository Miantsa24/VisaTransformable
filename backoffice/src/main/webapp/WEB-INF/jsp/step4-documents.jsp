<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Type de demande - Visa Long Séjour</title>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main" style="margin-left:220px;padding:20px;">
        <h1>Documents à fournir</h1>
        <c:if test="${not empty erreur}">
            <div style="color:red; margin-bottom:10px;">
                ${erreur}
            </div>
        </c:if>
        <form action="/step4-documents/validation" method="post">
            <!-- Bloc 1 : Documents communs -->
            <div style="border:1px solid #ccc; padding:15px; margin-bottom:20px;">
                <h2>Documents communs</h2>
                <c:forEach var="doc" items="${docsCommuns}">
                    <label>
                        <input type="checkbox" name="documents" value="${doc.id}">
                        ${doc.libelle}
                        <span style="color:red">
                            <c:choose>
                                <c:when test="${doc.obligatoire}">*</c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
</span>
                    </label><br>
                </c:forEach>
            </div>
            <!-- Bloc 2 : Documents spécifiques -->
            <c:if test="${not empty docsSpecifiques}">
                <div style="border:1px solid #007bff; padding:15px;">
                    <h2>Documents spécifiques</h2>
                    <c:forEach var="doc" items="${docsSpecifiques}">
                        <label>
                            <input type="checkbox" name="documents" value="${doc.id}">
                            ${doc.libelle}
                            <span style="color:red">
                                <c:choose>
                                    <c:when test="${doc.obligatoire}">*</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </span>
                        </label><br>
                    </c:forEach>
                </div>
            </c:if>
            <input type="hidden" name="type_visa" value="${typeVisa}">
            <br>
            <button type="submit">Enregistrer</button>
        </form>
    </div>
</body>
</html>
