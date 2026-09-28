package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu

import android.app.AlertDialog; // นำเข้าคลาส AlertDialog สำหรับสร้างหน้าต่างแจ้งเตือนยืนยัน (เช่น กดยืนยันลบรายการโปรด)
import android.content.Intent; // นำเข้าคลาส Intent สำหรับสั่งเปลี่ยนหน้าจอไปยัง Activity อื่น
import android.graphics.Bitmap; // นำเข้าคลาส Bitmap สำหรับจัดการโครงสร้างข้อมูลภาพในหน่วยความจำ
import android.graphics.BitmapFactory; // นำเข้าคลาสแปลงข้อมูลไบต์สตรีมให้เป็นภาพ Bitmap
import android.graphics.Color; // นำเข้าคลาสสำหรับกำหนดชุดสีต่างๆ
import android.os.Bundle; // นำเข้าคลาส Bundle สำหรับส่งผ่านข้อมูลสถานะของ Activity ตอนเริ่มต้น
import android.util.Base64; // นำเข้าคลาสสำหรับถอดรหัสข้อมูลภาพจากสตริง Base64
import android.widget.ImageView; // นำเข้าคลาสแสดงรูปภาพ
import android.widget.LinearLayout; // นำเข้าคลาสจัดเรียงเลย์เอาต์แนวตั้งหรือแนวนอน
import android.widget.TextView; // นำเข้าคลาสแสดงข้อความ
import android.widget.Toast; // นำเข้าคลาสแสดงข้อความแจ้งเตือนขนาดเล็กชั่วคราว

import androidx.appcompat.app.AppCompatActivity; // นำเข้าคลาสแม่ข่ายสำหรับการสร้างหน้าจอแอปพลิเคชัน

import java.util.List; // นำเข้าโครงสร้างข้อมูลแบบรายการ (List)

// [OOP - Inheritance] ประกาศคลาส FavoriteActivity โดยสืบทอดความสามารถมาจาก AppCompatActivity เพื่อให้ทำหน้าที่เป็นหน้าจอหลักของรายการโปรด
public class FavoriteActivity extends AppCompatActivity {

    // --- ประกาศตัวแปร (Attributes) สำหรับอ้างอิงองค์ประกอบ UI และชั้นข้อมูล (Repository) ---
    private LinearLayout containerFavoriteProducts; // ตัวแปรกล่องพื้นที่สำหรับแสดงรายการสินค้าโปรดทั้งหมดแบบไดนามิก
    private FavoriteRepository favoriteRepository; // ตัวแปรจัดการข้อมูลรายการโปรด (อ่าน/เขียน/ลบข้อมูลจาก SharedPreferences)

    // ==========================================
    // ส่วนที่ 1: วงจรชีวิตของหน้าจอ (Activity Lifecycle)
    // ==========================================

    // เมธอด onCreate จะทำงาน "ทันที" เมื่อผู้ใช้เปิดเข้ามาที่หน้ารายการโปรดนี้เป็นครั้งแรก
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // เรียกฟังก์ชันตั้งต้นของคลาสแม่
        setContentView(R.layout.activity_favorite); // สั่งให้หน้าจอนี้แสดงผลหน้าตาตามไฟล์ XML เลย์เอาต์ activity_favorite

        // เชื่อมโยงตัวแปร containerFavoriteProducts เข้ากับ ID ในไฟล์ XML
        containerFavoriteProducts = findViewById(R.id.containerFavoriteProducts);

        // สร้างออบเจ็กต์ (Instantiation) ของ FavoriteRepository โดยส่ง context (this) เข้าไป
        favoriteRepository = new FavoriteRepository(this);

        setupBottomNavigation(); // เรียกฟังก์ชันตั้งค่าแถบเมนูด้านล่างหน้าจอ
        loadFavoriteProducts(); // เรียกฟังก์ชันโหลดข้อมูลรายการโปรดขึ้นมาแสดงผล
    }

    // เมธอด onResume จะทำงานทุกครั้งที่หน้าจอนี้ "กลับมาแสดงผลต่อหน้าผู้ใช้อีกครั้ง"
    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteProducts(); // สั่งโหลดข้อมูลรายการโปรดใหม่ทันที เพื่อให้หน้าจออัปเดตตรงกับความเป็นจริงเสมอ
    }

    // ==========================================
    // ส่วนที่ 2: ระบบนำทางด้านล่าง (Bottom Navigation)
    // ==========================================

    // ฟังก์ชันจัดการการคลิกเมนูด้านล่างเพื่อสลับไปยังหน้าจออื่นๆ
    private void setupBottomNavigation() {
        LinearLayout navMarket = findViewById(R.id.navMarket); // ปุ่มไปหน้าตลาดสินค้าหลัก
        LinearLayout navFavorite = findViewById(R.id.navFavorite); // ปุ่มหน้าโปรด (หน้าปัจจุบัน)
        LinearLayout navProfile = findViewById(R.id.navProfile); // ปุ่มไปหน้าโปรไฟล์ส่วนตัว

        // ถ้าคลิกปุ่มไปหน้าตลาดสินค้า (MainActivity)
        if (navMarket != null) {
            navMarket.setOnClickListener(v -> {
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                // ตั้งค่า Flag เพื่อเคลียร์ Activity เก่าด้านบน ไม่ให้ซ้อนทับกันหลายชั้น
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // ปิดหน้าจอปัจจุบัน
            });
        }

        // ถ้าคลิกปุ่มหน้าโปรด (เนื่องจากอยู่หน้านี้อยู่แล้ว จึงเว้นว่างไว้ไม่ต้องทำอะไร)
        if (navFavorite != null) {
            navFavorite.setOnClickListener(v -> {
                // อยู่หน้าโปรดอยู่แล้ว
            });
        }

        // ถ้าคลิกปุ่มไปหน้าโปรไฟล์ (ProfileActivity)
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Intent intent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                startActivity(intent); // เปิดหน้าโปรไฟล์
            });
        }
    }

    // ==========================================
    // ส่วนที่ 3: การโหลดและจัดการข้อมูลรายการโปรด (Data & Rendering)
    // ==========================================

    // ฟังก์ชันสำหรับดึงข้อมูลรายการสินค้าโปรดทั้งหมดมาแสดงผล
    private void loadFavoriteProducts() {
        // ถ้ากล่องแสดงรายการไม่เป็นค่าว่าง ให้ล้างวิวเก่าทั้งหมดทิ้งก่อนเพื่อป้องกันข้อมูลซ้ำซ้อน
        if (containerFavoriteProducts != null) {
            containerFavoriteProducts.removeAllViews();
        }

        // [Repository Pattern] ดึงรายการสินค้าโปรดออกมาเป็น List ของ Product Objects ผ่าน FavoriteRepository
        List<Product> productList = favoriteRepository.getFavoriteProducts();

        // ตรวจสอบเงื่อนไข: ถ้าหากรายการโปรดว่างเปล่า (ไม่มีข้อมูล)
        if (productList.isEmpty()) {
            renderEmptyView(); // เรียกฟังก์ชันแสดงข้อความว่ายังไม่มีรายการโปรด
            return; // หยุดการทำงานชั่วคราว
        }

        // วนลูป (Loop) ดึงสินค้าโปรดออกมาทีละชิ้น พร้อมส่งค่าตำแหน่ง index ไปด้วย
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            final int index = i;
            renderProductCard(product, index); // เรียกฟังก์ชันสร้างการ์ดแสดงผลสินค้าโปรดทีละชิ้น
        }
    }

    // ฟังก์ชันสร้างหน้าจอแสดงข้อความกรณีที่ยังไม่มีรายการโปรดถูกบันทึกไว้
    private void renderEmptyView() {
        TextView tvEmpty = new TextView(this);
        tvEmpty.setText("ยังไม่มีรายการโปรดที่บันทึกไว้ 📭"); // ข้อความแจ้งเตือน
        tvEmpty.setTextColor(Color.parseColor("#6C757D")); // สีตัวหนังสือสีเทา
        tvEmpty.setTextSize(14); // ขนาดตัวอักษร
        tvEmpty.setPadding(16, 16, 16, 16); // ระยะห่างขอบรอบด้าน
        containerFavoriteProducts.addView(tvEmpty); // เพิ่มข้อความลงในกล่องหลัก
    }

    // ==========================================
    // ส่วนที่ 4: การสร้าง UI การ์ดสินค้าโปรดแบบไดนามิก (Dynamic UI)
    // ==========================================

    // ฟังก์ชันสร้างการ์ดแสดงผลสินค้าโปรดแต่ละรายการ พร้อมผูกระบบคลิกเพื่อลบ
    private void renderProductCard(Product product, int index) {
        LinearLayout cardLayout = createCardLayout(); // สร้างเลย์เอาต์กรอบการ์ดหลัก

        // [Event-Driven] เมื่อผู้ใช้คลิกที่การ์ดสินค้ารายการโปรดชิ้นนั้นๆ
        cardLayout.setOnClickListener(v -> {
            // สร้างหน้าต่างแจ้งเตือน (AlertDialog) ถามยืนยันว่าต้องการลบออกจากรายการโปรดหรือไม่
            new AlertDialog.Builder(FavoriteActivity.this)
                    .setTitle("จัดการรายการโปรด")
                    .setMessage("คุณต้องการลบ \"" + product.getName() + "\" ออกจากรายการโปรดใช่หรือไม่?")
                    .setPositiveButton("ลบออก", (dialog, which) -> {
                        // [Repository Pattern] สั่งลบข้อมูลสินค้าออกจาก SharedPreferences ตามตำแหน่ง index
                        favoriteRepository.removeFavoriteItem(index);
                        loadFavoriteProducts(); // โหลดรายการโปรดใหม่ทันเพื่ออัปเดตหน้าจอ
                        Toast.makeText(this, "ลบออกจากรายการโปรดแล้ว", Toast.LENGTH_SHORT).show(); // แจ้งเตือนสำเร็จ
                    })
                    .setNegativeButton("ยกเลิก", null) // ปุ่มยกเลิกไม่ต้องทำอะไร
                    .show(); // แสดง Dialog ขึ้นมา
        });

        // สร้างรูปภาพสินค้าและกล่องข้อความรายละเอียดสินค้า
        ImageView ivProduct = createProductImageView(product.getImageUri());
        LinearLayout infoLayout = createProductInfoLayout(product);

        // ประกอบร่าง: นำรูปภาพและกล่องข้อมูลใส่ลงไปในการ์ด แล้วนำการ์ดใส่ลงในกล่องแสดงผลหลัก
        cardLayout.addView(ivProduct);
        cardLayout.addView(infoLayout);
        containerFavoriteProducts.addView(cardLayout);
    }

    // ฟังก์ชันสร้างเลย์เอาต์กรอบการ์ดสินค้าโปรด (Card Container)
    private LinearLayout createCardLayout() {
        LinearLayout cardLayout = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 12); // กำหนดระยะห่างด้านล่างของการ์ดแต่ละใบ
        cardLayout.setLayoutParams(cardParams);
        cardLayout.setOrientation(LinearLayout.HORIZONTAL); // จัดเรียงองค์ประกอบภายในแบบแนวนอน (ซ้าย-ขวา)
        cardLayout.setBackgroundColor(Color.WHITE); // พื้นหลังการ์ดสีขาว
        cardLayout.setPadding(12, 12, 12, 12); // ระยะห่างภายในขอบการ์ด
        cardLayout.setElevation(2f); // ใส่เงาให้การ์ดดูมีมิติ
        cardLayout.setClickable(true); // เปิดให้สามารถคลิกได้
        cardLayout.setFocusable(true); // เปิดให้รองรับการโฟกัส
        return cardLayout;
    }

    // ฟังก์ชันสร้างและจัดการรูปภาพสินค้าในการ์ดรายการโปรด
    private ImageView createProductImageView(String imageUriStr) {
        ImageView ivProduct = new ImageView(this);
        int imgSizePx = (int) (80 * getResources().getDisplayMetrics().density); // แปลงหน่วย dp เป็น pixels (ขนาด 80x80 dp)
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(imgSizePx, imgSizePx);
        ivProduct.setLayoutParams(imgParams);
        ivProduct.setScaleType(ImageView.ScaleType.CENTER_CROP); // ตัดรูปให้เต็มกรอบสวยงาม
        ivProduct.setBackgroundColor(Color.parseColor("#CED4DA")); // สีพื้นหลังสำรองตอนยังไม่มีรูป

        // ตรวจสอบและถอดรหัสสตริง Base64 เป็น Bitmap เพื่อแสดงรูปภาพสินค้าจริง
        if (imageUriStr != null && !imageUriStr.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(imageUriStr, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bitmap != null) {
                    ivProduct.setImageBitmap(bitmap); // แสดงรูปภาพลงใน ImageView
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ivProduct;
    }

    // ฟังก์ชันสร้างกล่องข้อความรายละเอียดสินค้าภายในรายการโปรด (หมวดหมู่, ชื่อสินค้า, ราคา)
    private LinearLayout createProductInfoLayout(Product product) {
        LinearLayout infoLayout = new LinearLayout(this);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        infoParams.setMargins(12, 0, 0, 0); // ระยะห่างจากรูปภาพด้านซ้าย
        infoLayout.setLayoutParams(infoParams);
        infoLayout.setOrientation(LinearLayout.VERTICAL); // จัดเรียงข้อความแนวตั้ง (บนลงล่าง)

        // สร้าง TextView แสดงหมวดหมู่สินค้า[cite: 4]
        TextView tvCategory = new TextView(this);
        tvCategory.setText(product.getCategory() + " · PSRU SWAP");
        tvCategory.setTextColor(Color.parseColor("#00794C"));
        tvCategory.setTextSize(10);

        // สร้าง TextView แสดงชื่อสินค้า[cite: 4]
        TextView tvName = new TextView(this);
        tvName.setText(product.getName());
        tvName.setTextColor(Color.parseColor("#212529"));
        tvName.setTextSize(14);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD); // ตัวหนา

        // สร้าง TextView แสดงราคาสินค้า[cite: 4]
        TextView tvPrice = new TextView(this);
        tvPrice.setText(product.getPrice());
        tvPrice.setTextColor(Color.parseColor("#00794C"));
        tvPrice.setTextSize(13);
        tvPrice.setTypeface(null, android.graphics.Typeface.BOLD); // ตัวหนา

        // เพิ่มข้อความทั้งหมดลงในเลย์เอาต์ข้อมูลสินค้า[cite: 4]
        infoLayout.addView(tvCategory);
        infoLayout.addView(tvName);
        infoLayout.addView(tvPrice);

        return infoLayout; // ส่งคืนเลย์เอาต์ข้อมูลที่ประกอบเสร็จแล้ว
    }
}