package com.sssakib.loadmoreonrecyclerview

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sssakib.loadmoreonrecyclerview.model.DogBreed
import com.sssakib.loadmoreonrecyclerview.model.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var recyclerViewAdapter: RecyclerViewAdapter? = null
    var rowsArrayList: ArrayList<String?> = ArrayList()
    var isLoading = false

    var dogList : ArrayList<DogBreed>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDogs()
        populateData()
        initAdapter()
        initScrollListener()


    }
    private fun getDogs(){
        val call: Call<List<DogBreed>?>? = RetrofitClient
            .instance
            ?.aPI
            ?.getDogs()
        call?.enqueue(object : Callback<List<DogBreed>?> {
            override fun onFailure(call: Call<List<DogBreed>?>?, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<DogBreed>?>?,
                response: Response<List<DogBreed>?>?
            ) {
                if (response!!.isSuccessful) {
                    dogList = response.body() as ArrayList<DogBreed>
                }
            }

        })

    }

    private fun populateData() {


        var i = 0
        while (i < 10) {
            rowsArrayList.add("Name : $i")
            i++
        }
    }

    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(rowsArrayList)
        recyclerView!!.adapter = recyclerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add(null)
        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                rowsArrayList.add("Item $currentSize")
                currentSize++
            }
            recyclerViewAdapter!!.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }


}