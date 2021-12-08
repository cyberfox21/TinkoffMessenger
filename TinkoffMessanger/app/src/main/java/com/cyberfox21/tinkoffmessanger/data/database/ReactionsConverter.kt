package com.cyberfox21.tinkoffmessanger.data.database

import androidx.room.TypeConverter
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ReactionsConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromReactionsList(reactions: List<Reaction>): String {
        val type: Type = object : TypeToken<List<Reaction>>() {}.type
        return gson.toJson(reactions, type)
    }

    @TypeConverter
    fun toReactionsList(reactionsString: String): List<Reaction> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Reaction>>() {}.type
        return gson.fromJson<List<Reaction>>(reactionsString, type)
    }

}
