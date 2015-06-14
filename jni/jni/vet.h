#pragma once 
#include"Recourse.h"

class vet {

private:
	vet(vet&){}
	void operator=(vet&){}

private:
	int* _data;
	int _size;
	int _max_size;

	void _capacity(int size);

public:
	vet(){
		_max_size = 15;
		_data = new int[_max_size];
		_size = 0;
	}
	~vet() {
		delete []_data;
	}

	int Size() const { return _size; }
	void append(int elem);
	void pop();
	int at(int pos);
};

