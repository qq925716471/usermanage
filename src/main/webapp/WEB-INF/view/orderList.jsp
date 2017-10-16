        <%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@include file="head.jsp" %>
<!-- Left side column. contains the logo and sidebar -->
<%@include file="sidebar.jsp" %>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            订单列表
        </h1>
    </section>

    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <!-- /.box-header -->
                    <div class="box-body">
                        <form id="form" class="form-inline" action="../order/list" method="post">
                            <c:if test="${user.userName=='admin'}">
                            <div class="form-group">
                                <label for="userId">员工</label>
                                <select id="userId" name="userId" class="form-control input-sm">
                                    <option value="">全部</option>
                                    <c:forEach items="${userList}" var="user">
                                        <option value="${user.id}" <c:if test="${param.userId==user.id}"> select="selected"</c:if>>${user.name}</option>
                                    </c:forEach>

                                </select>
                            </div>
                            </c:if>
                            <div class="form-group">
                            <label for="startDate">开始时间</label>
                            <input type="text" class="form-control pull-right" id="startDate" name="startDate" endDate="endDate" data-date-format="yyyy-mm-dd">
                            </div>
                            <div class="form-group">
                            <label for="endDate">结束时间</label>
                            <input type="text" class="form-control pull-right" id="endDate" name="endDate" startDate="startDate" data-date-format="yyyy-mm-dd">
                            </div>
                            <div class="form-group">
                            <label for="mobile">客户电话</label>
                            <input type="email" class="form-control" id="mobile">
                            </div>
                            <div class="form-group">
                            <label for="deliverId">快递</label>
                            <input type="email" class="form-control" id="deliverId">
                            </div>
                            <div class="form-group">
                            <label for="status">状态</label>
                            <select id="status" name="status" class="form-control input-sm">
                            <option value="">全部</option>
                            <option value="待发货">待发货</option>
                            <option value="已发货">已发货</option>
                            <option value="丢单">丢单</option>
                            <option value="无收件人">无收件人</option>
                            <option value="退回">退回</option>
                            <option value="已完成">已完成</option>
                            </select>
                            </div>
                            <input type="hidden" id="pageIndex" name = "pageIndex">
                            <button type="submit" class="btn btn btn-success">查询</button>
                            <c:if test="${user.userName=='admin'}">
                            <button type="button" id="delete" class="btn btn btn-danger">删除</button>
                            </c:if>
                        </form>
                        <table class="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th><input type="checkbox" id="checkAll"></th>
                                <th>客户姓名</th>
                                <th>电话</th>
                                <th>省</th>
                                <th>市</th>
                                <th>区</th>
                                <th>详细地址</th>
                                <th>样式</th>
                                <th>型号</th>
                                <th>快递</th>
                                <th>销售人员</th>
                                <th>状态</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${orderList.content}" var="order">
                                <tr>
                                <td><input type="checkbox" value="${order.id}" class="check" ></td>
                                    <td>${order.name}</td>
                                    <td>${order.mobile}</td>
                                    <td>${order.province}</td>
                                    <td>${order.city}</td>
                                    <td>${order.area}</td>
                                    <td>${order.addr}</td>
                                    <td>${order.style}</td>
                                    <td>${order.size}</td>
                                    <td>${order.deliverId}</td>
                                    <td>${order.user}</td>
                                    <td>${order.status}</td>
                                    <td>${order.note}</td>
                                    <td>
                                        <button type="button" orderId="${order.id}"  class="btn btn-info update ">
                                            更新
                                        </button>

                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <nav  aria-label="Page navigation">
                            <ul class="pagination pull-right">
                            <c:forEach var="i" begin="1" end="${totalCount}">
                                <li <c:if test="${pageIndex==(i-1)}">class="active"</c:if> ><a href="#" class="pageIndex" pageIndex="${i}">${i}</a></li>
                            </c:forEach>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </section>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">处理订单</h4>
                    </div>
                    <div class="modal-body">
                        <form>
                            <input type="hidden"   id="updateOrderId" />
                            <div class="form-group">
                                <label for="saveDeliverId">快递单号</label>
                                <input type="text" class="form-control" id="saveDeliverId"/>
                            </div>
                            <div class="form-group">
                            <label for="deliverName">快递公司</label>
                                <select id="deliverName" class="form-control input-sm">
                                <option value="韵达">韵达</option>
                                <option value="圆通">圆通</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="orderStatus">状态</label>
                                <select id="orderStatus" class="form-control input-sm">
                                    <option value="已发货">已发货</option>
                                    <option value="丢单">丢单</option>
                                    <option value="无收件人">无收件人</option>
                                    <option value="退回">退回</option>
                                    <option value="待发货">待发货</option>
                                    <option value="已完成">已完成</option>
                                </select>
                            </div>
                            <div class="form-group">
                            <label for="note">备注</label>
                            <textarea  row="3" id="note" class="form-control input-sm">
                            </textarea >
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" id="save" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </section>

</div>

</div>

</div>
</aside>


<%@include file="js.jsp" %>
</body>
<script type="application/javascript">
    $(document).ready(function () {
            //全选/全不选
        $("#checkAll").bind("click",function(){
            $(".check").prop("checked",this.checked);
        });
        $(".check").click(function(){
            if(!this.checked){
                $("#checkAll").prop("checked",false);
            }else{
                var flag = true;
                $(".check").each(function(){
                    if(!this.checked){
                        flag = false
                        return;
                    }
                })
            $("#checkAll").prop("checked",flag);
            }
        })
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
        $(".update").click(function () {
            $("#updateOrderId").val($(this).attr("orderId"))
            $("#note").val()
            $("#myModal").modal('show')
        });

        $("#save").click(function () {
            var orderId = $("#updateOrderId").val()
            var deliverId = $("#saveDeliverId").val()
            var deliverName = $("#deliverName").val()
            var status = $("#orderStatus").val()
            var note = $("#note").val()
            $.post("../order/update", {"id": orderId,"deliverId": deliverId,"deliverName": deliverName,"status": status,"note":note}, function (result) {
                if(result=="success"){
                     alert( '修改成功');
                    location.href = "../order/list"
                }
            });
        });
        $("#delete").click(function () {
            var orderIds =[]
            $(".check:checked").each(function(){
                orderIds.push($(this).val())
            })
            if(orderIds.length==0){
                alert("请选择要删除的订单")
                return ;
            }
            if(confirm("确定要删除么?")){
                $.post("../order/delete", {"orderIds":orderIds.join(",")}, function (result) {
                    if(result=="success"){
                        alert( '修改成功');
                        location.href = "../order/list"
                    }
                });
            }

        });

        $(".pageIndex").click(function(){
            $("#pageIndex").val($(this).attr("pageIndex")-1)
            $("#form").submit();
        })
    })
</script>
</html>
