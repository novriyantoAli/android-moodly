# Task: Implement Paging, Search, and Category Filtering on Consultation Screen

## Deskripsi
Tugas ini bertujuan untuk mengubah list konsultasi yang saat ini masih menggunakan mock data menjadi data riil dari API dengan dukungan pagination (lazy load), fitur pencarian, dan filter kategori.

## Objective
1. Mengintegrasikan API `api/v1/consultations` dengan parameter query.
2. Mengimplementasikan Pagination (Lazy Loading) pada `ConsultationScreen`.
3. Menambahkan fitur Search dengan debounce (500ms).
4. Menambahkan filter kategori: Semua, Aktif, Ditutup.
5. Error handling pada level repository dan ViewModel.

---

## Tahapan Implementasi

### 1. Update Data Model & API Service
Sesuaikan `ConsultationResponse` di `ConsultationApiService.kt` untuk menangkap struktur baru:
- Update `ConsultationApiService.getConsultations` agar menerima parameter: `page`, `limit`, `status`, dan `search`.
- Tambahkan class wrapper untuk pagination: `PaginatedResponse<T>`.

**Target File:** `data/remote/api/ConsultationApiService.kt`

### 2. Update Domain Layer
- Update interface `ConsultationRepository` untuk mendukung parameter filter dan pagination.
- Pastikan return type mendukung state error atau gunakan Result wrapper.

**Target File:** `domain/repository/ConsultationRepository.kt`

### 3. Implementasi Paging Source (Optional tapi Disarankan)
Gunakan Android Jetpack Paging 3 jika sudah ada dependensinya, atau implementasikan manual lazy loading di ViewModel.
- Jika manual: Tambahkan state `currentPage`, `isLastPage`, dan `isLoading` di ViewModel.
- Jika Paging 3: Buat `ConsultationPagingSource`.

### 4. Update Repository Implementation
- Implementasikan logika pemanggilan API dengan query parameter.
- Tambahkan `try-catch` block untuk menangkap error network/API dan kembalikan pesan error yang human-readable.

**Target File:** `data/repository/ConsultationRepositoryImpl.kt`

### 5. Update ViewModel Logic
- Tambahkan `MutableStateFlow` untuk `searchQuery` dan `selectedStatus`.
- Implementasikan **Debounce** menggunakan operator flow `debounce(500)` pada `searchQuery` sebelum memicu reload data.
- Gabungkan trigger dari Search, Kategori, dan Load More untuk memanggil fungsi fetch data yang sama.
- Reset page ke 1 setiap kali Search atau Kategori berubah.

**Target File:** `presentation/consultation/ConsultationViewModel.kt`

### 6. Update UI (ConsultationScreen)
- Hubungkan `OutlinedTextField` (Search) ke `searchQuery` di ViewModel.
- Hubungkan Chips (Kategori) ke `selectedStatus` di ViewModel.
- Ganti `Column` + `forEach` dengan `LazyColumn`.
- Tambahkan deteksi scroll ke bawah untuk memicu load page berikutnya.
- Tampilkan loading indicator (Shimmer atau ProgressBar) saat fetching data.
- Tampilkan pesan error dan tombol "Try Again" jika fetch gagal.

**Target File:** `presentation/consultation/ConsultationScreen.kt`

---

## Spesifikasi API

**Endpoint:** `GET api/v1/consultations`

**Query Parameters:**
- `page`: Nomor halaman (default: 1)
- `limit`: Jumlah data per halaman (default: 10)
- `status`: Filter status (`active`, `closed`, atau kosongkan untuk semua)
- `search`: Keyword pencarian nama psikolog.

**Struktur Response:**
```json
{
  "data": [
    {
      "conversation_id": "uuid",
      "status": "ACTIVE/CLOSED",
      "psychologist": { "name": "Dr. Name" },
      "created_at": "timestamp"
    }
  ],
  "meta": {
    "current_page": 1,
    "total_pages": 5
  }
}
```

---

## Panduan Pengerjaan untuk Junior/AI
1. **Pahami Flow Data:** API -> Repository -> ViewModel -> UI.
2. **Gunakan StateFlow:** Pastikan UI bereaksi terhadap perubahan state di ViewModel.
3. **Optimasi:** Jangan hit API setiap ketikan di search box, gunakan `debounce`.
4. **Clean Code:** Pastikan mapping dari Response API ke UI Model (`ChatItem`) dilakukan dengan rapi.
