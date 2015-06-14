
#pragma once 
#include "Recourse.h"

class NumTrainData {
public:
	NumTrainData()
	{
		memset(data, 0, sizeof(data));
		result = -1;
	}
public:
	float data[TRAIN_SIZE*TRAIN_SIZE];
	int result;
};

class Classify {
private:
	Classify(const Classify&) {}
	void operator = (const Classify&) {}

	vector<NumTrainData> buffer;
	int featureLen;
	SVM svm;
	bool inited;

	void ReadTrainDate();
	void newSvmStudy(vector<NumTrainData>& trainData);

public:
	Classify() :featureLen(TRAIN_SIZE*TRAIN_SIZE), inited(false) {}
	~Classify() {}

	void CreatSVM() {
		svm.load("/data/data/com.example.anpr_android/data/SVM_DATA.xml");
	}
	
	Classify* StaticPtr() {
		return this;
	}
	void CreatTrainData(Mat& img);
	int Detect(Mat& src);
	bool Inited() {
		return inited;
	}
};
