package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 6]

import android.content.Intent; // นำเข้าคลาส Intent สำหรับสั่งเปลี่ยนหน้าจอไปยัง Activity อื่น[cite: 6]
import android.os.Bundle; // นำเข้าคลาส Bundle สำหรับส่งผ่านข้อมูลสถานะของ Activity ตอนเริ่มต้น[cite: 6]
import android.widget.Button; // นำเข้าคลาสปุ่มกด Button[cite: 6]
import android.widget.EditText; // นำเข้าคลาสช่องกรอกข้อความ EditText[cite: 6]
import android.widget.TextView; // นำเข้าคลาสแสดงข้อความ TextView[cite: 6]
import android.widget.Toast; // นำเข้าคลาสแสดงข้อความแจ้งเตือนขนาดเล็กชั่วคราว[cite: 6]
import androidx.appcompat.app.AppCompatActivity; // นำเข้าคลาสแม่ข่ายสำหรับการสร้างหน้าจอแอปพลิเคชัน[cite: 6]

// [OOP - Inheritance] ประกาศคลาส LoginActivity โดยสืบทอดความสามารถมาจาก AppCompatActivity เพื่อให้ทำหน้าที่เป็นหน้าจอเข้าสู่ระบบ[cite: 6]
public class LoginActivity extends AppCompatActivity {

    // --- ประกาศตัวแปร (Attributes) สำหรับอ้างอิงองค์ประกอบ UI และชั้นข้อมูล (Repository) ---[cite: 6]
    private EditText etLoginEmailOrId, etLoginPassword; // ตัวแปรช่องกรอกอีเมล/รหัสนักศึกษา และรหัสผ่าน[cite: 6]
    private Button btnLogin; // ตัวแปรปุ่มกดเข้าสู่ระบบ[cite: 6]
    private TextView tvGoToRegister; // ตัวแปรข้อความลิงก์สำหรับไปหน้าสมัครสมาชิก[cite: 6]
    private UserRepository userRepository; // ตัวแปรเรียกใช้คลาสจัดการข้อมูลผู้ใช้งานระบบ (Repository Pattern)[cite: 6]

    // ==========================================
    // ส่วนที่ 1: วงจรชีวิตของหน้าจอ (Activity Lifecycle)
    // ==========================================

    // เมธอด onCreate จะทำงาน "ทันที" เมื่อผู้ใช้เปิดเข้ามาที่หน้าจอเข้าสู่ระบบนี้เป็นครั้งแรก[cite: 6]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // เรียกฟังก์ชันตั้งต้นของคลาสแม่[cite: 6]
        setContentView(R.layout.activity_login); // สั่งให้หน้าจอนี้แสดงผลหน้าตาตามไฟล์ XML เลย์เอาต์ activity_login[cite: 6]

        // กำหนดใช้งาน Repository (Model/Data layer) สำหรับจัดการข้อมูลผู้ใช้[cite: 6]
        userRepository = new UserRepository(this);

        initView(); // เรียกฟังก์ชันผูกตัวแปร Java กับ ID ใน XML[cite: 6]
        initListener(); // เรียกฟังก์ชันดักจับการคลิกปุ่มต่างๆ[cite: 6]
    }

    // ==========================================
    // ส่วนที่ 2: การผูกค่า UI และการดักจับเหตุการณ์ (Event-Driven)
    // ==========================================

    // ฟังก์ชันสำหรับค้นหาและเชื่อมโยงตัวแปร Java เข้ากับ ID ของ View ต่างๆ ในไฟล์ XML[cite: 6]
    private void initView() {
        etLoginEmailOrId = findViewById(R.id.etLoginEmailOrId); // ค้นหาช่องกรอกอีเมลหรือรหัสนักศึกษา[cite: 6]
        etLoginPassword = findViewById(R.id.etLoginPassword); // ค้นหาช่องกรอกรหัสผ่าน[cite: 6]
        btnLogin = findViewById(R.id.btnLogin); // ค้นหาปุ่มเข้าสู่ระบบ[cite: 6]
        tvGoToRegister = findViewById(R.id.tvGoToRegister); // ค้นหาปุ่มไปหน้าสมัครสมาชิก[cite: 6]
    }

    // ฟังก์ชันสำหรับดักจับเหตุการณ์การกระทำของผู้ใช้ (Event-Driven Programming)[cite: 6]
    private void initListener() {
        // กดปุ่มเข้าสู่ระบบ ให้เรียกฟังก์ชันตรวจสอบการล็อกอิน handleLogin[cite: 6]
        btnLogin.setOnClickListener(v -> handleLogin());

        // ไปหน้าสมัครสมาชิก (RegisterActivity) เมื่อผู้ใช้คลิกที่ข้อความ[cite: 6]
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent); // เปิดหน้าจอสมัครสมาชิก[cite: 6]
        });
    }

    // ==========================================
    // ส่วนที่ 3: ฟังก์ชันการทำงานหลัก (Business Logic)
    // ==========================================

    // ฟังก์ชันตรวจสอบความถูกต้องของข้อมูลและดำเนินการเข้าสู่ระบบ[cite: 6]
    private void handleLogin() {
        String inputUser = etLoginEmailOrId.getText().toString().trim(); // ดึงข้อความชื่อผู้ใช้/อีเมลที่กรอก พร้อมตัดช่องว่าง[cite: 6]
        String inputPassword = etLoginPassword.getText().toString().trim(); // ดึงข้อความรหัสผ่านที่กรอก พร้อมตัดช่องว่าง[cite: 6]

        // ตรวจสอบเงื่อนไข: ถ้าช่องใดช่องหนึ่งว่างเปล่า ให้แจ้งเตือนผู้ใช้[cite: 6]
        if (inputUser.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "กรุณากรอกข้อมูลให้ครบทุกช่อง", Toast.LENGTH_SHORT).show();
            return; // หยุดการทำงานชั่วคราว[cite: 6]
        }

        // ให้ Repository เป็นคนตรวจสอบข้อมูล (แยก Business Logic ออกจาก Activity ตามหลักการออกแบบสถาปัตยกรรม)[cite: 6]
        boolean isAuthenticated = userRepository.authenticate(inputUser, inputPassword);

        // ตรวจสอบผลลัพธ์การตรวจสอบตัวตน (Authentication)[cite: 6]
        if (isAuthenticated) {
            Toast.makeText(LoginActivity.this, "เข้าสู่ระบบสำเร็จ!", Toast.LENGTH_SHORT).show(); // แจ้งเตือนสำเร็จ[cite: 6]

            // พาไปหน้าแรก (MainActivity) ทันทีด้วย Intent[cite: 6]
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // ปิดหน้าจอ Login ทันที เพื่อไม่ให้กดปุ่ม Back กลับมาได้[cite: 6]

        } else {
            // แจ้งเตือนเมื่อข้อมูลการเข้าสู่ระบบไม่ถูกต้อง[cite: 6]
            Toast.makeText(LoginActivity.this, "อีเมล/รหัสนักศึกษา หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }
}