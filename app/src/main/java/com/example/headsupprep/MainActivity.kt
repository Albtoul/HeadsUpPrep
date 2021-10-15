package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
   // private lateinit var rvMain: RecyclerView
    //private lateinit var rvAdapter: RV
   lateinit var txview: TextView
    val Url = "https://dojo-recipes.herokuapp.com/celebrities/"
    private val apiInterface by lazy { APIClient().getClient().create(APIInterface::class.java) }

    private lateinit var progressDialog: ProgressDialog

    private lateinit var btAdd: Button
    private lateinit var etCelebrity: EditText
    private lateinit var btDetails: Button

   private lateinit var celebrities: ArrayList<Celebrity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    /*    celebrities = arrayListOf()
        rvMain = findViewById(R.id.rvMain)
        rvAdapter = RV(celebrities)
        rvMain.adapter = rvAdapter
        rvMain.layoutManager = LinearLayoutManager(this)*/
        txview=findViewById(R.id.textView)
        btAdd = findViewById(R.id.btAdd)
        btAdd.setOnClickListener {
            intent = Intent(applicationContext, MainActivity2::class.java)
            val celebrityNames = arrayListOf<String>()
            for(c in celebrities){
                celebrityNames.add(c.name.lowercase())
            }
            intent.putExtra("celebrityNames", celebrityNames)
            startActivity(intent)
        }
        etCelebrity = findViewById(R.id.etCelebrity)
        btDetails = findViewById(R.id.btDetails)
        btDetails.setOnClickListener {
            Api()
            if(etCelebrity.text.isNotEmpty()){
                updateCelebrity()
            }else{
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()

        getCelebrities()
    }

    private fun getCelebrities(){
        apiInterface.getCelebrities().enqueue(object: Callback<ArrayList<Celebrity>>{
            override fun onResponse(
                call: Call<ArrayList<Celebrity>>,
                response: Response<ArrayList<Celebrity>>
            ) {
                progressDialog.dismiss()
                celebrities = response.body()!!
               // rvAdapter.update(celebrities)

            }

            override fun onFailure(call: Call<ArrayList<Celebrity>>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@MainActivity, "Unable to get data", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateCelebrity(){
        var celebrityID = 0
        for(celebrity in celebrities){
            if(etCelebrity.text.toString().capitalize() == celebrity.name){
                celebrityID = celebrity.pk

                intent = Intent(applicationContext, MainActivity3::class.java)
                intent.putExtra("celebrityID", celebrityID)
                startActivity(intent)
            }else{
                Toast.makeText(this, "${etCelebrity.text.toString().capitalize()} not found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun Api()
    {

        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                getCelebrities()

            }.await()

            //if (data.isNotEmpty())
            //{


            //}

        }

    }


    private suspend fun update(data: String)
    {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val ob = jsonObject.getJSONObject("ob")
            val pk = ob.getInt("pk")
            val name = ob.getString("name")
            val taboo1 = ob.getString("taboo1")
            val taboo2 = ob.getString("taboo2")
            val taboo3 = ob.getString("taboo3")

            txview.text= name
            txview.text= taboo1
            txview.text= taboo3
            txview.text= taboo3

        }

    }





}