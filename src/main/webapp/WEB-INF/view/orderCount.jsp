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
            员工出单量统计
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
                                        <option value="${user.id}" <c:if test="${param.userId==user.id}"> select="selected"</c:if>>${user.name}</option>
                                    </c:forEach>

                                </select>
                            </div>
                            <div class="form-group">
                                <label for="startDate">开始时间</label>
                                <input type="text" class="form-control pull-right" id="startDate" name="startDate" endDate="endDate" data-date-format="yyyy-mm-dd">
                            </div>
                            <div class="form-group">
                                <label for="endDate">结束时间</label>
                                <input type="text" class="form-control pull-right" id="endDate" name="endDate" startDate="startDate" data-date-format="yyyy-mm-dd">
                            </div>
                            <button type="submit" class="btn btn-default">查询</button>
                        </form>
                        <table class="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>员工</th>
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
                <div class="box box-danger">

                <div id="pieChart" style="height:400px;" class="box-body">
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
            endDate:new Date()
        }).on('changeDate',function(e){
            var startDate = e.date;
            $('#endDate').datepicker('setStartDate',startDate);
        });
        $('#endDate').datepicker({
            autoclose: true,
            endDate:new Date()
        }).on('changeDate',function(e){
            var endDate = e.date;
            $('#startDate').datepicker('setEndDate',endDate);
        });

            var data = genData(50);

            option = {
            title : {
            text: '同名数量统计',
            subtext: '纯属虚构',
            x:'center'
            },
            tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 10,
            top: 20,
            bottom: 20,
            data: data.legendData
            },
            series : [
            {
            name: '姓名',
            type: 'pie',
            radius : '55%',
            center: ['40%', '50%'],
            data: data.seriesData,
            itemStyle: {
            emphasis: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
            }
            }
            ]
            };




            function genData(count) {
            var nameList = [
            '赵', '钱', '孙', '李', '周', '吴', '郑', '王', '冯', '陈', '褚', '卫', '蒋', '沈', '韩', '杨', '朱', '秦', '尤', '许', '何', '吕', '施', '张', '孔', '曹', '严', '华', '金', '魏', '陶', '姜', '戚', '谢', '邹', '喻', '柏', '水', '窦', '章', '云', '苏', '潘', '葛', '奚', '范', '彭', '郎', '鲁', '韦', '昌', '马', '苗', '凤', '花', '方', '俞', '任', '袁', '柳', '酆', '鲍', '史', '唐', '费', '廉', '岑', '薛', '雷', '贺', '倪', '汤', '滕', '殷', '罗', '毕', '郝', '邬', '安', '常', '乐', '于', '时', '傅', '皮', '卞', '齐', '康', '伍', '余', '元', '卜', '顾', '孟', '平', '黄', '和', '穆', '萧', '尹', '姚', '邵', '湛', '汪', '祁', '毛', '禹', '狄', '米', '贝', '明', '臧', '计', '伏', '成', '戴', '谈', '宋', '茅', '庞', '熊', '纪', '舒', '屈', '项', '祝', '董', '梁', '杜', '阮', '蓝', '闵', '席', '季', '麻', '强', '贾', '路', '娄', '危'
            ];
            var legendData = [];
            var seriesData = [];
            for (var i = 0; i < 50; i++) {
            name = Math.random() > 0.65
            ? makeWord(4, 1) + '·' + makeWord(3, 0)
            : makeWord(2, 1);
            legendData.push(name);
            seriesData.push({
            name: name,
            value: Math.round(Math.random() * 100000)
            });
            }

            return {
            legendData: legendData,
            seriesData: seriesData
            };

            function makeWord(max, min) {
            var nameLen = Math.ceil(Math.random() * max + min);
            var name = [];
            for (var i = 0; i < nameLen; i++) {
            name.push(nameList[Math.round(Math.random() * nameList.length - 1)]);
            }
            return name.join('');
            }
            }
            var myChart = echarts.init(document.getElementById('pieChart'));
            myChart.setOption(option);
    })
</script>
</html>
