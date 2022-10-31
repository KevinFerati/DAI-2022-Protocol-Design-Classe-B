# Protocol specs

## Protocol objectives: what does the protocol do?
Permettre à un client de recevoir le résultat de l'application d'une opération mathématique sur 2 variables 


## Overall behavior:

### What transport protocol do we use?
TCP pour ouvrir une seule fois la connection et permettre d'effectuer plusieurs calculs

### How does the client find the server (addresses and ports)?

### Who speaks first?
le client qui souhaite effectuer un calcul

### Who closes the connection and when?
le client à la réception du résultat

## Messages:
### What is the syntax of the messages?

*client*:
> what happens when we apply [operation] to [A] and [B]

*server*:
> the result of [operation] applied to [A] and [B] is : [result]

### What is the sequence of messages exchanged by the client and the server? (flow)
1. hello client
2. hello serveur
3. question client
4. réponse serveur
5. bye bye client
6. bye bye serveur

### What happens when a message is received from the other party? (semantics)

## Specific elements (if useful)

## Supported operations
- addition: +
- soustraction: -
- division: /
- multiplication: *
- modulo: %

## Error handling
- Si le client essaie de casser la calculatrice (ex: division par zéro)
le serveur prévient ces erreurs et lui répond en l'insultant copieusement il l'avait vu venir:

*serveur*:
> the result of [operation] applied to [A] and [B] could not be made, did you really think you could fool me ?
> I was there when it was written.

- Si des execeptions sont levées une réponse spécifique est envoyée du type:

*serveur*:
> the result of [operation] applied to [A] and [B] could not be found

## Extensibility

- Ajout de nouvelles opérations mathématiques
- Possibilité de calcul sur plus que 2 nombres

## Examples: examples of some typical dialogs.

*client*:
> Hello calculator

*server*:
> Hello random client

*client*:
> what happens when we apply [operation] to [A] and [B]

*server*:
> the result of [operation] applied to [A] and [B] is : [result]

*client*:
> 'kay bye calculator

*server*:
> bye random client