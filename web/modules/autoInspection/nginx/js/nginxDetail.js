app.controller('NginxDetailCtrl', ['$scope','$state','$stateParams','$http','$filter',function($scope,$state,$stateParams,$http,$filter){
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
  $scope.nginxs = [
      {id:'1',check_time:'运维中',host_ip:'John',host_name:'2016-5-12',acc_ip:'@jh',acc_times:'a@company.com',check_status:'上海'},
      {id:'2',check_time:'运维中',host_ip:'Bill',host_name:'2016-5-9',acc_ip:'@bg',acc_times:'bg@company.com',check_status:'北京'},
      {id:'3',check_time:'暂停',host_ip:'Bobo',host_name:'2015-5-12',acc_ip:'@hz',acc_times:'c@company.com',check_status:'武汉'},
      {id:'4',check_time:'暂停',host_ip:'Bobo',host_name:'2015-5-12',acc_ip:'@hz',acc_times:'c@company.com',check_status:'武汉'},
      {id:'5',check_time:'暂停',host_ip:'Bobo',host_name:'2015-5-12',acc_ip:'@hz',acc_times:'c@company.com',check_status:'武汉'},
      {id:'6',check_time:'实施中',host_ip:'Lily',host_name:'2013-5-12',acc_ip:'@li',acc_times:'dd@company.com',check_status:'上海'}
    ];
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
                {index:0,status:'active',color:'#fff'},
                {index:1,status:'enable',color:'#fff'},
                {index:2,status:'enable',color:'#fff'},
                {index:3,status:'enable',color:'#fff'},
                {index:4,status:'enable',color:'#fff'}
              ];
  $scope.account="";
  $scope.userName="";
  $scope.telephone="";
  $scope.platformname="";
  
  $scope.dt=new Date();
  $scope.dt.setDate(new Date().getDate()-1);
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
  var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
  var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
  $scope.sourceip="";
  $scope.serviceid="";
  $http.post("auto/nginx/detailquery.do?pageSize="+$scope.pageSize+"&pageIndex=0"+"&sourceip="+$scope.sourceip+"&serviceid="+$scope.serviceid
		  +"&starttime="+starttime+"&endtime="+endtime).success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.nginxs = resp.result;
	  $scope.nginxs1 = resp.result1;//bynginx
	  $scope.nginxs2 = resp.result2;//top10
	  $scope.data1=[];
      for (var i = $scope.nginxs1.length - 1; i >= 0; i--) {
    	  var data={};
    	  data.value=$scope.nginxs1[i].count;
    	  data.name=$scope.nginxs1[i].service_type;
    	  $scope.data1.push(data);
       };
       $scope.option1.series[0].data=$scope.data1;
       $scope.data2=[];
       for (var i = $scope.nginxs2.length - 1; i >= 0; i--) {
    	   var data={};
    	   data.value=$scope.nginxs2[i].count;
    	   data.name=$scope.nginxs2[i].acc_ip;
    	   $scope.data2.push(data);
       };
       $scope.option2.series[0].data=$scope.data2;
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
    var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
    var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
	  $http.post("auto/nginx/detailquery.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&sourceip="+$scope.sourceip+"&serviceid="+$scope.serviceid
			  +"&starttime="+starttime+"&endtime="+endtime).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.nginxs = resp.result;
		  $scope.nginxs1 = resp.result1;//bynginx
		  $scope.nginxs2 = resp.result2;//top10
		  $scope.data1=[];
	      for (var i = $scope.nginxs1.length - 1; i >= 0; i--) {
	    	  var data={};
	    	  data.value=$scope.nginxs1[i].count;
	    	  data.name=$scope.nginxs1[i].service_type;
	    	  $scope.data1.push(data);
	       };
//	       $scope.option1.series[0].data=$scope.data1;
	       $scope.data2=[];
	       for (var i = $scope.nginxs2.length - 1; i >= 0; i--) {
	    	   var data={};
	    	   data.value=$scope.nginxs2[i].count;
	    	   data.name=$scope.nginxs2[i].acc_ip;
	    	   $scope.data2.push(data);
	       };
//	       $scope.option2.series[0].data=$scope.data2;
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
    var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
    var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
	  $http.post("auto/nginx/detailquery.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&sourceip="+$scope.sourceip+"&serviceid="+$scope.serviceid
			  +"&starttime="+starttime+"&endtime="+endtime).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.nginxs = resp.result;
		  $scope.nginxs1 = resp.result1;//bynginx
		  $scope.nginxs2 = resp.result2;//top10
		  $scope.data1=[];
	      for (var i = $scope.nginxs1.length - 1; i >= 0; i--) {
	    	  var data={};
	    	  data.value=$scope.nginxs1[i].count;
	    	  data.name=$scope.nginxs1[i].service_type;
	    	  $scope.data1.push(data);
	       };
//	       $scope.option1.series[0].data=$scope.data1;
	       $scope.data2=[];
	       for (var i = $scope.nginxs2.length - 1; i >= 0; i--) {
	    	   var data={};
	    	   data.value=$scope.nginxs2[i].count;
	    	   data.name=$scope.nginxs2[i].acc_ip;
	    	   $scope.data2.push(data);
	       };
	       $scope.option2.series[0].data=$scope.data2;
	  });

  };
  
  $scope.query=function(){
	    var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
	    var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
		  $http.post("auto/nginx/detailquery.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&sourceip="+$scope.sourceip+"&serviceid="+$scope.serviceid
				  +"&starttime="+starttime+"&endtime="+endtime).success(function(response) {
			  var resp =angular.fromJson(angular.fromJson(response));
			  console.log(resp);
			  var returnCode = resp.returnCode;
			  var message = resp.message;
			  $scope.counts=resp.counts;
			  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
			  if($scope.allpages<=5){
				  $scope.rightpagestatus=$scope.status1;
			  }
			  $scope.nginxs = resp.result;
			  $scope.nginxs1 = resp.result1;//bynginx
			  $scope.nginxs2 = resp.result2;//top10
			  $scope.data1=[];
		      for (var i = $scope.nginxs1.length - 1; i >= 0; i--) {
		    	  var data={};
		    	  data.value=$scope.nginxs1[i].count;
		    	  data.name=$scope.nginxs1[i].service_type;
		    	  $scope.data1.push(data);
		       };
		       $scope.option1.series[0].data=$scope.data1;
		       $scope.data2=[];
		       for (var i = $scope.nginxs2.length - 1; i >= 0; i--) {
		    	   var data={};
		    	   data.value=$scope.nginxs2[i].count;
		    	   data.name=$scope.nginxs2[i].acc_ip;
		    	   $scope.data2.push(data);
		       };
		       $scope.option2.series[0].data=$scope.data2;
		  });
  };
  $scope.data1=[];
  $scope.option1 = {
  	    title : {
  	        text: '车场调用访问明细(业务)',
	        textStyle:{
	        	color:'#fff'
	        },
  	        subtext: '',
  	        x:'center'
  	    },
  	    tooltip : {
  	        trigger: 'item',
  	        formatter: "{a} <br/>{b} : {c} 次({d}%)"
  	    },
  	    legend: {
  	    	show:false,
  	        x : 'center',
  	        y : 'bottom',
  	        data:[]
  	    },
  	    toolbox: {
  	        show : true,
  	        feature : {
  	            mark : {show: true},
  	            dataView : {show: true, readOnly: false},
  	            magicType : {
  	                show: true,
  	                type: ['pie', 'funnel']
  	            },
  	            restore : {show: false},
  	            saveAsImage : {show: true}
  	        }
  	    },
  	    calculable : true,
  	    series : [
  	        {
  	            name:'车场调用访问明细(业务)',
  	            type:'pie',
		        	radius : '55%',
		        	center: ['50%', '50%'],
  	            roseType : 'radius',
  	            label: {
  	                normal: {
  	                    show: true,
  	                    formatter: '{b} : ({d}%)'
  	                },
  	                emphasis: {
  	                    show: true
  	                }
  	            },
  	            lableLine: {
  	                normal: {
  	                    show: true
  	                },
  	                emphasis: {
  	                    show: true
  	                }
  	            },
  	            data:$scope.data1
  	        }
  	    ],
  	    color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
  	};
  function pieConfig1(params){
      console.log(params);
  };
  
  $scope.pieConfig1 = {
                      theme:'default',
                      event: [{click:pieConfig1}],
                      dataLoaded:true
                  }; 
  $scope.data2=[];
  $scope.option2 = {
		  title : {
			  text: '车场调用访问明细前十',
		        textStyle:{
		        	color:'#fff'
		        },
			  subtext: '',
			  x:'center'
		  },
		  tooltip : {
			  trigger: 'item',
			  formatter: "{a} <br/>{b} : {c} 次({d}%)"
		  },
		  legend: {
			  show:false,
			  x : 'center',
			  y : 'bottom',
			  data:[]
		  },
		  toolbox: {
			  show : true,
			  feature : {
				  mark : {show: true},
				  dataView : {show: true, readOnly: false},
				  magicType : {
					  show: true,
					  type: ['pie', 'funnel']
				  },
				  restore : {show: false},
				  saveAsImage : {show: true}
			  }
		  },
		  calculable : true,
		  series : [
		            {
		            	name:'车场调用访问明细前十',
		            	type:'pie',
		            	radius : '55%',
		            	center: ['50%', '50%'],
		            	roseType : 'radius',
		            	label: {
		            		normal: {
		            			show: true,
		            			formatter: '{b} : ({d}%)'
		            		},
		            		emphasis: {
		            			show: true
		            		}
		            	},
		            	lableLine: {
		            		normal: {
		            			show: true
		            		},
		            		emphasis: {
		            			show: true
		            		}
		            	},
		            	data:$scope.data2
		            }
		            ],
		            color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
  };
  function pieConfig2(params){
	  console.log(params);
  };
  
  $scope.pieConfig2 = {
		  theme:'default',
		  event: [{click:pieConfig2}],
		  dataLoaded:true
  };
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.nginxs.length;i++){
	  		if(i==index){
	  			$scope.nginxs[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.nginxs[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.nginxs.length;i++){
	  		$scope.nginxs[i].style="";
	  	}
	  }
}])