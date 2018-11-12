#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_sample_ndk_CppClass_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "你好，CppClass";
    return env->NewStringUTF(hello.c_str());
}

