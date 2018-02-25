app.controller('ProjectCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
//  $scope.projects = [];
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
    $scope.pages[$scope.currentpage].color='#fff';
    $scope.pages[index].color='#000';
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
                  {index:0,status:'active',color:'#fff'},
                  {index:1,status:'enable',color:'#fff'},
                  {index:2,status:'enable',color:'#fff'},
                  {index:3,status:'enable',color:'#fff'},
                  {index:4,status:'enable',color:'#fff'}
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
  $scope.projectid="";
  $scope.message=function(index){
	  $scope.projectid =$scope.projects[index].id;
	  $http.post("auto/project/queryById.do?id="+ $scope.projects[index].id).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.infoproject=resp.result[0];
		  $scope.infotels=$scope.infoproject.telphone;
		  $scope.infoprojectcode=$scope.infoproject.code;
		  $scope.infoprojectname=$scope.infoproject.name;
		  $scope.infoplatform=$scope.infoproject.platform_name;
	  });
  };

  $scope.updateInfo=function(){
	  $http.post("auto/project/updateById.do?id="+ $scope.projectid+"&telphones="+$scope.infotels).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  alert(message);
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
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.projects.length;i++){
	  		if(i==index){
	  			$scope.projects[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.projects[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.projects.length;i++){
	  		$scope.projects[i].style="";
	  	}
	  }
}])