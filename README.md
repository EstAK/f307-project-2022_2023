# English Version
# INFOF307 - Flashcard
## Quick links
- [Task assignments](team/tasksAssignments.md)
- [Points per stories](team/updatedStories.md)
- [Dependencies](lib/setup.md)
- [Compilation steps](dist/compilation.md)
## Setup
> To setup project dependencies, see [this document](lib/setup.md)

Since iteration 2 and the creation of the server, the application can no longer launch
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

