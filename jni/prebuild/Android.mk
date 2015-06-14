LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)

LOCAL_MODULE := libopencv_core
LOCAL_SRC_FILES := libopencv_core.so


include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libopencv_highgui
LOCAL_SRC_FILES := libopencv_highgui.so


include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libopencv_imgproc
LOCAL_SRC_FILES := libopencv_imgproc.so


include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libopencv_ml
LOCAL_SRC_FILES := libopencv_ml.so


include $(PREBUILT_SHARED_LIBRARY)
