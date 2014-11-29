package main

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

            char[] body = fileText.substring(classOpeningBraceIndex+1).toCharArray()

            MethodDefinition methodDefinition = new MethodDefinition()

            int lastIndex = 0
            int i = 0;
            while (i < body.length) {

                if (body[i] == '{') {
                    println("method left brace found at $i")

                    //add method definition / signature to header file
                    addCharsToBuffer(lastIndex, i-1, body, contents.headerFileContents)
                    contents.headerFileContents.append(";$newline")

                    //add method signature to cpp file
                    methodDefinition.parse(lastIndex, i, body)

                    if (methodDefinition.modifiers) {
                        contents.cppFileContents.append(methodDefinition.modifiers).append(" ")
                    }
                    contents.cppFileContents.append(contents.className).append("::")
                    contents.cppFileContents.append(methodDefinition.nameAndAfter)

                    int methodBodyIndex = body.findIndexOf(i, {char it -> it == '}'})

                    addCharsToBuffer(i, methodBodyIndex, body, contents.cppFileContents)

                    i = methodBodyIndex + 1
                    lastIndex = i
                } else {
                    if (body[i] == ':' || body[i] == ';' || body[i] == '}') {
                        println("semicolon, class definition closing right brace, or visibility level colon found at $i")
                        addCharsToBuffer(lastIndex, i, body, contents.headerFileContents)
                        lastIndex = i+1
                    }

                    i++
                }

            }

            File headerFile = new File(file.parentFile, contents.generateHeaderFilename())
            headerFile.write(contents.generateHeaderFileContents())

            File cppFile = new File(file.parentFile, contents.generateCppFilename())
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
}

