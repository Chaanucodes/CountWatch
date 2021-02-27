package com.chan.testtimer

import android.os.SystemClock
import java.lang.Math.abs

object Utils{
    fun getConvertedTime(chronoBaseTime : Long) : String{
        var time =
                SystemClock.elapsedRealtime() - chronoBaseTime
        var h = (time / 3600000).toInt()
        var m = (time - h * 3600000).toInt() / 60000
        var s = (time - h * 3600000 - m * 60000).toInt() / 1000
        h = abs(h)
        m = abs(m)
        s = abs(s)
        val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s

        return t
    }
}