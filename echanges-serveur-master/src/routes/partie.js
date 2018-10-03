const express = require('express');
const router = express.Router();

const gen = require('../generateur');

/* GET parties listing. */
router.get('/ListMatchs', function (req, res, next) {

	const game_1 = new Partie(new Joueur('Albert', 'Ramos', 28, 56, 'Espagne'), new Joueur('Milos', 'Raonic', 28, 16, 'Canada'), '1', 'hale', '12h30');  
	//const game_2 = new Partie(new Joueur('Roger', 'Federer', 28, 56, 'Suisse'), new Joueur('Rafael', 'Nadal', 28, 16, 'Espagne'), '1', 'hale', '10h30');  

	/*var ListMatchs = {
		match_1 : game_1,
		match_2 : game_2
	}*/

	res.send(game_1);
});

router.get('/:id', function (req, res, next) {
  res.send(gen.liste_partie[req.params.id]);
});


router.post('/post', function(req, res) {
	console.log(req.body);
	res.send("TEST");
});

module.exports = router;
