app.controller('RDSManageCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	$scope.isDeleted=false;
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
  $scope.rdss = [];
  $scope.counts=0;
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
  $http.post("auto/rdsmanage/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.allpages=1;
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.rdss = resp.result;
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

    $http.post("auto/rdsmanage/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=1;
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.rdss = resp.result;
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

    $http.post("auto/rdsmanage/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=1;
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.rdss = resp.result;
	  });
  };
  
  $scope.exportrds=function(){
		$http.post("auto/rdsmanage/exportrds.do").success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
			if(resp.url!=""){
				window.location.href = ""+resp.url;
			}
//			window.open("111.xls"); 
		});
	};
  
}])