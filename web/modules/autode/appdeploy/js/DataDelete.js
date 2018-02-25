app.controller('DataDeleteCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
            },
            {
            	name:'绿城物业',
            	code: 'lvcheng',
            },
            {
            	name:'金地物业',
            	code: 'jindi',
            },
            {
            	name:'碧桂园',
            	code: 'bgy',
            },
            {
            	name:'武汉公安',
            	code: 'whga',
            },
            {
            	name:'蓝光嘉宝',
            	code: 'lgjb',
            },
            {
            	name:'宝能物业',
            	code: 'bnwy',
            },
            {
            	name:'银海物业',
            	code: 'yhwy',
            },
            {
            	name:'天健物业',
            	code: 'tjwy',
            },
            {
            	name:'绵阳嘉兴',
            	code: 'myjx',
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
  $scope.datadeletes = [
      {id:'1',platform:'运维中',code:'John',name:'2016-5-12',starttime:'@jh',endtime:'a@company.com',status:'上海'},
      {id:'2',platform:'运维中',code:'Bill',name:'2016-5-9',starttime:'@bg',endtime:'bg@company.com',status:'北京'},
      {id:'3',platform:'暂停',code:'Bobo',name:'2015-5-12',starttime:'@hz',endtime:'c@company.com',status:'武汉'},
      {id:'4',platform:'暂停',code:'Bobo',name:'2015-5-12',starttime:'@hz',endtime:'c@company.com',status:'武汉'},
      {id:'5',platform:'暂停',code:'Bobo',name:'2015-5-12',starttime:'@hz',endtime:'c@company.com',status:'武汉'},
      {id:'6',platform:'实施中',code:'Lily',name:'2013-5-12',starttime:'@li',endtime:'dd@company.com',status:'上海'}
    ];
  $scope.datadeletecount=0;
  $scope.datadeletecount1=0;
  $scope.datadeletecount2=0;
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
  $http.post("auto/datadelete/list.do?pageIndex="+ 0 +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.datadeletecount=resp.datadeletecount;
			  $scope.datadeletecount1=resp.datadeletecount1;
			  $scope.datadeletecount2=resp.datadeletecount2;
			  $scope.allpages=Math.ceil($scope.datadeletecount/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.datadeletes = resp.result;
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

    $http.post("auto/datadelete/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.datadeletecount=resp.datadeletecount;
		  $scope.datadeletecount1=resp.datadeletecount1;
		  $scope.datadeletecount2=resp.datadeletecount2;
		  $scope.allpages=Math.ceil($scope.datadeletecount/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.datadeletes = resp.result;
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

    $http.post("auto/datadelete/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.datadeletecount=resp.datadeletecount;
		  $scope.datadeletecount1=resp.datadeletecount1;
		  $scope.datadeletecount2=resp.datadeletecount2;
		  $scope.allpages=Math.ceil($scope.datadeletecount/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.datadeletes = resp.result;
	  });

  };
  
  $scope.save=function(){
	  $http.post("auto/datadelete/add.do?platform="+ $scope.platform.code +"&code="+$scope.code+"&isDeleted="+$scope.isDeleted+"&prefix="+$scope.prefix).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  alert(message);
		  $http.post("auto/datadelete/list.do?pageIndex="+ $scope.pages[$scope.currentpage].index +"&pageSize="+$scope.pageSize).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.datadeletecount=resp.datadeletecount;
			  $scope.datadeletecount1=resp.datadeletecount1;
			  $scope.datadeletecount2=resp.datadeletecount2;
			  $scope.allpages=Math.ceil($scope.datadeletecount/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.datadeletes = resp.result;
		  });
	  });
  };
  
  $scope.message=function(index){
	  $http.post("auto/datadelete/queryById.do?id="+ $scope.datadeletes[index].id).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.msg=resp.result[0].log;
	  });
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.datadeletes.length;i++){
	  		if(i==index){
	  			$scope.datadeletes[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.datadeletes[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.datadeletes.length;i++){
	  		$scope.datadeletes[i].style="";
	  	}
	  }
}])