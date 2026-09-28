package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 9]

import android.content.Intent; // นำเข้าคลาส Intent สำหรับสั่งเปลี่ยนหน้าจอ[cite: 9]
import android.graphics.Color; // นำเข้าคลาสจัดการสี[cite: 9]
import android.net.Uri; // นำเข้าคลาสจัดการ Uri ของไฟล์รูปภาพ[cite: 9]
import android.os.Bundle; // นำเข้าคลาส Bundle สำหรับส่งผ่านข้อมูลสถานะตอนเริ่มต้น Activity[cite: 9]
import android.provider.MediaStore; // นำเข้าคลาสจัดการมีเดียเพื่อเปิดคลังรูปภาพ[cite: 9]
import android.widget.Button; // นำเข้าปุ่มกด[cite: 9]
import android.widget.EditText; // นำเข้ากล่องข้อความสำหรับพิมพ์ข้อมูล[cite: 9]
import android.widget.LinearLayout; // นำเข้ากล่องจัดเลย์เอาต์[cite: 9]
import android.widget.TextView; // นำเข้าคลาสแสดงข้อความ[cite: 9]
import android.widget.Toast; // นำเข้าคลาสแสดงข้อความแจ้งเตือนขนาดเล็ก[cite: 9]
import androidx.annotation.Nullable; // นำเข้าคลาสระบุตัวแปรที่สามารถเป็นค่าว่างได้[cite: 9]
import androidx.appcompat.app.AppCompatActivity; // นำเข้าคลาสแม่ข่ายสำหรับการสร้างหน้าจอแอปพลิเคชัน[cite: 9]

import java.io.File; // นำเข้าคลาสจัดการไฟล์[cite: 9]
import java.io.FileOutputStream; // นำเข้าคลาสเขียนไฟล์สตรีม[cite: 9]
import java.io.InputStream; // นำเข้าคลาสอ่านข้อมูลไฟล์สตรีม[cite: 9]

// [OOP - Inheritance] ประกาศคลาส RegisterActivity สืบทอดมาจาก AppCompatActivity เพื่อทำหน้าที่เป็นหน้าจอสมัครสมาชิก
public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // รหัสคำขอสำหรับการเลือกรูปภาพจากแกลเลอรี[cite: 9]

    // --- ประกาศตัวแปร (Attributes) สำหรับอ้างอิงองค์ประกอบ UI และ Repository ต่างๆ ---[cite: 9]
    private EditText etRegName, etRegStudentId, etRegEmail, etRegPassword; // กล่องกรอก ชื่อ, รหัสนักศึกษา, อีเมล, รหัสผ่าน[cite: 9]
    private EditText etRegFacebook, etRegInstagram, etRegPhone; // กล่องกรอกข้อมูลช่องทางติดต่อเสริม[cite: 9]
    private LinearLayout btnSelectProfileImage; // ปุ่มสำหรับกดเลือกรูปโปรไฟล์[cite: 9]
    private TextView tvSelectedImageStatus, tvBackToLogin; // ข้อความแสดงสถานะไฟล์รูปที่เลือก และปุ่มกลับหน้าเข้าสู่ระบบ[cite: 9]
    private Button btnRegister; // ปุ่มกดสมัครสมาชิก[cite: 9]

    private String profileImageUriStr = ""; // ตัวแปรเก็บที่อยู่ (URI) ของรูปโปรไฟล์ในรูปแบบข้อความ[cite: 9]
    private UserRepository userRepository; // ตัวจัดการข้อมูลผู้ใช้งานระบบ (Model/Data Layer)[cite: 9]

    // ==========================================
    // ส่วนที่ 1: วงจรชีวิตของหน้าจอ (Activity Lifecycle)
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // กำหนดเลย์เอาต์ XML สำหรับหน้าจอสมัครสมาชิก[cite: 9]

        userRepository = new UserRepository(this); // สร้างออบเจ็กต์ UserRepository[cite: 9]

        initViews(); // เรียกฟังก์ชันเชื่อมโยงตัวแปร UI กับ XML[cite: 9]
        setupListeners(); // เรียกฟังก์ชันดักจับการคลิกปุ่มต่างๆ[cite: 9]
    }

    // ฟังก์ชันเชื่อมโยงตัวแปร Java เข้ากับ ID ในไฟล์ XML[cite: 9]
    private void initViews() {
        etRegName = findViewById(R.id.etRegName);
        etRegStudentId = findViewById(R.id.etRegStudentId);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);

        etRegFacebook = findViewById(R.id.etRegFacebook);
        etRegInstagram = findViewById(R.id.etRegInstagram);
        etRegPhone = findViewById(R.id.etRegPhone);

        btnSelectProfileImage = findViewById(R.id.btnSelectProfileImage);
        tvSelectedImageStatus = findViewById(R.id.tvSelectedImageStatus);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    // ฟังก์ชันกำหนดการทำงานเมื่อผู้ใช้งานกดปุ่มต่างๆ[cite: 9]
    private void setupListeners() {
        // กดเลือกรูปโปรไฟล์ เปิดแกลเลอรี[cite: 9]
        btnSelectProfileImage.setOnClickListener(v -> openGallery());

        // กดปุ่มสมัครสมาชิก[cite: 9]
        btnRegister.setOnClickListener(v -> handleRegistration());

        // กดปุ่มย้อนกลับไปหน้า Login[cite: 9]
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    // ==========================================
    // ส่วนที่ 2: ระบบจัดการการสมัครสมาชิก (Business Logic)
    // ==========================================

    // ฟังก์ชันตรวจสอบความถูกต้องของข้อมูลและดำเนินการสมัครสมาชิก[cite: 9]
    private void handleRegistration() {
        // ดึงข้อความจากช่องกรอกข้อมูลต่างๆ พร้อมตัดช่องว่างส่วนเกิน[cite: 9]
        String name = etRegName.getText().toString().trim();
        String studentId = etRegStudentId.getText().toString().trim();
        String email = etRegEmail.getText().toString().trim();
        String password = etRegPassword.getText().toString().trim();

        String facebook = etRegFacebook != null ? etRegFacebook.getText().toString().trim() : "";
        String instagram = etRegInstagram != null ? etRegInstagram.getText().toString().trim() : "";
        String phone = etRegPhone != null ? etRegPhone.getText().toString().trim() : "";

        // ตรวจสอบว่ากรอกข้อมูลสำคัญครบถ้วนหรือไม่[cite: 9]
        if (name.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบทุกช่อง", Toast.LENGTH_SHORT).show();
            return;
        }

        // ตรวจสอบว่าผู้ใช้เลือกรูปโปรไฟล์หรือยัง[cite: 9]
        if (profileImageUriStr.isEmpty()) {
            Toast.makeText(this, "กรุณาเลือกรูปโปรไฟล์", Toast.LENGTH_SHORT).show();
            return;
        }

        // ส่งข้อมูลให้ UserRepository เป็นผู้จัดการบันทึกข้อมูล (แยก Business Logic ออกจาก Activity)[cite: 9]
        userRepository.registerUser(name, studentId, email, password, profileImageUriStr, facebook, instagram, phone);

        Toast.makeText(this, "สมัครสมาชิกสำเร็จ!", Toast.LENGTH_SHORT).show();
        finish(); // ปิดหน้าจอนี้และกลับสู่หน้า Login[cite: 9]
    }

    // ==========================================
    // ส่วนที่ 3: ระบบจัดการเลือกและบันทึกรูปภาพ
    // ==========================================

    // ฟังก์ชันเปิดคลังรูปภาพในเครื่อง (Gallery)[cite: 9]
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // เมธอดรับผลลัพธ์หลังจากผู้ใช้เลือกรูปภาพจากแกลเลอรีแล้ว[cite: 9]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri sourceUri = data.getData();

            // คัดลอกและบันทึกรูปภาพลงในหน่วยความจำแคชภายในแอป[cite: 9]
            File savedFile = saveUriToInternalCache(sourceUri);
            if (savedFile != null) {
                profileImageUriStr = Uri.fromFile(savedFile).toString();
                tvSelectedImageStatus.setText("เลือกไฟล์แล้ว: " + savedFile.getName());
                tvSelectedImageStatus.setTextColor(Color.parseColor("#00794C")); // เปลี่ยนสีข้อความเมื่อเลือกสำเร็จ[cite: 9]
            } else {
                Toast.makeText(this, "ไม่สามารถโหลดรูปภาพนี้ได้", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ฟังก์ชันช่วยคัดลอกไฟล์รูปจาก URI ภายนอกมาเก็บไว้ใน Cache ภายในแอป[cite: 9]
    private File saveUriToInternalCache(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File cacheDir = getCacheDir();
            File destinationFile = new File(cacheDir, "profile_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return destinationFile; // คืนค่าไฟล์รูปภาพที่บันทึกเรียบร้อยแล้ว[cite: 9]
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
