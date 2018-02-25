app.controller('BgyinoutCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.bgyinouts = [
      {id:'1',check_time:'运维中',check_date:'John',in_records:'2016-5-12',out_records:'@jh',request_time:'a@company.com',msg_code:'上海'},
      {id:'2',check_time:'运维中',check_date:'Bill',in_records:'2016-5-9',out_records:'@bg',request_time:'bg@company.com',msg_code:'北京'},
      {id:'3',check_time:'暂停',check_date:'Bobo',in_records:'2015-5-12',out_records:'@hz',request_time:'c@company.com',msg_code:'武汉'},
      {id:'4',check_time:'暂停',check_date:'Bobo',in_records:'2015-5-12',out_records:'@hz',request_time:'c@company.com',msg_code:'武汉'},
      {id:'5',check_time:'暂停',check_date:'Bobo',in_records:'2015-5-12',out_records:'@hz',request_time:'c@company.com',msg_code:'武汉'},
      {id:'6',check_time:'实施中',check_date:'Lily',in_records:'2013-5-12',out_records:'@li',request_time:'dd@company.com',msg_code:'上海'}
    ];
  $scope.counts=0;
  $scope.pageSize=6;
  $scope.currentpage=0;
  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
  $scope.status1='disabled';
  $scope.status2='enable';
  $scope.status3='active';
  $scope.leftpagestatus=$scope.status1;
  $scope.rightpagestatus=!$scope.status1;
  $scope.pages=[
                {index:0,status:'active',color:'#fff'},
                {index:1,status:'enable',color:'#fff'},
                {index:2,status:'enable',color:'#fff'},
                {index:3,status:'enable',color:'#fff'},
                {index:4,status:'enable',color:'#fff'}
              ];
  $scope.account="";
  $scope.userName="";
  $scope.telephone="";
  $scope.platformname="";
  //$scope.pages[$scope.currentpage].index
  $http.post("auto/bgyinout/list.do?pageSize="+$scope.pageSize+"&pageIndex=0").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.bgyinouts = resp.result;
  });
  $scope.changepage = function(index){
    $scope.pages[$scope.currentpage].status=$scope.status2;
    $scope.pages[index].status=$scope.status3;
    $scope.pages[$scope.currentpage].color='#fff';
    $scope.pages[index].color='#000';
    $scope.currentpage=index;
    console.log(index);
    console.log($scope.pages[index]);
    if (index!=0) {$scope.leftpagestatus=!$scope.status1;};
    if ($scope.pages[index].index==0) {$scope.leftpagestatus=$scope.status1;};
    if (index!=$scope.allpages) {$scope.rightpagestatus=!$scope.status1;};
    if ($scope.pages[index].index==$scope.allpages) {$scope.rightpagestatus=$scope.status1;};
    var projectName="";
    if($scope.apiname!=undefined){
    	projectName=$scope.apiname.projectName;
    }
	  $http.post("auto/bgyinout/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.bgyinouts = resp.result;
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
                  {index:0,status:'active',color:'#fff'},
                  {index:1,status:'enable',color:'#fff'},
                  {index:2,status:'enable',color:'#fff'},
                  {index:3,status:'enable',color:'#fff'},
                  {index:4,status:'enable',color:'#fff'}
                ];
    var projectName="";
    if($scope.apiname!=undefined){
    	projectName=$scope.apiname.projectName;
    }
	  $http.post("auto/bgyinout/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.bgyinouts = resp.result;
	  });

  };
  
  $scope.query=function(){
	    var projectName="";
	    if($scope.apiname!=undefined){
	    	projectName=$scope.apiname.projectName;
	    }
	  $http.post("auto/apiData/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.bgyinouts = resp.result;
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.bgyinouts.length;i++){
	  		if(i==index){
	  			$scope.bgyinouts[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.bgyinouts[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.bgyinouts.length;i++){
	  		$scope.bgyinouts[i].style="";
	  	}
	  }
}])