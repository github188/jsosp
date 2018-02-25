app.controller('DiscountCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
 $scope.returns = [
                   {
                     name:'异常',
                     code: '0'
                   },
                   {
                     name:'正常',
                     code: '1'
                   },
                   {
                   	name:'全部',
                   	code: '2'
                   }
               ];
  $scope.activebase='active';
  $scope.active1=$scope.activebase;
  $scope.active2=!$scope.activebase;
  $scope.active3=!$scope.activebase;
  $scope.loglog = function(item){
    console.log(item);
  }
  $scope.carno="";
  $scope.dt=new Date();
  $scope.dt.setDate(new Date().getDate()-7);
  $scope.openCalendar = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
  };
  $scope.dt1=new Date();
  $scope.openCalendar1 = function ($event) {
	  $event.preventDefault();
	  $event.stopPropagation();
	  $scope.opened1 = true;
  };
  $scope.users=[];
  $scope.lastactive=0;//保存上一个被选中的按钮
  $scope.active = function(index){
    $scope.items[$scope.lastactive].style='disable';
    $scope.items[index].style='active';
    $scope.lastactive=index;
  };
  $scope.discounts = [
      {id:'1',carno:'运维中',request_url:'John',oper_time:'2016-5-12',allocate_item:'@jh',allocate_value:'a@company.com',check_status:'上海'},
      {id:'2',carno:'运维中',request_url:'Bill',oper_time:'2016-5-9',allocate_item:'@bg',allocate_value:'bg@company.com',check_status:'北京'},
      {id:'3',carno:'暂停',request_url:'Bobo',oper_time:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
      {id:'4',carno:'暂停',request_url:'Bobo',oper_time:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
      {id:'5',carno:'暂停',request_url:'Bobo',oper_time:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
      {id:'6',carno:'实施中',request_url:'Lily',oper_time:'2013-5-12',allocate_item:'@li',allocate_value:'dd@company.com',check_status:'上海'}
    ];
  $scope.logs = [
              {id:'1',info:'运维中',log:'John',serviceid:'2016-5-12',allocate_item:'@jh',allocate_value:'a@company.com',check_status:'上海'},
              {id:'2',info:'运维中',log:'Bill',serviceid:'2016-5-9',allocate_item:'@bg',allocate_value:'bg@company.com',check_status:'北京'},
              {id:'3',info:'暂停',log:'Bobo',serviceid:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
              {id:'4',info:'暂停',log:'Bobo',serviceid:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
              {id:'5',info:'暂停',log:'Bobo',serviceid:'2015-5-12',allocate_item:'@hz',allocate_value:'c@company.com',check_status:'武汉'},
              {id:'6',info:'实施中',log:'Lily',serviceid:'2013-5-12',allocate_item:'@li',allocate_value:'dd@company.com',check_status:'上海'}
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
  $scope.starttime=$filter('date')($scope.dt,'yyyy-MM-dd');
  $scope.endtime=$filter('date')($scope.dt1,'yyyy-MM-dd');
  $http.post("auto/discount/list.do?pageSize="+$scope.pageSize+"&pageIndex=0"+"&starttime="+$scope.starttime+"&endtime="+$scope.endtime+"&carno="+$scope.carno+"&retCode="+$scope.returns[0].code).success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.discounts = resp.result;
  });
  $scope.changepage = function(index){
	  $scope.result="查询中";
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
    
    $scope.starttime=$filter('date')($scope.dt,'yyyy-MM-dd');
    $scope.endtime=$filter('date')($scope.dt1,'yyyy-MM-dd');
    $http.post("auto/discount/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&starttime="+$scope.starttime+"&endtime="+$scope.endtime+"&carno="+$scope.carno+"&retCode="+$scope.ret.code).success(function(response) {
  	  var resp =angular.fromJson(angular.fromJson(response));
  	  console.log(resp);
  	  var returnCode = resp.returnCode;
  	  var message = resp.message;
  	  $scope.counts=resp.counts;
  	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
  	  if($scope.allpages<=5){
  		  $scope.rightpagestatus=$scope.status1;
  	  }
  	  $scope.discounts = resp.result;
  	  $scope.result="查询完毕";
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
	  $scope.result="查询中";
    $scope.currentpage=0;
    $scope.pages=[
                  {index:0,status:'active',color:'#fff'},
                  {index:1,status:'enable',color:'#fff'},
                  {index:2,status:'enable',color:'#fff'},
                  {index:3,status:'enable',color:'#fff'},
                  {index:4,status:'enable',color:'#fff'}
                ];
    $scope.starttime=$filter('date')($scope.dt,'yyyy-MM-dd');
    $scope.endtime=$filter('date')($scope.dt1,'yyyy-MM-dd');
    $http.post("auto/discount/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&starttime="+$scope.starttime+"&endtime="+$scope.endtime+"&carno="+$scope.carno+"&retCode="+$scope.ret.code).success(function(response) {
  	  var resp =angular.fromJson(angular.fromJson(response));
  	  console.log(resp);
  	  var returnCode = resp.returnCode;
  	  var message = resp.message;
  	  $scope.counts=resp.counts;
  	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
  	  if($scope.allpages<=5){
  		  $scope.rightpagestatus=$scope.status1;
  	  }
  	  $scope.discounts = resp.result;
  	  $scope.result="查询完毕";
    });

  };
  
  $scope.query=function(){
	  $scope.result="查询中";
	  $scope.starttime=$filter('date')($scope.dt,'yyyy-MM-dd');
	  $scope.endtime=$filter('date')($scope.dt1,'yyyy-MM-dd');
	    $http.post("auto/discount/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&starttime="+$scope.starttime+"&endtime="+$scope.endtime+"&carno="+$scope.carno+"&retCode="+$scope.ret.code).success(function(response) {
	  	  var resp =angular.fromJson(angular.fromJson(response));
	  	  console.log(resp);
	  	  var returnCode = resp.returnCode;
	  	  var message = resp.message;
	  	  $scope.counts=resp.counts;
	  	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  	  if($scope.allpages<=5){
	  		  $scope.rightpagestatus=$scope.status1;
	  	  }
	  	  $scope.discounts = resp.result;
	  	  $scope.result="查询完毕";
	    });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.discounts.length;i++){
	  		if(i==index){
	  			$scope.discounts[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.discounts[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.discounts.length;i++){
	  		$scope.discounts[i].style="";
	  	}
	  }
	  
	  $scope.message=function(index){
		  $http.post("auto/discount/queryById.do?id="+ $scope.discounts[index].seqid).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.logs = resp.result;
			  $scope.resultMsg = resp.resultMsg;
		  });
	  };
	  
}])