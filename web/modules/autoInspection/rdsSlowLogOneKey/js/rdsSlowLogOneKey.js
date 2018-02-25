app.controller('RdsSlowLogOneKeyCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.RDSresult = [
      {id:'1',db_instance_id:'运维中',execute_start_time:'John',sql_param:'2016-5-12',client_ip:'@jh',db_nanme:'a@company.com',query_times:'上海',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'2',db_instance_id:'运维中',execute_start_time:'Bill',sql_param:'2016-5-9',client_ip:'@bg',db_nanme:'bg@company.com',query_times:'北京',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'3',db_instance_id:'暂停',execute_start_time:'Bobo',sql_param:'2015-5-12',client_ip:'@hz',db_nanme:'c@company.com',query_times:'武汉',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'4',db_instance_id:'暂停',execute_start_time:'Bobo',sql_param:'2015-5-12',client_ip:'@hz',db_nanme:'c@company.com',query_times:'武汉',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'5',db_instance_id:'暂停',execute_start_time:'Bobo',sql_param:'2015-5-12',client_ip:'@hz',db_nanme:'c@company.com',query_times:'武汉',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'6',db_instance_id:'实施中',execute_start_time:'Lily',sql_param:'2013-5-12',client_ip:'@li',db_nanme:'dd@company.com',query_times:'上海',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'}
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
  $http.post("auto/rdsSlowLogOnekey/list.do?pageSize="+$scope.pageSize+"&pageIndex=0").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.RDSs = resp.result;
	  $scope.RDSresult = resp.RDSresult;
	  $scope.RDSresult.splice(0, 0, {db_instance_id : ''});
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
    var rdsHosts="";
    if($scope.rdsname!=undefined){
    	rdsHosts=$scope.rdsname.db_instance_id;
    }
	  $http.post("auto/rdsSlowLogOnekey/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&rdsHosts="+rdsHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.RDSs = resp.result;
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
    var rdsHosts="";
    if($scope.rdsname!=undefined){
    	rdsHosts=$scope.rdsname.db_instance_id;
    }
	  $http.post("auto/rdsSlowLogOnekey/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&rdsHosts="+rdsHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.RDSs = resp.result;
	  });

  };
  
  $scope.query=function(){
	    var rdsHosts="";
	    if($scope.rdsname!=undefined){
	    	rdsHosts=$scope.rdsname.db_instance_id;
	    }
	  $http.post("auto/rdsSlowLogOnekey/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&rdsHosts="+rdsHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.RDSs = resp.result;
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.RDSs.length;i++){
	  		if(i==index){
	  			$scope.RDSs[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.RDSs[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.RDSs.length;i++){
	  		$scope.RDSs[i].style="";
	  	}
	  }
}])