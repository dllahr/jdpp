package main

/**
 * Created by dlahr on 12/25/14.
 */
class MethodDefinitionFinderTest extends GroovyTestCase {
    void testFindNextMethod() {
        MethodDefinitionFinder mdf = new MethodDefinitionFinder()

        String text = """
#include <iostream>

using namespace std;

class foo {
  public:

    int my_var;

    void do_something(char a, int b) {
        for (int i = 0; i < 10; i++) {
            cout << a << ' ' << i << ' ' << b*i << endl;
        }
    }

};



"""

        String body = text.substring(text.indexOf("public:") + 7)
//        println("body---------")
//        println(body)

        boolean result = mdf.findNextMethod(body)
        println("result:  $result")
        println("startIndex: ${mdf.startIndex}")
        println(body.substring(mdf.startIndex, mdf.startIndex+4))
        println("endIndex:  ${mdf.endIndex}")
        println(body.substring(mdf.endIndex-2, mdf.endIndex+2))
        println(body.substring(mdf.startIndex, mdf.endIndex+1))
    }
}
