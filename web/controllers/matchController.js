app.controller('matchController', ['$rootScope', '$scope', '$http', function($rootScope, $scope, $http) {
	$scope.div_=[]; 

	$scope.$on('match-clicked', function(event, args) {
		if($scope.div_[args.id.idMatch] == undefined){
			$scope.div_[args.id.idMatch] = false;
		}
    	console.log("event received");
    	console.log("ID : ", args.id);
    	$scope.div_[args.id.idMatch] = $scope.showElement($scope.div_[args.id.idMatch]);
	});

	$scope.betClicked = function(evt) {
		var idMatch = evt.target.id;
    	$rootScope.$broadcast('bet-clicked', { id: {idMatch} });
	}

}]);