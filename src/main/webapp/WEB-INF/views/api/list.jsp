<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="reqUrl" value="${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8" /><meta content="width=device-width,user-scalable=no" name="viewport" /><title>
	航运宝
</title>
    <style>
        body {
            background: #F8F7F5;
        }

        #guide {
            width: 100%;
            height: 400px;
            background-size: 320px 400px;
        }

        .iosguide {
            background: url("http://img2.cache.netease.com/m/newsapp/share/2015iosopen1.jpg") no-repeat center top;
            background-size: 320px 425px !important;
        }

        .androidguide {
            background: url("http://img2.cache.netease.com/m/newsapp/share/2014androidopen.jpg") no-repeat center top;
        }

        .iosguidedownload {
            background: url("http://img2.cache.netease.com/m/newsapp/share/2015iosopen1.jpg") no-repeat center top;
            background-size: 320px 425px !important;
        }

        .androidguidedownload {
            background: url("http://img2.cache.netease.com/m/newsapp/share/2014androidopen.jpg") no-repeat center top;
        }
    </style>
</head>
<body>
    <div id="guide" class="guide"></div>
    <script type="text/javascript">
          (function (window, doc) {
            var isAndroid = navigator.userAgent.match(/android/ig),
                isIos = navigator.userAgent.match(/iphone|ipod/ig),
                isIpad = navigator.userAgent.match(/ipad/ig),
                isWeixin = (/MicroMessenger/ig).test(navigator.userAgent),
                openparam = "id="+${id}+"&type="+${type};
            window.onload = function () {
                var iosCls = 'iosguidedownload',
                    androidCls = 'androidguidedownload',
                    elGuid = document.getElementById("guide");
                if ((isIos || isIpad) && !isAndroid) {
                    elGuid.classList.add(iosCls);
                } else if (isAndroid) {
                    elGuid.classList.add(androidCls);
                }
                if (!isWeixin) {
                    if ((isIos || isIpad) && !isAndroid) {
                        window.location = 'hyb://com.hyb.client?' + openparam;
                    } else if (isAndroid) {
                        
                        window.location = 'hyb://com.hyb.client?' + openparam;
                    }
                }
            }
        })(window, document);
    </script>
</body>
</html>
