package com.example.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val saleDao: SaleDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val allSales: Flow<List<Sale>> = saleDao.getAllSales()

    suspend fun initSeedProductsIfEmpty() {
        val count = productDao.getProductCount()
        if (count == 0) {
            val seedProducts = listOf(
                Product(id = "p1", name = "قلم حبر جاف أزرق", price = 30.0, stock = 200, category = "أقلام"),
                Product(id = "p2", name = "علبة أقلام رصاص (12 قطعة)", price = 180.0, stock = 50, category = "أقلام"),
                Product(id = "p3", name = "دفتر 100 صفحة مسطر", price = 120.0, stock = 80, category = "دفاتر وكراريس"),
                Product(id = "p4", name = "كراس رسم A4", price = 150.0, stock = 45, category = "دفاتر وكراريس"),
                Product(id = "p5", name = "حقيبة مدرسية ظهر", price = 2200.0, stock = 8, category = "حقائب مدرسية"),
                Product(id = "p6", name = "محفظة أدوات مدرسية", price = 450.0, stock = 20, category = "حقائب مدرسية"),
                Product(id = "p7", name = "مسطرة 30 سم", price = 60.0, stock = 45, category = "أدوات هندسية"),
                Product(id = "p8", name = "طقم هندسة كامل", price = 320.0, stock = 25, category = "أدوات هندسية"),
                Product(id = "p9", name = "ممحاة مع مبراة", price = 60.0, stock = 90, category = "أدوات هندسية"),
                Product(id = "p10", name = "علبة ألوان خشبية (24 لون)", price = 450.0, stock = 18, category = "ألوان ورسم"),
                Product(id = "p11", name = "ألوان مائية", price = 400.0, stock = 22, category = "ألوان ورسم"),
                Product(id = "p12", name = "ملف بلاستيكي شفاف", price = 60.0, stock = 100, category = "أخرى")
            )
            productDao.insertProducts(seedProducts)
        }
    }

    suspend fun saveProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(id: String) {
        productDao.deleteProductById(id)
    }

    suspend fun updateProductStock(id: String, decrementQty: Int) {
        val product = productDao.getProductById(id)
        if (product != null) {
            val newStock = (product.stock - decrementQty).coerceAtLeast(0)
            productDao.insertProduct(product.copy(stock = newStock))
        }
    }

    suspend fun saveSale(sale: Sale) {
        saleDao.insertSale(sale)
    }
}
