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
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils
import java.io.FileWriter
import java.io.IOException

object Authentication {
    var userId = ""
    var userEmail = ""

    // Log in error codes
    const val LOGGED_IN_SUCCESSFULLY = 0
    const val PASSWORD_WRONG = 1
    const val ACCOUNT_DOES_NOT_EXIST = 2
    const val OTHER = 3

    // Sign up error codes
    const val SIGNED_UP_SUCCESSFULLY = 0
    const val USER_ALREADY_EXISTS = 4
    const val EMAIL_WRONG_FORMAT = 5
    const val PASSWORD_WRONG_FORMAT = 6
}

class FirebaseViewModel : ViewModel() {

    private val _foodList = MutableLiveData<List<Food>>()
    val foodList: LiveData<List<Food>> = _foodList

    private val _moodsList = MutableLiveData<List<Mood>>()
    val moodsList: LiveData<List<Mood>> = _moodsList

    private val _waterData = MutableLiveData<Water>()
    val waterData: LiveData<Water> = _waterData

    private val _goalList = MutableLiveData<List<Goal>>()
    val goalData: LiveData<List<Goal>> = _goalList

    private val _sleepHours = MutableLiveData<Sleep>()
    val sleepHours: LiveData<Sleep> = _sleepHours

    private val database = FirebaseDatabase.getInstance()

    private val isLoggedIn = mutableStateOf(false)

    private var chosenDate = DesiredDate.date

    // Subscribing to "DesiredDate" object date change event
    private fun registerDateChangeListener() {
        DesiredDate.dateChangeListeners.add(::fetchAllData)
    }

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

    private fun updateSleepData() {
        fetchSleepData(chosenDate) { sleeps ->
            _sleepHours.value = sleeps
        }
    }

    private fun updateAllData() {
        fetchFoodData(chosenDate) { foods ->
            _foodList.value = foods
        }
        fetchMoodData(chosenDate) { moods ->
            _moodsList.value = moods
        }
        fetchGoalData(chosenDate) { goals ->
            _goalList.value = goals
        }
        fetchWaterData(chosenDate) { water ->
            _waterData.value = water
        }
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
    }

    init {
        registerDateChangeListener()
        // Fetch App data
        fetchAllData()
    }

    fun fetchFoodData(date: String, callback: (List<Food>) -> Unit) {
        val foodRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfFood")

        val foods = mutableListOf<Food>()

        foodRef.addValueEventListener(object : ValueEventListener {
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

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun fetchMoodData(date: String, callback: (List<Mood>) -> Unit) {
        val moodRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("ListOfMoods")

        val moods = mutableListOf<Mood>()

        moodRef.addValueEventListener(object : ValueEventListener {
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
                    Log.d("FirebaseViewModel", "Description: $optionalDescription")
                }
                callback(moods)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun fetchGoalData(date: String, callback: (List<Goal>) -> Unit) {
        val goalRef = database.getReference("Users")
            .child(Authentication.userId)
            .child(date)
            .child("CustomGoals")
        val goals = mutableListOf<Goal>()

        goalRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (goalSnapshot in snapshot.children) {
                    val goalTitle = goalSnapshot.key
                    val isDone = goalSnapshot.child("isDone").value.toString().toBoolean()
                    val goal = Goal(title = goalTitle!!, achieved = isDone)
                    goals.add(goal)
                }
                Log.d("FirebaseViewModel", "Goal is: $goals")
                callback(goals)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun fetchWaterData(date: String, callback: (Water) -> Unit) {
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
                        val water = Water(
                            cupSize.toString().toInt(),
                            hydrationGoal.toString().toInt(),
                            waterDrunk.toString().toInt()
                        )
                        callback(water)
                    }
                } else {
                    callback(Water(0, 0))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun fetchSleepData(date: String, callback: (Sleep) -> Unit) {
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

    fun addFood(name: String, kcal: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfFood")
                .child(name)
            Log.d("Test", date)
            databaseReference.child("kcal").setValue(kcal)
            databaseReference.child("amount").setValue(1)
                .addOnSuccessListener {
                // Update foodList after food is deleted
                updateFoodData()
            }
        }
    }

    fun deleteFood(name: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfFood")
                .child(name)
            databaseReference.removeValue().addOnSuccessListener {
                // Update foodList after food is deleted
                updateFoodData()
            }
        }
    }

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
                    // Update foodList after food is deleted
                    updateMoodData()
                }
        }
    }


    fun deleteMood(time: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("ListOfMoods")
                .child(time)
            databaseReference.removeValue().addOnSuccessListener {
                // Update foodList after food is deleted
                updateMoodData()
            }
        }
    }

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

    fun addGoal(title: String, isDone: Boolean, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("CustomGoals")
                .child(title)
            databaseReference.child("isDone").setValue(isDone).addOnSuccessListener {
                // Update foodList after food is deleted
                updateGoalData()
            }
        }
    }

    fun deleteGoal(title: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("CustomGoals")
                .child(title)
            databaseReference.removeValue().addOnSuccessListener {
                // Update foodList after food is deleted
                updateGoalData()
            }
        }
    }

    fun deleteAllUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
            databaseReference.removeValue().addOnSuccessListener {
                // Update foodList after food is deleted
                updateAllData()
            }
        }
    }

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

    fun addWater(cupSize: String, hydrationGoal: String, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("WaterData")
            databaseReference.child("hydrationGoal").setValue(hydrationGoal)
            databaseReference.child("cupSize").setValue(cupSize)
            databaseReference.child("waterDrunk").setValue(0).addOnSuccessListener {
                // Update foodList after food is deleted
                updateWaterData()
            }
        }
    }

    fun deleteWater(date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("WaterData")
            databaseReference.removeValue().addOnSuccessListener {
                // Update foodList after food is deleted
                updateWaterData()
            }
        }
    }

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
            databaseReference.child("optionalDescription").setValue(description).addOnSuccessListener {
                // Update foodList after food is deleted
                updateSleepData()
            }
        }
    }

    fun updateWaterCounter(incVal: Int, date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("WaterData")
            databaseReference.child("waterDrunk").setValue(_waterData.value!!.waterDrunk + incVal).addOnSuccessListener {
                // Update foodList after food is deleted
                updateWaterData()
            }
        }
    }

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
                // Handle error here
            }
        })
    }

    fun addUserName(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
            databaseReference.child("UserName").setValue(userName)
        }
    }

    fun getFoodsTotalCalories(foodName: String, date: String = chosenDate, callback: (Int) -> Unit) {
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
                // Handle error here
            }
        })
    }

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

    fun exportData() {
        // Get reference to the Firebase Realtime Database node
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Authentication.userId)

// Attach a listener to the node to retrieve the data
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Convert the DataSnapshot to a JSON string
                val gson = GsonBuilder().setPrettyPrinting().create()
                val json = gson.toJson(dataSnapshot.value)
                // Write the JSON string to a file
                try {
                    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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
                // Handle error
            }
        })
    }

    fun signInWithEmailAndPassword(email: String, password: String, callback: (Int) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // User signed in successfully
                isLoggedIn.value = true
                val user = Firebase.auth.currentUser
                if (user != null) {
                    Authentication.userId = user.uid
                    Authentication.userEmail = user.email.toString()
                }
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

    fun signUpWithEmailAndPassword(email: String, password: String, callback: (Int) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User signed up successfully
                    val user = Firebase.auth.currentUser
                    if (user != null) {
                        Authentication.userId = user.uid
                        Authentication.userEmail = user.email.toString()
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

    fun sendPasswordResetEmail(email: String, context: Context) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signOut(callback: (Boolean) -> Unit) {
        Firebase.auth.signOut()
        isLoggedIn.value = false
        Authentication.userId = ""
        callback(true)
    }
}