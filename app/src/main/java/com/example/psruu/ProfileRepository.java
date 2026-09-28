package com.example.psruu; // ประกาศว่าคลาสนี้อยู่ภายใต้แพ็กเกจ com.example.psruu[cite: 9]

import android.content.Context; // นำเข้าคลาส Context สำหรับจัดการสภาพแวดล้อมและดึงข้อมูล SharedPreferences[cite: 9]
import android.content.SharedPreferences; // นำเข้าคลาสจัดการจัดเก็บข้อมูลแบบ Key-Value ในเครื่อง[cite: 9]

import org.json.JSONArray; // นำเข้าคลาสจัดการชุดข้อมูล JSON แบบอาร์เรย์[cite: 9]
import org.json.JSONObject; // นำเข้าคลาสจัดการข้อมูล JSON แบบออบเจ็กต์[cite: 9]

import java.util.ArrayList; // นำเข้าโครงสร้างข้อมูลแบบ ArrayList[cite: 9]
import java.util.List; // นำเข้าอินเทอร์เฟซ List[cite: 9]

// คลาส ProfileRepository ทำหน้าที่เป็นตัวกลางในการดึงและบันทึกข้อมูลโปรไฟล์ โพสต์ และรีวิว[cite: 9]
public class ProfileRepository {

    // --- ประกาศตัวแปรค่าคงที่สำหรับกำหนดชื่อไฟล์ SharedPreferences และ Key ต่างๆ ---[cite: 9]
    private static final String PREF_NAME = "PSRU_USER_PREF"; // ชื่อไฟล์เก็บข้อมูลโปรไฟล์ผู้ใช้[cite: 9]
    private static final String KEY_USER_PROFILE = "user_profile"; // คีย์สำหรับข้อมูลโปรไฟล์[cite: 9]
    private static final String REVIEW_PREF_NAME = "PSRU_REVIEW_PREF"; // ชื่อไฟล์เก็บข้อมูลประวัติรีวิว[cite: 9]
    private static final String KEY_REVIEWS = "review_list"; // คีย์สำหรับรายการรีวิว[cite: 9]

    private Context context; // ตัวแปรเก็บ Context ของแอปพลิเคชัน[cite: 9]
    private SharedPreferences prefs; // ตัวจัดการ SharedPreferences ของโปรไฟล์[cite: 9]
    private SharedPreferences reviewPrefs; // ตัวจัดการ SharedPreferences ของรีวิว[cite: 9]
    private UserRepository userRepository; // ออบเจ็กต์สำหรับจัดการข้อมูลผู้ใช้เพิ่มเติม[cite: 9]

    // Constructor ของคลาส ทำหน้าที่กำหนดค่าเริ่มต้นเมื่อมีการเรียกใช้งาน ProfileRepository[cite: 9]
    public ProfileRepository(Context context) {
        this.context = context;
        // เชื่อมต่อ SharedPreferences สำหรับข้อมูลผู้ใช้[cite: 9]
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // เชื่อมต่อ SharedPreferences สำหรับข้อมูลรีวิว[cite: 9]
        this.reviewPrefs = context.getSharedPreferences(REVIEW_PREF_NAME, Context.MODE_PRIVATE);
        // สร้างออบเจ็กต์ UserRepository[cite: 9]
        this.userRepository = new UserRepository(context);
    }

    // ฟังก์ชันสำหรับบันทึกข้อมูลโปรไฟล์ผู้ใช้งาน[cite: 9]
    public void saveUserProfile(UserProfile profile) {
        try {
            // บันทึกข้อมูลลงใน UserRepository[cite: 9]
            userRepository.registerUser(
                    profile.getName(),
                    profile.getStudentId(),
                    "",
                    "",
                    profile.getImageUri(),
                    profile.getFacebook(),
                    profile.getInstagram(),
                    profile.getPhone()
            );

            // สร้าง JSONObject เพื่อแปลงข้อมูลโปรไฟล์ให้อยู่ในรูปข้อความ JSON[cite: 9]
            JSONObject obj = new JSONObject();
            obj.put("name", profile.getName());
            obj.put("facebook", profile.getFacebook());
            obj.put("instagram", profile.getInstagram());
            obj.put("phone", profile.getPhone());
            obj.put("imageUri", profile.getImageUri());

            // บันทึกลงใน SharedPreferences[cite: 9]
            prefs.edit().putString(KEY_USER_PROFILE, obj.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันสำหรับดึงข้อมูลโปรไฟล์ผู้ใช้งาน[cite: 9]
    public UserProfile getUserProfile() {
        if (userRepository != null) {
            UserProfile profile = userRepository.getUserProfile();
            if (profile != null && profile.getName() != null && !profile.getName().equals("ชื่อผู้ขาย")) {
                return profile;
            }
        }

        // ดึงข้อมูลจาก SharedPreferences[cite: 9]
        String jsonString = prefs.getString(KEY_USER_PROFILE, null);
        if (jsonString == null) {
            // คืนค่าโปรไฟล์เริ่มต้นหากยังไม่มีข้อมูล[cite: 9]
            return new UserProfile("นักศึกษา พบส.", "-", "-", "-", "", "");
        }

        try {
            // แปลงข้อความ JSON กลับเป็นออบเจ็กต์ UserProfile[cite: 9]
            JSONObject obj = new JSONObject(jsonString);
            String name = obj.optString("name", "นักศึกษา พบส.");
            String facebook = obj.optString("facebook", "-");
            String instagram = obj.optString("instagram", "-");
            String phone = obj.optString("phone", "-");
            String imageUri = obj.optString("imageUri", "");

            return new UserProfile(name, facebook, instagram, phone, imageUri, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserProfile("นักศึกษา พบส.", "-", "-", "-", "", "");
    }

    // ฟังก์ชันสำหรับดึงรายการโพสต์ทั้งหมดของผู้ใช้งาน (ทั้งโพสต์ขายสินค้าและโพสต์ตามหาของ)[cite: 9]
    public List<PostItem> getAllMyPosts() {
        List<PostItem> postItems = new ArrayList<>();
        ProductRepository productRepo = new ProductRepository(context);

        // 1. ดึงโพสต์จากตลาดสินค้าปกติ (isWanted = false)[cite: 9]
        List<Product> normalProducts = productRepo.getProducts(false, 0, 0);
        for (int i = 0; i < normalProducts.size(); i++) {
            Product p = normalProducts.get(i);
            try {
                PostItem item = new PostItem(
                        p.getName(),
                        p.getPrice(),
                        p.getCategory(),
                        p.getImageUri(),
                        "KEY_PRODUCTS", // ใช้ระบุ sourceBoard ว่ามาจากตลาดสินค้าปกติ[cite: 9]
                        i             // ระบุตำแหน่ง Index เดิม[cite: 9]
                );
                postItems.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2. ดึงโพสต์จากบอร์ดตามหาของ (isWanted = true) มารวมกัน[cite: 9]
        List<Product> wantedProducts = productRepo.getProducts(true, 0, 0);
        for (int i = 0; i < wantedProducts.size(); i++) {
            Product p = wantedProducts.get(i);
            try {
                PostItem item = new PostItem(
                        p.getName(),
                        p.getPrice(),
                        p.getCategory(),
                        p.getImageUri(),
                        "KEY_WANTED", // ใช้ระบุ sourceBoard ว่ามาจากบอร์ดตามหาของ[cite: 9]
                        i             // ระบุตำแหน่ง Index เดิม[cite: 9]
                );
                postItems.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return postItems; // คืนค่ารายการโพสต์ทั้งหมด[cite: 9]
    }

    // ฟังก์ชันสำหรับลบโพสต์ตามประเภทบอร์ดและตำแหน่ง Index[cite: 9]
    public void deletePost(String sourceBoard, int index) {
        try {
            ProductRepository productRepo = new ProductRepository(context);
            // ตรวจสอบว่าเป็นบอร์ดตามหาของหรือไม่[cite: 9]
            boolean isWanted = "KEY_WANTED".equals(sourceBoard);
            productRepo.deleteProduct(isWanted, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันสำหรับเพิ่มรีวิวใหม่[cite: 9]
    public void addReview(String reviewerName, float rating, String comment, String date) {
        try {
            UserProfile currentProfile = getUserProfile();
            String actualName = (currentProfile != null && currentProfile.getName() != null && !currentProfile.getName().isEmpty())
                    ? currentProfile.getName() : "ผู้ใช้งาน";

            // ดึงรายการรีวิวเดิมในรูป JSONArray[cite: 9]
            JSONArray jsonArray = new JSONArray(reviewPrefs.getString(KEY_REVIEWS, "[]"));
            JSONObject obj = new JSONObject();
            obj.put("reviewerName", actualName);
            obj.put("rating", rating);
            obj.put("comment", comment);
            obj.put("date", date);

            // นำรีวิวใหม่มาไว้ด้านบนสุดของรายการ[cite: 9]
            JSONArray newArray = new JSONArray();
            newArray.put(obj);
            for (int i = 0; i < jsonArray.length(); i++) {
                newArray.put(jsonArray.getJSONObject(i));
            }
            // บันทึกลง SharedPreferences[cite: 9]
            reviewPrefs.edit().putString(KEY_REVIEWS, newArray.toString()).apply();

            // คำนวณและบันทึกคะแนนเฉลี่ยใหม่ทันที[cite: 9]
            List<ReviewItem> updatedList = getReviewList();
            if (!updatedList.isEmpty()) {
                float total = 0;
                for (ReviewItem item : updatedList) {
                    try {
                        total += Float.parseFloat(item.getRating());
                    } catch (Exception ignored) {}
                }
                float newAvg = total / updatedList.size();
                userRepository.saveAverageRating(Math.round(newAvg * 10.0f) / 10.0f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันนับจำนวนรีวิวทั้งหมด[cite: 9]
    public int getReviewCount() {
        return getReviewList().size();
    }

    // ฟังก์ชันดึงรายการรีวิวทั้งหมดแปลงเป็น List ของ ReviewItem[cite: 9]
    public List<ReviewItem> getReviewList() {
        List<ReviewItem> list = new ArrayList<>();
        String jsonString = reviewPrefs.getString(KEY_REVIEWS, "[]");

        UserProfile currentProfile = getUserProfile();
        String currentRealName = (currentProfile != null && currentProfile.getName() != null && !currentProfile.getName().equals("ชื่อผู้ขาย"))
                ? currentProfile.getName() : "นักศึกษา พบส.";

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String name = currentRealName;
                String savedName = obj.optString("reviewerName", "");
                if (!savedName.isEmpty() && !savedName.equals("ผู้ใช้งานทั่วไป") && !savedName.equals("นักศึกษา พบส.") && !savedName.equals("ชื่อผู้ขาย")) {
                    name = savedName;
                }

                float ratingFloat = (float) obj.optDouble("rating", 5.0);
                String ratingStr = String.valueOf(ratingFloat);
                String comment = obj.optString("comment", "");
                String date = obj.optString("date", "วันนี้");

                list.add(new ReviewItem(name, ratingStr, comment, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list; // คืนค่ารายการรีวิว[cite: 9]
    }
}