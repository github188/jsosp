app.controller('HttpCheckOneKeyCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.Https = [
      {id:'1',platform_name:'运维中',request_url:'John',oper_time:'2016-5-12',allocate_item:'@jh',allocate_value:'a@company.com',check_status:'上海'},
      {id:'2',platform_name:'运维中',request_url:'Bill',oper_time:'2016-5-9',allocate_item:'@bg',allocate_value:'bg@company.com',check_status:'北京'},
      {id:'3',platform_name:'暂停',request_url:'Bobo',oper_time:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
      {id:'4',platform_name:'暂停',request_url:'Bobo',oper_time:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
      {id:'5',platform_name:'暂停',request_url:'Bobo',oper_time:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
      {id:'6',platform_name:'实施中',request_url:'Lily',oper_time:'2013-5-12',allocate_item:'@li',allocate_value:'dd@company.com',check_status:'上海'}
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
  $http.post("auto/httpCheckOnkey/list.do?pageSize="+$scope.pageSize+"&pageIndex=0").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.Https = resp.result;
	  $scope.Httpresult = resp.Httpresult;
	  $scope.Httpresult.splice(0, 0, {platform_name : ''});
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
    var httpService="";
    if($scope.httpname!=undefined){
    	httpService=$scope.httpname.platform_name;
    }
	  $http.post("auto/httpCheckOnkey/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&httpService="+httpService).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.Https = resp.result;
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
    var httpService="";
    if($scope.httpname!=undefined){
    	httpService=$scope.httpname.platform_name;
    }
	  $http.post("auto/httpCheckOnkey/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&httpService="+httpService).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.Https = resp.result;
	  });

  };
  
  $scope.query=function(){
	    var httpService="";
	    if($scope.httpname!=undefined){
	    	httpService=$scope.httpname.platform_name;
	    }
	  $http.post("auto/httpCheckOnkey/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&httpService="+httpService).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.Https = resp.result;
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.Https.length;i++){
	  		if(i==index){
	  			$scope.Https[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.Https[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.Https.length;i++){
	  		$scope.Https[i].style="";
	  	}
	  }
}])