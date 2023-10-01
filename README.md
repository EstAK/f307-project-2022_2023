# English Version
# INFOF307 - Flashcard
## Project Description
First of all, this project has been imported from the private school's GitLab server, and it currently has no commit history.
For this project, we developed a flashcard application that is executable on Linux, Windows, and MacOS. To accomplish this, we employed the JAVA programming language.
This project was undertaken using the XP agile methodology within a team of 10 individuals. Our main objective was to create a comprehensive flashcards application with the following features:
1.A secure login system.
2.A fully implemented card package management.
3.Diversity of card types such as multiple choice, open response and blank text to complete.
4.A system to import and export a card package on your computer.
5.A store where you can download other players custom card packages and upload yours as well.
6.Storage and data management in a database.
7.Multiple Learning Modes: Our application supports various learning modes to cater to different learning styles and needs. These modes include:
                          -Normal Revision: Users can review their flashcards at their own pace.
                          -Quiz Mode: Users can test their knowledge with randomized questions.
                          -Custom Difficulty Mode : Users can choose a difficulty mode for a certain card package from level 1(easiest) to 3(hardest).
8.Implementation of a server using sockets to enable communication among users.
9.Progress Tracking: The application keeps track of users' progress and performance.
10.Personal score and a ranking system.
11.A Text to speech system.
12.Modular Card : The users can create math questions with variables that are going to be given random values. For example a card with the question: a+b will always have a random answer.

Throughout the project, we applied agile principles and collaborated effectively within the team. We successfully developed a robust, cross-platform application using JAVA. We also tackled various technical challenges and provided a complete solution for learning project management.

## Quick links
- [Task assignments](team/tasksAssignments.md)
- [Points per stories](team/updatedStories.md)
- [Dependencies](lib/setup.md)
- [Compilation steps](dist/compilation.md)
## Setup
> To setup project dependencies, see [this document](lib/setup.md)

Since iteration 2 and the creation of the server, the application can no longer be launched
alone: ​​you must also launch the server application for this,
1. Go to `Run -> Edit Configuration`
2. Add an application that uses `Server.java` as its main class
> It is possible to launch the server and the client in a single click by 
> adding in a compound 
## Using the jar
If you use the pre-compiled `.jar` (for Windows), enter these commands in
the terminal to launch the client and the server,
```agsl
java -jar /path/to/g9-iteration-4.jar 
java -cp /path/to/g9-iteration-4.jar ulb.infof307.g9.server.Server
```
> Please note, the application will not work correctly if the server
> is not launched. Because, since iteration 2, many actions have taken place
> by the latter
### Other OS
You must [compile](dist/compilation.md) the `.jar` yourself. You will find it in
`/target` under the name of `g9-iteration-2-shaded.jar`
> Do not make the mistake of `.jar` because the unshaded version
> **It won't work** 

# French Version
# INFOF307 - Flashcard
## Description du projet
Bienvenue à bord! Tout d'abord, ce projet a été importé depuis le serveur GitLab de l'école privée, et il n'a actuellement aucun historique de commit.
Pour ce projet, nous avons développé une application de cartes mémoire exécutable sous Linux, Windows et MacOS. Pour ce faire, nous avons utilisé le langage de programmation JAVA.
Ce projet a été réalisé en utilisant la méthodologie agile XP au sein d'une équipe de 10 personnes. Notre objectif principal était de créer une application de cartes mémoire complète avec les fonctionnalités suivantes :
1.Un système de connexion sécurisé.
2.Une gestion complète des paquets de cartes.
3.Divers types de cartes, tels que les questions à choix multiples, les réponses ouvertes et les espaces réservés pour compléter.
4.Un système d'importation et d'exportation de paquets de cartes sur votre ordinateur.
5.Une boutique où vous pouvez télécharger les paquets de cartes personnalisés d'autres joueurs et télécharger les vôtres.
6.Stockage et gestion des données dans une base de données.
7.Modes d'apprentissage multiples : Notre application prend en charge différents modes d'apprentissage pour répondre à différents styles et besoins d'apprentissage. Ces modes comprennent :
                                    -Révision normale : les utilisateurs peuvent revoir leurs cartes à leur propre rythme.
                                    -Mode quiz : les utilisateurs peuvent tester leurs connaissances avec des questions aléatoires.
                                    -Mode de difficulté personnalisée : les utilisateurs peuvent choisir un niveau de difficulté pour un certain paquet de cartes, de 1 (le plus facile) à 3 (le plus difficile).
8.Mise en place d'un serveur en utilisant des sockets pour permettre la communication entre les utilisateurs.
9.Suivi de la progression : l'application suit la progression et la performance des utilisateurs.
10.Score personnel et un système de classement.
11.Un système de synthèse vocale (text-to-speech).
12.Carte modulaire : les utilisateurs peuvent créer des questions mathématiques avec des variables qui recevront des valeurs aléatoires. Par exemple, une carte avec la question "a+b" aura toujours une réponse aléatoire.
Tout au long du projet, nous avons appliqué les principes agiles et collaboré efficacement au sein de l'équipe. Nous avons réussi à développer avec succès une application robuste et multiplateforme en utilisant JAVA. Nous avons également relevé divers défis techniques et fourni une solution complète pour la gestion de projets d'apprentissage.

## Liens rapides
- [Attributions des tâches](team/tasksAssignments.md)
- [Points par histoires](team/updatedStories.md)
- [Dépendances](lib/setup.md)
- [Etapes de compilation](dist/compilation.md)
## Setup
>Pour setup les dépendances du projet, consultez [ce document](lib/setup.md)

Depuis l'itération 2 et la création du serveur l'application ne peut plus se lancer
seule : il faut aussi lancer l'application serveur pour cela,
1. Aller sur `Run -> Edit Configuration`
2. Ajoutez une application qui utilise `Server.java` comme classe principale
> Il est possible de lancer le serveur et le client en un seul click en les 
> ajoutant dans un compound 
## Utilisation du jar
Si vous utilisez le `.jar` pré-compilé (pour Windows), rentrez ces commandes dans
le terminal pour lancer le client et le serveur,
```agsl
java -jar /path/to/g9-iteration-4.jar 
java -cp /path/to/g9-iteration-4.jar ulb.infof307.g9.server.Server
```
> Attention, l'application ne fonctionnera pas correctement si le serveur
> n'est pas lancé. Car, depuis l'itération 2 beaucoup d'actions passent
> par ce dernier
### Autres OS
Il faudra [compiler](dist/compilation.md) le `.jar` soit même. Ce dernier se retrouvera
dans `/target` sous le nom de `g9-iteration-2-shaded.jar`
> Ne pas se tromper de `.jar` car, la version non shaded 
> **ne fonctionnera pas** 

