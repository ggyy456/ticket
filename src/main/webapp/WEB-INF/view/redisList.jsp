<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="isomorphic" prefix="isomorphic" %>

<html>
<head>
    <title>trainList</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <isomorphic:loadISC skin="Enterprise" isomorphicURI="${pageContext.request.contextPath}/isomorphic/" />
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/isomorphic/locales/frameworkMessages_zh_CN.properties"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.11.3.min.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/js/iscext/isc-listgrid-ext.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/js/cityselect.js"></SCRIPT>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cityselect.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/globle.css">
    <style>
        .positionDiv {
            position: absolute;
            width: 1178px;
            height: 650px;
        }
    </style>

    <script language="javascript">
        function _query(){
            myListGrid.query(document.getElementById('mainForm'),{startIndex:0,sizePerPage:30});
        }

        function setValue(){
            var str="";
            $("input[name='trainTypeCheck']:checked").each(function(){
                str += ",'"+$(this).val()+"'";
            })
            if(str!=""){
                str = str.substring(1);
                $("#trainType").val(str);
            }
            else{
                $("#trainType").val("");
            }

            var beginTime = $("#beginTime").val();
            if(beginTime!=""){
                var arr = beginTime.split("-");
                $("#queryTime1").val(arr[0]);
                $("#queryTime2").val(arr[1]);
            }
            else{
                $("#queryTime1").val("");
                $("#queryTime2").val("");
            }
        }
    </script>

</head>
<body>

<form id="mainForm" name="mainForm" method="post">

    <table class="commonTable"  id="commonTable"  cellpadding="0" cellspacing="0" width="1178">
    <tr>
        <td>
        出发地：<input type="text" class="cityinput" id="beginStation" name="beginStation" placeholder="请输入出发地" size="15" />
        </td>
        <td>
        目的地：<input type="text" class="cityinput" id="endStation" name="endStation" placeholder="请输入目的地" size="15" />
        </td>
        <td>
        车次类型：<input name="trainTypeCheck" type="checkbox" value="G" onclick="setValue()" />高铁
                  <input name="trainTypeCheck" type="checkbox" value="C" onclick="setValue()" />城际
                  <input name="trainTypeCheck" type="checkbox" value="D" onclick="setValue()" />动车
                  <input name="trainTypeCheck" type="checkbox" value="Z" onclick="setValue()" />直达
                  <input name="trainTypeCheck" type="checkbox" value="T" onclick="setValue()" />特快
                  <input name="trainTypeCheck" type="checkbox" value="K" onclick="setValue()" />快速
            <input type="hidden" id="trainType" name="trainType" />
        </td>
        <td>
        发车时间：<select id="beginTime" name="beginTime" onchange="setValue()">
                    <option value="">00:00-24:00</option>
                    <option value="00:00-06:00">00:00-06:00</option>
                    <option value="06:00-12:00">06:00-12:00</option>
                    <option value="12:00-18:00">12:00-18:00</option>
                    <option value="18:00-24:00">18:00-24:00</option>
                  </select>
            <input type="hidden" id="queryTime1" name="queryTime1" />
            <input type="hidden" id="queryTime2" name="queryTime2" />
        </td>
        <td>
        <input type="button" class="btn" value="查 询" onclick="_query();"/>
        </td>
    </tr>
    </table>

</form>

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
                    title : "预定",
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
            {name:"trainNo", title:"车次",type:"text",width:80 },
            {name:"beginStation", title:"始发站",width:80 },
            {name:"endStation", title:"到达站",type:"text"  ,width:80},
            {name:"beginTime", title:"开始时间",width:80 },
            {name:"endTime", title:"到达时间",type:"text",width:80},
            {name:"takeTime", title:"耗时",type:"text"  ,width:80},
            {name:"firstSeat", title:"一等座",width:80 },
            {name:"secondSeat", title:"二等座",width:80 },
            {name:"businessSeat", title:"商务座",width:80 },
            {name:"hardSeat", title:"硬座",width:80 },
            {name:"softSeat", title:"软座",width:80 },
            {name:"hardSleep", title:"硬卧",width:80 },
            {name:"softSleep", title:"软卧",width:80 },
            {name:"noSeat", title:"无座",width:80 },

        ],
        defaultSort:'',
        url : "${pageContext.request.contextPath}/train/queryRedisListAction.do"
    });
</script>
</div>
<script>
    try{
        $(document).ready(function() {
            iscResizeEvent("resizeISCObjWithContainer(myListGrid,'#containerDIV');");
            _query();

            var beginStation=new Vcity.CitySelector({input:'beginStation'});
            var endStation=new Vcity.CitySelector({input:'endStation'});
        });
    }catch(e){}

</script>

</body>
</html>
