#pragma once
#include "Recourse.h"
#include "Contiour.h"

class Processor {

private:
	Processor(){};
	Processor(Processor&){};
	void operator=(Processor&) {};

private:
	Contiour  _Mat_contours;
	Mat _img;
	int _angle;
	
	void _rotation();

public:

	Processor(Mat& _img, int angle = 0);
	~Processor() {};

	void Img_pro();

	int Mat_Sample_Size() const {
		return _Mat_contours.Size();
	}
	const Sample_Elem& at(int pos) {
		return _Mat_contours.at(pos);
	}

};
