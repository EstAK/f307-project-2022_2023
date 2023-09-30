# INFOF307 - Flashcard
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

