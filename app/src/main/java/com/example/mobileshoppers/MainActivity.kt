package com.example.mobileshoppers

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileshoppers.R.*

// To store the 32 possible items, we use a Data class containing the item name & price values
data class ShopItem(val name: String, val price: Int)
class MainActivity : AppCompatActivity() {
    // Use a companion object to keep our money and items saved even if the screen rotates
    // Balance randomization and items logic will be contained within the companion obj
    companion object {
        // Give a random balance var between 0 and 2500
        var balance: Int = (0..2500).random()

        // Create a list of 36 possible items
        val shoppingList = listOf(
            ShopItem("Gaming PC", 2200), ShopItem("Smartphone", 800), ShopItem("Smart Watch", 250),
            ShopItem("Bluetooth Mic", 150), ShopItem("4K Monitor", 400), ShopItem("Drone", 1000),
            ShopItem("VR Headset", 200), ShopItem("Mechanical KB", 120), ShopItem("Gaming Mouse", 55),
            ShopItem("Tablet", 350), ShopItem("DSLR Camera", 550), ShopItem("Smart Speaker", 50),
            ShopItem("Graphics Card", 500), ShopItem("SSD 2TB", 150), ShopItem("E-Reader", 45),
            ShopItem("Webcam", 69), ShopItem("Router", 145), ShopItem("Power Bank", 40),
            ShopItem("Projector", 450), ShopItem("Soundbar", 300), ShopItem("Headphones", 220),
            ShopItem("Smart Light", 20), ShopItem("Game Console", 400), ShopItem("Fitness Tracker", 85),
            ShopItem("USB-C Hub", 30), ShopItem("Microphone", 200), ShopItem("Electric Scooter", 600),
            ShopItem("Laptop Stand", 25), ShopItem("External HD", 100), ShopItem("Smart Plug", 15),
            ShopItem("Trackpad", 100), ShopItem("Action Cam", 280), ShopItem("Air", price = 0),
            ShopItem("AirPods", 150), ShopItem("Gum", price = 5), ShopItem("Paper", price = 10)
        )

        // Randomly pick 8 items to display on "shelves" for the user this session
        var storeItems = shoppingList.shuffled().take(8)

        // Keep track of the text in history so it doesn't vanish when swapping view states
        var currentHistory: String = "Recent Transactions: \n"

        // Keep track of which of the 8 items have been bought (by their index 0-7)
        var purchasedIndices = mutableSetOf<Int>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // IMPORTANT: Only run shop logic if we are in the Landscape view
        // We check this by looking for the balanceText ID
        val balanceView = findViewById<TextView>(R.id.balanceText)
        if (balanceView != null){
            setupShop(balanceView)
        }
    }

    private fun setupShop(balanceView: TextView){
        val historyView = findViewById<TextView>(R.id.transactionText)

        balanceView.text = "Balance: $${balance}"
        historyView?.text = currentHistory

        // Connecting the XML layout slots to our logic
        // Note: This will use the IDs from the previous XML snippets (item1, item2, etc.)
        val uiSlots = listOf(
            Pair(findViewById<LinearLayout>(R.id.containerItem1), findViewById<TextView>(R.id.labelItem1)),
            Pair(findViewById<LinearLayout>(R.id.containerItem2), findViewById<TextView>(R.id.labelItem2)),
            Pair(findViewById<LinearLayout>(R.id.containerItem3), findViewById<TextView>(R.id.labelItem3)),
            Pair(findViewById<LinearLayout>(R.id.containerItem4), findViewById<TextView>(R.id.labelItem4)),
            Pair(findViewById<LinearLayout>(R.id.containerItem5), findViewById<TextView>(R.id.labelItem5)),
            Pair(findViewById<LinearLayout>(R.id.containerItem6), findViewById<TextView>(R.id.labelItem6)),
            Pair(findViewById<LinearLayout>(R.id.containerItem7), findViewById<TextView>(R.id.labelItem7)),
            Pair(findViewById<LinearLayout>(R.id.containerItem8), findViewById<TextView>(R.id.labelItem8))
        )

        uiSlots.forEachIndexed { index, (container, label) ->
            val itemData = storeItems[index]
            // Set the text for the item name and price
            label?.text = "${itemData.name}\n$${itemData.price}"

            // If this item was already bought before rotation, hide it immediately
            if (purchasedIndices.contains(index)){
                container?.visibility = View.GONE
            }

            // Set the click listener for the item
            container?.setOnClickListener {
                if (balance >= itemData.price)
                {
                    // Deduct the price from the balance & update the money
                    balance -= itemData.price

                    // Update state in Companion Object
                    purchasedIndices.add(index)
                    currentHistory += "\nBought ${itemData.name} for $${itemData.price}"

                    // Update UI
                    balanceView.text = "Balance: $${balance}"
                    historyView?.text = currentHistory

                    // Run the Pulse & Vanish Animations
                    animatePurchase(container)

                    if (purchasedIndices.size == 8){
                        Toast.makeText(this, "Congratulations! You've cleared the store!", Toast.LENGTH_SHORT).show()
                        // Optional: Update the balance text to show a victory message
                        balanceView.text = "SHOP CLEARED!"
                    }
                }
                else
                {
                    // Inform the user that they don't have enough money
                    val msg = if (balance == 0) "Balance is $0! Go find some coins." else "Too expensive!"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
                }
            }
        }

    private fun animatePurchase(view: View) {
        // Step 1: Pulse Up
        view.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .setDuration(150)
            .withEndAction {
                // Step 2: Shrink and Fade Away
                view.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .setDuration(350)
                    .withEndAction {
                        view.visibility = View.GONE
                    }
                    .start()
            }
            .start()
        }
}