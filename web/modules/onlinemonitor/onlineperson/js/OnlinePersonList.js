
app.controller('OnlinePersonListCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
	  $scope.onlinePersonList = [
	      {id:'1',personname:'运维中',company:'John',department:'2016-5-12',telephone:'@jh',createTime:'a@company.com'},
	      {id:'2',personname:'运维中',company:'Bill',department:'2016-5-9',telephone:'@bg',createTime:'bg@company.com'},
	      {id:'3',personname:'暂停',company:'Bobo',department:'2015-5-12',telephone:'@hz',createTime:'c@company.com'},
	      {id:'4',personname:'暂停',company:'Bobo',department:'2015-5-12',telephone:'@hz',createTime:'c@company.com'},
	      {id:'5',personname:'暂停',company:'Bobo',department:'2015-5-12',telephone:'@hz',createTime:'c@company.com'},
	      {id:'6',personname:'暂停',company:'Bobo',department:'2015-5-12',telephone:'@hz',createTime:'c@company.com'}
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
	  $scope.onlinePersonCounts=100;
	  $scope.pageSize=6;
	  $scope.currentpage=0;
	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
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
	      {index:0,status:'active'},
	      {index:1,status:'enable'},
	      {index:2,status:'enable'},
	      {index:3,status:'enable'},
	      {index:4,status:'enable'}
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
}])
