package main

/**
 * Created by dlahr on 12/25/14.
 */
class MethodDefinitionFinder {
    Integer startIndex = null
    Integer endIndex = null

    /**
     * finds the next method definition within the text, if there is one present returns true or returns false if there
     * is not.  If found, fills in the members startIndex and endIndex with the position within the text, inclusive
     * @param text
     * @param si
     * @return
     */
    boolean findNextMethod(String text) {
        startIndex = null
        endIndex = null

        String[] split = text.trim().split("\\s+")
//        println(split)
//        println(split.findIndexValues({String it -> ! it}))

        int i = 0;
        while (i < split.length) {
            int j = i

            //does the first entry start with a letter?
            if (split[j].substring(0,1).matches("\\w")) {
                final int nameSplitIndex = j

                //look for a left parenthesis next
                Integer leftParensSplitIndex = null
                if (split[j].indexOf("(") != -1) {  //does the current entry contain a left parenthesis?
                    leftParensSplitIndex = j
                } else {
                    //or does the next entry start with a parenthesis?
                    j++
                    if (j < split.length && split[j].startsWith("(")) {
                        leftParensSplitIndex = j
                    }
                }


                //if the left parenthesis was found in the correct position (above), look for the right parenthesis
                if (leftParensSplitIndex != null) {
                    Integer rightParensSplitIndex = null

                    while (j < split.length && null == rightParensSplitIndex) {
                        if (split[j].indexOf(")") != -1) {
                            rightParensSplitIndex = j
                        }
                        j++
                    }
                    j--


                    //if the right parenthesis was found above, look for the left curly brace
                    if (rightParensSplitIndex != null) {
                        Integer leftCurlSplitIndex = null

                        if (split[j].indexOf("{") != -1) {
                            leftCurlSplitIndex = j
                        } else {
                            j++
                            if (j < split.length && split[j].indexOf("{") != -1) {
                                leftCurlSplitIndex = j
                            }
                        }

                        //if we found everything, calculate the positions, return true
                        if (leftCurlSplitIndex != null) {
                            startIndex = text.indexOf(split[nameSplitIndex])
                            endIndex = startIndex + text.substring(startIndex).indexOf(split[leftCurlSplitIndex]) +
                                    split[leftCurlSplitIndex].length()
                            return true
                        }
                    }
                }
            }
            i++
        }

        return false
    }
}
