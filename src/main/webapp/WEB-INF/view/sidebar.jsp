    <%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <%@ page isELIgnored="false" %>
    <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="../img/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>${user.name}</p>
                <a href="../index"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
        </div>
        <!-- search form -->

        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu" data-widget="tree">


            <li class="treeview">
                <a href="#">
                    <i class="fa fa-pie-chart"></i>
                    <span>用户管理</span>
                    <span class="pull-right-container">
        <i class="fa fa-angle-left pull-right"></i>
        </span>
                </a>
                <ul class="treeview-menu">
                    <c:if test="${user.userName=='admin'}">
                    <li><a href="../user"><i class="fa fa-circle-o"></i>用户管理</a></li>
                    </c:if>
                </ul>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-laptop"></i>
                    <span>订单管理</span>
                    <span class="pull-right-container">
        <i class="fa fa-angle-left pull-right"></i>
        </span>
                </a>
                <ul class="treeview-menu">
                    <li><a href="../order/list"><i class="fa fa-circle-o"></i>订单列表</a></li>
                    <c:if test="${user.userName=='admin'}">
                    <li><a href="../order/count"><i class="fa fa-circle-o"></i>员工单量统计表</a></li>
                    <li><a href="../order/dateCount"><i class="fa fa-circle-o"></i>每日单量统计表</a></li>
                    </c:if>
                </ul>
            </li>



        </ul>
    </section>
    <!-- /.sidebar -->
</aside>