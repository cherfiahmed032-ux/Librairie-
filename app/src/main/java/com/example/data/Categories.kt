package com.example.data

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberDim
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanLight
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintDim
import com.example.ui.theme.MintLight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PinkDim
import com.example.ui.theme.PinkLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VioletLight
import com.example.ui.theme.VioletPrimary
import java.util.Locale

data class CategoryItem(
    val name: String,
    val color: Color,
    val gradient: Brush,
    val gradientColors: List<Color>
)

object CategoryConstants {
    val CATEGORIES = listOf(
        CategoryItem(
            name = "أقلام",
            color = VioletPrimary,
            gradient = Brush.linearGradient(listOf(VioletPrimary, VioletLight)),
            gradientColors = listOf(VioletPrimary, VioletLight)
        ),
        CategoryItem(
            name = "دفاتر وكراريس",
            color = CyanAccent,
            gradient = Brush.linearGradient(listOf(CyanAccent, CyanLight)),
            gradientColors = listOf(CyanAccent, CyanLight)
        ),
        CategoryItem(
            name = "حقائب مدرسية",
            color = PinkDim,
            gradient = Brush.linearGradient(listOf(PinkAccent, PinkLight)),
            gradientColors = listOf(PinkAccent, PinkLight)
        ),
        CategoryItem(
            name = "أدوات هندسية",
            color = AmberDim,
            gradient = Brush.linearGradient(listOf(AmberAccent, AmberLight)),
            gradientColors = listOf(AmberAccent, AmberLight)
        ),
        CategoryItem(
            name = "ألوان ورسم",
            color = MintDim,
            gradient = Brush.linearGradient(listOf(MintAccent, MintLight)),
            gradientColors = listOf(MintAccent, MintLight)
        ),
        CategoryItem(
            name = "أخرى",
            color = TextMuted,
            gradient = Brush.linearGradient(listOf(Color(0xFF9CA0C2), Color(0xFFC4C7DE))),
            gradientColors = listOf(Color(0xFF9CA0C2), Color(0xFFC4C7DE))
        )
    )

    fun catInfo(categoryName: String): CategoryItem {
        return CATEGORIES.find { it.name == categoryName } ?: CATEGORIES.last()
    }

    fun getInitials(name: String): String {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return "--"
        val firstWord = trimmed.split(" ").firstOrNull() ?: ""
        return if (firstWord.length >= 2) firstWord.substring(0, 2) else firstWord
    }

    fun formatPrice(amount: Double): String {
        return String.format(Locale.ENGLISH, "%.2f دج", amount)
    }
}
