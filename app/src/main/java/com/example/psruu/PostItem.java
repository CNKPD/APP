package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 7]

// [OOP - Encapsulation] ประกาศคลาส PostItem ทำหน้าที่เป็นคลาสโมเดล (Model) สำหรับเก็บโครงสร้างข้อมูลของโพสต์แต่ละรายการ[cite: 7]
public class PostItem {
    // --- ประกาศตัวแปร (Attributes) สำหรับเก็บคุณลักษณะต่างๆ ของโพสต์ ---[cite: 7]
    private String title;          // หัวข้อโพสต์[cite: 7]
    private String priceOrBudget;  // ราคาหรือบัดเจ็ทของโพสต์[cite: 7]
    private String type;           // ประเภทของโพสต์[cite: 7]
    private String imageUri;       // สตริงรูปภาพของโพสต์ (เช่น รูปในรูปแบบ Base64)[cite: 7]
    private String sourceBoard;    // บอร์ดต้นทางที่โพสต์นี้ถูกดึงมา[cite: 7]
    private int originalIndex;     // ตำแหน่งดัชนี (Index) เดิมของโพสต์[cite: 7]

    // [OOP - Constructor] คอนสตรัคเตอร์สำหรับกำหนดค่าเริ่มต้นให้กับออบเจ็กต์ PostItem ทุกครั้งที่มีการสร้างขึ้นมาใหม่[cite: 7]
    public PostItem(String title, String priceOrBudget, String type, String imageUri, String sourceBoard, int originalIndex) {
        this.title = title;                   // กำหนดหัวข้อโพสต์[cite: 7]
        this.priceOrBudget = priceOrBudget;   // กำหนดราคาหรือบัดเจ็ท[cite: 7]
        this.type = type;                     // กำหนดประเภทโพสต์[cite: 7]
        this.imageUri = imageUri;             // กำหนดรูปภาพ[cite: 7]
        this.sourceBoard = sourceBoard;       // กำหนดบอร์ดต้นทาง[cite: 7]
        this.originalIndex = originalIndex;   // กำหนดตำแหน่งดัชนีเดิม[cite: 7]
    }

    // ==========================================
    // เมธอด Getter สำหรับดึงค่าข้อมูลต่างๆ ออกไปใช้งาน (Encapsulation)
    // ==========================================

    // เมธอดดึงหัวข้อโพสต์[cite: 7]
    public String getTitle() { return title; }

    // เมธอดดึงราคาหรือบัดเจ็ทของโพสต์[cite: 7]
    public String getPriceOrBudget() { return priceOrBudget; }

    // เมธอดดึงประเภทของโพสต์[cite: 7]
    public String getType() { return type; }

    // เมธอดดึงข้อมูลรูปภาพของโพสต์[cite: 7]
    public String getImageUri() { return imageUri; }

    // เมธอดดึงชื่อบอร์ดต้นทาง[cite: 7]
    public String getSourceBoard() { return sourceBoard; }

    // เมธอดดึงตำแหน่งดัชนีเดิมของโพสต์[cite: 7]
    public int getOriginalIndex() { return originalIndex; }
}