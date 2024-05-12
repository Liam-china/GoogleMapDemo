package com.veo.googlemapdemo.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager

object PermissionUtil {

    @JvmStatic
    fun hasLocationPermission(ctx: Activity): Boolean {
        return ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    fun requestLocationPermission(ctx: Activity, code: Int) {
        ctx.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), code)
    }

}