package net.coblos.moodly.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSenderEntity(sender: SenderEntity?): String? {
        return gson.toJson(sender)
    }

    @TypeConverter
    fun toSenderEntity(senderString: String?): SenderEntity? {
        return gson.fromJson(senderString, SenderEntity::class.java)
    }
}
