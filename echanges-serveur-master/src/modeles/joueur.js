class Joueur {
  constructor (prenom, nom, age, rang, pays) {
    this.prenom = prenom;
    this.nom = nom;
    this.age = age;
    this.rang = rang;
    this.pays = pays;
  }

  toString() {
  	return this.prenom + " " + this.nom;
  }
}

module.exports = Joueur;
