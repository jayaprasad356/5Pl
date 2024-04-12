package com.app.fivepl.model

class Recharge {


    var id: String? = null
    var user_id: String? = null
    var image: String? = null
    var recharge_amount: String? = null
    var status: String? = null
    var datetime: String? = null


    constructor(
        id: String?,
        user_id: String?,
        image: String?,
        recharge_amount: String?,
        status: String?,
        datetime: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.image = image
        this.recharge_amount = recharge_amount
        this.status = status
        this.datetime = datetime

    }


}