package edu.skku.map.week4

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val showButton = findViewById<Button>(R.id.showButton)

        //Log.w(localClassName, "message")

        showButton.setOnClickListener{
            val layoutInflater: LayoutInflater =
                applicationContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
                ) as LayoutInflater

            val container = findViewById<LinearLayout>(R.id.container)

            layoutInflater.inflate(
                R.layout.sub_layout, container, true
            )

            val leftImageView = findViewById<ImageView>(R.id.leftImageView)
            val rightImageView = findViewById<ImageView>(R.id.rightImageView)

            leftImageView.setImageResource(R.drawable.bbq)
            rightImageView.setImageResource(R.drawable.bhc)

            val pizzaButton = findViewById<Button>(R.id.pizzaButton)

            pizzaButton.setOnClickListener{
                val pizzaData = ArrayList<Restaurant>()
                pizzaData.add(Restaurant(R.drawable.domino, "domino pizza"))
                pizzaData.add(Restaurant(R.drawable.pizzahut, "pizza hut"))
                pizzaData.add(Restaurant(R.drawable.pizzanarachickengongju, "pizzanara chickengongju"))

                val myAdapter = CustomAdapter(pizzaData, applicationContext)
                val listView = findViewById<ListView>(R.id.restaurantListView)
                listView.adapter = myAdapter
            }

            val hamburgerButton = findViewById<Button>(R.id.hamburgerButton)
            hamburgerButton.setOnClickListener{
                val hamburgerData = ArrayList<Restaurant>()
                hamburgerData.add(Restaurant(R.drawable.mcdonalds, "mcdonalds"))
                hamburgerData.add(Restaurant(R.drawable.lotteria, "burgerking"))
                hamburgerData.add(Restaurant(R.drawable.burgerking, "burgerking"))
                hamburgerData.add(Restaurant(R.drawable.momstouch, "momstouch"))

                val myAdapter = CustomAdapter(hamburgerData, applicationContext)
                val listView = findViewById<ListView>(R.id.restaurantListView)
                listView.adapter = myAdapter
            }

            val chickenButton = findViewById<Button>(R.id.chickenButton)
            chickenButton.setOnClickListener{
                val chickenData = ArrayList<Restaurant>()
                chickenData.add(Restaurant(R.drawable.bbq, "bbq"))
                chickenData.add(Restaurant(R.drawable.bhc, "bhc"))
                chickenData.add(Restaurant(R.drawable.pizzanarachickengongju, "pizzanara chickengongju"))
                chickenData.add(Restaurant(R.drawable.goobne, "goobne chicken"))

                val myAdapter = CustomAdapter(chickenData, applicationContext)
                val listView = findViewById<ListView>(R.id.restaurantListView)
                listView.adapter = myAdapter
            }
        }
    }
}