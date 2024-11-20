package com.akshat.findmytutor

class Chat {
    var senderId: String? = null
    var message: String? = null

    constructor()

    constructor(message:String?, senderId:String?){
        this.message = message
        this.senderId = senderId
    }
}