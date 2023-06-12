package edu.skku.cs.Week10

data class DataModel(var page : Int ?= null, var data: ArrayList<User>){
    data class User(var id: Int ?= null, var last_name: String ?= null)
}
data class Register(var email: String ?= null, var password: String ?= null)
data class RegisterResponse(var id: String ?= null, var createdAt : String ?= null)