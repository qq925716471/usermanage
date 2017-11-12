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
            订单系统
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> 主页</a></li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title"> </h3>
            </div>
            <div class="box-body">
                <label>我的链接</label>
                <br>
                <label>${myUrl}</label>
                <br>
                <label>我的二维码</label>
                <br>
                <label><img src="/user/download?userId=${user.id}"></label>
                <br>
                <label>今日订单量</label>
                <br>
                <label>${sum}</label>
                <br>
            </div>
            <!-- /.box-body -->
        </div>
    </section>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
</div>
<!-- /.tab-pane -->
</div>
</aside>
<!-- /.control-sidebar -->
<!-- Add the sidebar's background. This div must be placed
immediately after the control sidebar -->
<!-- ./wrapper -->

<%@include file="js.jsp" %>
</body>
</html>
