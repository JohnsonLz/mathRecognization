#include "Processor.h"

Processor::Processor(Mat& img, int angle) {
	_img = img;
	_angle = angle;
}


void Processor::_rotation() {
	
	Mat t,f;
	while(_angle != 0) {
		_angle -= 90;
		transpose(_img, t);
		flip(t, f, 1);
		_img = f;
	}

}

void Processor::Img_pro() {

	_rotation();

	imwrite("/storage/emulated/0/baidu/img.jpg",_img);
	Mat gray;
	cvtColor(_img, gray, CV_RGB2GRAY);

	Mat bin;
	adaptiveThreshold(gray, bin, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY_INV, 101, 1);
	imwrite("/storage/emulated/0/baidu/bin.jpg", bin);

	std::vector<std::vector<cv::Point> > contours;
	//des,contours,�ⲿ������������
	findContours(bin, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

	Mat des(_img.size(), CV_8UC1, Scalar(0));

	for (int i = 0; i < contours.size(); i++) {
		if (contourArea(contours[i]) < 350)
			continue;
		drawContours(des, contours, i, Scalar(255), -1);
	}
	imwrite("/storage/emulated/0/baidu/des.jpg", des);
	Mat element = getStructuringElement(MORPH_RECT, Size(3,3));
	dilate(des, des, element);


	contours.clear();
	findContours(des, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

	for (int i = 0; i < contours.size(); i++) {
		if (contourArea(contours[i]) < 350)
			continue;

		Mat sample(_img.size(), CV_8UC1, Scalar(0));
		drawContours(sample, contours, i, Scalar(255), - 1);

		Mat regionOfInterest;
		Rect rec = boundingRect(contours[i]);

		Sample_Elem tmp;
		tmp.x_position = rec.x;
		tmp.y_position = rec.y;
		tmp.height = rec.height;
		tmp.weight = rec.width;

		if (rec.width < rec.height) {
			rec.x -= (rec.height - rec.width) / 2;
			if (rec.x < 0)
				rec.x = 0;
			if (rec.x + rec.height>sample.cols)
				rec.width = sample.cols - rec.x;
			else
			rec.width = rec.height;
		}
		else{
			rec.y -= (rec.width - rec.height) / 2;
			if (rec.y < 0)
				rec.y = 0;
			if (rec.y + rec.width>sample.rows)
				rec.height = sample.rows - rec.y;
			else
				rec.height = rec.width;
			tmp.y_position = rec.y;
			tmp.height = rec.width;
		}

/*	    FILE* fp;
		fp = fopen(".\\data\\sample_rect_data.txt", "a+");
		if (fp == NULL) {
			std::cout << "rect error";
			return;
		}

		fprintf(fp, "%d,%d,%d,%d\n", rec.x, rec.y, rec.width, rec.height);
		fclose(fp);
*/
		regionOfInterest = sample(rec);
		resize(regionOfInterest, regionOfInterest, Size(SAMPLE_SIZE, SAMPLE_SIZE));

		tmp.img = regionOfInterest;
		_Mat_contours.append(tmp);
	}
}
