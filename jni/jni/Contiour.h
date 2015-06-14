#pragma once 
#include"Recourse.h"

struct Sample_Elem{
	Mat img;
	int x_position;
	int y_position;
	int height;
	int weight;
};


class Contiour {

private:
	Contiour(Contiour&){};
	void operator=(Contiour&) {};

private:
	Sample_Elem* _data;
	int _max_size;
	int _size;
	int* _index;

	void _capacity(int size);
	void _sort();

public:
	Contiour();
	~Contiour();

	int Size() const { return _size;}
	void append(const Sample_Elem& item);
	const Sample_Elem& at(int pos);
};