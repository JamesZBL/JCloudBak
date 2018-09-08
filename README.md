
# JCloudBak



### 基于七牛云存储的可移动设备文件自动备份工具



#### 1.配置


```properties
# 1.应用配置

# 启动延时(ms)
begin_delay=1000
# 设备插入检测周期(ms)
task_period=1000
# 要上传的文件类型
file_extensions=png,gif,jpg,jpeg,bmp,tif,mp4,rmvb,mkv,avi,3gp,wmv,mpg,mov,flv,mp3,swf,wma,ape,aac,wav,doc,docx,ppt,pptx,xls,xlsx,pdf,epub,mobi,txt,html,rar,zip,7z
# 忽略文件名中含有如下字符串的文件(多个字符以逗号间隔)
file_name_part_ignore=$

# 2.七牛云存储

qiniu.access_key=your_access_key
qiniu.secret_key=your_secret_key
qiniu.bucket_name=your_bucket_name

# 3.邮件提醒

mail.smtp.host=smtp.163.com
mail.smtp.port=25
mail.to.name=James
mail.to.address=mail_to@example.com
mail.from.address=mail_from@example.com
mail.from.username=Jacky
mail.from.password=123456
mail.subject=JCloudBak
```


#### 2.编译打包


方式一

```
mvn clean package
```

每次打包会执行单元测试中的测试用例，包括发送测试邮件，所以应保证邮件配置正确性

方式二

```
mvn package -Dmaven.test.skip=true 
```

此方式会跳过所有测试用例，并在编译期间忽略所有测试类

使用 IntelliJ IDEA 打包，jar 包默认保存在 ./target 目录下，名称为 app.jar



#### 3.部署运行


批处理脚本实现后台进程（无 cmd 窗口）方式

```
@echo off
   
start javaw -Xmx100m -Xms10m -jar app.jar

exit
```

