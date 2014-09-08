package main

/**
 * Created by dlahr on 7/20/14.
 */
class Contents {
    String className

    List<String> commonPrefixLines

    List<String> headerFileContents
    List<String> cppFileContents

    String getHeaderFilename() {return "${className}.h"}
    String getClassFilename() {return "${className}.cpp"}
}
