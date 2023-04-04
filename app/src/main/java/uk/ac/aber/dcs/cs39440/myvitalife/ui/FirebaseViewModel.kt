package uk.ac.aber.dcs.cs39440.myvitalife.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs39440.myvitalife.model.*
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

object Authentication {
    var userId = ""
    var userEmail = ""

    // Log in error codes
    const val LOGGED_IN_SUCCESSFULLY = 0
    const val PASSWORD_WRONG = 1
    const val ACCOUNT_DOES_NOT_EXIST = 2
    const val OTHER = 3

    // Sign in error codes
    const val SIGNED_IN_SUCCESSFULLY = 0
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
                var calories = 0
                for (foodSnapshot in snapshot.children) {
                    val foodName = foodSnapshot.key
                    val kcal = foodSnapshot.child("kcal").value.toString().toInt()
                    foods.add(
                        Food(
                            name = foodName!!,
                            kcal = kcal
                        )
                    )
                    calories += kcal
                }
                Log.d("FirebaseViewModel", "Total calories: $calories")
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
            databaseReference.child("kcal").setValue(kcal).addOnSuccessListener {
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

    fun deleteSleep(date: String = chosenDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference("Users")
                .child(Authentication.userId)
                .child(date)
                .child("SleepData")
            databaseReference.removeValue().addOnSuccessListener {
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


    fun logInWithEmailAndPassword(email: String, password: String, callback: (Int) -> Unit) {
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
                    callback(Authentication.SIGNED_IN_SUCCESSFULLY)
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

    fun signOut(callback: (Boolean) -> Unit) {
        Firebase.auth.signOut()
        isLoggedIn.value = false
        Authentication.userId = ""
        callback(true)
    }
}