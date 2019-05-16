package cd.gamedesigner.vijey.philadelphiempjl.models

import android.os.Parcelable

@Parcelize
class User (val uid: String, val username: String): Parcelable {
    constructor(): this("","")
}