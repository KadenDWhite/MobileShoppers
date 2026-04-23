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
        // Give a random balance var between 0 and 1500
        var balance: Int = (0..1500).random()

        // Create a list of 32 items
        val shoppingList = listOf(
            ShopItem("Gaming PC", 1200), ShopItem("Smartphone", 800), ShopItem("Smart Watch", 250),
            ShopItem("Bluetooth Mic", 150), ShopItem("4K Monitor", 400), ShopItem("Drone", 900),
            ShopItem("VR Headset", 500), ShopItem("Mechanical KB", 120), ShopItem("Gaming Mouse", 80),
            ShopItem("Tablet", 350), ShopItem("DSLR Camera", 1100), ShopItem("Smart Speaker", 60),
            ShopItem("Graphics Card", 700), ShopItem("SSD 2TB", 180), ShopItem("E-Reader", 130),
            ShopItem("Webcam", 90), ShopItem("Router", 200), ShopItem("Power Bank", 50),
            ShopItem("Projector", 450), ShopItem("Soundbar", 300), ShopItem("Headphones", 220),
            ShopItem("Smart Light", 30), ShopItem("Game Console", 499), ShopItem("Fitness Tracker", 110),
            ShopItem("USB-C Hub", 45), ShopItem("Microphone", 190), ShopItem("Electric Scooter", 600),
            ShopItem("Laptop Stand", 40), ShopItem("External HD", 100), ShopItem("Smart Plug", 25),
            ShopItem("Trackpad", 130), ShopItem("Action Cam", 280)
        )

        // Randomly pick 8 items to display on "shelves" for the user this session
        var storeItems = shoppingList.shuffled().take(7)
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

        // Connecting the XML layout slots to our logic
        // Note: This will use the IDs from the previous XML snippets (item1, item2, etc.)
        val uiSlots = listOf(
            Pair(findViewById<LinearLayout>(R.id.containerItem1), findViewById<TextView>(R.id.labelItem1)),
            Pair(findViewById<LinearLayout>(R.id.containerItem2), findViewById<TextView>(R.id.labelItem2)),
            Pair(findViewById<LinearLayout>(R.id.containerItem3), findViewById<TextView>(R.id.labelItem3)),
            Pair(findViewById<LinearLayout>(R.id.containerItem4), findViewById<TextView>(R.id.labelItem4)),
            Pair(findViewById<LinearLayout>(R.id.containerItem6), findViewById<TextView>(R.id.labelItem6)),
            Pair(findViewById<LinearLayout>(R.id.containerItem7), findViewById<TextView>(R.id.labelItem7)),
            Pair(findViewById<LinearLayout>(R.id.containerItem8), findViewById<TextView>(R.id.labelItem8))
        )

        uiSlots.forEachIndexed { index, (container, label) ->
            val itemData = storeItems[index]
            // Set the text for the item name and price
            label?.text = "${itemData.name}\n$${itemData.price}"

            // Set the click listener for the item
            container?.setOnClickListener {
                if (balance >= itemData.price)
                {
                    // Deduct the price from the balance & update the money
                    balance -= itemData.price

                    //Update UI
                    balanceView.text = "Balance: $${balance}"

                    // Update the Transaction History
                    historyView?.append("\n- Bought ${itemData.name} for $${itemData.price}")

                    // Run the Pulse & Vanish Animations
                    animatePurchase(container)
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