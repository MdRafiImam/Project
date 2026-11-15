package cse.librarymanagement.model;

import java.time.LocalDate;

public class Student {

    private int studentId;
    private String studentIdNumber;
    private String membershipId;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private LocalDate joinDate;

    public Student() {
    }

    public Student(int studentId, String studentIdNumber,
                   String membershipId, String fullName,
                   String email, String phone,
                   String department, LocalDate joinDate) {

        this.studentId = studentId;
        this.studentIdNumber = studentIdNumber;
        this.membershipId = membershipId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.joinDate = joinDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentIdNumber() {
        return studentIdNumber;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setStudentIdNumber(String studentIdNumber) {
        this.studentIdNumber = studentIdNumber;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
}
