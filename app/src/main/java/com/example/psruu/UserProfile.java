package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 11]

// คลาส UserProfile ทำหน้าที่เป็นโครงสร้างข้อมูล (Model) สำหรับเก็บข้อมูลรายละเอียดโปรไฟล์ของผู้ใช้งาน[cite: 11]
public class UserProfile {

    // --- ประกาศตัวแปร (Attributes) สำหรับเก็บข้อมูลส่วนตัวและช่องทางติดต่อ ---[cite: 11]
    private String name;      // ชื่อ-นามสกุล หรือชื่อผู้ใช้งาน[cite: 11]
    private String studentId; // รหัสนักศึกษา[cite: 11]
    private String imageUri;  // ที่อยู่ (URI) ของรูปโปรไฟล์[cite: 11]
    private String facebook;  // ชื่อบัญชีเฟซบุ๊กสำหรับติดต่อ[cite: 11]
    private String instagram; // ชื่อบัญชีอินสตาแกรมสำหรับติดต่อ[cite: 11]
    private String phone;     // เบอร์โทรศัพท์สำหรับติดต่อ[cite: 11]

    // Constructor ของคลาส ทำหน้าที่กำหนดค่าเริ่มต้นให้กับข้อมูลโปรไฟล์ทั้งหมดเมื่อมีการสร้างออบเจ็กต์ขึ้นมา[cite: 11]
    public UserProfile(String name, String studentId, String imageUri, String facebook, String instagram, String phone) {
        this.name = name;
        this.studentId = studentId;
        this.imageUri = imageUri;
        this.facebook = facebook;
        this.instagram = instagram;
        this.phone = phone;
    }

    // --- เมธอด Getter และ Setter สำหรับดึงค่าและกำหนดค่าตัวแปรต่างๆ (Encapsulation) ---[cite: 11]

    // ดึงค่าชื่อผู้ใช้งาน[cite: 11]
    public String getName() {
        return name;
    }
    // กำหนดหรือเปลี่ยนค่าชื่อผู้ใช้งาน[cite: 11]
    public void setName(String name) {
        this.name = name;
    }

    // ดึงค่ารหัสนักศึกษา[cite: 11]
    public String getStudentId() {
        return studentId;
    }
    // กำหนดหรือเปลี่ยนค่ารหัสนักศึกษา[cite: 11]
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    // ดึงค่าที่อยู่รูปภาพโปรไฟล์[cite: 11]
    public String getImageUri() {
        return imageUri;
    }
    // กำหนดหรือเปลี่ยนค่าที่อยู่รูปภาพโปรไฟล์[cite: 11]
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // ดึงค่าข้อมูลเฟซบุ๊ก[cite: 11]
    public String getFacebook() {
        return facebook;
    }
    // กำหนดหรือเปลี่ยนค่าข้อมูลเฟซบุ๊ก[cite: 11]
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    // ดึงค่าข้อมูลอินสตาแกรม[cite: 11]
    public String getInstagram() {
        return instagram;
    }
    // กำหนดหรือเปลี่ยนค่าข้อมูลอินสตาแกรม[cite: 11]
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    // ดึงค่าเบอร์โทรศัพท์[cite: 11]
    public String getPhone() {
        return phone;
    }
    // กำหนดหรือเปลี่ยนค่าเบอร์โทรศัพท์[cite: 11]
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
