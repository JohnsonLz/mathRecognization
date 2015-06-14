LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	/usr/local/include \

LOCAL_LDLIBS := -llog -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_ml 

LOCAL_MODULE    := vet
LOCAL_SRC_FILES := vet.cpp


include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	/usr/local/include \

LOCAL_LDLIBS := -llog -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_ml 

LOCAL_MODULE    := contiour
LOCAL_SRC_FILES := Contiour.cpp


include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	/usr/local/include \

LOCAL_LDLIBS := -llog -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_ml 

LOCAL_MODULE    := classify
LOCAL_SRC_FILES := Classify.cpp


include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	/usr/local/include \

LOCAL_LDLIBS := -llog -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_ml 

LOCAL_MODULE    := processor
LOCAL_SRC_FILES := Processor.cpp

LOCAL_SHARED_LIBRARIES := contiour

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	/usr/local/include \

LOCAL_LDLIBS := -llog -pthread -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_ml 

LOCAL_MODULE    := caculator
LOCAL_SRC_FILES := Caculator.cpp

LOCAL_SHARED_LIBRARIES := classify  vet processor contiour

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	/usr/local/include \

LOCAL_LDLIBS := -llog -pthread -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_ml 

LOCAL_MODULE    := anpr
LOCAL_SRC_FILES := anpr.cpp

LOCAL_SHARED_LIBRARIES := caculator classify  vet processor contiour

include $(BUILD_SHARED_LIBRARY)

