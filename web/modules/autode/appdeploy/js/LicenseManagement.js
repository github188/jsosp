app.controller('LicenseManagementCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	$scope.services = [
            {
              name:'jsifs',
              label: '集成服务',
              file:"app.properties,jdbc.properties",
              ips: [
                '10.10.201.1','10.10.201.1','10.10.201.1','10.10.201.1'
              ]
            },
            {
              name:'jsstApp',
              label: '云平台',
              file:"application.properties,hibernate.properties,jdbc.properties,jobConfig.properties,wx_url.properties,xmpp.properties",
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
            {
              name:'jsis',
              label: '管理平台',
              file:"application.properties,jdbc.properties,xmpp.properties",
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
            {
              name:'jscsp',
              label: '云服务',
              file:"cloud.properties,jdbc.properties",
              ips: [
                '10.10.203.1','10.10.203.1','10.10.203.1','10.10.203.1'
              ]
            }
        ];
    $scope.dt=new Date();
    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    
	$scope.isDeleted=false;
	$scope.platforms = [
            {
              name:'租用平台',
              code: 'zuyong',
            },
            {
            	name:'扫码平台',
            	code: 'jssm',
            },
            {
            	name:'全生态',
            	code: 'qst',
            },
            {
            	name:'展厅',
            	code: 'jszt',
            },
            {
            	name:'万科物业',
            	code: 'wanke',
            }
        ];
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
  $scope.licenses = [
      {id:'1',project:'蓝光',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true,project_type:"jsis",telephone:"13641451355"},
      {id:'2',project:'华侨城',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true,project_type:"jsis",telephone:"13641451355"},
      {id:'3',project:'创泰',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true,project_type:"jsis",telephone:"13641451355"},
      {id:'4',project:'龙湖',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true,project_type:"jsis",telephone:"13641451355"},
      {id:'5',project:'凯德',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true,project_type:"jsis",telephone:"13641451355"},
      {id:'6',project:'嘉宝',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:false,project_type:"jsis",telephone:"13641451355"}
    ];
  $scope.licensecounts=10;
  $scope.pageSize=6;
  $scope.currentpage=0;
  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
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
  $http.post("auto/license/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.licensecounts=resp.licensecounts;
			  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.licenses = resp.result;
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

    $http.post("auto/license/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
    	 var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.licensecounts=resp.licensecounts;
		  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.licenses = resp.result;
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

    $http.post("auto/license/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
    	 var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.licensecounts=resp.licensecounts;
		  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.licenses = resp.result;
	  });

  };
  
  $scope.save=function(){
	  var date=$filter('date')($scope.dt,'yyyyMMdd');
	  $scope.dt1=angular.copy($scope.dt);
	  $scope.dt1.setDate($scope.dt.getDate()+$scope.duetime*30);
	  var date1=$filter('date')($scope.dt1,'yyyyMMdd');
	  $http.post("auto/license/add.do?project="+ $scope.project +"&licensestartTime="+date+"&licenseendTime="+date1+"&project_type="+$scope.project_type.name+"&telephone="+$scope.tels+"&duetime="+$scope.duetime+"&ips="+$scope.ips).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  alert(message);
		  $http.post("auto/license/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.licensecounts=resp.licensecounts;
			  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.licenses = resp.result;
		  });
	  });
  };
  
  $scope.isenable=function(index){
	  console.log($scope.licenses);
	  var licensesbak= angular.copy($scope.licenses);
	  licensesbak[index].enable=!$scope.licenses[index].enable;
	  $scope.licenses= licensesbak;
	  console.log(licensesbak);
	  console.log($scope.licenses);
	  
//	  $scope.licenses = [
//	                     {id:'1',project:'蓝光',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:false},
//	                     {id:'2',project:'华侨城',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true},
//	                     {id:'3',project:'创泰',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true},
//	                     {id:'4',project:'龙湖',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true},
//	                     {id:'5',project:'凯德',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:true},
//	                     {id:'6',project:'嘉宝',licensestartTime:'2016-5-12',licenseendTime:'2016-5-12',enable:false}
//	                   ];
  };
  
  $scope.message=function(index){
	  $scope.licenseid =$scope.licenses[index].id;
	  $http.post("auto/license/queryById.do?id="+ $scope.licenses[index].id).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.infoproject=resp.result[0].project;
		  for(var i=0;i<$scope.services.length;i++){
			  if($scope.services[i].name==resp.result[0].project_type){
				  $scope.infoproject_type=$scope.services[i];
				  break;
			  }
		  }
		  $scope.infodt=new Date(resp.result[0].licensestartTime);
		  $scope.infoduetime=resp.result[0].duetime;
		  $scope.infotels=resp.result[0].telephone;
		  $scope.infoip=resp.result[0].ip;
	  });
  };
  
  $scope.operate=function(){
    $http.post("auto/license/querylicenses.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&ip="+$scope.ip+"&projectname="+$scope.projectname).success(function(response) {
    	 var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.licensecounts=resp.licensecounts;
		  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.licenses = resp.result;
	  });
  };
  
  $scope.updateInfo=function(){
	  var date=$filter('date')($scope.infodt,'yyyyMMdd');
	  $scope.dt1=angular.copy($scope.infodt);
	  $scope.dt1.setDate($scope.infodt.getDate()+$scope.infoduetime*30);
	  var date1=$filter('date')($scope.dt1,'yyyyMMdd');
	  
	  $http.post("auto/license/updateById.do?id="+ $scope.licenseid+"&project="+$scope.infoproject+"&project_type="+$scope.infoproject_type.name
			  +"&licensestartTime="+date+"&licenseendTime="+date1+"&telephone="+$scope.infotels+"&duetime="+$scope.infoduetime+"&ip="+$scope.infoip).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		 /* $scope.infoproject=resp.result[0].project;
		  for(var i=0;i<$scope.services.length;i++){
			  if($scope.services[i].name==resp.result[0].project_type){
				  $scope.infoproject_type=$scope.services[i];
				  break;
			  }
		  }
		  $scope.infodt=resp.result[0].licensestartTime;
		  $scope.infoduetime=resp.result[0].duetime;
		  $scope.infotels=resp.result[0].telephone;*/
		  alert(message);
		  $http.post("auto/license/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.licensecounts=resp.licensecounts;
			  $scope.allpages=Math.ceil($scope.licensecounts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.licenses = resp.result;
		  });
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.licenses.length;i++){
	  		if(i==index){
	  			$scope.licenses[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.licenses[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.licenses.length;i++){
	  		$scope.licenses[i].style="";
	  	}
	  }
}])