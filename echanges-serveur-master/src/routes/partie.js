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

router.post('/pari/', function(req, res) {
	console.log("\nPOST request : " + JSON.stringify(req.body));
	
	res.send("Bite");
});

module.exports = router;
