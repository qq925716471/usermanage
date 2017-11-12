<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@include file="head.jsp" %>
<!-- Left side column. contains the logo and sidebar -->
<%@include file="sidebar.jsp" %>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            每日出单量统计
        </h1>
    </section>

    <section class="content">
        <div class="row">
            <div class="col-md-6">
                <div class="box">
                    <!-- /.box-header -->
                    <div class="box-body">
                        <form class="form-inline">
                            <div class="form-group">
                                <label for="userId">员工</label>
                                <select id="userId" name="userId" class="form-control input-sm">
                                    <option value="">全部</option>
                                    <c:forEach items="${userList}" var="user">
                                        <option value="${user.id}" <c:if
                                                test="${param.userId==user.id}"> select="selected"</c:if>>${user.name}</option>
                                    </c:forEach>

                                </select>
                            </div>
                            <div class="form-group">
                                <input type="text" value="${searchVo.startDate}" class="form-control pull-right"
                                       id="startDate" name="startDate" endDate="endDate" data-date-format="yyyy-mm-dd">
                            </div>
                            <div class="form-group">
                                <label>至</label>
                            </div>
                            <div class="form-group">
                                <input type="text" value="${searchVo.endDate}" class="form-control pull-right"
                                       id="endDate" name="endDate" startDate="startDate" data-date-format="yyyy-mm-dd">
                            </div>
                            <button type="submit" class="btn btn-default">查询</button>
                        </form>
                        <table class="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>日期</th>
                                <th>工单量</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${list}" var="count">
                                <tr>
                                    <td>${count[0]}</td>
                                    <td>${count[1]}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!-- /.col -->
            <div class="col-md-6">
                <div  >
                    <div id="chart" style="height:800px;" class="">
                    </div>
                    <!-- /.box-body -->
                </div>
            </div>
        </div>


    </section>

</div>

</div>

</div>
</aside>


<%@include file="js.jsp" %>
<script src="../js/echarts.simple.min.js"></script>
</body>
<script type="application/javascript">
    $(document).ready(function () {
        $('#startDate').datepicker({
            autoclose: true,
            endDate: new Date()
        }).on('changeDate', function (e) {
            var startDate = e.date;
            $('#endDate').datepicker('setStartDate', startDate);
        });
        $('#endDate').datepicker({
            autoclose: true,
            endDate: new Date()
        }).on('changeDate', function (e) {
            var endDate = e.date;
            $('#startDate').datepicker('setEndDate', endDate);
        });


        var seriesData = [
            <c:forEach items="${list}" var="count">
            {
                name: '${count[0]}',
                value: ${count[1]}
            },
            </c:forEach>
        ]

        var dateList = seriesData.map(function (item) {
            return item["name"];
        });
        var valueList = seriesData.map(function (item) {
            return item["value"];
        });
        var option = {
            color: ['#3398DB'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : dateList,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'订单量',
                    type:'bar',
                    barWidth: '60%',
                    data:valueList
                }
            ]
        };



        var myChart = echarts.init(document.getElementById('chart'));
        myChart.setOption(option);

    })
</script>
</html>
