#pragma once 
#include <pthread.h>
#include "Recourse.h"
#include "Processor.h"
#include "Classify.h"
#include "vet.h"

class Caculator {

private:
	Caculator(Caculator&){}
	void operator=(Caculator&){}

private:
	vet _data;

	int _caculate_find(double* data, int start,int end,int direct);
	int _caculate(double* data, int pos,int min, int max);
public:
	Caculator(){};
	~Caculator(){};

	void translate(Processor& proc,Classify& clf);
	double caculate();
	char* data();

};

