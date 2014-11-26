package main

/**
 * Created by dlahr on 11/25/14.
 */
class MethodDefinition {
    String prefix
    String modifiers
    String nameAndAfter

    void parse(int startIndex, int endIndex, char[] chars) {
        StringBuilder builder = new StringBuilder()
        int i = startIndex
        while (chars[i] == ' ' || chars[i] == '\t' || chars[i] == Main.newline.toCharArray()[0]) { // && ((int)chars[i]) != 32
            builder.append(chars[i])
            i++
        }
        prefix = builder.toString()

        String text = new String(Arrays.copyOfRange(chars, i, endIndex))
        String[] split = text.split("\\s")

        i = 0;
        while (split[i].indexOf("(") == -1) {i++}
        int nameIndex = text.indexOf(split[i])
        nameAndAfter = text.substring(nameIndex)

        if (nameIndex > 0) {
            modifiers = text.substring(0, nameIndex-1)
        } else {
            modifiers = ""
        }
    }

}
