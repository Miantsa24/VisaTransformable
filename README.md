# VisaTransformable

pre-recquis:
java 17
mvn installe
mysql installe

Avant chaque changement, verifier dans quelle branche vous travaillez
Il faut faire des commits coherents (le nom des commits)

verifier les configs dans applications.properties : username = root, password = root (a modifier selon le votre)

Comment faire marcher le projet:
mysql actif, base insere
Execution a la racine du projet dans /backoffice :
mvn clean install -> message : build success
mvn spring-boot:run -> message : 2026-04-18T19:12:04.256+03:00  INFO 24780 --- [transformable] [           main] c.v.t.TransformableApplication           : Started TransformableApplication in 2.123 seconds (process running for 2.347)
 l'application demarre a localhost:8080 

Lire d'abord comprehension.md avant de coder
Et lire le to do en entier (celui de dev 1 et dev 2)

La liste des documents a cocher est dans comprehension.md