app.controller('listMatchController', ['$rootScope', '$scope', '$http', function($rootScope, $scope, $http) {
	$scope.matchClicked = function(evt) {
		var idMatch = evt.target.id;
    	$rootScope.$broadcast('match-clicked', { id: {idMatch} });
	}
}]);