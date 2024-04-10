package com.aslanjavasky.springbackend.ServerOfVirtualBank.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CurrencyConversionUtils {

    @Value("\${currency.conversion.galleons-to-sickles-ratio}")
    lateinit var galleonsToSicklesRatio: String

    @Value("\${currency.conversion.sickles-to-knuts-ratio}")
    lateinit var sicklesToKnutsRatio: String

    fun convertGalleonsToSickles(galleons: Int): Int {
        return galleons * galleonsToSicklesRatio.toInt()
    }

    fun convertSicklesToGalleons(sickles: Int): Int {
        return sickles / galleonsToSicklesRatio.toInt()
    }

    fun convertSicklesToKnuts(sickles: Int): Int {
        return sickles * sicklesToKnutsRatio.toInt()
    }

    fun convertKnutsToSickles(knuts: Int): Int {
        return knuts / sicklesToKnutsRatio.toInt()
    }

    fun convertGalleonsToKnuts(galleons: Int): Int {
        return convertSicklesToKnuts(convertGalleonsToSickles(galleons))
    }

    fun convertKnutsToGalleons(amountKnuts: Int): Int {
        return convertSicklesToGalleons(convertKnutsToSickles(amountKnuts))
    }

}