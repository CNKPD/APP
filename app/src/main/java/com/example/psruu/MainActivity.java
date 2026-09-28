package com.example.psruu; // ประกาศบอกว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu

import android.app.Dialog; // นำเข้าคลาส Dialog เพื่อใช้สร้างหน้าต่างป๊อปอัป (Popup) บนหน้าจอ
import android.content.Intent; // นำเข้าคลาส Intent เพื่อใช้สั่งเปลี่ยนหน้าจอหรือส่งข้อมูลข้ามหน้า
import android.graphics.Bitmap; // นำเข้าคลาส Bitmap สำหรับจัดการข้อมูลรูปภาพในหน่วยความจำ
import android.graphics.BitmapFactory; // นำเข้าคลาสสำหรับแปลงข้อมูลไบต์ให้เป็นรูปภาพ Bitmap
import android.graphics.Color; // นำเข้าคลาสสำหรับจัดการชุดสีต่างๆ
import android.graphics.drawable.ColorDrawable; // นำเข้าคลาสสำหรับกำหนดพื้นหลังสีโปร่งใสให้ Dialog
import android.net.Uri; // นำเข้าคลาส Uri สำหรับจัดการที่อยู่ของไฟล์หรือลิงก์รูปภาพ
import android.os.Bundle; // นำเข้าคลาส Bundle สำหรับส่งผ่านข้อมูลสถานะของ Activity
import android.provider.MediaStore; // นำเข้าคลาสเข้าถึงคลังรูปภาพ (Gallery) ในเครื่อง
import android.util.Base64; // นำเข้าคลาสสำหรับเข้ารหัสและถอดรหัสข้อมูลภาพเป็นตัวอักษร Base64
import android.view.View; // นำเข้าคลาส View แม่แบบสำหรับจัดการปุ่มหรือเลย์เอาต์บนหน้าจอ
import android.view.ViewGroup; // นำเข้าคลาสจัดการขนาดความกว้างความสูงของเลย์เอาต์
import android.widget.ArrayAdapter; // นำเข้าคลาสแปลงข้อมูลอาเรย์ให้ใส่ลงใน Spinner (รายการเลือก) ได้
import android.widget.Button; // นำเข้าคลาสปุ่มกด Button
import android.widget.EditText; // นำเข้าคลาสช่องพิมพ์ข้อความ EditText
import android.widget.ImageView; // นำเข้าคลาสแสดงรูปภาพ ImageView
import android.widget.LinearLayout; // นำเข้าคลาสเลย์เอาต์แบบเรียงแถวแนวตั้ง/แนวนอน
import android.widget.RatingBar; // นำเข้าคลาสแสดงดาวรีวิว
import android.widget.Spinner; // นำเข้าคลาสเมนูแบบดรอปดาวน์เลือกข้อมูล
import android.widget.TextView; // นำเข้าคลาสแสดงข้อความ TextView
import android.widget.Toast; // นำเข้าคลาสแสดงข้อความแจ้งเตือนขนาดเล็กชั่วคราว (Toast message)

import androidx.annotation.Nullable; // ระบุว่าตัวแปรหรือพารามิเตอร์นี้สามารถเป็นค่าว่าง (null) ได้
import androidx.appcompat.app.AppCompatActivity; // นำเข้าคลาสแม่ข่ายสำหรับการสร้างหน้าจอแอปพลิเคชัน Android

import java.io.ByteArrayOutputStream; // นำเข้าคลาสแปลงข้อมูลรูปภาพเป็นสตรีมไบต์
import java.io.InputStream; // นำเข้าคลาสอ่านข้อมูลสตรีมไฟล์
import java.util.List; // นำเข้าโครงสร้างข้อมูลแบบรายการ (List)

// [OOP - Inheritance] ประกาศคลาสชื่อ MainActivity โดยทำการ "สืบทอด (extends)" ความสามารถมาจาก AppCompatActivity
// ทำให้ออบเจ็กต์นี้มีสถานะเป็น "หน้าจอ (Activity)" ที่ระบบ Android สามารถควบคุมวงจรชีวิต (Lifecycle) ได้
public class MainActivity extends AppCompatActivity {

    // ประกาศตัวแปรคงที่ (Constant) กำหนดรหัสคำขอเลือกรูปภาพจากแกลเลอรีมีค่าเท่ากับ 1
    private static final int PICK_IMAGE_REQUEST = 1;

    // --- ประกาศตัวแปร (Attributes) สำหรับอ้างอิงองค์ประกอบหน้าจอ (UI Components) ---
    private TextView btnOpenPost; // ตัวแปรปุ่มสำหรับเปิดหน้าต่างสร้างโพสต์
    private LinearLayout containerProducts; // ตัวแปรกล่องพื้นที่สำหรับแสดงรายการสินค้า
    private TextView tabMarketplace, tabWantedBoard, tvSectionTitle; // ตัวแปรแท็บตลาดสินค้า, แท็บตามหาของ และหัวข้อหน้าจอ
    private TextView catAll, catBook, catEquipment, catIt; // ตัวแปรปุ่มกรองหมวดหมู่สินค้าต่างๆ
    private TextView typeAll, typeSell, typeSwap, typeFree; // ตัวแปรปุ่มกรองประเภทโพสต์ย่อย

    private ImageView ivPreviewImage; // ตัวแปรแสดงรูปภาพตัวอย่างตอนกำลังโพสต์สินค้า
    private LinearLayout layoutPlaceholderImage; // ตัวแปรเลย์เอาต์ไอคอนรูปภาพสำรองตอนยังไม่เลือกภาพ
    private String selectedImageUriStr = ""; // ตัวแปรสตริงเก็บค่า URI ของรูปภาพที่แปลงเป็น Base64 แล้ว (เริ่มต้นเป็นค่าว่าง)
    private Dialog currentDialog; // ตัวแปรเก็บหน้าต่าง Dialog ที่กำลังเปิดใช้งานอยู่ปัจจุบัน
    private LinearLayout currentRowLayout = null; // ตัวแปรเก็บเลย์เอาต์แถวปัจจุบันของการ์ดสินค้า

    private boolean isWantedTab = false; // ตัวแปรเช็คสถานะว่าตอนนี้อยู่แท็บตามหาของหรือไม่ (ค่าเริ่มต้นคือ false อยู่ตลาดปกติ)
    private int selectedCategoryIndex = 0; // ตัวแปรเก็บลำดับดัชนีหมวดหมู่สินค้าที่ถูกเลือก (เริ่มต้นที่ 0 คือทั้งหมด)
    private int selectedPostTypeFilter = 0; // ตัวแปรเก็บตัวกรองประเภทโพสต์หลัก
    private int selectedPostTypeIndex = 0; // ตัวแปรเก็บลำดับประเภทโพสต์ตอนสร้างสินค้า

    // --- ประกาศตัวแปรเชื่อมโยงคลาสจัดการข้อมูล (Repositories / Model Layer ตามหลัก OOP) ---
    private ProductRepository productRepository; // ตัวแปรเรียกใช้คลาสจัดการข้อมูลสินค้า (บันทึก/ดึง/ลบสินค้า)
    private UserRepository userRepository; // ตัวแปรเรียกใช้คลาสจัดการข้อมูลผู้ใช้งานระบบ

    // ==========================================
    // ส่วนที่ 1: วงจรชีวิตของหน้าจอ (Activity Lifecycle)
    // ==========================================

    // เมธอด onCreate จะทำงาน "ทันที" เมื่อหน้าจอนี้ถูกเปิดขึ้นมาครั้งแรก (เปรียบเสมือนจุดเริ่มต้นการทำงาน)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // เรียกใช้ฟังก์ชันตั้งต้นของคลาสแม่ (AppCompatActivity)
        setContentView(R.layout.activity_main); // สั่งให้หน้าจอนี้แสดงผลหน้าตาตามไฟล์เลย์เอาต์ XML ชื่อ activity_main

        // สร้างออบเจ็กต์ (Instantiation) ของ Repository โดยส่ง context (this) เข้าไปเพื่อเชื่อมโยงการทำงานกับแอป
        productRepository = new ProductRepository(this);
        userRepository = new UserRepository(this);

        initViews(); // เรียกใช้ฟังก์ชันภายในเพื่อผูกตัวแปร Java กับรหัส ID ในหน้าจอ XML
        initListeners(); // เรียกใช้ฟังก์ชันภายในเพื่อดักจับการคลิกปุ่มหรือการกระทำของผู้ใช้ (Event-Driven)
        loadSavedProducts(); // เรียกใช้ฟังก์ชันโหลดข้อมูลสินค้าขึ้นมาแสดงผลบนหน้าจอ
        updatePostTypeFilterUI(); // เรียกใช้ฟังก์ชันอัปเดตหน้าตาปุ่มกรองประเภทโพสต์
    }

    // เมธอด onResume จะทำงานทุกครั้งที่หน้าจอนี้ "กลับมาแสดงผลต่อหน้าผู้ใช้อีกครั้ง" (เช่น กดปุ่ม Back กลับมาจากหน้าอื่น)
    @Override
    protected void onResume() {
        super.onResume(); // เรียกฟังก์ชัน onResume ของคลาสแม่
        loadSavedProducts(); // สั่งโหลดข้อมูลสินค้าใหม่ทันที เพื่อให้ข้อมูลบนหน้าจออัปเดตเป็นปัจจุบันเสมอ
    }

    // ==========================================
    // ส่วนที่ 2: การผูกค่า UI และการดักจับเหตุการณ์ (Event-Driven)
    // ==========================================

    // ฟังก์ชันสำหรับค้นหาและเชื่อมโยงตัวแปร Java เข้ากับ ID ของ View ต่างๆ ในไฟล์ XML
    private void initViews() {
        btnOpenPost = findViewById(R.id.btnOpenPost); // ค้นหาปุ่มโพสต์จาก ID ใน XML มาเก็บไว้ในตัวแปร
        containerProducts = findViewById(R.id.containerProducts); // ค้นหากล่องแสดงรายการสินค้าจาก ID ใน XML
        tabMarketplace = findViewById(R.id.tabMarketplace); // ค้นหาปุ่มแท็บตลาดสินค้า
        tabWantedBoard = findViewById(R.id.tabWantedBoard); // ค้นหาปุ่มแท็บบอร์ดตามหาของ
        tvSectionTitle = findViewById(R.id.tvSectionTitle); // ค้นหาข้อความหัวข้อหมวดหมู่

        catAll = findViewById(R.id.catAll); // ค้นหาปุ่มหมวดหมู่ทั้งหมด
        catBook = findViewById(R.id.catBook); // ค้นหาปุ่มหมวดหมู่หนังสือเรียน
        catEquipment = findViewById(R.id.catEquipment); // ค้นหาปุ่มหมวดหมู่อุปกรณ์การเรียน
        catIt = findViewById(R.id.catIt); // ค้นหาปุ่มหมวดหมู่ไอที

        typeAll = findViewById(R.id.typeAll); // ค้นหาปุ่มกรองประเภททั้งหมด
        typeSell = findViewById(R.id.typeSell); // ค้นหาปุ่มกรองประเภทขาย
        typeSwap = findViewById(R.id.typeSwap); // ค้นหาปุ่มกรองประเภทแลก
        typeFree = findViewById(R.id.typeFree); // ค้นหาปุ่มกรองประเภทให้ฟรี
    }

    // ฟังก์ชันสำหรับดักจับเหตุการณ์การกระทำของผู้ใช้ (Event-Driven Programming เช่น การคลิกปุ่ม)
    private void initListeners() {
        // เช็คว่าแท็บตลาดและแท็บตามหาไม่เป็นค่าว่าง ถ้ามีอยู่ให้เซ็ตตัวดักฟังการคลิก (SetOnClickListener)
        if (tabMarketplace != null && tabWantedBoard != null) {
            tabMarketplace.setOnClickListener(v -> switchTab(false)); // ถ้าคลิกแท็บตลาด ให้เรียกฟังก์ชัน switchTab ส่งค่า false
            tabWantedBoard.setOnClickListener(v -> switchTab(true)); // ถ้าคลิกแท็บบอร์ดตามหา ให้เรียก switchTab ส่งค่า true
        }

        // เซ็ตตัวดักจับการคลิกปุ่มเลือกหมวดหมู่สินค้าต่างๆ โดยส่งค่าลำดับดัชนี (Index) เข้าไป
        if (catAll != null) catAll.setOnClickListener(v -> filterCategory(0)); // คลิกหมวดทั้งหมด (index 0)
        if (catBook != null) catBook.setOnClickListener(v -> filterCategory(1)); // คลิกหมวดหนังสือ (index 1)
        if (catEquipment != null) catEquipment.setOnClickListener(v -> filterCategory(2)); // คลิกหมวดอุปกรณ์ (index 2)
        if (catIt != null) catIt.setOnClickListener(v -> filterCategory(3)); // คลิกหมวดไอที (index 3)

        // เซ็ตตัวดักจับการคลิกปุ่มตัวกรองประเภทโพสต์ย่อย
        if (typeAll != null) typeAll.setOnClickListener(v -> filterPostType(0));
        if (typeSell != null) typeSell.setOnClickListener(v -> filterPostType(1));
        if (typeSwap != null) typeSwap.setOnClickListener(v -> filterPostType(2));
        if (typeFree != null) typeFree.setOnClickListener(v -> filterPostType(3));

        // ถ้าคลิกปุ่มสร้างโพสต์ (btnOpenPost) ให้เปิดหน้าต่าง Dialog โพสต์สินค้า (ถ้าอยู่แท็บตามหาให้ตั้งค่าเริ่มต้นเป็น 3 ถ้าไม่ใช่ให้เป็น 0)
        if (btnOpenPost != null) {
            btnOpenPost.setOnClickListener(v -> showPostItemDialog(isWantedTab ? 3 : 0));
        }

        // ค้นหาปุ่มเมนูด้านล่างสำหรับไปหน้าโปรดและหน้าโปรไฟล์
        View navFavorite = findViewById(R.id.navFavorite);
        View navProfile = findViewById(R.id.navProfile);

        // ถ้าคลิกปุ่มไปหน้าโปรด ให้สั่งเปลี่ยนหน้าจอ (Intent) ไปยัง FavoriteActivity
        if (navFavorite != null) {
            navFavorite.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavoriteActivity.class)));
        }

        // ถ้าคลิกปุ่มไปหน้าโปรไฟล์ ให้สั่งเปลี่ยนหน้าจอ (Intent) ไปยัง ProfileActivity
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
        }
    }

    // ==========================================
    // ส่วนที่ 3: ฟังก์ชันการทำงานหลัก (Business Logic)
    // ==========================================

    // ฟังก์ชันสำหรับสลับหน้าจอแท็บระหว่าง "ตลาดสินค้าปกติ" และ "บอร์ดตามหาของ"
    private void switchTab(boolean wanted) {
        isWantedTab = wanted; // บันทึกค่าสถานะแท็บปัจจุบันลงในตัวแปร
        int filterVisibility = wanted ? View.GONE : View.VISIBLE; // ถ้าเป็นแท็บตามหาให้ซ่อนตัวกรองย่อย ถ้าไม่ใช่ให้แสดงขึ้นมา

        // กำหนดการมองเห็นของปุ่มตัวกรองประเภทย่อยตามแท็บที่เลือก
        if (typeAll != null) typeAll.setVisibility(filterVisibility);
        if (typeSell != null) typeSell.setVisibility(filterVisibility);
        if (typeSwap != null) typeSwap.setVisibility(filterVisibility);
        if (typeFree != null) typeFree.setVisibility(filterVisibility);

        // ตรวจสอบเงื่อนไขการเปลี่ยนสีและข้อความหัวข้อตามแท็บที่ผู้ใช้กดเลือก
        if (isWantedTab) {
            if (tabMarketplace != null) {
                tabMarketplace.setBackgroundColor(Color.TRANSPARENT); // ทำให้แท็บตลาดโปร่งใส
                tabMarketplace.setTextColor(Color.parseColor("#6C757D")); // เปลี่ยนสีตัวหนังสือเป็นสีเทา
            }
            if (tabWantedBoard != null) {
                tabWantedBoard.setBackgroundColor(Color.WHITE); // เปลี่ยนพื้นหลังแท็บบอร์ดตามหาเป็นสีขาว
                tabWantedBoard.setTextColor(Color.parseColor("#00794C")); // เปลี่ยนสีตัวหนังสือเป็นสีเขียว
            }
            if (tvSectionTitle != null) tvSectionTitle.setText("รายการประกาศตามหาของ"); // เปลี่ยนข้อความหัวข้อ
        } else {
            if (tabMarketplace != null) {
                tabMarketplace.setBackgroundColor(Color.WHITE); // เปลี่ยนพื้นหลังแท็บตลาดเป็นสีขาว
                tabMarketplace.setTextColor(Color.parseColor("#00794C")); // เปลี่ยนสีตัวหนังสือเป็นสีเขียว
            }
            if (tabWantedBoard != null) {
                tabWantedBoard.setBackgroundColor(Color.TRANSPARENT); // ทำให้แท็บบอร์ดตามหาโปร่งใส
                tabWantedBoard.setTextColor(Color.parseColor("#6C757D")); // เปลี่ยนสีตัวหนังสือเป็นสีเทา
            }
            if (tvSectionTitle != null) tvSectionTitle.setText("รายการสินค้า (ขาย / แลก / ให้ฟรี)"); // เปลี่ยนข้อความหัวข้อ
        }

        loadSavedProducts(); // โหลดรายการสินค้าใหม่ให้สอดคล้องกับแท็บที่เพิ่งเปลี่ยน
    }

    // ฟังก์ชันกรองข้อมูลตามหมวดหมู่สินค้า
    private void filterCategory(int index) {
        selectedCategoryIndex = index; // บันทึกลำดับดัชนีหมวดหมู่ที่ถูกเลือก
        updateCategoryButtonUI(); // อัปเดตสีปุ่มหมวดหมู่
        loadSavedProducts(); // โหลดรายการสินค้าใหม่ตามหมวดหมู่ที่เลือก
    }

    // ฟังก์ชันกรองข้อมูลตามประเภทโพสต์ย่อย
    private void filterPostType(int typeIndex) {
        selectedPostTypeFilter = typeIndex; // บันทึกค่าตัวกรองประเภทโพสต์
        updatePostTypeFilterUI(); // อัปเดตหน้าตาปุ่มกรองประเภทโพสต์
        loadSavedProducts(); // โหลดข้อมูลสินค้าใหม่ทันทีที่เปลี่ยนตัวกรอง
    }

    // ฟังก์ชันอัปเดตสีและข้อความของปุ่มหมวดหมู่สินค้าตามสถานะที่ถูกเลือก
    private void updateCategoryButtonUI() {
        TextView[] buttons = {catAll, catBook, catEquipment, catIt}; // รวบรวมปุ่มหมวดหมู่เป็นอาเรย์
        String[] texts = {"🔥 ทั้งหมด", "📖 หนังสือเรียน", "✏️ อุปกรณ์เรียน", "💻 ไอที/หูฟัง"}; // ข้อความบนปุ่ม

        // วนลูปเช็คทีละปุ่มเพื่อเปลี่ยนสีไฮไลท์ปุ่มที่ถูกเลือก
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != null) {
                buttons[i].setText(texts[i]); // กำหนดข้อความให้ปุ่ม
                if (i == selectedCategoryIndex) {
                    buttons[i].setBackgroundColor(Color.parseColor("#00794C")); // ถ้าตรงกับที่เลือกให้ใส่พื้นหลังสีเขียว
                    buttons[i].setTextColor(Color.WHITE); // ตัวหนังสือสีขาว
                } else {
                    buttons[i].setBackgroundColor(Color.parseColor("#FFFFFF")); // ถ้าไม่ใช่ให้พื้นหลังสีขาว
                    buttons[i].setTextColor(Color.parseColor("#495057")); // ตัวหนังสือสีเทาเข้ม
                }
            }
        }
    }

    // ฟังก์ชันอัปเดตสีปุ่มตัวกรองประเภทโพสต์ย่อย
    private void updatePostTypeFilterUI() {
        if (typeAll == null || typeSell == null || typeSwap == null || typeFree == null) return; // ถ้าปุ่มเป็นค่าว่างให้หยุดทำงาน

        // เช็คเงื่อนไขทีละปุ่ม แล้วเปลี่ยนสีพื้นหลังและสีตัวหนังสือตามตัวกรองที่เลือก
        typeAll.setBackgroundColor(Color.parseColor(selectedPostTypeFilter == 0 ? "#212529" : "#E9ECEF"));
        typeAll.setTextColor(Color.parseColor(selectedPostTypeFilter == 0 ? "#FFFFFF" : "#495057"));

        typeSell.setBackgroundColor(Color.parseColor(selectedPostTypeFilter == 1 ? "#00794C" : "#D1E7DD"));
        typeSell.setTextColor(Color.parseColor(selectedPostTypeFilter == 1 ? "#FFFFFF" : "#00794C"));

        typeSwap.setBackgroundColor(Color.parseColor(selectedPostTypeFilter == 2 ? "#495057" : "#E2E3E5"));
        typeSwap.setTextColor(Color.parseColor(selectedPostTypeFilter == 2 ? "#FFFFFF" : "#495057"));

        typeFree.setBackgroundColor(Color.parseColor(selectedPostTypeFilter == 3 ? "#842029" : "#F8D7DA"));
        typeFree.setTextColor(Color.parseColor(selectedPostTypeFilter == 3 ? "#FFFFFF" : "#842029"));
    }

    // ==========================================
    // ส่วนที่ 4: การจัดการ Dialog และการสร้างโพสต์สินค้าใหม่
    // ==========================================

    // ฟังก์ชันเปิดหน้าต่างป๊อปอัป (Dialog) สำหรับให้ผู้ใช้กรอกข้อมูลสร้างโพสต์สินค้า
    private void showPostItemDialog(int defaultTypeIndex) {
        currentDialog = new Dialog(MainActivity.this); // สร้างออบเจ็กต์ Dialog ขึ้นมาใหม่
        currentDialog.setContentView(R.layout.dialog_post_item); // กำหนดให้แสดงหน้าตาตามไฟล์ XML เลย์เอาต์ dialog_post_item

        // ปรับแต่งหน้าต่าง Dialog ให้พื้นหลังโปร่งใส และกำหนดความกว้างให้พอดีหน้าจอ (92%)
        if (currentDialog.getWindow() != null) {
            currentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.92);
            currentDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // ค้นหาช่องกรอกข้อมูลและปุ่มต่างๆ ภายในป๊อปอัป Dialog
        EditText etProductName = currentDialog.findViewById(R.id.etProductName); // ช่องกรอกชื่อสินค้า
        EditText etProductPrice = currentDialog.findViewById(R.id.etProductPrice); // ช่องกรอกราคา
        EditText etProductDetail = currentDialog.findViewById(R.id.etProductDetail); // ช่องกรอกรายละเอียด
        EditText etLocation = currentDialog.findViewById(R.id.etLocation); // ช่องกรอกสถานที่นัดรับ
        Spinner spinnerCategory = currentDialog.findViewById(R.id.spinnerCategory); // ดรอปดาวน์เลือกหมวดหมู่
        TextView btnClose = currentDialog.findViewById(R.id.btnClose); // ปุ่มปิดหน้าต่างป๊อปอัป
        Button btnSubmitPost = currentDialog.findViewById(R.id.btnSubmitPost); // ปุ่มกดส่งข้อมูลโพสต์

        LinearLayout btnSelectImage = currentDialog.findViewById(R.id.btnSelectImage); // ปุ่มเลือกรูปภาพ
        ivPreviewImage = currentDialog.findViewById(R.id.ivPreviewImage); // รูปภาพตัวอย่าง
        layoutPlaceholderImage = currentDialog.findViewById(R.id.layoutPlaceholderImage); // ไอคอนช่องว่างรูปภาพ
        selectedImageUriStr = ""; // ล้างค่าสตริงรูปภาพให้เป็นค่าว่าง

        // ค้นหาปุ่มเลือกประเภทโพสต์ย่อยใน Dialog
        LinearLayout btnTypeSell = currentDialog.findViewById(R.id.btnTypeSell);
        LinearLayout btnTypeSwap = currentDialog.findViewById(R.id.btnTypeSwap);
        LinearLayout btnTypeFree = currentDialog.findViewById(R.id.btnTypeFree);
        LinearLayout btnTypeWanted = currentDialog.findViewById(R.id.btnTypeWanted);

        selectedPostTypeIndex = defaultTypeIndex; // กำหนดประเภทโพสต์เริ่มต้น
        updatePostTypeUI(btnTypeSell, btnTypeSwap, btnTypeFree, btnTypeWanted); // อัปเดตหน้าตาปุ่มประเภทโพสต์

        // เซ็ตตัวดักจับการคลิกเลือกประเภทโพสต์ใน Dialog
        if (btnTypeSell != null) btnTypeSell.setOnClickListener(v -> { selectedPostTypeIndex = 0; updatePostTypeUI(btnTypeSell, btnTypeSwap, btnTypeFree, btnTypeWanted); });
        if (btnTypeSwap != null) btnTypeSwap.setOnClickListener(v -> { selectedPostTypeIndex = 1; updatePostTypeUI(btnTypeSell, btnTypeSwap, btnTypeFree, btnTypeWanted); });
        if (btnTypeFree != null) btnTypeFree.setOnClickListener(v -> { selectedPostTypeIndex = 2; updatePostTypeUI(btnTypeSell, btnTypeSwap, btnTypeFree, btnTypeWanted); });
        if (btnTypeWanted != null) btnTypeWanted.setOnClickListener(v -> { selectedPostTypeIndex = 3; updatePostTypeUI(btnTypeSell, btnTypeSwap, btnTypeFree, btnTypeWanted); });

        // สร้างรายการหมวดหมู่สำหรับใส่ลงใน Spinner (ดรอปดาวน์)
        String[] categories = {"📖 หนังสือเรียน", "✏️ อุปกรณ์เรียน", "💻 ไอที/หูฟัง", "👕 เสื้อผ้า/เบ็ดเตล็ด"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter); // ผูกอาเรย์ข้อมูลเข้ากับ Spinner

        // ถ้าคลิกปุ่มปิด (btnClose) ให้สั่งปิดหน้าต่าง Dialog
        if (btnClose != null) btnClose.setOnClickListener(v -> currentDialog.dismiss());

        // ถ้าคลิกปุ่มเลือกรูปภาพ ให้เปิด Intent ไปเลือกภาพจากแกลเลอรีในเครื่อง
        if (btnSelectImage != null) {
            btnSelectImage.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST); // ส่ง Intent พร้อมรหัสคำขอเลือกภาพ
            });
        }

        // [OOP & Repository Pattern] เมื่อผู้ใช้คลิกปุ่ม "ยืนยันการโพสต์" (btnSubmitPost)
        if (btnSubmitPost != null) {
            btnSubmitPost.setOnClickListener(v -> {
                String name = etProductName.getText().toString().trim(); // ดึงข้อความชื่อสินค้าที่ผู้ใช้พิมพ์
                String price = etProductPrice.getText().toString().trim(); // ดึงข้อความราคา
                String detail = etProductDetail.getText().toString().trim(); // ดึงข้อความรายละเอียด
                String location = etLocation.getText().toString().trim(); // ดึงข้อความสถานที่
                String category = spinnerCategory.getSelectedItem().toString(); // ดึงหมวดหมู่ที่เลือกจาก Spinner

                // ตรวจสอบว่าถ้าไม่ได้กรอกชื่อสินค้า ให้แจ้งเตือน Error ที่ช่องกรอกชื่อ
                if (name.isEmpty()) {
                    etProductName.setError("กรุณากรอกชื่อสินค้า");
                    return; // หยุดการทำงานชั่วคราวเพื่อให้ผู้ใช้กรอกใหม่
                }

                if (price.isEmpty()) price = "0"; // ถ้าไม่กรอกราคาให้กำหนดเป็น 0 อัตโนมัติ
                if (detail.isEmpty()) detail = "ไม่มีรายละเอียดเพิ่มเติม"; // ถ้าไม่กรอกรายละเอียดให้ใส่ข้อความสำรอง
                if (location.isEmpty()) location = "ม.ราชภัฏพิบูลสงคราม"; // ถ้าไม่กรอกสถานที่ให้ใส่ค่าเริ่มต้น

                boolean isWanted = (selectedPostTypeIndex == 3); // เช็คว่าถ้าเลือกประเภทเป็น 3 แสดงว่าเป็นโพสต์ตามหาของ

                // ดึงข้อมูลโปรไฟล์ผู้ใช้งานปัจจุบันผ่าน UserRepository
                UserProfile profile = userRepository.getUserProfile();
                String sellerName = profile != null ? profile.getName() : "ชื่อผู้ขาย";
                String sellerFacebook = profile != null ? profile.getFacebook() : "-";
                String sellerInstagram = profile != null ? profile.getInstagram() : "-";
                String sellerPhone = profile != null ? profile.getPhone() : "-";
                String sellerProfileImage = profile != null ? profile.getImageUri() : "";

                // [OOP - Object Creation] สร้างออบเจ็กต์ Product ใหม่ขึ้นมา โดยส่งข้อมูลทั้งหมดผ่าน Constructor ของคลาส Product
                Product newProduct = new Product(
                        name, "฿" + price, category, selectedImageUriStr, detail, location, selectedPostTypeIndex,
                        sellerName, sellerFacebook, sellerInstagram, sellerPhone, sellerProfileImage
                );

                // สั่งบันทึกออบเจ็กต์สินค้าผ่าน ProductRepository ลงใน SharedPreferences
                productRepository.saveProduct(newProduct, isWanted);
                loadSavedProducts(); // โหลดรายการสินค้าใหม่มาแสดงผลอัปเดตบนหน้าจอทันที

                Toast.makeText(MainActivity.this, "โพสต์ประกาศสำเร็จ! 🎉", Toast.LENGTH_SHORT).show(); // แจ้งเตือนสำเร็จ
                currentDialog.dismiss(); // ปิดหน้าต่าง Dialog โพสต์สินค้า
            });
        }

        currentDialog.show(); // สั่งแสดงหน้าต่าง Dialog ขึ้นมาบนหน้าจอ
    }

    // ฟังก์ชันอัปเดตสีพื้นหลังปุ่มประเภทโพสต์ภายใน Dialog
    private void updatePostTypeUI(LinearLayout s, LinearLayout sw, LinearLayout f, LinearLayout w) {
        if (s == null || sw == null || f == null || w == null) return;
        s.setBackgroundColor(Color.parseColor(selectedPostTypeIndex == 0 ? "#D1E7DD" : "#FFFFFF"));
        sw.setBackgroundColor(Color.parseColor(selectedPostTypeIndex == 1 ? "#E2E3E5" : "#FFFFFF"));
        f.setBackgroundColor(Color.parseColor(selectedPostTypeIndex == 2 ? "#F8D7DA" : "#FFFFFF"));
        w.setBackgroundColor(Color.parseColor(selectedPostTypeIndex == 3 ? "#E8DAEF" : "#FFFFFF"));
    }

    // ==========================================
    // ส่วนที่ 5: การจัดการรูปภาพและ Base64
    // ==========================================

    // เมธอดสำหรับรับผลลัพธ์ข้อมูลที่ส่งกลับมา (เช่น รูปภาพที่เลือกมาจากแกลเลอรี)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // เรียกฟังก์ชันคลาสแม่
        // ตรวจสอบว่าตรงกับรหัสคำขอเลือกภาพ และผู้ใช้เลือกสำเร็จจริงหรือไม่
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri sourceUri = data.getData(); // ดึงที่อยู่ Uri ของรูปภาพที่เลือก
            selectedImageUriStr = convertUriToBase64(sourceUri); // แปลงไฟล์รูปภาพ Uri ให้เป็นสตริง Base64

            // ถ้าแปลงสำเร็จและมีตัวแปร ImageView อยู่จริง ให้ถอดรหัส Base64 เป็น Bitmap แล้วนำมาแสดงตัวอย่างบนหน้าจอ
            if (!selectedImageUriStr.isEmpty() && ivPreviewImage != null && layoutPlaceholderImage != null) {
                try {
                    byte[] decodedString = Base64.decode(selectedImageUriStr, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivPreviewImage.setImageBitmap(decodedBitmap); // แสดงภาพตัวอย่าง
                    ivPreviewImage.setVisibility(View.VISIBLE); // เปิดการมองเห็น ImageView
                    layoutPlaceholderImage.setVisibility(View.GONE); // ซ่อนช่องไอคอนสำรอง
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ฟังก์ชันช่วยแปลงไฟล์รูปภาพ Uri ให้เป็นข้อความสตริง Base64 เพื่อให้สามารถบันทึกเก็บไว้ใน SharedPreferences ได้
    private String convertUriToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return "";

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            int targetSize = 400; // กำหนดขนาดเป้าหมายเพื่อย่อภาพไม่ให้ใหญ่เกินไป
            int width = options.outWidth;
            int height = options.outHeight;
            int inSampleSize = 1;

            if (width > targetSize || height > targetSize) {
                final int halfWidth = width / 2;
                final int halfHeight = height / 2;
                while ((halfWidth / inSampleSize) >= targetSize && (halfHeight / inSampleSize) >= targetSize) {
                    inSampleSize *= 2;
                }
            }

            inputStream = getContentResolver().openInputStream(uri);
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // ถ้ารูปภาพไม่เป็นค่าว่าง ให้ทำการบีบอัดเป็นไฟล์ JPEG และแปลงเป็น Base64 String
            if (bitmap != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                byte[] byteArray = outputStream.toByteArray();
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ==========================================
    // ส่วนที่ 6: การโหลดและแสดงผลรายการสินค้า (Data Rendering)
    // ==========================================

    // ฟังก์ชันโหลดรายการสินค้าจาก Repository มาแสดงผลที่หน้าจอหลัก
    private void loadSavedProducts() {
        if (containerProducts != null) containerProducts.removeAllViews(); // เคลียร์หน้าจอวิวเก่าทิ้งทั้งหมดก่อนโหลดใหม่
        currentRowLayout = null; // รีเซ็ตแถวปัจจุบันให้เป็นค่าว่าง

        // [Repository Pattern] เรียกใช้งานเมธอดจาก productRepository เพื่อดึงข้อมูลสินค้าออกมาเป็น List ของ Product Objects
        List<Product> productList = productRepository.getProducts(isWantedTab, selectedCategoryIndex, selectedPostTypeFilter);

        // ใช้คำสั่งวนลูป (Loop) ดึงข้อมูล Product ออกมาทีละชิ้น แล้วส่งไปสร้าง Card UI แสดงผล
        for (Product product : productList) {
            addProductCardUI(product);
        }
    }

    // ฟังก์ชันสร้างการ์ดแสดงผลสินค้าแต่ละชิ้นแบบไดนามิก (Dynamic UI Generation)
    private void addProductCardUI(Product product) {
        // จัดการโครงสร้างแถวแนวนอน (LinearLayout) ให้ใส่การ์ดสินค้าได้สูงสุดแถวละ 2 ชิ้น
        if (currentRowLayout == null || currentRowLayout.getChildCount() >= 2) {
            currentRowLayout = new LinearLayout(this);
            currentRowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            currentRowLayout.setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout.LayoutParams) currentRowLayout.getLayoutParams()).setMargins(0, 0, 0, 12);
            containerProducts.addView(currentRowLayout, 0); // เพิ่มแถวเข้าไปในกล่องหลัก
        }

        // นำไฟล์เลย์เอาต์ย่อย item_product มาสร้างเป็น View การ์ดสินค้า
        View cardView = getLayoutInflater().inflate(R.layout.item_product, currentRowLayout, false);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        int childIndex = currentRowLayout.getChildCount();
        cardParams.setMargins(childIndex == 0 ? 0 : 6, 0, childIndex == 0 ? 6 : 0, 0);
        cardView.setLayoutParams(cardParams);

        // ค้นหา View องค์ประกอบภายใน Card
        ImageView ivProduct = cardView.findViewById(R.id.ivProduct);
        TextView tvBadge = cardView.findViewById(R.id.tvBadge);
        TextView tvCategory = cardView.findViewById(R.id.tvCategory);
        TextView tvName = cardView.findViewById(R.id.tvName);
        TextView tvPrice = cardView.findViewById(R.id.tvPrice);

        // [OOP - Getter Method] ดึงข้อมูลรูปภาพจากออบเจ็กต์ Product ผ่าน Getter (product.getImageUri()) มาถอดรหัสแสดงผล
        if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(product.getImageUri(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bitmap != null) {
                    ivProduct.setImageBitmap(bitmap); // ใส่รูปภาพลงใน ImageView
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String badgeText = "ขาย";
        int badgeColor = Color.parseColor("#00794C");
        if (isWantedTab) {
            badgeText = "ตามหา";
            badgeColor = Color.parseColor("#6F42C1");
        } else {
            // [OOP - Getter Method] เรียกใช้ product.getPostType() เพื่อตรวจสอบประเภทโพสต์ แล้วกำหนดสีและป้ายกำกับ
            switch (product.getPostType()) {
                case 0: badgeText = "ขาย"; badgeColor = Color.parseColor("#00794C"); break;
                case 1: badgeText = "แลก"; badgeColor = Color.parseColor("#495057"); break;
                case 2: badgeText = "ให้ฟรี"; badgeColor = Color.parseColor("#842029"); break;
                case 3: badgeText = "ตามหา"; badgeColor = Color.parseColor("#6F42C1"); break;
            }
        }
        tvBadge.setText(badgeText);
        tvBadge.setBackgroundColor(badgeColor);

        // [OOP - Getter Methods] ดึงข้อมูลหมวดหมู่, ชื่อสินค้า และราคา ผ่าน Getter ของคลาส Product มาแสดงผลลงใน TextView
        tvCategory.setText(product.getCategory());
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice());

        // เมื่อผู้ใช้คลิกที่การ์ดสินค้าชิ้นนั้นๆ จะสั่งเปิดหน้าต่างดูรายละเอียด โดยส่งออบเจ็กต์ Product ติดไปกับคำสั่งด้วย
        cardView.setOnClickListener(v -> showProductDetailDialog(product));
        currentRowLayout.addView(cardView); // เพิ่มการ์ดลงในแถว
    }

    // ==========================================
    // ส่วนที่ 7: หน้าต่างรายละเอียดสินค้าและการรีวิว
    // ==========================================

    // ฟังก์ชันแสดงหน้าต่าง Dialog รายละเอียดสินค้าแบบเต็ม เมื่อผู้ใช้คลิกเลือกสินค้า
    private void showProductDetailDialog(Product product) {
        Dialog detailDialog = new Dialog(MainActivity.this); // สร้าง Dialog รายละเอียดสินค้า
        detailDialog.setContentView(R.layout.dialog_product_detail); // กำหนดเลย์เอาต์ XML

        if (detailDialog.getWindow() != null) {
            detailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.92);
            detailDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // ค้นหา View ต่างๆ ใน Dialog รายละเอียดสินค้า
        ImageView ivDetailProduct = detailDialog.findViewById(R.id.ivDetailProduct);
        ImageView ivSellerProfile = detailDialog.findViewById(R.id.ivSellerProfile);
        TextView tvDetailCategory = detailDialog.findViewById(R.id.tvDetailCategory);
        TextView tvDetailName = detailDialog.findViewById(R.id.tvDetailName);
        TextView tvDetailPrice = detailDialog.findViewById(R.id.tvDetailPrice);
        TextView tvDetailDescription = detailDialog.findViewById(R.id.tvDetailDescription);
        TextView tvDetailLocation = detailDialog.findViewById(R.id.tvDetailLocation);
        View btnCloseDetail = detailDialog.findViewById(R.id.btnClose);
        TextView btnFavorite = detailDialog.findViewById(R.id.ivFavorite);
        TextView btnRateSeller = detailDialog.findViewById(R.id.btnRateSeller);

        TextView tvDetailSellerName = detailDialog.findViewById(R.id.tvSellerName);
        TextView tvDetailFacebook = detailDialog.findViewById(R.id.tvContactFacebook);
        TextView tvDetailInstagram = detailDialog.findViewById(R.id.tvContactInstagram);
        TextView tvDetailPhone = detailDialog.findViewById(R.id.tvContactPhone);

        // [OOP - Getter Methods] ดึงข้อมูลจากออบเจ็กต์ Product มาแสดงลงในหน้าจอรายละเอียดผ่าน Getter
        if (tvDetailName != null) tvDetailName.setText(product.getName());
        if (tvDetailPrice != null) tvDetailPrice.setText(product.getPrice());
        if (tvDetailCategory != null) tvDetailCategory.setText(product.getCategory() + " · PSRU SWAP");
        if (tvDetailDescription != null) tvDetailDescription.setText(product.getDetail());
        if (tvDetailLocation != null) tvDetailLocation.setText(product.getLocation());

        // [OOP - Getter Methods] ดึงข้อมูลผู้ขาย (Seller) ผ่าน Getter
        if (tvDetailSellerName != null) tvDetailSellerName.setText(product.getSellerName());
        if (tvDetailFacebook != null) tvDetailFacebook.setText("Facebook: " + product.getSellerFacebook());
        if (tvDetailInstagram != null) tvDetailInstagram.setText("Instagram: " + product.getSellerInstagram());
        if (tvDetailPhone != null) tvDetailPhone.setText("โทร: " + product.getSellerPhone());

        // แสดงรูปภาพสินค้าหลักใน Dialog
        if (ivDetailProduct != null && product.getImageUri() != null && !product.getImageUri().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(product.getImageUri(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bitmap != null) {
                    ivDetailProduct.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // แสดงรูปโปรไฟล์ผู้ขาย
        if (ivSellerProfile != null) {
            try {
                UserProfile currentProfile = userRepository.getUserProfile();
                String profileImg = "";

                if (currentProfile != null && currentProfile.getImageUri() != null && !currentProfile.getImageUri().isEmpty()) {
                    profileImg = currentProfile.getImageUri();
                } else if (product.getSellerProfileImage() != null && !product.getSellerProfileImage().isEmpty()) {
                    profileImg = product.getSellerProfileImage();
                }

                if (!profileImg.isEmpty()) {
                    if (!profileImg.startsWith("file://") && !profileImg.startsWith("content://") && !profileImg.startsWith("http")) {
                        byte[] decodedString = Base64.decode(profileImg, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        if (bitmap != null) {
                            ivSellerProfile.setImageBitmap(bitmap);
                        }
                    } else {
                        ivSellerProfile.setImageURI(Uri.parse(profileImg));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ถ้าคลิกปุ่มให้คะแนนผู้ขาย ให้เปิด Dialog ให้คะแนนโดยส่งชื่อผู้ขายผ่าน Getter (product.getSellerName()) ไปด้วย
        if (btnRateSeller != null) {
            btnRateSeller.setOnClickListener(v -> showRatingDialog(product.getSellerName()));
        }

        // [Event-Driven] เมื่อผู้ใช้คลิกปุ่มรูปหัวใจ (บันทึกรายการโปรด Favorite)
        if (btnFavorite != null) {
            btnFavorite.setOnClickListener(v -> {
                try {
                    android.content.SharedPreferences prefs = getSharedPreferences("PSRU_FAVORITE_PREF", MODE_PRIVATE);
                    String favJson = prefs.getString("favorite_list", "[]");
                    org.json.JSONArray jsonArray = new org.json.JSONArray(favJson);
                    org.json.JSONObject newObj = new org.json.JSONObject();
                    // นำข้อมูลสินค้าจาก Getter บันทึกลง JSONArray เพื่อเก็บใน SharedPreferences
                    newObj.put("name", product.getName());
                    newObj.put("price", product.getPrice());
                    newObj.put("category", product.getCategory());
                    newObj.put("image", product.getImageUri());
                    newObj.put("detail", product.getDetail());
                    newObj.put("location", product.getLocation());
                    jsonArray.put(newObj);
                    prefs.edit().putString("favorite_list", jsonArray.toString()).apply();
                    Toast.makeText(MainActivity.this, "บันทึกเข้ารายการโปรดแล้ว ❤️", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (btnCloseDetail != null) btnCloseDetail.setOnClickListener(v -> detailDialog.dismiss()); // ปิดหน้าต่างรายละเอียด
        detailDialog.show(); // แสดง Dialog รายละเอียด
    }

    // ฟังก์ชันแสดงหน้าต่าง Dialog สำหรับให้คะแนนดาวและรีวิวผู้ขาย
    private void showRatingDialog(String sellerName) {
        Dialog ratingDialog = new Dialog(MainActivity.this);
        ratingDialog.setContentView(R.layout.dialog_rating); // กำหนดเลย์เอาต์ XML รีวิว

        if (ratingDialog.getWindow() != null) {
            ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
            ratingDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        RatingBar ratingBar = ratingDialog.findViewById(R.id.ratingBar); // ตัวเลือกระดับดาว
        EditText etReviewComment = ratingDialog.findViewById(R.id.etReviewComment); // ช่องพิมพ์คอมเมนต์
        Button btnSubmitRating = ratingDialog.findViewById(R.id.btnSubmitRating); // ปุ่มส่งรีวิว

        // เมื่อผู้ใช้กดปุ่มส่งคะแนนรีวิว
        if (btnSubmitRating != null) {
            btnSubmitRating.setOnClickListener(v -> {
                float ratingScore = ratingBar != null ? ratingBar.getRating() : 5.0f; // ดึงคะแนนดาว
                String comment = etReviewComment != null ? etReviewComment.getText().toString().trim() : ""; // ดึงข้อความคอมเมนต์

                // เรียก ProfileRepository มาบันทึกรีวิวลงระบบ
                ProfileRepository profileRepository = new ProfileRepository(MainActivity.this);
                profileRepository.addReview("ผู้ใช้งานทั่วไป", ratingScore, comment, "วันนี้");

                Toast.makeText(MainActivity.this,
                        "ให้คะแนน " + sellerName + " " + (int)ratingScore + " ดาวสำเร็จ! ⭐",
                        Toast.LENGTH_SHORT).show(); // แจ้งเตือนสำเร็จ

                ratingDialog.dismiss(); // ปิดหน้าต่าง Dialog รีวิว
            });
        }

        ratingDialog.show(); // แสดง Dialog รีวิว
    }
}