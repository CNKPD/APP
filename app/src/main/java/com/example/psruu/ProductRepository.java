package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu[cite: 9]

import android.content.Context; // นำเข้าคลาส Context สำหรับเข้าถึงระบบภายในแอปพลิเคชัน[cite: 9]
import android.content.SharedPreferences; // นำเข้าคลาสสำหรับจัดเก็บข้อมูลแบบคู่คีย์-วาลู (Key-Value) ในเครื่องอย่างถาวร[cite: 9]

import org.json.JSONArray; // นำเข้าคลาสจัดการชุดข้อมูลอาเรย์ในรูปแบบ JSON[cite: 9]
import org.json.JSONObject; // นำเข้าคลาสจัดการข้อมูลโครงสร้าง JSON แบบระบุคีย์[cite: 9]

import java.util.ArrayList; // นำเข้าโครงสร้างข้อมูลแบบอาเรย์ลิสต์ (ArrayList)[cite: 9]
import java.util.List; // นำเข้าอินเทอร์เฟซ List ของ Java[cite: 9]

// ประกาศคลาส ProductRepository ทำหน้าที่เป็นตัวจัดการชั้นข้อมูล (Repository Pattern) สำหรับจัดการข้อมูลสินค้าและรายการต้องการซื้อ
public class ProductRepository {

    // --- ประกาศตัวแปรคงที่ (Constants) สำหรับระบุชื่อไฟล์และคีย์ข้อมูลใน SharedPreferences ---[cite: 9]
    private static final String PREF_NAME = "PSRU_PRODUCTS_PREF"; // ชื่อไฟล์ SharedPreferences สำหรับเก็บข้อมูลสินค้า[cite: 9]
    private static final String KEY_PRODUCTS = "product_list";   // คีย์สำหรับเก็บรายการสินค้าทั่วไป[cite: 9]
    private static final String KEY_WANTED = "wanted_list";     // คีย์สำหรับเก็บรายการสินค้าที่ต้องการ (Wanted)[cite: 9]

    private SharedPreferences prefs; // ประกาศตัวแปรสำหรับใช้งาน SharedPreferences[cite: 9]

    // คอนสตรัคเตอร์ (Constructor) ทำงานเมื่อมีการสร้างออบเจ็กต์ ProductRepository ขึ้นมา[cite: 9]
    public ProductRepository(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // เปิดใช้งาน SharedPreferences ในโหมดส่วนตัว[cite: 9]
    }

    // เมธอดสำหรับบันทึกข้อมูลสินค้าใหม่ลงในระบบ[cite: 9]
    public void saveProduct(Product product, boolean isWanted) {
        // ดึงรายการสินค้าปัจจุบันออกมาก่อน (โดยไม่กรอง เพื่อให้ได้ข้อมูลทั้งหมด)[cite: 9]
        List<Product> currentList = getProducts(isWanted, 0, 0);
        currentList.add(0, product); // เพิ่มสินค้าใหม่เข้าไปที่ตำแหน่งแรกสุดของรายการ[cite: 9]
        saveListToPrefs(currentList, isWanted); // บันทึกรายการทั้งหมดลงใน SharedPreferences[cite: 9]
    }

    // เมธอดส่วนตัวสำหรับแปลง List ของ Product ให้เป็น JSON String และบันทึกลง SharedPreferences[cite: 9]
    private void saveListToPrefs(List<Product> list, boolean isWanted) {
        JSONArray jsonArray = new JSONArray(); // สร้าง JSONArray เปล่าสำหรับเก็บข้อมูลสินค้าทั้งหมด[cite: 9]
        for (Product p : list) {
            try {
                JSONObject obj = new JSONObject(); // สร้าง JSONObject ของสินค้าแต่ละชิ้น[cite: 9]
                // ใส่ข้อมูลคุณลักษณะต่างๆ ของสินค้าลงใน JSONObject[cite: 9]
                obj.put("name", p.getName());
                obj.put("price", p.getPrice());
                obj.put("category", p.getCategory());
                obj.put("imageUri", p.getImageUri());
                obj.put("detail", p.getDetail());
                obj.put("location", p.getLocation());
                obj.put("postType", p.getPostType());

                // ใส่ข้อมูลรายละเอียดผู้ขายลงใน JSONObject[cite: 9]
                obj.put("sellerName", p.getSellerName());
                obj.put("sellerFacebook", p.getSellerFacebook());
                obj.put("sellerInstagram", p.getSellerInstagram());
                obj.put("sellerPhone", p.getSellerPhone());
                obj.put("sellerProfileImage", p.getSellerProfileImage());

                jsonArray.put(obj); // เพิ่ม JSONObject ลงใน JSONArray[cite: 9]
            } catch (Exception e) {
                e.printStackTrace(); // ดักจับข้อผิดพลาด[cite: 9]
            }
        }
        // เลือกว่าจะบันทึกลงคีย์สินค้าปกติหรือคีย์รายการต้องการซื้อ (Wanted) ตามเงื่อนไข isWanted[cite: 9]
        String key = isWanted ? KEY_WANTED : KEY_PRODUCTS;
        prefs.edit().putString(key, jsonArray.toString()).apply(); // บันทึกข้อมูลลง SharedPreferences[cite: 9]
    }

    // เมธอดสำหรับดึงรายการสินค้า พร้อมรองรับระบบกรองตามหมวดหมู่และประเภทโพสต์[cite: 9]
    public List<Product> getProducts(boolean isWanted, int categoryFilter, int postTypeFilter) {
        List<Product> list = new ArrayList<>(); // สร้าง List สำหรับเก็บผลลัพธ์[cite: 9]
        String key = isWanted ? KEY_WANTED : KEY_PRODUCTS; // เลือกว่าจะดึงจากคีย์ไหน[cite: 9]
        String jsonString = prefs.getString(key, "[]"); // ดึงข้อมูล JSON String ออกมา (ถ้าไม่มีให้คืนค่าเป็น "[]")[cite: 9]

        try {
            JSONArray jsonArray = new JSONArray(jsonString); // แปลง JSON String เป็น JSONArray[cite: 9]
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i); // ดึงข้อมูลสินค้าทีละตัว[cite: 9]
                // ดึงค่าต่างๆ ออกจาก JSONObject พร้อมกำหนดค่าสำรอง (Default)[cite: 9]
                String name = obj.optString("name", "");
                String price = obj.optString("price", "");
                String category = obj.optString("category", "");
                String imageUri = obj.optString("imageUri", "");
                String detail = obj.optString("detail", "");
                String location = obj.optString("location", "");
                int postType = obj.optInt("postType", 0);

                String sellerName = obj.optString("sellerName", "ชื่อผู้ขาย");
                String sellerFacebook = obj.optString("sellerFacebook", "-");
                String sellerInstagram = obj.optString("sellerInstagram", "-");
                String sellerPhone = obj.optString("sellerPhone", "-");
                String sellerProfileImage = obj.optString("sellerProfileImage", "");

                // 1. เงื่อนไขกรองตามหมวดหมู่ (Category Filter)[cite: 9]
                if (categoryFilter > 0) {
                    String targetCat = "";
                    if (categoryFilter == 1) targetCat = "หนังสือเรียน";
                    else if (categoryFilter == 2) targetCat = "อุปกรณ์เรียน";
                    else if (categoryFilter == 3) targetCat = "ไอที";
                    else if (categoryFilter == 4) targetCat = "เสื้อผ้า";

                    // ถ้าหมวดหมู่ไม่ตรงกับที่กรอง ให้ข้ามรายการนี้ไป[cite: 9]
                    if (!category.contains(targetCat)) {
                        continue;
                    }
                }

                // 2. เงื่อนไขกรองตามประเภทประกาศย่อย (ขาย, แลก, ให้ฟรี) เฉพาะในหน้าตลาดสินค้า[cite: 9]
                if (!isWanted && postTypeFilter > 0) {
                    int targetPostType = postTypeFilter - 1; // แปลงค่าฟิลเตอร์เป็นค่า postType (1->0 ขาย, 2->1 แลก, 3->2 ให้ฟรี)[cite: 9]
                    // ถ้าประเภทโพสต์ไม่ตรง ให้ข้ามรายการนี้ไป[cite: 9]
                    if (postType != targetPostType) {
                        continue;
                    }
                }

                // สร้างออบเจ็กต์ Product จากข้อมูลที่ดึงมา[cite: 9]
                Product p = new Product(
                        name, price, category, imageUri, detail, location, postType,
                        sellerName, sellerFacebook, sellerInstagram, sellerPhone,
                        sellerProfileImage
                );
                list.add(p); // เพิ่มสินค้าลงใน List ผลลัพธ์[cite: 9]
            }
        } catch (Exception e) {
            e.printStackTrace(); // ดักจับข้อผิดพลาด[cite: 9]
        }
        return list; // ส่งคืนรายการสินค้าทั้งหมดที่ผ่านการกรองแล้ว[cite: 9]
    }

    // เมธอดสำหรับลบสินค้าออกจากระบบตามตำแหน่ง index ที่ระบุ[cite: 9]
    public void deleteProduct(boolean isWanted, int index) {
        try {
            // ดึงรายการปัจจุบันขึ้นมาทั้งหมดก่อน (ไม่ใช้ตัวกรอง เพื่อให้ index ตรงกับข้อมูลจริงใน SharedPreferences)[cite: 9]
            List<Product> currentList = getProducts(isWanted, 0, 0);

            // ตรวจสอบความถูกต้องของ Index ว่าอยู่ในช่วงที่มีอยู่จริงหรือไม่[cite: 9]
            if (index >= 0 && index < currentList.size()) {
                currentList.remove(index); // ลบรายการออกจาก List[cite: 9]
                saveListToPrefs(currentList, isWanted); // บันทึกรายการใหม่ลง SharedPreferences ทันที[cite: 9]
            }
        } catch (Exception e) {
            e.printStackTrace(); // ดักจับข้อผิดพลาด[cite: 9]
        }
    }
}