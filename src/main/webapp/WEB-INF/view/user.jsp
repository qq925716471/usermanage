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
            用户列表
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">Dashboard</li>
        </ol>
    </section>

    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <!-- /.box-header -->
                    <div class="box-body">
                        <c:if test="${user.userName=='admin'}">
                        <button type="button" class="btn btn-primary  " data-toggle="modal" data-target="#myModal">
                            添加
                        </button>
                        </c:if>
                        <table id="example2" class="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>用户</th>
                                <th>用户姓名</th>
                                <th>是否有效</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${pageVo.content}" var="user">
                                <tr>
                                    <td>${user.userName}</td>
                                    <td>${user.name}
                                    </td>
                                    <td>
                                        <c:if test="${user.enable}">
                                            有效
                                        </c:if>
                                        <c:if test="${!user.enable}">
                                            无效
                                        </c:if>
                                    </td>
                                    <td>
                                    <c:if test="${user.enable && sessionScope.user.userName=='admin'}">
                                        <button type="button" userId="${user.id}" class="btn btn-danger disable " >
                                        冻结
                                        </button>
                                    </c:if>
                                    <c:if test="${!user.enable && sessionScope.user.userName=='admin'}">
                                        <button type="button" userId="${user.id}" class="btn btn-info enable " >
                                        解冻
                                        </button>
                                    </c:if>
                                    <button type="button" userId="${user.id}" class="btn btn-success codeImg " >
                                    下载二维码
                                    </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!-- /.col -->
        </div>
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">添加用户</h4>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="form-group">
                                <label for="userName">用户名</label>
                                <input type="text" class="form-control" id="userName"/>
                            </div>
                            <div class="form-group">
                                <label for="passWord">密码</label>
                                <input type="password" class="form-control" id="passWord"
                                       placeholder="Password">
                            </div>
                            <div class="form-group">
                                <label for="name">姓名</label>
                                <input type="text" class="form-control" id="name"/>
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
        $("#save").click(function () {
            var userName = $("#userName").val();
            var passWord = $("#passWord").val();
            passWord= md5(passWord);
            var name = $("#name").val();
            $.post("user/save", {"userName": userName, "passWord": passWord, "name": name}, function (result) {
                if(result=="success"){
                    $("#myModal").modal('hide')
                    alert("添加成功")
                    location.href = "/user"
                }
            });
        });

        $('.disable').on('click', function () {
            var userId = $(this).attr("userId")
            $.post("user/disable", {"userId": userId}, function (result) {
                if(result=="success"){
                    alert("修改成功")
                    location.href = "/user"
                }
            });
        });

        $('.enable').on('click', function () {
            var userId = $(this).attr("userId")
            $.post("user/enable", {"userId": userId}, function (result) {
                if(result=="success"){
                    alert("修改成功")
                    location.href = "/user"
                }
            });
        });

        $('.codeImg').on('click', function () {
                var userId = $(this).attr("userId")
                location.href = "user/download?userId="+userId;
        });
    })
</script>
</html>
