# Restaurant Dashboard - Food Delivery Platform

Dashboard frontend untuk restoran dalam sistem Food Delivery Platform. Interface ini membolehkan pemilik restoran menguruskan profil, menu, pesanan, dan melihat analytics.

## 🚀 Ciri-Ciri Utama

### 1. **Dashboard Overview**
- Statistik harian (jumlah pesanan, pendapatan, pesanan pending)
- Senarai pesanan terkini
- Ringkasan prestasi restoran

### 2. **Pengurusan Pesanan**
- Lihat semua pesanan masuk
- Update status pesanan (Pending → Accepted → Preparing → Ready → Delivered)
- Filter pesanan mengikut status
- Maklumat lengkap setiap pesanan

### 3. **Pengurusan Menu**
- Tambah item menu baru
- Edit item menu sedia ada
- Toggle ketersediaan item
- Upload gambar untuk setiap item
- Kategorikan menu (Appetizer, Main Course, Dessert, dll)

### 4. **Profil Restoran**
- Kemaskini maklumat restoran
- Set waktu operasi
- Tetapkan radius penghantaran
- Upload logo restoran
- Status aktif/tidak aktif

### 5. **Analytics & Reports**
- Graf trend pesanan harian
- Analisis pendapatan
- Laporan prestasi (coming soon)

## 📁 Struktur File

```
restaurant/
├── index.html          # Dashboard utama
├── login.html          # Halaman login
├── styles.css          # Styling CSS
├── script.js           # JavaScript functionality
└── README.md           # Dokumentasi ini
```

## 🛠️ Teknologi Yang Digunakan

- **HTML5** - Struktur halaman
- **CSS3** - Styling dan responsive design
- **Vanilla JavaScript** - Functionality dan API integration
- **Google Fonts (Inter)** - Typography
- **Flexbox & CSS Grid** - Layout system

## 🎨 Design Features

- **Modern UI/UX** - Clean dan professional
- **Responsive Design** - Berfungsi di desktop, tablet, dan mobile
- **Dark Sidebar** - Navigation yang mudah
- **Card-based Layout** - Organized content display
- **Color-coded Status** - Visual status indicators
- **Smooth Animations** - Enhanced user experience

## 🔧 Setup & Installation

1. **Clone atau download** file-file dalam folder `restaurant/`

2. **Pastikan backend server berjalan** di `http://localhost:8080`

3. **Buka `login.html`** dalam browser untuk mula

4. **Gunakan demo credentials:**
   - Username: `restaurant@demo.com`
   - Password: `demo123`

## 🔗 API Integration

Dashboard ini berinteraksi dengan backend Java melalui REST API endpoints:

### Authentication
- `POST /api/auth/login` - Login restoran

### Restaurant Management
- `GET /api/restaurants` - Dapatkan maklumat restoran
- `POST /api/restaurants` - Cipta restoran baru
- `PUT /api/restaurants/{id}` - Kemaskini restoran

### Menu Management
- `GET /api/fooditems` - Dapatkan semua menu items
- `POST /api/fooditems` - Tambah item baru
- `PUT /api/fooditems/{id}` - Kemaskini item
- `DELETE /api/fooditems/{id}` - Padam item

### Order Management
- `GET /api/orders` - Dapatkan semua pesanan
- `PUT /api/orders/{id}` - Kemaskini status pesanan

## 📱 Responsive Breakpoints

- **Desktop**: > 1024px
- **Tablet**: 768px - 1024px
- **Mobile**: < 768px

## 🎯 Status Pesanan Flow

```
PENDING → ACCEPTED → PREPARING → READY_FOR_DELIVERY → ON_THE_WAY → DELIVERED
    ↓
CANCELLED (boleh dari mana-mana status awal)
```

## 🔐 Authentication & Security

- JWT token authentication
- Token disimpan dalam localStorage
- Auto-redirect jika tidak authenticated
- Secure API calls dengan Authorization header

## 🎨 Color Scheme

```css
Primary Blue: #3b82f6
Dark Background: #1e293b
Light Gray: #f8fafc
Success Green: #10b981
Warning Orange: #f59e0b
Danger Red: #ef4444
```

## 📊 Dashboard Statistics

Dashboard menunjukkan:
- **Total Orders Today** - Jumlah pesanan hari ini
- **Today's Revenue** - Pendapatan hari ini
- **Pending Orders** - Pesanan yang menunggu
- **Menu Items** - Jumlah item dalam menu

## 🔄 Real-time Updates

- Auto-refresh data setiap 30 saat (boleh dikonfigurasi)
- Instant UI updates selepas actions
- Loading states untuk better UX
- Error handling dengan user-friendly messages

## 🚀 Future Enhancements

1. **Real-time Notifications** - WebSocket integration
2. **Advanced Analytics** - Charts dan graphs
3. **Bulk Operations** - Multiple item management
4. **Export Reports** - PDF/Excel export
5. **Multi-language Support** - i18n implementation
6. **Dark Mode** - Theme switching
7. **Offline Support** - PWA capabilities

## 🐛 Troubleshooting

### Common Issues:

1. **API Connection Failed**
   - Pastikan backend server berjalan di port 8080
   - Check CORS settings di backend

2. **Login Tidak Berjaya**
   - Gunakan demo credentials yang disediakan
   - Check browser console untuk error messages

3. **Data Tidak Load**
   - Check network tab dalam browser developer tools
   - Verify API endpoints dalam script.js

4. **Responsive Issues**
   - Clear browser cache
   - Test dalam different browsers

## 📞 Support

Untuk sokongan teknikal atau pertanyaan:
- Check browser console untuk error messages
- Verify backend API is running
- Ensure proper authentication token

## 📄 License

Projek ini adalah sebahagian daripada BITP3123 coursework untuk Universiti Teknikal Malaysia Melaka (UTeM).

---

**Dicipta oleh Team 2 BITS S1G2 - UTeM FTKK**
