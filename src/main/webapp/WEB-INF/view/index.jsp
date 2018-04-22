<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="isomorphic" prefix="isomorphic" %>

<html>
<head>
    <title>index</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <isomorphic:loadISC skin="Enterprise" isomorphicURI="${pageContext.request.contextPath}/isomorphic/" />
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/isomorphic/locales/frameworkMessages_zh_CN.properties"></SCRIPT>
    <SCRIPT src="${pageContext.request.contextPath}/js/jquery/jquery-1.11.3.min.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/js/iscext/isc-listgrid-ext.js"></SCRIPT>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/globle.css">
    <style>
        .positionDiv {
            position: relative;
            width: 600px;
            height: 400px;
        }
    </style>

    <script language="javascript">
        $(function() {
        });

        function jsFun(data) {//一直被后台调用的方法
            //$('#container').append("<br/>");
            //$('#container').append(data);
            $('#container').html(data);
        }

        function create(type) {//用户进入页面后就自动发起form表单的提交，激活长连接
            var action = "${pageContext.request.contextPath}/ticket/polling.do?type="+type;
            $('#myForm').attr("action", action);
            $('#myForm').submit();
        }

    </script>

</head>
<body>



<div class="positionDiv" id="containerDIV">
<script language="javascript">
    var exampleData = [
        {partName:"北京", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:1},
        {partName:"上海", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:2},
        {partName:"天津", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:3},
        {partName:"重庆", partSrc:"${pageContext.request.contextPath}/image/star_red.png", partNum:4},
        {partName:"长沙", partSrc:"${pageContext.request.contextPath}/image/star_yellow.png", partNum:5},

        {partName:"长春", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:6},
        {partName:"成都", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:7},
        {partName:"福州", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:8},
        {partName:"广州", partSrc:"${pageContext.request.contextPath}/image/star_red.png", partNum:9},
        {partName:"贵阳", partSrc:"${pageContext.request.contextPath}/image/star_yellow.png", partNum:10},

        {partName:"呼和浩特", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:11},
        {partName:"哈尔滨", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:12},
        {partName:"合肥", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:13},
        {partName:"杭州", partSrc:"${pageContext.request.contextPath}/image/star_red.png", partNum:14},
        {partName:"海口", partSrc:"${pageContext.request.contextPath}/image/star_yellow.png", partNum:15},

        {partName:"济南", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:16},
        {partName:"昆明", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:17},
        {partName:"拉萨", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:18},
        {partName:"兰州", partSrc:"${pageContext.request.contextPath}/image/star_red.png", partNum:19},
        {partName:"南宁", partSrc:"${pageContext.request.contextPath}/image/star_yellow.png", partNum:20},

        {partName:"南京", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:21},
        {partName:"南昌", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:22},
        {partName:"沈阳", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:23},
        {partName:"石家庄", partSrc:"${pageContext.request.contextPath}/image/star_red.png", partNum:24},
        {partName:"太原", partSrc:"${pageContext.request.contextPath}/image/star_yellow.png", partNum:25},

        {partName:"乌鲁木齐", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:26},
        {partName:"武汉", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:27},
        {partName:"西宁", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:28},
        {partName:"西安", partSrc:"${pageContext.request.contextPath}/image/star_red.png", partNum:29},
        {partName:"银川", partSrc:"${pageContext.request.contextPath}/image/star_yellow.png", partNum:30},

        {partName:"郑州", partSrc:"${pageContext.request.contextPath}/image/star_blue.png", partNum:31},
        {partName:"深圳", partSrc:"${pageContext.request.contextPath}/image/star_green.png", partNum:32},
        {partName:"厦门", partSrc:"${pageContext.request.contextPath}/image/star_grey.png", partNum:33}

    ];

    isc.defineClass("PartsListGrid","ListGrid").addProperties({
        width:180, cellHeight:24, imageSize:16,
        showEdges:true, border:"0px", bodyStyleName:"normal",
        alternateRecordStyles:true, showHeader:false, leaveScrollbarGap:false,
        emptyMessage:"<br><br>请选择城市",
        fields:[
            {name:"partSrc", type:"image", width:24, imgDir:"pieces/16/"},
            {name:"partName"},
            {name:"partNum", width:20}
        ],
        trackerImage:{src:"pieces/24/cubes_all.png", width:24, height:24}
    });


    var choiceGrid = isc.HStack.create({
        membersMargin:10,
        height:380,

        members:[
            isc.PartsListGrid.create({
                ID:"myList1",
                data:exampleData,
                canDragRecordsOut: true,
                canAcceptDroppedRecords: true,
                canReorderRecords: true,
                dragDataAction: "move"
            }),
            isc.VStack.create({width:32, height:74, layoutAlign:"center", membersMargin:10, members:[
                isc.Img.create({src:"${pageContext.request.contextPath}/image/arrow_right.png", width:32, height:32,
                    click:"myList2.transferSelectedData(myList1)"
                }),
                isc.Img.create({src:"${pageContext.request.contextPath}/image/arrow_left.png", width:32, height:32,
                    click:"myList1.transferSelectedData(myList2)"
                })
            ]}),
            isc.PartsListGrid.create({
                ID:"myList2",
                canDragRecordsOut: true,
                canAcceptDroppedRecords: true,
                canReorderRecords: true
            })
        ]
    });

</script>
</div>

<div>
<form action="" method="post" id="myForm" target="myiframe"></form>
<!-- iframe要隐藏哦 -->
<iframe id="myiframe" name="myiframe" style="display: none;"></iframe>
<input type="button" class="btn" value="缓存火车数据" onclick="create('train');"/>
<input type="button" class="btn" value="缓存车票数据" onclick="create('ticket');"/>
<div id="container" style="height: 200px;"></div>
</div>

<script>
    try{
        $(document).ready(function() {
            //iscResizeEvent("resizeISCObjWithContainer(choiceGrid,'#containerDIV');");
        });
    }catch(e){}

</script>
</body>
</html>