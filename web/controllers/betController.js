app.controller('betController', ['$rootScope', '$scope', '$http', function($rootScope, $scope, $http) {
	let vm = this;
	$scope.bets = [];
	$scope.idMatch = null;
	$scope.idPlayer = null;

	$scope.$on('bet-clicked', function(event, args) {
    	$scope.idMatch = args.id.idMatch;
	});

	vm.storeBet = function (bet, idMatch, idPlayer) {
		if (typeof(Storage) !== "undefined") {
			localStorage.setItem(idMatch, [idPlayer, bet]);
			console.log("\n----- \nBet stored: ");
			console.log(localStorage.getItem(idMatch));
			console.log("----- \n");
		}
	}

	vm.showBetAmountOnPage = function () {
		//debug 

		/*console.log("\n--------\n");
		console.log("showBetAmountOnPage function");
		console.log("match 1");*/

		//match 1
		data = localStorage.getItem(0);
		var dataArray = JSON.parse("[" + data + "]");
		//console.log(typeof(array));
		if (dataArray[0] != undefined && dataArray[1] != undefined) {
			document.getElementById("bet-alert1").innerHTML = "You bet $" + dataArray[1] + " on match 1.";
		} 
		
		//match 2 
		dataArray = null;
		data = localStorage.getItem(1);
		dataArray = JSON.parse("[" + data + "]");
		//console.log("\nmatch 2");
		//console.log(data);
		if (dataArray[0] != undefined && dataArray[1] != undefined) {
		document.getElementById("bet-alert2").innerHTML = "You bet $" + dataArray[1] + " on match 2.";;
		}		
	}

	vm.showBetAmountOnPage(); 

	vm.getBetStored = function (idMatch) {
		console.log(localStorage.getItem(idMatch));
		return localStorage.getItem(idMatch);
	}

	vm.alreadyBet = function (idMatch) {
		if(vm.getBetStored(idMatch) == null) {
			return false;
		} else {
			return true;
		}
	}

	vm.betRequest = function (bet) {
    	console.log("request bet : POST");
	    $http({
	        method: "POST",
	        url: "https://localhost:8000/parties/pari",		//127.0.0.1
	        headers: {
			    'Accept': 'application/json',
			    'Content-Type': 'application/json',
			},
	        params: bet
	    }).then(function mySuccess(response) {
	        vm.showBetAmountOnPage();
	    }, function myError(response) {
			console.log(response.statusText);
	    })
	}

    $scope.postBet = function (betInfos){
    	if (!vm.alreadyBet(parseInt($scope.idMatch))) {
    		if(betInfos.player1){
				$scope.idPlayer = 0;
			} else {
				$scope.idPlayer = 1;
			}
			var bet = new Bet(parseInt($scope.idMatch), $scope.idPlayer, betInfos.amount);
			$scope.bets.push(bet);
			console.log($scope.bets);
			vm.storeBet(bet.bet_amount, bet.id_match, bet.id_player);
			vm.betRequest(bet);
    	} else {
    		alert("You have already bet on that match.");
    	}
    	$rootScope.$broadcast('modal-bet');
		
	};

	$scope.cancelBet = function (){
		$rootScope.$broadcast('modal-bet');
	};
	
}]);