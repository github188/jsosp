app.controller('RedisResourceCtrl', ['$scope','$state','$stateParams','$http','$filter','$interval',function($scope,$state,$stateParams,$http,$filter,$interval){
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
  $scope.redisSources = [
      {id:'1',check_time:'运维中',redis_host:'John',sql_param:'2016-5-12',client_ip:'@jh',db_nanme:'a@company.com',query_times:'上海',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'2',check_time:'运维中',redis_host:'Bill',sql_param:'2016-5-9',client_ip:'@bg',db_nanme:'bg@company.com',query_times:'北京',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'3',check_time:'暂停',redis_host:'Bobo',sql_param:'2015-5-12',client_ip:'@hz',db_nanme:'c@company.com',query_times:'武汉',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'4',check_time:'暂停',redis_host:'Bobo',sql_param:'2015-5-12',client_ip:'@hz',db_nanme:'c@company.com',query_times:'武汉',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'5',check_time:'暂停',redis_host:'Bobo',sql_param:'2015-5-12',client_ip:'@hz',db_nanme:'c@company.com',query_times:'武汉',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'},
      {id:'6',check_time:'实施中',redis_host:'Lily',sql_param:'2013-5-12',client_ip:'@li',db_nanme:'dd@company.com',query_times:'上海',lock_times:'上海',parser_row_counts:'上海',return_row_counts:'上海'}
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
  //$scope.pages[$scope.currentpage].index
  $http.post("auto/redisSource/list.do?pageSize="+$scope.pageSize+"&pageIndex=0").success(function(response) {
	  var resp =angular.fromJson(angular.fromJson(response));
	  console.log(resp);
	  var returnCode = resp.returnCode;
	  var message = resp.message;
	  $scope.counts=resp.counts;
	  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
	  if($scope.allpages<=5){
		  $scope.rightpagestatus=$scope.status1;
	  }
	  $scope.redisSources = resp.result;
	  $scope.RedisSourceresult = resp.Redisresult;
	  $scope.RedisSourceresult.splice(0, 0, {redis_host : ''});
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
    var redisHosts="";
    if($scope.redisname!=undefined){
    	redisHosts=$scope.redisname.redis_host;
    }
	  $http.post("auto/redisSource/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&redisHosts="+redisHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.redisSources = resp.result;
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
    var redisHosts="";
    if($scope.redisname!=undefined){
    	redisHosts=$scope.redisname.redis_host;
    }
	  $http.post("auto/redisSource/list.do?pageSize="+$scope.pageSize+"&pageIndex="+$scope.pages[$scope.currentpage].index+"&redisHosts="+redisHosts).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  $scope.allpages=Math.ceil($scope.counts/$scope.pageSize);
		  if($scope.allpages<=5){
			  $scope.rightpagestatus=$scope.status1;
		  }
		  $scope.redisSources = resp.result;
	  });

  };

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
  
  $scope.query=function(){
	  var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
	  var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
	  $http.post("auto/redisSource/listbyline.do?starttime="+starttime+"&endtime="+endtime).success(function(response) {
			var resp =angular.fromJson(angular.fromJson(response));
			console.log(resp);
			var returnCode = resp.returnCode;
			var message = resp.message;
			$scope.counts=resp.counts;
			var result = resp.result;
			if(result ==null || result.length==0){
				alert('查询条件有误，无数据');
			};
			$scope.projects=[];
			for (var i = result.length - 1; i >= 0; i--) {
				var option1 = {
					    title: {
					        text: 'Redis资源监控',
					        textStyle:{
					        	color:'#fff'
					        },
					        x:'center'
					    },
					    tooltip: {
					        trigger: 'axis'
					    },
					    legend: {
					        data:['最大值','最小值']
					    },
					    toolbox: {
					        show: false,
					        feature: {
					            dataZoom: {
					                yAxisIndex: 'none'
					            },
					            dataView: {readOnly: false},
					            magicType: {type: ['line', 'bar']},
					            restore: {},
					            saveAsImage: {}
					        }
					    },
					    xAxis:  {
					        type: 'category',
					        boundaryGap: false,
					        data: ['周一','周二','周三','周四','周五','周六','周日'],
					        axisLine :{
					        	lineStyle :{
					        		color:'#fff'
					        	}
					        }
					    },
					    yAxis: {
					        type: 'value',
					        axisLabel: {
					            formatter: '{value} G'
					        },
					        axisLine :{
					        	lineStyle :{
					        		color:'#fff'
					        	}
					        }
					    },
					    series: [
					        {
					            name:'redis资源',
					            type:'line',
					            data:[11, 11, 15, 13, 12, 13, 10],
					            markPoint: {
					                data: [
					                    {type: 'max', name: '最大值'},
					                    {type: 'min', name: '最小值'}
					                ]
					            },
					            markLine: {
					                data: [
					                    {type: 'average', name: '平均值'}
					                ]
					            }
					        }
					    ],
					    color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
					};
				option1.title.text=result[i].name;
				var xAxis=[];
				var data=[];
				for (var j = 0;j < result[i].value.length ; j ++) {
					var tt= result[i].value[j].total;
					xAxis.push(result[i].value[j].date);
					data.push(tt.replace(/\s+/g,"").substr(0,tt.replace(/\s+/g,"").length-1));
					option1.yAxis.axisLabel.formatter='{value} '+tt.replace(/\s+/g,"").charAt(tt.replace(/\s+/g,"").length - 1);
				}
				option1.xAxis.data=xAxis;
				option1.series[0].data=data;
				$scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
			};
		});	  
  };
  
  $scope.listbynodeline=function(){
	  var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
	  var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
	  $http.post("auto/redisSource/listbynodeline.do?starttime="+starttime+"&endtime="+endtime).success(function(response) {
		  var resp =angular.fromJson(angular.fromJson(response));
		  console.log(resp);
		  var returnCode = resp.returnCode;
		  var message = resp.message;
		  $scope.counts=resp.counts;
		  var result = resp.result;
		  if(result ==null || result.length==0){
			  alert('查询条件有误，无数据');
		  };
		  $scope.projects=[];
		  for (var i = result.length - 1; i >= 0; i--) {
			  var option1 = {
					  title: {
						  text: 'Redis资源监控',
					        textStyle:{
					        	color:'#fff'
					        },
						  x:'center'
					  },
					  tooltip: {
						  trigger: 'axis'
					  },
					  legend: {
						  show:true,
						  bottom: 10,
						  data:['node1','node2','node3','node4','node5','node6','node7','node8']
					  },
					  toolbox: {
						  show: false,
						  feature: {
							  dataZoom: {
								  yAxisIndex: 'none'
							  },
							  dataView: {readOnly: false},
							  magicType: {type: ['line', 'bar']},
							  restore: {},
							  saveAsImage: {}
						  }
					  },
					  xAxis:  {
						  type: 'category',
						  boundaryGap: false,
						  data: ['周一','周二','周三','周四','周五','周六','周日'],
					        axisLine :{
					        	lineStyle :{
					        		color:'#fff'
					        	}
					        }
					  },
					  yAxis: {
						  type: 'value',
						  axisLabel: {
							  formatter: '{value} G'
						  },
					        axisLine :{
					        	lineStyle :{
					        		color:'#fff'
					        	}
					        }
					  },
					  series: [
					           {
					        	   name:'node1',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node2',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node3',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node4',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node5',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node6',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node7',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           },
					           {
					        	   name:'node8',
					        	   type:'line',
					        	   data:[11, 11, 15, 13, 12, 13, 10],
					        	   markPoint: {
					        		   data: [
					        		          {type: 'max', name: '最大值'},
					        		          {type: 'min', name: '最小值'}
					        		          ]
					        	   },
					        	   markLine: {
					        		   data: [
					        		          {type: 'average', name: '平均值'}
					        		          ]
					        	   }
					           }
					           ],
					           color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
			  };
			  option1.title.text=result[i].name;
			  var xAxis=[];
			  var data1=[];
			  var data2=[];
			  var data3=[];
			  var data4=[];
			  var data5=[];
			  var data6=[];
			  var data7=[];
			  var data8=[];
			  for (var j = 0;j < result[i].value.length ; j ++) {
				  var node1= result[i].value[j].node1;
				  var node2= result[i].value[j].node2;
				  var node3= result[i].value[j].node3;
				  var node4= result[i].value[j].node4;
				  var node5= result[i].value[j].node5;
				  var node6= result[i].value[j].node6;
				  var node7= result[i].value[j].node7;
				  var node8= result[i].value[j].node8;
				  xAxis.push(result[i].value[j].date);
				  data1.push(node1.replace(/\s+/g,"").substr(0,node1.replace(/\s+/g,"").length-1));
				  data2.push(node2.replace(/\s+/g,"").substr(0,node2.replace(/\s+/g,"").length-1));
				  data3.push(node3.replace(/\s+/g,"").substr(0,node3.replace(/\s+/g,"").length-1));
				  data4.push(node4.replace(/\s+/g,"").substr(0,node4.replace(/\s+/g,"").length-1));
				  data5.push(node5.replace(/\s+/g,"").substr(0,node5.replace(/\s+/g,"").length-1));
				  data6.push(node6.replace(/\s+/g,"").substr(0,node6.replace(/\s+/g,"").length-1));
				  data7.push(node7.replace(/\s+/g,"").substr(0,node7.replace(/\s+/g,"").length-1));
				  data8.push(node8.replace(/\s+/g,"").substr(0,node8.replace(/\s+/g,"").length-1));
				  option1.yAxis.axisLabel.formatter='{value} '+node1.replace(/\s+/g,"").charAt(node1.replace(/\s+/g,"").length - 1);
			  }
			  option1.xAxis.data=xAxis;
			  option1.series[0].data=data1;
			  option1.series[1].data=data2;
			  option1.series[2].data=data3;
			  option1.series[3].data=data4;
			  option1.series[4].data=data5;
			  option1.series[5].data=data6;
			  option1.series[6].data=data7;
			  option1.series[7].data=data8;
			  $scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
		  };
	  });	  
  };

  $scope.option1 = {
	    title: {
	        text: '未来一周气温变化',
	        textStyle:{
	        	color:'#fff'
	        },
	        subtext: '纯属虚构'
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['最高气温','最低气温']
	    },
	    toolbox: {
	        show: true,
	        feature: {
	            dataZoom: {
	                yAxisIndex: 'none'
	            },
	            dataView: {readOnly: false},
	            magicType: {type: ['line', 'bar']},
	            restore: {},
	            saveAsImage: {}
	        }
	    },
	    xAxis:  {
	        type: 'category',
	        boundaryGap: false,
	        data: ['周一','周二','周三','周四','周五','周六','周日'],
	        axisLine :{
	        	lineStyle :{
	        		color:'#fff'
	        	}
	        }
	    },
	    yAxis: {
	        type: 'value',
	        axisLabel: {
	            formatter: '{value} °C'
	        },
	        axisLine :{
	        	lineStyle :{
	        		color:'#fff'
	        	}
	        }
	    },
	    series: [
	        {
	            name:'最高气温',
	            type:'line',
	            data:[11, 11, 15, 13, 12, 13, 10],
	            markPoint: {
	                data: [
	                    {type: 'max', name: '最大值'},
	                    {type: 'min', name: '最小值'}
	                ]
	            },
	            markLine: {
	                data: [
	                    {type: 'average', name: '平均值'}
	                ]
	            }
	        },
	        {
	            name:'最低气温',
	            type:'line',
	            data:[1, -2, 2, 5, 3, 2, 0],
	            markPoint: {
	                data: [
	                    {name: '周最低', value: -2, xAxis: 1, yAxis: -1.5}
	                ]
	            },
	            markLine: {
	                data: [
	                    {type: 'average', name: '平均值'},
	                    [{
	                        symbol: 'none',
	                        x: '90%',
	                        yAxis: 'max'
	                    }, {
	                        symbol: 'circle',
	                        label: {
	                            normal: {
	                                position: 'start',
	                                formatter: '最大值'
	                            }
	                        },
	                        type: 'max',
	                        name: '最高点'
	                    }]
	                ]
	            }
	        }
	    ]
	};
	
  function pieConfig1(params){
      console.log(params);
  };
  
  $scope.pieConfig1 = {
                      theme:'default',
                      event: [{click:pieConfig1}],
                      dataLoaded:true
                  }; 
  
  $http.post("auto/redisSource/listbyline.do?starttime="+starttime+"&endtime="+endtime).success(function(response) {
		var resp =angular.fromJson(angular.fromJson(response));
		console.log(resp);
		var returnCode = resp.returnCode;
		var message = resp.message;
		$scope.counts=resp.counts;
		var result = resp.result;
		if(result ==null || result.length==0){
			alert('查询条件有误，无数据');
		};
		$scope.projects=[];
		for (var i = result.length - 1; i >= 0; i--) {
			var option1 = {
				    title: {
				        text: 'Redis资源监控',
				        textStyle:{
				        	color:'#fff'
				        },
				        x:'center'
				    },
				    tooltip: {
				        trigger: 'axis'
				    },
				    legend: {
				        data:['最大值','最小值']
				    },
				    toolbox: {
				        show: false,
				        feature: {
				            dataZoom: {
				                yAxisIndex: 'none'
				            },
				            dataView: {readOnly: false},
				            magicType: {type: ['line', 'bar']},
				            restore: {},
				            saveAsImage: {}
				        }
				    },
				    xAxis:  {
				        type: 'category',
				        boundaryGap: false,
				        data: ['周一','周二','周三','周四','周五','周六','周日'],
				        axisLine :{
				        	lineStyle :{
				        		color:'#fff'
				        	}
				        }
				    },
				    yAxis: {
				        type: 'value',
				        axisLabel: {
				            formatter: '{value} G'
				        },
				        axisLine :{
				        	lineStyle :{
				        		color:'#fff'
				        	}
				        }
				    },
				    series: [
				        {
				            name:'redis资源',
				            type:'line',
				            data:[11, 11, 15, 13, 12, 13, 10],
				            markPoint: {
				                data: [
				                    {type: 'max', name: '最大值'},
				                    {type: 'min', name: '最小值'}
				                ]
				            },
				            markLine: {
				                data: [
				                    {type: 'average', name: '平均值'}
				                ]
				            }
				        }
				    ],
				    color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
				};
			option1.title.text=result[i].name;
			var xAxis=[];
			var data=[];
			for (var j = 0;j < result[i].value.length ; j ++) {
				var tt= result[i].value[j].total;
				xAxis.push(result[i].value[j].date);
				data.push(tt.replace(/\s+/g,"").substr(0,tt.replace(/\s+/g,"").length-1));
				option1.yAxis.axisLabel.formatter='{value} '+tt.replace(/\s+/g,"").charAt(tt.replace(/\s+/g,"").length - 1);
			}
			option1.xAxis.data=xAxis;
			option1.series[0].data=data;
			$scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
		};
	});  
//	$scope.timeout_upd = $interval(function () {
//		  var starttime=$filter('date')($scope.dt,'yyyy-MM-dd HH:mm:ss');
//		  var endtime=$filter('date')($scope.dt1,'yyyy-MM-dd HH:mm:ss');
//		$http.post("auto/redisSource/listbyline.do?starttime="+starttime+"&endtime="+endtime).success(function(response) {
//			var resp =angular.fromJson(angular.fromJson(response));
//			console.log(resp);
//			var returnCode = resp.returnCode;
//			var message = resp.message;
//			$scope.counts=resp.counts;
//			var result = resp.result;
//			if(result ==null || result.length==0){
//				alert('查询条件有误，无数据');
//			};
//			$scope.projects=[];
//			for (var i = result.length - 1; i >= 0; i--) {
//				var option1 = {
//					    title: {
//					        text: 'Redis资源监控',
//					        x:'center'
//					    },
//					    tooltip: {
//					        trigger: 'axis'
//					    },
//					    legend: {
//					        data:['最大值','最小值']
//					    },
//					    toolbox: {
//					        show: false,
//					        feature: {
//					            dataZoom: {
//					                yAxisIndex: 'none'
//					            },
//					            dataView: {readOnly: false},
//					            magicType: {type: ['line', 'bar']},
//					            restore: {},
//					            saveAsImage: {}
//					        }
//					    },
//					    xAxis:  {
//					        type: 'category',
//					        boundaryGap: false,
//					        data: ['周一','周二','周三','周四','周五','周六','周日']
//					    },
//					    yAxis: {
//					        type: 'value',
//					        axisLabel: {
//					            formatter: '{value} G'
//					        }
//					    },
//					    series: [
//					        {
//					            name:'redis资源',
//					            type:'line',
//					            data:[11, 11, 15, 13, 12, 13, 10],
//					            markPoint: {
//					                data: [
//					                    {type: 'max', name: '最大值'},
//					                    {type: 'min', name: '最小值'}
//					                ]
//					            },
//					            markLine: {
//					                data: [
//					                    {type: 'average', name: '平均值'}
//					                ]
//					            }
//					        }
//					    ],
//					    color:['#ff9f39','#75d075','#ed0f89','#c23531', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
//					};
//				option1.title.text=result[i].name;
//				var xAxis=[];
//				var data=[];
//				for (var j = 0;j < result[i].value.length ; j ++) {
//					var tt= result[i].value[j].total;
//					xAxis.push(result[i].value[j].date);
//					data.push(tt.replace(/\s+/g,"").substr(0,tt.replace(/\s+/g,"").length-1));
//					option1.yAxis.axisLabel.formatter='{value} '+tt.replace(/\s+/g,"").charAt(tt.replace(/\s+/g,"").length - 1);
//				}
//				option1.xAxis.data=xAxis;
//				option1.series[0].data=data;
//				$scope.projects.push({option1:option1,pieConfig1:$scope.pieConfig1});
//			};
//		});
//		
//	}, 5000);//每一秒更新一次theTime
  $scope.tableblur = function (index){
	  	console.log(index);
	  	for(var i=0;i<$scope.redisSources.length;i++){
	  		if(i==index){
	  			$scope.redisSources[i].style="background-color:#0c67c4;";
	  		}else{
	  			$scope.redisSources[i].style="";
	  		}
	  	}
	  }
	  $scope.tableleave = function (){
	  	for(var i=0;i<$scope.redisSources.length;i++){
	  		$scope.redisSources[i].style="";
	  	}
	  }
}])