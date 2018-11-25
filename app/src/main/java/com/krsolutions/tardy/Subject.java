package com.krsolutions.tardy;

public class Subject {

    long subjectID;
    String SubjectName;
    int TotalClasses;
    int ClassesAttended;

    public Subject(){
        subjectID=-1;
        SubjectName="NaN";
        TotalClasses=-1;
        ClassesAttended=-1;
    }
    public Subject(long id, String name, int i, int j){
        subjectID=id;
        SubjectName=name;
        TotalClasses= i;
        ClassesAttended=j;
    }
    public long getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(long subjectID) {
        this.subjectID = subjectID;
    }
    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public int getTotalClasses() {
        return TotalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        TotalClasses = totalClasses;
    }

    public int getClassesAttended() {
        return ClassesAttended;
    }

    public void setClassesAttended(int classesAttended) {
        ClassesAttended = classesAttended;
    }

    public void setNull(){
        subjectID = -1;
        SubjectName = null;
        TotalClasses = -1;
        ClassesAttended = -1;
    }
}
