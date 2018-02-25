app.controller('DownloadCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.projects = [
      {id:'1',platform:'运维中',code:'John',name:'2016-5-12',starttime:'@jh',endtime:'a@company.com',status:'上海',watch:false},
      {id:'2',platform:'运维中',code:'Bill',name:'2016-5-9',starttime:'@bg',endtime:'bg@company.com',status:'北京',watch:true},
      {id:'3',platform:'暂停',code:'Bobo',name:'2015-5-12',starttime:'@hz',endtime:'c@company.com',status:'武汉',watch:false},
      {id:'4',platform:'暂停',code:'Bobo',name:'2015-5-12',starttime:'@hz',endtime:'c@company.com',status:'武汉',watch:false},
      {id:'5',platform:'暂停',code:'Bobo',name:'2015-5-12',starttime:'@hz',endtime:'c@company.com',status:'武汉',watch:false},
      {id:'6',platform:'实施中',code:'Lily',name:'2013-5-12',starttime:'@li',endtime:'dd@company.com',status:'上海',watch:false}
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
  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+0+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.projects = resp.result;
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
    $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
	  if($scope.watch1 && !$scope.watch2){
		  $scope.watch=1;
	  }else if (!$scope.watch1 && $scope.watch2){
		  $scope.watch=0;
	  }
	  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.projects = resp.result;
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
    $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
	  if($scope.watch1 && !$scope.watch2){
		  $scope.watch=1;
	  }else if (!$scope.watch1 && $scope.watch2){
		  $scope.watch=0;
	  }
	  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.projects = resp.result;
	  });

  };
  
  $scope.save=function(){
	  $http.post("auto/project/add.do?platform="+ $scope.platform.id +"&code="+$scope.systemcode+"&name="+$scope.systemname).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  alert(message);
		  $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
		  if($scope.watch1 && !$scope.watch2){
			  $scope.watch=1;
		  }else if (!$scope.watch1 && $scope.watch2){
			  $scope.watch=0;
		  }
		  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.projects = resp.result;
		  });
	  });
  };
  
  $scope.play=function(index){
	  $http.post("auto/project/updateproject.do?id="+ $scope.projects[index].id+"&watch="+1).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
		  if($scope.watch1 && !$scope.watch2){
			  $scope.watch=1;
		  }else if (!$scope.watch1 && $scope.watch2){
			  $scope.watch=0;
		  }
		  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.projects = resp.result;
		  });
	  });
  };
  
  $scope.pause=function(index){
	  $http.post("auto/project/updateproject.do?id="+ $scope.projects[index].id+"&watch="+0).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
		  if($scope.watch1 && !$scope.watch2){
			  $scope.watch=1;
		  }else if (!$scope.watch1 && $scope.watch2){
			  $scope.watch=0;
		  }
		  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.projects = resp.result;
		  });
	  });
  };

  $scope.operate=function(index){
	  $scope.watch="";//空全部查询，1查询监控项目，0查询未监控项目
	  console.log($scope.watch1);
	  console.log($scope.watch2);
	  if($scope.watch1 && !$scope.watch2){
		  $scope.watch=1;
	  }else if (!$scope.watch1 && $scope.watch2){
		  $scope.watch=0;
	  }
	  $http.post("auto/project/query.do?platform="+ $scope.platformname.id+"&name="+$scope.projectname+"&code="+$scope.projectcode+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&pageSize="+$scope.pageSize+"&watch="+$scope.watch).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.projects = resp.result;
	  });
  };
  
}])