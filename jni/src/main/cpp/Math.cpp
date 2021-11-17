#include <jni.h>

#include "android/log.h"
static const char *TAG="Math-lib";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

extern "C" JNIEXPORT void JNICALL Java_cn_blogss_jni_NativeTest_print99(JNIEnv *env, jobject){

    for (int i = 1; i <= 9; ++i) {
    }

    LOGE("Math.cpp: successful call print99.");
}

extern "C" JNIEXPORT void JNICALL Java_cn_blogss_jni_NativeTest_bubbleSort(JNIEnv *env, jobject, jintArray){

}

