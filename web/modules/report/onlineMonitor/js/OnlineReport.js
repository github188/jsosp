app.controller('OnlineReportCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	 $scope.platforms = [ {
				id : '123',
				name : '',
			}, {
				id : '123',
				name : '集成服务',
			}, {
				id : '123',
				name : '云平台',
			}, {
				id : '123',
				name : '管理平台',
			}, {
				id : '123',
				name : '云服务',
			}
	        ];
	 
  $scope.versionDate="";
  
  $scope.dt1=new Date();
  $scope.dt1.setDate(new Date().getDate()-7);
  $scope.openCalendar1 = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened1 = true;
  };
  $scope.dt2=new Date();
  $scope.dt2.setDate(new Date().getDate());
  $scope.openCalendar2 = function ($event) {
	  $event.preventDefault();
	  $event.stopPropagation();
	  $scope.opened2 = true;
  };
  
  $scope.activebase='active';
  $scope.active1=$scope.activebase;
  $scope.active2=!$scope.activebase;
  $scope.active3=!$scope.activebase;
  $scope.loglog = function(item){
    console.log(item);
  }
  $scope.projects=[
      {id:'1',platform_name:'',name:'100',code:'active',date:'1',offtimes:'123',offtime:'12'},
      {id:'2',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'2',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'2',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'3',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'4',platform_name:'',name:'10',code:'disable',date:'',offtimes:'',offtime:''}
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
    $state.go('OnlineProjectDetail',{id: $scope.projects[index].id});
  };
  $scope.menus=['项目名称','接入日期','所属系统'];
  $scope.sortBy = function(menu){
    console.log(menu);
  };
  $scope.onlineRate='0%';
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
  $scope.componentId="";
  $scope.principal="";
  $scope.hasDbScript=false;
  $scope.hasParaFile=false;
  $http.post("report/onlinereport/platformlist.do").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.platforms = resp.result;
	  $scope.platforms.splice(0, 0, {id : '',name : ''});
//	  $scope.platforms.push({id : '',name : ''});
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
    $http.post("report/onlinereport/projectlist.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
    		$scope.platformid.id+"&projectname="+$scope.projectname+"&projectcode="+$scope.projectcode+"&starttime="+
    		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')).success(function(response) {
    	  var resp =angular.fromJson(angular.fromJson(response));
    	  console.log(resp);
    	  var returnCode = resp.returnCode;
    	  var message = resp.message;
    	  $scope.onlineRate=resp.onlineRate;
    	  $scope.userscount=resp.projectlistCounts;
    	  $scope.offlinetimes=resp.offlinetimes;
    	  $scope.offlinetime=resp.offlinetime;
    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
    	  if($scope.allpages<=5){
    		  $scope.rightpagestatus=$scope.status1;
    	  }
    	  $scope.projects = resp.result;
    	  if($scope.userscount==0){
    		  alert('查询条件有误，无数据');
    	  }
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
    $http.post("report/onlinereport/projectlist.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
    		$scope.platformid.id+"&projectname="+$scope.projectname+"&projectcode="+$scope.projectcode+"&starttime="+
    		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')).success(function(response) {
    	  var resp =angular.fromJson(angular.fromJson(response));
    	  console.log(resp);
    	  var returnCode = resp.returnCode;
    	  var message = resp.message;
    	  $scope.onlineRate=resp.onlineRate;
    	  $scope.userscount=resp.projectlistCounts;
    	  $scope.offlinetimes=resp.offlinetimes;
    	  $scope.offlinetime=resp.offlinetime;
    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
    	  if($scope.allpages<=5){
    		  $scope.rightpagestatus=$scope.status1;
    	  }
    	  $scope.projects = resp.result;
    	  if($scope.userscount==0){
    		  alert('查询条件有误，无数据');
    	  }
      });
  };
  $http.post("report/onlinereport/projectlist.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize+"&starttime="+
  		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')).success(function(response) {
  	  var resp =angular.fromJson(angular.fromJson(response));
  	  console.log(resp);
  	  var returnCode = resp.returnCode;
  	  var message = resp.message;
  	  $scope.onlineRate=resp.onlineRate;
  	  $scope.userscount=resp.projectlistCounts;
	  $scope.offlinetimes=resp.offlinetimes;
	  $scope.offlinetime=resp.offlinetime;
  	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
  	  if($scope.allpages<=5){
  		  $scope.rightpagestatus=$scope.status1;
  	  }
  	  $scope.projects = resp.result;
	  if($scope.userscount==0){
		  alert('查询条件有误，无数据');
	  }
    });
  
  $scope.account="";
  $scope.password="";
  $scope.account="";
  
  $scope.platformid="";
  $scope.projectname="";
  $scope.projectcode="";
  
  $scope.query= function(){
	    $http.post("report/onlinereport/projectlist.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
	    		$scope.platformid.id+"&projectname="+$scope.projectname+"&projectcode="+$scope.projectcode+"&starttime="+
	    		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')).success(function(response) {
	    	  var resp =angular.fromJson(angular.fromJson(response));
	    	  console.log(resp);
	    	  var returnCode = resp.returnCode;
	    	  var message = resp.message;
	    	  $scope.onlineRate=resp.onlineRate;
	    	  $scope.userscount=resp.projectlistCounts;
	    	  $scope.offlinetimes=resp.offlinetimes;
	    	  $scope.offlinetime=resp.offlinetime;
	    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
	    	  if($scope.allpages<=5){
	    		  $scope.rightpagestatus=$scope.status1;
	    	  }
	    	  $scope.projects = resp.result;
	    	  if($scope.userscount==0){
	    		  alert('查询条件有误，无数据');
	    	  }
	      });
  }
}])

app.controller('OnlineProjectDetailCtrl', ['$scope','$state','$stateParams','$http','$filter','$window','$cookieStore', function($scope,$state,$stateParams,$http,$filter,$window,$cookieStore){
	$scope.handleUser=$cookieStore.get('account');
	var id = $stateParams.id;

	 $scope.activebase='active';
	 $scope.active1=$scope.activebase;
	 $scope.active2=!$scope.activebase;
	 $scope.active3=!$scope.activebase;
	 $scope.loglog = function(item){
	   console.log(item);
	 }
	 $scope.projects=[
	     {id:'1',name:'100',code:'active',event_name:'1',event_time:'123',check:false},
	     {id:'2',name:'20',code:'disable',event_name:'',event_time:'',check:true},
	     {id:'2',name:'20',code:'disable',event_name:'',event_time:'',check:false},
	     {id:'2',name:'20',code:'disable',event_name:'',event_time:'',check:false},
	     {id:'3',name:'20',code:'disable',event_name:'',event_time:'',check:false},
	     {id:'4',name:'10',code:'disable',event_name:'',event_time:'',check:false}
	   ];
	 $scope.lastactive=0;//保存上一个被选中的按钮
	 $scope.active = function(index){
	   $scope.items[$scope.lastactive].style='disable';
	   $scope.items[index].style='active';
	   $scope.lastactive=index;
	 };
	 $scope.counts=0;
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
	 $scope.componentId="";
	 $scope.principal="";
	 $scope.hasDbScript=false;
	 $scope.hasParaFile=false;
	 $http.post("report/onlinereport/onlinereportdetail.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize+"&id="+id).success(function(response) {
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
		 $http.post("report/onlinereport/onlinereportdetail.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&id="+id).success(function(response) {
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
		 $http.post("report/onlinereport/onlinereportdetail.do?pageIndex="+$scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&id="+id).success(function(response) {
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
	 
	 $scope.account="";
	 $scope.password="";
	 $scope.account="";
	 
	 $scope.platformid="";
	 $scope.projectname="";
	 $scope.projectcode="";
	 
	 $scope.query= function(){
		 $http.post("report/onlinereport/onlinereportdetail.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&id="+id).success(function(response) {
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
	 }
	 
	 $scope.reason="";
	 $scope.reasonchange= function(index){
		 console.log(index);
		 console.log($scope.projects[index]);
		 console.log($scope.reasons[index]);
		 $http.post("report/onlinereport/onlinereporthandle.do?id="+ $scope.id +"&reason="+$scope.reason.name).success(function(response) {
			 var resp =angular.fromJson(angular.fromJson(response));
			 console.log(resp);
			 var returnCode = resp.returnCode;
			 if(returnCode!=0){
				 alert(resp.message);
			 }
			 $http.post("report/onlinereport/onlinereportdetail.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&id="+id).success(function(response) {
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
	 }
	 
	 $scope.reasons=[
	                 {
	                	 id : '123',
	                	 name : '网络故障',
	                 }, {
	                	 id : '123',
	                	 name : '系统故障',
	                 }, {
	                	 id : '123',
	                	 name : '前端软件卡死',
	                 }, {
	                	 id : '123',
	                	 name : '电脑重启',
	                 } , {
	                	 id : '123',
	                	 name : '适配器被关闭',
	                 }   , {
	                	 id : '123',
	                	 name : '电脑被关闭',
	                 }   , {
	                	 id : '123',
	                	 name : '适配器假死',
	                 }  , {
	                	 id : '123',
	                	 name : '电脑重启后没输入密码',
	                 }, {
	                	 id : '123',
	                	 name : '其他',
	                 }           
	          ];
	 $scope.ids=[];
	 $scope.remark='';
	 $scope.optchange= function(index){
		 if($scope.projects[index].check!=true){
			 $scope.projects[index].check=true;
			 $scope.ids.push($scope.projects[index].id);
		 }else{
			 $scope.projects[index].check=false;
			 for (var i = 0; i < $scope.ids.length; i++) {
				 if($scope.ids[i]==$scope.projects[index].id){
					 $scope.ids.splice(i,1);
					 break;
				 }
			 };
		 }
		 console.log($scope.ids);
	 }
	 
	 $scope.message= function(index){
		 $scope.id=$scope.projects[index].id;
		 $scope.infoproject=$scope.projects[index].name;
		 $scope.infocode=$scope.projects[index].code;
		 for (var i = 0; i < $scope.reasons.length; i++) {
			 if($scope.reasons[i].name==$scope.projects[index].reason_type){
				 $scope.reasons.splice(i,1);
				 break;
			 }
		 };
		 $scope.oo = $scope.projects[index].reason_type ;
	 }
	 
	 $scope.save= function(){
		 if($scope.ids.length==0){
			 return
		 }
		 $http.post("report/onlinereport/onlinereporthandle.do?ids="+ $scope.ids +"&reason="+$scope.reason.name+"&remark="+$scope.remark+"&create_user="+$scope.handleUser).success(function(response) {
			 var resp =angular.fromJson(angular.fromJson(response));
			 console.log(resp);
			 var returnCode = resp.returnCode;
			 if(returnCode!=0){
				 alert(resp.message);
			 }
			 $http.post("report/onlinereport/onlinereportdetail.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&id="+id).success(function(response) {
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
			 $scope.ids=[];
		 });
	 }
	 
}])

app.controller('OfflineHandleReportCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	 $scope.platforms = [ {
				id : '123',
				name : '',
			}, {
				id : '123',
				name : '集成服务',
			}, {
				id : '123',
				name : '云平台',
			}, {
				id : '123',
				name : '管理平台',
			}, {
				id : '123',
				name : '云服务',
			}
	        ];
	 
  $scope.versionDate="";
  
  $scope.dt1=new Date();
  $scope.dt1.setDate(new Date().getDate()-7);
  $scope.openCalendar1 = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened1 = true;
  };
  $scope.dt2=new Date();
  $scope.dt2.setDate(new Date().getDate());
  $scope.openCalendar2 = function ($event) {
	  $event.preventDefault();
	  $event.stopPropagation();
	  $scope.opened2 = true;
  };
  
  $scope.activebase='active';
  $scope.active1=$scope.activebase;
  $scope.active2=!$scope.activebase;
  $scope.active3=!$scope.activebase;
  $scope.loglog = function(item){
    console.log(item);
  }
  $scope.projects=[
      {id:'1',platform_name:'',name:'100',code:'active',date:'1',offtimes:'123',offtime:'12'},
      {id:'2',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'2',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'2',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'3',platform_name:'',name:'20',code:'disable',date:'',offtimes:'',offtime:''},
      {id:'4',platform_name:'',name:'10',code:'disable',date:'',offtimes:'',offtime:''}
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
    $state.go('OnlineProjectDetail',{id: $scope.projects[index].id});
  };
  $scope.menus=['项目名称','接入日期','所属系统'];
  $scope.sortBy = function(menu){
    console.log(menu);
  };
  $scope.onlineRate='0%';
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
  $scope.componentId="";
  $scope.principal="";
  $scope.hasDbScript=false;
  $scope.hasParaFile=false;
  $http.post("report/onlinereport/platformlist.do").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.platforms = resp.result;
	  $scope.platforms.splice(0, 0, {id : '',name : ''});
//	  $scope.platforms.push({id : '',name : ''});
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
    var handle=0;//0全部查询，1查询已处理，2查询未处理
	  console.log($scope.handle1);
	  console.log($scope.handle2);
	  if($scope.handle1 && !$scope.handle2){
		  handle=1;
	  }else if (!$scope.handle1 && $scope.handle2){
		  handle=2;
	  }
	    $http.post("report/onlinereport/offlinereport.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
	    		$scope.platformid.id+"&projectname="+$scope.projectname+"&projectcode="+$scope.projectcode+"&starttime="+
	    		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')+"&handle="+handle).success(function(response) {
	    	  var resp =angular.fromJson(angular.fromJson(response));
	    	  console.log(resp);
	    	  var returnCode = resp.returnCode;
	    	  var message = resp.message;
	    	  $scope.onlineRate=resp.onlineRate;
	    	  $scope.userscount=resp.projectlistCounts;
	    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
	    	  if($scope.allpages<=5){
	    		  $scope.rightpagestatus=$scope.status1;
	    	  }
	    	  $scope.projects = resp.result;
	    	  if($scope.userscount==0){
	    		  alert('查询条件有误，无数据');
	    	  }
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
    var handle=0;//0全部查询，1查询已处理，2查询未处理
	  console.log($scope.handle1);
	  console.log($scope.handle2);
	  if($scope.handle1 && !$scope.handle2){
		  handle=1;
	  }else if (!$scope.handle1 && $scope.handle2){
		  handle=2;
	  }
	    $http.post("report/onlinereport/offlinereport.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
	    		$scope.platformid.id+"&projectname="+$scope.projectname+"&projectcode="+$scope.projectcode+"&starttime="+
	    		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')+"&handle="+handle).success(function(response) {
	    	  var resp =angular.fromJson(angular.fromJson(response));
	    	  console.log(resp);
	    	  var returnCode = resp.returnCode;
	    	  var message = resp.message;
	    	  $scope.onlineRate=resp.onlineRate;
	    	  $scope.userscount=resp.projectlistCounts;
	    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
	    	  if($scope.allpages<=5){
	    		  $scope.rightpagestatus=$scope.status1;
	    	  }
	    	  $scope.projects = resp.result;
	    	  if($scope.userscount==0){
	    		  alert('查询条件有误，无数据');
	    	  }
	      });
  };
  $http.post("report/onlinereport/projectlist.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize+"&starttime="+
  		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')).success(function(response) {
  	  var resp =angular.fromJson(angular.fromJson(response));
  	  console.log(resp);
  	  var returnCode = resp.returnCode;
  	  var message = resp.message;
  	  $scope.onlineRate=resp.onlineRate;
  	  $scope.userscount=resp.projectlistCounts;
  	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
  	  if($scope.allpages<=5){
  		  $scope.rightpagestatus=$scope.status1;
  	  }
  	  $scope.projects = resp.result;
	  if($scope.userscount==0){
		  alert('查询条件有误，无数据');
	  }
    });
  
  $scope.account="";
  $scope.password="";
  $scope.account="";
  
  $scope.platformid="";
  $scope.projectname="";
  $scope.projectcode="";
  
  $scope.query= function(){
	  var handle=0;//0全部查询，1查询已处理，2查询未处理
	  console.log($scope.handle1);
	  console.log($scope.handle2);
	  if($scope.handle1 && !$scope.handle2){
		  handle=1;
	  }else if (!$scope.handle1 && $scope.handle2){
		  handle=2;
	  }
	    $http.post("report/onlinereport/offlinereport.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
	    		$scope.platformid.id+"&projectname="+$scope.projectname+"&projectcode="+$scope.projectcode+"&starttime="+
	    		$filter('date')($scope.dt1,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt2,'yyyy-MM-dd')+"&handle="+handle).success(function(response) {
	    	  var resp =angular.fromJson(angular.fromJson(response));
	    	  console.log(resp);
	    	  var returnCode = resp.returnCode;
	    	  var message = resp.message;
	    	  $scope.onlineRate=resp.onlineRate;
	    	  $scope.userscount=resp.projectlistCounts;
	    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
	    	  if($scope.allpages<=5){
	    		  $scope.rightpagestatus=$scope.status1;
	    	  }
	    	  $scope.projects = resp.result;
	    	  if($scope.userscount==0){
	    		  alert('查询条件有误，无数据');
	    	  }
	      });
  }
}])

app.controller('ActualTimeOnlineReportCtrl', ['$scope','$state','$stateParams','$http','$filter','$interval','$timeout',function($scope,$state,$stateParams,$http,$filter,$interval,$timeout){
	$scope.tongjicount=0;
	$scope.platforms = [ {
		id : '123',
		name : '',
	}, {
		id : '123',
		name : '集成服务',
	}, {
		id : '123',
		name : '云平台',
	}, {
		id : '123',
		name : '管理平台',
	}, {
		id : '123',
		name : '云服务',
	}
	];
	
	$scope.versionDate="";
	
	$scope.activebase='active';
	$scope.active1=$scope.activebase;
	$scope.active2=!$scope.activebase;
	$scope.active3=!$scope.activebase;
	$scope.loglog = function(item){
		console.log(item);
	}
	$scope.projects=[
	                 {id:'1',platform_name:'',offlineproject:'100',offlineprojectRate:'active'},
	                 {id:'2',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'2',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'2',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'3',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'4',platform_name:'',offlineproject:'10',offlineprojectRate:'disable'}
	                 ];
	$scope.lastactive=0;//保存上一个被选中的按钮
	$scope.active = function(index){
		$scope.items[$scope.lastactive].style='disable';
		$scope.items[index].style='active';
		$scope.lastactive=index;
	};
	$scope.edit = function(index){
		$state.go('ActualTimeOnlineReportDetail',{id: $scope.projects[index].id});
	};
	$scope.menus=['项目名称','接入日期','所属系统'];
	$scope.sortBy = function(menu){
		console.log(menu);
	};
	$scope.offlineprojectcounts=0;
	$scope.counts=0;
	$scope.pageSize=6;
	$scope.currentpage=0;
	$scope.allpages=Math.ceil($scope.offlineprojectcounts/$scope.pageSize);
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
	$scope.componentId="";
	$scope.principal="";
	$scope.hasDbScript=false;
	$scope.hasParaFile=false;
	$http.post("report/onlinereport/platformlist.do").success(function(response) {
		var resp =angular.fromJson(angular.fromJson(response));
		console.log(resp);
		var returnCode = resp.returnCode;
		var message = resp.message;
		$scope.platforms = resp.result;
		$scope.platforms.splice(0, 0, {id : '',name : ''});
//	  $scope.platforms.push({id : '',name : ''});
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
		$http.post("report/onlinereport/actualTimeOnlineReport.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
				$scope.platformid.id).success(function(response) {
					var resp =angular.fromJson(angular.fromJson(response));
					console.log(resp);
					var returnCode = resp.returnCode;
					var message = resp.message;
					$scope.offlineprojectcounts=resp.counts;
					$scope.counts=resp.offcounts;
					$scope.offRateAll=resp.offRateAll;
					$scope.option1.series[0].data[0].value= $scope.offRateAll;
					$scope.allpages=Math.ceil($scope.offlineprojectcounts/$scope.pageSize);
					if($scope.allpages<=5){
						$scope.rightpagestatus=$scope.status1;
					}
					$scope.projects = resp.result;
					if($scope.projects ==null || $scope.projects.length==0){
						alert('查询条件有误，无数据');
					}
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
		$http.post("report/onlinereport/actualTimeOnlineReport.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
				$scope.platformid.id).success(function(response) {
					var resp =angular.fromJson(angular.fromJson(response));
					console.log(resp);
					var returnCode = resp.returnCode;
					var message = resp.message;
					$scope.offlineprojectcounts=resp.counts;
					$scope.counts=resp.offcounts;
					$scope.offRateAll=resp.offRateAll;
					$scope.option1.series[0].data[0].value= $scope.offRateAll;
					$scope.allpages=Math.ceil($scope.offlineprojectcounts/$scope.pageSize);
					if($scope.allpages<=5){
						$scope.rightpagestatus=$scope.status1;
					}
					$scope.projects = resp.result;
					if($scope.projects ==null || $scope.projects.length==0){
						alert('查询条件有误，无数据');
					}
				});
	};
    $timeout(function () {
        $http.post("report/onlinereport/actualTimeOnlineReport.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize).success(function(response) {
        	var resp =angular.fromJson(angular.fromJson(response));
        	console.log(resp);
        	var returnCode = resp.returnCode;
        	var message = resp.message;
        	$scope.offlineprojectcounts=resp.counts;
        	$scope.counts=resp.offcounts;
        	$scope.offRateAll=resp.offRateAll;
        	$scope.option1.series[0].data[0].value= $scope.offRateAll;
        	$scope.allpages=Math.ceil($scope.offlineprojectcounts/$scope.pageSize);
        	if($scope.allpages<=5){
        		$scope.rightpagestatus=$scope.status1;
        	}
        	var result = resp.result;
        	if(result ==null || result.length==0){
        		alert('查询条件有误，无数据');
        	};
        	$scope.projects=[];
        	for (var i = result.length - 1; i >= 0; i--) {
        		var option1 = {
        				title : {
        					text: '离线率',
        					subtext: '',
        					x:'center'
        				},
        				tooltip : {
//					        formatter: "{a} <br/>{b} : {c}%"
        					formatter: "{b} : {c}%"
        				},
        				toolbox: {
        					feature: {
        						restore: {},
        						saveAsImage: {}
        					}
        				},
        				series: [
        				         {
        				        	 name: '',
        				        	 type: 'gauge',
        				        	 axisLine: {
        				        		 show: true,
        				        		 lineStyle: {
        				        			 width: 10,
        				        			 shadowBlur: 0,
        				        			 color: [[0.2, '#9CD6CE'],
        				        			         [0.4, '#7CBB55'],
        				        			         [0.6, '#DDBD4D'],
        				        			         [0.8, '#E98E2C'],
        				        			         [1, '#E43F3D']]
        				        		 }
        				        	 },
        				        	 splitLine: {
        				        		 show: true,
        				        		 length: 10,
        				        		 lineStyle: {
        				        			 //color:'black'
        				        		 },
        				        	 },
        				        	 pointer: {
        				        		 width: 2,
        				        		 shadowColor: '#fff', //默认透明
        				        		 shadowBlur: 5
        				        	 },
        				        	 detail: {
        				        		 formatter:'{value}',
        				        		 offsetCenter: [0, 50],
        				        		 textStyle: {
        				        			 fontSize: 20
        				        		 }
        				        	 },		            	
        				        	 data: [{value: $scope.offRateAll, name: '离线率(%)'}]
        				         }
        				         ],
        				         color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
        		};
        		option1.title.text=result[i].platform_name;
        		option1.series[0].data[0].value=result[i].offRate;
        		$scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
        	};
        });
    }, 1000);//延时2s后执行
	
	$scope.account="";
	$scope.password="";
	$scope.account="";
	
	$scope.platformid="";
	$scope.projectname="";
	$scope.projectcode="";
	
//	$scope.query= function(){
//		$scope.msg="正在轮询.第"+$scope.tongjicount+"次";
		$scope.timeout_upd = $interval(function () {
			$http.post("report/onlinereport/actualTimeOnlineReport.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize).success(function(response) {
				var resp =angular.fromJson(angular.fromJson(response));
				console.log(resp);
				var returnCode = resp.returnCode;
				var message = resp.message;
				$scope.offlineprojectcounts=resp.counts;
				$scope.counts=resp.offcounts;
				$scope.offRateAll=resp.offRateAll;
				$scope.option1.series[0].data[0].value= $scope.offRateAll;
				$scope.allpages=Math.ceil($scope.offlineprojectcounts/$scope.pageSize);
				if($scope.allpages<=5){
					$scope.rightpagestatus=$scope.status1;
				}
				var result = resp.result;
				if(result ==null || result.length==0){
					alert('查询条件有误，无数据');
				};
				$scope.projects=[];
				for (var i = result.length - 1; i >= 0; i--) {
					var option1 = {
							title : {
								text: '离线率',
								subtext: '',
								x:'center'
							},
							tooltip : {
//					        formatter: "{a} <br/>{b} : {c}%"
								formatter: "{b} : {c}%"
							},
							toolbox: {
								feature: {
									restore: {},
									saveAsImage: {}
								}
							},
							series: [
							         {
							        	 name: '',
							        	 type: 'gauge',
							        	 axisLine: {
							        		 show: true,
							        		 lineStyle: {
							        			 width: 10,
							        			 shadowBlur: 0,
							        			 color: [[0.2, '#9CD6CE'],
							        			         [0.4, '#7CBB55'],
							        			         [0.6, '#DDBD4D'],
							        			         [0.8, '#E98E2C'],
							        			         [1, '#E43F3D']]
							        		 }
							        	 },
							        	 splitLine: {
							        		 show: true,
							        		 length: 10,
							        		 lineStyle: {
							        			 //color:'black'
							        		 },
							        	 },
							        	 pointer: {
							        		 width: 2,
							        		 shadowColor: '#fff', //默认透明
							        		 shadowBlur: 5
							        	 },
							        	 detail: {
							        		 formatter:'{value}',
							        		 offsetCenter: [0, 50],
							        		 textStyle: {
							        			 fontSize: 20
							        		 }
							        	 },		            	
							        	 data: [{value: $scope.offRateAll, name: '离线率(%)'}]
							         }
							         ],
							         color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
					};
					option1.title.text=result[i].platform_name;
					option1.series[0].data[0].value=result[i].offRate;
					$scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
				};
			});
			
		}, 5000);//每一秒更新一次theTime
//	}
	
	$scope.$on('$destroy', function(){
		$interval.cancel($scope.timeout_upd);  
	});
	$scope.chg=function(){
		$http.post("report/onlinereport/actualTimeOnlineReport.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&platformid="+
				$scope.platformid.id).success(function(response) {
					var resp =angular.fromJson(angular.fromJson(response));
					console.log(resp);
					var returnCode = resp.returnCode;
					var message = resp.message;
					$scope.offlineprojectcounts=resp.counts;
					$scope.counts=resp.offcounts;
					$scope.offRateAll=resp.offRateAll;
					$scope.option1.series[0].data[0].value= $scope.offRateAll;
					$scope.allpages=Math.ceil($scope.offlineprojectcounts/$scope.pageSize);
					if($scope.allpages<=5){
						$scope.rightpagestatus=$scope.status1;
					}
					var result = resp.result;
					if(result ==null || result.length==0){
						alert('查询条件有误，无数据');
					}
					$scope.projects=[];
			        for (var i = result.length - 1; i >= 0; i--) {
			        	var option1 = {
			        			title : {
			        				text: '离线率',
			        				subtext: '',
			        				x:'center'
			        			},
			        			tooltip : {
//						        formatter: "{a} <br/>{b} : {c}%"
			        				formatter: "{b} : {c}%"
			        			},
			        			toolbox: {
			        				feature: {
			        					restore: {},
			        					saveAsImage: {}
			        				}
			        			},
			        			series: [
			        			         {
			        			        	 name: '',
			        			        	 type: 'gauge',
			        			        	 axisLine: {
			        			        		 show: true,
			        			        		 lineStyle: {
			        			        			 width: 10,
			        			        			 shadowBlur: 0,
			        			        			 color: [[0.2, '#9CD6CE'],
			        			        			         [0.4, '#7CBB55'],
			        			        			         [0.6, '#DDBD4D'],
			        			        			         [0.8, '#E98E2C'],
			        			        			         [1, '#E43F3D']]
			        			        		 }
			        			        	 },
			        			        	 splitLine: {
			        			        		 show: true,
			        			        		 length: 10,
			        			        		 lineStyle: {
			        			        			 //color:'black'
			        			        		 },
			        			        	 },
			        			        	 pointer: {
			        			        		 width: 2,
			        			        		 shadowColor: '#fff', //默认透明
			        			        		 shadowBlur: 5
			        			        	 },
			        			        	 detail: {
			        			        		 formatter:'{value}',
			        			        		 offsetCenter: [0, 50],
			        			        		 textStyle: {
			        			        			 fontSize: 20
			        			        		 }
			        			        	 },		            	
			        			        	 data: [{value: $scope.offRateAll, name: '离线率(%)'}]
			        			         }
			        			         ],
			        			         color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
			        	};
			        	option1.title.text=result[i].platform_name;
			        	option1.series[0].data[0].value=result[i].offRate;
			        	$scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
			        };
					
					
				});
	}
  $scope.offRateAll=0;
  $scope.option1 = {
		  	title : {
	  	        text: '离线率',
	  	        subtext: '',
	  	        x:'center'
	  	    },
		    tooltip : {
//		        formatter: "{a} <br/>{b} : {c}%"
		        formatter: "{b} : {c}%"
		    },
		    toolbox: {
		        feature: {
		            restore: {},
		            saveAsImage: {}
		        }
		    },
		    series: [
		        {
		            name: '',
		            type: 'gauge',
		            axisLine: {
				        	show: true,
				            lineStyle: {
				                width: 10,
				                shadowBlur: 0,
				                color: [[0.2, '#9CD6CE'],
				                        [0.4, '#7CBB55'],
				                        [0.6, '#DDBD4D'],
				                        [0.8, '#E98E2C'],
				                        [1, '#E43F3D']]
				            }
				        },
			        splitLine: {
			            show: true,
			            length: 10,
			            lineStyle: {
			                //color:'black'
			            },
			        },
			        pointer: {
                        width: 2,
                        shadowColor: '#fff', //默认透明
                        shadowBlur: 5
                    },
                    detail: {
                    	formatter:'{value}',
                        offsetCenter: [0, 50],
                        textStyle: {
                            fontSize: 20
                        }
                    },		            	
		            data: [{value: $scope.offRateAll, name: '离线率(%)'}]
		        }
		    ],
		    color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
		};;
  function pieConfig1(params){
      console.log(params);
  };
  
  $scope.pieConfig1 = {
                      theme:'default',
                      event: [{click:pieConfig1}],
                      dataLoaded:true
                  }; 
}])

app.controller('ActualTimeOnlineReportDetailCtrl', ['$scope','$state','$stateParams','$http','$filter','$interval',function($scope,$state,$stateParams,$http,$filter,$interval){
	$scope.tongjicount=0;
	$scope.platforms = [ {
		id : '123',
		name : '',
	}, {
		id : '123',
		name : '集成服务',
	}, {
		id : '123',
		name : '云平台',
	}, {
		id : '123',
		name : '管理平台',
	}, {
		id : '123',
		name : '云服务',
	}
	];
	
	$scope.versionDate="";
	
	$scope.activebase='active';
	$scope.active1=$scope.activebase;
	$scope.active2=!$scope.activebase;
	$scope.active3=!$scope.activebase;
	$scope.loglog = function(item){
		console.log(item);
	}
	$scope.projects=[
	                 {id:'1',platform_name:'',offlineproject:'100',offlineprojectRate:'active'},
	                 {id:'2',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'2',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'2',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'3',platform_name:'',offlineproject:'20',offlineprojectRate:'disable'},
	                 {id:'4',platform_name:'',offlineproject:'10',offlineprojectRate:'disable'}
	                 ];
	$scope.lastactive=0;//保存上一个被选中的按钮
	$scope.active = function(index){
		$scope.items[$scope.lastactive].style='disable';
		$scope.items[index].style='active';
		$scope.lastactive=index;
	};
	$scope.edit = function(index){
//		console.log($scope.projects[index].code);
//		console.log($scope.projects[index].event_time);
		$state.go('OnlineProjectDetail',{id: $scope.projects[index].id});
	};
	$scope.menus=['项目名称','接入日期','所属系统'];
	$scope.sortBy = function(menu){
		console.log(menu);
	};
	$scope.counts=0;
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
	$scope.componentId="";
	$scope.principal="";
	$scope.hasDbScript=false;
	$scope.hasParaFile=false;
	$http.post("report/onlinereport/platformlist.do").success(function(response) {
		var resp =angular.fromJson(angular.fromJson(response));
		console.log(resp);
		var returnCode = resp.returnCode;
		var message = resp.message;
		$scope.platforms = resp.result;
		$scope.platforms.splice(0, 0, {id : '',name : ''});
//	  $scope.platforms.push({id : '',name : ''});
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
		$http.post("report/onlinereport/actualTimeOnlineReportDetail.do?platformid="+
				$scope.platformid.id).success(function(response) {
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
					if($scope.counts==0){
						alert('查询条件有误，无数据');
					}
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
		$http.post("report/onlinereport/actualTimeOnlineReportDetail.do?&platformid="+
				$scope.platformid.id).success(function(response) {
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
					if($scope.counts==0){
						alert('查询条件有误，无数据');
					}
				});
	};
	$http.post("report/onlinereport/actualTimeOnlineReportDetail.do?platformid="+$stateParams.id).success(function(response) {
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
		if($scope.counts==0){
			alert('查询条件有误，无数据');
		}
	});
	
	$scope.account="";
	$scope.password="";
	$scope.account="";
	
	$scope.platformid="";
	$scope.projectname="";
	$scope.projectcode="";
	
	$scope.query= function(){
		$scope.msg="正在轮询.第"+$scope.tongjicount+"次";
		$scope.timeout_upd = $interval(function () {
			$http.post("report/onlinereport/actualTimeOnlineReportDetail.do?platformid="+
					$scope.platformid.id).success(function(response) {
						$scope.tongjicount++;
						$scope.msg="正在轮询.第"+$scope.tongjicount+"次";
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
						if($scope.counts==0){
		    				  $interval.cancel($scope.timeout_upd);  
		    				  $scope.msg="";
		    				  alert('查询条件有误，无数据');
						}
					});
		}, 5000);//每一秒更新一次theTime
	}
	
	$scope.$on('$destroy', function(){
		$interval.cancel($scope.timeout_upd);  
	});
	
	$scope.exportOffline = function(){
		$http.post("report/onlinereport/exportOnlineProjects.do").success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
			if(resp.url!=""){
				window.location.href = ""+resp.url;
			}
//			window.open("111.xls"); 
		});
	}
}])
