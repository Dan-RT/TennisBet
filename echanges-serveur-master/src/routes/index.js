const express = require('express');
const router = express.Router();

const Partie = require('../modeles/partie.js');
const Joueur = require('../modeles/joueur.js');

/* GET home page. */
router.get('/', function (req, res, next) {
  res.send('Bienvenue dans le serveur du service Échanges.');
  res.send(p);
});

router.get('/ListMatchs', function (req, res, next) {

	var game_1 = new Partie(0, new Joueur('Albert', 'Ramos', 28, 56, 'Espagne'), new Joueur('Milos', 'Raonic', 28, 16, 'Canada'), '1', 'hale', '12h30');  
	var game_2 = new Partie(1, new Joueur('Roger', 'Federer', 28, 56, 'Suisse'), new Joueur('Rafael', 'Nadal', 28, 16, 'Espagne'), '1', 'hale', '10h30');  
	var game_3 = new Partie(2, new Joueur('Lucas', 'Pouille', 28, 56, 'France'), new Joueur('Kai', 'Nishikori', 28, 16, 'Japon'), '1', 'hale', '12h30');  
	var game_4 = new Partie(3, new Joueur('Alexandre', 'Zverev', 28, 56, 'Allemand'), new Joueur('Dominic', 'Thiem', 28, 16, 'Autriche'), '1', 'hale', '10h30');  

	var ListMatchs = {
		"matchs": [
		{0 : game_1},
		{1 : game_2},
		{2 : game_3},
		{3 : game_4}
	]};

	res.send(ListMatchs);
});

router.get('/match/:id', function (req, res, next) {
	var id_match = req.params.id;

	console.log("id received: " + id_match);
	var game = null;

	switch (id_match) {
		case '0':
			game = new Partie(0, new Joueur('Albert', 'Ramos', 28, 56, 'Espagne'), new Joueur('Milos', 'Raonic', 28, 16, 'Canada'), '1', 'hale', '12h30');  
			break;
		case '1':
			game = new Partie(1, new Joueur('Roger', 'Federer', 28, 56, 'Suisse'), new Joueur('Rafael', 'Nadal', 28, 16, 'Espagne'), '1', 'hale', '10h30');  
			break;
		case '2':
			game = new Partie(2, new Joueur('Lucas', 'Pouille', 28, 56, 'France'), new Joueur('Kai', 'Nishikori', 28, 16, 'Japon'), '1', 'hale', '12h30');  
			break;
		case '3':
			console.log("case 3");
			game = new Partie(3, new Joueur('Alexandre', 'Zverev', 28, 56, 'Allemand'), new Joueur('Dominic', 'Thiem', 28, 16, 'Autriche'), '1', 'hale', '10h30');  
			break;
		default:
			console.log("Default\tid match: " + id_match);
			break;
	}

	if (game) {
		var ListMatchs = {
			"matchs": [
			{0 : game}
		]};
		res.send(ListMatchs);
	} else {
		res.send("Error");
	}
});


module.exports = router;
