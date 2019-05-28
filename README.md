# DEBS2016V2

## Résultats de l'algorithme

### Test léger (3 Ko)

temps moyen : 120 ms
temps moyen par évènement : 4 ms
=> 250 évènements par seconde

Le résultat semble lent à cause de la petite taille des données. Peu d'évènements sont traités par rapport au temps d'initialisation du projet.

### Test moyen (120 Mo)

temps moyen : 33.3 s
temps moyen par évènement : 28.3 µs
=> 35 335 évènements par seconde

Le résultat montre notre algorithme à son pic de performance (pas trop lourd, pas trop léger)

### Test lourd (4.5 Go)

temps moyen : 25 min 30 s
temps moyen par évènement : 35 µs
=> 28 571 évènements par seconde

Le résultat est en baisse du fait que le garbage collector prend du temps de calcul et passe bien trop souvent à cause de l'amas de données à traiter.
