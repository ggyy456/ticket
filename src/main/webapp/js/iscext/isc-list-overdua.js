var buildOverDueQueryBaseStyle = function(fieldOverDua){
	var overDueQueryBaseStyle = function(record, rowNum, colNum){
		var css = 'cell';
		var overFields = [fieldOverDua];
		var fieldName = this.getFieldName(colNum);
		if(overFields.contains(fieldName)){
			if(record[fieldName+'Lvl']!=null){
				css=fieldName+'_'+record[fieldName+'Lvl'];
			}else{
				css = 'force_cell';
			}
		}
		return css;
	};
	return overDueQueryBaseStyle;
}

var buildOverDuaQuery = function(param){
	//参数
	var fieldOverDua = param.fieldOverDua;
	var fieldApplyTaskId=param.fieldApplyTaskId;
	var category = param.category;
	var lvlSetList = param.lvlSetList;
	//生成的刷新方法
	var overDuaQuery = function(){
		var listGrid = this;
		var dataList = arguments[1];
		var applyTaskIds = $.map(dataList, function(obj){
			return obj[fieldApplyTaskId];
		});
		if(applyTaskIds.length){
			ajaxSubmit({
				url:getContextPath()+'/overdue/json/findOverDueInfo.action'
				,data:{overDue:{objIdList:applyTaskIds, objName:'TASK', category:category}, lvlSetList:lvlSetList}
				,notBlockUI:true
				,successFun:function(data){
					var overDuaList = data.extObjA;
					if(overDuaList){
						$.each(overDuaList, function(i, overDua){
							var item = findObjectFormColl(dataList, overDua.objId);
							item[fieldOverDua]=overDua.overTimeStr;
							item[fieldOverDua+'Lvl'] = overDua.lvl;
						});
						listGrid.markForRedraw();
					}
				}
			});
		}
	}
	return overDuaQuery;
}