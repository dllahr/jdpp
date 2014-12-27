package main

/**
 * Created by dlahr on 12/26/14.
 */
class MainTest extends GroovyTestCase {
    void testFindMatchingCurlyBrace() {
        String text = "}"
        assertEquals(0, Main.findMatchingCurlyBrace(text.toCharArray(), 0))

        text = """
{
}

{
    {
    }
}

{{{{}}}}

} //this is the matching one

"""

        int expected = text.indexOf("} //this is the matching one")
        int actual = Main.findMatchingCurlyBrace(text.toCharArray(), 0)
        assertEquals(expected, actual)
    }

    void testIndexOfScopeDeclaration() {
        assertEquals(15, Main.indexOfScopeDeclarationColon(":: asdfsd hello: and hello :"))
        assertEquals(-1, Main.indexOfScopeDeclarationColon(":: asdf"))
        assertEquals(-1, Main.indexOfScopeDeclarationColon("hello world"))
        assertEquals(5, Main.indexOfScopeDeclarationColon("hello:"))
    }

    void testIndexOfMemberVariable() {
        assertEquals(5, Main.indexOfMemberVariableSeminColon("hello;"))
        assertEquals(6, Main.indexOfMemberVariableSeminColon("hello ;"))
        assertEquals(6, Main.indexOfMemberVariableSeminColon("hello ; "))
        assertEquals(-1, Main.indexOfMemberVariableSeminColon("hello"))
        assertEquals(5, Main.indexOfMemberVariableSeminColon("hello;; ;;; ;;;"))
    }

    void testGetNextEntry() {
        Entry entry = Main.getNextEntry("public: hello")
        assertEquals(Entry.EntryType.scopeDeclaration, entry.entryType)
        assertEquals(6, entry.endIndex)

        entry = Main.getNextEntry("int a; hello")
        assertEquals(Entry.EntryType.memberVariable, entry.entryType)
        assertEquals(5, entry.endIndex)

        entry = Main.getNextEntry("int my_method() { hello")
        assertEquals(Entry.EntryType.methodDeclaration, entry.entryType)
        assertEquals(16, entry.endIndex)

        entry = Main.getNextEntry("""
public:
int a;
int my_method() {}
""")
        assertEquals(Entry.EntryType.scopeDeclaration, entry.entryType)
        assertEquals(7, entry.endIndex)

        entry = Main.getNextEntry("""
int a;
public:
int my_method() {}
""")
        assertEquals(Entry.EntryType.memberVariable, entry.entryType)
        assertEquals(6, entry.endIndex)

        entry = Main.getNextEntry("""
int my_method() {}
int a;
public:
""")
        assertEquals(Entry.EntryType.methodDeclaration, entry.entryType)
        assertEquals(18, entry.endIndex)

    }


    void testGetClassNameFromClassDefLine() {
        String text = """class my_class {
  private:
    int a;
  public:
    int b;
"""

        assertTrue(Main.getClassNameFromClassDefLine(text).equals("my_class"))

        text = """class my_class : another_parent_class {
  private:
    int a;
  public:
    int b;
"""
        assertTrue(Main.getClassNameFromClassDefLine(text).equals("my_class"))
    }
}
