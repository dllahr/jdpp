package main

/**
 * Created by dlahr on 11/25/14.
 */
class MethodDefinitionTest extends GroovyTestCase {
    void testParse() {
        String e = """
class foo {

    public:

        virtual void hello(int my, pretty how) {

            blah blah blah

        }

    """

        final int startIndex = e.indexOf("virtual") - 2
        final int endIndex = e. lastIndexOf("{") - 1

        MethodDefinition md = new MethodDefinition()

        md.parse(startIndex, endIndex, e.toCharArray())

//        println("prefix---------")
//        println(md.prefix)
//        println("-----------")
//        println("modifiers-----------")
//        println(md.modifiers)
        assert md.modifiers.equals("virtual void")
//        println("-----------")
//        println("nameAndAfter---------")
//        println(md.nameAndAfter)
        assert md.nameAndAfter.equals("hello(int my, pretty how)")
    }


    void testParseWithForLoop() {
        String e = """
class foo {

    public:

        virtual void hello(int my, pretty how) {

            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }

        }

    """

        final int startIndex = e.indexOf("virtual") - 2
        final int endIndex = e. lastIndexOf("{") - 1

        MethodDefinition md = new MethodDefinition()

        md.parse(startIndex, endIndex, e.toCharArray())

        println("prefix\n---------")
        println(md.prefix)
        println("-----------")
        println("modifiers\n-----------")
        println(md.modifiers)
//        assert md.modifiers.equals("virtual void")
        println("-----------")
        println("nameAndAfter\n---------")
        println(md.nameAndAfter)
//        assert md.nameAndAfter.equals("hello(int my, pretty how)")
    }
}
