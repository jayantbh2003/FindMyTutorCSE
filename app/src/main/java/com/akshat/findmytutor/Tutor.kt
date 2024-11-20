package com.akshat.findmytutor


class Tutor {
    var name:String?=null
    var email:String?=null
    var uid:String?=null
    var subject:String?=null
    var userType: String? = null

    constructor()

    constructor(name:String,email:String,uid:String,subject: String){
        this.name=name
        this.email=email
        this.uid=uid
        this.subject=subject
        this.userType = "tutor"
    }

}