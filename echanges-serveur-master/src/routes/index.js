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


	var ListMatchs = {
		"matchs": [
		{0 : game_1},
		{1 : game_2}
	]};

	res.send(ListMatchs);
});

router.get('/match/:id', function (req, res, next) {
	console.log("id received: " + req.params.id);

	var game = null;

	if (req.params.id == 1) {
		game = new Partie(0, new Joueur('Albert', 'Ramos', 28, 56, 'Espagne'), new Joueur('Milos', 'Raonic', 28, 16, 'Canada'), '1', 'hale', '12h30');  
	} else {
		game = new Partie(1, new Joueur('Roger', 'Federer', 28, 56, 'Suisse'), new Joueur('Rafael', 'Nadal', 28, 16, 'Espagne'), '1', 'hale', '10h30');  
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
