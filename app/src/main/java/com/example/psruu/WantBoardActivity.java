package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 10]

import android.os.Bundle; // นำเข้าคลาส Bundle สำหรับส่งผ่านข้อมูลสถานะตอนเริ่มต้น Activity[cite: 10]
import android.widget.Toast; // นำเข้าคลาสแสดงข้อความแจ้งเตือนขนาดเล็ก[cite: 10]

import androidx.appcompat.app.AppCompatActivity; // นำเข้าคลาสแม่ข่ายสำหรับการสร้างหน้าจอแอปพลิเคชัน[cite: 10]

// [OOP - Inheritance] ประกาศคลาส WantBoardActivity สืบทอดมาจาก AppCompatActivity เพื่อทำหน้าที่เป็นหน้าจอบอร์ดตามหาของ
public class WantBoardActivity extends AppCompatActivity {

    private WantBoardRepository wantBoardRepository; // ตัวจัดการข้อมูลและตรรกะของบอร์ดตามหาของ (Model/Data Layer)[cite: 10]

    // ==========================================
    // ส่วนที่ 1: วงจรชีวิตของหน้าจอ (Activity Lifecycle)
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_wanted); // กำหนดเลย์เอาต์ XML สำหรับหน้าจอบอร์ดตามหาของ[cite: 10]

        wantBoardRepository = new WantBoardRepository(this); // สร้างออบเจ็กต์ WantBoardRepository โดยอิงจาก Context ปัจจุบัน[cite: 10]

        // ตัวอย่างการเรียกใช้งานเมื่อผู้ใช้คลิกรายการในบอร์ดตามหาของ[cite: 10]
        // showWantedDetailDialog("ชื่อผู้โพสต์ตัวอย่าง", "ตามหาหนังสือเรียน...", "500 บาท");[cite: 10]
    }

    /**
     * ควบคุมการแสดง Dialog รายละเอียดของบอร์ดตามหา[cite: 10]
     */
    public void showWantedDetailDialog(String posterName, String postTitle, String budget) {
        try {
            // ให้ Repository จัดการสร้างและผูกเหตุการณ์ของ Dialog (แยก Business Logic ออกจาก Activity)[cite: 10]
            wantBoardRepository.createWantedDetailDialog(posterName, postTitle, budget, new WantBoardRepository.OnDialogActionListener() {
                @Override
                public void onContactClicked() {
                    // จัดการตรรกะเมื่อกดปุ่มติดต่อ (เช่น แสดง Toast หรือเปิดระบบโทร)[cite: 10]
                    Toast.makeText(WantBoardActivity.this, "ระบบแชทถูกปิดใช้งาน", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
