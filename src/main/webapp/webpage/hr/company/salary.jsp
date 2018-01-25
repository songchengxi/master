<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div>
    <script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="plug-in/layui/layui.js"></script>
    <link rel="stylesheet" href="plug-in/layui/css/layui.css"  media="all">

    <div class="layui-tab layui-tab-brief" lay-filter="salary">
        <ul class="layui-tab-title">
            <li class="layui-this">薪资设定</li>
            <li>请假扣款</li>
            <li>加班设定</li>
        </ul>
        <div class="layui-tab-content">
            <%--薪资设定--%>
            <div class="layui-tab-item layui-show">
                <form class="layui-form" action="salaryController.do?saveDayNum">
                    <input type="hidden" name="id" value='${company.id}'>

                    <div class="layui-form-item">
                        <label class="layui-form-label">月计薪天数</label>
                        <div class="layui-input-inline">
                            <select id="dayNumType" name="dayNumType" lay-verify="required" lay-filter="dayNumType">
                                <option value=""></option>
                                <option value="1">自定义</option>
                                <option value="2">标准计薪21.75天</option>
                                <option value="3">当月应出勤天数</option>
                            </select>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" id="dayNum" name="dayNum" value="${company.dayNum}"
                                   lay-verify="required|number|dayNum" autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-form-mid layui-word-aux">天</div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">日计薪小时</label>
                        <div class="layui-input-inline">
                            <input type="text" name="hour" value="${company.hour}" lay-verify="required|number|hour"
                                   autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-form-mid layui-word-aux">小时</div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="*">保存</button>
                        </div>
                    </div>
                </form>
                <%--<iframe src="hrUserController.do?list" scrolling="yes" frameborder="0" width="100%" height="100%"></iframe>--%>
            </div>
            <%--请假扣款--%>
            <div class="layui-tab-item">
                <form class="layui-form">
                    <input type="hidden" name="id" value='${company.id}'>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label" style="width:130px;">病假工资发放比例</label>
                            <div class="layui-input-inline">
                                <input type="text" name="sick" value="${company.sick}" lay-verify="required|number|leave"
                                       autocomplete="off" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">%</div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width:130px;">产假工资发放比例</label>
                        <div class="layui-input-inline">
                            <input type="text" name="maternity" value="${company.maternity}" lay-verify="required|number|leave"
                                   autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-form-mid layui-word-aux">%</div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width:130px;">陪产假工资发放比例</label>
                        <div class="layui-input-inline">
                            <input type="text" name="paternity" value="${company.paternity}" lay-verify="required|number|leave"
                                   autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-form-mid layui-word-aux">%</div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-form-mid layui-word-aux">注：年假、调休、丧假、婚假不扣工资</div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="*">保存</button>
                        </div>
                    </div>
                </form>
            </div>

            <%--加班设定--%>
            <div class="layui-tab-item">
                <form class="layui-form">
                    <input type="hidden" name="id" value='${company.id}'>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width:100px;">工作日加班</label>
                        <div class="layui-input-inline">
                            <select id="weekdayType" name="weekdayType" lay-verify="required" lay-filter="weekdayType">
                                <option value=""></option>
                                <option value="1">固定薪资</option>
                                <option value="2">时薪</option>
                            </select>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="weekday" value="${company.weekday}"
                                   lay-verify="required|number|weekday" autocomplete="off" class="layui-input">
                        </div>
                        <div id="a1" class="layui-form-mid layui-word-aux" style="display: none;">元/小时</div>
                        <div id="a2" class="layui-form-mid layui-word-aux" style="display: none;">%</div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width:100px;">休息日加班</label>
                        <div class="layui-input-inline">
                            <select id="playdayType" name="playdayType" lay-verify="required" lay-filter="playdayType">
                                <option value=""></option>
                                <option value="1">固定薪资</option>
                                <option value="2">时薪</option>
                            </select>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="playday" value="${company.playday}"
                                   lay-verify="required|number|playday" autocomplete="off" class="layui-input">
                        </div>
                        <div id="b1" class="layui-form-mid layui-word-aux" style="display: none;">元/小时</div>
                        <div id="b2" class="layui-form-mid layui-word-aux" style="display: none;">%</div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width:100px;">法定节假日加班</label>
                        <div class="layui-input-inline">
                            <select id="statutoryType" name="statutoryType" lay-verify="required" lay-filter="statutoryType">
                                <option value=""></option>
                                <option value="1">固定薪资</option>
                                <option value="2">时薪</option>
                            </select>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="statutory" value="${company.statutory}"
                                   lay-verify="required|number|statutory" autocomplete="off" class="layui-input">
                        </div>
                        <div id="c1" class="layui-form-mid layui-word-aux" style="display: none;">元/小时</div>
                        <div id="c2" class="layui-form-mid layui-word-aux" style="display: none;">%</div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="*">保存</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        $("#dayNumType").val("${company.dayNumType}");
        dayNumTypeChange("${company.dayNumType}");

        $("#weekdayType").val("${company.weekdayType}");
        $("#playdayType").val("${company.playdayType}");
        $("#statutoryType").val("${company.statutoryType}");

        overtimeChange("a", "${company.weekdayType}");
        overtimeChange("b", "${company.playdayType}");
        overtimeChange("c", "${company.statutoryType}");
    });

    //月计薪天数
    function dayNumTypeChange(value) {
        if (2 == value) {
            $("#dayNum").show();
            $("#dayNum").val("21.75");
            $("#dayNum").attr("disabled", "disabled");
        } else if (3 == value) {
            $("#dayNum").val("");
            $("#dayNum").hide();
            $("#dayNum").removeAttr("lay-verify");
        } else {
            $("#dayNum").show();
            $("#dayNum").removeAttr("disabled");
            $("#dayNum").attr("lay-verify", "required|number|dayNum");
        }
    }

    //加班
    function overtimeChange(type, value) {
        if (1 == value) {
            $("#" + type + "1").show();
            $("#" + type + "2").hide();
        } else {
            $("#" + type + "1").hide();
            $("#" + type + "2").show();
        }
    }

    layui.use('element', function(){
        var element = layui.element;

        //一些事件监听
        element.on('tab(salary)', function(data){
//            console.log(data.index);
        });
    });

    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form
                ,layer = layui.layer
                ,layedit = layui.layedit
                ,laydate = layui.laydate;

        //月计薪天数
        form.on('select(dayNumType)', function (data) {
            dayNumTypeChange(data.value);
        });

        //工作日加班
        form.on('select(weekdayType)', function (data) {
            overtimeChange("a", data.value);
        });

        //休息日加班
        form.on('select(playdayType)', function (data) {
            overtimeChange("b", data.value);
        });

        //法定节假日加班
        form.on('select(statutoryType)', function (data) {
            overtimeChange("c", data.value);
        });

        //自定义验证规则
        form.verify({
            dayNum: function (value) {
                if (31 < value) {
                    return "月计薪天数不能大于31";
                }
            },
            hour: function (value) {
                if (24 < value) {
                    return "日计薪小时不能大24";
                }
            },
            leave: function (value) {
                if (100 < value || value < 0) {
                    return "比例不能大于100%";
                }
            },
            weekday: function (value) {
                if (value < 0) {
                    return "不能小于0";
                }
                var t = $("#weekdayType").val();
                if (t == 2 && value > 500) {
                    return "比例不能大于500%";
                }
            },
            playday: function (value) {
                if (value < 0) {
                    return "不能小于0";
                }
                var t = $("#playdayType").val();
                if (t == 2 && value > 500) {
                    return "比例不能大于500%";
                }
            },
            statutory: function (value) {
                if (value < 0) {
                    return "不能小于0";
                }
                var t = $("#statutoryType").val();
                if (t == 2 && value > 500) {
                    return "比例不能大于500%";
                }
            }
        });

        //保存提交
        form.on('submit(*)', function (data) {
            var url = "salaryController.do?saveComSalary";
            $.ajax({
                url: url,
                type: "post",
                data: data.field,
                dataType: "json",
                success: function (data) {
                    layer.alert(data.msg, {
                        title: '提交结果'
                    });
                }
            });
            return false;
        });
    });
</script>