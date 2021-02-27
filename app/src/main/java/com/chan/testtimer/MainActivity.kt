package com.chan.testtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {


    private var running  = false
    private var pauseTime : Long = 0
    private var lastVal = ""
    private var intervalSet = 1
    set(value) {
        if(value <-10){
            field = -10
            return
        }
        if(value> 10){
            field = 10
            return
        }
        field = value
    }
    private var currentInterval = 1
        set(value) {
            if(value <-10){
                field = -10
                return
            }
            if(value> 10){
                field = 10
                return
            }
            if(value == 0) return
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {chrono->

            val chronoBase = Utils.getConvertedTime(chronometer.base)


            if(chrono.isCountDown && chronoBase == "00:00:00"){
                zeroHit()

                return@OnChronometerTickListener
            }

            if(currentInterval>1){
                currentInterval--
                chrono.text = lastVal
            }else{
                chrono.text = chronoBase
                lastVal = chronoBase
                currentInterval = abs(intervalSet)
            }


        }

        buttonPlayPause.setOnClickListener {
            startChronometer()
        }

        addButton.setOnClickListener {
            intervalSet++
            if(intervalSet == 0)
                intervalSet = 1
            currentInterval = abs(intervalSet)

            pauseChronometer()

            if(intervalSet>0){
                textView.text = "Increment set to $intervalSet"
                chronometer.isCountDown = false
            }else{
                textView.text = "Decrement set to $intervalSet"
            }

            startChronometer()
        }

        removeButton.setOnClickListener {
            --intervalSet
            if(intervalSet == 0)
                intervalSet = -1
            currentInterval = abs(intervalSet)


            pauseChronometer()

            if(intervalSet<0) {
                chronometer.isCountDown = true
                textView.text = "Decrement set to $intervalSet"
            }else{
                textView.text ="Increment set to $intervalSet"
            }

            startChronometer()
        }

        buttonStop.setOnClickListener {
            resetTimer()
        }
    }

    private fun zeroHit() {
        resetTimer()
        Toast.makeText(this, "Can't go in negative time so returning to default increment",
        Toast.LENGTH_LONG).show()
        chronometer.isCountDown = false
    }

    private fun resetTimer(){
        intervalSet = 1
        currentInterval = intervalSet
        textView.text = "Increment set to $intervalSet"
        chronometer.stop()
        pauseTime = 0
        running = false
        chronometer.text = "00:00:00"
        buttonPlayPause.text = "play"
    }

    private fun startChronometer(){
        if(!running){
            if(chronometer.isCountDown){

                chronometer.base = SystemClock.elapsedRealtime() + abs(pauseTime)
            }
            else chronometer.base = SystemClock.elapsedRealtime() - abs(pauseTime)
            Log.i("CHRONOS_PLAY", "${chronometer.base} = ${SystemClock.elapsedRealtime()} - $pauseTime")
            chronometer.start()
            running = true
            buttonPlayPause.text = "Pause"
        }else{
          pauseChronometer()
        }
    }

    private fun pauseChronometer(){
        chronometer.stop()
        pauseTime = SystemClock.elapsedRealtime() - chronometer.base
        Log.i("CHRONOS_PAUSE", "Real time - base time : ${SystemClock.elapsedRealtime() }" +
                " - ${chronometer.base} = $pauseTime")
        running = false
        buttonPlayPause.text = "Play"
    }

}