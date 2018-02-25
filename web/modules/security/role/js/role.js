

app.controller('RolesListCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
  $scope.activebase='active';
  $scope.active1=$scope.activebase;
  $scope.active2=!$scope.activebase;
  $scope.active3=!$scope.activebase;
  $scope.loglog = function(item){
    console.log(item);
  }
  $scope.roles=[
      {id:'1',roleName:'20',createTime:'12'},
      {id:'2',roleName:'20',createTime:''},
      {id:'2',roleName:'20',createTime:''},
      {id:'2',roleName:'20',createTime:''},
      {id:'3',roleName:'20',createTime:''},
      {id:'4',roleName:'10',createTime:''}
    ];
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
  $scope.roleName="";
  $http.post("security/role/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize+"&roleName="+$scope.roleName).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.userscount=resp.roleCounts;
		  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.roles = resp.result;
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
    $http.post("security/role/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&roleName="+$scope.roleName).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.userscount=resp.roleCounts;
		  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.roles = resp.result;
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
    $http.post("security/role/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&roleName="+$scope.roleName).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.userscount=resp.roleCounts;
		  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.roles = resp.result;
	  });
  };
  $scope.account="";
  $scope.password="";
  $scope.account="";
  $scope.query= function(){
	    $http.post("security/role/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&roleName="+$scope.roleName).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.userscount=resp.roleCounts;
			  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.roles = resp.result;
		  });
  }
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.roles.length;i++){
	  		if(i==index){
	  			$scope.roles[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.roles[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.roles.length;i++){
	  		$scope.roles[i].style="";
	  	}
	  }
}])

app.controller('RoleAddCtrl', ['$scope','$rootScope','$http', function($scope,$rootScope,$http){
   $scope.roles = [
          {id:'1',roleName:'100',createTime:''},
          {id:'2',roleName:'20',createTime:''},
          {id:'2',roleName:'20',createTime:''},
          {id:'2',roleName:'20',createTime:''},
          {id:'3',roleName:'20',createTime:''},
          {id:'4',roleName:'10',createTime:''}
        ];
   $scope.account="";
   $scope.password="";
   $scope.userName="";
   $scope.unitName="";
   $scope.telephone="";
   $scope.remark="";
   $scope.user="";
  $scope.save= function (){
    console.log($scope.account,$scope.password,$scope.userName,$scope.unitName,$scope.telephone,$scope.remark,$scope.user);
	   $scope.userroles=[];
	   console.log($scope.account,$scope.password,$scope.userName,$scope.unitName,$scope.telephone,$scope.remark,$scope.user);
//	   console.log(angular.toJson($scope.roles[$scope.role]));
	   for(var i=0;i<$scope.user.length;i++){
		   $scope.userroles.push($scope.userslist[i]);
	   }
	   $http.post("security/role/add.do?roleName="+ $scope.roleName+"&remark="+$scope.remark+"&remark="
			+$scope.userName+"&users="+angular.toJson($scope.userroles)).success(function(response) {
				var resp =angular.fromJson(angular.fromJson(response));
				console.log(resp);
				var returnCode = resp.returnCode;
				var message = resp.message;
				alert(message);
			});
  };
  
  $scope.perm1=false;
  $scope.perm2=false;
  $scope.perm11={name:'用户角色管理',code:'1'};
  $scope.perm22={name:'自动化部署管理',code:'2'};
  $scope.click1=function() {
    console.log($scope.perm1);
    console.log($scope.perm2);
    console.log($scope.perm11);
    console.log($scope.perm22);
  }
  $scope.click2=function() {
    console.log($scope.perm1);
    console.log($scope.perm2);
    console.log($scope.perm11);
    console.log($scope.perm22);
  }
  $scope.users=[
          {id:'1',userName:'name1'},
          {id:'2',userName:'name2'},
          {id:'3',userName:'name3'},
          {id:'4',userName:'name4'},
          {id:'5',userName:'name5'},
          {id:'6',userName:'name6'}
        ];
  $scope.users1=$scope.users.slice($scope.users.length/2,$scope.users.length);
  $scope.users2=$scope.users.slice(0,$scope.users.length/2);
  
}])