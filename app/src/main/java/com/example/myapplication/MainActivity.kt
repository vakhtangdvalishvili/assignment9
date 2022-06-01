package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.myapplication.dbactivities.Delete
import com.example.myapplication.dbactivities.Insert

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val insert: OneTimeWorkRequest = OneTimeWorkRequestBuilder<Insert>().build()
        val delete: OneTimeWorkRequest = OneTimeWorkRequestBuilder<Delete>().build()

        WorkManager.getInstance(this)
            .beginWith(insert)
            .then(delete)
            .enqueue()

        recyclerView = findViewById(R.id.recyclerView)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(insert.id).observe(

            this, Observer {
                if (it != null && it.state.isFinished){
                    val resources = App.instance.db.getResourceDao().selectAll()

                    recyclerView.adapter = Adapter(resources)
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                }
            }
        )



    }
}