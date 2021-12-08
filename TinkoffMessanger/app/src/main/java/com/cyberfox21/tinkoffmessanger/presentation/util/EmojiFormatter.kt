package com.cyberfox21.tinkoffmessanger.presentation.util

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

object EmojiFormatter {

    private fun codeToEmoji(code: String): String {
        return String(Character.toChars(Integer.parseInt(code, 16)))
    }

    fun jsonToEmoji(jsonElement: com.google.gson.JsonElement): String {
        stringToEmoji(jsonElement.toString())
        return ""
    }

    fun stringToEmoji(emojiCode: String): String? {
        if (emojiCode == "zulip") return ""
        if (!emojiCode.contains("-")) {
            return codeToEmoji(emojiCode.replace("\"", ""))
        }
        return null
    }

    fun jsonObjectToReactionsList(jsonObject: com.google.gson.JsonObject): List<Reaction> {
        val reactionList = sortedSetOf(
            comparator = Comparator<Reaction> { o1, o2 ->
                (o1.reaction).compareTo(o2.reaction)
            })


        jsonObject.keySet().forEach { key ->
            val value = jsonObject.get(key)
            val emojiString = stringToEmoji(value.asString)
            if (emojiString != null) {
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