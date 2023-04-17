package com.example.project_g09.models
data class AllStatesResponse(
    val data:List<State>
){}

data class State(

    //val id:String,
    val url:String,
    val fullName:String,
    val description:String,
    val latitude:String,
    val longitude:String,
    val addresses:List<Addresses>,
    val images:List<Img>

){}

data class Addresses(
    val postalCode:String,
    val city:String,
    val stateCode:String,
    val line1:String,
    val type:String,

){}

data class Img(
    val credit:String,
    val title:String,
    val altText:String,
    val caption:String,
    val url:String
){}
