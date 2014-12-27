#include <iostream>

#include "entry.h"

using namespace std;

int main() {
    cout << "jd++ compiler v0.2" << endl;

    entry::entry_type et = entry::scope_declaration;
    cout << "et:  " << et << endl;

    entry ent(entry::scope_declaration, 10);
    cout << "ent:  " << ent.my_entry_type << " " << ent.end_index << endl;
}
