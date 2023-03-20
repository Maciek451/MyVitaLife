package uk.ac.aber.dcs.cs39440.myvitalife.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs39440.myvitalife.model.Food
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

class FirebaseViewModel : ViewModel() {

    private val _foodList = MutableLiveData<List<Food>>()
    val foodList: LiveData<List<Food>> = _foodList

    private val _waterLevel = MutableLiveData<Int>()
    val waterLevel: LiveData<Int> = _waterLevel

    private val _goal = MutableLiveData<String>()
    val goal: LiveData<String> = _goal

    private val _sleepHours = MutableLiveData<Int>()
    val sleepHours: LiveData<Int> = _sleepHours

    private val database = FirebaseDatabase.getInstance()

    init {
        // Fetch Food data
        fetchFoodData()
        fetchWaterData()
        fetchGoalData()

//        // Fetch Water data
//        val waterRef = database.getReference("Water")
//        waterRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val waterLevel = snapshot.value.toString().toInt()
//                _waterLevel.value = waterLevel
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
//            }
//        })
//
//        // Fetch Goal data
//        val goalRef = database.getReference("Goal")
//        goalRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val goal = snapshot.value.toString()
//                _goal.value = goal
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseViewModel", "Failed to read value.", error.toException())
//            }
//        })
//
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

    private fun fetchFoodData(){
        val foodRef =
            database.getReference(Utils.getCurrentDate())
                .child("ListOfFood")

        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foods = mutableListOf<Food>()
                var calories = 0
                for (foodSnapshot in snapshot.children) {
                    val foodName = foodSnapshot.key
                    val kcal = foodSnapshot.child("kcal").value.toString().toInt()
                    foods.add(Food(name = foodName!!, kcal = kcal))
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

    private fun fetchWaterData(){

    }

    private fun fetchGoalData(){

    }

    fun addFood(name: String, kcal: String){
        viewModelScope.launch(Dispatchers.IO) {
            val databaseReference = database.getReference(Utils.getCurrentDate())
                    .child("ListOfFood").child(name)
            databaseReference.child("kcal").setValue(kcal)
        }
    }
}