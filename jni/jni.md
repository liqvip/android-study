[TOC]
### 开发步骤

```
1. D:\android_project\my\android_study\core\src\main\java\cn\blogss\core\jni>javac HelloJni.java

2. D:\android_project\my\android_study\core\src\main\java>javah -jni cn.blogss.core.jni.HelloJni
```
通过以上两个命令，会在当前目下生成一个名为`cn_blogss_core_jni_HelloJni.h`的头文件，它是 javah 命令自动生成的。
头文件名称生成格式为`包名_类名.h`。内容如下所示：
```c
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class cn_blogss_core_jni_HelloJni */

#ifndef _Included_cn_blogss_core_jni_HelloJni
#define _Included_cn_blogss_core_jni_HelloJni
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     cn_blogss_core_jni_HelloJni
 * Method:    get
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_cn_blogss_core_jni_HelloJni_get
  (JNIEnv *, jobject);

/*
 * Class:     cn_blogss_core_jni_HelloJni
 * Method:    set
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_cn_blogss_core_jni_HelloJni_set
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif

```
函数名的格式遵循如下规则：Java_包名_类名_方法名。jstring 对应于 java 中的 String 数据类型，关于 java 和 JNI 
的数据类型之间的对应关系，翻看目录 java 和 JNI 的数据类型之间的对应关系一小节。
JNIEXPORT、JNICALL、JNIEnv、jobject 都是 JNI 标准中所定义的类型或者宏，它们的含义如下：
1. JNIEXPORT 和 JNICALL 是 JNI 中所定义的宏，可以在 `jni_md.h` 这个头文件中查找到，内容如下
```
#ifndef _JAVASOFT_JNI_MD_H_
#define _JAVASOFT_JNI_MD_H_

#define JNIEXPORT __declspec(dllexport) // 表明（类、函数、数据）可以被外部函数使用，即把 DLL 中相关代码暴露出来为其他应用程序使用
#define JNIIMPORT __declspec(dllimport)
#define JNICALL __stdcall   // 函数调用约定

typedef long jint;
typedef __int64 jlong;
typedef signed char jbyte;

#endif /* !_JAVASOFT_JNI_MD_H_ */
```
2. JNIEnv *，表示一个指向 JNI 环境的指针，可以通过它来访问 JNI 提供的接口方法
3. jobject，表示 Java 对象中的 this

之后新建 `C\C++`文件实现头文件中的方法，然后编译 dll 库并在 Java 中调用，使用 g++ 命令
```
g++ -I $JAVA_HOME/include -fPIC -shared HelloJni.cpp -o libHelloJni.dll
```
之后使用 java 命令行来运行我们的主程序
```
D:\android_project\my\android_study\core\src\main\java>java -Djava.library.path=cn/blogss/core/jni/dll  cn.blogss.core.jni.HelloJni
```

### java 和 JNI 的数据类型之间的对应关系