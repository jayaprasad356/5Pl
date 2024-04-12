package com.app.fivepl.model

class Transaction {
    var id: String? = null
    var type: String? = null
    var amount: String? = null
    var datetime: String? = null


    constructor(id: String?, type: String?, amount: String?, datetime: String?) {
        this.id = id
        this.type = type
        this.amount = amount
        this.datetime = datetime

    }


}