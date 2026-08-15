package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class SaleItem(
    val name: String,
    val price: Double,
    val qty: Int,
    val lineTotal: Double
)

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey val id: String,
    val ts: Long,
    val itemsJson: String,
    val total: Double,
    val payMethod: String
) {
    fun getItems(): List<SaleItem> {
        return try {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, SaleItem::class.java)
            val adapter = moshi.adapter<List<SaleItem>>(listType)
            adapter.fromJson(itemsJson) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    companion object {
        fun itemsToJson(items: List<SaleItem>): String {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, SaleItem::class.java)
            val adapter = moshi.adapter<List<SaleItem>>(listType)
            return adapter.toJson(items)
        }
    }
}
