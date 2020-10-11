'use strict';

/**
 * @ngdoc function
 * @name deliverySearchApp.controller:SearchCtrl
 * @description
 * # SearchCtrl
 * Controller of the deliverySearchApp
 */
angular.module('deliverySearchApp')
  .controller('SearchCtrl', ['paginator', '$http', '$scope', '$window', function(paginator, $http, $scope, $window) {

    $scope.results = [];
    $scope.pager = {};
    $scope.noRes = false;
    $scope.page = 1;
    
    $scope.changeSearch=function(query){
    	document.getElementById("query").value=query;
    	$scope.query=query;
    	$scope.search();
    }
/*
      var sessionObj =  $window.sessionStorage.getItem('results');
      if(sessionObj){

        $scope.results = JSON.parse(sessionObj);
        $scope.page = $window.sessionStorage.getItem('page');
        $scope.pager = JSON.parse($window.sessionStorage.getItem('pager'));
        $scope.resultsDisp = JSON.parse($window.sessionStorage.getItem('resDisp'));
      }
      */
    	$scope.test=function(){
    		var query=window.query;
    		var collection=window.collection;
    		if(collection!="[object HTMLInputElement]" && collection!="" && query!="[object HTMLInputElement]" && query!=""){
	    		$scope.query=query;
	    		$scope.collection=collection;
				document.getElementById("generateBtn").disabled = 0;
				document.getElementById("generateBtn").style="height:60px;width:130px;cursor: pointer;";
				$scope.search();
    		}
    	}
    
      $scope.search = function() {
    	document.getElementById("myDialog").show();
        $scope.results = [];
        $scope.noRes = false;
        console.log("search");

        $http.get("/SearchMetadata/Search?query=" + $scope.query +"&collection="+$scope.collection)
          .then(function successCallback(response1) {

              var searchResults = response1.data;
              $scope.results = searchResults;

              if($scope.results.length==0){
                $scope.noRes = true;
              }

              $scope.initController();

              $window.sessionStorage.setItem('results', JSON.stringify($scope.results));
              document.getElementById("myDialog").close();
          }, function errorCallback(response1) {
              $scope.noRes = true;
              $scope.initController();
        	  document.getElementById("myDialog").close();
            console.log(response1);
          });


    }

    $scope.initController = function() {
        $scope.pager=null;
        // initialize to page 1
        $scope.setPage(1);
    }

    $scope.setPage= function(page) {

      if(page!=1){
        if (page < 1 || page > $scope.pager.totalPages) {
            return;
        }
      }
        // get pager object from service
        $scope.pager = paginator.getPager($scope.results.length, page);

        // get current page of items
        $scope.resultsDisp = $scope.results.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);

        $window.sessionStorage.setItem('page', $scope.page);
        //$window.sessionStorage.setItem('pager', JSON.stringify($scope.pager));
        //$window.sessionStorage.setItem('resDisp', JSON.stringify($scope.resultsDisp));

    }

  }]);
