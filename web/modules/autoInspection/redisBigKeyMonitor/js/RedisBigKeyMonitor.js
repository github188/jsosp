app.controller('RedisBigKeyMonitorCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.rediss = [
      {id:'1',node:'运维中',redis_host:'John',check_time:'2016-5-12',key_name:'@jh',key_type:'a@company.com',key_value:'上海'},
      {id:'2',node:'运维中',redis_host:'Bill',check_time:'2016-5-9',key_name:'@bg',key_type:'bg@company.com',key_value:'北京'},
      {id:'3',node:'暂停',redis_host:'Bobo',check_time:'2015-5-12',key_name:'@hz',key_type:'c@company.com',key_value:'武汉'},
      {id:'4',node:'暂停',redis_host:'Bobo',check_time:'2015-5-12',key_name:'@hz',key_type:'c@company.com',key_value:'武汉'},
      {id:'5',node:'暂停',redis_host:'Bobo',check_time:'2015-5-12',key_name:'@hz',key_type:'c@company.com',key_value:'武汉'},
      {id:'6',node:'实施中',redis_host:'Lily',check_time:'2013-5-12',key_name:'@li',key_type:'dd@company.com',key_value:'上海'}
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
  $http.post("auto/redisBigTableMonitor/list.do?pageSize="+$scope.pageSize+"&pageIndex=0").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.rediss = resp.result;
	  $scope.Redisresult = resp.Redisresult;
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
    var redisHosts="";
    if($scope.redname!=undefined){
    	redisHosts=$scope.redname.redis_host;
    }
	  $http.post("auto/redisBigTableMonitor/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&redisHosts="+redisHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.rediss = resp.result;
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
    var redisHosts="";
    if($scope.redname!=undefined){
    	redisHosts=$scope.redname.redis_host;
    }
	  $http.post("auto/redisBigTableMonitor/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&redisHosts="+redisHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.rediss = resp.result;
	  });

  };
  
  $scope.query=function(){
	    var redisHosts="";
	    if($scope.redname!=undefined){
	    	redisHosts=$scope.redname.redis_host;
	    }
	  $http.post("auto/redisBigTableMonitor/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&redisHosts="+redisHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.rediss = resp.result;
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.rediss.length;i++){
	  		if(i==index){
	  			$scope.rediss[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.rediss[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.rediss.length;i++){
	  		$scope.rediss[i].style="";
	  	}
	  }
}])