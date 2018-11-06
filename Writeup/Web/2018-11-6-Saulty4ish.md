### 2018-11-6 i春秋 

##### 题目连接：http://120.132.56.20:1515/route.php?act=index

##### 题目考点：.svn信息泄露，sql注入，waf过滤问题

首先观察url发现疑似文件包含，尝试包含route，确实是文件包含，之后使用伪协议读取源码失败。

网页功能是改变帽子的颜色(根据输入加载不同的图片)，输入的是用户名，同时还有注册的功能，疑似二次注入。

在输入用户名的地方给了sql回显：select count(*) from t_info where username = '111' or nickname = '111'。

并没有查询的地方，所以二次注入自然也是不行了，思路终结在这里了。

强行扫描一下目录，发现.svn目录，使用工具还原.svn，之前的工具都有问题，重现下了个dvcs-ripper这个项目。

`$name = str_replace("'", "", trim(waf($_POST["name"])));`

发现这个地方不仅替代了'而且还有一个waf函数，存在绕过可能，并且register处没有过滤，可能存在insert的时间盲注。

首先用失去了sqlmap验证时间盲注，失败了。没办法只好去硬刚waf，

	function waf($value){
    $Filt = "\bUNION.+SELECT\b|SELECT.+?FROM";
    if (preg_match("/".$Filt."/is",$value)==1){
        die("found a hacker");
    }
    $value = str_replace(" ","",$value);  
    return $value;}

正则比较简单，过滤UNION（任意字符）SELECT和select（任意字符）from，之后用空替代空格。

但是我们发现页面给我们的回显对'的过滤是gpc转义，此处存在二次过滤，可能有问题。

测试数据为：test'->转义test\'->外层替代变为\，sql语句变为：

select count(*) from t_info where username = 'test\' or nickname = 'test\'

如果我们输入 and 1=1 # '，语句变成：

select count(*) from t_info where username = 'and1=1#\' or nickname = 'and1=1#\'

sql语句可以成功执行，但没有回显，我们试试sleep，难受，说长度太长了...

看了wp，服了。。。or(1)#' flag在当前表，这个过滤。。。甚至连or 1=1 #'都不行,后来发现是因为过滤了空格，()替代了空格的作用...

思路是真的骚啊。。。flag{good_job_white_hat}