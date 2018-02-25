var app = angular.module('jsywp', ['ui.bootstrap','ui.router','chart.js','ngFileUpload','ngCookies','ng-echarts']);

app.controller('indexCtrl', ['$scope','$rootScope','$http','$cookieStore','$state' ,'$timeout',function($scope,$rootScope,$http,$cookieStore,$state,$timeout){
	 $timeout(function () {
		 $scope.one=$cookieStore.get('one');
		 $scope.two=$cookieStore.get('two');
		 $scope.three=$cookieStore.get('three');
		 $scope.four=$cookieStore.get('four');
		 $scope.five=$cookieStore.get('five');
	}, 100);//延时2s后执行
	
	if($cookieStore.get('signin')!=true){
		$scope.main=  'ng-hide';
		$scope.signin='ng-show';
	}else{
		$scope.main=  'ng-show';
		$scope.signin='ng-hide';
	}
	console.log('1231231111111');
	$scope.data={'name': '擦', 'age': '28'}//测试Django后台是否可以读取数据
	/*$http.post("/index/",{params:{"data": $scope.data}}).success(function(response) {
		console.log(response);
		$rootScope.subsystems = response;
		for (var i = 0; i < $rootScope.subsystems.length; i++) {
			$rootScope.systems.push($rootScope.subsystems[i].label);
		};
		console.log($rootScope.subsystems)
		console.log("123"+$rootScope.systems);
	});*/
    $rootScope.systems=[];//子系统
    $rootScope.subsystems = [];//子系统+分公司
	$scope.status = {
		closeothers : true,
		isoneopen : true ,
		isonedisable : false
	}
	$http.post("security/role/list.do?pageIndex="+ 0 +"&pageSize="+0).success(function(response) {
		var resp =angular.fromJson(angular.fromJson(response));
		console.log(resp);
		var returnCode = resp.returnCode;
		var message = resp.message;
		$rootScope.roleslist=[];
		for (var i = 0; i < resp.result.length; i++) {
			var x={};
			x.id=resp.result[i].id;
			x.roleName=resp.result[i].roleName;
			$rootScope.roleslist.push(x);
		};

		console.log($scope.roleslist);
//	      $('#selectmul').selectpicker('refresh');
//	      $('#selectmul').selectpicker('render');
	});
	
	$http.post("security/user/list.do?pageIndex="+ 0 +"&pageSize="+0).success(function(response) {
		var resp =angular.fromJson(angular.fromJson(response));
		console.log(resp);
		var returnCode = resp.returnCode;
		var message = resp.message;
		$rootScope.userslist = resp.result;
		console.log($scope.userslist);
//	      $('#selectmul').selectpicker('refresh');
//	      $('#selectmul').selectpicker('render');
	});
	$scope.account="";
	$scope.passwd="";
	$scope.login=function(){
		if($scope.account=="admin"){
			$cookieStore.put('one', 'ng-show');
			$cookieStore.put('two', 'ng-show');
			$cookieStore.put('three', 'ng-show');
			$cookieStore.put('four', 'ng-show');
			$cookieStore.put('five', 'ng-show');
			$scope.one='ng-show';
			$scope.two='ng-show';
			$scope.three='ng-show';
			$scope.four='ng-show';
			$scope.five='ng-show';
			$state.go('CardMappedMonitor');
		}else{
			$cookieStore.put('one', 'ng-hide');
			$cookieStore.put('two', 'ng-hide');
			$cookieStore.put('three', 'ng-hide');
			$cookieStore.put('four', 'ng-hide');
			$cookieStore.put('five', 'ng-show');
			$scope.one='ng-hide';
			$scope.two='ng-hide';
			$scope.three='ng-hide';
			$scope.four='ng-hide';
			$scope.five='ng-show';
			$state.go('CardMappedMonitor');
		}
		$http.post("security/user/login.do?account="+ $scope.account +"&passwd="+$scope.passwd).success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
			var returnCode = resp.returnCode;
			if(returnCode==0){
				$scope.main=  'ng-show';
				$scope.signin='ng-hide';
				$cookieStore.put('signin', true);
				$cookieStore.put('account', $scope.account);
			}else{
				alert(resp.message);
			}
		});

	}
	
	$scope.signout=function(){
		$scope.main=  'ng-hide';
		$scope.signin='ng-show';
		$cookieStore.put('signin', false);
		window.location.href = "";
	}
	
	$scope.myKeyup = function(e){
		var keycode = window.event?e.keyCode:e.which; 
	        if(keycode==13){
	    		if($scope.account=="admin"){
	    			$cookieStore.put('one', 'ng-show');
	    			$cookieStore.put('two', 'ng-show');
	    			$cookieStore.put('three', 'ng-show');
	    			$cookieStore.put('four', 'ng-show');
	    			$cookieStore.put('five', 'ng-show');
	    			$scope.one='ng-show';
	    			$scope.two='ng-show';
	    			$scope.three='ng-show';
	    			$scope.four='ng-show';
	    			$scope.five='ng-show';
	    			$state.go('CardMappedMonitor');
	    		}else{
	    			$cookieStore.put('one', 'ng-hide');
	    			$cookieStore.put('two', 'ng-hide');
	    			$cookieStore.put('three', 'ng-hide');
	    			$cookieStore.put('four', 'ng-hide');
	    			$cookieStore.put('five', 'ng-show');
	    			$scope.one='ng-hide';
	    			$scope.two='ng-hide';
	    			$scope.three='ng-hide';
	    			$scope.four='ng-hide';
	    			$scope.five='ng-show';
	    			$state.go('CardMappedMonitor');
	    		}
	    		$http.post("security/user/login.do?account="+ $scope.account +"&passwd="+$scope.passwd).success(function(response) {
	    			var resp =angular.fromJson(angular.fromJson(response));
	    			console.log(resp);
	    			var returnCode = resp.returnCode;
	    			if(returnCode==0){
	    				$scope.main=  'ng-show';
	    				$scope.signin='ng-hide';
	    				$cookieStore.put('signin', true);
	    			}else{
	    				alert(resp.message);
	    			}
	    		});
	        }
	};
		
}]);

app.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/CardMappedMonitor');
    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'templates/project/ProjectOnlineState.html',
            controller: 'ProjectOnlineStateCtrl'
        })
    .state('ProjectList', {
            url: '/ProjectList',
            templateUrl: 'templates/project/ProjectList.html',
            controller : 'ProjectListCtrl'
        })
    .state('ProjectInfo',{
        url:'/ProjectInfo/id=:id',
        templateUrl:'templates/project/ProjectInfo.html',
        controller :'ProjectInfoCtrl'
    })
    .state('ProjectOnlineList', {
            url: '/ProjectOnlineList',
            templateUrl: 'templates/project/ProjectOnlineList.html',
            controller :'ProjectOnlineListCtrl'
        })
    .state('ProjectOnlineEdit',{
        url:'/ProjectOnlineEdit/id=:id',
        templateUrl:'templates/project/ProjectOnlineEdit.html',
        controller :'ProjectOnlineEditCtrl'
    })
    .state('ProjectOnlineAdd',{
        url:'/ProjectOnlineAdd',
        templateUrl:'templates/project/ProjectOnlineAdd.html',
        controller :'ProjectOnlineAddCtrl'
    })
    .state('ProjectOnlineState', {
            url: '/ProjectOnlineState',
            templateUrl: 'templates/project/ProjectOnlineState.html',
            controller: 'ProjectOnlineStateCtrl'
        })
    .state('APICustomerList', {
            url: '/APICustomerList',
            templateUrl: 'templates/apilink/APICustomerList.html',
            controller : 'APICustomerListCtrl'
        })
    .state('APICustomerEdit',{
        url:'/APICustomerEdit/id=:id',
        templateUrl:'templates/apilink/APICustomerEdit.html',
        controller : 'APICustomerEditCtrl'
    })
    .state('APICustomerAdd',{
        url:'/APICustomerAdd',
        templateUrl:'templates/apilink/APICustomerAdd.html',
        controller : 'APICustomerAddCtrl'
    })
    .state('APIProjectList', {
            url: '/APIProjectList',
            templateUrl: 'templates/apilink/APIProjectList.html',
            controller : 'APIProjectListCtrl'
        })
    .state('APIProjectInfo',{
        url:'/APIProjectInfo/id=:id',
        templateUrl:'templates/apilink/APIProjectInfo.html',
        controller : 'APIProjectInfoCtrl'
    })
    .state('APIProjectAdd',{
        url:'/APIProjectAdd',
        templateUrl:'templates/apilink/APIProjectAdd.html',
        controller : 'APIProjectAddCtrl'
    })
    .state('APIProjectState', {
            url: '/APIProjectState',
            templateUrl: 'templates/apilink/APIProjectState.html',
            controller:'APIProjectStateCtrl'
        })
    .state('APIList',{
        url: '/APIList',
        templateUrl: 'templates/apilink/APIList.html',
        controller : 'APIListCtrl'
    })
    .state('APIEdit',{
        url: '/APIEdit/id=:id',
        templateUrl: 'templates/apilink/APIEdit.html',
        controller : 'APIEditCtrl'
    })
    .state('APIAdd',{
        url: '/APIAdd',
        templateUrl: 'templates/apilink/APIAdd.html',
        controller : 'APIAddCtrl'
    })
    .state('Hosts',{
        url: '/Hosts',
        templateUrl: 'templates/automation/Hosts.html',
        controller : 'HostsCtrl'
    })
    .state('HostEdit',{
        url: '/HostEdit/id=:id',
        templateUrl: 'templates/automation/HostEdit.html',
        controller : 'HostEditCtrl'
    })
    .state('HostAdd',{
        url: '/HostAdd',
        templateUrl: 'templates/automation/HostAdd.html',
        controller : 'HostAddCtrl'
    })
    .state('Projects',{
        url: '/Projects',
        templateUrl: 'templates/automation/Projects.html',
        controller : 'ProjectsCtrl'
    })
    .state('ProjectsEdit',{
        url: '/ProjectsEdit/id=:id',
        templateUrl: 'templates/automation/ProjectsEdit.html',
        controller : 'ProjectsEditCtrl'
    })
    .state('ProjectsAdd',{
        url: '/ProjectsAdd',
        templateUrl: 'templates/automation/ProjectsAdd.html',
        controller : 'ProjectsAddCtrl'
    })
    .state('Services',{
        url: '/Services',
        templateUrl: 'templates/automation/Services.html',
        controller : 'ServicesCtrl'
    })
    .state('ServicesEdit',{
        url: '/ServicesEdit/id=:id',
        templateUrl: 'templates/automation/ServicesEdit.html',
        controller : 'ServicesEditCtrl'
    })
    .state('ServicesAdd',{
        url: '/ServicesAdd',
        templateUrl: 'templates/automation/ServicesAdd.html',
        controller : 'ServicesAddCtrl'
    })
    .state('Automation',{
        url: '/Automation/service=:service,ip=:ip,label=:label,path=:path',
        templateUrl: 'modules/autode/appdeploy/html/Automation.html',
        controller : 'AutomationCtrl'
    })
    .state('BackUp',{
    	url: '/BackUp',
    	templateUrl: 'modules/autode/appdeploy/html/BackUp.html',
    	controller : 'BackUpCtrl'
    })
    .state('Restart',{
    	url: '/Restart',
    	templateUrl: 'modules/autode/appdeploy/html/Restart.html',
    	controller : 'RestartCtrl'
    })
    .state('Machine',{
    	url: '/Machine',
    	templateUrl: 'modules/autode/appdeploy/html/Machine.html',
    	controller : 'MachineCtrl'
    })
    .state('CheckProperties',{
    	url: '/CheckProperties',
    	templateUrl: 'modules/autode/appdeploy/html/CheckProperties.html',
    	controller : 'CheckPropertiesCtrl'
    })
    .state('QueryHistoryDeployed',{
    	url: '/QueryHistoryDeployed',
    	templateUrl: 'modules/autode/appdeploy/html/QueryHistoryDeployed.html',
    	controller : 'QueryHistoryDeployedCtrl'
    })
    .state('UsersList',{
    	url: '/UsersList',
    	templateUrl: 'modules/security/user/html/UsersList.html',
    	controller : 'UsersListCtrl'
    })
    .state('UserAdd',{
    	url: '/UserAdd',
    	templateUrl: 'modules/security/user/html/UserAdd.html',
    	controller : 'UserAddCtrl'
    })
    .state('RolesList',{
    	url: '/RolesList',
    	templateUrl: 'modules/security/role/html/RolesList.html',
    	controller : 'RolesListCtrl'
    })
    .state('RoleAdd',{
        url: '/RoleAdd',
        templateUrl: 'modules/security/role/html/RoleAdd.html',
        controller : 'RoleAddCtrl'
    })
    .state('PackagesList',{
        url: '/PackagesList',
        templateUrl: 'modules/master/package/html/PackagesList.html',
        controller : 'PackagesListCtrl'
    })
    .state('PackageAdd',{
    	url: '/PackageAdd',
    	templateUrl: 'modules/master/package/html/PackageAdd.html',
    	controller : 'PackageAddCtrl'
    })
    .state('OnlinePersonList',{
    	url: '/OnlinePersonList',
    	templateUrl: 'modules/onlinemonitor/onlineperson/html/OnlinePersonList.html',
    	controller : 'OnlinePersonListCtrl'
    })
    .state('DataDelete',{
    	url: '/DataDelete',
    	templateUrl: 'modules/autode/appdeploy/html/DataDelete.html',
    	controller : 'DataDeleteCtrl'
    })
    .state('LicenseManagement',{
    	url: '/LicenseManagement',
    	templateUrl: 'modules/autode/appdeploy/html/LicenseManagement.html',
    	controller : 'LicenseManagementCtrl'
    })
    .state('OnlineReport',{
    	url: '/OnlineReport',
    	templateUrl: 'modules/report/onlineMonitor/html/OnlineReport.html',
    	controller : 'OnlineReportCtrl'
    })
    .state('OnlineProjectDetail',{
        url: '/OnlineProjectDetail/id=:id',
        templateUrl: 'modules/report/onlineMonitor/html/OnlineProjectDetail.html',
        controller : 'OnlineProjectDetailCtrl'
    })
    .state('OfflineHandleReport',{
    	url: '/OfflineHandleReport',
    	templateUrl: 'modules/report/onlineMonitor/html/OfflineHandleReport.html',
    	controller : 'OfflineHandleReportCtrl'
    })
    .state('CardMappedMonitor',{
    	url: '/CardMappedMonitor',
    	templateUrl: 'modules/onlinemonitor/elk/html/CardMappedMonitor.html',
    	controller : 'CardMappedMonitorCtrl'
    })
    .state('ActualTimeOnlineReport',{
    	url: '/ActualTimeOnlineReport',
    	templateUrl: 'modules/report/onlineMonitor/html/ActualTimeOnlineReport.html',
    	controller : 'ActualTimeOnlineReportCtrl'
    })
    .state('ActualTimeOnlineReportDetail',{
    	url: '/ActualTimeOnlineReportDetail/id=:id',
    	templateUrl: 'modules/report/onlineMonitor/html/ActualTimeOnlineReportDetail.html',
    	controller : 'ActualTimeOnlineReportDetailCtrl'
    })
    .state('RDSManage',{
    	url: '/RDSManage',
    	templateUrl: 'modules/autode/appdeploy/html/RDSManage.html',
    	controller : 'RDSManageCtrl'
    })
    .state('Project',{
    	url: '/Project',
    	templateUrl: 'modules/onlinemonitor/project/html/Project.html',
    	controller : 'ProjectCtrl'
    })
    .state('BigTableMonitor',{
    	url: '/BigTableMonitor',
    	templateUrl: 'modules/autode/appdeploy/html/BigTableMonitor.html',
    	controller : 'BigTableMonitorCtrl'
    })
    .state('DBRequest',{
    	url: '/DBRequest',
    	templateUrl: 'modules/autode/appdeploy/html/DBRequest.html',
    	controller : 'DBRequestCtrl'
    })
    .state('Middleware',{
    	url: '/Middleware',
    	templateUrl: 'modules/autoInspection/middleware/html/Middleware.html',
    	controller : 'MiddlewareCtrl'
    })
    .state('ApplicationService',{
    	url: '/ApplicationService',
    	templateUrl: 'modules/autoInspection/applicationService/html/ApplicationService.html',
    	controller : 'ApplicationServiceCtrl'
    })
    .state('Inspection',{
    	url: '/Inspection',
    	templateUrl: 'modules/autoInspection/inspection/html/Inspection.html',
    	controller : 'InspectionCtrl'
    })
    .state('Download',{
    	url: '/Download',
    	templateUrl: 'modules/autoInspection/download/html/Download.html',
    	controller : 'DownloadCtrl'
    })
    .state('RedisBigKeyMonitor',{
    	url: '/RedisBigKeyMonitor',
    	templateUrl: 'modules/autoInspection/redisBigKeyMonitor/html/RedisBigKeyMonitor.html',
    	controller : 'RedisBigKeyMonitorCtrl'
    })
    .state('RdsSlowLog',{
    	url: '/RdsSlowLog',
    	templateUrl: 'modules/autoInspection/rdsSlowLog/html/RdsSlowLog.html',
    	controller : 'RdsSlowLogCtrl'
    })
    .state('HttpCheck',{
    	url: '/HttpCheck',
    	templateUrl: 'modules/autoInspection/httpCheck/html/HttpCheck.html',
    	controller : 'HttpCheckCtrl'
    })
    .state('RedisResource',{
    	url: '/RedisResource',
    	templateUrl: 'modules/autoInspection/redisResource/html/RedisResource.html',
    	controller : 'RedisResourceCtrl'
    })
    .state('ApiData',{
    	url: '/ApiData',
    	templateUrl: 'modules/autoInspection/apiData/html/ApiData.html',
    	controller : 'ApiDataCtrl'
    })
    .state('Bgyinout',{
    	url: '/Bgyinout',
    	templateUrl: 'modules/autoInspection/bgyinout/html/bgyinout.html',
    	controller : 'BgyinoutCtrl'
    })
    .state('RdsSlowLogOneKey',{
    	url: '/RdsSlowLogOneKey',
    	templateUrl: 'modules/autoInspection/rdsSlowLogOneKey/html/rdsSlowLogOneKey.html',
    	controller : 'RdsSlowLogOneKeyCtrl'
    })
    .state('HttpCheckOneKey',{
    	url: '/HttpCheckOneKey',
    	templateUrl: 'modules/autoInspection/HttpCheckOneKey/html/HttpCheckOneKey.html',
    	controller : 'HttpCheckOneKeyCtrl'
    })
    .state('WankeUnsubmitSubsystem',{
    	url: '/WankeUnsubmitSubsystem',
    	templateUrl: 'modules/autoInspection/WankeUnsubmitSubsystem/html/WankeUnsubmitSubsystem.html',
    	controller : 'WankeUnsubmitSubsystemCtrl'
    })
    .state('NginxInfo',{
    	url: '/NginxInfo',
    	templateUrl: 'modules/autoInspection/nginx/html/nginxInfo.html',
    	controller : 'NginxInfoCtrl'
    })
    .state('NginxDetail',{
    	url: '/NginxDetail',
    	templateUrl: 'modules/autoInspection/nginx/html/nginxDetail.html',
    	controller : 'NginxDetailCtrl'
    })
    .state('Lockwait',{
    	url: '/Lockwait',
    	templateUrl: 'modules/autoInspection/lockwait/html/lockwait.html',
    	controller : 'LockwaitCtrl'
    })
    .state('SlowSql',{
    	url: '/SlowSql',
    	templateUrl: 'modules/autoInspection/lockwait/html/slowsql.html',
    	controller : 'SlowSqlCtrl'
    })
    .state('Discount',{
    	url: '/Discount',
    	templateUrl: 'modules/autoInspection/discount/html/Discount.html',
    	controller : 'DiscountCtrl'
    });
});
