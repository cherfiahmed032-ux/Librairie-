package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CategoryConstants
import com.example.data.Product
import com.example.data.ProductRepository
import com.example.data.Sale
import com.example.data.SaleItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class CartItemState(
    val product: Product,
    val qty: Int
) {
    val lineTotal: Double get() = product.price * qty
}

data class ProductFormData(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val stock: String = "",
    val category: String = "أقلام",
    val isEdit: Boolean = false
)

enum class AppTab {
    POS, PRODUCTS, TRANSACTIONS
}

class PosViewModel(private val repository: ProductRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.initSeedProductsIfEmpty()
        }
    }

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSales: StateFlow<List<Sale>> = repository.allSales
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTab = MutableStateFlow(AppTab.POS)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _posSearchQuery = MutableStateFlow("")
    val posSearchQuery: StateFlow<String> = _posSearchQuery.asStateFlow()

    private val _productsSearchQuery = MutableStateFlow("")
    val productsSearchQuery: StateFlow<String> = _productsSearchQuery.asStateFlow()

    private val _transactionsSearchQuery = MutableStateFlow("")
    val transactionsSearchQuery: StateFlow<String> = _transactionsSearchQuery.asStateFlow()

    private val _activeCategory = MutableStateFlow("الكل")
    val activeCategory: StateFlow<String> = _activeCategory.asStateFlow()

    // Cart items: Map<ProductId, Qty>
    private val _cartMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartMap: StateFlow<Map<String, Int>> = _cartMap.asStateFlow()

    val cartItems: StateFlow<List<CartItemState>> = combine(allProducts, _cartMap) { products, cart ->
        cart.mapNotNull { (prodId, qty) ->
            val product = products.find { it.id == prodId }
            if (product != null) CartItemState(product, qty) else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotalCount: StateFlow<Int> = _cartMap.combine(allProducts) { cart, _ ->
        cart.values.sum()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartTotalPrice: StateFlow<Double> = cartItems.combine(_cartMap) { items, _ ->
        items.sumOf { it.lineTotal }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Modal States
    private val _qtyModalProduct = MutableStateFlow<Product?>(null)
    val qtyModalProduct: StateFlow<Product?> = _qtyModalProduct.asStateFlow()

    private val _qtyModalValue = MutableStateFlow(1)
    val qtyModalValue: StateFlow<Int> = _qtyModalValue.asStateFlow()

    private val _cartModalOpen = MutableStateFlow(false)
    val cartModalOpen: StateFlow<Boolean> = _cartModalOpen.asStateFlow()

    private val _selectedPayMethod = MutableStateFlow("نقدي")
    val selectedPayMethod: StateFlow<String> = _selectedPayMethod.asStateFlow()

    private val _receiptModalSale = MutableStateFlow<Sale?>(null)
    val receiptModalSale: StateFlow<Sale?> = _receiptModalSale.asStateFlow()

    private val _productFormData = MutableStateFlow<ProductFormData?>(null)
    val productFormData: StateFlow<ProductFormData?> = _productFormData.asStateFlow()

    private val _confirmDeleteProduct = MutableStateFlow<Product?>(null)
    val confirmDeleteProduct: StateFlow<Product?> = _confirmDeleteProduct.asStateFlow()

    private val _confirmClearCart = MutableStateFlow(false)
    val confirmClearCart: StateFlow<Boolean> = _confirmClearCart.asStateFlow()

    // Toast Events
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun switchTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setPosSearchQuery(query: String) {
        _posSearchQuery.value = query
    }

    fun setProductsSearchQuery(query: String) {
        _productsSearchQuery.value = query
    }

    fun setTransactionsSearchQuery(query: String) {
        _transactionsSearchQuery.value = query
    }

    fun viewSaleReceipt(sale: Sale) {
        _receiptModalSale.value = sale
    }

    fun setActiveCategory(category: String) {
        _activeCategory.value = category
    }

    fun openQtyModal(product: Product) {
        if (product.stock <= 0) return
        val currentQty = _cartMap.value[product.id] ?: 1
        _qtyModalProduct.value = product
        _qtyModalValue.value = currentQty.coerceIn(1, product.stock)
    }

    fun closeQtyModal() {
        _qtyModalProduct.value = null
    }

    fun incrementQtyModal() {
        val product = _qtyModalProduct.value ?: return
        if (_qtyModalValue.value < product.stock) {
            _qtyModalValue.value += 1
        }
    }

    fun decrementQtyModal() {
        if (_qtyModalValue.value > 1) {
            _qtyModalValue.value -= 1
        }
    }

    fun confirmQtyAdd() {
        val product = _qtyModalProduct.value ?: return
        val qty = _qtyModalValue.value
        val newMap = _cartMap.value.toMutableMap()
        newMap[product.id] = qty
        _cartMap.value = newMap
        closeQtyModal()
        emitToast("أُضيف ${product.name} إلى السلة")
    }

    fun openCartModal() {
        _cartModalOpen.value = true
    }

    fun closeCartModal() {
        _cartModalOpen.value = false
    }

    fun setPayMethod(method: String) {
        _selectedPayMethod.value = method
    }

    fun changeCartItemQty(productId: String, delta: Int) {
        val product = allProducts.value.find { it.id == productId } ?: return
        val currentQty = _cartMap.value[productId] ?: return
        val newQty = currentQty + delta
        val newMap = _cartMap.value.toMutableMap()

        if (newQty <= 0) {
            newMap.remove(productId)
        } else if (newQty > product.stock) {
            newMap[productId] = product.stock
            emitToast("لا يوجد مخزون كافٍ")
        } else {
            newMap[productId] = newQty
        }
        _cartMap.value = newMap
    }

    fun removeCartItem(productId: String) {
        val newMap = _cartMap.value.toMutableMap()
        newMap.remove(productId)
        _cartMap.value = newMap
    }

    fun requestClearCart() {
        if (_cartMap.value.isNotEmpty()) {
            _confirmClearCart.value = true
        }
    }

    fun confirmClearCartAction() {
        _cartMap.value = emptyMap()
        _confirmClearCart.value = false
    }

    fun cancelClearCart() {
        _confirmClearCart.value = false
    }

    fun checkout() {
        val currentCart = cartItems.value
        if (currentCart.isEmpty()) return

        val total = currentCart.sumOf { it.lineTotal }
        val saleId = "id" + System.currentTimeMillis().toString(36) + UUID.randomUUID().toString().substring(0, 5)
        val items = currentCart.map {
            SaleItem(
                name = it.product.name,
                price = it.product.price,
                qty = it.qty,
                lineTotal = it.lineTotal
            )
        }
        val sale = Sale(
            id = saleId,
            ts = System.currentTimeMillis(),
            itemsJson = Sale.itemsToJson(items),
            total = total,
            payMethod = _selectedPayMethod.value
        )

        viewModelScope.launch {
            // Decrement stock for all items
            for (item in currentCart) {
                repository.updateProductStock(item.product.id, item.qty)
            }
            repository.saveSale(sale)

            _cartMap.value = emptyMap()
            _cartModalOpen.value = false
            _receiptModalSale.value = sale
        }
    }

    fun closeReceiptModal() {
        _receiptModalSale.value = null
    }

    fun openAddProductModal() {
        _productFormData.value = ProductFormData(
            id = "",
            name = "",
            price = "",
            stock = "",
            category = CategoryConstants.CATEGORIES.first().name,
            isEdit = false
        )
    }

    fun openEditProductModal(product: Product) {
        _productFormData.value = ProductFormData(
            id = product.id,
            name = product.name,
            price = if (product.price % 1.0 == 0.0) product.price.toInt().toString() else product.price.toString(),
            stock = product.stock.toString(),
            category = product.category,
            isEdit = true
        )
    }

    fun updateProductFormField(
        name: String? = null,
        price: String? = null,
        stock: String? = null,
        category: String? = null
    ) {
        val current = _productFormData.value ?: return
        _productFormData.value = current.copy(
            name = name ?: current.name,
            price = price ?: current.price,
            stock = stock ?: current.stock,
            category = category ?: current.category
        )
    }

    fun closeProductFormModal() {
        _productFormData.value = null
    }

    fun saveProductForm() {
        val form = _productFormData.value ?: return
        val name = form.name.trim()
        val price = form.price.toDoubleOrNull()
        val stock = form.stock.toIntOrNull()

        if (name.isEmpty() || price == null || price < 0 || stock == null || stock < 0) {
            emitToast("يرجى تعبئة جميع الحقول بشكل صحيح")
            return
        }

        val id = if (form.isEdit && form.id.isNotEmpty()) form.id else "id" + System.currentTimeMillis().toString(36) + UUID.randomUUID().toString().substring(0, 5)
        val product = Product(
            id = id,
            name = name,
            price = price,
            stock = stock,
            category = form.category
        )

        viewModelScope.launch {
            repository.saveProduct(product)
            closeProductFormModal()
            emitToast(if (form.isEdit) "تم تحديث المنتج" else "تمت إضافة المنتج")
        }
    }

    fun requestDeleteProduct(product: Product) {
        _confirmDeleteProduct.value = product
    }

    fun cancelDeleteProduct() {
        _confirmDeleteProduct.value = null
    }

    fun confirmDeleteProductAction() {
        val product = _confirmDeleteProduct.value ?: return
        viewModelScope.launch {
            repository.deleteProduct(product.id)
            // Also remove from cart
            val newMap = _cartMap.value.toMutableMap()
            newMap.remove(product.id)
            _cartMap.value = newMap
            _confirmDeleteProduct.value = null
            emitToast("تم حذف المنتج")
        }
    }

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toastEvent.emit(msg)
        }
    }
}

class PosViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PosViewModel::class.java)) {
            return PosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
