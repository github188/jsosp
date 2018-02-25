app.controller('ProjectOnlineStateCtrl', ['$scope', '$rootScope','$http', function($scope,$rootScope,$http){
	$http.post("/chart1/").success(function(response) {
		console.log(response);
		for (var i = 0; i < response.length; i++) {
			$scope.systems1.push(response[i].label);
			$scope.dataall.push(response[i].data);
			$scope.totalcount=$scope.totalcount+response[i].data;
		};
	});
	$http.post("/chart2/").success(function(response) {
		console.log(response);
		for (var i = 0; i < response.length; i++) {
			$scope.systems2.push(response[i].label);
			$scope.dataonline.push(response[i].data);
			$scope.onlinecount=$scope.onlinecount+response[i].data;
		};
	});
	$http.post("/chart3/").success(function(response) {
		for(var i = 0; i < response[0].data.length; i++){
			$scope.dataoffline[i]=[];
			console.log(response[i].data[i].label);
			$scope.systems3.push(response[i].data[i].label);
		}
		for (var i = 0; i < response.length; i++) {
			$scope.labelsoffline.push(response[i].label);
			
			for(var j = 0; j < response[0].data.length; j++){
				$scope.dataoffline[j].push(response[i].data[j].data);
			}
			
		};
		
		for (var i = 0; i < response.length; i++) {
//			$scope.dataoffline[0].push(response[i].data);
		};
	});
	$http.post("/chart4/").success(function(response) {
		for (var i = 0; i < response.length; i++) {
			$scope.labels1.push(response[i].label);
			$scope.datatotal[0].push(response[i].data);
		};
	});
	$http.post("/chart5/").success(function(response) {
		for(var i = 0; i < response[0].data.length; i++){
			$scope.datacomming[i]=[];
		}
		for(var i = 0; i < response[0].data.length; i++){
			$scope.seriescomming2.push(response[0].data[i].label);
		}
		for (var i = 0; i < response.length; i++) {
			$scope.labels2.push(response[i].label);
			
			for(var j = 0; j < response[0].data.length; j++){
				$scope.datacomming[j].push(response[i].data[j].data);
			}
			
		};
	});
//	$http.post("/projectonlinestate/").success(function(response) {
//		console.log(response);
//		$scope.seriescomming=response.seriescomming;
//		$scope.datacomming=response.datacomming;
//		$scope.systems=response.systems;
//		$scope.dataall=response.dataall;
//		for (var i = 0; i < $scope.dataall.length; i++) {
//			$scope.totalcount = $scope.totalcount + $scope.dataall[i];
//		};
//		$scope.dataonline=response.dataonline;
//		for (var i = 0; i < $scope.dataonline.length; i++) {
//			$scope.onlinecount = $scope.onlinecount + $scope.dataonline[i];
//		};
//		$scope.labelsoffline=response.labelsoffline;
//		$scope.dataoffline=response.dataoffline;
//	});
	//$http.post("http://localhost:8086/archive")
	//	.success(function(response) {console.log(response);});
	$scope.labels1 = [];
	$scope.labels2= [];
	$scope.seriestotal = ['项目在线率'];
	$scope.datatotal = [[]];
	
	$scope.seriescomming1 = ["总在线率"];
	$scope.seriescomming2 = [];
	$scope.datacomming = [];
	$scope.onClick = function (points, evt) {
	  console.log(points, evt);
	};

	//$scope.coloursall=['#803690', '#00ADF9', '#DCDCDC'];
	/*$scope.labelsall = ["<p style='background-color:#803690'>一系统</p>",
	 					"<p style='background-color:#00ADF9'>二系统</p>",
	  					"<p style='background-color:#DCDCDC'>三系统</p>"];*/
	$scope.systems1 = [];
	$scope.systems2 = [];
	$scope.systems3 = [];
	$scope.dataall = [];
	$scope.totalcount=0;
 	$scope.dataonline = [];
 	$scope.onlinecount=0;
 	$scope.labelsoffline =[];
	$scope.dataoffline = [[]];
}])

app.controller('APIProjectStateCtrl', ['$scope', function($scope){
	$scope.apianalyselabels = ["January", "February", "March", "April", "May", "June", "July"];
	$scope.apianalyseseries = ['Series A', 'Series B'];
	$scope.apianalysedata = [
	  [65, 59, 80, 81, 56, 55, 40],
	  [28, 48, 40, 19, 86, 27, 90]
	];
	//$scope.apicolourscustomers=['#803690', '#00ADF9', '#DCDCDC'];
	/*$scope.labelsall = ["<p style='background-color:#803690'>一系统</p>",
	 					"<p style='background-color:#00ADF9'>二系统</p>",
	  					"<p style='background-color:#DCDCDC'>三系统</p>"];*/
	$scope.apicustomerslabels = ["一系统","二系统","三系统"];
	$scope.apicustomers = [300,50,100];

	$scope.apiprojectslabels = ["一系统", "二系统", "三系统"];
 	$scope.apiprojectsdata = [300, 50, 100];

 	$scope.apiexceptionlabels =["网络不通", "适配器关闭", "SDK服务停止", "客户不接入", "集成服务不在线", "适配器假死", "前端电脑当机"];
	$scope.apiexceptiondata = [
	  [65, 59, 90, 81, 56, 55, 40],
	  [28, 48, 40, 19, 96, 27, 100],
	  [43, 41, 35, 21, 41, 33, 90]
	];
}])

app.controller('ProjectListCtrl', ['$scope','$state','$http', function($scope,$state,$http){
	$http.post("/projectlist/",{params:{"page": 1,"count":8,"type":"TOTAL"}}).success(function(response) {
		$scope.allpages=response.allpages;
		if($scope.allpages<5){
			console.log($scope.allpages);
			$scope.pages.push({index:0,status:'active'});
			for (var i = 0; i < $scope.allpages-1; i++) {
				$scope.pages.push({index:i+1,status:'enable'});
			};
		}else{
			$scope.pages=[
	  			{index:0,status:'active'},
	  			{index:1,status:'enable'},
	  			{index:2,status:'enable'},
	  			{index:3,status:'enable'},
	  			{index:4,status:'enable'}
	  		];
		}
		//console.log(response);
		$scope.projectList=response.projectList;
		var json = angular.fromJson(response.items); 
		$scope.items[0].count=json.total;
		$scope.items[1].count=json.new;
		$scope.items[2].count=json.offline;
	});
	$scope.itemss={};
	$scope.activebase='active';
	$scope.active1=$scope.activebase;
	$scope.active2=!$scope.activebase;
	$scope.active3=!$scope.activebase;
	$scope.loglog = function(item){
		console.log(item);
	}
	$scope.items=[
			{name:'总接入项目数',count:0,style:'active',type:'TOTAL'},
			{name:'新接入项目数',count:0,style:'disable',type:'NEW'},
			{name:'离线项目数',count:0,style:'disable',type:'OFFLINE'}
		];
	$scope.lastactive=0;//保存上一个被选中的按钮
	$scope.active = function(index){
		$scope.first(0);//切换items时，换回到第一页
		console.log(index);
		$scope.items[$scope.lastactive].style='disable';
		$scope.items[index].style='active';
		$scope.lastactive=index;
		$http.post("/projectlist/",{params:{"page": 1,"count":8,"type":$scope.items[index].type}}).success(function(response) {
			$scope.allpages=response.allpages;
			console.log(response);
			$scope.projectList=response.projectList;
			var json = angular.fromJson(response.items); 
			$scope.items[0].count=json.total;
			$scope.items[1].count=json.new;
			$scope.items[2].count=json.offline;
		});
	};
	$scope.projectList = [];
	$scope.edit = function(index){
 		$state.go('ProjectInfo',{id: index.project.id});
	};
	$scope.menus=['项目名称','接入日期','所属系统'];
	$scope.sortBy = function(menu){
		console.log(menu);
	};
	$scope.currentpage=0;
	$scope.allpages=0;
	$scope.status1='disabled';
	$scope.status2='enable';
	$scope.status3='active';
	$scope.leftpagestatus=$scope.status1;
	$scope.rightpagestatus=!$scope.status1;
	$scope.pages=[];
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
		$http.post("/projectlist/",{params:{"page": $scope.pages[index].index+1,"count":8}}).success(function(response) {
			$scope.allpages=response.allpages;
			console.log(response);
			$scope.projectList=response.projectList;
			var json = angular.fromJson(response.items); 
			$scope.items[0].count=json.total;
			$scope.items[1].count=json.new;
			$scope.items[2].count=json.offline;
		});
	};
	$scope.rightchangepage= function(){
		console.log($scope.pages.length-1);
		if ($scope.pages[$scope.pages.length-1].index!=$scope.allpages-1) {
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
		$scope.pages=[];
		if($scope.allpages<5){
			console.log($scope.allpages);
			$scope.pages.push({index:0,status:'active'});
			for (var i = 0; i < $scope.allpages-1; i++) {
				$scope.pages.push({index:i+1,status:'enable'});
			};
		}else{
			$scope.pages=[
	  			{index:0,status:'active'},
	  			{index:1,status:'enable'},
	  			{index:2,status:'enable'},
	  			{index:3,status:'enable'},
	  			{index:4,status:'enable'}
	  		];
		}
	};
}])

app.controller('ProjectInfoCtrl', ['$scope','$stateParams', function($scope, $stateParams){
    console.log($stateParams);

    $scope.backToPrevious = function() {
        window.history.back();
    };
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
}])

app.controller('ProjectOnlineListCtrl', ['$scope','$state', '$http','Upload',function($scope,$state,$http,Upload){
	$http.post("/projectonlinelist/",{params:{"page": 1,"count":8,"type":"ONLINE"}}).success(function(response) {
		$scope.allpages=response.allpages;
		if($scope.allpages<5){
			console.log($scope.allpages);
			$scope.pages.push({index:0,status:'active'});
			for (var i = 0; i < $scope.allpages-1; i++) {
				$scope.pages.push({index:i+1,status:'enable'});
			};
		}else{
			$scope.pages=[
	  			{index:0,status:'active'},
	  			{index:1,status:'enable'},
	  			{index:2,status:'enable'},
	  			{index:3,status:'enable'},
	  			{index:4,status:'enable'}
	  		];
		}
		//console.log(response);
		$scope.projectList=response.projectList;
		var json = angular.fromJson(response.items); 
		$scope.items[0].count=json.online;
		$scope.items[1].count=json.offline;
	});
	$scope.items=[
			{name:'在线项目数',count:0,style:'active',type:'ONLINE'},
			{name:'离线项目数',count:0,style:'disable',type:'OFFLINE'},
		];
	$scope.lastactive=0;//保存上一个被选中的按钮
	$scope.active = function(index){
		$scope.first(0);//切换items时，换回到第一页
		$scope.items[$scope.lastactive].style='disable';
		$scope.items[index].style='active';
		$scope.lastactive=index;
		$http.post("/projectonlinelist/",{params:{"page": 1,"count":8,"type":$scope.items[index].type}}).success(function(response) {
			$scope.allpages=response.allpages;
			console.log(response);
			$scope.projectList=response.projectList;
			var json = angular.fromJson(response.items); 
			$scope.items[0].count=json.online;
			$scope.items[1].count=json.offline;
		});
	};
	$scope.menus=['项目名称','接入日期','所属系统'];
	$scope.projectList = [];
	$scope.currentpage=0;
	$scope.allpages=0;
	$scope.status1='disabled';
	$scope.status2='enable';
	$scope.status3='active';
	$scope.leftpagestatus=$scope.status1;
	$scope.rightpagestatus=!$scope.status1;
	$scope.pages=[];
	$scope.edit = function(index){
		console.log(index.project.id);
 		$state.go('ProjectOnlineEdit',{id: index.project.id});
	};
	$scope.changepage = function(index){
		$scope.pages[$scope.currentpage].status=$scope.status2;
		$scope.pages[index].status=$scope.status3;
		$scope.currentpage=index;
		//console.log(index);
		//console.log($scope.pages[index]);
		if (index!=0) {$scope.leftpagestatus=!$scope.status1;};
		if ($scope.pages[index].index==0) {$scope.leftpagestatus=$scope.status1;};
		if (index!=$scope.allpages) {$scope.rightpagestatus=!$scope.status1;};
		if ($scope.pages[index].index==$scope.allpages) {$scope.rightpagestatus=$scope.status1;};
		//$http.get("js/data.json")
    	//	.success(function(response) {console.log(response);$scope.projectList = response;});

    	//$http.post("http://192.168.1.32:8000/user/")
    	//	.success(function(response) {console.log(response);});
		$http.post("/projectonlinelist/",{params:{"page": $scope.pages[index].index+1,"count":8}}).success(function(response) {
			$scope.allpages=response.allpages;
			console.log(response);
			$scope.projectList=response.projectList;
			var json = angular.fromJson(response.items); 
			$scope.items[0].count=json.online;
			$scope.items[1].count=json.offline;
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
		$scope.pages=[];
		if($scope.allpages<5){
			console.log($scope.allpages);
			$scope.pages.push({index:0,status:'active'});
			for (var i = 0; i < $scope.allpages-1; i++) {
				$scope.pages.push({index:i+1,status:'enable'});
			};
		}else{
			$scope.pages=[
	  			{index:0,status:'active'},
	  			{index:1,status:'enable'},
	  			{index:2,status:'enable'},
	  			{index:3,status:'enable'},
	  			{index:4,status:'enable'}
	  		];
		}
	};
	$scope.uploadExcel=function(file){
		//console.log(file);
		Upload.upload({
            //服务端接收
            url: '/upload/',
            //上传的同时带的参数
            //上传的文件
            file: file
        }).progress(function (evt) {
            //进度条
        	//console.log("正在上传");
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $scope.uploadstatus="正在上传,"+progressPercentage +"%";
            //console.log('progess:' + progressPercentage + '%' + evt.config.file.name);
        }).success(function (data, status, headers, config) {
            //上传成功
            //console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
        	//console.log("上传成功");
            $scope.uploadstatus="上传成功";
           // $scope.uploadImg = data;
        }).error(function (data, status, headers, config) {
            //上传失败
            //console.log('error status: ' + status);
        	//console.log("上传失败");
            $scope.uploadstatus="上传失败";
        });
	};
}])

app.controller('ProjectOnlineEditCtrl', ['$scope','$stateParams', function($scope,$stateParams){
	console.log($stateParams);
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
    $scope.dt1=new Date();

    $scope.openCalendar1 = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened1 = true;
    }
}])

app.controller('ProjectOnlineAddCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
    $scope.dt1=new Date();

    $scope.openCalendar1 = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened1 = true;
    }
}])

app.controller('APICustomerListCtrl', ['$scope','$state', function($scope,$state){
	$scope.items=[
			{name:'新接入客户数',count:'8',style:'active'},
			{name:'总接入客户数',count:'100',style:'disable'}
		];
	$scope.lastactive=0;//保存上一个被选中的按钮
	$scope.active = function(index){
		$scope.items[$scope.lastactive].style='disable';
		$scope.items[index].style='active';
		$scope.lastactive=index;
	};
	$scope.menus=['客户名称','接入日期','所属系统'];
	$scope.customers=[
		{id:'1',status:'合作中',code:'10086',name:'Goods',date:'2016-5-12',person:'Henry',subsystem:'a@company.com',subcompany:'上海'},
		{id:'2',status:'中止合作',code:'10086',name:'Authen',date:'2016-5-12',person:'Henry',subsystem:'a@company.com',subcompany:'北京'},
		{id:'3',status:'合作中',code:'10086',name:'Jesica',date:'2016-5-12',person:'Henry',subsystem:'a@company.com',subcompany:'佛山'},
		{id:'4',status:'合作中',code:'10086',name:'Tom',date:'2016-5-12',person:'Henry',subsystem:'a@company.com',subcompany:'东莞'},
		{id:'5',status:'中止合作',code:'10086',name:'Book',date:'2016-5-12',person:'Henry',subsystem:'a@company.com',subcompany:'西安'},
		{id:'6',status:'合作中',code:'10086',name:'Bill',date:'2016-5-12',person:'Henry',subsystem:'a@company.com',subcompany:'南京'}
	];
	$scope.edit = function(index){
		$state.go('APICustomerEdit',{id: index.customer.id});
	};
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
}])

app.controller('APICustomerEditCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
}])

app.controller('APICustomerAddCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
}])

app.controller('APIProjectListCtrl', ['$scope','$state', function($scope,$state){
	$scope.items=[
			{name:'新接入项目数',count:'8',style:'active'},
			{name:'总接入项目数',count:'80',style:'disable'}
		];
	$scope.lastactive=0;//保存上一个被选中的按钮
	$scope.active = function(index){
		$scope.items[$scope.lastactive].style='disable';
		$scope.items[index].style='active';
		$scope.lastactive=index;
	};
	$scope.menus=['项目名称','接入日期','所属系统'];
	$scope.sortBy = function(menu){
		console.log(menu);
	};
	$scope.projects = [
			{id:'1',status:'正常',name:'John',date:'2016-5-12',enviroment:'联调环境',person:'霍壮宁',customer:'四川九寨沟',subsystem:'a@company.com',subcompany:'上海'},
			{id:'2',status:'正常',name:'Bill',date:'2016-5-9',enviroment:'联调环境',person:'余腾',customer:'万科',subsystem:'bg@company.com',subcompany:'北京'},
			{id:'3',status:'正常',name:'Bobo',date:'2015-5-12',enviroment:'联调环境',person:'刘安',customer:'格力',subsystem:'c@company.com',subcompany:'武汉'},
			{id:'4',status:'正常',name:'liLy',date:'2015-5-12',enviroment:'中试环境',person:'余腾',customer:'4',subsystem:'c@company.com',subcompany:'武汉'},
			{id:'5',status:'部分正常',name:'Bobo',date:'2015-5-12',enviroment:'中试环境',person:'刘安',customer:'5',subsystem:'c@company.com',subcompany:'武汉'},
			{id:'6',status:'部分正常',name:'Lily',date:'2013-5-12',enviroment:'生产环境',person:'霍壮宁',customer:'6',subsystem:'dd@company.com',subcompany:'上海'}
		];
	$scope.edit = function(index){
 		$state.go('APIProjectInfo',{id: index.project.id});
	};
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
}])

app.controller('APIProjectInfoCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
    $scope.dt1=new Date();

    $scope.openCalendar1 = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened1 = true;
    }

    $scope.apiprojects = [
			{id:'1',status:'正常',name:'门禁开门',serviceid:'3c.door.opendoor',recentdate:'2016-5-9',input:'是'},
			{id:'2',status:'正常',name:'开门协议',serviceid:'3c.door.querydoors',recentdate:'2016-5-9',input:'是'},
			{id:'3',status:'正常',name:'开门协议',serviceid:'3c.door.querydoors',recentdate:'2016-5-9',input:'是'},
			{id:'4',status:'正常',name:'开门协议',serviceid:'3c.door.querydoors',recentdate:'2016-5-9',input:'是'}
		];
	$scope.inputs=['serviceId','requestType','parkCode','carNo'];
	$scope.outputs=['serviceId','resultCode','message','objectId','operateType','carNo','enterTime','enterPicUrl'];

	$scope.set=function(input,value){
		console.log(input);
		console.log(value);
		console.log(input+":"+value);
	}
	$scope.showorhide=false;
	$scope.showit=function(){
		$scope.showorhide=true;
	};
	$scope.hideit=function(){
		$scope.showorhide=false;
	};
}])

app.controller('APIProjectAddCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
    $scope.dt1=new Date();

    $scope.openCalendar1 = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened1 = true;
    }
    $scope.apiprojects = [
			{id:'1',status:'正常',name:'门禁开门',serviceid:'3c.door.opendoor',recentdate:'2016-5-9',input:'是'},
			{id:'2',status:'正常',name:'开门协议',serviceid:'3c.door.querydoors',recentdate:'2016-5-9',input:'是'},
			{id:'3',status:'正常',name:'开门协议',serviceid:'3c.door.querydoors',recentdate:'2016-5-9',input:'是'},
			{id:'4',status:'正常',name:'开门协议',serviceid:'3c.door.querydoors',recentdate:'2016-5-9',input:'是'}
		];
	$scope.showorhide=false;
	$scope.showit=function(){
		$scope.showorhide=true;
	};
	$scope.hideit=function(){
		$scope.showorhide=false;
	};
}])

app.controller('APIListCtrl', ['$scope','$state', function($scope,$state){
	$scope.menus=['协议名称','服务标识','所属版本'];
	$scope.apis=[
		{id:'1',name:'门禁开门协议',serviceid:'3c.door.opendoor',version:'v2.0'},
		{id:'2',name:'生成订单协议',serviceid:'3c.pay.createorderbycard',version:'v2.0'},
		{id:'3',name:'支付结果通知协议',serviceid:'3c.pay.notifyorderresult',version:'v2.0'},
		{id:'4',name:'查询相似车辆协议',serviceid:'3c.pay.querycarbycarno',version:'v2.0'},
		{id:'5',name:'生成订单协议',serviceid:'3c.pay.createorderbycarno',version:'v2.0'},
		{id:'6',name:'停车场车位信息查询协议',serviceid:'3c.park.queryparkplacelist',version:'v2.0'},
		{id:'7',name:'车辆停放位置查询协议',serviceid:'3c.park.querycarparkingspot',version:'v2.0'}
	];
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
	$scope.edit = function(index){
 		$state.go('APIEdit',{id: index.api.id});
	};
}])

app.controller('APIEditCtrl', ['$scope', function($scope){
	$scope.apis={
		input:['serviceId','requestType','parkCode','carNo'],
		output:['serviceId','resultCode','message','objectId','operateType','carNo','enterTime','enterPicUrl']
	};

	$scope.inputshow1=false;
	$scope.input1='';
	$scope.inputadd= function (){
		$scope.input1='';
		$scope.show=true;
	};
	$scope.inputdelete= function (input){
		$scope.apis.input.splice(input,1);
		console.log($scope.apis.input);
	};
	$scope.inputblur= function (){
		if($scope.inputshow1.length==0){
			$scope.inputshow1=true;
		}else{
			$scope.inputshow1=false;
		}
	};
	$scope.save1= function (){
		if($scope.input1.length!=0){
			$scope.apis.input.push($scope.input1);
			console.log($scope.apis.input);
			$scope.show=false;
		}else{
			$scope.inputshow1=true;
		}
	};

	$scope.outputshow1=false;
	$scope.output1='';
	$scope.outputadd= function (){
		$scope.output1='';
		$scope.show1=true;
	};
	$scope.outputdelete= function (output){
		console.log(output);
		$scope.apis.output.splice(output,1);
		console.log($scope.apis.output);
	};
	$scope.outputblur= function (){
		if($scope.outputshow1.length==0){
			$scope.outputshow1=true;
		}else{
			$scope.outputshow1=false;
		}
	};
	$scope.save2= function (){
		if($scope.output1.length!=0){
			$scope.apis.output.push($scope.output1);
			console.log($scope.apis.output);
			$scope.show1=false;
		}else{
			$scope.outputshow1=true;
		}
	}
	;
}])

app.controller('APIAddCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

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
    $scope.apis={
		input:[],
		output:[]
	};
	
	$scope.inputshow1=false;
	$scope.input1='';
	$scope.inputadd= function (){
		$scope.input1='';
		$scope.show=true;
	};
	$scope.inputdelete= function (input){
		console.log(input);
		$scope.apis.input.splice(input,1);
		console.log($scope.apis.input);
	};
	$scope.inputblur= function (){
		if($scope.inputshow1.length==0){
			$scope.inputshow1=true;
		}else{
			$scope.inputshow1=false;
		}
	};
	$scope.save1= function (){
		if($scope.input1.length!=0){
			$scope.apis.input.push($scope.input1);
			console.log($scope.apis.input);
			$scope.show=false;
		}else{
			$scope.inputshow1=true;
		}
	};

	$scope.outputshow1=false;
	$scope.output1='';
	$scope.outputadd= function (){
		$scope.output1='';
		$scope.show1=true;
	};
	$scope.outputdelete= function (output){
		console.log(output);
		$scope.apis.output.splice(output,1);
		console.log($scope.apis.output);
	};
	$scope.outputblur= function (){
		if($scope.outputshow1.length==0){
			$scope.outputshow1=true;
		}else{
			$scope.outputshow1=false;
		}
	};
	$scope.save2= function (){
		if($scope.output1.length!=0){
			$scope.apis.output.push($scope.output1);
			console.log($scope.apis.output);
			$scope.show1=false;
		}else{
			$scope.outputshow1=true;
		}
	};
}])

app.controller('HostsCtrl', ['$scope','$state', function($scope,$state){
	$scope.menus=['主机状态','主机IP','所属系统'];
	$scope.hosts=[
		{id:'1',status:'正常',ip:'10.10.201.22',servicename:'集成服务',projectname:'租用平台',subsystem:'一系统',name:'public-n-master'},
		{id:'2',status:'闲置',ip:'10.10.201.22',servicename:'集成服务',projectname:'租用平台',subsystem:'二系统',name:'public-n-slave'},
		{id:'3',status:'闲置',ip:'10.10.201.22',servicename:'集成服务',projectname:'华侨城',subsystem:'三系统',name:'hqc-n-file'},
		{id:'4',status:'闲置',ip:'10.10.201.25',servicename:'集成服务',projectname:'珠海保利',subsystem:'二系统',name:'zhbl-n-sync'},
		{id:'5',status:'正常',ip:'10.10.201.22',servicename:'集成服务',projectname:'租用平台',subsystem:'二系统',name:'public-n-slave'},
		{id:'6',status:'正常',ip:'10.10.201.23',servicename:'集成服务',projectname:'租用平台',subsystem:'一系统',name:'public-n-slave'},
		{id:'7',status:'正常',ip:'10.10.201.22',servicename:'集成服务',projectname:'租用平台',subsystem:'一系统',name:'public-n-slave'}
	];
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
	$scope.edit = function(index){
		console.log(index);
 		$state.go('HostEdit',{id: index.host.id});
	};
}])

app.controller('HostEditCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
}])

app.controller('HostAddCtrl', ['$scope', function($scope){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
}])

app.controller('ProjectsCtrl', ['$scope','$state', function($scope,$state){
	$scope.menus=['项目名称','项目状态','所属系统'];
	$scope.projects=[
		{id:'1',name:'华侨城',status:'正常',starttime:'2015-5-2',person:'余腾',hostcount:'4',subsystem:'一系统'},
		{id:'2',name:'珠海保利',status:'正常',starttime:'2015-4-2',person:'刘安',hostcount:'4',subsystem:'二系统'},
		{id:'3',name:'租用平台',status:'正常',starttime:'2015-7-2',person:'余腾',hostcount:'4',subsystem:'一系统'},
		{id:'4',name:'云平台',status:'停用',starttime:'2015-5-7',person:'余腾',hostcount:'4',subsystem:'三系统'},
		{id:'5',name:'凯德物业',status:'正常',starttime:'2015-10-6',person:'余腾',hostcount:'4',subsystem:'一系统'},
		{id:'6',name:'华侨城',status:'正常',starttime:'2015-5-2',person:'余腾',hostcount:'4',subsystem:'一系统'}
	];
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
	$scope.edit = function(index){
		console.log(index);
 		$state.go('ProjectsEdit',{id: index.project.id});
	};
}])

app.controller('ProjectsEditCtrl', ['$scope','$state','$stateParams',function($scope,$state,$stateParams){
	console.log($stateParams);
	$scope.hosts=[
		{id:'1',ip:'10.10.201.22',servicename:'集成服务',name:'public-n-master'},
		{id:'2',ip:'10.10.201.22',servicename:'集成服务',name:'public-n-slave'},
		{id:'6',ip:'10.10.201.23',servicename:'集成服务',name:'public-n-slave'},
		{id:'7',ip:'10.10.201.22',servicename:'集成服务',name:'public-n-slave'}
	];
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    $scope.edit = function(index){
		console.log(index);
 		$state.go('HostEdit',{id: index.host.id});
	};
	$scope.showorhide=false;
	$scope.showit=function(){
		$scope.showorhide=true;
	};
	$scope.hideit=function(){
		$scope.showorhide=false;
	};
	$scope.save=function(){
		$state.go('ProjectsEdit',{id:$stateParams.id});
	};
}])

app.controller('ProjectsAddCtrl', ['$scope','$state', function($scope,$state){
	$scope.dt=new Date();

    $scope.openCalendar = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    $scope.edit = function(index){
		console.log(index);
 		$state.go('HostEdit',{id: index.host.id});
	};
	$scope.showorhide=false;
	$scope.showit=function(){
		$scope.showorhide=true;
	};
	$scope.hideit=function(){
		$scope.showorhide=false;
	};
	$scope.save=function(){
		$state.go('ProjectsAdd');
	};
}])

app.controller('ServicesCtrl', ['$scope','$state', function($scope,$state){
	$scope.menus=['服务名称','主机总数','ip段'];
	$scope.services=[
		{id:'1',name:'云服务',counts:'100',network:'10.10.201.1/22'},
		{id:'1',name:'云平台',counts:'100',network:'10.10.201.1/22'},
		{id:'2',name:'集成服务',counts:'100',network:'10.10.201.1/22'},
		{id:'3',name:'管理平台',counts:'100',network:'10.10.201.1/22'},
		{id:'4',name:'通讯网关-c',counts:'100',network:'10.10.201.1/22'},
		{id:'6',name:'通讯网关-n',counts:'100',network:'10.10.201.1/22'},
		{id:'7',name:'支付网关',counts:'100',network:'10.10.201.1/22'}
	];
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
	$scope.edit = function(index){
		console.log(index);
 		$state.go('ServicesEdit',{id: index.service.id});
	};
}])

app.controller('ServicesEditCtrl', ['$scope','$state','$stateParams',function($scope,$state,$stateParams){
	$scope.hosts=[
		{id:'1',status:'正常',ip:'10.10.201.22',servicename:'云服务',projectname:'租用平台',subsystem:'一系统',name:'public-n-master'},
		{id:'2',status:'闲置',ip:'10.10.201.22',servicename:'云服务',projectname:'租用平台',subsystem:'二系统',name:'public-n-slave'},
		{id:'3',status:'闲置',ip:'10.10.201.22',servicename:'云服务',projectname:'华侨城',subsystem:'三系统',name:'hqc-n-file'},
		{id:'4',status:'闲置',ip:'10.10.201.25',servicename:'云服务',projectname:'珠海保利',subsystem:'二系统',name:'zhbl-n-sync'},
		{id:'5',status:'正常',ip:'10.10.201.22',servicename:'云服务',projectname:'租用平台',subsystem:'二系统',name:'public-n-slave'},
		{id:'6',status:'正常',ip:'10.10.201.22',servicename:'云服务',projectname:'租用平台',subsystem:'一系统',name:'public-n-slave'}
	];
	$scope.edit = function(index){
		console.log(index);
 		$state.go('HostEdit',{id: index.host.id});
	};
	$scope.currentpage=0;
	$scope.allpages=10;
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
	};
}])

app.controller('ServicesAddCtrl', ['$scope','$state','$stateParams',function($scope,$state,$stateParams){

}])



app.service('backToPreviousService', function() {
    this.backToPrevious = function () {
        return window.history.back();
    }
});
