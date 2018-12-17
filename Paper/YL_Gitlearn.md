#                             Doctor's  Gitlearn

这是我的git学习记录内容比较少，比较简陋，但是够我暂时使用了，我看的是廖雪峰的git教程，网址：https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000

 

让我印象深刻的一句话：Git是目前世界上最先进的分布式版本控制系统（没有之一）

## 一、安装git

Windows linux mac 都可以安装

我是在虚拟机里用基于linux的centos里面使用的的，正好自带。

## 二、使用git在本地练习

### 1、创建版本库（repository）：

a. 选择合适的文件位置，创建一个空目录

b. 使用 **`git init`**命令把这个目录变成Git可以管理的仓库

c. 文件放到learngit目录下

​        用命令`git add+<file>`告诉Git，把文件添加到仓库(可反复添加多个文件)

​         用命令`git commit -m “  ”` 告诉Git，把文件提交到仓库  

使用Windows的童鞋要特别注意：

千万不要使用Windows自带的**记事本**编辑任何文本文件

### 2、删改操作

`git status`仓库当前的状态

`git diff` 查看difference，显示的格式正是Unix通用的diff格式

`git log`  我们历史记录

HEAD表示当前版本,上一个版本就是`HEAD^`，上上一个版本就是`HEAD^^`，往上100个版本`HEAD~100`

a. 用`git reset --hard HEAD^`命令把当前版本回退到上一个版本

b. 用`git reflog`查看命令历史，以便确定要回到未来的哪个版本,用`git reset --hard +版本的前几个字符`

c.  `git checkout -- <file>` 把文件在工作区的修改全部撤销,让这个文件回到最近一次git commit或git add时的状态

d.  用命令`git reset HEAD <file>`可以把暂存区的修改撤销掉（unstage）。`git reset HEAD <file>`命令既可以回退版本，也可以把暂存区的修改回退到工作区。

e. 用`rm`命令删了文件。确实要从版本库中删除该文件，那就用命令`git rm`删掉，并且`git commit` 。

删错了，`git checkout --<file>` 其实是用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”。

 

## 三、远程仓库

### 1、 上传本地仓库：

先新建一个github仓库， `git remote add orign git+网址` 本地关联远程库，`git push -u origin master` 把当前分支master推送到远程。

   由于远程库是空的，我们第一次推送master分支时，加上了`-u`参数，Git不但会把本地的master分支内容推送的远程新的master分支，还会把本地的master分支和远程的master分支关联起来，在以后的推送或者拉取时就可以简化命令。此后，每次本地提交后，只要有必要，就可以使用命令`git push origin master`推送最新修改

### 2、 clone远程仓库：

​          `git clone+网址`

​     