package main

import main.Entry.EntryType

//TODO:  handle nested curly braces - for loops, if statements, etc.  Demonstrated in example003 and MethodDefinitionTest.testParseWithForLoop()
//TODO: handle code file without class

/**
 * Created by dlahr on 7/20/14.
 */
class Main {
    public static final String newline = "\n"

    public static void main(String[] args) {
        println("jd++ compiler v0.1")

        for (String filename : args) {
            File file = new File(filename);
            println(file.absolutePath)

            String fileText = file.text

            Contents contents = new Contents()

            int classDefIndex = fileText.indexOf("class ")

            contents.commonPrefixLines = fileText.substring(0, classDefIndex)

            contents.className = getClassNameFromClassDefLine(fileText.substring(classDefIndex))


            contents.cppFileContents.append("#include \"${contents.generateHeaderFilename()}\"$newline")
            contents.cppFileContents.append(newline)

            //get class declaration e.g. class example001 {
            int classOpeningBraceIndex = fileText.indexOf("{")
            String classDeclarationLine = fileText.substring(classDefIndex, classOpeningBraceIndex+1)
            contents.headerFileContents.append(classDeclarationLine)

            String body = fileText.substring(classOpeningBraceIndex+1)
            int matchingClassRightCurlyBraceIndex = findMatchingCurlyBrace(body, 0)
            if (matchingClassRightCurlyBraceIndex == -1) {
                System.err.println("did not find closing curly brace for class in file ${file.absolutePath}")
                return
            }
            body = body.substring(0, matchingClassRightCurlyBraceIndex)


            MethodDefinition methodDefinition = new MethodDefinition()

            //1.  find closing brace for class - use curly brace depth & trailing semicolon - to define body
            //2.  work through body - need to look for:
            //      a.  scope declaration (private:, public:, private :, public :, etc.)
            //      b.  member variable (int a;, int * b; std::cout * a)
            //      c.  method declaration (virtual std::cout * my_meth(int a, int * b, int &c, std::cout * d) {

            int i = 0;
            while (i < body.length() && ! body.substring(i).matches("\\s+")) {

                Entry entry = getNextEntry(body.substring(i))

                if (! entry) {
                    int lineNum = body.substring(0,i).findAll(newline).size()
                    System.err.println("in file ${file.absolutePath} could not find recognizable entry after line number $lineNum")
                    return
                }

                if (EntryType.scopeDeclaration == entry.entryType || EntryType.memberVariable == entry.entryType) {
                    contents.headerFileContents.append(body.substring(i, i + entry.endIndex + 1))
                    i += entry.endIndex + 1
                } else if (EntryType.methodDeclaration == entry.entryType) {
                    contents.headerFileContents.append(body.substring(i, i + entry.endIndex - 1))
                    contents.headerFileContents.append(";$newline")

                    methodDefinition.parse(i, i + entry.endIndex, body.toCharArray())

                    if (methodDefinition.modifiers) {
                        contents.cppFileContents.append(methodDefinition.modifiers).append(" ")
                    }
                    contents.cppFileContents.append(contents.className).append("::")
                    contents.cppFileContents.append(methodDefinition.nameAndAfter)

                    int closeCurlyBraceIndex = findMatchingCurlyBrace(body, i + entry.endIndex)
                    if (-1 == closeCurlyBraceIndex) {
                        System.err.println("could not find closing curly brace for method.  file:  ${file.absolutePath} " +
                                "method: ${body.substring(i, i+entry.endIndex)}")
                        return
                    }

                    contents.cppFileContents.append(body.substring(i + entry.endIndex + 1, closeCurlyBraceIndex + 1))

                    i = closeCurlyBraceIndex + 1
                }
            }

            contents.headerFileContents.append("};")

            File headerFile = new File(contents.generateHeaderFilename())
            headerFile.write(contents.generateHeaderFileContents())

            File cppFile = new File(contents.generateCppFilename())
            cppFile.write(contents.generateCppFileContents())
        }
    }

    static String getClassNameFromClassDefLine(String body) {
        int endIndex = body.indexOf("{")
        return body.substring(6, endIndex).trim()
    }

    static void addCharsToBuffer(int startIndex, int endIndex, char[] chars, StringBuilder builder) {
        for (int i = startIndex; i <= endIndex; i++) {
            builder.append(chars[i])
        }
    }

    static int findMatchingCurlyBrace(String text, int si) {

        int i = si
        int curlyBraceDepth = 0
        while (i < text.length() && curlyBraceDepth >= 0) {
            if (text.charAt(i) == '{') {
                curlyBraceDepth++
            } else if (text.charAt(i) == '}') {
                curlyBraceDepth--
            }

            i++
        }

        if (curlyBraceDepth < 0) {
            return i-1
        } else {
            return -1
        }
    }

    /**
     * return the index of the colon that is at the end of a scope declaration within the text, if found - otherwise
     * return -1
     * @param text
     * @return
     */
    static int indexOfScopeDeclarationColon(String text) {
        String scopeDeclarationColon = text.find("[\\w*,\\s*]:(?!:)")
        if (scopeDeclarationColon) {
            int internalOffset = scopeDeclarationColon.indexOf(":")
            return text.indexOf(scopeDeclarationColon) + internalOffset
        } else {
            return -1
        }
    }

    /**
     * return the index of the semicolon that is at the end of member variable declaration, if found - otherwise return
     * -1
     * @param text
     * @return
     */
    static int indexOfMemberVariableSeminColon(String text) {
//        String memberVariableSemiColon = text.find("[\\w*,\\s*];")
        String memberVariableSemiColon = text.find("[\\w*,\\s*, }];")
        if (memberVariableSemiColon) {
            int internalOffset = memberVariableSemiColon.indexOf(";")
            return text.indexOf(memberVariableSemiColon) + internalOffset
        } else {
            return -1
        }
    }


    static Entry getNextEntry(String text) {
        int scope = indexOfScopeDeclarationColon(text)
        if (-1 == scope) {
            scope = Integer.MAX_VALUE
        }

        int member = indexOfMemberVariableSeminColon(text)
        if (-1 == member) {
            member = Integer.MAX_VALUE
        }

        MethodDefinitionFinder mdf = new MethodDefinitionFinder()
        int method = Integer.MAX_VALUE
        if (mdf.findNextMethod(text)) {
            method = mdf.endIndex
        }

        if (scope < member && scope < method) {
            return new Entry(EntryType.scopeDeclaration, scope)
        } else if (member < scope && member < method) {
            return new Entry(EntryType.memberVariable, member)
        } else if (method < scope && method < member) {
            return new Entry(EntryType.methodDeclaration, method)
        } else {
            return null
        }
    }

}

