package main

/**
 * Created by dlahr on 7/20/14.
 */
class Main {
    public static void main(String[] args) {
        println("jd++ compiler")

        FilesBuilder filesBuilder = new FilesBuilder()

        for (String filename : args) {
            File file = new File(filename);
            println(file.absolutePath)

            List<String> lines = readFile(file)

            Contents contents = new Contents()

            int classDefLineIndex = lines.findIndexOf({String l -> l.startsWith("class ")})

            contents.commonPrefixLines = lines.subList(0, classDefLineIndex)

            contents.headerFileContents = lines.subList(classDefLineIndex, lines.size())

            contents.cppFileContents = new LinkedList<>()

            String classDefLine = contents.headerFileContents.get(0)
            int endIndex = classDefLine.endsWith("{") ? classDefLine.length() - 1 : classDefLine.length()
            contents.className = classDefLine.substring(6, endIndex).trim()

            filesBuilder.build(contents, new File("."))
        }
    }

    static List<String> readFile(File file) {
        List<String> result = new LinkedList<>()

        BufferedReader reader = new BufferedReader(new FileReader(file))

        String line
        while ((line = reader.readLine()) != null) {
            result.add(line)
        }

        reader.close()

        return result
    }
}
