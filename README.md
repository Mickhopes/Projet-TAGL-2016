# Projet TAGL clé-valeur
Projet de TAGL sur les outils vus en cours.


## Membres du groupe
- Line Pouvaret
- Antoine Thebaud
- Aurélien Monnet-Paquet
- Mickaël Turnel

## Fonctionnalités du projet
Le projet est composé en 3 parties principales:
- La partie stockage
- La partie serveur
- La partie client

### Stockage
Fonctionnalités implémentées:
- Stockage d'objets sous forme de clé valeur.

Chaque objet est sauvegardé et accessible par une clé.
- Parallélisation.

Un même "espace" de stockage (instance) est **synchronisé** et donc utilisable par plusieurs threads simultanément.
- Cache LRU.

Notre projet ne gère pas la taille des objets en mémoire, mais la **quantité** d'objets en mémoire car Java ne nous permet seulement que des valeurs approximatives de taille.

Une quantité **maximale** d'objets est définie soit par défaut soit par l'utilisateur lors de l'instanciation d'un objet Stockage.
Lorsque que cette limite est sur le point d'être dépassée, le plus ancien objet est stocké dans le système de fichier et supprimé de la mémoire. Si l'utilisateur cherche à utiliser une valeur sauvegardée dans le système de fichier, l'objet est remis en mémoire (et le plus ancien est sauvegardé si la taille limite est toujours dépassée).
- Manipulation de structures complexes

Nous avons choisi d'ajouter les structures de listes. En plus de pouvoir stocker des objets par rapport à une clé, un utilisateur peut stocker une liste d'objets associée à une clé.

Il peut ainsi manipuler cette liste directement à partir de sa clé.

## Fonctionnalités validées par des tests unitaires

- Stockage

Toutes les fonctionnalités de la partie stockage ont été validées avec l'outil EclEmma.
Seulement quelques catch d'exception non pas été couvert (pour les fonctions _sauvegarderPlusAncien_ et _chargerObjet_)

## Manuel d'utilisation du client et du serveur

### Serveur
Il faut exécuter le fichier `Serveur.java` pour lancer le serveur qui se met en écoute sur le port _4242_.

### Client
Il faut exécuter le fichier `Client.java`pour lancer le client (plusieurs peuvent être lancés en même temps).

Un invité de commande s'ouvre et permet d'utiliser les fonctionnalités de notre projet.
Le client ne permet de tester qu'avec des chaînes de caractères à titre d'exemple.

Voici la liste des commandes utilisables:

- HELP - Affiche la liste des commandes.
- SET clé valeur - Crée une nouvelle entré avec la clé et la valeur.
- GET clé - Récupère la valeur pointée par la clé.
- DEL clé - Supprime l'entrée pointée par la clé.
- RPUSH clé valeur - Ajoute la valeur à la fin de la liste pointée par la clé.
- LPUSH clé valeur - Ajoute la valeur au début de la liste pointée par la clé.
- RPOP clé - Supprime le dernier élément de la liste pointée par la clé.
- LPOP clé - Supprime le premier élément de la liste pointée par la clé.
- LRANGE clé début fin - Retoune la liste des valeurs de l'index de début à l'index de fin de la liste pointée par la clé. Si -1 est indiqué pour fin, alors toute la liste est renvoyée.

Pour les commandes RPUSH et LPUSH, si la liste n'existe pas, elle est créée.

## Feedback

Travis est un bon outil qui nous a permis de détecter rapidement les erreurs dans notre code.
Malheureusement ce qu'il manque à Travis est l'opportunité de pouvoir ajouter des collaborateurs pour leur permettre de voir les résultats. Actuellement le seul moyen est de mettre les collaborateurs en admin du projet.

Le projet est assez intéressant et nous permet de bien "toucher à tout". Les parties Maven, tests unitaires et couvertures de code sont celles qui ont été le plus appréciées dans notre groupe car ce sont des outils nécessaires dans le monde de l'entreprise.

Ce qui pourrait être intéressant pour les prochaines années, mais peut-être dur à mettre en place, serait de faire en sorte que le projet se développe avec une gestion de projet (Agile ou incrémentale par exemple). C'est une partie que l'on voit beaucoup en cours mais qu'on ne pratique pas assez.
