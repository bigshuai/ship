# ship
船务宝
1 可以使用嵌入的 jetty.bat refesh-db.bat直接启动，也可以修改spring配置部署到服务器启动
2 架构为 spring+springMVC+shiro(权限控制)+mybatis
3 直接发送post请求登陆(http://localhost/shipService/login {username=admin&password=admin}),shiro会验证权限缓存住,以后调用业务API都要验证权限
4 注册不需要验证