package com.sssakib.loadmoreonrecyclerview.model
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Call<List<DogBreed>?>
}