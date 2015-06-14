#include "vet.h"

void vet::_capacity(int size) {
	if (size < _max_size)
		return;
	while (size >= _max_size)
		_max_size += 10;

	int* backup = new int[_max_size];
	for (int i = 0; i < _size; i++)
		backup[i] = _data[i];

	delete[]_data;
	_data = backup;
}

void vet::append(int elem) {
	_capacity(_size + 1);
	_data[_size] = elem;
	_size++;
}

int vet::at(int pos) {
	assert(pos < _size);
	return _data[pos];
}

void vet::pop() {
	assert(_size>0);
	_size--;
}
