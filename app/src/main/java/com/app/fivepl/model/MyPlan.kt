package com.app.fivepl.model

class MyPlan {
    var id: String? = null
    var plan_id: String? = null
    var products: String? = null
    var price: String? = null
    var daily_income: String? = null
    var monthly_income: String? = null
    var invite_bonus: String? = null
    var validity: String? = null
    var image: String? = null
    var unit: String? = null
    var daily_quantity: String? = null
    var income : String? = null
    var joined_date : String? = null
    var claim : String? = null
    var num_times : String? = null
    var description : String? = null
    var name : String? = null

    constructor(
        id: String?,
        plan_id: String?,
        products: String?,
        price: String?,
        daily_income: String?,
        monthly_income: String?,
        invite_bonus: String?,
        validity: String?,
        image: String?,
        unit: String?,
        daily_quantity: String?,
        income: String?,
        joined_date: String?,
        claim: String?,
        num_times: String?,
        description: String?,
        name: String?
    ) {
        this.id = id
        this.products = products
        this.price = price
        this.daily_income = daily_income
        this.monthly_income = monthly_income
        this.invite_bonus = invite_bonus
        this.validity = validity
        this.image = image
        this.unit = unit
        this.daily_quantity = daily_quantity
        this.income = income
        this.joined_date = joined_date
        this.claim = claim
        this.plan_id = plan_id
        this.num_times = num_times
        this.description = description
        this.name = name
    }
}