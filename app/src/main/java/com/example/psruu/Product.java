package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 8]

// [OOP - Encapsulation] ประกาศคลาส Product ทำหน้าที่เป็นคลาสโมเดล (Model) สำหรับเก็บโครงสร้างข้อมูลของสินค้าแต่ละรายการ[cite: 8]
public class Product {
    // --- ประกาศตัวแปร (Attributes) สำหรับเก็บคุณลักษณะต่างๆ ของสินค้า ---[cite: 8]
    private String name;              // ชื่อสินค้า[cite: 8]
    private String price;             // ราคาสินค้า[cite: 8]
    private String category;          // หมวดหมู่สินค้า[cite: 8]
    private String imageUri;          // สตริงรูปภาพสินค้า (เช่น รูปในรูปแบบ Base64)[cite: 8]
    private String detail;            // รายละเอียดของสินค้า[cite: 8]
    private String location;          // สถานที่นัดรับสินค้า[cite: 8]
    private int postType;             // ประเภทของโพสต์[cite: 8]
    private String sellerName;        // ชื่อผู้ขาย[cite: 8]
    private String sellerFacebook;    // เฟซบุ๊กผู้ขาย[cite: 8]
    private String sellerInstagram;   // อินสตาแกรมผู้ขาย[cite: 8]
    private String sellerPhone;       // เบอร์โทรศัพท์ผู้ขาย[cite: 8]
    private String sellerProfileImage;// รูปโปรไฟล์ผู้ขาย[cite: 8]

    // [OOP - Constructor] คอนสตรัคเตอร์สำหรับกำหนดค่าเริ่มต้นให้กับออบเจ็กต์ Product ทุกครั้งที่มีการสร้างขึ้นมาใหม่ (รับพารามิเตอร์ครบทั้ง 12 ตัว)[cite: 8]
    public Product(String name, String price, String category, String imageUri,
                   String detail, String location, int postType,
                   String sellerName, String sellerFacebook, String sellerInstagram,
                   String sellerPhone, String sellerProfileImage) {
        this.name = name;                             // กำหนดชื่อสินค้า[cite: 8]
        this.price = price;                           // กำหนดราคาสินค้า[cite: 8]
        this.category = category;                     // กำหนดหมวดหมู่สินค้า[cite: 8]
        this.imageUri = imageUri;                     // กำหนดรูปภาพสินค้า[cite: 8]
        this.detail = detail;                         // กำหนดรายละเอียดสินค้า[cite: 8]
        this.location = location;                     // กำหนดสถานที่นัดรับ[cite: 8]
        this.postType = postType;                     // กำหนดประเภทโพสต์[cite: 8]
        this.sellerName = sellerName;                 // กำหนดชื่อผู้ขาย[cite: 8]
        this.sellerFacebook = sellerFacebook;         // กำหนดเฟซบุ๊กผู้ขาย[cite: 8]
        this.sellerInstagram = sellerInstagram;       // กำหนดอินสตาแกรมผู้ขาย[cite: 8]
        this.sellerPhone = sellerPhone;               // กำหนดเบอร์โทรผู้ขาย[cite: 8]
        this.sellerProfileImage = sellerProfileImage; // กำหนดรูปโปรไฟล์ผู้ขาย[cite: 8]
    }

    // ==========================================
    // เมธอด Getter สำหรับดึงค่าข้อมูลต่างๆ ออกไปใช้งาน (Encapsulation)
    // ==========================================

    // เมธอดดึงชื่อสินค้า[cite: 8]
    public String getName() { return name; }

    // เมธอดดึงราคาสินค้า[cite: 8]
    public String getPrice() { return price; }

    // เมธอดดึงหมวดหมู่สินค้า[cite: 8]
    public String getCategory() { return category; }

    // เมธอดดึงข้อมูลรูปภาพสินค้า[cite: 8]
    public String getImageUri() { return imageUri; }

    // เมธอดดึงรายละเอียดสินค้า[cite: 8]
    public String getDetail() { return detail; }

    // เมธอดดึงสถานที่นัดรับสินค้า[cite: 8]
    public String getLocation() { return location; }

    // เมธอดดึงประเภทของโพสต์[cite: 8]
    public int getPostType() { return postType; }

    // เมธอดดึงชื่อผู้ขาย[cite: 8]
    public String getSellerName() { return sellerName; }

    // เมธอดดึงเฟซบุ๊กผู้ขาย[cite: 8]
    public String getSellerFacebook() { return sellerFacebook; }

    // เมธอดดึงอินสตาแกรมผู้ขาย[cite: 8]
    public String getSellerInstagram() { return sellerInstagram; }

    // เมธอดดึงเบอร์โทรผู้ขาย[cite: 8]
    public String getSellerPhone() { return sellerPhone; }

    // เมธอดดึงรูปโปรไฟล์ผู้ขาย[cite: 8]
    public String getSellerProfileImage() { return sellerProfileImage; }
}