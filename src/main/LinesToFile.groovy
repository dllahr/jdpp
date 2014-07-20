package main

/**
 * Created by dlahr on 7/20/14.
 */
class LinesToFile {
    void write(List<String> lines, File file) {
        BufferedWriter w = new BufferedWriter(new FileWriter(file))

        for (String l : lines) {
            w.write(l)
            w.newLine()
        }

        w.close()
    }
}
