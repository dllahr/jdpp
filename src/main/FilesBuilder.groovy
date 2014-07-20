package main

/**
 * Created by dlahr on 7/20/14.
 */
class FilesBuilder {
    private LinesToFile linesToFile

    FilesBuilder() {
        linesToFile = new LinesToFile()
    }

    void build(Contents contents, File directory) {
        File headerFile = new File(directory, contents.className + ".h")

        def lines = new LinkedList(contents.commonPrefixLines)
        lines.addAll(contents.headerFileContents)

        linesToFile.write(lines, headerFile)

        File cppFile = new File(directory, contents.className + ".cpp")

        lines = new LinkedList(contents.commonPrefixLines)
        lines.addAll(contents.cppFileContents)

        linesToFile.write(lines, cppFile)
    }
}
