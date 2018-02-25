app.controller('PackagesListCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	 $scope.services = [
				{
				    name:'',
				    label: '',
				    file:"app.properties,jdbc.properties",
				    ips: [
				      '10.10.201.1','10.10.201.1','10.10.201.1','10.10.201.1'
				    ]
				 },
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
  $scope.versionDate="";

  $scope.openCalendar = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
  }
  $scope.activebase='active';
  $scope.active1=$scope.activebase;
  $scope.active2=!$scope.activebase;
  $scope.active3=!$scope.activebase;
  $scope.loglog = function(item){
    console.log(item);
  }
  $scope.packages=[
      {componentId:'1',versionDate:'100',principal:'active',hasDbScript:'1',hasParaFile:'123',createTime:'12'},
      {componentId:'2',versionDate:'20',principal:'disable',hasDbScript:'',hasParaFile:'',createTime:''},
      {componentId:'2',versionDate:'20',principal:'disable',hasDbScript:'',hasParaFile:'',createTime:''},
      {componentId:'2',versionDate:'20',principal:'disable',hasDbScript:'',hasParaFile:'',createTime:''},
      {componentId:'3',versionDate:'20',principal:'disable',hasDbScript:'',hasParaFile:'',createTime:''},
      {componentId:'4',versionDate:'10',principal:'disable',hasDbScript:'',hasParaFile:'',createTime:''}
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
  $http.post("master/softwarepackage/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize+"&componentId="+$scope.componentId.name
		  +"&versionDate="+$filter('date')($scope.versionDate,'yyyyMMdd')+"&principal="+$scope.principal
		  +"&hasDbScript="+$scope.hasDbScript+"&hasParaFile="+$scope.hasParaFile).success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.userscount=resp.packageCounts;
	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.packages = resp.result;
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
    $http.post("master/softwarepackage/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&componentId="+$scope.componentId.name
  		  +"&versionDate="+$filter('date')($scope.versionDate,'yyyyMMdd')+"&principal="+$scope.principal
  		  +"&hasDbScript="+$scope.hasDbScript+"&hasParaFile="+$scope.hasParaFile).success(function(response) {
  	  var resp =angular.fromJson(angular.fromJson(response));
  	  console.log(resp);
  	  var returnCode = resp.returnCode;
  	  var message = resp.message;
  	  $scope.userscount=resp.packageCounts;
  	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
  	  if($scope.allpages<=5){
  		  $scope.rightpagestatus=$scope.status1;
  	  }
  	  $scope.packages = resp.result;
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
    $http.post("master/softwarepackage/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&componentId="+$scope.componentId.name
    		  +"&versionDate="+$filter('date')($scope.versionDate,'yyyyMMdd')+"&principal="+$scope.principal
    		  +"&hasDbScript="+$scope.hasDbScript+"&hasParaFile="+$scope.hasParaFile).success(function(response) {
    	  var resp =angular.fromJson(angular.fromJson(response));
    	  console.log(resp);
    	  var returnCode = resp.returnCode;
    	  var message = resp.message;
    	  $scope.userscount=resp.packageCounts;
    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
    	  if($scope.allpages<=5){
    		  $scope.rightpagestatus=$scope.status1;
    	  }
    	  $scope.packages = resp.result;
      });
  };
  $scope.account="";
  $scope.password="";
  $scope.account="";
  $scope.query= function(){
	    $http.post("master/softwarepackage/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize+"&componentId="+$scope.componentId.name
	    		  +"&versionDate="+$filter('date')($scope.versionDate,'yyyyMMdd')+"&principal="+$scope.principal
	    		  +"&hasDbScript="+$scope.hasDbScript+"&hasParaFile="+$scope.hasParaFile).success(function(response) {
	    	  var resp =angular.fromJson(angular.fromJson(response));
	    	  console.log(resp);
	    	  var returnCode = resp.returnCode;
	    	  var message = resp.message;
	    	  $scope.userscount=resp.packageCounts;
	    	  $scope.allpages=Math.ceil($scope.userscount/$scope.pageSize);
	    	  if($scope.allpages<=5){
	    		  $scope.rightpagestatus=$scope.status1;
	    	  }
	    	  $scope.packages = resp.result;
	      });
  }
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.packages.length;i++){
	  		if(i==index){
	  			$scope.packages[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.packages[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.packages.length;i++){
	  		$scope.packages[i].style="";
	  	}
	  }
}])

app.controller('PackageAddCtrl', ['$scope','Upload','$filter', function($scope,Upload,$filter){
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
	$scope.componentId="";
	$scope.principal="";
	$scope.releaseNote="";
	$scope.dbScript="";
	$scope.paraFile="";
	$scope.componentFile="";
	$scope.remark="";
  $scope.versionDate=new Date();

  $scope.openCalendar = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
  }
  $scope.lefile1name="";
/*  $scope.lefile2name="";
  $scope.lefile3name="";*/
  $scope.changelefile1 = function ($event) {
	  $scope.lefile1name="";
//	 alert($scope.lefile1.name);
     for (var i = $scope.lefile1.length - 1; i >= 0; i--) {
         $scope.lefile1name+=$scope.lefile1[i].name;
       };
       console.log($scope.lefile1name);
  }
/*  $scope.changelefile2 = function ($event) {
	  console.log($scope.lefile2);
	  $scope.lefile2name="";
//	 alert($scope.lefile1.name);
	  for (var i = $scope.lefile2.length - 1; i >= 0; i--) {
		  $scope.lefile2name+=$scope.lefile2[i].name;
	  };
	  console.log($scope.lefile2name);
  }
  $scope.changelefile3 = function ($event) {
	  $scope.lefile3name="";
	  $scope.lefile3name=$scope.lefile3.name;
	  console.log($scope.lefile3name);
  }*/

/*  $scope.uploadlefile1=function(file){
	  console.log(file);
	  Upload.upload({
	          //服务端接收
	          url: 'master/softwarepackage/add.do',
	          //上传的同时带的参数
	          data: {'componentId': $scope.componentId,'versionDate': $scope.versionDate,
	        	  'principal': $scope.principal,'releaseNote': $scope.releaseNote,'remark': $scope.remark},
	          //上传的文件
	          file: file
	      }).progress(function (evt) {
	          //进度条
	          var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
	          //console.log('progess:' + progressPercentage + '%' + evt.config.file.name);
	      }).success(function (data, status, headers, config) {
	          //上传成功
	          console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
	         // $scope.uploadImg = data;
	      }).error(function (data, status, headers, config) {
	          //上传失败
	          //console.log('error status: ' + status);
	      });
  };*/

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
   $scope.role="";
  $scope.save= function (){
	  console.log($scope.versionDate);
	  var date=$filter('date')($scope.versionDate,'yyyyMMdd');
	  Upload.upload({
	          //服务端接收
	          url: 'master/softwarepackage/add.do',
	          //上传的同时带的参数
	          data: {'componentId': $scope.componentId.name,'versionDate': date,
	        	  'principal': $scope.principal,'releaseNote': $scope.releaseNote,'remark': $scope.remark},
	          //上传的文件
	          file: $scope.lefile1
	      }).progress(function (evt) {
	          //进度条
	          var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
	          //console.log('progess:' + progressPercentage + '%' + evt.config.file.name);
	      }).success(function (data, status, headers, config) {
	          //上传成功
	    	  var resp =angular.fromJson(angular.fromJson(data));
	    	  alert(resp.message);
	         // $scope.uploadImg = data;
	      }).error(function (data, status, headers, config) {
	          //上传失败
	          //console.log('error status: ' + status);
	      });
  };

}])