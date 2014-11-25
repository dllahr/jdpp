package main

/**
 * Created by dlahr on 7/20/14.
 */
class Contents {
    String className

    String commonPrefixLines

    StringBuilder headerFileContents = new StringBuilder()
    StringBuilder cppFileContents = new StringBuilder()

    String generateHeaderFilename() {return "${className}.h"}
    String generateCppFilename() {return "${className}.cpp"}

    String generateHeaderFileContents() {
        StringBuilder builder = new StringBuilder()

        builder.append("#ifndef _${className}_h$Main.newline")
        builder.append("#define _${className}_h$Main.newline")
        builder.append(Main.newline)

        builder.append(commonPrefixLines)

        builder.append(headerFileContents)

        builder.append(Main.newline).append(Main.newline)
        builder.append("#endif")

        return builder.toString()
    }

    String generateCppFileContents() {
        StringBuilder builder = new StringBuilder()

        builder.append(commonPrefixLines)
        builder.append(cppFileContents)

        return builder.toString()
    }
}
