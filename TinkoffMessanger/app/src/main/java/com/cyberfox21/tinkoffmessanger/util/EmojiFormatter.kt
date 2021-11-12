package com.cyberfox21.tinkoffmessanger.util

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

object EmojiFormatter {

    private fun codeToEmoji(code: String): String {
        return String(Character.toChars(Integer.parseInt(code, 16)))
    }

    fun jsonToEmoji(jsonElement: com.google.gson.JsonElement): String {
        stringToEmoji(jsonElement.toString())
        return ""
    }

    fun stringToEmoji(emojiCode: String): String {
        if (!emojiCode.contains("-")) {
            return codeToEmoji(emojiCode.replace("\"", ""))
        }
        return emojiCode
    }

    fun jsonObjectToReactionsList(jsonObject: com.google.gson.JsonObject): List<Reaction> {
        val reactionList = sortedSetOf(
            comparator = Comparator<Reaction> { o1, o2 ->
                (o1.name).compareTo(o2.name)
            })


        jsonObject.keySet().forEach { key ->
            val value = jsonObject.get(key)
            val emojiString = stringToEmoji(value.asString)
            if (emojiString.isNotEmpty()) {
                reactionList.add(
                    Reaction(
                        reaction = emojiString,
                        name = key,
                        userId = Reaction.UNDEFINED_ID
                    )
                )
            }
        }
        return reactionList.toList()
    }
}