package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 11]

import android.app.Dialog; // นำเข้าคลาส Dialog สำหรับสร้างหน้าต่างป๊อปอัป (Popup)[cite: 11]
import android.content.Context; // นำเข้าคลาส Context สำหรับจัดการสภาพแวดล้อมของแอปพลิเคชัน[cite: 11]
import android.content.Intent; // นำเข้าคลาส Intent สำหรับสั่งเปิดหน้าจอใหม่[cite: 11]
import android.view.LayoutInflater; // นำเข้าคลาสแปลงไฟล์ XML เลย์เอาต์ให้ออกมาเป็น View[cite: 11]
import android.view.View; // นำเข้าคลาสพื้นฐานขององค์ประกอบ UI[cite: 11]
import android.widget.Button; // นำเข้าคลาสปุ่มกด[cite: 11]

// คลาส WantBoardRepository ทำหน้าที่จัดการตรรกะเบื้องหลังและการสร้างหน้าต่าง Dialog ของบอร์ดตามหาของ
public class WantBoardRepository {

    private Context context; // ตัวแปรเก็บ Context สำหรับใช้งานเปิดหน้าจอหรือจัดการ Layout[cite: 11]

    // [Interface] กำหนดรูปแบบของ Callback สำหรับดักจับเหตุการณ์การคลิกปุ่มภายใน Dialog[cite: 11]
    public interface OnDialogActionListener {
        void onContactClicked(); // เมธอดที่จะทำงานเมื่อมีการกดปุ่มติดต่อ[cite: 11]
    }

    // Constructor ของคลาส ทำหน้าที่รับค่า Context เข้ามาเก็บไว้ใช้งานเมื่อมีการสร้างออบเจ็กต์[cite: 11]
    public WantBoardRepository(Context context) {
        this.context = context;
    }

    /**
     * จัดการสร้าง Dialog รายละเอียดของประกาศตามหาของ (แยกตรรกะออกจาก Activity)[cite: 11]
     */
    public void createWantedDetailDialog(String posterName, String postTitle, String budget, OnDialogActionListener listener) {
        try {
            Dialog dialog = new Dialog(context); // สร้างออบเจ็กต์ Dialog ขึ้นมาใหม่โดยอิงจาก Context[cite: 11]

            LayoutInflater inflater = LayoutInflater.from(context); // เรียกใช้งาน LayoutInflater[cite: 11]
            View dialogView = inflater.inflate(R.layout.dialog_wanted_detail, null); // แปลงไฟล์ XML (dialog_wanted_detail) ให้เป็น View[cite: 11]
            dialog.setContentView(dialogView); // กำหนด View ให้กับ Dialog[cite: 11]

            // ตรวจสอบและตั้งค่าให้พื้นหลังของ Dialog มีความโปร่งใสสวยงาม[cite: 11]
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            // ปุ่มดูโปรไฟล์ของผู้โพสต์[cite: 11]
            Button btnViewProfile = dialogView.findViewById(R.id.btnViewProfile);
            if (btnViewProfile != null) {
                btnViewProfile.setOnClickListener(v -> {
                    try {
                        // สั่งเปิดหน้า ProfileActivity เมื่อผู้ใช้กดปุ่มดูโปรไฟล์[cite: 11]
                        Intent intent = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent);
                        dialog.dismiss(); // ปิดหน้าต่าง Dialog หลังจากกด[cite: 11]
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            // ปุ่มติดต่อผู้โพสต์[cite: 11]
            Button btnContactWanted = dialogView.findViewById(R.id.btnContactWanted);
            if (btnContactWanted != null) {
                btnContactWanted.setOnClickListener(v -> {
                    try {
                        // ตรวจสอบว่ามีผู้ฟังเหตุการณ์ (Listener) ส่งเข้ามาหรือไม่ ถ้ามีให้สั่งทำงาน[cite: 11]
                        if (listener != null) {
                            listener.onContactClicked();
                        }
                        dialog.dismiss(); // ปิดหน้าต่าง Dialog หลังจากกด[cite: 11]
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            dialog.show(); // สั่งแสดงหน้าต่าง Dialog ขึ้นมาบนหน้าจอ[cite: 11]
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
