<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/context/mytags.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>工作台</title>
    <link href="plug-in/hplus/css/bootstrap.min.css?v=3.3.5" rel="stylesheet">
    <link href="plug-in/hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="plug-in/hplus/css/animate.min.css" rel="stylesheet">
    <link href="plug-in/hplus/css/style.min.css?v=4.0.0" rel="stylesheet"><base target="_blank">
    <script src="plug-in/hplus/js/jquery.min.js?v=2.1.4"></script>
    <script src="plug-in/hplus/js/bootstrap.min.js?v=3.3.5"></script>
    <script src="plug-in/hplus/js/plugins/sparkline/jquery.sparkline.min.js"></script>
    <script src="plug-in/hplus/js/plugins/peity/jquery.peity.min.js"></script>
    <script src="plug-in/hplus/js/content.min.js?v=1.0.0"></script>
    <script src="plug-in/hplus/js/demo/peity-demo.js"></script>
    <script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">员工数</h5>
                    <h2 class="text-navy">${userCount}</h2>
                </div>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">未参保人数</h5>
                    <h2 class="text-danger">${noSocialCount}</h2>
                    <small>${noSocialName}</small>
                </div>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">未签订合同人数</h5>
                    <h2 class="text-danger">${noTreatyCount}</h2>
                    <small>${noTreatyName}</small>
                </div>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">未设置薪酬人数</h5>
                    <h2 class="text-danger">${noTreatyCount}</h2>
                    <small>${noTreatyName}</small>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5>男女比例</h5>
                    <h2>${male}/${female}</h2>
                    <div class="text-center">
                        <div id="sparkline"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">最近10天即将转正人数</h5>
                    <h2 class="text-danger">${formalCount}</h2>
                    <small>${formalName}</small>
                </div>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">最近10天合同即将到期人数</h5>
                    <h2 class="text-danger">${treatyCount}</h2>
                    <small>${treatyName}</small>
                </div>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="ibox">
                <div class="ibox-content">
                    <h5 class="m-b-md">最近7天生日人数</h5>
                    <h2 class="text-danger">${birthdayCount}</h2>
                    <small>${birthdayName}</small>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-3">
            <div class="ibox">
                <div class="site-demo-laydate">
                    <div class="layui-inline" id="test-n1"></div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>
    $(document).ready(function () {
        $("#sparkline").sparkline([${male}, ${female}], {type: "pie", height: "140", sliceColors: ["#49D3FF", "#FF8FE7"]});

        //直接嵌套显示
        laydate.render({
            elem: '#test-n1'
            ,position: 'static'
            ,showBottom: false
            ,calendar: true
        });
    });
</script>
</body>
</html>