
#include "Classify.h"
#include <stdio.h>

void Classify::CreatTrainData(Mat& img) {
	int label;
	namedWindow("img");
	imshow("img", img);
	waitKey();
	std::cin >> label;
	if (label == -1)
		return;

	Mat temp = Mat::zeros(TRAIN_SIZE,TRAIN_SIZE, CV_8UC1);
	if (img.type() != 0) {
		Mat mid = Mat::zeros(img.size(), CV_8UC1);
		cvtColor(img, mid, COLOR_RGB2GRAY);
		resize(mid, temp, temp.size());
	}
	else
		resize(img, temp, temp.size());

	FILE *fp = fopen("/data/data/com.example.anpr_android/data/traindata.dat", "ab+");
	if (fp == NULL) {
		std::cout << "open error";
		exit(1);
	}
	fprintf(fp, "%d", label);

	uchar data;
	for (int i = 0; i < TRAIN_SIZE; i++) {
		for (int j = 0; j < TRAIN_SIZE; j++) {
			data = temp.at<uchar>(i, j);
			fprintf(fp, "%c", data);
		}
	}
	fprintf(fp, "%c", ';');
	fclose(fp);
}

void Classify::ReadTrainDate() {
	NumTrainData data;
	int label;
	uchar udata;

	FILE* fp = fopen("/data/data/com.example.anpr_android/data/traindata.dat", "rb");
	if (fp == NULL) {
		std::cout << "open error";
		exit(1);
	}
	fseek(fp, 0, 0);
	while (fscanf(fp, "%d", &label) == 1) {
		data.result = label;
		for (int i = 0; i < TRAIN_SIZE*TRAIN_SIZE; i++) {
			if (fscanf(fp, "%c", &udata) == 1)
				data.data[i] = static_cast<float>(udata);
			else {
				std::cout << "read error";
				std::cout << label;
				fclose(fp);
				exit(1);
			}
		}
		buffer.push_back(data);
		fscanf(fp, "%c", &udata);
	}
	fclose(fp);
	
/*	Mat img = Mat::zeros(TRAIN_SIZE,TRAIN_SIZE, CV_8UC1);
	for (int i = 0; i < TRAIN_SIZE; i++) {
		for (int j = 0; j < TRAIN_SIZE; j++) {
			img.at<uchar>(i, j) = data.data[i * TRAIN_SIZE + j];
		}
	}
	Mat dst = Mat::zeros(img.rows * 10, img.cols * 10, CV_8UC1);
	resize(img, dst, dst.size());
	namedWindow("data");
	imshow("data", dst);
	waitKey();
	*/
}

void Classify::newSvmStudy(vector<NumTrainData>& trainData) {
	int testCount = trainData.size();

	Mat m = Mat::zeros(1, featureLen, CV_32FC1);
	Mat data = Mat::zeros(testCount, featureLen, CV_32FC1);
	Mat res = Mat::zeros(testCount, 1, CV_32SC1);

	for (int i = 0; i< testCount; i++)
	{

		NumTrainData td = trainData.at(i);
		memcpy(m.data, td.data, featureLen*sizeof(float));
		normalize(m, m);
		memcpy(data.data + i*data.step, m.data, featureLen*sizeof(float));

		res.at<unsigned int>(i, 0) = td.result;
	}

	/////////////START SVM TRAINNING//////////////////   

	CvSVMParams param;
	CvTermCriteria criteria;

	criteria = cvTermCriteria(CV_TERMCRIT_EPS, 1000, FLT_EPSILON);
	param = CvSVMParams(CvSVM::C_SVC, CvSVM::RBF, 10.0, 8.0, 1.0, 10.0, 0.5, 0.1, NULL, criteria);

	svm.train(data, res, Mat(), Mat(), param);
//	svm.save("SVM_DATA.xml");
}

int Classify::Detect(Mat& src) {

	Mat m = Mat::zeros(1, featureLen, CV_32FC1);
	Mat temp = Mat::zeros(TRAIN_SIZE,TRAIN_SIZE, CV_8UC1);
	if (src.type() != 0) {
		Mat mid = Mat::zeros(src.size(), CV_8UC1);
		cvtColor(src, mid, COLOR_RGB2GRAY);
		resize(mid, temp, temp.size());
	}
	else
		resize(src, temp, temp.size());

	for (int i = 0; i<TRAIN_SIZE; i++) {
		for (int j = 0; j<TRAIN_SIZE; j++) {
			m.at<float>(0, j + i * TRAIN_SIZE) = static_cast<float>(temp.at<uchar>(i, j));
		}
	}
	normalize(m, m);
	int ret = (char)svm.predict(m);
	return ret;
}
