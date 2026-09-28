package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu

import android.content.Context; // นำเข้าคลาส Context สำหรับเข้าถึงระบบภายในแอปพลิเคชัน
import android.content.SharedPreferences; // นำเข้าคลาสสำหรับจัดเก็บข้อมูลแบบคู่คีย์-วาลู (Key-Value) ในเครื่องอย่างถาวร

import org.json.JSONArray; // นำเข้าคลาสจัดการชุดข้อมูลอาเรย์ในรูปแบบ JSON
import org.json.JSONObject; // นำเข้าคลาสจัดการข้อมูลโครงสร้าง JSON แบบระบุคีย์

import java.util.ArrayList; // นำเข้าโครงสร้างข้อมูลแบบอาเรย์ลิสต์ (ArrayList)
import java.util.List; // นำเข้าอินเทอร์เฟซ List ของ Java

// ประกาศคลาส FavoriteRepository ทำหน้าที่เป็นตัวจัดการชั้นข้อมูล (Repository Pattern) เฉพาะสำหรับรายการโปรด
public class FavoriteRepository {
    private SharedPreferences prefs; // ประกาศตัวแปรสำหรับใช้งาน SharedPreferences

    // คอนสตรัคเตอร์ (Constructor) ทำงานเมื่อมีการสร้างออบเจ็กต์ FavoriteRepository ขึ้นมา
    public FavoriteRepository(Context context) {
        // เปิดใช้งาน SharedPreferences โดยกำหนดชื่อไฟล์ฐานข้อมูลจำลองว่า "PSRU_FAVORITE_PREF" ในโหมดส่วนตัว (MODE_PRIVATE)
        prefs = context.getSharedPreferences("PSRU_FAVORITE_PREF", Context.MODE_PRIVATE);
    }

    // ฟังก์ชันสำหรับดึงรายการสินค้าโปรดทั้งหมดที่ถูกบันทึกไว้ออกมาเป็น List ของ Product Objects[cite: 5]
    public List<Product> getFavoriteProducts() {
        List<Product> productList = new ArrayList<>(); // สร้างอาเรย์ลิสต์เปล่าสำหรับเก็บรายการสินค้าโปรด[cite: 5]

        // ดึงข้อมูลสตริง JSON จาก SharedPreferences (ถ้าไม่มีให้คืนค่าเริ่มต้นเป็นอาเรย์ว่าง "[]")[cite: 5]
        String favJson = prefs.getString("favorite_list", "[]");

        try {
            // แปลงสตริง JSON ให้กลายเป็น JSONArray เพื่อเตรียมวนลูปอ่านข้อมูล[cite: 5]
            JSONArray jsonArray = new JSONArray(favJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i); // ดึงข้อมูล JSONObject ทีละตัวตามลำดับดัชนี (index)[cite: 5]
                if (obj == null) continue; // ถ้าข้อมูลเป็นค่าว่าง ข้ามรอบนี้ไป[cite: 5]

                // [OOP - Object Creation] สร้างออบเจ็กต์ Product ขึ้นมาใหม่จากข้อมูล JSON ที่ดึงมา โดยส่งค่าพารามิเตอร์ครบทั้ง 12 ตัว[cite: 5]
                Product product = new Product(
                        obj.optString("name"), // ดึงชื่อสินค้า[cite: 5]
                        obj.optString("price"), // ดึงราคาสินค้า[cite: 5]
                        obj.optString("category"), // ดึงหมวดหมู่สินค้า[cite: 5]
                        obj.optString("image"), // ดึงสตริง Base64 ของรูปภาพสินค้า[cite: 5]
                        obj.optString("detail"), // ดึงรายละเอียดสินค้า[cite: 5]
                        obj.optString("location"), // ดึงสถานที่นัดรับสินค้า[cite: 5]
                        obj.optInt("postType", 0), // ดึงประเภทโพสต์ (ค่าเริ่มต้นเป็น 0)[cite: 5]
                        obj.optString("sellerName", "ชื่อผู้ขาย"), // ดึงชื่อผู้ขาย[cite: 5]
                        obj.optString("sellerFacebook", "-"), // ดึงเฟซบุ๊กผู้ขาย[cite: 5]
                        obj.optString("sellerInstagram", "-"), // ดึงอินสตาแกรมผู้ขาย[cite: 5]
                        obj.optString("sellerPhone", "-"), // ดึงเบอร์โทรผู้ขาย[cite: 5]
                        obj.optString("sellerProfileImage", "") // ดึงรูปโปรไฟล์ผู้ขาย (พารามิเตอร์ตัวที่ 12)[cite: 5]
                );
                productList.add(product); // เพิ่มออบเจ็กต์ Product ลงใน List[cite: 5]
            }
        } catch (Exception e) {
            e.printStackTrace(); // ดักจับข้อผิดพลาดหากเกิดปัญหาในการแปลงข้อมูล JSON
        }

        return productList; // ส่งคืนรายการสินค้าโปรดทั้งหมดกลับไปใช้งาน[cite: 5]
    }

    // ฟังก์ชันสำหรับลบรายการโปรดออกจากระบบตามตำแหน่ง index ที่ผู้ใช้เลือก[cite: 5]
    public void removeFavoriteItem(int index) {
        String favJson = prefs.getString("favorite_list", "[]"); // ดึงข้อมูลรายการโปรดเดิมทั้งหมดขึ้นมา[cite: 5]
        try {
            JSONArray jsonArray = new JSONArray(favJson); // แปลงเป็น JSONArray[cite: 5]
            JSONArray newArray = new JSONArray(); // สร้าง JSONArray ตัวใหม่สำหรับเก็บข้อมูลที่เหลืออยู่หลังลบ[cite: 5]

            // วนลูปตรวจสอบข้อมูลทั้งหมด[cite: 5]
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i != index) { // ถ้าตำแหน่ง index ไม่ตรงกับตัวที่จะลบ[cite: 5]
                    newArray.put(jsonArray.get(i)); // นำข้อมูลเก็บใส่ลงใน JSONArray ตัวใหม่[cite: 5]
                }
            }

            // บันทึก JSONArray ตัวใหม่ (ที่ลบรายการออกแล้ว) กลับลงไปใน SharedPreferences[cite: 5]
            prefs.edit().putString("favorite_list", newArray.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace(); // ดักจับข้อผิดพลาด
        }
    }
}