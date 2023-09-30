# Libraries
- sqlite-jdbc
- javafx-sdk
- gson  
- junit
- SI-VOX (windows et linux)
 
# Maven (conseillé)
## Gson 
1. Aller sur `File -> Projetc Structure -> Librairies`
2. Appuyer sur le + en haut à gauche, sélectionner Maven et ajouter `com.google.code.gson:gson:2.10.1`

Le reste des librairies devrait se faire automatiquement en lançant Maven
## Run
1. Aller sur `Run -> Edit configuration` et ajouter une nouvelle application Maven
2. Coller dans la section Run,
```
javafx:run
```
## javafx (Windows)
>Cette section est à consulter seulement si lancer l'application en suivant les étapes précédentes
n'a pas marché (les autres OS n'ont pas l'air d'être affecté par le problème)

1. Il faut aller dans les variables d'environnement (dans les paramètres avancés du système ou taper variables d'environnement dans la barre de recherche)
2. Aller dans la variable `JAVA_HOME` et ajouter/remplacer le path vers **jdk19\bin** 
3. Dans la variable Path, le rajouter si ce n'est pas déjà fait et supprimer l'ancien (par exemple le path vers **jdk12\bin**)
4. Placer le path vers **jdk19\bin** tout en haut de tous les autres path et cela devrait marcher
5. Si ça ne va pas, supprimer les fichiers compilés .jar si certains sont présents et invalider le cache d'IntelliJ si vous utilisez IntelliJ

# Manuel (déconseillé)
## sqlite-jdbc
1. Aller sur `File -> Project structure -> Librairies`
2. Appuyer sur le + en haut à gauche, sélectionner java et ajouter `sqlite-jdbc-3.32.3.2.jar`

## javafx-sdk
1. Télécharger le javafx-sdk qui correspond à **votre OS et architecture** depuis [ici](https://gluonhq.com/products/javafx/)
et extraire vers `2023-groupe-9/lib`
2. Aller sur `File -> Project structure -> Librairies`
3. Appuyer sur le + en haut à gauche, sélectionner java et choisir le dossier `*/lib` de la librairie javafx-sdk  
4. Aller sur `Run -> Edit configuration` et ajouter une nouvelle configuration d'application avec Main comme classe principale
5. Appuyer sur `add vm options` et coller en complétant,
```
--module-path $PATH_TO_PROJECT$/2023-groupe-9/lib/javafx-sdk-19.0.2.1/lib
--add-modules=javafx.base,javafx.controls,javafx.graphics,javafx.fxml
```
> N'oubliez pas de remplacer $PATH_TO_PROJECT$ avec votre chemin