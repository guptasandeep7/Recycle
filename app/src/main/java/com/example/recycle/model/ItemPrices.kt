package com.example.recycle.model

data class ItemPrices(
    val name:String,
    val price: String
)

val priceList = listOf(
    ItemPrices("Water bottle", "Rs 5"),
    ItemPrices("Container", "Rs 5"),
    ItemPrices("Bottle", "Rs 10-50"),
    ItemPrices("Tableware", "Rs 50-100"),
    ItemPrices("Electronic device", "Rs 100-1000"),
    ItemPrices("Keyboard", "Rs 100"),
    ItemPrices("Mouse", "Rs 10-50"),
    ItemPrices("Watch", "Rs 100-500"),
    ItemPrices("Dustbin", "Rs 50-500"),
    ItemPrices("Laptop", "Rs 1000-5000"),
)



