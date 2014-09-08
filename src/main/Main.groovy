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

            List<String> body = lines.subList(classDefLineIndex, lines.size())

            contents.headerFileContents = new LinkedList<>()
            contents.cppFileContents = new LinkedList<>()

            String classDefLine = body.get(0)
            int endIndex = classDefLine.endsWith("{") ? classDefLine.length() - 1 : classDefLine.length()
            contents.className = classDefLine.substring(6, endIndex).trim()

            contents.cppFileContents.add("#include \"${contents.headerFilename}\"")
            contents.cppFileContents.add("")

            for (String line : body) {
                String[] split = line.replaceAll("\\s+", " ").split(" ")
                split = split.findAll({String s -> s.length() > 0}) as String[]

                if (split.length > 1 && split[1].length() > 2 && split[1].endsWith("()")) {
                    int methodBodyIndex = line.indexOf(split[2])
                    String methodBody = line.substring(methodBodyIndex, line.length())
                    contents.cppFileContents.add("${split[0]} ${contents.className}::${split[1]} $methodBody")

                    contents.headerFileContents.add("${line.substring(0, methodBodyIndex)};")
                } else {
                    contents.headerFileContents.add(line)
                }
            }

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
