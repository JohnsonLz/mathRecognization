#include "Contiour.h"

Contiour::Contiour() {
	_max_size = 15;
	_data = new Sample_Elem[_max_size];
	_size = 0;
	_index = NULL;
}

Contiour::~Contiour() {
	delete[]_data;
	if (_index != NULL)
		delete[]_index;
}

void Contiour::_capacity(int size) {
	if (size < _max_size)
		return;
	while (size >= _max_size)
		_max_size += 5;
	Sample_Elem* backup = new Sample_Elem[_max_size];
	for (int i = 0; i < _size; i++)
		backup[i] = _data[i];
	delete[]_data;
	_data = backup;
}

void Contiour::append(const Sample_Elem& item) {
	_capacity(_size + 1);
	_data[_size] = item;
	_size++;
}

void Contiour::_sort() {
	_index = new int[_size];
	for (int i = 0; i < _size; i++)
		_index[i] = i;

	for (int i = 1; i<_size; i++) {
		int key = _index[i];
		int j = i - 1;
		while (j >= 0) {
			int position1 = _data[_index[j]].y_position;
			int position2 = _data[key].y_position;
			if ((position1 - position2)>_data[_index[j]].height || (abs(position1 - position2)<_data[_index[j]].height && (_data[_index[j]].x_position>_data[key].x_position))) {
				_index[j + 1] = _index[j];
				j--;
			}
			else
				break;
		}
		_index[j + 1] = key;
	}
}

const Sample_Elem& Contiour::at(int pos){
	assert(pos < _size);
	if (_index == NULL)
		_sort();
	return _data[_index[pos]];
}