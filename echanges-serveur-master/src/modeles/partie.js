const Pointage = require('./pointage.js');

class Partie {
  constructor (joueur1, joueur2, terrain, tournoi, heureDebut, tickDebut) {
    this.joueur1 = joueur1;
    this.joueur2 = joueur2;
    this.terrain = terrain;
    this.tournoi = tournoi;
    this.heure_debut = heureDebut;
    this.pointage = new Pointage(this);
    this.temps_partie = 0;
    this.joueur_au_service = Math.floor(Math.random() * 2);
    this.vitesse_dernier_service = 0;
    this.nombre_coup_dernier_echange = 0;
    this.constestation = [3, 3];
    this.tick_debut = tickDebut;
    this.totalPariJ1 = 0;
    this.totalPariJ2 = 0;
    this.montantTotalPari = 0;
    this.montantPariGagnant = -1;
    this.idVainqueur = -1;
  }

  jouerTour () {
    
    let contestationReussi = false;
    if ((Math.random() * 100) < 20) { // 3% de contestation
      if (!Partie.contester()) {
        const contestant = Math.floor(Math.random() * 2);
        this.constestation[contestant] = Math.max(0, this.constestation[contestant] - 1);
        console.log('contestation echouee');
      } else {
        contestationReussi = true;
        console.log('contestation reussie');
      }
    }

    if (!contestationReussi) {
      this.pointage.ajouterPoint(Math.floor(Math.random() * 2));
    }
    this.temps_partie += Math.floor(Math.random() * 60); // entre 0 et 60 secondes entre chaque point
    this.vitesse_dernier_service = Math.floor(Math.random() * (250 - 60 + 1)) + 60; // entre 60 et 250 km/h
    this.nombre_coup_dernier_echange = Math.floor(Math.random() * (30 - 1 + 1)) + 1; // entre 1 et 30 coups par échange

    this.afficherPoints();
  }

  afficherPoints() {
    console.log(this.joueur1.toString() + " " + this.pointage.getPoints(0));
    console.log(this.joueur2.toString() + " " + this.pointage.getPoints(1) + "\n");
  }

  static contester () {
    return (Math.random() * 100) > 75; // 75% de chance que la contestation passe
  }

  changerServeur () {
    this.joueur_au_service = (this.joueur_au_service + 1) % 2;
  }

  nouvelleManche () {
    this.constestation = [3, 3];
  }

  estTerminee () {
    return this.pointage.final;
  }

  _determinerVaingueur () {
    if (this.pointage.final) {
        if (this.pointage.manches[0] > this.pointage.manches[1]) {
            this.idVainqueur = 0;
            this.montantPariGagnant = this.totalPariJ1;
        } else if(this.pointage.manches[0] < this.pointage.manches[1]) {
            this.idVainqueur = 1;
            this.montantPariGagnant = this.totalPariJ2;
        } else {
            this.idVainqueur = -1;
            this.montantPariGagnant = -1;
        }
    } else {
        this.idVainqueur = -1;
    }
  }

  ajouterPari(montant, idJoueur) {
      if (idJoueur === 0) {
          this.totalPariJ1 += montant;
          console.log("montantTotalPari: " + this.totalPariJ1);
      } else if (idJoueur === 1){
          this.totalPariJ2 += montant;
          console.log("montantTotalPari: " + this.totalPariJ2);
      }
      this.setMontantTotalPari()
  }

  montantPariJ1 () {
    return this.totalPariJ1;
  }

  montantPariJ2 () {
    return this.totalPariJ2;
  }

  setMontantTotalPari () {
      this.montantTotalPari = this.totalPariJ1 + this.totalPariJ2;
      console.log("montantTotalPari: " + this.montantTotalPari);
  }

  renvoyerGain(mise, idJoueurParie) {
    if (this.pointage.final) {
        this._determinerVaingueur();
        if (idJoueurParie === this.idVainqueur) {
            //gagné

            var portionPariGagnant = mise / this.montantPariGagnant;
            return portionPariGagnant * this.montantPariGagnant;

        } else {
            //perdu
            return 0;
        }
    } else {
        return -1;
    }
  }

  toJSON () {
    return {
      'joueur1': this.joueur1,
      'joueur2': this.joueur2,
      'terrain': this.terrain,
      'tournoi': this.tournoi,
      'heure_debut': this.heure_debut,
      'pointage': this.pointage,
      'temps_partie': this.temps_partie,
      'serveur': this.joueur_au_service,
      'vitesse_dernier_service': this.vitesse_dernier_service,
      'nombre_coup_dernier_echange': this.nombre_coup_dernier_echange,
      'constestation': this.constestation
    };
  }
}
module.exports = Partie;
