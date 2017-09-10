 
String.prototype.trim   =   function()
{
         //   用正则表达式将前后空格
         //   用空字符串替代。
         return   this.replace(/(^\s*)|(\s*$)/g, "");
};

String.prototype.startWith=function(str){     
	  var reg=new RegExp("^"+str);     
	  return reg.test(this);        
	}; 
	 
	String.prototype.endWith=function(str){     
	  var reg=new RegExp(str+"$");     
	  return reg.test(this);        
	};

var lasListGridDefaultAdvFilterOperatorArr=['equals',
                                            'contains',
                                            'greaterOrEqual',
                                            'lessOrEqual',
                                            'notEqual',
                                            'notContains'];
	
	
	
	
	
var defaultSortFun =function(param,fieldArrays,replaceSortFunction,defaultSort){
	var order ='';
//	alert(isc.JSON.encode(param));
	if(!isc.propertyDefined(
			param, "multiSortBy")){
		//param.multiSortBy ='';
	}else{
		var sortDesc ='ASC nulls last';
		for(var i =0;i<param.multiSortBy.length;i++){
			
			if (param.multiSortBy[i].startWith('-')){
				sortDesc="DESC nulls last";
				s=param.multiSortBy[i].substring(1);
			}else{
				s=param.multiSortBy[i];
			}
			s=replaceSortFunction(s,fieldArrays);
			order = order + s +' '+sortDesc+',';
		}
		if (order !=''){
			param.multiSortBy = 'Order By '+order.substring(0, order.length-1);
		}else{
			param.multiSortBy = defaultSort==null?'':defaultSort;
		}
	}
//	alert(param.order);
};
 

var defaultReplaceWorkFlowSort = function (key,fa){
	var field=null;
	var name=null;
	
	for ( var f in fa ){ // 方法 
		  if ( typeof ( fa [ f ]) != "function" ){ 
			  field= fa [ f ] ; 
			  
			  name = field.name;
			  
			  if (name==key){
				  
				  if (field.sortName&&field.sortName!=''){
					  
					  return field.sortName;
				  }
				  
			  }
			   
		  }
		    field = null;
			name = null;
	  } 
	
 
	return key;
	
};

var buildColumnProvider = function(fa) {

	var field = null;
	var provider = null;
	var colDef = null;
	var colType = null;
	var canExport=null;
 
	var r = {};
	var name = null;
	var title = null;

	  for ( var f in fa ){ // 方法 
		  if ( typeof ( fa [ f ]) != " function " ){ 
			  field= fa [ f ] ; 
			  
			  name = field.name;
			  title=field.title;
				if (isc.propertyDefined(field, "provider")) {
					provider = field.provider;
				} else {
					provider = '';
				}
				if (isc.propertyDefined(field, "colDef")) {
					colDef = field.colDef;
				} else {
					if (isc.propertyDefined(field, "sortName")) {
						colDef = field.sortName;
					} else {
						colDef = '';
					}
				}
				if (isc.propertyDefined(field, "colType")) {
					colType = field.colType;
				} else {
					colType = '';
				}

				if (isc.propertyDefined(field, "canExport")) {
					canExport = field.canExport;
				} else {
					canExport = false;
				}
				r[name] = "{\"name\":\""+name +"\",\"title\":\""+title +"\",\"provider\":\""+provider + "\",\"colDef\":\"" + colDef + "\",\"colType\":\""+ colType + "\",\"canExport\":\""+ canExport + "\"}";
		  }
		    field = null;
			provider = null;
			colDef = null;
			colType = null;
			canExport=false;
			name = null;
			title=null;
	  } 
	
	 
	return r;
};



var getFieldGroupBy = function(fa,name) {
	var field = fa[name];
	if (isc.propertyDefined(field, "groupBy")) {
		 return field.groupBy;
	}else{
		return '';
	}
 
};
 
var form2json = function(form) {

	var r = {};

	for (var i = 0; i < form.elements.length; i++) {
		var el = form.elements[i];
		if (el.type == 'text'|| el.type == 'textarea') {
			if (el.name != '' && el.value != '') {

				r[el.name] = el.value.trim();
			}
		}if (el.type == 'hidden') {
			if (el.name != '' && el.value != '') {
				if($.isArray(r[el.name])){
					r[el.name].push(el.value.trim()); 
				}else if(r[el.name]!=null){
					r[el.name]=[r[el.name],el.value.trim()];
				}else{
					r[el.name] = el.value.trim();
				}
			}
		} else if (el.type == 'checkbox') {
			if (el.checked) {
				if (el.name != '' && el.value != '') {
					if($.isArray(r[el.name])){
						r[el.name].push(el.value.trim()); 
					}else if(r[el.name]!=null){
						r[el.name]=[r[el.name],el.value.trim()];
					}else{
						r[el.name] = el.value.trim();
					}
				}
			}
		}else if(el.type == 'radio'){
			if (el.checked) {
				if (el.name != '' && el.value != '') {
					r[el.name] = el.value.trim();
				}
			}
		} else if (el.tagName == 'SELECT') {
			for (var selcount = 0; selcount < el.options.length; selcount++) {
				if (el.options[selcount].selected) {
					if (el.options[selcount].value != '') {
						r[el.name] = el.options[selcount].value;
					}
				}
			}
		}
	}

	return r;

};

var formHidden2json = function(form) {

	var r = {};

	for (var i = 0; i < form.elements.length; i++) {
		var el = form.elements[i];
		if (  el.type == 'hidden' ) {
			if (el.name != '' && el.value != '') {

				r[el.name] = el.value.trim();
			}
		} 
	}
	return r;
};



var iscResizeEvent = function(str) {
	isc.Page.setEvent(isc.EH.RESIZE, str, null);
	eval(str);
	try{$(window).resize(eval(str));}catch(e){alert(e);}
};

var getListGridColumnFilter = function(listgrid, dsRequest) {
	var cf = null;
//	alert(isc.JSON.encode(dsRequest));
	if (listGrid.$32e) {
		cf = listGrid.$32e();
	}
	return cf;
};

var getListGridMultiSortBy = function(listgrid, dsRequest) {
 
	return dsRequest.sortBy == null ? '' : dsRequest.sortBy;
	 
};


isc.defineClass("LasDataSource", "RestDataSource")
		.addProperties(
				{

					dataFormat : "json",
					jsonRecordXPath : "/data",
					listGrid : null,
					requestProperties : {
						httpMethod : "POST"
					},
					operationBindings : [ {
						operationType : "fetch",
						dataProtocol : "postParams"
					} ],
					extVar : {
						provider : {},
						codDef : {},
						reQueryParams : {},
						paginatorVar : {
							sizePerPage : 0,
							totalSize : 0,
							currPage : 0,
							pageCount : 0,
							hasPrev : false,
							hasNext : false,
							hasFirst : false,
							hasLast : false,
							prevIndex : 0,
							lastIndex : 0,
							nextIndex : 0
						}
					},
					replaceSortFunction:defaultReplaceWorkFlowSort,
					sortFunction:defaultSortFun,
					defaultSort:null,
					getExtVar : function() {
						return this.extVar;
					},

					syncPaginatorVer : function(jsonData) {

						this.extVar.paginatorVar.totalSize = jsonData.totalSize;
						this.extVar.paginatorVar.currPage = jsonData.currPage;
						this.extVar.paginatorVar.pageCount = jsonData.pageCount;
						this.extVar.paginatorVar.sizePerPage = jsonData.sizePerPage;

						this.extVar.paginatorVar.prevIndex = (jsonData.currPage - 2)
								* jsonData.sizePerPage;
						this.extVar.paginatorVar.nextIndex = (jsonData.currPage)
								* jsonData.sizePerPage;
						this.extVar.paginatorVar.lastIndex = (jsonData.pageCount - 1)
								* jsonData.sizePerPage;

						if (jsonData.pageCount > 0) {
							if (jsonData.currPage == 1) {

								if (jsonData.currPage != jsonData.pageCount) {

									this.extVar.paginatorVar.hasFirst = false;
									this.extVar.paginatorVar.hasLast = true;
									this.extVar.paginatorVar.hasPrev = false;
									this.extVar.paginatorVar.hasNext = true;

								} else {
									this.extVar.paginatorVar.hasFirst = false;
									this.extVar.paginatorVar.hasLast = false;
									this.extVar.paginatorVar.hasPrev = false;
									this.extVar.paginatorVar.hasNext = false;

								}
							} else if (jsonData.currPage != 1) {

								if (jsonData.currPage == jsonData.pageCount) {

									this.extVar.paginatorVar.hasFirst = true;
									this.extVar.paginatorVar.hasLast = false;
									this.extVar.paginatorVar.hasPrev = true;
									this.extVar.paginatorVar.hasNext = false;

								} else {
									this.extVar.paginatorVar.hasFirst = true;
									this.extVar.paginatorVar.hasLast = true;
									this.extVar.paginatorVar.hasPrev = true;
									this.extVar.paginatorVar.hasNext = true;
								}

							} else {
								this.extVar.paginatorVar.hasFirst = false;
								this.extVar.paginatorVar.hasLast = false;
								this.extVar.paginatorVar.hasPrev = false;
								this.extVar.paginatorVar.hasNext = false;
							}
						}
					},
					transformRequest : function(dsRequest) {
						// var filterCritria =
						// myListGrid.getFilterEditorCriteria();
						// alert('dsRequest:'+isc.JSON.encode(dsRequest));
						if (!isc.propertyDefined(dsRequest.initParams,"smartClient")) {
							 isc.addProperties(
										dsRequest.initParams,
										{smartClient : "true"});
						} 

						if (isc.propertyDefined(dsRequest.initParams,"sizePerPage")) {
							this.extVar.paginatorVar.sizePerPage = dsRequest.initParams.sizePerPage;
						} else {
							isc.addProperties(
								dsRequest.initParams,
								{sizePerPage : this.extVar.paginatorVar.sizePerPage});
						}
						if (!isc.propertyDefined(dsRequest.initParams,"startIndex")) {
							isc.addProperties(dsRequest.initParams, {
								startIndex : 0
							});
						}
						var params = dsRequest.initParams;

						if (dsRequest.operationType == "fetch") {
							if ('fetch' == dsRequest.__operType) {
								//alert('dsRequest:' + isc.JSON.encode(dsRequest));
								// alert('111:'+isc.JSON.encode(getListGridColumnFilter(this.listGrid,dsRequest));
								if (isc.propertyDefined(dsRequest,"columnFilter")) {
									params.columnFilter = dsRequest.columnFilter;
								}
			
								if (this.listGrid.advancedFilter!=null){
									if (this.listGrid.advancedFilter.validate()){
										var criteria = this.listGrid.advancedFilter.getCriteria();
									    var car =  criteria.criteria;
									    if (car!=0){
									    	params.advancedFilter=isc.JSON.encode(this.listGrid.advancedFilter.getCriteria());
									    }
									}
	 							}
								
								isc.addProperties(dsRequest.initParams, {
									multiSortBy : getListGridMultiSortBy(
											this.listGrid, dsRequest)
								});

							} else {
								params = this.extVar.reQueryParams;
								params.multiSortBy = getListGridMultiSortBy(
										this.listGrid, dsRequest);

								if ('page' == dsRequest.__operType) {

									params.startIndex = dsRequest.initParams.startIndex;

								} else if ('requery' == dsRequest.__operType) {

								} else if ('changePageSize' == dsRequest.__operType) {

									if (isc.propertyDefined(
											dsRequest.initParams, "startIndex")) {
										params.startIndex = dsRequest.initParams.startIndex;
									}
									if (isc.propertyDefined(dsRequest.initParams,"sizePerPage")) {
										params.sizePerPage = dsRequest.initParams.sizePerPage;
									}
								} else {
								//	alert('2222:' + isc.JSON.encode(params));
									isc.addProperties(params, {
										columnFilter : dsRequest.data
									});
									params.startIndex = 0;
									params.sizePerPage = this.extVar.paginatorVar.sizePerPage;
								}
							}
 
							if (this.sortFunction){
								this.sortFunction(params,this.fields,this.replaceSortFunction,this.defaultSort);
							}
						//	alert('fields:' + isc.JSON.encode(this.fields));
								var  provider = buildColumnProvider(this.fields);
							
							//	alert('provider:' + isc.JSON.encode(provider));
								isc.addProperties(params, {provider : provider});
								this.extVar.reQueryParams = params;
 
							//	alert('params:' + isc.JSON.encode(params));
							return params;
						} else {
							return false;
						}
					},
					transformResponse : function(dsResponse, dsRequest,
							jsonData) {
						if (this.listGrid.onDataArrived){
							this.listGrid.onDataArrived(jsonData);
						}		
						dsResponse.data = jsonData.data;
						dsResponse.startRow = 0;
						dsResponse.endRow = dsResponse.totalRows;
						this.syncPaginatorVer(jsonData);
						if (this.listGrid.paginatorPane!=null){
							this.syncPaginatorPane(jsonData);
						}
						return dsResponse;
					},
					syncPaginatorPane : function(jsonData) {
						var label = this.listGrid.paginatorPane.pageInfoLabel;
					//	alert(isc.JSON.encode(label));
						var firstEle = this.listGrid.paginatorPane.members[0];
						var prevEle = this.listGrid.paginatorPane.members[1];
						var nextEle = this.listGrid.paginatorPane.members[2];
						var lastEle = this.listGrid.paginatorPane.members[3];
//						var endIndex = this.extVar.paginatorVar.sizePerPage == 0 ? jsonData.totalSize
//								: jsonData.startIndex + jsonData.sizePerPage;
						try {
							var l = '' + jsonData.totalSize
									+ '&nbsp;条&nbsp;,';
//							if (jsonData.totalSize > 0) {
//								l += '显示[&nbsp;' + (jsonData.startIndex + 1)
//										+ '-' + endIndex
//										+ '&nbsp;]条&nbsp;&nbsp;';
//							} else {
//							}
							if (jsonData.totalSize > 0) {
								l += '&nbsp;' + jsonData.currPage
										+ '&nbsp;/&nbsp;'+jsonData.pageCount+'&nbsp;页';
							}else{
								l += '&nbsp;' + jsonData.pageCount + '&nbsp;页';
							}
							if(label&&label!=null){
							label.setContents(l);
						//	alert(l);
							}

							if (this.extVar.paginatorVar.hasFirst) {
								firstEle.setDisabled(false);
							} else {
								firstEle.setDisabled(true);
							}
							if (this.extVar.paginatorVar.hasPrev) {
								prevEle.setDisabled(false);
							} else {
								prevEle.setDisabled(true);
							}
							if (this.extVar.paginatorVar.hasNext) {
								nextEle.setDisabled(false);
							} else {
								nextEle.setDisabled(true);
							}
							if (this.extVar.paginatorVar.hasLast) {
								lastEle.setDisabled(false);
							} else {
								lastEle.setDisabled(true);
							}
						} catch (e) {
							alert(e);
						}
					}
				});

isc.defineClass("LasListGrid", "ListGrid").addProperties({
	dataPageSize : 100000000,
	width : '100%',
	height : '100%',
	enableExport:false,
	enableAdvancedFilter:false,
    exportCurrentPage:false,
    exportCallBackClassName:'',
    exportCustomFields:'',
    exportFileName:'',
	paginatorPane : null,
	gridComponents : null,
	alternateRecordStyles : true,
	canDragSelectText:true,
	canMultiSort : true,
	autoFetchData : false,
	cellHeight: 30,
	advancedFilter:null,
	advancedFilterMethod:null,
	advancedFilterWindow:null,
	exportWindow:null,
	queryCallbacks:[],//查询默认回调方法
	_fetchDataInner :function(listgrid, customRequest, type,callback) {
		var t = new Date().getTime();
		var params = {
			__requestTimestamp : t
		};
		isc.addProperties(customRequest, {
			__operType : type 
		});
		
		//listgrid.fetchData(params, callback, customRequest);
		listgrid.fetchData(params, function(){
			if(callback){
				callback.call(listgrid, arguments[0],arguments[1],arguments[2]);
			}
			for(var i=0; i<listgrid.queryCallbacks.length; i++){
				listgrid.queryCallbacks[i].call(listgrid, arguments[0],arguments[1],arguments[2]);
			}
			//listgrid.markForRedraw();
		}, customRequest);
	 
	},
	query: function(formObj,extParam,callback) {
		var param = {
			initParams : {
				startIndex : extParam.startIndex? extParam.startIndex:0,
				sizePerPage : extParam.sizePerPage? extParam.sizePerPage:50,
				smartClient:'true'
			}
		};
		if (formObj&&formObj!=null){
		isc.addProperties(param.initParams, form2json(formObj));
		}
		isc.addProperties(param.initParams, extParam );
		// alert('12321321'+isc.JSON.encode(param));
		if (this.paginatorPane!=null&&this.paginatorPane){
			this.updatePaginatorPaneOnQuery(this,extParam);
			
		}
		
	
		this._fetchDataInner(this, param, 'fetch',callback);
	},
	updatePaginatorPaneOnQuery:function(grid,extParam){
		var changePageSizeForm=	grid.paginatorPane.changePageSizeForm ;
		if (changePageSizeForm!=null&&changePageSizeForm){
				changePageSizeForm.setValue("selectPageSize",extParam.sizePerPage);
			}
	},
getRowSpan:function(record, rowNum, colNum) {
		
		var columnName = this.getFieldName(colNum);
		
		var groupBy =  getFieldGroupBy(this.dataSource.fields,columnName);
		 var j = 0;         
		if (groupBy!=null&&groupBy!=''){
			
			var groupByValue  =  record[groupBy];
			var columnValue  =  record[columnName];
			   var endRow = this.getDrawnRows().get(1);
			 for(i=rowNum;i<endRow+1;i++){       
                 if(groupByValue ==this.data.get(i)[groupBy]){                           
                         j = j+1;                     
                  } else {                         
                       break;                      
                  }           
        	}
			 return j;
		}else{
			return 1;
		}
 		
	 
	},
	onDataArrived:function(data){},
	allowRowSpanning:true,
	showRollOverCanvas : true,
	reQuery: function(callback) {
		this._fetchDataInner(this, {}, 'requery',callback);
	},
	goFirstPage : function() {
		this._fetchDataInner(this, {initParams:{startIndex:0}}, 'page');
	},
	goPrevPage : function() {
		this._fetchDataInner(this, {initParams:{startIndex:this.dataSource.extVar.paginatorVar.prevIndex}}, 'page');
	},
	goNextPage : function(listgrid, customRequest) {
		this._fetchDataInner(this, {initParams:{startIndex:this.dataSource.extVar.paginatorVar.nextIndex}}, 'page');
	},
	goLastPage : function(listgrid, customRequest) {
		this._fetchDataInner(this, {initParams:{startIndex:this.dataSource.extVar.paginatorVar.lastIndex}}, 'page');
	},
	goPage : function(value) {
		this.dataSource.extVar.paginatorVar.sizePerPage=value;
		this._fetchDataInner(this, {initParams:{sizePerPage:value,startIndex:0}}, 'changePageSize');
	},
	exportAll : function() {
			var params = this.getExportParam();
			top.waitSubmit();
			
		
			RPCManager.sendRequest({  
			    actionURL:this.dataSource.fetchDataURL,  
			    params:params,  
			    callback:function(data) {
			    	top.$.unblockUI();
			        var c= eval("("+data.data+")");
			       	window.location.href=getContextPath()+c.actionPath+'.action?encodeStr='+c.encodeStr; 
			    }  
			});  
	},
	getExportParam : function() {
		var	params = this.dataSource.extVar.reQueryParams;
		 
			if (!isc.propertyDefined(params,"enableExport")) {
				 isc.addProperties(
						 params,
							{enableExport : "true"});	 
			}
			//if (!isc.propertyDefined(params,"exportCustomFields")) {
				 isc.addProperties(
						 params,
							{exportCustomFields : this.exportCustomFields});	 
			//}
			
			
			if (!isc.propertyDefined(params,"exportFileName")) {
				 isc.addProperties(
						 params,
							{exportFileName : this.exportFileName});	 
			}
	 
			if (!isc.propertyDefined(params,"exportCurrentPage")) {
				 isc.addProperties(
						 params,
							{exportCurrentPage : "false"});	 
			}
			if (!isc.propertyDefined(params,"exportCallBackClassName")) {
			 isc.addProperties(
					 params,
						{exportCallBackClassName : this.exportCallBackClassName});
			}
		//	alert(isc.JSON.encode(this.exportCustomFields));
			params.startIndex = 0;
			params.sizePerPage = 0;
			return params;
	},
	ready:function(readyparams){
		if (this.paginatorPane!=null){
			this.paginatorPane.grid = this;
		}
		
		for (var i=0;i<readyparams.fields.length;i++){
	 
			if (!isc.propertyDefined(readyparams.fields[i],"validOperators")) {
				 isc.addProperties(
						 readyparams.fields[i],
							{validOperators : lasListGridDefaultAdvFilterOperatorArr});
			} 
			if (!isc.propertyDefined(readyparams.fields[i],"canFilter")) {
				 isc.addProperties(
						 readyparams.fields[i],
							{canFilter : false});
			} 
		}
		if (isc.propertyDefined(readyparams,"onDataArrived")) {
			this.onDataArrived=readyparams.onDataArrived;
		} 
		
		this.setDataSource(_createDataSource(readyparams.fields,this,readyparams.defaultSort,readyparams.url));
		this.dataProperties = _createDataProperties(this);
		this.advancedFilter=readyparams.advancedFilter;
		if (this.enableAdvancedFilter==true){
			this.advancedFilterWindow=buildAdvancedFilterWindow(this);
		}
		if (this.enableExport==true){
			this.exportWindow=buildExportWindow(this,readyparams.fields);
		}
	}
});
isc.defineClass("PaginatorPaneToolStrip", "ToolStrip").addProperties({
	changePageSizeForm:null,
	pageInfoLabel:null
});


isc.defineClass("LasAListGrid", "LasListGrid").addProperties({
	position:'absolute',//absolute
	width : '100%',
	height : '100%'
});

isc.defineClass("LasRListGrid", "LasListGrid").addProperties({
	position:'relative',//absolute
	width : '100%',
	height : '100%'
});

isc.defineClass("GoFirstButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/first.gif",prompt : "首页"
});

isc.defineClass("GoPrevButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/prev.gif",prompt : "上一页"
});

isc.defineClass("GoNextButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/next.gif",prompt : "下一页"
});

isc.defineClass("GoLastButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/last.gif",prompt : "尾页"
});
isc.defineClass("RefreshButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/refresh.gif",prompt : "刷新"
});
isc.defineClass("ExportAllButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/toexcel.png",prompt : "导出EXCEL"
});
isc.defineClass("ExcelExportButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/toexcel.png",prompt : "导出EXCEL"
});
isc.defineClass("AdvancedFilterButton", "ToolStripButton").addProperties({
	icon : "[SKIN]/actions/page/filter.png",prompt : "筛选"
});
isc.defineClass("ChangePageSizeForm", "DynamicForm").addProperties({
	showResizeBar:false,
	width:65,
	minWidth:65,
	numCols:1
});






var containerFull = function(ele, type) {
	if ('LB' == type) {
		
		
		var docbodych = (document.documentElement.clientHeight !=0) ? document.documentElement.clientHeight : document.body.clientHeight;
		
	
		
		//var ddbch = $(ele).position().top;
		
		docbodych=	(docbodych - 100 - 3);

		$(ele).css("height", docbodych + 'px');
 
	//	alert($(ele).width() +":"+ $(ele).height());
		
		
	}
};

var containerFullLB = function(ele) {
	containerFull(ele, 'LB');
};

var resizeISCObjWithContainer = function(ISCObj, container) {
	containerFullLB(container);
 
	ISCObj.resizeTo($(container).width() , $(container).height() );

};



var seqFieldFormatter=function(value, record, rowNum, colNum, grid){
	
	if(grid.dataSource.extVar.paginatorVar.sizePerPage==0){
		return rowNum+1;
	}else{
		var v =(grid.dataSource.extVar.paginatorVar.currPage-1)*grid.dataSource.extVar.paginatorVar.sizePerPage;
		return v+rowNum+1;
	}
};
  var seqField = {name:"seq", title:"序号",type:"text",formatCellValue : seqFieldFormatter,autoFitWidth:false,canSort:false,frozen:true,width:40,canHide:false,canGroupBy:false,canFreeze:false,canFilter:false,canExport:false,canEdit:false,canHilite:false,canToggle:false,canReorder:false };

var flowDidlogFormatter=function(value, record, rowNum, colNum, grid){
	var imagespath= getContextPath()+'/images/';
	var flowSrc =imagespath +'workdiag.png';	
	var flowImg = "<img border='0' src='"+flowSrc+"' />";
	return '<a href="'+getContextPath()+'/comm/processMonitor.action?piid='+ record.piid+'" target="_blank">'+flowImg+'</a>'; 
	 
	
};

var flowDialogField = {name:"flowDialog", title:"流程图",type:"text",formatCellValue : flowDidlogFormatter,autoFitWidth:false,frozen:true,width:30,canSort:false,canHide:false,canGroupBy:false,canFreeze:false,canFilter:false,canExport:false,canEdit:false,canHilite:false,canToggle:false,canReorder:false};


var canWithDrawFormatter=function(value, record, rowNum, colNum, grid){
	
	if (record.wiid!=null && record.wiid!=''&& record.canWithdraw=='true'){
		var imagespath= getContextPath()+'/isomorphic/skins/Enterprise/images/actions/';
		 
		var flowImg ='<img border="0" src="'+imagespath +'undo.png" />';
		
		return flowImg; 
		
	}else{
		
		return "";
	}
	
};
var goBackFormatter=function(value, record, rowNum, colNum, grid){
	
	if (record.goBack=='true'||record.goBack==true){
		var imagespath= getContextPath()+'/isomorphic/skins/Enterprise/images/actions/';
		 
		var flowImg ='<img border="0" src="'+imagespath +'undo.png" />';
 
		return flowImg; 
		
	}else{
		
		return "";
	}
	
};


var canWithDrawField = {name:"canWithDraw", title:"可撤回",type:"link",formatCellValue : canWithDrawFormatter,frozen:true,autoFitWidth:false,width:40,canSort:false,canHide:false,canGroupBy:false,canFreeze:false,canFilter:false,canExport:false,canEdit:false,canHilite:false,canToggle:false,canReorder:false};

var goBackField = {name:"goBack", title:"退",type:"link",formatCellValue:goBackFormatter,frozen:true,autoFitWidth:false,width:40,canSort:false,canHide:false,canGroupBy:false,canFreeze:false,canFilter:false,canExport:false,canEdit:false,canHilite:false,canToggle:false,canReorder:false};


function createLasAListGrid(_id, _url, _fields, _defaultSort,isPaging,_getRollOverCanvas){
	var _tool = null;
	if (isPaging){
		_tool=_createToolStrip();
	}
	var _grid = isc.LasAListGrid.create({
		ID:_id,
		paginatorPane:_tool,
		gridComponents : isPaging?[ "header","filterEditor","body",_tool]:[ "header","filterEditor","body"],
		getRollOverCanvas:_getRollOverCanvas==null?function(rowNum, colNum){return null;}:_getRollOverCanvas,
		canDragSelectText:true
	});
	_grid.setDataSource(_createDataSource(_id,_fields,_grid,_defaultSort,_url));
	_grid.dataProperties = _createDataProperties(_grid);
	if (isPaging){
		_tool.grid = _grid;
	}
	return _grid;
}

function createLasRListGrid(_id, _url, _fields, _defaultSort,isPaging,_getRollOverCanvas){
	var _tool = null;
	if (isPaging){
		_tool=_createToolStrip();
	}
	var _grid = isc.LasRListGrid.create({
		ID:_id,
		paginatorPane:_tool,
		gridComponents : isPaging?[ "header","filterEditor","body",_tool]:[ "header","filterEditor","body"],
		getRollOverCanvas:_getRollOverCanvas==null?function(rowNum, colNum){return null;}:_getRollOverCanvas,
		canDragSelectText:true
	});
	_grid.setDataSource(_createDataSource(_id,_fields,_grid,_defaultSort,_url));
	_grid.dataProperties = _createDataProperties(_grid);
	if (isPaging){
		_tool.grid = _grid;
	}
	return _grid;
}

function _createToolStrip(_grid,param){
	var showChangePageSize = true;
	if (param&&param!=null){
		if (isc.propertyDefined(param,"showChangePageSize")){
			showChangePageSize=param.changePageSizeForm;
		}
	}
	var _tool =null;
	var memberArr =new Array();
	
 
	memberArr.push(isc.GoFirstButton.create({click : function() {_tool.grid.goFirstPage();}}));
	memberArr.push(isc.GoPrevButton.create ({click : function() {_tool.grid.goPrevPage(); }}));
	memberArr.push(isc.GoNextButton.create ({click : function() {_tool.grid.goNextPage(); }}));
	memberArr.push(isc.GoLastButton.create ({click : function() {_tool.grid.goLastPage(); }}));
	memberArr.push(isc.RefreshButton.create({click : function() {_tool.grid.reQuery(); }}));
	if (param&&param!=null){
		if (isc.propertyDefined(param,"showExportAll")){
			if (param.showExportAll==true){
				memberArr.push(isc.ExportAllButton.create({click : function() {_tool.grid.exportWindow.show(); }}));
			}
		}
	}
	if (param&&param!=null){
		if (isc.propertyDefined(param,"showAdvancedFilter")){
			if (param.showAdvancedFilter==true){
				memberArr.push(isc.AdvancedFilterButton.create({click : function() {_tool.grid.advancedFilterWindow.show(); }}));
			}
		}
	}

	var c =null;
	if (showChangePageSize){
		 c =isc.ChangePageSizeForm.create({
	    	fields: [{
	    		name: "selectPageSize",showTitle: false,width:"65",
	         	valueMap: {"20": "20条","50": "50条","100": "100条"}, 
	         	defaultValue:"50",
	         	changed:function(form,item,value){
	         		_tool.grid.goPage(value);
	         	},
	         	redrawOnChange:true
	        }]
	    });
		memberArr.push(c);
	}
	var b6=isc.Label.create({padding : 5,width : "60%" });
	memberArr.push(b6);
	

	_tool = isc.PaginatorPaneToolStrip.create({
		width : "100%",
		height : 24,
		grid: _grid,
		members : memberArr,
		changePageSizeForm:c,
		pageInfoLabel:b6
	});
	return _tool;
}



function _createDataProperties(_grid){
	var _dp = isc.ResultSet.create({
 		dataSource :_grid.dataSource,
 		useClientSorting : false,
 		useClientFiltering : false
 	});
	return _dp;
}

function _createDataSource(_fields,_grid,_defaultSort,_url){
	return isc.LasDataSource.create({
	 	fields:_fields,
	   	listGrid:_grid,
	   	paginatorPane :_grid.paginatorPane,
	    allowAdvancedCriteria : true,
	    defaultSort:_defaultSort,
	    fetchDataURL : _url
	    
	});
}

function _createGridComponents(_paginatorPane){
	if (_paginatorPane==null){
		return [ "header","body"];
	}else{
		return [ "header","body",_paginatorPane];
	}
}

function _createTab(_id, url){
	return isc.HTMLPane.create({
		ID:_id,
		contentsURL:url,
		contentsType:"page"
	});
}
/**
 * 构建一般的cnas的ListGrid
 * @param param
 * @returns
 */
function buildCommListGrid(param, rollCanvas){
	if(param==null){
		param = {
			ID:'myListGrid'
		};
	}
	var myPaginatorPane = _createToolStrip(eval(param.ID));
	var defaultParam = {
		paginatorPane:myPaginatorPane,
		gridComponents : [ "header","body",myPaginatorPane],
		width : '100%',
		height : '100%',
		getRollOverCanvas:function(rowNum, colNum){
			if(rollCanvas){
				var _record = this.getRecord(rowNum);
				var record = _record;
				return rollCanvas.getRollCanvas({record:record,oldrecord:_record});
			}else{
				return null;
			}
		}
	};
	param = mergeAddAttr(param, defaultParam);
	var listGrid = isc.LasRListGrid.create(param);
	return listGrid; 
}


var buildAdvancedFilterWindow = function(listGrid){
	
	listGrid.advancedFilter = isc.FilterBuilder.create({
	    dataSource:listGrid.dataSource,
	    topOperatorAppearance: "radio",
	    height:"200",
	    width:"100%"
	});
 
	return isc.Window.create({
		title:"筛选",
		width :  '600',
		height :'300',
		autoSize :   false,
		autoCenter : true,
		isModal : false,
		showModalMask : false,
		showMaximizeButton : true,
		showMinimizeButton : false,
		autoDraw : false,
		subCallBack : "",
		canDragReposition :  true,
		canDragResize : true,
		items : [isc.VLayout.create({
			width : "100%",
			height : "100%",
			members : [ isc.ToolStrip.create({width:"100%",height:30,
						members:[
			        	isc.Button.create({title:"重置筛选",width:'80',click : function() {listGrid.advancedFilter.clearCriteria(); }}),
			         	 isc.Button.create({title:"查询",width:'60',click : function() {listGrid.advancedFilterMethod(); }})
			       		]}),
			   			 isc.Canvas.create({height:"*",width:"100%",children:[listGrid.advancedFilter ]})
				]
			})
		]
	});
 
}

var buildExportFormData=function(array){
	var data =new Array();
	var obj = null;
	for (var i =0;i<array.length;i++){
		
		obj=array[i];

	//	alert(isc.JSON.encode(obj["canExport"]+"   "+obj.canExport));
 
		if (obj.canExport==true){
			
				data.push({name:obj.name,title:obj.title});
		}
		
	}
	return data;
};

var buildExportWindow = function(listGrid,gridFields){
	var exportChooseForm=  isc.ListGrid.create({
		fields:[{name:"name",width:0,title:"可选列",type:"text",hidden:true} ,   {name:"title",width:"100%",title:"可选列",type:"text"}],
		  data:null,
		canDragRecordsOut: true,
        canAcceptDroppedRecords: true,
        canReorderRecords: true,
        dragDataAction: "move",
        rowDoubleClick:function (viewer, record, recordNum, field, fieldNum, value, rawValue){
        	 exportChooseTo.transferSelectedData(exportChooseForm);
        }
    });

	var exportChooseTo=isc.ListGrid.create({
		  fields:[{name:"name",width:0,title:"已选列",type:"text",hidden:true} ,   {name:"title",width:"100%",title:"已选列",type:"text"}],
	        data:null,
	        canDragRecordsOut: true,
	        canAcceptDroppedRecords: true,
	        canReorderRecords: true,
	        rowDoubleClick:function (viewer, record, recordNum, field, fieldNum, value, rawValue){
	        	exportChooseForm.transferSelectedData(exportChooseTo);
	        }
 
	});

	var exportData = buildExportFormData(gridFields);
	 
	exportChooseForm.setData(exportData);
	exportChooseTo.setData(null);
	
	var window=isc.Window.create({
		title:"导出列选择",
		width :  '520',
		height :'300',
		autoSize :   false,
		autoCenter : true,
		isModal : false,
		showModalMask : false,
		showMaximizeButton : true,
		showMinimizeButton : false,
		autoDraw : false,
		exportChooseForm:exportChooseForm,
		exportChooseTo:exportChooseTo,
		exportData:exportData,
		gridFields:gridFields,
		subCallBack : "",
		canDragReposition :  true,
		canDragResize : true,
		items : [ isc.VLayout.create({
					width : "100%",
					height : "100%",
					members : [
						     isc.HStack.create({ align:"center",membersMargin:10, width:'100%',height:'*', members:[
						          exportChooseForm,
						                                                               
						          	isc.VStack.create({ align:"center",width:32, height:74, layoutAlign:"center", membersMargin:10, members:[
							     
		                                  isc.IButton.create({
		                                	  align:"center",
		  						    	    width: 40,
		  						    	    title: ">",
		  						    	    click:function(){
		  						    	      exportChooseTo.transferSelectedData(exportChooseForm);
		  							         }
		  						    	   }),
		  						    	  isc.IButton.create({
		  						    		 align:"center",
		  						    	    width: 40,
		  						    	    title: "<",
		  						    	    click:function(){
		  						    	      exportChooseForm.transferSelectedData(exportChooseTo);
		  							         }
		  						    	  	}),
		  						    	  isc.IButton.create({
								              align:"center",
								              width: 60,
								               title: "全选",
								               click:function(){
								            	   exportChooseForm.selectAllRecords();
								            	   exportChooseTo.transferSelectedData(exportChooseForm);
								               }
								          }),
									        isc.IButton.create({
										    	  align:"center",
										    	    width: 60,
										    	    title: "重置",
										    	    click:function(){
											        	 exportChooseForm.setData(buildExportFormData(gridFields));
											        	 exportChooseTo.setData(new Array());
											         }
										    	}),
										    	  isc.IButton.create({
										    		  align:"center",
											    	    width: 80,
											    	    title: "导入EXCEL",
											    	    click : function() {
													    	 var array = exportChooseTo.data;
													    		var obj=null;
													    		var str='';
													    		for(var i=0;i<array.length;i++){
													    			
													    			obj=array[i];
													    			
													    			str+=(obj.name+',');
													    		}
													    		if (str!=''){
													    			str=str.substring(0,str.length-1);
													    		}
													    	 
													    	 listGrid.exportCustomFields=str;
													    	 listGrid.exportAll();}
											    	})
	  						    	  ]}),                                                         
								      exportChooseTo
							      ]
						     }) 
					]
				}) 

		    
		]
	});
	
	return window;
	
	
};
 
var mergeBaseStyle=function(listgrid, funs){
	var mergeFun = function(record, rowNum, colNum){
		var css = 'cell';
		for(var i=0; i<funs.length; i++){
			var css1 = funs[i].call(this, record, rowNum, colNum);
			if(css1!='cell'){//取第一个不一般的样式。
				css = css1;
				break;
			}
		}
		if(css.startsWith('force_')){
			css = css.replace('force_','');
		}
		return css;
	}
	return mergeFun;
}
