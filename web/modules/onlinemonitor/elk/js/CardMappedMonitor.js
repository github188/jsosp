
app.controller('CardMappedMonitorCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	$scope.platformid = "";
	$scope.dt=new Date();
	  $scope.openCalendar = function ($event) {
	      $event.preventDefault();
	      $event.stopPropagation();
	      $scope.opened = true;
	  };
	  $http.post("report/onlinereport/platformlist.do").success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.platforms = resp.result;
		  $scope.platforms.splice(0, 0, {id : '',name : ''});
//		  $scope.platforms.push({id : '',name : ''});
	  });
	  $scope.personname="";
	  $scope.company="";
	  $scope.department="";
	  $scope.telephone="";
	  
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
	  $scope.elkCardmapped = [
	      {id:'1',platform:'运维中',subsystemname:'John',subsystemcode:'2016-5-12',createtime:"12"},
	      {id:'2',platform:'运维中',subsystemname:'Bill',subsystemcode:'2016-5-9',createtime:"12"},
	      {id:'3',platform:'暂停',subsystemname:'Bobo',subsystemcode:'2015-5-12',createtime:"12"},
	      {id:'4',platform:'暂停',subsystemname:'Bobo',subsystemcode:'2015-5-12',createtime:"12"},
	      {id:'5',platform:'暂停',subsystemname:'Bobo',subsystemcode:'2015-5-12',createtime:"12"},
	      {id:'6',platform:'暂停',subsystemname:'Bobo',subsystemcode:'2015-5-12',createtime:"12"}
	    ];
	  $scope.edit = function(index){
			console.log(index.onlinePerson);
			$scope.personname=index.onlinePerson.personname;
			$scope.company=index.onlinePerson.company;
			$scope.department=index.onlinePerson.department;
			$scope.telephone=index.onlinePerson.telephone;
	  };
	  $scope.menus=['项目名称','接入日期','所属系统'];
	  $scope.sortBy = function(menu){
	    console.log(menu);
	  };
	  $scope.counts=100;
	  $scope.pageSize=6;
	  $scope.currentpage=0;
	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
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
	  $http.post("elk/cardMapped.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize
			  +"&stat_date="+$filter('date')($scope.dt,'yyyy-MM-dd')).success(function(response) {
				  var resp =angular.fromJson(angular.fromJson(response));
				  console.log(resp);
				  var returnCode = resp.returnCode;
				  var message = resp.message;
				  $scope.counts=resp.counts;
				  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
				  if($scope.allpages<=5){
					  $scope.rightpagestatus=$scope.status1;
				  }
				  $scope.elkCardmapped = resp.result;
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

	  $http.post("elk/cardMapped.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platform="
			  +$scope.platformid.name+"&stat_date="+$filter('date')($scope.dt,'yyyy-MM-dd')).success(function(response) {
				  var resp =angular.fromJson(angular.fromJson(response));
				  console.log(resp);
				  var returnCode = resp.returnCode;
				  var message = resp.message;
				  $scope.counts=resp.counts;
				  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
				  if($scope.allpages<=5){
					  $scope.rightpagestatus=$scope.status1;
				  }
				  $scope.elkCardmapped = resp.result;
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
		  $http.post("elk/cardMapped.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platform="
				  +$scope.platformid.name+"&stat_date="+$filter('date')($scope.dt,'yyyy-MM-dd')).success(function(response) {
					  var resp =angular.fromJson(angular.fromJson(response));
					  console.log(resp);
					  var returnCode = resp.returnCode;
					  var message = resp.message;
					  $scope.counts=resp.counts;
					  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
					  if($scope.allpages<=5){
						  $scope.rightpagestatus=$scope.status1;
					  }
					  $scope.elkCardmapped = resp.result;
				  });
	  };
	  $scope.query= function(){
		  $http.post("elk/cardMapped.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platform="
				  +$scope.platformid.name+"&stat_date="+$filter('date')($scope.dt,'yyyy-MM-dd')).success(function(response) {
					  var resp =angular.fromJson(angular.fromJson(response));
					  console.log(resp);
					  var returnCode = resp.returnCode;
					  var message = resp.message;
					  $scope.counts=resp.counts;
					  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
					  if($scope.allpages<=5){
						  $scope.rightpagestatus=$scope.status1;
					  }
					  $scope.elkCardmapped = resp.result;
				  });
	  }
	  $scope.tableblur = function (index){
		  	console.log(index);
		  	for(var i=0;i<$scope.elkCardmapped.length;i++){
		  		if(i==index){
		  			$scope.elkCardmapped[i].style="background-color:#0c67c4;";
		  		}else{
		  			$scope.elkCardmapped[i].style="";
		  		}
		  	}
		  }
		  $scope.tableleave = function (){
		  	for(var i=0;i<$scope.elkCardmapped.length;i++){
		  		$scope.elkCardmapped[i].style="";
		  	}
		  }
}])
