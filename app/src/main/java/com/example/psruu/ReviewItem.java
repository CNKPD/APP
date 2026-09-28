package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 10]

// คลาส ReviewItem ทำหน้าที่เป็นโครงสร้างข้อมูล (Model) สำหรับเก็บข้อมูลของรีวิวแต่ละรายการ[cite: 10]
public class ReviewItem {

    // --- ประกาศตัวแปร (Attributes) สำหรับเก็บข้อมูลรีวิว ---[cite: 10]
    private String reviewer; // ชื่อผู้รีวิว[cite: 10]
    private String rating;   // คะแนนรีวิว (เช่น 5.0 หรือ 4)[cite: 10]
    private String comment;  // ข้อความความคิดเห็นรีวิว[cite: 10]
    private String date;     // วันที่ทำการรีวิว[cite: 10]

    // Constructor ของคลาส ทำหน้าที่กำหนดค่าเริ่มต้นให้กับข้อมูลรีวิวเมื่อมีการสร้างออบเจ็กต์[cite: 10]
    public ReviewItem(String reviewer, String rating, String comment, String date) {
        this.reviewer = reviewer;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    // --- เมธอด Getter สำหรับดึงค่าตัวแปรต่างๆ ไปใช้งาน ---[cite: 10]

    // ดึงชื่อผู้รีวิว[cite: 10]
    public String getReviewer() {
        return reviewer;
    }

    // ดึงคะแนนรีวิว[cite: 10]
    public String getRating() {
        return rating;
    }

    // ดึงข้อความความคิดเห็น[cite: 10]
    public String getComment() {
        return comment;
    }

    // ดึงวันที่รีวิว[cite: 10]
    public String getDate() {
        return date;
    }
}
