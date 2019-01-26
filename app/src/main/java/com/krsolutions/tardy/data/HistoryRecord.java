package com.krsolutions.tardy.data;

public class HistoryRecord {
    int RecordID;
    int EntryType;
    String EntryTime;
    int SubjectID;
    String SubjectName;

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public HistoryRecord(int recordID, int entryType, String entryTime, int subjectID, String subjectName) {
        RecordID = recordID;
        EntryType = entryType;
        EntryTime = entryTime;
        SubjectID = subjectID;
        SubjectName = subjectName;
    }

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int recordID) {
        RecordID = recordID;
    }

    public int getEntryType() {
        return EntryType;
    }

    public void setEntryType(int entryType) {
        EntryType = entryType;
    }

    public String getEntryTime() {
        return EntryTime;
    }

    public void setEntryTime(String entryTime) {
        EntryTime = entryTime;
    }

    public int getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(int subjectID) {
        SubjectID = subjectID;
    }
}
