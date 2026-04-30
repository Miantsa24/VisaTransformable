                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Date de modification</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Statut</th>
                            <th style="text-align:left;padding:12px;border-bottom:1px solid #e2e8f0;">Commentaire</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="h" items="${historiques}">
                            <tr>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <fmt:formatDate value="${h.dateChangement}" pattern="yyyy-MM-dd" />
                                </td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    ${h.statutDemande.libelle}
                                </td>
                                <td style="padding:12px;border-bottom:1px solid #e2e8f0;">
                                    <c:out value="${h.commentaire}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div style="margin-top:24px;">
                <a href="/backoffice/demande/liste" style="display:inline-block;padding:12px 18px;border-radius:10px;background:#2563eb;color:#fff;text-decoration:none;font-weight:600;">Retour à la liste</a>
            </div>
        </div>
    </div>
</body>
</html>