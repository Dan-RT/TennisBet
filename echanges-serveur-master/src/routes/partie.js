const express = require('express');
const router = express.Router();

const gen = require('../generateur');

/* GET parties listing. */
router.get('/', function (req, res, next) {
  res.send(gen.liste_partie);
});

router.get('/:id', function (req, res, next) {
  console.log("Request on match " + req.params.id);
  res.send(gen.liste_partie[req.params.id]);
});

router.get('/resultat/:id_match/:id_player/:montant', function (req, res, next) {
  	console.log("GET Request Pari");
	console.log("id_match " + req.params.id_match);
	console.log("id_player " + req.params.id_player);
	console.log("montant " + req.params.montant);

	gain = gen.liste_partie[req.params.id_match].renvoyerGain(parseFloat(req.params.montant));

  	res.send(gain.toString());
});

router.post('/pari/', function(req, res) {
	var result = JSON.stringify(req.body);
    var result = result.slice(2,-5);
	var resultCleaned = result.replace(/\\"/g, '"');
	console.log("\nPOST request resultCleaned : " + resultCleaned);


    var resultJSON = JSON.parse(resultCleaned); 
    console.log(resultJSON);

	console.log("\nid match : " + resultJSON.id_match);
	console.log("\nid player : " + resultJSON.id_player);
	console.log("\nbet bet_amount : " + resultJSON.bet_amount);

	gen.liste_partie[resultJSON.id_match].ajouterPari(parseInt(resultJSON.bet_amount));

	console.log("\nMontant total pari: " + gen.liste_partie[resultJSON.id_match].montantTotalPari());
	res.send("Bite");
});

module.exports = router;
