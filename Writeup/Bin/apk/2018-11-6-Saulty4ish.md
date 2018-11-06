### 2018-11-6 bugku 安卓逆向

##### 题目连接：/Challenges/apk/gctf_mobile1.apk

首先定位到错误信息，搜索“错误”，没有资源id，并不能定位到关键跳转，所以直接看入口文件。使用插件反编译成java代码。

```
try
      {
        if (paramString1.length() == 0) {
          return false;
        }
        if ((paramString2 != null) && (paramString2.length() == 22))
        {
          Object localObject = MessageDigest.getInstance("MD5");
          ((MessageDigest)localObject).reset();
          ((MessageDigest)localObject).update(paramString1.getBytes());
          paramString1 = toHexString(((MessageDigest)localObject).digest(), "");
          localObject = new StringBuilder();
          int i = 0;
          while (i < paramString1.length())
          {
            ((StringBuilder)localObject).append(paramString1.charAt(i));
            i += 2;
          }
          paramString1 = ((StringBuilder)localObject).toString();
          boolean bool = ("flag{" + paramString1 + "}").equalsIgnoreCase(paramString2);
          if (bool) {
            return true;
          }
        }
      }
```

函数流程：先对str1 md5加密，然后取0,2,4..30同时还要满足str2长度为22.

接下来找到函数调用的地方，

<code>
	.method public onClick(Landroid/view/View;)V
    .locals 4
    .param p1, "v"    # Landroid/view/View;

    .prologue
    const/4 v3, 0x0

    .line 34
    iget-object v0, p0, Lcom/example/crackme/MainActivity$1;->this$0:Lcom/example/crackme/MainActivity;

    iget-object v1, p0, Lcom/example/crackme/MainActivity$1;->this$0:Lcom/example/crackme/MainActivity;

    iget-object v1, v1, Lcom/example/crackme/MainActivity;->edit_userName:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v1

    iget-object v2, p0, Lcom/example/crackme/MainActivity$1;->this$0:Lcom/example/crackme/MainActivity;

    # getter for: Lcom/example/crackme/MainActivity;->edit_sn:Landroid/widget/EditText;
    invoke-static {v2}, Lcom/example/crackme/MainActivity;->access$000(Lcom/example/crackme/MainActivity;)Landroid/widget/EditText;

    # invokes: Lcom/example/crackme/MainActivity;->checkSN(Ljava/lang/String;Ljava/lang/String;)Z
    invoke-static {v0, v1, v2}, Lcom/example/crackme/MainActivity;->access$100(Lcom/example/crackme/MainActivity;Ljava/lang/String;Ljava/lang/String;)Z
</code>

在onclick方法里调用了checksn，调用处三个参数，v0为this，v1为str1，v2为str2，找到v1和v2，

v1来自Lcom/example/crackme/MainActivity;->edit_userName，

v2来自Lcom/example/crackme/MainActivity$1;->this$0，

```
	public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968601);
    setTitle(2131099677);
    this.edit_userName = "Tenshine";
    this.edit_sn = ((EditText)findViewById(2131492945));
    this.btn_register = ((Button)findViewById(2131492946));
    this.btn_register.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (!MainActivity.this.checkSN(MainActivity.this.edit_userName.trim(), MainActivity.this.edit_sn.getText().toString().trim()))
        {
          Toast.makeText(MainActivity.this, 2131099678, 0).show();
          return;
        }
        Toast.makeText(MainActivity.this, 2131099675, 0).show();
        MainActivity.this.btn_register.setEnabled(false);
        MainActivity.this.setTitle(2131099673);
      }
    });
  }
```

我们可以知道v1="Tenshine",v2是一个实例化的第一个参数，猜测为我们用户的输入。

v1 md5之后的结果：b9c77224ff234f27ac6badf83b855c76

取小于30的偶数位+flag{}正好22位，符合我们之前看到的逻辑。

flag{bc72f242a6af3857}
