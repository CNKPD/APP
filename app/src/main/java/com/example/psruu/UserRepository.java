package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 12]

import android.content.Context; // นำเข้าคลาส Context สำหรับจัดการสภาพแวดล้อมและดึงข้อมูล SharedPreferences[cite: 12]
import android.content.SharedPreferences; // นำเข้าคลาสจัดการจัดเก็บข้อมูลแบบ Key-Value ในเครื่อง[cite: 12]

// คลาส UserRepository ทำหน้าที่จัดการข้อมูลผู้ใช้ การบันทึก และดึงข้อมูลผ่าน SharedPreferences[cite: 12]
public class UserRepository {
    private SharedPreferences prefs; // ตัวจัดการ SharedPreferences สำหรับเก็บข้อมูลผู้ใช้[cite: 12]
    private Context context; // ตัวแปรเก็บ Context ของแอปพลิเคชัน[cite: 12]

    // Constructor ของคลาส ทำหน้าที่กำหนดค่าเริ่มต้นและเชื่อมต่อ SharedPreferences[cite: 12]
    public UserRepository(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("PSRU_USER_PREF", Context.MODE_PRIVATE);
    }

    // เมธอดตรวจสอบการเข้าสู่ระบบเบื้องต้นจากชื่อผู้ใช้และรหัสผ่าน[cite: 12]
    public boolean authenticate(String username, String password) {
        return username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty();
    }

    // เมธอดสำหรับบันทึกข้อมูลการสมัครสมาชิก[cite: 12]
    public void registerUser(String name, String studentId, String email, String password,
                             String imageUri, String facebook, String instagram, String phone) {
        SharedPreferences.Editor editor = prefs.edit(); // เปิดโหมดแก้ไขข้อมูลใน SharedPreferences[cite: 12]
        editor.putString("USER_NAME", name); // บันทึกชื่อผู้ใช้[cite: 12]
        editor.putString("USER_STUDENT_ID", studentId); // บันทึกรหัสนักศึกษา[cite: 12]
        editor.putString("USER_EMAIL", email); // บันทึกอีเมล[cite: 12]
        editor.putString("USER_PASSWORD", password); // บันทึกรหัสผ่าน[cite: 12]
        editor.putString("USER_IMAGE", imageUri); // บันทึกที่อยู่รูปโปรไฟล์[cite: 12]
        editor.putString("USER_FACEBOOK", facebook); // บันทึกข้อมูลเฟซบุ๊ก[cite: 12]
        editor.putString("USER_INSTAGRAM", instagram); // บันทึกข้อมูลอินสตาแกรม[cite: 12]
        editor.putString("USER_PHONE", phone); // บันทึกเบอร์โทรศัพท์[cite: 12]
        editor.apply(); // บันทึกข้อมูลลงหน่วยความจำแบบ asynchronous[cite: 12]
    }

    // เมธอดสำหรับดึงข้อมูลโปรไฟล์ (ปรับลำดับพารามิเตอร์ให้ตรงกับ UserProfile constructor ของคุณ)[cite: 12]
    public UserProfile getUserProfile() {
        String name = prefs.getString("USER_NAME", "ชื่อผู้ขาย"); // ดึงชื่อผู้ใช้ (ค่าเริ่มต้นคือ "ชื่อผู้ขาย")[cite: 12]
        String studentId = prefs.getString("USER_STUDENT_ID", "-"); // ดึงรหัสนักศึกษา[cite: 12]
        String imageUri = prefs.getString("USER_IMAGE", ""); // ดึงที่อยู่รูปโปรไฟล์[cite: 12]
        String facebook = prefs.getString("USER_FACEBOOK", "-"); // ดึงข้อมูลเฟซบุ๊ก[cite: 12]
        String instagram = prefs.getString("USER_INSTAGRAM", "-"); // ดึงข้อมูลอินสตาแกรม[cite: 12]
        String phone = prefs.getString("USER_PHONE", "-"); // ดึงเบอร์โทรศัพท์[cite: 12]

        // ส่งค่าครบทั้ง 6 ตัว (รวม studentId และ imageUri ให้ตรงกับ Constructor ของ UserProfile)[cite: 12]
        return new UserProfile(name, studentId, imageUri, facebook, instagram, phone);
    }

    // เมธอดสำหรับบันทึกคะแนนรีวิวเฉลี่ย[cite: 12]
    public void saveAverageRating(float rating) {
        prefs.edit().putFloat("USER_AVG_RATING", rating).apply();
    }

    // คำนวณคะแนนเฉลี่ยจากรีวิวจริง ๆ ที่ถูกบันทึกไว้[cite: 12]
    public float getAverageRating() {
        try {
            ProfileRepository profileRepo = new ProfileRepository(context); // สร้างออบเจ็กต์ ProfileRepository[cite: 12]
            java.util.List<ReviewItem> reviews = profileRepo.getReviewList(); // ดึงรายการรีวิวทั้งหมด[cite: 12]
            if (reviews == null || reviews.isEmpty()) {
                return prefs.getFloat("USER_AVG_RATING", 5.0f); // ค่าเริ่มต้นถ้ายังไม่มีรีวิว[cite: 12]
            }

            float totalSum = 0; // ตัวแปรเก็บผลรวมคะแนนทั้งหมด[cite: 12]
            for (ReviewItem item : reviews) {
                try {
                    totalSum += Float.parseFloat(item.getRating()); // แปลงคะแนนรีวิวเป็นตัวเลขแล้วบวกสะสม[cite: 12]
                } catch (Exception ignored) {
                }
            }
            float average = totalSum / reviews.size(); // คำนวณค่าเฉลี่ย[cite: 12]
            // ปัดเศษให้เหลือทศนิยม 1 ตำแหน่ง[cite: 12]
            return Math.round(average * 10.0f) / 10.0f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefs.getFloat("USER_AVG_RATING", 5.0f); // คืนค่าเริ่มต้นหากเกิดข้อผิดพลาด[cite: 12]
    }
}
