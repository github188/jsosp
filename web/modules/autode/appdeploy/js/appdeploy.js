
app.controller('AutomationCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	console.log($stateParams);
	 /*$scope.services = [
        {
          name:'jsifs',
          label: '集成服务',
          ips: [
            '10.10.201.1','10.10.201.1','10.10.201.1','10.10.201.1'
          ]
        },
        {
          name:'jsstApp',
          label: '云平台',
          ips: [
            '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
          ]
        },
	{
          name:'jsis',
          label: '管理平台',
          ips: [
            '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
          ]
        },
        {
          name:'jscsp',
          label: '云服务',
          ips: [
            '10.10.203.1','10.10.203.1','10.10.203.1','10.10.203.1'
          ]
        }
    ];*/
    $scope.dt=new Date();
    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
//    $scope.ip='';
//    $scope.address='';
    $scope.logs='';
    var phoneNumberRegex = /^\d+\.\d+\.\d+\.\d+(,\d+\.\d+\.\d+\.\d+)*$/;
    $scope.phoneNumberPattern = phoneNumberRegex;
    $scope.service=$stateParams.label;
	$scope.ip=$stateParams.ip;
	$scope.address=$stateParams.path.replace(/\./g,'/');
    $scope.operate = function () {
    	$scope.logs="";
        $scope.result="部署中";
    	var date=$filter('date')($scope.dt,'yyyyMMdd');
    	$scope.data={'service': $stateParams.service, 'ip': $scope.ip,'date':date,'address':$scope.address}
    	$http.post("jsosp/automation.do?service="+ $stateParams.service+"&ip="+$scope.ip+"&date="+date+"&address="+$scope.address).success(function(response) {
    		var resp =angular.fromJson(angular.fromJson(response));
    		console.log(resp);
    		console.log(resp.params.result);
    		var result=resp.params.result;
		console.log(resp.params.history);
		$scope.logs='历史版本信息：'+'\n'+resp.params.history;
    		if(result=="success"){
    			$scope.result="成功";
    		}else{
    			$scope.result="失败";
    		}
   		});
    };
    $scope.operateout = function () {
    	$scope.logs="";
    	$scope.result="部署中";
    	var date=$filter('date')($scope.dt,'yyyyMMdd');
    	$scope.data={'service': $stateParams.service, 'ip': $scope.ip,'date':date,'address':$scope.address}
    	$http.post("jsosp/automation.do?service=jsifs.out"+"&ip="+$scope.ip+"&date="+date+"&address="+$scope.address).success(function(response) {
    		var resp =angular.fromJson(angular.fromJson(response));
    		console.log(resp);
    		console.log(resp.params.result);
    		var result=resp.params.result;
    		console.log(resp.params.history);
    		$scope.logs='历史版本信息：'+'\n'+resp.params.history;
    		if(result=="success"){
    			$scope.result="成功";
    		}else{
    			$scope.result="失败";
    		}
    	});
    };
    $scope.queryversion = function () {
    	$scope.logs="";
    	$scope.result1="正在查询";
    	$http.post("jsosp/queryversion.do?ip="+ $scope.ip).success(function(response) {
    		var resp =angular.fromJson(angular.fromJson(response));
    		console.log(resp);
    		console.log(resp.params.result);
    		var result=resp.params.result;
    		console.log(resp.params.history);
    		$scope.logs='历史版本信息：'+'\n'+resp.params.history;
    		if(result=="success"){
    			$scope.result1="查询成功";
    		}else{
    			$scope.result1="查询失败";
    		}
    	});
    };
}])
app.controller('BackUpCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	 $scope.services = [
            {
              name:'jsstApp',
              label: '云平台',
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
    	{
              name:'jsis',
              label: '管理平台',
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
        ];
	$scope.dt=new Date();
	$scope.openCalendar = function ($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};
	$scope.ip='';
	$scope.address='';
	$scope.logs='';
	var phoneNumberRegex = /^\d+\.\d+\.\d+\.\d+(,\d+\.\d+\.\d+\.\d+)*$/;
	$scope.phoneNumberPattern = phoneNumberRegex;
	$scope.operate = function () {
		$scope.result="正在回滚";
		var date=$filter('date')($scope.dt,'yyyyMMdd');
		$scope.data={ 'ip': $scope.ip,'date':date}
		$http.post("jsosp/backup.do?ip="+$scope.ip+"&date="+date+"&service="+$scope.service.name).success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
    		console.log(resp.params.result);
    		var result=resp.params.result;
			console.log(resp.params.history);
			$scope.logs='历史版本信息：'+'\n'+resp.params.history;
    		if(result=="success"){
    			$scope.result="回滚成功";
    		}else{
    			$scope.result="回滚失败";
    		}
	   	});
	};
}])

app.controller('RestartCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	 $scope.services = [
            {
              name:'jsstApp',
              label: '云平台',
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
    	{
              name:'jsis',
              label: '管理平台',
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
        ];
	$scope.dt=new Date();
	$scope.openCalendar = function ($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};
	$scope.ip='';
	$scope.address='';
	$scope.logs='';
	var phoneNumberRegex = /^\d+\.\d+\.\d+\.\d+(,\d+\.\d+\.\d+\.\d+)*$/;
	$scope.phoneNumberPattern = phoneNumberRegex;
	$scope.operate = function () {
		$scope.result="正在重启";
		var date=$filter('date')($scope.dt,'yyyy-MM-dd-HH-mm-ss');
		$scope.data={ 'ip': $scope.ip,'date':date}
		$http.post("jsosp/restart.do?ip="+$scope.ip+"&date="+date+"&service="+$scope.service.name).success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
    		console.log(resp.params.result);
    		var result=resp.params.result;
			console.log(resp.params.history);
			$scope.logs='历史版本信息：'+'\n'+resp.params.history;
    		if(result=="success"){
    			$scope.result="任务已计划";
    		}else{
    			$scope.result="任务已计划";
    		}
	   	});
	};
}])

app.controller('MachineCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	$scope.ip='';
	$scope.address='';
	$scope.logs='';
	var phoneNumberRegex = /^\d+\.\d+\.\d+\.\d+(,\d+\.\d+\.\d+\.\d+)*$/;
	$scope.phoneNumberPattern = phoneNumberRegex;
	$scope.operate = function () {
		$scope.result="正在获取";
		$scope.data={ 'ip': $scope.ip}
		$http.post("jsosp/machine.do?ip="+$scope.ip).success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
			console.log(resp.params.result);
			var result=resp.params.result;
			console.log(resp.params.history);
			$scope.logs='历史版本信息：'+'\n'+resp.params.history;
			if(result=="success"){
				$scope.result="获取成功";
			}else{
				$scope.result="获取成功";
			}
		});
	};
}])

app.controller('CheckPropertiesCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	 $scope.services = [
            {
              name:'jsifs',
              label: '集成服务',
              file:"app.properties,jsifs.ini",
              ips: [
                '10.10.201.1','10.10.201.1','10.10.201.1','10.10.201.1'
              ]
            },
            {
              name:'jsstApp',
              label: '云平台管理系统',
              file:"application.properties,hibernate.properties,jobConfig.properties,wx_url.properties,xmpp.properties",
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
            {
            	name:'jsaims',
            	label: '云平台通讯系统',
            	file:"config.properties,xmpp.properties,pay_wx.properties,pay_zfb.properties,specialScan.properties,wandaConfig.properties,hibernate.properties,redisClient.properties",
            	ips: [
            	      '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
            	]
            },
            {
              name:'jsis',
              label: '管理平台',
              file:"application.properties,xmpp.properties,redisClient.properties",
              ips: [
                '10.10.202.1','10.10.202.1','10.10.202.1','10.10.202.1'
              ]
            },
            {
              name:'jscsp',
              label: '云服务',
              file:"cloud.properties,eclipse.ini,redis.properties",
              ips: [
                '10.10.203.1','10.10.203.1','10.10.203.1','10.10.203.1'
              ]
            }
        ];
    $scope.ip='';
    $scope.path='';
    $scope.file='';
    $scope.logs='';
    $scope.params2=[];
//    var phoneNumberRegex = /^\d+\.\d+\.\d+\.\d+$/;
    var phoneNumberRegex = /^\d+\.\d+\.\d+\.\d+(,\d+\.\d+\.\d+\.\d+)*$/;
    $scope.phoneNumberPattern = phoneNumberRegex;
    $scope.operate = function () {
    	$scope.params2=[];
        $scope.result="正在对比";
        $scope.logs='对比结果：'
        for (var i = 0; i < $scope.ip.split(',').length; i++) {
        	$http.post("jsosp/checkproperties.do?service="+ $scope.service.name+"&ip="+$scope.ip.split(',')[i]+"&path="+$scope.path+"&file="+$scope.service.file).success(function(response) {
        		var resp =angular.fromJson(angular.fromJson(response.replace('false','×').replace('true','√')));
        		$scope.params2.push(resp.params2);
        		console.log(resp);
        		console.log(resp.params.result);
        		var result=resp.params.result;
        		console.log(resp.params.history);
        		$scope.logs=$scope.logs+'\n\n'+resp.params.history.replace(/;/g,'\n');
//        		if(result=="success"){
//        		}else{
//        			$scope.result="对比失败";
//        		}
        	});
		};
		$scope.result="对比成功";
    };
    $scope.next = function(){
    	$state.go('Automation',{service: $scope.service.name,ip:$scope.ip,label:$scope.service.label,path:$scope.path.replace(/\//g,'.')});
    }
}])

app.controller('QueryHistoryDeployedCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
	$scope.dt=new Date();
	$scope.dt.setDate(new Date().getDate()-7);
	$scope.openCalendar = function ($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};
	$scope.dt1=new Date();
	$scope.openCalendar1 = function ($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened1 = true;
	};
	$scope.historys = [];
	$scope.operate = function () {
    	$http.post("jsosp/queryhistorydeployed.do?starttime="+ $filter('date')($scope.dt,'yyyy-MM-dd')+"&endtime="+$filter('date')($scope.dt1,'yyyy-MM-dd')).success(function(response) {
    		var resp =angular.fromJson(angular.fromJson(response));
    		console.log(resp);
    		$scope.historys=resp;
    	});
    };
}])