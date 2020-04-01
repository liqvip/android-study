### 1. Service 分类

#### 1.1 Service的类型
<div align="center">
<img src="https://upload-images.jianshu.io/upload_images/944365-d42fa78828930bdb.png?imageMogr2/auto-orient/strip|imageView2/2/w/339" alt="">
</div><br>

#### 1.2 特点
<div align="center">
<img src="https://upload-images.jianshu.io/upload_images/944365-8855e3a5340bece5.png?imageMogr2/auto-orient/strip|imageView2/2/w/1010" alt="">
</div><br>

#### 1.3 Androidmanifest里Service的常见属性说明
属性 | 说明 | 备注
---|---|---
android:name | Service的类名 |
android:label | Service的名字 | 若不设置，默认为Service类名
android:icon  | Service的图标 |
android:permission | 申明此Service的权限 | 有提供了该权限的应用才能控制或连接此服务
android:process | 表示该服务是否在另一个进程中运行（远程服务) | 不设置默认为本地服务；remote则设置成远程服务
android:enabled | 系统默认启动 | true：Service 将会默认被系统启动；不设置则默认为false
android:exported | 该服务是否能够被其他应用程序所控制或连接 | 不设置默认此项为 false

### 2. 本地Service
#### 2.1 生命周期
onCreate->onStartCommand->onDestroy


### 3. 可通讯Service
#### 3.1 生命周期
onCreate->onBind->onUnbind->onDestroy