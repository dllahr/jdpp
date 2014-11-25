package main
//TODO: handle code file without class
//TODO: handle constructors

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

            int lastIndex = 0
            int i = 0;
            while (i < body.length) {

                if (body[i] == '{') {
                    println("method left brace found at $i")

                    //add method definition / signature to header file
                    addCharsToBuffer(lastIndex, i-1, body, contents.headerFileContents)
                    contents.headerFileContents.append(";$newline")

                    //add method signature to cpp file
                    int s = lastIndex
                    while (body[s] == ' ' || body[s] == '\t' || body[s] == newline.toCharArray()[0]) {s++}

                    boolean spaceFound = false
                    for (int j = s; j < i; j++) {
                        contents.cppFileContents.append(body[j])

                        if (! spaceFound && body[j] == ' ') {
                            contents.cppFileContents.append(contents.className).append("::")
                            spaceFound = true
                        }
                    }

                    int methodBodyIndex = i+1
                    while (body[methodBodyIndex] != '}') {methodBodyIndex++}

                    addCharsToBuffer(i, methodBodyIndex, body, contents.cppFileContents)

                    i = methodBodyIndex + 1
                    lastIndex = i+1
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

