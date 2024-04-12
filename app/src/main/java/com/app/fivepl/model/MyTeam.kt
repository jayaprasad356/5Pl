package com.app.fivepl.model

class MyTeam {
    var id: String? = null
    var name: String? = null
    var mobile: String? = null
    var registered_date: String? = null
    var team_size: String? = null
    var total_assets: String? = null
    var valid_team: String? = null


    constructor(id: String?, name: String?, mobile: String?, registered_date: String?, team_size: String?, total_assets: String?, valid_team: String?) {
        this.id = id
        this.name = name
        this.mobile = mobile
        this.registered_date = registered_date
        this.team_size = team_size
        this.total_assets = total_assets
        this.valid_team = valid_team
    }


}