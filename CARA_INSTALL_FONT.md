# PENTING: Cara Download dan Install Font Plus Jakarta Sans

## Langkah-Langkah:

### Opsi 1: Download Manual dari Google Fonts (RECOMMENDED)

1. **Buka browser dan kunjungi:**
   ```
   https://fonts.google.com/specimen/Plus+Jakarta+Sans
   ```

2. **Klik tombol "Download Family" (pojok kanan atas)**

3. **Extract file ZIP yang terdownload**

4. **Cari 2 file font berikut di folder `static/`:**
   - `PlusJakartaSans-Regular.ttf`
   - `PlusJakartaSans-Bold.ttf`

5. **Rename dan copy ke project:**
   - Copy `PlusJakartaSans-Regular.ttf`
   - Rename menjadi `plus_jakarta_sans_regular.ttf` (huruf kecil semua, pakai underscore)
   - Paste ke: `app/src/main/res/font/`
   
   - Copy `PlusJakartaSans-Bold.ttf`
   - Rename menjadi `plus_jakarta_sans_bold.ttf`
   - Paste ke: `app/src/main/res/font/`

6. **Hapus file XML placeholder yang ada:**
   - Delete `plus_jakarta_sans_regular.xml`
   - Delete `plus_jakarta_sans_bold.xml`

7. **Update file `plus_jakarta_sans.xml`:**
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <font-family xmlns:android="http://schemas.android.com/apk/res/android">
       <font
           android:fontStyle="normal"
           android:fontWeight="400"
           android:font="@font/plus_jakarta_sans_regular" />
       <font
           android:fontStyle="normal"
           android:fontWeight="700"
           android:font="@font/plus_jakarta_sans_bold" />
   </font-family>
   ```

8. **Sync dan Rebuild Project:**
   - File → Sync Project with Gradle Files
   - Build → Clean Project
   - Build → Rebuild Project

### Opsi 2: Gunakan Downloadable Fonts (Lebih Mudah)

1. **Buka Android Studio**
2. **Buka file layout XML manapun (misal: `activity_patient_management.xml`)**
3. **Di Design view, klik pada TextView**
4. **Di Attributes panel, cari `fontFamily`**
5. **Klik dropdown, pilih "More Fonts..."**
6. **Search "Plus Jakarta Sans"**
7. **Centang:**
   - Add font to project
   - Create downloadable font
8. **Pilih Regular (400) dan Bold (700)**
9. **Klik OK**

Android Studio akan otomatis:
- Download font
- Membuat file konfigurasi
- Menambahkan ke resources

### Opsi 3: Gunakan Font Default (Jika Tidak Bisa Download)

Jika kedua cara di atas tidak berhasil, Anda bisa menggunakan font default Android:

**Edit semua file layout XML, ganti:**
```xml
android:fontFamily="@font/plus_jakarta_sans_regular"
```
**Menjadi:**
```xml
android:fontFamily="sans-serif"
```

**Dan ganti:**
```xml
android:fontFamily="@font/plus_jakarta_sans_bold"
```
**Menjadi:**
```xml
android:fontFamily="sans-serif-medium"
```

## Verifikasi Font Terinstall

Setelah install, check di Android Studio:
1. Buka folder `app/src/main/res/font/`
2. Pastikan ada file:
   - `plus_jakarta_sans_regular.ttf` (atau .otf)
   - `plus_jakarta_sans_bold.ttf` (atau .otf)
   - `plus_jakarta_sans.xml`

## Troubleshooting

**Error: "Unresolved reference '@font/plus_jakarta_sans_regular'"**
- Pastikan nama file huruf kecil semua
- Tidak ada spasi, gunakan underscore
- File harus .ttf atau .otf, bukan .xml untuk actual font file

**Font tidak muncul di aplikasi:**
- Clean Project (Build → Clean Project)
- Rebuild Project (Build → Rebuild Project)
- Invalidate Caches and Restart (File → Invalidate Caches...)

**File font terlalu besar:**
- Plus Jakarta Sans Regular: ~100KB
- Plus Jakarta Sans Bold: ~100KB
- Total: ~200KB (masih sangat kecil untuk APK)

---

Setelah font terinstall dengan benar, aplikasi akan menggunakan Plus Jakarta Sans untuk semua teks sesuai dengan desain mockup yang diberikan.

