package uk.ac.aber.dcs.cs39440.myvitalife.ui

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs39440.myvitalife.model.*
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.model.quotes.Quote
import uk.ac.aber.dcs.cs39440.myvitalife.model.quotes.QuoteOfTheDay
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils
import java.io.FileWriter
import java.io.IOException
import java.time.Duration

// A singleton object to hold the currently logged in user's authentication details
object Authentication {
    var userId = ""
    var userEmail = ""

    // Log in error codes
    const val LOGGED_IN_SUCCESSFULLY = 0
    const val PASSWORD_WRONG = 1
    const val ACCOUNT_DOES_NOT_EXIST = 2
    const val OTHER = 3
    const val USER_IS_NOT_VERIFIED = 7

    // Sign up error codes
    const val SIGNED_UP_SUCCESSFULLY = 0
    const val USER_ALREADY_EXISTS = 4
    const val EMAIL_WRONG_FORMAT = 5
    const val PASSWORD_WRONG_FORMAT = 6
}

class FirebaseViewModel : ViewModel() {

    // LiveData objects to hold the fetched data from Firebase database
    private val _foodList = MutableLiveData<List<Food>>()
    val foodList: LiveData<List<Food>> = _foodList

    private val _moodsList = MutableLiveData<List<Mood>>()
    val moodsList: LiveData<List<Mood>> = _moodsList

    private val _waterData = MutableLiveData<Water>()
    val waterData: LiveData<Water> = _waterData

    private val _favouriteQuotes = MutableLiveData<List<Quote>>()
    val favouriteQuotes: LiveData<List<Quote>> = _favouriteQuotes

    private val _goalList = MutableLiveData<List<Goal>>()
    val goalData: LiveData<List<Goal>> = _goalList

    private val _sleepHours = MutableLiveData<Sleep>()
    val sleepHours: LiveData<Sleep> = _sleepHours

    // Firebase database instance
    private val database = FirebaseDatabase.getInstance()

    // A mutable state variable to indicate whether the user is logged in or not
    private var isLoggedIn = mutableStateOf(false)

    // The currently selected date in the app, initially set to today's date
    private var chosenDate = DesiredDate.date

    // A private function to register a listener to the "DesiredDate" object date change event
    private fun registerDateChangeListener() {
        DesiredDate.dateChangeListeners.add(::fetchAllData)
    }

    // Private helper functions to update each LiveData object with the latest data from the Firebase database
    private fun updateFoodData() {
        fetchFoodData(chosenDate) { foods ->
            _foodList.value = foods
        }
    }

    private fun updateMoodData() {
        fetchMoodData(chosenDate) { moods ->
            _moodsList.value = moods
        }
    }

    private fun updateGoalData() {
        fetchGoalData(chosenDate) { goals ->
            _goalList.value = goals
        }
    }

    private fun updateWaterData() {
        fetchWaterData(chosenDate) { water ->
            _waterData.value = water
        }
    }

    private fun updateQuotesData() {
        fetchQuoteData(chosenDate) { quote ->
            QuoteOfTheDay.quote = quote
        }
        fetchFavouriteQuotes { quotes ->
            _favouriteQuotes.value = quotes
        }
    }

    private fun updateSleepData() {
        fetchSleepData(chosenDate) { sleeps ->
            _sleepHours.value = sleeps
        }
    }

    private fun fetchAllData() {
        chosenDate = DesiredDate.date
        updateFoodData()
        updateWaterData()
        updateGoalData()
        updateMoodData()
        updateSleepData()
        updateQuotesData()
    }

    init {
        registerDateChangeListener()
        // Fetch App data
        fetchAllData()
    }

    /**
     * Fetches food data from Firebase Realtime Database for a given date.
     *
     * @param date The date for which to fetch food data. Defaults to chosenDate.
     * @param callback A function to be called with a list of Food objects representing the fetched data.
     */
    fun fetchFoodData(date: String = chosenDate, callback: (List<Food>) -> Unit) {

        // Get a reference to the "ListOfFood" node for the current user and date.
        val foodRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfFood")

        // Initialize an empty list of Food objects.
        val foods = mutableListOf<Food>()

        // Attach a ValueEventListener to the foodRef node.
        foodRef.addValueEventListener(object : ValueEventListener {

            /**
             * Called when the data at the specified database reference changes.
             *
             * @param snapshot A snapshot of the data at the specified database reference.
             */
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val foodName = foodSnapshot.key
                    var amount = foodSnapshot.child("amount").value
                    var kcal = foodSnapshot.child("kcal").value
                    if (amount != null) {
                        amount = amount.toString().toInt()
                    } else {
                        amount = 0
                    }
                    if (kcal != null) {
                        kcal = kcal.toString().toInt()
                    } else {
                        kcal = 0
                    }
                    foods.add(
                        Food(
                            name = foodName!!,
                            kcal = kcal,
                            amount = amount
                        )
                    )
                }
                callback(foods)
            }

            /**
             * Called when the listener is cancelled and the database reference is no longer valid.
             *
             * @param error The error that caused the listener to be cancelled.
             */
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Fetches mood data from Firebase Realtime Database for a given date.
     *
     * @param date The date for which to fetch mood data. Defaults to chosenDate.
     * @param callback A function to be called with a list of Mood objects representing the fetched data.
     */
    fun fetchMoodData(date: String = chosenDate, callback: (List<Mood>) -> Unit) {

        // Get a reference to the "ListOfMoods" node for the current user and date.
        val moodRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfMoods")

        // Initialize an empty list of Mood objects.
        val moods = mutableListOf<Mood>()

        // Attach a ValueEventListener to the moodRef node.
        moodRef.addValueEventListener(object : ValueEventListener {

            /**
             * Called when the data at the specified database reference changes.
             *
             * @param snapshot A snapshot of the data at the specified database reference.
             */
            override fun onDataChange(snapshot: DataSnapshot) {
                for (moodSnapshot in snapshot.children) {
                    val moodType = moodSnapshot.child("emojiIndex").value.toString()
                    val optionalDescription =
                        moodSnapshot.child("optionalDescription").value.toString()
                    val time = moodSnapshot.key
                    moods.add(
                        Mood(
                            type = moodType.toInt(),
                            description = optionalDescription,
                            time = time!!
                        )
                    )
                }
                callback(moods)
            }

            /**
             * Called when the listener is cancelled and the database reference is no longer valid.
             *
             * @param error The error that caused the listener to be cancelled.
             */
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Fetches goal data from Firebase Realtime Database for a given date.
     *
     * @param date The date for which to fetch goal data. Defaults to chosenDate.
     * @param callback A function to be called with a list of Goal objects representing the fetched data.
     */
    fun fetchGoalData(date: String = chosenDate, callback: (List<Goal>) -> Unit) {

        // Get a reference to the "CustomGoals" node for the current user and date.
        val goalRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("CustomGoals")

        // Initialize an empty list of Goal objects.
        val goals = mutableListOf<Goal>()

        // Attach a ValueEventListener to the goalRef node.
        goalRef.addValueEventListener(object : ValueEventListener {

            /**
             * Called when the data at the specified database reference changes.
             *
             * @param snapshot A snapshot of the data at the specified database reference.
             */
            override fun onDataChange(snapshot: DataSnapshot) {
                for (goalSnapshot in snapshot.children) {
                    val goalTitle = goalSnapshot.key
                    val isDone = goalSnapshot.child("isDone").value.toString().toBoolean()
                    val goal = Goal(title = goalTitle!!, achieved = isDone)
                    goals.add(goal)
                }
                callback(goals)
            }

            /**
             * Called when the listener is cancelled and the database reference is no longer valid.
             *
             * @param error The error that caused the listener to be cancelled.
             */
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Fetches the water data from Firebase for the given date and invokes the callback with a [Water] object.
     * If no data exists for the given date, invokes the callback with a [Water] object with default values.
     *
     * @param date The date for which the water data is to be fetched. Defaults to the [chosenDate] if not specified.
     * @param callback The function to be invoked with the fetched [Water] data.
     */
    fun fetchWaterData(date: String = chosenDate, callback: (Water) -> Unit) {
        val waterRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("WaterData")

        waterRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val hydrationGoal = snapshot.child("hydrationGoal").value
                    val cupSize = snapshot.child("cupSize").value
                    val waterDrunk = snapshot.child("waterDrunk").value
                    if (hydrationGoal != null && cupSize != null && waterDrunk != null) {
                        // If all necessary data exists, create a [Water] object and invoke the callback with it.
                        val water = Water(
                            cupSize.toString().toInt(),
                            hydrationGoal.toString().toInt(),
                            waterDrunk.toString().toInt()
                        )
                        callback(water)
                    }
                } else {
                    // If no data exists for the given date, invoke the callback with a default [Water] object.
                    callback(Water(0, 0))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Fetches a "quote of the day" from the Firebase Realtime Database based on a given date.
     *
     * @param date The date for which to fetch the quote (default is the current date).
     * @param callback A function that takes a `Quote` object as its parameter and is called when the data is fetched.
     */
    fun fetchQuoteData(date: String = chosenDate, callback: (Quote) -> Unit) {
        val quoteRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("QuoteOfTheDay")

        quoteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val quotes = snapshot.child("quote").value.toString()
                    val author = snapshot.child("author").value.toString()
                    val isFavourite = snapshot.child("isFavourite").value.toString().toBoolean()
                    if (quotes != "" && author != "") {
                        val quote = Quote(
                            quotes,
                            author,
                            isFavourite
                        )
                        callback(quote)
                    }
                } else {
                    callback(Quote("", ""))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Fetches the user's favorite quotes from Firebase and invokes the callback with a list of [Quote] objects.
     *
     * @param callback The function to be invoked with the fetched list of [Quote] objects.
     */
    fun fetchFavouriteQuotes(callback: (List<Quote>) -> Unit) {
        val userRef = database.getReference("Users")
            .child(Authentication.userId)

        val quotes = mutableListOf<Quote>()

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dateSnapShot in snapshot.children) {
                    val quoteOfTheDay = dateSnapShot.child("QuoteOfTheDay")

                    if (quoteOfTheDay.exists()) {
                        val quoteText = quoteOfTheDay.child("quote").value.toString()
                        val author = quoteOfTheDay.child("author").value.toString()
                        val isFavourite =
                            quoteOfTheDay.child("isFavourite").getValue(Boolean::class.java)
                        val date = dateSnapShot.key.toString()

                        if (isFavourite == true) {
                            val quote = Quote(quoteText, author, isFavourite, date)
                            quotes.add(quote)
                        }
                    }
                }
                callback(quotes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Fetches the sleep data from Firebase for the given date and invokes the callback with a [Sleep] object.
     * If no data exists for the given date, invokes the callback with a [Sleep] object with default values.
     *
     * @param date The date for which the sleep data is to be fetched. Defaults to the [chosenDate] if not specified.
     * @param callback The function to be invoked with the fetched [Sleep] data.
     */
    fun fetchSleepData(date: String = chosenDate, callback: (Sleep) -> Unit) {
        val sleepRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("SleepData")

        sleepRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val sleepScore = snapshot.child("sleepScore").value
                    val sleepStart = snapshot.child("sleepStart").value
                    val sleepEnd = snapshot.child("sleepEnd").value
                    val sleepDuration = snapshot.child("sleepDuration").value
                    val optionalDescription = snapshot.child("optionalDescription").value.toString()
                    if (sleepScore != null && sleepStart != null && sleepEnd != null) {
                        val sleep = Sleep(
                            sleepScore.toString().toFloat(),
                            sleepStart.toString(),
                            sleepEnd.toString(),
                            sleepDuration.toString(),
                            optionalDescription
                        )
                        callback(sleep)
                    }
                } else {
                    callback(Sleep(0f, "", "", "", ""))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Adds food item to the user's list of food for a specific date.
     *
     * @param name The name of the food.
     * @param kcal The number of calories in the food.
     * @param date The date for which the food is to be added. Defaults to the [chosenDate].
     */
    fun addFood(name: String, kcal: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfFood")
                .child(name)
            databaseReference.child("kcal").setValue(kcal)
            databaseReference.child("amount").setValue(1)
                .addOnSuccessListener {
                    updateFoodData()
                }
        }
    }

    /**
     * Deletes a food item from the user's list of food for a specific date.
     *
     * @param name The name of the food item to be deleted
     * @param date The date for which the food item is to be deleted. Defaults to the [chosenDate].
     */
    fun deleteFood(name: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfFood")
                .child(name)
            databaseReference.removeValue().addOnSuccessListener {
                updateFoodData()
            }
        }
    }

    /**
     * Adds mood entry to the user's list of moods for a specific date.
     *
     * @param type The index of the emoji corresponding to the mood.
     * @param description An optional description of the mood.
     * @param date The date for which the mood entry is to be added. Defaults to the [chosenDate].
     */
    fun addMood(type: Int, description: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfMoods")
                .child(Utils.getCurrentTime())
            databaseReference.child("emojiIndex").setValue(type.toString())
            databaseReference.child("optionalDescription").setValue(description)
                .addOnSuccessListener {
                    updateMoodData()
                }
        }
    }

    /**
     * Deletes a mood entry from the user's list of mood for a specific date.
     *
     * @param time The time of the mood to be deleted.
     * @param date The date of the mood to be deleted. Defaults to [chosenDate].
     */
    fun deleteMood(time: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfMoods")
                .child(time)
            databaseReference.removeValue().addOnSuccessListener {
                updateMoodData()
            }
        }
    }

    /**
     * Fetches mood summary for the specified day.
     *
     * @param date The date for which to fetch the mood summary. Defaults to the [chosenDate].
     * @param callback The callback function to be called with the mood summary.
     */
    fun fetchMoodSummaryForTheDay(date: String = chosenDate, callback: (Map<Int, Int>) -> Unit) {
        val moodRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfMoods")

        val moodCountMap = mutableMapOf<Int, Int>()

        moodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (moodSnapshot in snapshot.children) {
                    val moodType = moodSnapshot.child("emojiIndex").value.toString().toInt()
                    if (moodCountMap.containsKey(moodType)) {
                        moodCountMap[moodType] = moodCountMap[moodType]!! + 1
                    } else {
                        moodCountMap[moodType] = 1
                    }
                }
                callback(moodCountMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Adds a custom goal to the user's list of goals for a specific date.
     *
     * @param title the title of the goal to be added
     * @param isDone whether the goal is already completed or not (default is false)
     * @param date the date for which the goal should be added. Defaults to the [chosenDate].
     */
    fun addGoal(title: String, isDone: Boolean = false, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("CustomGoals")
                .child(title)
            databaseReference.child("isDone").setValue(isDone)
                .addOnSuccessListener {
                    updateGoalData()
                }
        }
    }

    /**
     * Deletes a custom goal from the user's list of goals for a specific date.
     *
     * @param title The title of the custom goal to delete.
     * @param date The date for which to delete the custom goal. Defaults to [chosenDate].
     */
    fun deleteGoal(title: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("CustomGoals")
                .child(title)
            databaseReference.removeValue().addOnSuccessListener {
                // Update goalList after goal is deleted
                updateGoalData()
            }
        }
    }

    /**
     * Deletes all user data from the database.
     * After deletion, it fetches all data from the database again to update the UI.
     */
    fun deleteAllUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
            databaseReference.removeValue().addOnSuccessListener {
// Update UI after deletion
                fetchAllData()
            }
        }
    }

    /**
     * Get the isDone value of a goal.
     *
     * @param title the title of the goal
     * @param date the date for which the goal should be retrieved. Defaults to the [chosenDate].
     *
     * @return a Boolean representing the isDone value of the goal, or false if not found
     */
    suspend fun getGoalIsDone(title: String, date: String = chosenDate): Boolean {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("CustomGoals")
            .child(title)
            .child("isDone")
        val dataSnapshot = databaseReference.get().await()
        return dataSnapshot.getValue(Boolean::class.java) ?: false
    }

    /**
     * Adds water data to the database for a specific date.
     *
     * @param cupSize The size of the water cup in milliliters.
     * @param hydrationGoal The daily hydration goal in milliliters.
     * @param date The date for which the water data is added. Defaults to the [chosenDate].
     */
    fun addWater(cupSize: String, hydrationGoal: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("WaterData")
            databaseReference.child("hydrationGoal").setValue(hydrationGoal)
            databaseReference.child("cupSize").setValue(cupSize)
            databaseReference.child("waterDrunk").setValue(0).addOnSuccessListener {
                updateWaterData()
            }
        }
    }

    /**
     * Adds a new quote to the database for a specified date.
     *
     * @param quote the Quote object to be added
     * @param date the date for which the quote is being added. Defaults to the [chosenDate].
     */
    fun addQuote(quote: Quote, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("QuoteOfTheDay")
            databaseReference.child("quote").setValue(quote.text)
            databaseReference.child("isFavourite").setValue(quote.isFavourite)
            databaseReference.child("author").setValue(quote.author).addOnSuccessListener {
                // Update quotes data after quote is added
                updateQuotesData()
            }
        }
    }

    /**
     * Updates the favourite status of the Quote of the Day for a given date or the previously chosen date if not provided.
     *
     * @param date: Optional parameter representing the date for which the user's favourite
     * status of the Quote of the Day is being updated. If not provided, it defaults to a
     * previously chosen date.
     */
    fun changeQuotesFavouriteStatus(date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("QuoteOfTheDay")
            databaseReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val isFavourite =
                        currentData.child("isFavourite").getValue(Boolean::class.java) ?: false
                    currentData.child("isFavourite").value = !isFavourite
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (committed) {
                        updateQuotesData()
                    } else {
                        Log.e("Firebase", "Transaction failed", error?.toException())
                    }
                }
            })
        }
    }

    /**
     * Deletes the user's water intake data for a given date or the previously chosen date if not provided.
     *
     * @param date: Optional parameter representing the date for which the user's water intake data is being deleted. If not provided, it defaults to a previously chosen date.
     */
    fun deleteWater(date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("WaterData")
            databaseReference.removeValue().addOnSuccessListener {
                updateWaterData()
            }
        }
    }

    /**
     * Adds the user's sleep data for a given date or the previously chosen date if not provided.
     *
     * @param score: The sleep score for the night.
     * @param start: The time when the user started sleeping.
     * @param end: The time when the user woke up.
     * @param duration: The duration of the sleep.
     * @param description: Optional parameter for any additional notes or description related to the sleep.
     * @param date: Optional parameter representing the date for which the user's sleep data is being added. If not provided, it defaults to a previously chosen date.
     */
    fun addSleep(
        score: Int,
        start: String,
        end: String,
        duration: String,
        description: String,
        date: String = chosenDate
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("SleepData")
            databaseReference.child("sleepScore").setValue(score)
            databaseReference.child("sleepStart").setValue(start)
            databaseReference.child("sleepEnd").setValue(end)
            databaseReference.child("sleepDuration").setValue(duration)
            databaseReference.child("optionalDescription").setValue(description)
                .addOnSuccessListener {
                    updateSleepData()
                }
        }
    }

    /**
     * Updates the user's water counter for a given date or the previously chosen date if not provided.
     *
     * @param incVal: The value by which the water counter should be incremented.
     * @param date: Optional parameter representing the date for which the user's water counter is being updated. If not provided, it defaults to a previously chosen date.
     */
    fun updateWaterCounter(incVal: Int, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("WaterData")
            databaseReference.child("waterDrunk").setValue(_waterData.value!!.waterDrunk + incVal)
                .addOnSuccessListener {
                    updateWaterData()
                }
        }
    }

    /**
     * Adds the given username for the currently authenticated user to the database.
     *
     * @param userName The username to be added to the database.
     */
    fun addUserName(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
            databaseReference.child("UserName").setValue(userName)
        }
    }

    /**
     * Retrieves the username of the currently authenticated user from the database.
     *
     * @param callback A function that takes a single String argument representing the retrieved username.
     */
    fun getUserName(callback: (String) -> Unit) {
        val userRef = database.getReference("Users")
            .child(Authentication.userId)
            .child("UserName")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.value.toString()
                    callback(name)
                } else {
                    callback("")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * Adds the sign up date for the currently authenticated user to the database.
     *
     * @param date The date to be added to the database.
     */
    private fun addSignUpDate(date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
            databaseReference.child("SignUpDate").setValue(date)
        }
    }

    /**
     * Retrieves the sign up date of the currently authenticated user from the database.
     *
     * @param callback A function that takes a single String argument representing the retrieved sign up date.
     */
    fun displaySignUpDate(callback: (String) -> Unit) {
        val dateRef = database.getReference("Users")
            .child(Authentication.userId)
            .child("SignUpDate")

        dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val date = snapshot.value.toString()
                    callback(date)
                } else {
                    callback("")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * Retrieves the total number of calories for a specific food item consumed by the currently authenticated user on a specific date.
     *
     * @param foodName The name of the food item to retrieve the total calories for.
     * @param date The date on which the food item was consumed. Defaults to the chosen date.
     * @param callback A function that takes a single Int argument representing the retrieved total calories for the specified food item.
     */
    fun getFoodsTotalCalories(
        foodName: String,
        date: String = chosenDate,
        callback: (Int) -> Unit
    ) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfFood")
            .child(foodName)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val amount = snapshot.child("amount").value?.toString()?.toIntOrNull() ?: 0
                    val kcal = snapshot.child("kcal").value?.toString()?.toIntOrNull() ?: 0
                    callback(amount * kcal)
                } else {
                    callback(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun convertDurationToString(duration: Duration): String {
        val minutes = duration.toMinutes() % 60
        val hours = (duration.toHours() % 24).toInt()
        val days = duration.toDays().toInt()
        return String.format(
            "%02d:%02d:%02d",
            days,
            hours,
            minutes
        )
    }

    fun getAllTimeSleepData(callback: (DataSummary) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)

        var totalSleepDuration = Duration.ZERO
        var sleepCounter = 0
        var averageScore = 0

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val sleepSnapshot = dateSnapshot.child("SleepData")
                        if (sleepSnapshot.exists()) {
                            val sleepScore = sleepSnapshot.child("sleepScore").value
                            val sleepDuration = sleepSnapshot.child("sleepDuration").value
                            if (sleepDuration.toString().isNotEmpty() && sleepDuration != null) {
                                val parts = sleepDuration.toString().split(":")
                                val hours = parts[0].toLong()
                                val minutes = parts[1].toLong()
                                val duration = Duration.ofHours(hours).plusMinutes(minutes)
                                totalSleepDuration = totalSleepDuration.plus(duration)
                            }
                            if (sleepScore != null) {
                                sleepCounter++
                                averageScore += sleepScore.toString().toInt()
                            }
                        }
                    }
                    if (totalSleepDuration != Duration.ZERO && sleepCounter != 0 && averageScore != 0) {
                        callback(
                            DataSummary(
                                convertDurationToString(totalSleepDuration),
                                (averageScore / sleepCounter).toString()
                            )
                        )
                    } else {
                        callback(DataSummary("0", "0"))
                    }
                } else {
                    callback(DataSummary("0", "0"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun getAllTimeWaterData(callback: (DataSummary) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)

        var totalWaterDrunk = 0
        var waterEntryCounter = 0

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val waterSnapshot = dateSnapshot.child("WaterData")
                        if (waterSnapshot.exists()) {
                            val waterDrunk = waterSnapshot.child("waterDrunk").value
                            waterEntryCounter++
                            if (waterDrunk.toString().isNotEmpty() && waterDrunk != null) {
                                val amount = waterDrunk.toString().toInt()
                                totalWaterDrunk += amount
                            }
                        }
                    }
                }
                callback(DataSummary(totalWaterDrunk.toString(), waterEntryCounter.toString()))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun getAllTimeFoodData(callback: (DataSummary) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)

        var totalCalories = 0
        var dayEntryCounter = 0

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val foodListSnapshot = dateSnapshot.child("ListOfFood")
                        if (foodListSnapshot.exists()) {
                            for (foodSnapshot in foodListSnapshot.children) {
                                val amount =
                                    foodSnapshot.child("amount").value?.toString()?.toIntOrNull()
                                        ?: 0
                                val kcal =
                                    foodSnapshot.child("kcal").value?.toString()?.toIntOrNull() ?: 0
                                totalCalories += amount * kcal
                            }
                            dayEntryCounter++
                        }
                    }
                    if (totalCalories != 0 && dayEntryCounter != 0) {
                        callback(
                            DataSummary(
                                dayEntryCounter.toString(),
                                (totalCalories / dayEntryCounter).toString()
                            )
                        )
                    } else {
                        callback(DataSummary("0", "0"))
                    }
                } else {
                    callback(DataSummary("0", "0"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun getMoodAllTimeData(callback: (Map<Int, Int>) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)

        val moodCountMap = mutableMapOf<Int, Int>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val moodListSnapshot = dateSnapshot.child("ListOfMoods")
                        if (moodListSnapshot.exists()) {
                            for (moodSnapshot in moodListSnapshot.children) {
                                val moodType =
                                    moodSnapshot.child("emojiIndex").value.toString().toInt()
                                if (moodCountMap.containsKey(moodType)) {
                                    moodCountMap[moodType] = moodCountMap[moodType]!! + 1
                                } else {
                                    moodCountMap[moodType] = 1
                                }
                            }
                        }
                    }
                }
                callback(moodCountMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun getAllTimeQuoteData(callback: (DataSummary) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)

        var allQuotes = 0
        var totalFavouriteQuotes = 0

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val quoteSnapshot = dateSnapshot.child("QuoteOfTheDay")
                        if (quoteSnapshot.exists()) {
                            val isFavourite =
                                quoteSnapshot.child("isFavourite").getValue(Boolean::class.java)
                            if (isFavourite == true) {
                                totalFavouriteQuotes++
                            }
                            allQuotes++
                        }
                    }
                }
                callback(DataSummary(allQuotes.toString(), totalFavouriteQuotes.toString()))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun getAllTimeGoalData(callback: (DataSummary) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)

        var allGoals = 0
        var goalsAchieved = 0

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val goalListSnapshot = dateSnapshot.child("CustomGoals")
                        if (goalListSnapshot.exists()) {
                            allGoals += goalListSnapshot.children.count()
                            for (goalSnapshot in goalListSnapshot.children) {
                                val isAchieved =
                                    goalSnapshot.child("isDone").getValue(Boolean::class.java)
                                if (isAchieved == true) {
                                    goalsAchieved++
                                }
                            }
                        }
                    }
                }
                callback(DataSummary(goalsAchieved.toString(), allGoals.toString()))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }


    /**
     * Retrieves the total number of calories consumed by the currently authenticated user on a specific date.
     *
     * @param date The date to retrieve the total calories for. Defaults to the chosen date.
     * @param callback A function that takes a single Int argument representing the retrieved total calories for the specified date.
     */
    fun getTotalCaloriesForADay(date: String = chosenDate, callback: (Int) -> Unit) {
        val databaseReference = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfFood")
        var totalKcal = 0

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    var amount = foodSnapshot.child("amount").value
                    var kcal = foodSnapshot.child("kcal").value
                    if (amount != null) {
                        amount = amount.toString().toInt()
                    } else {
                        amount = 0
                    }
                    if (kcal != null) {
                        kcal = kcal.toString().toInt()
                    } else {
                        kcal = 0
                    }
                    totalKcal += amount * kcal
                }
                callback(totalKcal)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Increases the amount of a specific food item consumed by the currently authenticated user on a specific date by 1.
     *
     * @param foodName The name of the food item to increase the amount for.
     * @param date The date to increase the amount of the food item for. Defaults to the chosen date.
     */
    fun countUp(foodName: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfFood")
                .child(foodName)
            databaseReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val amount = currentData.child("amount").getValue(Int::class.java) ?: 0
                    currentData.child("amount").value = amount + 1
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (committed) {
                        // Update foodList after food is added
                        updateFoodData()
                    } else {
                        Log.e("Firebase", "Transaction failed", error?.toException())
                    }
                }
            })
        }
    }

    /**
     * Decreases the amount of a specific food item consumed by the currently authenticated user on a specific date by 1.
     *
     * @param foodName The name of the food item to increase the amount for.
     * @param date The date to increase the amount of the food item for. Defaults to the chosen date.
     */
    fun countDown(foodName: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfFood")
                .child(foodName)
            databaseReference.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val amount = currentData.child("amount").getValue(Int::class.java) ?: 0
                    if (amount == 1) {
                        return Transaction.abort()
                    }
                    currentData.child("amount").value = amount - 1
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (committed) {
                        // Update foodList after food is added
                        updateFoodData()
                    } else {
                        Log.e("Firebase", "Transaction failed", error?.toException())
                    }
                }
            })
        }
    }

    /**
     * Exports user data from Firebase database to a JSON file in the Downloads folder.
     */
    fun exportData(date: String = chosenDate) {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(Authentication.userId)
                .child(date)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val json = gson.toJson(dataSnapshot.value)
                try {
                    val downloadFolder =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val downloadFolderPath = downloadFolder.absolutePath
                    val currentDate = Utils.getCurrentDate()
                    val jsonFilePath = "$downloadFolderPath/data_$currentDate.txt"
                    val fileWriter = FileWriter(jsonFilePath)
                    fileWriter.write(json)
                    fileWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    /**
     * Authenticates user with provided email and password using Firebase authentication service.
     *
     * @param email User's email.
     * @param password User's password.
     * @param callback Callback function to be invoked upon completion of sign in attempt.
     *                 Receives an integer parameter representing the result of sign in attempt.
     */
    fun signInWithEmailAndPassword(email: String, password: String, callback: (Int) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // User signed in successfully
                val user = Firebase.auth.currentUser

                if (user != null) {
                    Authentication.userId = user.uid
                    Authentication.userEmail = user.email.toString()
                    // Check if user email is verified
                    if (!user.isEmailVerified) {
                        callback(Authentication.USER_IS_NOT_VERIFIED)
                        return@addOnSuccessListener
                    }
                }

                isLoggedIn.value = true
                callback(Authentication.LOGGED_IN_SUCCESSFULLY)
            }
            .addOnFailureListener { exception ->
                // User log in failed
                when {
                    exception.message?.contains("The password is invalid") == true -> {
                        callback(Authentication.PASSWORD_WRONG)
                    }
                    exception.message?.contains("The email address is badly formatted") == true -> {
                        callback(Authentication.EMAIL_WRONG_FORMAT)
                    }
                    exception.message?.contains("There is no user record") == true -> {
                        callback(Authentication.ACCOUNT_DOES_NOT_EXIST)
                    }
                    else -> {
                        callback(Authentication.OTHER)
                    }
                }
            }
    }

    /**
     * Registers a new user with provided email and password using Firebase authentication service.
     *
     * @param email User's email.
     * @param password User's password.
     * @param username User's username.
     * @param callback Callback function to be invoked upon completion of sign up attempt.
     *                 Receives an integer parameter representing the result of sign up attempt.
     */
    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        username: String,
        callback: (Int) -> Unit
    ) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User signed up successfully
                    val user = Firebase.auth.currentUser
                    if (user != null) {
                        Authentication.userId = user.uid
                        Authentication.userEmail = user.email.toString()
                        if (username.isNotEmpty()) {
                            addUserName(username)
                            addSignUpDate()
                        }
                    }
                    callback(Authentication.SIGNED_UP_SUCCESSFULLY)
                } else {
                    // User sign up failed
                    val exception = task.exception
                    if (exception != null) {
                        when (exception) {
                            is FirebaseAuthUserCollisionException -> callback(Authentication.USER_ALREADY_EXISTS)
                            else -> {
                                when {
                                    exception.message?.contains("The email address is badly formatted") == true -> callback(
                                        Authentication.EMAIL_WRONG_FORMAT
                                    )
                                    exception.message?.contains("The given password is invalid") == true -> callback(
                                        Authentication.PASSWORD_WRONG_FORMAT
                                    )
                                    else -> callback(Authentication.OTHER)
                                }
                            }
                        }
                    }
                }
            }
    }

    /**
     * Sends a verification email to the current user's email address using Firebase authentication service.
     *
     * @param context The context from which this function is called.
     */
    fun sendVerificationEmail(context: Context) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "Verification email sent. Please check your inbox.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Failed to send verification email.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        signOut()
    }

    /**
     * Sends a password reset email to the given email address using Firebase authentication service.
     *
     * @param email The email address for which the password reset email is to be sent.
     * @param context The context from which this function is called.
     */
    fun sendPasswordResetEmail(email: String, context: Context) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to send password reset email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * Signs out the current user from the app by calling Firebase authentication service's signOut() method.
     *
     * @param callback A callback function to be executed after the user has been signed out. Default value is an empty function.
     *                 The callback function takes a single Boolean parameter that indicates whether the sign-out was successful or not.
     *                 True indicates successful sign-out, false indicates failed sign-out.
     */
    fun signOut(callback: (Boolean) -> Unit = {}) {
        Firebase.auth.signOut()
        isLoggedIn.value = false
        Authentication.userId = ""
        callback(true)
    }
}