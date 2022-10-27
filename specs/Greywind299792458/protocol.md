# Protocol specs

## Protocol objectives: what does the protocol do?

Permettre à un client de recevoir le résultat d'une opération mathématique effectuée sur 2 valeurs numériques.

## Overall behavior:

### What transport protocol do we use?
TCP pour ouvrir une seule fois la connection et permettre d'effectuer plusieurs calculs

### How does the client find the server (addresses and ports)?

Le serveur et le client tournent sur la même machine l'adresse sera donc localhost.

On peut choisir un port parmis les ports éphemères (49152 - 65535)

### Who speaks first?
le client qui souhaite effectuer un calcul

### Who closes the connection and when?
le client à la réception du résultat

## Messages:
### What is the syntax of the messages?

Chaque message contient toujours 2 éléments séparés par des \n et se terminant par un double \n\n :

> 1 : Opération

Côté client :

- COMPUTE ADD
- COMPUTE MULT
- COMPUTE DIV
- COMPUTE SUB
- COMPUTE MODULO

Côté serveur :
- RESULT
- ERROR

Partagé :

- HELLO
- END

> 2 : Contenu

- vide
- valeurs numériques par des \n

### What is the sequence of messages exchanged by the client and the server? (flow)
1. client : HELLO \n\n
2. serveur : HELLO \n\n
3. client : COMPUTE ADD \n 2 \n 3 \n\n
4. serveur : RESULT 5 \n\n
5. client : END \n\n
6. serveur : END \n\n

### What happens when a message is received from the other party? (semantics)

## Specific elements (if useful)

## Supported operations

- addition: +
- soustraction: -
- division: /
- multiplication: *
- modulo: %

## Error handling

cas possibles :

- opérations impossibles (divisions par zéro par ex.)
- erreur interne côté serveur pendant le calcul
- erreur pendant la communication

le serveur renvoie dans le contenu une valeur numérique indiquant un code d'erreur

> 0 : internal error

> 1 : communication error

> 2 : operation forbidden

## Extensibility

- Ajout de nouvelles opérations mathématiques
- Possibilité de calcul sur plus que 2 nombres

## Examples: examples of some typical dialogs.

> cas ok

1. client : HELLO \n\n
2. serveur : HELLO \n\n
3. client : COMPUTE ADD \n 2 \n 3 \n\n
4. serveur : RESULT 5 \n\n
5. client : END \n\n
6. serveur : END \n\n

> cas not ok

1. client : HELLO \n\n
2. serveur : HELLO \n\n
3. client : COMPUTE DIV \n 2 \n 0 \n\n
4. serveur : ERROR 2 \n\n
5. client : END \n\n
6. serveur : END \n\n

1. client : HELLO \n\n
2. serveur : HELLO \n\n
3. client : COMPUTE ADD \n 2 \n 3 \n\n
4. serveur : RESULT 5 \n\n
3. client : COMPUTE SUB \n 2 \n 3 \n\n
4. serveur : RESULT -1 \n\n
5. client : END \n\n
6. serveur : END \n\n


## questions :

- le serveur ne devrait-il pas fermer lui même la connection ?
    - évite des oublis user + peut fermer direct si erreur

- ajouter un champ additionnel pour les messages d'erreurs ?