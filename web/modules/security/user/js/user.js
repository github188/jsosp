
app.controller('UsersListCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.projectList = [
      {id:'1',status:'运维中',name:'John',date:'2016-5-12',customer:'@jh',subsystem:'a@company.com',subcompany:'上海'},
      {id:'2',status:'运维中',name:'Bill',date:'2016-5-9',customer:'@bg',subsystem:'bg@company.com',subcompany:'北京'},
      {id:'3',status:'暂停',name:'Bobo',date:'2015-5-12',customer:'@hz',subsystem:'c@company.com',subcompany:'武汉'},
      {id:'4',status:'暂停',name:'Bobo',date:'2015-5-12',customer:'@hz',subsystem:'c@company.com',subcompany:'武汉'},
      {id:'5',status:'暂停',name:'Bobo',date:'2015-5-12',customer:'@hz',subsystem:'c@company.com',subcompany:'武汉'},
      {id:'6',status:'实施中',name:'Lily',date:'2013-5-12',customer:'@li',subsystem:'dd@company.com',subcompany:'上海'}
    ];
  $scope.edit = function(index){
    $state.go('ProjectInfo',{id: index.project.id});
  };
  $scope.menus=['项目名称','接入日期','所属系统'];
  $scope.sortBy = function(menu){
    console.log(menu);
  };
  $scope.userscount=100;
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
  $http.post("security/user/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize+"&account="
		  +$scope.account+"&userName="+$scope.userName+"&telephone="+$scope.telephone).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.userscount=resp.userCounts;
			  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.users = resp.result;
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
    $http.post("security/user/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&account="
  		  +$scope.account+"&userName="+$scope.userName+"&telephone="+$scope.telephone).success(function(response) {
  			  var resp =angular.fromJson(angular.fromJson(response));
  			  console.log(resp);
  			  var returnCode = resp.returnCode;
  			  var message = resp.message;
  			  $scope.userscount=resp.userCounts;
  			  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
  			  if($scope.allpages<=5){
  				  $scope.rightpagestatus=$scope.status1;
  			  }
  			  $scope.users = resp.result;
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
    $http.post("security/user/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&account="
		  +$scope.account+"&userName="+$scope.userName+"&telephone="+$scope.telephone).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.userscount=resp.userCounts;
			  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.users = resp.result;
		  });
  };
  $scope.query= function(){
	    $http.post("security/user/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&account="
    		  +$scope.account+"&userName="+$scope.userName+"&telephone="+$scope.telephone).success(function(response) {
    			  var resp =angular.fromJson(angular.fromJson(response));
    			  console.log(resp);
    			  var returnCode = resp.returnCode;
    			  var message = resp.message;
    			  $scope.userscount=resp.userCounts;
    			  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
    			  if($scope.allpages<=5){
    				  $scope.rightpagestatus=$scope.status1;
    			  }
    			  $scope.users = resp.result;
    		  });
  }
  
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.users.length;i++){
	  		if(i==index){
	  			$scope.users[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.users[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.users.length;i++){
	  		$scope.users[i].style="";
	  	}
	  }
}])

app.controller('UserAddCtrl', ['$scope','$rootScope','$http','$window', function($scope,$rootScope,$http,$window){
	$http.post("security/role/list.do?pageIndex="+ 0 +"&pageSize="+0).success(function(response) {
		var resp =angular.fromJson(angular.fromJson(response));
		console.log(resp);
		var returnCode = resp.returnCode;
		var message = resp.message;
		$scope.roleslist=[];
		for (var i = 0; i < resp.result.length; i++) {
			var x={};
			x.id=resp.result[i].id;
			x.roleName=resp.result[i].roleName;
			$scope.roleslist.push(x);
		};
	});
	
//	$rootScope.roleslist = [
//	                    {id:'1',roleName:'领导'},
//	                    {id:'2',roleName:'员工'},
//	                    {id:'2',roleName:'大妈'},
//	                    {id:'2',roleName:'阿姨'},
//	                    {id:'4',roleName:'波波'}
//	                    ];
//	$http.post("security/role/list.do?pageIndex="+ 0 +"&pageSize="+0).success(function(response) {
//		var resp =angular.fromJson(angular.fromJson(response));
//		console.log(resp);
//		var returnCode = resp.returnCode;
//		var message = resp.message;
//		$rootScope.roleslist = [
//		                    {id:'1',roleName:'领导'},
//		                    {id:'2',roleName:'员工'},
//		                    {id:'2',roleName:'大妈'},
//		                    {id:'2',roleName:'阿姨'},
//		                    {id:'2',roleName:'阿姨'},
//		                    {id:'4',roleName:'波波'}
//		                    ];
//		console.log($scope.roleslist);
//	});
	$scope.telclass="form-group";
    var phoneNumberRegex = /^1\d{10}$/;
    $scope.phoneNumberPattern = phoneNumberRegex;
   $scope.account="";
   $scope.password="";
   $scope.userName="";
   $scope.unitName="";
   $scope.telephone="";
   $scope.remark="";
   $scope.role="";
   $scope.save= function (){
	   $scope.userroles=[];
	   console.log($scope.account,$scope.password,$scope.userName,$scope.unitName,$scope.telephone,$scope.remark,$scope.role);
//	   console.log(angular.toJson($scope.roles[$scope.role]));
//	   for(var i=0;i<$scope.role.length;i++){
		   $scope.userroles.push($scope.roleslist[$scope.role]);
//	   }
	   $http.post("security/user/add.do?account="+ $scope.account+"&password="+$scope.password+"&userName="
			+$scope.userName+"&unitName="+$scope.unitName+"&telephone="+$scope.telephone+"&remark="
			+$scope.remark+"&roles="+angular.toJson($scope.userroles)).success(function(response) {
				var resp =angular.fromJson(angular.fromJson(response));
				console.log(resp);
				var returnCode = resp.returnCode;
				var message = resp.message;
				alert(message);
			});
   };
   $scope.checktel= function (){
	   if(!phoneNumberRegex.test($scope.telephone)){
		   $scope.telclass="form-group has-error";
	   }else{
		   $scope.telclass="form-group has-success";
	   }
   };

}])