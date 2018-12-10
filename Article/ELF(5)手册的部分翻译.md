
***原文地址：http://man7.org/linux/man-pages/man5/elf.5.html***

.fini 这个节包括去处理结尾代码的可执行命令，当程序正常退出的时候，系统会安排执行在这个节里面的代码，这节的类型是SHT_PROGBITS.这个被使用的属性是SHF_ALLOC 和 SHF_EXECINSTR.

*.init* 这节包括那些被用来初始化的代码的可执行的命令。当程序开始运行，系统在引用系统入口点之前执行节内代码。这一节的类型是SHT_PROGBITS.这个被使用的属性是SHF_ALLOC and SHF_EXECINSTR.

*.plt* 这一节包括过程连接目录。这一节的类型是SHT_PROGBITS.属性是processor-specific.

*.text* 这一节包括文本，或者说一个程序的可执行命令。这一节的类型是SHT_PROGBITS.这个被使用的属性是SHF_ALLOC and SHF_EXECINSTR.

*.rodata* 这一节包括的是在处理镜像中那些不可写片段的只读数据。这一节的类型是SHT_PROGBITS.这个被使用的属性是SHF_ALLOC。

*.ctors* 这一节包括c++构造函数的初始化指针，这一节的类型是SHT_PROGBITS. 这个被使用的属性是SHF_ALLOC和SHF_WRITE

*.dtors* 这一节包括c++析构函数的初始化指针，这一节的类型是SHT_PROGBITS. 这个被使用的属性是SHF_ALLOC和SHF_WRITE

*.dynamic* 这一节包括动态链接信息，这一节的属性包括SHF_ALLOC 位。SHF_WRITE位无论怎么设置都会被设置成processor-specific。这一节的类型是SHT_DYNAMIC.我们从上文查看属性。

*.got* 这一节包括全局偏移量目录，这一节的类型是SHT_PROGBITS. 这个被使用的属性是SHF_ALLOC

*.bss* 这一节包括那些在程序内存镜像里没被初始化的数据。在程序开始运行的时候通过定义，系统将这些数据用0初始化。这一节的类型是SHT_NOBITS. 这个被使用的属性是SHF_ALLOC和SHF_WRITE

*.comment* 这一节包括的是版本限制（控制）信息，这一节的类型是SHT_PROGBITS.没有属性被使用。

*.shstrtab* 这一节包括节名。这一节的类型是SHT_STRTAB。没有属性被使用。

*.symtab* 这一节包括一个符号（标记）目录。如果这个文件有一个可加载段包含这个标记目录，这一节的属性将会包括SHF_ALLOC位。否则，这一位将会被关闭。这一节的类型是SHT_SYMTAB.



**Elf文件节的了解有助于我们对于原理的理解，所以我们还是要考虑的！**

未完待续……
