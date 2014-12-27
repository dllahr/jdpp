#include <iostream>

#include "entry.h"
#include "test_contents.h"
#include "test_entry.h"

using namespace std;

int main() {
    cout << "jd++ compiler v0.2" << endl;

    (new test_entry())->run_all();
    cout << endl;
    (new test_contents())->run_all();
    



}
