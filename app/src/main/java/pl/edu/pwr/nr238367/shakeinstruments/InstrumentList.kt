package pl.edu.pwr.nr238367.shakeinstruments

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader


object InstrumentList {
    var instrumentList: List<Instrument> = emptyList()
    private set

    fun initializeList(jsonInputStream: InputStream) {
        if (instrumentList.isEmpty()) {
            instrumentList = try {
                val jsonFileStream = InputStreamReader(jsonInputStream)
                val instrumentsType = object : TypeToken<List<Instrument>>() {}.type
                Gson().fromJson(jsonFileStream, instrumentsType)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}