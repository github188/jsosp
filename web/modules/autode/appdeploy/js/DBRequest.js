app.controller('DBRequestCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	  $http.post("auto/rdsmanage/list.do?pageIndex="+ 0 +"&pageSize=0").success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.rdss=[];
		  $scope.rdsss = resp.result;
		  for(var i=0;i<$scope.rdsss.length;i++){
			  if($scope.rdsss[i].clustername=='主实例'){
				  $scope.rdss.push($scope.rdsss[i]);
			  }
		  }
	  });
	$scope.isDeleted=false;
	$scope.dbs = [
	              {person:'asd',instanceName:'12',dbName:'testdb',requestDate:'2017-01-01',endDate:'2018-01-01'},
	              {person:'12',instanceName:'12',dbName:'testdb',requestDate:'2017-01-01',endDate:'2018-01-01'},
	              {person:'1',instanceName:'12',dbName:'testdb',requestDate:'2017-01-01',endDate:'2012-01-01'}
	        ];
  $scope.activebase='active';
  $scope.active1=$scope.activebase;
  $scope.active2=!$scope.activebase;
  $scope.active3=!$scope.activebase;
  $scope.loglog = function(item){
    console.log(item);
  }
  $scope.users=[];
  $scope.lastactive=0;//保存上一个被选中的按钮
  $scope.active = function(index){
    $scope.items[$scope.lastactive].style='disable';
    $scope.items[index].style='active';
    $scope.lastactive=index;
  };
  $scope.pageSize=6;
  $scope.currentpage=0;
  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
  $scope.status1='disabled';
  $scope.status2='enable';
  $scope.status3='active';
  $scope.leftpagestatus=$scope.status1;
  $scope.rightpagestatus=!$scope.status1;
  $scope.pages=[
      {index:0,status:'active'},
      {index:1,status:'enable'},
      {index:2,status:'enable'},
      {index:3,status:'enable'},
      {index:4,status:'enable'}
    ];
  $scope.account="";
  $scope.userName="";
  $scope.telephone="";
  $http.post("auto/dbrequest/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.dbcount1=resp.DBCreatingCounts;
			  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.dbs = resp.result;
		  });
  $scope.changepage = function(index){
    $scope.pages[$scope.currentpage].status=$scope.status2;
    $scope.pages[index].status=$scope.status3;
    $scope.currentpage=index;
    console.log(index);
    console.log($scope.pages[index]);
    if (index!=0) {$scope.leftpagestatus=!$scope.status1;};
    if ($scope.pages[index].index==0) {$scope.leftpagestatus=$scope.status1;};
    if (index!=$scope.allpages) {$scope.rightpagestatus=!$scope.status1;};
    if ($scope.pages[index].index==$scope.allpages) {$scope.rightpagestatus=$scope.status1;};

    $http.post("auto/dbrequest/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.dbcount1=resp.DBCreatingCounts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.dbs = resp.result;
	  });
  };
  $scope.rightchangepage= function(){
    if ($scope.pages[4].index!=$scope.allpages-1) {
      for (var i = $scope.pages.length - 1; i >= 0; i--) {
        $scope.pages[i].index++;
      };
    };
  };
  $scope.leftchangepage= function(){
    if ($scope.pages[0].index!=0) {
      for (var i = $scope.pages.length - 1; i >= 0; i--) {
        $scope.pages[i].index--;
      };
    };
  };
  $scope.first=function(){
    $scope.currentpage=0;
    $scope.pages=[
      {index:0,status:'active'},
      {index:1,status:'enable'},
      {index:2,status:'enable'},
      {index:3,status:'enable'},
      {index:4,status:'enable'}
    ];

    $http.post("auto/dbrequest/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.dbcount1=resp.DBCreatingCounts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.dbs = resp.result;
	  });

  };
  $scope.requestDate=new Date();
  $scope.openCalendar = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
  };
  $scope.endDate=new Date();
  $scope.openCalendar1 = function ($event) {
	  $event.preventDefault();
	  $event.stopPropagation();
	  $scope.opened1 = true;
  };
  $scope.requestDate=new Date();
  $scope.openCalendar = function ($event) {
	  $event.preventDefault();
	  $event.stopPropagation();
	  $scope.opened = true;
  };
  $scope.save=function(index){
	  var requestDate=$filter('date')($scope.requestDate,'yyyy-MM-dd');
	  var endDate=$filter('date')($scope.endDate,'yyyy-MM-dd');
	  $http.post("auto/dbrequest/add.do?requestPerson="+ $scope.requestPerson +"&dbName="+$scope.dbName+
			  "&instanceName="+$scope.rds.ip+"&requestDate="+requestDate+"&endDate="+endDate+"&purpose="+$scope.purpose).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  alert(message);
		  $http.post("auto/dbrequest/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.dbcount1=resp.DBCreatingCounts;
			  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.dbs = resp.result;
		  });
	  });
  };
  
  $scope.play=function(index){
	  $http.post("auto/dbrequest/updateById.do?id="+ $scope.dbs[index].id+"&IsCreated=1").success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  if(returnCode==0){
			    $http.post("auto/dbrequest/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
					  var resp =angular.fromJson(angular.fromJson(response));
					  console.log(resp);
					  var returnCode = resp.returnCode;
					  var message = resp.message;
					  $scope.counts=resp.counts;
					  $scope.dbcount1=resp.DBCreatingCounts;
					  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
					  if($scope.allpages<=5){
						  $scope.rightpagestatus=$scope.status1;
					  }
					  $scope.dbs = resp.result;
				  });
		  }
	  });
  };
  $scope.pause=function(index){
	  $http.post("auto/dbrequest/updateById.do?id="+ $scope.dbs[index].id+"&IsCreated=0").success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  if(returnCode==0){
			    $http.post("auto/dbrequest/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
					  var resp =angular.fromJson(angular.fromJson(response));
					  console.log(resp);
					  var returnCode = resp.returnCode;
					  var message = resp.message;
					  $scope.counts=resp.counts;
					  $scope.dbcount1=resp.DBCreatingCounts;
					  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
					  if($scope.allpages<=5){
						  $scope.rightpagestatus=$scope.status1;
					  }
					  $scope.dbs = resp.result;
				  });
		  }
	  });
  };
  
}])