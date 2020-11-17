

var app = angular.module("myApp", []);
app.controller("myCtrl", function($scope,$http,$timeout,$window) {
  $scope.phrases1="";
  $scope.phrases2="";
  $scope.matrixes;
  $scope.concetto="Define concept";
  $scope.lines=3;
  $scope.columns=4;
  $scope.arraycolumns=new Array();
  $scope.matrixcells=[];
  $scope.arraylines=new Array();
  $scope.collection;
  $scope.loadedMatrix;
  $scope.loadedDescription="";
  $scope.phrases1=[];
  $scope.phrases2=[];
  $scope.textDescription;
  for(var i=0;i<$scope.columns;i++){
	  $scope.arraycolumns[i]={"label":"Define concept","terms":[""]};
  }
  
  for(var i=0;i<$scope.lines;i++){
	  $scope.arraylines[i]={"label":"Define concept","terms":[""]};
  }
  
  $scope.refresh=function(){
	  $timeout(function() {
	      $('.selectpicker').selectpicker('refresh');
	  });
  }
  
  $scope.getinfos=function(){
	  if($scope.loadedMatrix!=' '){
		  for(var i=0;i<$scope.lines;i++){
			  if($scope.arraylines[i].terms!="" && $scope.phrases2[i]){
				document.getElementById('secondTextInput'+i).style.display="none";
				document.getElementById('tokensSecond'+i).style.display="inline";
			  }
		  }
		  for(var i=0;i<$scope.columns;i++){
			  if($scope.arraycolumns[i].terms!="" && $scope.phrases1[i]) {
				document.getElementById('firstTextInput'+i).style.display="none";
				document.getElementById('tokensFirst'+i).style.display="inline";
			  }
		  }
	  }
  }
  
  
  
  $scope.saveMatrix=function(){  
	  var collName=$scope.collection;
	  var linesConcepts=new Array();
	  var columnsConcepts=new Array();
	  var saveName=$scope.saveName;
	  var date=currentDate();
	  var author=$scope.saveAuthor;
	  var phrases1=$scope.phrases1;
	  var phrases2=$scope.phrases2;
	  var obj3={};
	  var obj={};
	  var rows=new Array();
	  var columns=new Array();
	  var description=$scope.saveDesc;
		for(i=0;i<$scope.lines;i++){
			  rows.push($scope.arraylines[i]);
		  }
		for(i=0;i<$scope.columns;i++){
			  columns.push($scope.arraycolumns[i]);
		  }
		obj3={"conceptRighe":rows,"conceptColonne":columns,"collection":collName,"author":author,"date":date,"description":description,"name_A":saveName, "phrases1":phrases1,"phrases2":phrases2};
		request=JSON.stringify(obj3);
		$http.post("/SearchMetadata/Matrix",request)
	    .then(function successCallback(response1) {
	    	console.log('post',response1);
	    	alert("Saved successfully");
	    }, function errorCallback(response1) {
	        console.log(response1);
	        alert("Error. Matrix not saved");
	    });
    }
	
  	$scope.deleteMatrix=function(index){
  		$http.get("/SearchMetadata/Matrix?index="+index)
	    .then(function successCallback(response1) {
	    	console.log('post',response1);
	    	alert("Deleted successfully");
	    	location.reload();
	    }, function errorCallback(response1) {
	        console.log(response1);
	        alert("Error. Matrix not deleted");
	    });
  	}
  
	$scope.getMatrixes=function(){
		$http.get("/SearchMetadata/LoadMatrix")
	    .then(function successCallback(response1) {
	    	console.log('get',response1);
	    	var matrixes=response1.data["matrixModels"];
	    	$scope.matrixes=matrixes;
	    }, function errorCallback(response1) {
	        console.log(response1);
	    });
	}
	
	$('#columns').on('change',function(){
		 $scope.arraycolumns=new Array();
		  for(var i=0;i<$scope.columns;i++){
			  $scope.arraycolumns[i]={"label":"Define concept","terms":[""]};
		  }
		  $scope.initMatrix();
		  
	});
	
	$('#lines').on('change',function(){
	 $scope.arraylines=new Array();
	  for(var i=0;i<$scope.lines;i++){
		  $scope.arraylines[i]={"label":"Define concept","terms":[""]};
	  }
	  $scope.initMatrix();
	});
	
	$scope.resetLabels=function(){
		 $scope.arraycolumns=new Array();
		  for(var i=0;i<$scope.columns;i++){
			  document.getElementById("firstTextInput"+i).style.display="inline";
			  document.getElementById("tokensFirst"+i).style.display="none";
			  $scope.arraycolumns[i]={"label":"Define concept","terms":[""]};
			  $scope.phrases1=new Array($scope.columns);
		  }
		 $scope.arraylines=new Array();
		  for(var i=0;i<$scope.lines;i++){
			  document.getElementById("secondTextInput"+i).style.display="inline";
			  document.getElementById("tokensSecond"+i).style.display="none";
			  $scope.arraylines[i]={"label":"Define concept","terms":[""]};
			  $scope.phrases2=new Array($scope.lines);
		  }
		  
		  $scope.initMatrix();
	}
	
	$scope.getSelectedMatrix=function(){
		$scope.loadedMatrix=JSON.parse($window.sessionStorage.getItem("Matrix"));
		if($scope.loadedMatrix!=' '){
			$scope.lines=$scope.loadedMatrix.conceptRighe.length;
			$scope.columns=$scope.loadedMatrix.conceptColonne.length;
			$scope.collection=$scope.loadedMatrix.collection;
			$scope.arraycolumns=$scope.loadedMatrix.conceptColonne;
			$scope.arraylines=$scope.loadedMatrix.conceptRighe;
			$scope.loadedDescription=$scope.loadedMatrix.description;
			$scope.phrases1=$scope.loadedMatrix.phrases1;
			$scope.phrases2=$scope.loadedMatrix.phrases2;
			if(document.getElementById("collection").value){
				document.getElementById("generateBtn").style="height:60px;width:130px;cursor: pointer;";
				document.getElementById("generateBtn").disabled = 0;
			}
			if($scope.phrases1==null){
				$scope.phrases1=new Array($scope.columns);
			}
			if($scope.phrases2==null){
				$scope.phrases2=new Array($scope.lines);
			}
		}else{
			  $scope.concetto="Define concept";
			  $scope.lines=3;
			  $scope.columns=4;
		}
		 $scope.initMatrix();
	}
	
	$scope.setMatrix=function(variable){
		$window.sessionStorage.setItem("Matrix",JSON.stringify(variable));
	}
	
	$scope.initMatrix=function(){
		for(var i=0;i<$scope.lines;i++){
			$scope.matrixcells[i]=[];
			for(var j=0;j<$scope.columns;j++){
				$scope.matrixcells[i][j]="";
			}
		}
	}
	
	$scope.checkValues=function(message){
		for(var i=0;i<$scope.lines;i++){
			 if($scope.arraylines[i].terms==""){
				 alert(message);
				 return -1;
			 }
		}
		for(var i=0;i<$scope.columns;i++){
			 if($scope.arraycolumns[i].terms==""){
				 alert(message);
				 return -1;
			 }
		}
		return 0;
	}
	
	$scope.fillMatrix=function(){
		if($scope.checkValues("Be sure all concepts have values")!=-1){
			$scope.url="http://90.147.102.57:9001/ivp/v2/fillMatrix/";
			var righe=JSON.stringify({"rows":$scope.arraylines,"columns":$scope.arraycolumns});
			var temp;
			$scope.url=$scope.url+$scope.collection+"/1000/10";
			$http.post($scope.url, righe,{
				headers : {
					'Content-Type' : 'text/plain'
				}
			}).then(function successCallback(response1) {
		    	console.log('post',response1);
		    	temp=JSON.parse(JSON.stringify(response1.data));
			    var k=0;
		    	for(var i=0;i<$scope.lines;i++){
					$scope.matrixcells[i]=[];
					for(var j=0;j<$scope.columns;j++){
						var restemp=[];
						restemp[k]=[];
						for(var z=0;z< temp[k].results.length;z++){
							var score=parseFloat(temp[k].results[z].score);
							score=score.toFixed(2);
							var keyword={};
							keyword={"keyword":temp[k].results[z].keyword,"score":score};
							restemp[k].push(keyword);
						}
						$scope.matrixcells[i][j]=restemp[k];
						k++;
					}
				}
		    	alert("Matrix filled in!")
		    }, function errorCallback(response1) {
		        console.log(response1);
		        alert("Error: matrix not filled in.")
		    });
		}
	}
	 function currentDate(){
	     var d=new Date();
	     var anno=d.getFullYear();
	     var mese=d.getMonth()+1;
	     var giorno=d.getDate();
	     var data=giorno+"-"+mese+"-"+anno;
	     return data;
	 }  
	 
	 $scope.viewConcepts = function() {
	        var conceptsWindow=window.open('conceptsList.html');
	        conceptsWindow.collection=$scope.collection;
	    }
	 
	 $scope.search = function(term) {
	        $scope.results = [];
	        $scope.noRes = false;
	        console.log("search");
	        var newWindow=window.open('search.html');
	        newWindow.query=term;
	        newWindow.collection=$scope.collection;
	    }
	 
	/* $scope.assignValues = function(modal,index){
		 var select = document.getElementById(modal+"Selectpicker"+index);
		 var terms=[];
		 if(modal=="first"){
			 terms=$scope.arraycolumns[index].terms;
		 }else{
			 terms=$scope.arraylines[index].terms;
		 }
		 for(i in $scope.phrases5.data) {
			   select.options[select.options.length] = new Option($scope.phrases5.data[i], $scope.phrases5.data[i]);
			   select.options[i].selected=(terms.includes(select.options[i].value));
		 } 
	 }*/
	 
	 $scope.conceptsFromText1=function(text,index,tohide,toshow){
		 $scope.url="http://90.147.102.57:9001/ivp/v2/conceptsFromText/";
		 $scope.url=$scope.url+$scope.collection;
		 var reqObj={"text":text,"filterStopWords":"true","filterNoLetterString":"true"};
			$http.post($scope.url, reqObj,{
				headers : {
					'Content-Type' : 'text/plain'
				}
			}).then(function successCallback(response1) {
				console.log('post',response1);
				$scope.phrases1[index]=response1.data;
				document.getElementById(tohide+index).style.display="none";
				document.getElementById(toshow+index).style.display="inline";
			}, function errorCallback(response1) {
		        console.log(response1);
		    });
			
	 }
	 $scope.conceptsFromText2=function(text,index,tohide,toshow){
		 $scope.url="http://90.147.102.57:9001/ivp/v2/conceptsFromText/";
		 $scope.url=$scope.url+$scope.collection;
		 var reqObj={"text":text,"filterStopWords":"true","filterNoLetterString":"true"};
			$http.post($scope.url, reqObj,{
				headers : {
					'Content-Type' : 'text/plain'
				}
			}).then(function successCallback(response1) {
				console.log('post',response1);
				$scope.phrases2[index]=response1.data;
				document.getElementById(tohide+index).style.display="none";
				document.getElementById(toshow+index).style.display="inline";
			}, function errorCallback(response1) {
		        console.log(response1);
		    });
			
	 }
	 
	 $scope.setSecondsTerms=function(index){
		 	var nameCheckboxes='phrases2'+index+'[]';
		 	var array=[];
		 	$("input:checkbox[name='" + nameCheckboxes + "']:checked").each(function(){
		 		array.push($(this).val());
		 	});
		 	$scope.arraylines[index].terms=array;
	 }
	 
	 $scope.setFirstTerms=function(index){
		 	var nameCheckboxes='phrases1'+index+'[]';
		 	var array=[];
		 	$("input:checkbox[name='" + nameCheckboxes + "']:checked").each(function(){
		 		array.push($(this).val());
		 	});
		 	$scope.arraycolumns[index].terms=array;
	 }
	 
	 $scope.loadFirstCheckbox=function(index){
		 var nameCheckboxes='phrases1'+index+'[]';
		 $("input:checkbox[name='" + nameCheckboxes + "']").each(function() {
			 	$(this).prop("checked", false);
		 		if($scope.arraycolumns[index].terms.includes($(this).val())) {
		 			$(this).prop("checked", true);
		 		}
		 	});
	 }
	 $scope.loadSecondCheckbox=function(index){
		 var nameCheckboxes='phrases2'+index+'[]';
		 $("input:checkbox[name='" + nameCheckboxes + "']").each(function(){
			 	$(this).prop("checked", false);
		 		if($scope.arraylines[index].terms.includes($(this).val())) {
		 			$(this).prop("checked", true);
		 		}
		 	});
	 }
	 
	 $scope.resetCheckbox=function(index,toshow,tohide,toreset){
		 document.getElementById(tohide+index).style.display="none";
		 document.getElementById(toshow+index).style.display="inline";
		 document.getElementById(toreset+index).value ="";
		 if(tohide=="tokensSecond"){
			 var nameCheckboxes='phrases2'+index+'[]';
			 $("input:checkbox[name='" + nameCheckboxes + "']:checked").each(function(){
			 		$(this).prop("checked",false);
			 	});
			 $scope.arraylines[index].terms=[""];
		 }else if (tohide=="tokensFirst"){
			 var nameCheckboxes='phrases1'+index+'[]';
			 $("input:checkbox[name='" + nameCheckboxes + "']:checked").each(function(){
			 		$(this).prop("checked",false);
			 	});
			 $scope.arraycolumns[index].terms=[""];
		 }
	 }
	 
	 $(document).on('hide.bs.modal', function () {
		 $('[id^=firstSelectpicker]')
		    .find('option')
		    .remove()
		    .end()
		;
		 $('[id^=secondSelectpicker]')
		    .find('option')
		    .remove()
		    .end()
		;
	 });
	
});
