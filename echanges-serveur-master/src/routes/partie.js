const express = require('express');
const router = express.Router();

const gen = require('../generateur');

/* GET parties listing. */
router.get('/', function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  res.send(gen.liste_partie);
});


router.get('/:id', function (req, res, next) {
  console.log("Request on match " + req.params.id);
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  res.send(gen.liste_partie[req.params.id]);
});

router.get('/resultat/id_match/:id_match/id_player/:id_player/montant/:montant', function (req, res, next) {
  	console.log("GET Request Pari");
	console.log("id_match " + req.params.id_match);
	console.log("id_player " + req.params.id_player);
	console.log("montant " + req.params.montant);

	var gain = gen.liste_partie[req.params.id_match].renvoyerGain(parseFloat(req.params.montant), parseInt(req.params.id_player));

	res.header("Access-Control-Allow-Origin", "*");
	res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  	res.send(gain.toString());
});

router.post('/pari/', function(req, res) {
	console.log(req.query);

	var result = JSON.stringify(req.query);
    //var result = result.slice(2,-5);
	var resultCleaned = result.replace(/\\"/g, '"');
	console.log("\nPOST request resultCleaned : " + resultCleaned);

    var resultJSON = JSON.parse(resultCleaned); 
    console.log(resultJSON);

	console.log("\nid match : " + resultJSON.id_match);
	console.log("\nid player : " + resultJSON.id_player);
	console.log("\nbet bet_amount : " + resultJSON.bet_amount);

	gen.liste_partie[resultJSON.id_match].ajouterPari(parseInt(resultJSON.bet_amount), parseInt(resultJSON.id_player));

	//console.log("\nMontant total pari: " + gen.liste_partie[resultJSON.id_match].montantTotalPari());

	res.header("Access-Control-Allow-Origin", "*");
	res.send(200);
});

module.exports = router;

