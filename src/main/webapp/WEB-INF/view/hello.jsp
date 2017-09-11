<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="isomorphic" prefix="isomorphic" %>

<html>
<head>
    <title>Hello</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <isomorphic:loadISC skin="Enterprise" isomorphicURI="${pageContext.request.contextPath}/isomorphic/" />
    <SCRIPT src="${pageContext.request.contextPath}/isomorphic/locales/frameworkMessages_zh_CN.properties"></SCRIPT>
    <SCRIPT src="${pageContext.request.contextPath}/js/jquery/jquery-1.11.3.min.js"></SCRIPT>
    <SCRIPT src="${pageContext.request.contextPath}/js/iscext/isc-listgrid-ext.js"></SCRIPT>
    <style>
        .positionDiv {
            position: absolute;
            width: 950px;
            height: 650px;
        }
    </style>

    <script language="javascript">
        function _query(){
            myListGrid.query(document.getElementById('mainForm'),{startIndex:0,sizePerPage:20});
        }
    </script>

</head>
<body>

<div class="positionDiv" id="containerDIV">
<script language="javascript">
    /*
     * 配置开始
     */
    var myListGrid       = null;//定义listGrid 对象
    var unDoPaginatorPane  = null;//定义paginatorPane 对象

    unDoPaginatorPane =  _createToolStrip(myListGrid,{showExportAll:true});

    var rollCanvas = isc.HStack.create({
        align:'right',
        width:60,
        height:20,
        members:[],
        snapTo:'TR',
        visibility:'hidden'
    });

    myListGrid = isc.LasRListGrid.create({
        enableExport:false,
        paginatorPane:unDoPaginatorPane,
        gridComponents : _createGridComponents(unDoPaginatorPane),
        getRollOverCanvas:function (rowNum, colNum){
            rollCanvas.clear();
            rollCanvas.removeMembers (rollCanvas.getMembers());
            var _record = myListGrid.getRecord(rowNum);
            var updateClick = "_update("+rowNum+")";

            rollCanvas.addMember(
                isc.Button.create({
                    title : "修改",
                    click : function(){
                        eval(updateClick);
                    },
                    height : 25,width : 50
                })
            );

            rollCanvas.show();
            return rollCanvas;
        }
    });

    myListGrid.ready({
        fields:[
            seqField,
            {name:"userId", title:"用户ID",type:"text",width:60 },
            {name:"userName", title:"用户名",width:100 },
            {name:"sex", title:"性别",type:"text"  ,width:60},
            {name:"phone", title:"电话",width:100 },
            {name:"province", title:"所在省",type:"text",width:100},
            {name:"city", title:"所在市",type:"text"  ,width:100},

            {name:"source", title:"来源",width:100 },
            {name:"createTs", title:"登记时间",width:150 },

        ],
        defaultSort:'',
        url : "${pageContext.request.contextPath}/ticket/queryUserListAction.do"
    });
</script>
</div>
<script>
    try{
        $(document).ready(function() {
            iscResizeEvent("resizeISCObjWithContainer(myListGrid,'#containerDIV');");
            _query();
        });
    }catch(e){}

</script>

</body>
</html>
