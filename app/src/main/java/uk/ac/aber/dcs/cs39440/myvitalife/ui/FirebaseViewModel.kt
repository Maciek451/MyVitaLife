package uk.ac.aber.dcs.cs39440.myvitalife.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.aber.dcs.cs39440.myvitalife.model.Food
import uk.ac.aber.dcs.cs39440.myvitalife.model.Goal
import uk.ac.aber.dcs.cs39440.myvitalife.model.Mood
import uk.ac.aber.dcs.cs39440.myvitalife.model.Water
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

class FirebaseViewModel : ViewModel() {

    private val _foodList = MutableLiveData<List<Food>>()
    val foodList: LiveData<List<Food>> = _foodList

    private val _moodsList = MutableLiveData<List<Mood>>()
    val moodsList: LiveData<List<Mood>> = _moodsList

    private val _waterData = MutableLiveData<Water>()
    val waterData: LiveData<Water> = _waterData

    private val _goalList = MutableLiveData<List<Goal>>()
    val goalData: LiveData<List<Goal>> = _goalList

    private val _sleepHours = MutableLiveData<Int>()
    val sleepHours: LiveData<Int> = _sleepHours

    private val database = FirebaseDatabase.getInstance()

    private val isLoggedIn = mutableStateOf(false)

    init {
        // Fetch App data
        fetchFoodData()
        fetchWaterData()
        fetchGoalData()
        fetchMoodData()

//        // Fetch Sleep data
//        val sleepRef = database.getReference("Sleep")
//        sleepRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val sleepHours = snapshot.value.toString().toInt()
//                _sleepHours.value = sleepHours
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
//            }
//        })
    }

    private fun fetchFoodData() {
        val foodRef = database.getReference(Utils.getCurrentDate())
            .child("ListOfFood")

        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foods = mutableListOf<Food>()
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
                _foodList.value = foods
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchMoodData() {
        val foodRef = database.getReference(Utils.getCurrentDate())
            .child("ListOfMoods")

        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moods = mutableListOf<Mood>()
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
                _moodsList.value = moods
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchGoalData() {
        val goalRef = database.getReference(Utils.getCurrentDate())
            .child("CustomGoals")

        goalRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val goals = mutableListOf<Goal>()
                for (goalSnapshot in snapshot.children) {
                    val goalTitle = goalSnapshot.key
                    val isDone = goalSnapshot.child("isDone").value.toString().toBoolean()
                    val goal = Goal(title = goalTitle!!, achieved = isDone)
                    goals.add(goal)
                }
                Log.d("FirebaseViewModel", "Goal is: $goals")
                _goalList.value = goals
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchWaterData() {
        val waterRef = database.getReference(Utils.getCurrentDate())
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
                        _waterData.value = water
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
            }
        })
    }

    fun addFood(name: String, kcal: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("ListOfFood")
                .child(name)
            databaseReference.child("kcal").setValue(kcal)
        }
    }

    fun deleteFood(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("ListOfFood")
                .child(name)
            databaseReference.removeValue()
        }
    }

    fun addMood(type: Int, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("ListOfMoods")
                .child(Utils.getCurrentTime())
            databaseReference.child("emojiIndex").setValue(type.toString())
            databaseReference.child("optionalDescription").setValue(description)
        }
    }

    fun deleteMood(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("ListOfMoods")
                .child(time)
            Log.d("TEST", time)
            databaseReference.removeValue()
        }
    }

    fun addGoal(title: String, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("CustomGoals")
                .child(title)
            databaseReference.child("isDone").setValue(isDone)
        }
    }

    fun deleteGoal(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("CustomGoals")
                .child(title)
            databaseReference.removeValue()
        }
    }

    suspend fun getGoalIsDone(title: String): Boolean {
        val databaseReference = database.getReference(Utils.getCurrentDate())
            .child("CustomGoals")
            .child(title)
            .child("isDone")
        val dataSnapshot = databaseReference.get().await()
        return dataSnapshot.getValue(Boolean::class.java) ?: false
    }

    fun addWater(cupSize: String, hydrationGoal: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("WaterData")
            databaseReference.child("hydrationGoal").setValue(hydrationGoal)
            databaseReference.child("cupSize").setValue(cupSize)
            databaseReference.child("waterDrunk").setValue(0)
        }
    }

    fun deleteWater() {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("WaterData")
            databaseReference.removeValue()
        }
    }

    fun updateWaterCounter(incVal: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                .child("WaterData")
            databaseReference.child("waterDrunk").setValue(_waterData.value!!.waterDrunk + incVal)
        }
    }

    fun signInWithEmail(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                isLoggedIn.value = true
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

}