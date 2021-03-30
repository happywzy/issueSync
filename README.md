# issueSync

#### 介绍
github上面的issue同步到gitee上.

#### 软件架构
通过GitHub restful api获取仓库issue及评论

通过gitee open API 将issue及评论同步


#### 安装使用

1.  启动工具
```bash
java -jar ggis-1.0.0-SNAPSHOT.jar
```

2.  打开同步页面
```json
http://localhost:9090/sync
```

3.  输入相关信息

4.  等待issue同步完成,期间请勿操作,可以刷新gitee页面确认.


#### 工具截图
![sync](/doc/sync.png)

