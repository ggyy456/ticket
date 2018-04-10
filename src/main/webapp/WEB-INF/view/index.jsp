<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>index</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <SCRIPT src="${pageContext.request.contextPath}/js/jquery/jquery-1.11.3.min.js"></SCRIPT>


    <script language="javascript">
        $(function() {
            init();
        });

        function jsFun(data) {//一直被后台调用的方法
            //$('#container').append("<br/>");
            //$('#container').append(data);
            $('#container').html(data);
        }

        function init() {//用户进入页面后就自动发起form表单的提交，激活长连接
            var action = "${pageContext.request.contextPath}/ticket/polling.do";
            $('#myForm').attr("action", action);
            $('#myForm').submit();
        }
    </script>

</head>
<body>

<form action="" method="post" id="myForm" target="myiframe"></form>
<!-- iframe要隐藏哦 -->
<iframe id="myiframe" name="myiframe" style="display: none;"></iframe>
1111111333333
<div id="container" style="height: 800px"></div>
</body>
</html>