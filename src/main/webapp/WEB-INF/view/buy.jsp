<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>阿迪运动套装免费送</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/profile_style.css" rel="stylesheet">
    <link href="css/response.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../css/jquery-confirm.min.css">

    <style>
        .title {
            font-weight: bold;
            text-align: center;
            font-size: 1.6em;
        }

        .sub_title {
            text-align: center;
            color: red;
            font-size: 1.4em;
            margin-top: 15px;
        }

        .input-margin-btm {
            margin-bottom: 8px;
        }
    </style>
</head>
<body>
<div class="weixin">
    <img src="img/weixin-show.jpg">
</div>
<div class="panel panel-default">
    <div class="panel-heading">
        <h2 class="panel-title title">
            阿迪时尚运动套装
        </h2>
        <p class="sub_title">金秋换季 免费领取</p>
    </div>
    <div class="panel-body">
        <img src="img/img_01.jpg">
        <img src="img/img_02.jpg">
        <img src="img/img_03.jpg">
        <img src="img/img_04.jpg">
        <img src="img/img_05.jpg">
        <img src="img/img_06.jpg">
        <img src="img/img_07.jpg">
        <img src="img/img_08.jpg">
        <img src="img/img_09.jpg">
        <img src="img/img_10.jpg">
        <div class="alert alert-info" style="font-size: 1.2em">
            重要提示：官方活动，参与即将免费领取阿迪运动套装一套(衣+裤)，男女款尺码自选。 <strong style="color: red;">此次免费赠予活动不包邮，<span
                style="font-size: 1.2em;">打包费+包装费+快递费共计不超过35元 </span>，货到后支付给您送货的快递人员即可，全国统一用中通快递发货(部分地区韵达快递)
        </strong>！
        </div>
        <hr>
        <form role="form" id="fm">
            <fieldset>
                <legend style="color:red;">请认真填写您的收货地址相关信息</legend>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名</span>
                    <input type="text"
                           class="form-control" id="name" name="name"
                           placeholder="请输入联系人姓名">
                    <input type="hidden" name="ts" id="ts" value="">
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">联系电话</span> <input type="number"
                                                                       class="form-control" id="mobile"
                                                                       placeholder="请输入联系电话"
                                                                       name="mobile">
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;省份</span>
                    <select id="province" name="province" class="form-control">
                    </select>
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;城市</span>
                    <select id="city" name="city" class="form-control">
                    </select>
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地区</span>
                    <select id="area" name="area" class="form-control">
                    </select>
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">详细地址</span>
                    <textarea class="form-control" id="addr"
                              name="addr" placeholder="请输入详细地址"></textarea>
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">选择款式</span>
                    <select id="style" name="style" class="form-control">
                        <option value="">请选择...</option>
                        <option value="男款-黑色">男款-黑色</option>
                        <option value="男款-白色">男款-白色</option>
                        <option value="女款-黑色">女款-黑色</option>
                        <option value="女款-白色">女款-白色</option>
                    </select>
                </div>
                <div class="form-group input-group input-margin-btm">
                    <span class="input-group-addon">选择尺码</span>
                    <select id="size" name="size" class="form-control">
                        <otpion value="">请选择...</otpion>
                        <option value="S">S</option>
                        <option value="M">M</option>
                        <option value="L">L</option>
                        <option value="XL">XL</option>
                        <option value="2XL">2XL</option>
                        <option value="3XL">3XL</option>
                        <option value="4XL">4XL</option>
                    </select>
                </div>
                <div class="form-group input-group">
                    <span class="input-group-addon">验证码</span>
                    <input type="text" id="imageCode" name="imageCode" class="form-control" placeholder="请输入验证码">
                    <img src="/regImgCode" id="Verify" style="cursor:pointer;" alt="看不清，换一张"/>
                </div>
                <input type="hidden" id="userId" value="${param.userId}" name="userId">
                <div style="text-align:center;"><input style="width:18px;height:18px;margin-top:6px;" id="agree"
                                                       type="checkbox"
                                                       value="1">&nbsp;&nbsp;<strong style="color:red;font-size:1.4em;">我已阅读，可以承担运费！</strong>
                </div>
                <br>
                <button id="fmBtn" type="button" disabled="disabled" class="btn btn-danger btn-block">填好了->提交</button>
            </fieldset>
        </form>
        <img src="img/code.jpg">
        <div style="margin:15px;">
            <button style="float:right;" id="badBtn" type="button" class="btn btn-warning">举报</button>
            <button id="goodBtn" type="button" class="btn btn-info">赞</button>
        </div>
    </div>
</div>
<div id="shareWin" style="display : none;">
    <div class="panel-body">
        <div style="line-height:1.5em;">
            恭喜您下单成功！请点击右上角的“<img src='http://os.0sg3.cn:80/static/images/weixin-menu.png'>”按钮，
            将本页面分享到您的朋友圈，公司会根据您的下单手机号在包裹附赠一张30元的手机话费充值卡！
        </div>
    </div>
</div>

<script src="../js/jquery.min.js"></script>
<script src="css/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="js/PCASClass.js"></script>
<script src="../js/jquery-confirm.min.js"></script>
<script>
    //判断是否为微信浏览器
    function isWeiXin() {
        return false;
        /*var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            return true;
        } else {
            return false;
        }*/
    }
    //初始化省份、城市、地区下拉列表
    new PCAS("province", "city", "area");
    $(function () {
        if(isWeiXin()) {
            $(".weixin").show();
            $(".panel-default").hide();
        }else{
            $(".weixin").hide();
            $(".panel-default").show();
        }
        //点击图片更换验证码
        $("#Verify").click(function () {
            $(this).attr("src", "regImgCode.do?timestamp=" + new Date().getTime());
        });
        $("#agree").on("click", function () {
            if (this.checked) {
                $("#fmBtn").prop("disabled", "")
            } else {
                $("#fmBtn").prop("disabled", "disabled")
            }

        });
        //提交表单
        $("#fmBtn").on("click", function () {
            var userName = $("#name").val();
            var mobile = $("#mobile").val();
            var province = $("#province").val();
            var city = $("#city").val();
            var area = $("#area").val();
            var addr = $("#addr").val();
            var size = $("#size").val();
            var style = $("#style").val();
            var imageCode = $("#imageCode").val() && $("#imageCode").val().trim();
            if ($.trim(userName) == '') {
                $.alert({
                    title: '提示',
                    content: '请填写姓名!',
                });
                return false;
            }
            if ($.trim(mobile) == '') {
                $.alert({
                    title: '提示',
                    content: '请填写联系电话!',
                });
                return false;
            }
            if ($.trim(mobile).length != 11) {
                $.alert({
                    title: '提示',
                    content: '请填写正确的手机号码!',
                });
                return false;
            }
            if ($.trim(province) == '') {
                $.alert({
                    title: '提示',
                    content: '请选择省份!',
                });
                return false;
            }
            if ($.trim(city) == '') {
                $.alert({
                    title: '提示',
                    content: '请选择城市!',
                });
                return false;
            }
            if ($.trim(area) == '') {
                $.alert({
                    title: '提示',
                    content: '请选择地区!',
                });
                return false;
            }
            if ($.trim(style) == '') {
                $.alert({
                    title: '提示',
                    content: '请选择款式!',
                });
                return false;
            }
            if ($.trim(size) == '') {
                $.alert({
                    title: '提示',
                    content: '请选择尺码!',
                });
                return false;
            }
            if ($.trim(imageCode) == '') {
                $.alert({
                    title: '提示',
                    content: '请输入验证码!',
                });
                return false;
            }

            $.ajaxSetup({
                async: false
            });
            $.post(
                '../doBuy',
                $("#fm").serialize(),
                function (rs) {
                    if (rs == "success") {
                        $.alert({
                            title: '提示',
                            content: '下单成功!',
                        });
                    } else if (rs == "invalid") {
                        $.alert({
                            title: '提示',
                            content: '无效二维码!请联系销售人员',
                        });
                    } else if (rs == "codeError") {
                        $.alert({
                            title: '提示',
                            content: '验证码错误!请重新输入或点击验证码图片更换验证码',
                        });
                    } else if (rs == "repeat") {
                        $.alert({
                            title: '提示',
                            content: '该手机已经领取',
                        });
                    } else {
                        $.alert({
                            title: '提示',
                            content: '下单错误请联系销售人员!',
                        });
                    }
                }
            )
        });
        $("#badBtn").on("click", function () {
            $.alert({
                title: '提示',
                content: '举报成功!',
            });
        });
        $("#goodBtn").on("click", function () {
            $.alert({
                title: '提示',
                content: '点赞成功!',
            });
        });
        var storage = window.localStorage;
        if (storage) {
            var ts = storage["ts"];
            if (ts == "" || ts == null) {
                ts = "" + new Date().getTime() + parseInt(Math.random() * 1000)
                storage["ts"] = ts;
            }
            $("#ts").val(ts);
        }
    });
</script>
</body>
</html>