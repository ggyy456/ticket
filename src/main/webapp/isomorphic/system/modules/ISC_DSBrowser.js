/*
 * Isomorphic SmartClient
 * Version v9.1p_2014-03-25 (2014-03-25)
 * Copyright(c) 1998 and beyond Isomorphic Software, Inc. All rights reserved.
 * "SmartClient" is a trademark of Isomorphic Software, Inc.
 *
 * licensing@smartclient.com
 *
 * http://smartclient.com/license
 */

if(window.isc&&window.isc.module_Core&&!window.isc.module_DSBrowser){isc.module_DSBrowser=1;isc._moduleStart=isc._DSBrowser_start=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc._moduleEnd&&(!isc.Log||(isc.Log&&isc.Log.logIsDebugEnabled('loadTime')))){isc._pTM={message:'DSBrowser load/parse time: '+(isc._moduleStart-isc._moduleEnd)+'ms',category:'loadTime'};if(isc.Log&&isc.Log.logDebug)isc.Log.logDebug(isc._pTM.message,'loadTime');else if(isc._preLog)isc._preLog[isc._preLog.length]=isc._pTM;else isc._preLog=[isc._pTM]}isc.definingFramework=true;isc.defineClass("DSRegistryList","ListGrid");isc.A=isc.DSRegistryList.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.dataSource="RepoRegistry";isc.B.push(isc.A.initWidget=function isc_DSRegistryList_initWidget(){this.Super("initWidget",arguments)});isc.B._maxIndex=isc.C+1;isc.defineClass("DSList","ListGrid");isc.A=isc.DSList.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_DSList_initWidget(){this.Super("initWidget",arguments)});isc.B._maxIndex=isc.C+1;isc.defineClass("DSBrowser","VLayout");isc.A=isc.DSBrowser;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.showWindow=function isc_c_DSBrowser_showWindow(_1,_2){isc.Window.create({title:"DS Builder",width:"100%",height:"100%",canDragReposition:false,closeClick:function(){this.destroy()},items:[isc.DSBrowser.create({autoDraw:false},_2)]},_1).show()});isc.B._maxIndex=isc.C+1;isc.A=isc.DSBrowser.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.dsRegistryListDefaults={_constructor:"DSRegistryList",height:150,autoFetchData:true,canHover:true,defaultFields:[{name:"ID",title:"Name"}],recordClick:"this.creator.dsRegistryChanged(record)"};isc.A.dsRegistryListRefreshButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/refresh.png",click:"this.creator.dsRegistryList.invalidateCache()"};isc.A.dsListDefaults={_constructor:"DSList",canHover:true,showFilterEditor:true,defaultFields:[{name:"ID",title:"Name"}],recordClick:"this.creator.dsChanged(record)"};isc.A.dsListAddButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/add.png",click:"this.creator.dsList.startEditingNew()"};isc.A.dsListRemoveButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/remove.png",click:"this.creator.dsList.removeSelectedData()"};isc.A.dsListRefreshButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/refresh.png",click:"this.creator.dsList.invalidateCache()"};isc.A.leftSectionDefaults={_constructor:"SectionStack",headerHeight:25,width:300,showResizeBar:true,animateSections:isc.Browser.isSafari,visibilityMode:"visible",autoParent:"mainLayout"};isc.A.mainLayoutDefaults={_constructor:"HLayout",height:"*"};isc.A.rightPaneDefaults={_constructor:"TabSet",tabs:[{name:"welcome",title:"Welcome",ID:"dsb_welcome_tab",canClose:true,pane:isc.Label.create({height:10,autoDraw:false,overflow:"visible",contents:"Select a datasource registry on the left..."})}]};isc.A.autoChildren=["mainLayout"];isc.A.dsRegistryPaneDefaults={_constructor:"DSRegistryPane"};isc.B.push(isc.A.initWidget=function isc_DSBrowser_initWidget(){this.Super("initWidget",arguments);this.dsRegistryList=this.createAutoChild("dsRegistryList");this.dsRegistryListRefreshButton=this.createAutoChild("dsRegistryListRefreshButton");this.dsList=this.createAutoChild("dsList");this.dsListAddButton=this.createAutoChild("dsListAddButton");this.dsListRemoveButton=this.createAutoChild("dsListRemoveButton");this.dsListRefreshButton=this.createAutoChild("dsListRefreshButton");this.leftSection=this.createAutoChild("leftSection",{sections:[{name:"registries",title:"DataSource Registries",expanded:true,controls:[this.dsRegistryListRefreshButton],items:[this.dsRegistryList]},{name:"datasources",title:"DataSources",expanded:true,controls:[this.dsListAddButton,this.dsListRemoveButton,this.dsListRefreshButton],items:[this.dsList]}]});this.addAutoChildren(this.autoChildren);this.mainLayout.addMember(this.leftSection);this.rightPane=this.createAutoChild("rightPane");this.mainLayout.addMember(this.rightPane)},isc.A.dsRegistryChanged=function isc_DSBrowser_dsRegistryChanged(_1){this.currentRegistry=_1;isc.DMI.call("isc_builtin","com.isomorphic.tools.BuiltinRPC","dsFromXML",_1.object,this.getID()+".dsLoaded(data)")},isc.A.dsLoaded=function isc_DSBrowser_dsLoaded(_1){this.currentDS=_1;this.showDSRegistryPane();this.dsList.setDataSource(_1);this.dsList.setFields([{name:"ID",title:"Name"}]);this.dsList.filterData()},isc.A.showDSRegistryPane=function isc_DSBrowser_showDSRegistryPane(){var _1=this.currentRegistry;this.showPane({ID:this.escapeForId("registryPane_"+_1.ID),title:_1.ID,paneClass:"dsRegistryPane"},_1)},isc.A.dsChanged=function isc_DSBrowser_dsChanged(_1){this.currentDS=_1;this.showDSPane()},isc.A.showDSPane=function isc_DSBrowser_showDSPane(){var _1=this.currentDS;this.showDSRegistryPane();var _2={};isc.addProperties(_2,_1,{registry:isc.clone(this.currentRegistry)});this.currentPane.showDSPane(_2)},isc.A.escapeForId=function isc_DSBrowser_escapeForId(_1){return isc.isA.String(_1)?_1.replace(/(\/|\.)/g,'_'):_1},isc.A.showPane=function isc_DSBrowser_showPane(_1,_2){var _3=this.rightPane.getTab(_1.ID);if(_3){this.currentPane=_3.pane;this.rightPane.selectTab(_3);return}
_3={};isc.addProperties(_3,_1,{canClose:true,pane:this.createAutoChild(_1.paneClass,{config:_2})});var _4=this.rightPane.getTab(0);if(_4&&_4.name=="welcome")this.rightPane.removeTab(0);this.rightPane.addTab(_3);this.rightPane.selectTab(_3);this.currentPane=_3.pane});isc.B._maxIndex=isc.C+8;isc.defineClass("DSRegistryPane","TabSet");isc.A=isc.DSRegistryPane.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.dsPaneDefaults={_constructor:"DSEditor"};isc.B.push(isc.A.initWidget=function isc_DSRegistryPane_initWidget(){this.Super("initWidget",arguments)},isc.A.showDSPane=function isc_DSRegistryPane_showDSPane(_1){var _2=this.escapeForId("dsPane_"+this.config.ID+'_'+_1.ID);this.showPane({ID:_2,title:_1.ID,paneClass:"dsPane"},_1)},isc.A.escapeForId=function isc_DSRegistryPane_escapeForId(_1){return isc.isA.String(_1)?_1.replace(/(\/|\.)/g,'_'):_1},isc.A.showPane=function isc_DSRegistryPane_showPane(_1,_2){var _3=this.getTab(_1.ID);if(_3){this.selectTab(_3);return}
_3={};isc.addProperties(_3,_1,{canClose:true,pane:this.createAutoChild(_1.paneClass,{config:_2})});this.addTab(_3);this.selectTab(_3);this.currentPane=_3.pane});isc.B._maxIndex=isc.C+4;isc.defineClass("DSEditor","SectionStack");isc.A=isc.DSEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.visibilityMode="visible";isc.A.fieldGridDefaults={_constructor:"ListGrid",canReorderRecords:true,canDragRecordsOut:false,canEdit:true,autoSaveEdits:true};isc.A.fieldGridAddButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/add.png",click:"this.creator.fieldGrid.startEditingNew()"};isc.A.fieldGridRemoveButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/remove.png",click:"this.creator.fieldGrid.removeSelectedData()"};isc.A.fieldGridRefreshButtonDefaults={_constructor:"Img",size:16,src:"[SKIN]/actions/refresh.png",click:"this.creator.fieldGrid.invalidateCache()"};isc.A.deriveFormDefaults={_constructor:"DynamicForm"};isc.A.dbListDefaults={_constructor:"DBCompactList"};isc.A.showSQLBrowserButtonDefaults={_constructor:"IButton",title:"Show SQL Browser",width:150,click:function(){isc.SQLBrowser.showWindow({width:"95%",height:"95%",isModal:true,autoCenter:true})}};isc.A.fetchOperationFormDefaults={_constructor:"DynamicForm",fields:[{name:"selectClause",title:"SELECT",formItemType:"AutoFitTextAreaItem",height:10,width:"*",colSpan:"*",defaultValue:"*"},{name:"tableClause",title:"FROM",formItemType:"AutoFitTextAreaItem",height:10,width:"*",colSpan:"*",defaultValue:""},{name:"whereClause",title:"WHERE",formItemType:"AutoFitTextAreaItem",height:10,width:"*",colSpan:"*",defaultValue:"$defaultWhereClause"},{name:"groupClause",title:"GROUP BY",formItemType:"AutoFitTextAreaItem",height:10,width:"*",colSpan:"*",defaultValue:"$defaultGroupClause"},{name:"orderClause",title:"ORDER BY",formItemType:"AutoFitTextAreaItem",height:10,width:"*",colSpan:"*",defaultValue:"$defaultOrderClause"}]};isc.A.actionBarDefaults={_constructor:"HLayout",height:20};isc.A.tryButtonDefaults={_constructor:"IButton",title:"Try it",click:"this.creator.tryIt()",autoParent:"actionBar"};isc.A.saveButtonDefaults={_constructor:"IButton",title:"Save",click:"this.creator.saveDS()",autoParent:"actionBar"};isc.A.previewGridDefaults={_constructor:"ListGrid",showFilterEditor:true};isc.B.push(isc.A.initWidget=function isc_DSEditor_initWidget(){this.Super("initWidget",arguments);this.fieldGrid=this.createAutoChild("fieldGrid",{fields:[{name:"title",title:"Title"},{name:"name",title:"Name"},{name:"width",title:"Width"},{name:"height",title:"Height"},{name:"operator",title:"Operator",valueMap:["equals","notEqual","greaterThan","lessThan","greaterOrEqual","lessOrEqual","contains","startsWith","endsWith","iContains","iStartsWith","iEndsWith","notContains","notStartsWith","notEndsWith","iNotContains","iNotStartsWith","iNotEndsWith","regexp","iregexp","isNull","notNull","inSet","notInSet","equalsField","notEqualField","and","not","or","between","betweenInclusive"]},{name:"formItemType",title:"Form Item Type"},{name:"tableName",title:"Table Name"},{name:"type",title:"Type"}]});this.fieldGridAddButton=this.createAutoChild("fieldGridAddButton");this.fieldGridRemoveButton=this.createAutoChild("fieldGridRemoveButton");this.addSection({ID:"fields",title:"Fields",expanded:true,items:[this.fieldGrid],controls:[this.fieldGridAddButton,this.fieldGridRemoveButton]});var _1=this;this.deriveForm=this.createAutoChild("deriveForm",{fields:[{name:"sql",showTitle:false,formItemType:"AutoFitTextAreaItem",width:"*",height:40,colSpan:"*",keyPress:function(_2,_3,_4){if(_4=='Enter'&&isc.EH.ctrlKeyDown()){if(isc.Browser.isSafari)_2.setValue(_2.getElementValue());_1.execSQL();if(isc.Browser.isSafari)return false}}},{type:"button",title:"Execute",startRow:true,click:this.getID()+".execSQL()"}]});this.dbList=this.createAutoChild("dbList");this.showSQLBrowserButton=this.createAutoChild("showSQLBrowserButton");this.addSection({ID:"derive",title:"Derive Fields From SQL",expanded:false,items:[this.deriveForm],controls:[this.dbList,this.showSQLBrowserButton]});this.fetchOperationForm=this.createAutoChild("fetchOperationForm");this.addSection({ID:"fetchOperation",title:"Fetch Operation",expanded:true,items:[this.fetchOperationForm]});this.actionBar=this.createAutoChild("actionBar");this.addSection({ID:"actionBar",showHeader:false,expanded:true,items:[this.actionBar]});this.addAutoChildren(["tryButton","saveButton"]);this.previewGrid=this.createAutoChild("previewGrid");this.addSection({ID:"preview",title:"Preview",items:[this.previewGrid]});this.loadDS(this.config)},isc.A.execSQL=function isc_DSEditor_execSQL(){var _1=this.deriveForm.getValue("sql");if(_1){_1=_1.trim().replace(/(.*);+/,"$1");var _2=isc.DataSource.get("DataSourceStore");_2.performCustomOperation("dsFromSQL",{dbName:this.dbList.getSelectedDB(),sql:_1},this.getID()+".deriveDSLoaded(data)")}},isc.A.deriveDSLoaded=function isc_DSEditor_deriveDSLoaded(_1){var _2=_1.ds;this.dsLoaded(_1.ds)},isc.A.loadDS=function isc_DSEditor_loadDS(_1){this.currentRegistry=_1;isc.DMI.call("isc_builtin","com.isomorphic.tools.BuiltinRPC","dsConfigFromXML",_1.object,this.getID()+".dsLoaded(data)")},isc.A.dsLoaded=function isc_DSEditor_dsLoaded(_1){var _2=isc.DataSource.create(_1);this.currentDS=_2;_2.repo=this.config.registry.ID;this.deriveFields(_2);this.previewGrid.setDataSource(_2);if(_2.dbName)this.dbList.setSelectedDB(_2.dbName);var _3=_2.operationBindings;if(_3&&_3.length>0){this.fetchOperationForm.setValues(_3[0])}},isc.A.deriveFields=function isc_DSEditor_deriveFields(_1){var _2=_1.getFieldNames();var _3=[];for(var i=0;i<_2.length;i++){var _5=_2[i]
var _6={};var _7=_1.getField(_5);for(var _8 in _7){if(isc.isA.String(_8)&&_8.startsWith("_"))continue;_6[_8]=_7[_8]}
_3.add(_6)}
this.fieldGrid.setData(_3)},isc.A.tryIt=function isc_DSEditor_tryIt(){var _1=this.buildDSConfig(this.config.ID+"$71x");var _2=isc.DataSource.get("DataSourceStore");_2.performCustomOperation("dsFromConfig",{config:_1},this.getID()+".tryItCallback(data)")},isc.A.tryItCallback=function isc_DSEditor_tryItCallback(_1){this.expandSection("preview");this.previewGrid.setDataSource(_1.ds);this.previewGrid.filterData()},isc.A.saveDS=function isc_DSEditor_saveDS(){var _1=this.buildDSConfig(this.config.ID);var _2=isc.DataSource.get("DataSourceStore");_2.performCustomOperation("dsFromConfig",{config:_1},this.getID()+".xmlLoaded(data)")},isc.A.xmlLoaded=function isc_DSEditor_xmlLoaded(_1){var _2=isc.DataSource.get(this.config.registry.ID);_2.updateData({pk:this.config.pk,object:_1.dsXML})},isc.A.buildDSConfig=function isc_DSEditor_buildDSConfig(_1){var _2={ID:_1,serverType:"sql",dbName:this.dbList.getSelectedDB(),__autoConstruct:"DataSource",operationBindings:[isc.addProperties({operationType:"fetch",skipRowCount:"true",qualifyColumnNames:false},this.fetchOperationForm.getValues())],fields:this.fieldGrid.data};return _2});isc.B._maxIndex=isc.C+11;isc._nonDebugModules=(isc._nonDebugModules!=null?isc._nonDebugModules:[]);isc._nonDebugModules.push('DSBrowser');isc.checkForDebugAndNonDebugModules();isc._moduleEnd=isc._DSBrowser_end=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc.Log&&isc.Log.logIsInfoEnabled('loadTime'))isc.Log.logInfo('DSBrowser module init time: '+(isc._moduleEnd-isc._moduleStart)+'ms','loadTime');delete isc.definingFramework;}else{if(window.isc&&isc.Log&&isc.Log.logWarn)isc.Log.logWarn("Duplicate load of module 'DSBrowser'.");}
/*
 * Isomorphic SmartClient
 * Version v9.1p_2014-03-25 (2014-03-25)
 * Copyright(c) 1998 and beyond Isomorphic Software, Inc. All rights reserved.
 * "SmartClient" is a trademark of Isomorphic Software, Inc.
 *
 * licensing@smartclient.com
 *
 * http://smartclient.com/license
 */

