#include <jni.h>
#include <string.h>
#include "Caculator.h"

#include<android/log.h>
#define LOG_TAG "JNILOG" // 这个是自定义的LOG的标识
#undef LOG // 取消默认的LOG

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG,__VA_ARGS__) // 定义LOG类型

extern "C" {

Classify clsf;

void JNICALL Java_com_example_anpr_1android_anpr_CreatSVM
  (JNIEnv* env, jobject thzi) {

	
	clsf.CreatSVM();
}



jstring Java_com_example_anpr_1android_anpr_Recognize_1result
  (JNIEnv* env, jclass thzi, jstring path, jint angle) {
	//TODO thread
	char buf[128];
	const char* str = env->GetStringUTFChars(path, 0);
	sprintf(buf, "%s", str);
	env->ReleaseStringUTFChars(path, str);
	
	LOGI("Android Jni Log test !==%s==\n",buf);
	std::string p(buf);
	Mat img = imread(p);
	int ag = angle;
	Processor proc(img, ag);


	Caculator cacu;
	cacu.translate(proc, clsf);
	
	char* pat = cacu.data();
	jstring jstrBuf = env->NewStringUTF(pat);
	
	LOGI("Android Jni Log test !==%s==\n", pat);
	delete []pat;
	return jstrBuf;
  }

}
