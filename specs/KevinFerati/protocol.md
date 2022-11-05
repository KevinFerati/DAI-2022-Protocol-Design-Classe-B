# DAI
## Spécifications d'un protocol de calcul client-serveur
Auteur : Kevin Ferati
### Objectif de ces spécifications
L'objectif de ce protocol est de spécifier et de réglementer le protocol de la calculatrice à distance. Celle-ci sera implémenté sous la forme d'une application client qui va gérer l'envoi au serveur qui va ensuite réaliser le calcul et le transmettre au client.

Ce protocol va définir l'ordre des messages (client <=> server), le protocole de transport le port, qui va initialiser la connexion, le format des messages, les intervalles de valeurs acceptées, les formules acceptées, le format d'un message (en cas d'erreur ou autre).

### Comportement global

#### Protocol de transport

Les communications entre le client et le serveur se feront en utilisant le protocole TCP / IP. Le serveur écoutera sur le port **54328**.

#### Initialisation de la connexion
Ce sera le client qui devra initialiser la connexion avec le serveur.

#### Trouver le serveur depuis le client
Le serveur et client seront exécutés depuis le même host. L'adresse sera donc "localhost". Quant au port, il sera fixe et donc est prévisibile et ne nécessitera pas d'être trouvé.

#### Fermeture de la connexion
Le client devra envoyer les caractères STP pour fermer la connexion.

### Spécifications détaillées
Les opérations possibles sont uniquement binaires. Il n'y a pas de notion de paranthèses. Chaque opération est atomique.

#### Opérateurs acceptées
Il y aura 4 opérateurs acceptées représentées par un code à 3 lettres :
| Opérateur  | Code   |
|---|---|
|   Division | DIV  |
|   Addition | ADD  |
|   Suppression | SUB  |
|   Multiplication | MUL  |

#### Valeurs acceptées
Les valeurs des opérandes peut prendre des valeurs dans l'intervalle des doubles en java, c'est-à-dire de 4.9e-324 à 1.8e+308. 
Les valeurs peuvent être ou non chiffres à décimal. Dans ce cas, le caractère séparant les parties avant et après la virgule doit être un ".".
Sans signe, les valeurs seront positives par défaut. Les opérandes négatives doivent être préfixées d'un "-".

#### Messages

Chaque ligne sera séparée par le caractère ';'

##### Demande de calcul : Client -> serveur
Depuis le client, les messages doivent être au format suivant : 

```
[Opérande 1];[Opérateur];[Opérande 2]
```
Par exemple : 

```
-43.32;ADD;32
```

#### Réponses : Serveur -> client
##### Calcul accepté
Dans le cas où le calcul a été accepté, le format du résultat sera le suivant : 
```
OK;[Résultat]
```

En prenant l'exemple du dessus :
```
OK;-11.32
```

##### Erreur

En cas d'erreur, le format est le suivant : 
```
ERR;[Code d'erreur]
```

Les codes d'erreur possibles sont les suivants : 
| Code  | Description  |
|---|---|
| 1  | Opérande non acceptée  |
| 2  | Erreur inconnue  |
| 3  | Opérateur inconnu|
| 4  | Division par zéro
| 5  | Message malformé (ne correspond pas aux spécifications)