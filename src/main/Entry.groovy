package main

/**
 * Created by dlahr on 12/26/14.
 */
class Entry {
    static enum EntryType {scopeDeclaration, memberVariable, methodDeclaration}

    EntryType entryType

    int endIndex

    Entry() {
    }

    Entry(EntryType entryType, int endIndex) {
        this.entryType = entryType
        this.endIndex = endIndex
    }
}
