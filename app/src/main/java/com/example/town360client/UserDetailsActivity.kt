package com.example.town360client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var userImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userNumberTextView: TextView
    private lateinit var userJobTextView: TextView
    private lateinit var userDetailsTextView: TextView
    private lateinit var userReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        // Initialize UI components
        userImageView = findViewById(R.id.userImageView)
        userNameTextView = findViewById(R.id.userNameTextView)
        userNumberTextView = findViewById(R.id.userNumberTextView)
        userJobTextView = findViewById(R.id.userJobTextView)
        userDetailsTextView = findViewById(R.id.userDetailsTextView)

        // Retrieve user information from intent
        val collection = intent.getStringExtra("ITEM_NAME")
        val userId = intent.getStringExtra("USER_ID").toString()

        // Initialize Firebase Database reference
        userReference = FirebaseDatabase.getInstance().getReference(collection.toString()).child(userId)

        // Add a ValueEventListener to listen for changes in user data
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User data found, update UI
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        // Load image using Picasso library
                        if (!user.image.isNullOrBlank()) {
                            Picasso.get().load(user.image).into(userImageView)
                        } else {
                            // If the image URL is empty or null, set a default image resource
                            userImageView.setImageResource(R.drawable.wp)
                        }

                        // Display user details
                        userNameTextView.text = user.name
                        userNumberTextView.text = user.number
                        userJobTextView.text = user.job
                        userDetailsTextView.text = user.details

                        // Set click listener for the call button
                        val callButton = findViewById<Button>(R.id.callButton)
                        callButton.setOnClickListener {
                            // Call the user's phone number
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:${user.number}")
                            startActivity(dialIntent)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Override onCreateOptionsMenu to inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_details, menu)
        return true
    }

    // Override onOptionsItemSelected to handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle back button click
                onBackPressed()
                return true
            }
            R.id.action_share -> {
                // Handle share button click
                shareUserDetails()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // Add a method to share user details
    private fun shareUserDetails() {
        // Replace this with the actual user details you want to share
        val userDetails = "Name: ${userNameTextView.text}\nNumber: ${userNumberTextView.text}\nJob: ${userJobTextView.text}"

        // Use ShareCompat.IntentBuilder to create a share intent
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(userDetails)
            .intent

        // Start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent, "Share User Details"))
    }
}
