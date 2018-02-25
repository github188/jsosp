app.controller('InspectionCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	  $http.post("report/onlinereport/platformlist.do").success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.platforms = resp.result;
		  $scope.platforms.splice(0, 0, {id : '',name : ''});
//		  $scope.platforms.push({id : '',name : ''});
	  });
	  $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
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
  $scope.watch1=true;
  $scope.inspections = [
      {id:'1',name:'Redis大键监控',result:'正常',check:true,starttime:'@jh',endtime:'a@company.com',status:'上海',watch:false},
      {id:'2',name:'RDS慢sql',result:'正常',check:true,starttime:'@bg',endtime:'bg@company.com',status:'北京',watch:true},
      {id:'3',name:'Redis资源检测',result:'正常',check:true,starttime:'@hz',endtime:'c@company.com',status:'武汉',watch:false},
      {id:'4',name:'HTTP服务检测',result:'正常',check:true,starttime:'@hz',endtime:'c@company.com',status:'武汉',watch:false},
      {id:'5',name:'API数据',result:'正常',check:true,starttime:'@hz',endtime:'c@company.com',status:'武汉',watch:false},
      {id:'6',name:'碧桂园出入场数据',result:'正常',check:false,starttime:'@li',endtime:'dd@company.com',status:'上海',watch:false}
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
      {index:0,status:'active'},
      {index:1,status:'enable'},
      {index:2,status:'enable'},
      {index:3,status:'enable'},
      {index:4,status:'enable'}
    ];
  $scope.account="";
  $scope.userName="";
  $scope.telephone="";
  $scope.platformname="";
  
  $scope.bigkey=true;//Redis大键监控
  $scope.slowlog=true;//RDS慢sql
  $scope.init=true;//Redis资源检测
  $scope.readfile=true;//HTTP服务检测
  $scope.api=true;//API数据
  $scope.bgy=true;//碧桂园出入场数据
  $scope.save=function(){
	  $scope.result="正在巡检";
	  $scope.bigkeyResult='';
	  $scope.slowlogResult='';
	  $scope.initResult='';
	  $scope.readfileResult='';
	  $scope.apiResult='';
	  $scope.bgyResult='';
	  console.log($scope.bigkey);
	  console.log($scope.slowlog);
	  console.log($scope.init);
	  console.log($scope.readfile);
	  console.log($scope.api);
	  console.log($scope.bgy);
	  var bigkeys="";
	  var slowlogs="";
	  var inits="";
	  var readfiles="";
	  var apis="";
	  var bgys="";
	  if($scope.bigkey){
		  bigkeys="bigkey";
	  }
	  if($scope.slowlog){
		  slowlogs="slowlog";
	  }
	  if($scope.init){
		  inits="init";
	  }
	  if($scope.readfile){
		  readfiles="readfile";
	  }
	  if($scope.api){
		  apis="api";
	  }
	  if($scope.bgy){
		  bgys="bgy";
	  }
//	  $http.post("inspection/doInspection.do?init="+inits+"&bigkey="+bigkeys+"&readfile="+readfiles+"&slowlog="+slowlogs
//			  +"&api="+apis+"&bgy="+bgys).success(function(response) {
//		  var resp =angular.fromJson(response);
//		  if(resp=='ERROR'){
//			  alert('一键巡检异常');
//		  }
//	  });
	  $http.post("inspection/doinsp.do").success(function(response) {
		  var resp =angular.fromJson(response);
		  $scope.result="";
		  if(resp=='1'){
			  $scope.bigkeyResult='异常';
			  $scope.slowlogResult='异常';
			  $scope.initResult='异常';
			  $scope.readfileResult='异常';
			  $scope.apiResult='异常';
			  $scope.bgyResult='异常';
		  }else{
			  $scope.bigkeyResult='正常';
			  $scope.slowlogResult='正常';
			  $scope.initResult='正常';
			  $scope.readfileResult='正常';
			  $scope.apiResult='正常';
			  $scope.bgyResult='正常';
		  }
	  });
};
  
}])