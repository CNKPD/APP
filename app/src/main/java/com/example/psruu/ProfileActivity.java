package com.example.psruu; // ประกาศว่าไฟล์นี้อยู่ในแพ็กเกจ (Package) ชื่อ com.example.psruu

import android.app.AlertDialog; // นำเข้าคลาส AlertDialog สำหรับสร้างหน้าต่างแจ้งเตือนหรือแสดงประวัติรีวิว
import android.content.Context; // นำเข้าคลาส Context สำหรับเข้าถึงระบบภายในแอปพลิเคชัน
import android.content.Intent; // นำเข้าคลาส Intent สำหรับสั่งเปลี่ยนหน้าจอไปยัง Activity อื่น
import android.content.SharedPreferences; // นำเข้าคลาสจัดการข้อมูลแบบ Key-Value
import android.graphics.Bitmap; // นำเข้าคลาสจัดการโครงสร้างข้อมูลภาพ
import android.graphics.BitmapFactory; // นำเข้าคลาสแปลงไบต์สตรีมให้เป็นภาพ Bitmap
import android.graphics.Color; // นำเข้าคลาสจัดการสี
import android.graphics.drawable.ColorDrawable; // นำเข้าคลาสจัดการพื้นหลังสี
import android.net.Uri; // นำเข้าคลาสจัดการ Uri ของไฟล์หรือรูปภาพ
import android.os.Bundle; // นำเข้าคลาส Bundle สำหรับส่งผ่านข้อมูลสถานะตอนเริ่มต้น Activity
import android.text.TextUtils; // นำเข้าคลาสจัดการข้อความ
import android.util.Base64; // นำเข้าคลาสถอดรหัส Base64 ของรูปภาพ
import android.view.View; // นำเข้าคลาส View องค์ประกอบ UI ทั่วไป
import android.view.ViewGroup; // นำเข้าคลาสจัดกลุ่ม View
import android.widget.BaseAdapter; // นำเข้าอะแดปเตอร์พื้นฐานสำหรับ ListView
import android.widget.ImageView; // นำเข้าคลาสแสดงรูปภาพ
import android.widget.LinearLayout; // นำเข้าคลาสจัดเรียงเลย์เอาต์แนวตั้งและแนวนอน
import android.widget.ListView; // นำเข้าคลาสแสดงรายการ ListView
import android.widget.TextView; // นำเข้าคลาสแสดงข้อความ
import android.widget.Toast; // นำเข้าคลาสแสดงข้อความแจ้งเตือนขนาดเล็ก
import androidx.appcompat.app.AppCompatActivity; // นำเข้าคลาสแม่ข่ายสำหรับการสร้างหน้าจอแอปพลิเคชัน

import java.io.File; // นำเข้าคลาสจัดการไฟล์
import java.io.InputStream; // นำเข้าคลาสสตรีมข้อมูลรูปภาพ
import java.util.List; // นำเข้าโครงสร้างข้อมูลแบบรายการ List
import java.util.Locale; // นำเข้าคลาสจัดการรูปแบบภาษาและตัวเลข

// [OOP - Inheritance] ประกาศคลาส ProfileActivity สืบทอดมาจาก AppCompatActivity เพื่อทำหน้าที่เป็นหน้าจอโปรไฟล์ผู้ใช้งาน
public class ProfileActivity extends AppCompatActivity {

    // --- ประกาศตัวแปร (Attributes) สำหรับอ้างอิงองค์ประกอบ UI และ Repository ต่างๆ ---
    private LinearLayout layoutMyPostsGridContainer; // กล่องพื้นที่สำหรับแสดงตารางโพสต์ของผู้ใช้งานแบบไดนามิก
    private TextView tvPostCount, tvReviewCount, tvReviewHistoryCount; // ข้อความแสดงจำนวนโพสต์, คะแนนรีวิวเฉลี่ย, และจำนวนประวัติรีวิว
    private TextView tvProfileFacebook, tvProfileInstagram, tvProfilePhone; // ข้อความแสดงช่องทางการติดต่อ (Facebook, IG, เบอร์โทร)
    private LinearLayout btnReviewHistory; // ปุ่มกดเปิดดูประวัติรีวิว

    private UserRepository userRepository; // ตัวจัดการข้อมูลผู้ใช้งานระบบ
    private ProfileRepository profileRepository; // ตัวจัดการข้อมูลโปรไฟล์และโพสต์ของผู้ใช้

    // ==========================================
    // ส่วนที่ 1: วงจรชีวิตของหน้าจอ (Activity Lifecycle)
    // ==========================================

    // เมธอด onCreate จะทำงานทันทีเมื่อผู้ใช้เปิดเข้ามาที่หน้าจอโปรไฟล์
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // กำหนดเลย์เอาต์ XML สำหรับหน้าจอโปรไฟล์

        // สร้างออบเจ็กต์ UserRepository และ ProfileRepository
        userRepository = new UserRepository(this);
        profileRepository = new ProfileRepository(this);

        try {
            // เชื่อมโยงตัวแปร Java กับ ID ในไฟล์ XML
            layoutMyPostsGridContainer = findViewById(R.id.layoutMyPostsGridContainer);
            tvPostCount = findViewById(R.id.tvPostCount);
            tvReviewCount = findViewById(R.id.tvReviewCount);
            tvReviewHistoryCount = findViewById(R.id.tvReviewHistoryCount);

            tvProfileFacebook = findViewById(R.id.tvProfileFacebook);
            tvProfileInstagram = findViewById(R.id.tvProfileInstagram);
            tvProfilePhone = findViewById(R.id.tvProfilePhone);

            // ดักจับเหตุการณ์คลิกปุ่มดูประวัติรีวิว
            btnReviewHistory = findViewById(R.id.btnReviewHistory);
            if (btnReviewHistory != null) {
                btnReviewHistory.setOnClickListener(v -> showReviewHistoryDialog());
            }

            // โหลดข้อมูลโปรไฟล์ คะแนนรีวิว จำนวนรีวิว และรายการโพสต์
            loadUserProfile();
            loadRatingData();
            loadReviewCount();
            loadAndDisplayMyPostsGrid();
            setupBottomNavigation(); // ตั้งค่าแถบเมนูด้านล่าง

            // ปุ่มเพิ่มโพสต์ใหม่ (พาไปหน้า MainActivity)
            View btnAddPost = findViewById(R.id.btnAddPost);
            if (btnAddPost != null) {
                btnAddPost.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            // ปุ่มออกจากระบบ (พาไปหน้า LoginActivity และเคลียร์หน้าจอเก่าทั้งหมด)
            View btnLogout = findViewById(R.id.btnLogout);
            if (btnLogout != null) {
                btnLogout.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // เมธอดสำหรับบันทึกคะแนนรีวิวใหม่
    public static void submitNewRating(Context context, float newRating) {
        UserRepository repo = new UserRepository(context);
        repo.saveAverageRating(newRating);
    }

    // ฟังก์ชันโหลดและแสดงจำนวนประวัติรีวิวทั้งหมด
    private void loadReviewCount() {
        try {
            int totalReviews = profileRepository.getReviewCount();
            if (tvReviewHistoryCount != null) {
                tvReviewHistoryCount.setText(String.valueOf(totalReviews));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันแสดงหน้าต่าง Dialog ประวัติรีวิว
    private void showReviewHistoryDialog() {
        try {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_review_history, null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create();

            // ปุ่มปิด Dialog
            View btnClose = dialogView.findViewById(R.id.btnCloseDialog);
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> dialog.dismiss());
            }

            List<ReviewItem> reviewList = profileRepository.getReviewList(); // ดึงรายการรีวิว

            ListView lvReviewList = dialogView.findViewById(R.id.lvReviewList);
            BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() { return reviewList.size(); }
                @Override
                public Object getItem(int position) { return reviewList.get(position); }
                @Override
                public long getItemId(int position) { return position; }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.item_review_history, parent, false);
                    }
                    ReviewItem data = reviewList.get(position);

                    TextView tvName = convertView.findViewById(R.id.tvReviewerName);
                    TextView tvRating = convertView.findViewById(R.id.tvReviewRating);
                    TextView tvComment = convertView.findViewById(R.id.tvReviewComment);
                    TextView tvDate = convertView.findViewById(R.id.tvReviewDate);

                    if (tvName != null) tvName.setText(data.getReviewer());
                    if (tvRating != null) tvRating.setText("⭐ (" + data.getRating() + ")");
                    if (tvComment != null) tvComment.setText(data.getComment());
                    if (tvDate != null) tvDate.setText(data.getDate());

                    return convertView;
                }
            };

            if (lvReviewList != null) {
                lvReviewList.setAdapter(adapter);
            }

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            dialog.show(); // แสดงหน้าต่าง Dialog
        } catch (Exception e) {
            Toast.makeText(this, "เกิดข้อผิดพลาดในการโหลดประวัติรีวิว", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // เมธอด onResume จะทำงานทุกครั้งที่หน้าจอนี้กลับมาแสดงผลต่อ
    @Override
    protected void onResume() {
        super.onResume();
        loadRatingData();
        loadReviewCount();
        loadAndDisplayMyPostsGrid(); // โหลดรายการโพสต์ใหม่ทุกครั้งที่กลับมาที่หน้านี้
    }

    // ฟังก์ชันโหลดข้อมูลโปรไฟล์ผู้ใช้มาแสดงผล
    private void loadUserProfile() {
        try {
            UserProfile profile = userRepository.getUserProfile();

            TextView tvProfileName = findViewById(R.id.tvProfileName);
            if (tvProfileName != null) tvProfileName.setText(profile.getName());

            TextView tvProfileStudentId = findViewById(R.id.tvProfileStudentId);
            if (tvProfileStudentId != null) tvProfileStudentId.setText("รหัสนักศึกษา: " + profile.getStudentId());

            if (tvProfileFacebook != null) tvProfileFacebook.setText(profile.getFacebook());
            if (tvProfileInstagram != null) tvProfileInstagram.setText(profile.getInstagram());
            if (tvProfilePhone != null) tvProfilePhone.setText(profile.getPhone());

            ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
            if (ivProfileImage != null && profile.getImageUri() != null && !profile.getImageUri().isEmpty()) {
                safelySetImageUri(ivProfileImage, profile.getImageUri());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันโหลดข้อมูลคะแนนรีวิวเฉลี่ย
    private void loadRatingData() {
        try {
            float averageRating = userRepository.getAverageRating();
            if (tvReviewCount != null) {
                tvReviewCount.setText(String.format(Locale.getDefault(), "%.1f", averageRating));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ส่วนที่ 2: การแสดงผลตารางโพสต์ของผู้ใช้ (Grid Layout)
    // ==========================================

    // ฟังก์ชันโหลดและจัดเรียงตารางแสดงโพสต์ของผู้ใช้ทีละแถว (2 คอลัมน์ต่อแถว)
    private void loadAndDisplayMyPostsGrid() {
        try {
            if (layoutMyPostsGridContainer == null) return;
            layoutMyPostsGridContainer.removeAllViews(); // ล้างข้อมูลเก่าทิ้งก่อน

            List<PostItem> allPosts = profileRepository.getAllMyPosts(); // ดึงรายการโพสต์ทั้งหมดของผู้ใช้

            if (tvPostCount != null) {
                tvPostCount.setText(String.valueOf(allPosts.size())); // แสดงจำนวนโพสต์รวม
            }

            // วนลูปทีละ 2 รายการเพื่อจัดวางเป็น 2 คอลัมน์ซ้าย-ขวา
            for (int i = 0; i < allPosts.size(); i += 2) {
                LinearLayout rowLayout = new LinearLayout(this);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowParams.setMargins(0, 0, 0, 8);
                rowLayout.setLayoutParams(rowParams);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                PostItem leftItem = allPosts.get(i);
                View cardLeft = createProductCardView(leftItem, 8, 0); // สร้างการ์ดฝั่งซ้าย
                rowLayout.addView(cardLeft);

                if (i + 1 < allPosts.size()) {
                    PostItem rightItem = allPosts.get(i + 1);
                    View cardRight = createProductCardView(rightItem, 0, 8); // สร้างการ์ดฝั่งขวา
                    rowLayout.addView(cardRight);
                } else {
                    // ถ้าเหลือโพสต์เศษเดี่ยว ให้สร้าง View ว่างดันทรง
                    View emptyView = new View(this);
                    LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(0, 0, 1.0f);
                    emptyView.setLayoutParams(emptyParams);
                    rowLayout.addView(emptyView);
                }

                layoutMyPostsGridContainer.addView(rowLayout); // เพิ่มแถวลงในกล่องหลัก
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันสร้างหน้าตาการ์ดสินค้า/โพสต์แต่ละใบ
    private View createProductCardView(PostItem item, int marginRight, int marginLeft) {
        LinearLayout card = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
        );
        cardParams.setMargins(dpToPx(marginRight), 0, dpToPx(marginLeft), 0);
        card.setLayoutParams(cardParams);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0xFFFFFFFF);
        card.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        card.setElevation(dpToPx(1));

        // ป้ายแสดงประเภทโพสต์ (เช่น ขาย, แลก)
        TextView tvBadge = new TextView(this);
        tvBadge.setText(item.getType());
        tvBadge.setTextSize(9);
        tvBadge.setTextColor(0xFFFFFFFF);
        tvBadge.setPadding(dpToPx(6), dpToPx(2), dpToPx(6), dpToPx(2));
        tvBadge.getPaint().setFakeBoldText(true);
        if (item.getType().equals("ขาย")) {
            tvBadge.setBackgroundColor(0xFF00794C); // สีเขียวสำหรับขาย
        } else {
            tvBadge.setBackgroundColor(0xFFD97706); // สีส้มสำหรับประเภทอื่น
        }
        card.addView(tvBadge);

        // รูปภาพประกอบโพสต์
        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(80)
            );
            imgParams.setMargins(0, dpToPx(6), 0, 0);
            img.setLayoutParams(imgParams);

            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img.setBackgroundColor(0xFFF8F9FA);

            safelySetImageUri(img, item.getImageUri());
            card.addView(img);
        } else {
            View viewImg = new View(this);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(80)
            );
            viewParams.setMargins(0, dpToPx(6), 0, 0);
            viewImg.setLayoutParams(viewParams);
            viewImg.setBackgroundColor(item.getType().equals("ขาย") ? 0xFFE9ECEF : 0xFF343A40);
            card.addView(viewImg);
        }

        // หัวข้อโพสต์
        TextView tvTitle = new TextView(this);
        tvTitle.setText(item.getTitle() != null ? item.getTitle() : "");
        tvTitle.setTextSize(12);
        tvTitle.setTextColor(0xFF333333);
        tvTitle.setMaxLines(2);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.getPaint().setFakeBoldText(true);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, dpToPx(6), 0, 0);
        tvTitle.setLayoutParams(titleParams);
        card.addView(tvTitle);

        // ราคาหรือบัดเจ็ท
        TextView tvPrice = new TextView(this);
        tvPrice.setText(item.getPriceOrBudget() != null ? item.getPriceOrBudget() : "");
        tvPrice.setTextSize(12);
        tvPrice.setTextColor(0xFF00794C);
        tvPrice.getPaint().setFakeBoldText(true);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        priceParams.setMargins(0, dpToPx(4), 0, 0);
        tvPrice.setLayoutParams(priceParams);
        card.addView(tvPrice);

        // ปุ่มลบประกาศ
        TextView btnDelete = new TextView(this);
        btnDelete.setText("🗑️ ลบประกาศ");
        btnDelete.setTextSize(11);
        btnDelete.setTextColor(0xFFDC2626);
        btnDelete.setGravity(android.view.Gravity.CENTER);
        btnDelete.setBackgroundColor(0xFFFFF5F5);
        btnDelete.setPadding(dpToPx(4), dpToPx(6), dpToPx(4), dpToPx(6));
        btnDelete.getPaint().setFakeBoldText(true);

        LinearLayout.LayoutParams delParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        delParams.setMargins(0, dpToPx(8), 0, 0);
        btnDelete.setLayoutParams(delParams);
        // ดักจับเหตุการณ์คลิกปุ่มลบประกาศ
        btnDelete.setOnClickListener(v -> deletePost(item.getSourceBoard(), item.getOriginalIndex()));
        card.addView(btnDelete);

        return card;
    }

    // ฟังก์ชันช่วยตั้งค่ารูปภาพอย่างปลอดภัย รองรับทั้ง Base64, Uri, และไฟล์โลคอล
    private void safelySetImageUri(ImageView imageView, String uriStr) {
        try {
            if (uriStr == null || uriStr.isEmpty()) {
                imageView.setBackgroundColor(0xFFE9ECEF);
                return;
            }

            if (!uriStr.startsWith("file://") && !uriStr.startsWith("content://") && !uriStr.startsWith("http")) {
                try {
                    byte[] decodedString = Base64.decode(uriStr, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (uriStr.startsWith("file://")) {
                Uri uri = Uri.parse(uriStr);
                File imgFile = new File(uri.getPath());
                if (imgFile.exists()) {
                    imageView.setImageURI(uri);
                } else {
                    imageView.setBackgroundColor(0xFFE9ECEF);
                }
            } else {
                Uri uri = Uri.parse(uriStr);
                try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                    if (inputStream != null) {
                        imageView.setImageURI(uri);
                    } else {
                        imageView.setBackgroundColor(0xFFE9ECEF);
                    }
                } catch (Exception e) {
                    imageView.setBackgroundColor(0xFFE9ECEF);
                }
            }
        } catch (Exception e) {
            imageView.setBackgroundColor(0xFFE9ECEF);
            e.printStackTrace();
        }
    }

    // ฟังก์ชันสั่งลบโพสต์ออกจากระบบ
    private void deletePost(String sourceBoard, int index) {
        try {
            profileRepository.deletePost(sourceBoard, index);
            Toast.makeText(this, "ลบประกาศเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();

            // รีเฟรชข้อมูลและอัปเดตหน้าจอทันทีหลังจากลบสำเร็จ
            loadAndDisplayMyPostsGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ฟังก์ชันแปลงหน่วย dp เป็นพิกเซล (px)
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    // ==========================================
    // ส่วนที่ 3: ระบบนำทางด้านล่าง (Bottom Navigation)
    // ==========================================

    // ฟังก์ชันจัดการการคลิกเมนูด้านล่างเพื่อสลับไปยังหน้าจออื่นๆ
    private void setupBottomNavigation() {
        try {
            LinearLayout navMarket = findViewById(R.id.navMarket);
            if (navMarket != null) {
                navMarket.setOnClickListener(v -> {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            }

            LinearLayout navFavorite = findViewById(R.id.navFavorite);
            if (navFavorite != null) {
                navFavorite.setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class));
                    } catch (Exception e) {
                        Toast.makeText(this, "ยังไม่ได้สร้างหน้า FavoriteActivity", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            LinearLayout navProfile = findViewById(R.id.navProfile);
            if (navProfile != null) {
                navProfile.setOnClickListener(v -> {
                    // อยู่หน้าโปรไฟล์อยู่แล้ว ไม่ต้องทำอะไร
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}