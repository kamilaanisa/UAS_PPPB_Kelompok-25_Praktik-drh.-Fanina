# Fitur Manajemen Pasien - Praktik drh. Fanina

## Deskripsi
Aplikasi manajemen pasien untuk klinik hewan dengan fitur lengkap untuk mengelola data pasien dan hewan peliharaan mereka.

## Fitur-Fitur

### 1. **Daftar Pasien**
- Menampilkan list semua pasien dengan informasi:
  - Nama lengkap pasien
  - Nomor HP
  - Email
  - Tanggal dibuat reservasi
- Tombol aksi untuk setiap pasien (Edit, Hapus, Detail)

### 2. **Pencarian Pasien**
- Search bar untuk mencari pasien berdasarkan:
  - Nama lengkap
  - Email
  - Nomor HP
- Real-time filtering saat mengetik

### 3. **Tambah Pasien Baru**
- Form input meliputi:
  - Nama lengkap
  - Nomor HP
  - Email
  - Password
- Validasi input untuk memastikan data valid
- Tanggal pembuatan otomatis

### 4. **Edit Pasien**
- Mengubah informasi pasien:
  - Nama lengkap
  - Nomor HP
  - Email
- Data existing di-populate ke form

### 5. **Hapus Pasien**
- Konfirmasi dialog sebelum menghapus
- Menghapus semua data terkait pasien termasuk antrian reservasi

### 6. **Detail Hewan Pasien**
- Melihat daftar hewan yang dimiliki pasien
- Menampilkan nama hewan dan jenisnya (Kucing, Anjing, Kelinci, dll)

## Spesifikasi Warna

- **Teks Primary**: #333333
- **Button Tambah/Simpan/Detail**: #0081DD
- **Button Edit**: #FFAB2F
- **Button Hapus**: #ED4264
- **Button Tutup (Detail)**: #030213
- **Button Cancel**: 
  - Fill: #FFFFFF
  - Outline: #333333
  - Shadow: #000000 (10% opacity)
- **Placeholder Background**: #E8E8E8
- **Placeholder Text**: #606060

## Font
- **Body Text**: Plus Jakarta Sans Regular
- **Headers**: Plus Jakarta Sans Bold

## Instalasi Font

### Cara 1: Download dari Google Fonts
1. Kunjungi [Google Fonts - Plus Jakarta Sans](https://fonts.google.com/specimen/Plus+Jakarta+Sans)
2. Download font family
3. Extract file zip
4. Copy file TTF berikut ke folder `app/src/main/res/font/`:
   - `PlusJakartaSans-Regular.ttf` → rename menjadi `plus_jakarta_sans_regular.ttf`
   - `PlusJakartaSans-Bold.ttf` → rename menjadi `plus_jakarta_sans_bold.ttf`

### Cara 2: Menggunakan downloadable fonts (Alternatif)
Jika tidak ingin download manual, Anda bisa menggunakan downloadable fonts dari Android Studio:
1. Buka file XML layout
2. Klik kanan pada `fontFamily`
3. Pilih "More Fonts..."
4. Cari "Plus Jakarta Sans"
5. Select Regular dan Bold
6. Klik OK

## Struktur File

```
app/src/main/
├── java/com/example/praktikdrhfanina/
│   ├── PatientManagementActivity.kt
│   ├── MainActivity.kt
│   ├── SplashActivity.kt
│   ├── adapter/
│   │   └── PatientAdapter.kt
│   └── model/
│       ├── Patient.kt
│       └── Pet.kt
├── res/
│   ├── layout/
│   │   ├── activity_patient_management.xml
│   │   ├── item_patient.xml
│   │   ├── dialog_add_patient.xml
│   │   ├── dialog_edit_patient.xml
│   │   ├── dialog_delete_patient.xml
│   │   └── dialog_patient_pets.xml
│   ├── drawable/
│   │   ├── bg_button_primary.xml
│   │   ├── bg_button_danger.xml
│   │   ├── bg_button_warning.xml
│   │   ├── bg_button_cancel.xml
│   │   ├── bg_button_close.xml
│   │   ├── bg_input.xml
│   │   ├── bg_search.xml
│   │   ├── ic_add.xml
│   │   ├── ic_edit.xml
│   │   ├── ic_delete.xml
│   │   ├── ic_info.xml
│   │   ├── ic_search.xml
│   │   └── ic_close.xml
│   ├── font/
│   │   ├── plus_jakarta_sans.xml
│   │   ├── plus_jakarta_sans_regular.xml (atau .ttf)
│   │   └── plus_jakarta_sans_bold.xml (atau .ttf)
│   └── values/
│       └── colors.xml
```

## Cara Menjalankan

1. **Sync Gradle**
   - Buka project di Android Studio
   - Klik "Sync Project with Gradle Files"
   
2. **Install Font** (Lihat bagian Instalasi Font di atas)

3. **Build Project**
   - Build → Rebuild Project
   - Tunggu hingga proses selesai

4. **Run Application**
   - Pilih emulator atau device
   - Klik tombol Run (hijau) atau Shift+F10

## Sample Data

Aplikasi dilengkapi dengan sample data untuk testing:
- **Budi Santoso** - 081234567890 - budi@email.com
  - Hewan: Rakai (Kucing), Rio (Anjing), Prihastomo (Kelinci)
- **Siti Nurhaliza** - 081234567891 - siti@email.com
- **Ahmad Dahlan** - 081234567892 - ahmad@email.com
- **Dewi Lestari** - 081234567893 - dewi@email.com

## Fitur Tambahan yang Bisa Dikembangkan

1. **Integrasi Database**
   - Room Database untuk persistent storage
   - Firebase Realtime Database untuk cloud storage

2. **Manajemen Hewan**
   - CRUD operations untuk data hewan
   - Upload foto hewan

3. **Manajemen Reservasi**
   - Lihat dan kelola antrian reservasi pasien
   - Notifikasi reminder

4. **Authentication**
   - Login system untuk admin
   - Role-based access control

5. **Export Data**
   - Export ke PDF
   - Export ke Excel

## Dependencies

```kotlin
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("androidx.cardview:cardview:1.0.0")
```

## Minimum Requirements

- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36
- **Kotlin**: Latest version
- **Android Studio**: Arctic Fox atau lebih baru

## Troubleshooting

### Font tidak muncul
- Pastikan file font sudah ada di folder `res/font/`
- Check nama file (harus lowercase dengan underscore)
- Clean dan Rebuild project

### ViewBinding error
- Pastikan ViewBinding enabled di build.gradle.kts
- Sync Gradle
- Rebuild project

### RecyclerView tidak muncul
- Check adapter sudah di-set
- Check layoutManager sudah di-set
- Pastikan data tidak kosong

## Support

Untuk pertanyaan atau issue, silakan hubungi tim developer.

---
**Kelompok 25 - UAS PPPB**
**Praktik drh. Fanina**

