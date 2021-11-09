package com.cyberfox21.tinkoffmessanger.data.api.dto


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("members")
    val userDTOS: List<UserDTO>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("result")
    val result: String
) {
    data class UserDTO(
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("bot_owner_id")
        val botOwnerId: Int,
        @SerializedName("bot_type")
        val botType: Any,
        @SerializedName("date_joined")
        val dateJoined: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("is_active")
        val isActive: Boolean,
        @SerializedName("is_admin")
        val isAdmin: Boolean,
        @SerializedName("is_billing_admin")
        val isBillingAdmin: Boolean,
        @SerializedName("is_bot")
        val isBot: Boolean,
        @SerializedName("is_guest")
        val isGuest: Boolean,
        @SerializedName("is_owner")
        val isOwner: Boolean,
        @SerializedName("profile_data")
        val profileData: ProfileData,
        @SerializedName("role")
        val role: Int,
        @SerializedName("timezone")
        val timezone: String,
        @SerializedName("user_id")
        val userId: Int
    ) {
        data class ProfileData(
            @SerializedName("1")
            val x1: X1,
            @SerializedName("2")
            val x2: X2,
            @SerializedName("3")
            val x3: X3,
            @SerializedName("4")
            val x4: X4,
            @SerializedName("5")
            val x5: X5,
            @SerializedName("6")
            val x6: X6,
            @SerializedName("7")
            val x7: X7,
            @SerializedName("8")
            val x8: X8
        ) {
            data class X1(
                @SerializedName("rendered_value")
                val renderedValue: String,
                @SerializedName("value")
                val value: String
            )

            data class X2(
                @SerializedName("rendered_value")
                val renderedValue: String,
                @SerializedName("value")
                val value: String
            )

            data class X3(
                @SerializedName("rendered_value")
                val renderedValue: String,
                @SerializedName("value")
                val value: String
            )

            data class X4(
                @SerializedName("value")
                val value: String
            )

            data class X5(
                @SerializedName("value")
                val value: String
            )

            data class X6(
                @SerializedName("value")
                val value: String
            )

            data class X7(
                @SerializedName("value")
                val value: String
            )

            data class X8(
                @SerializedName("value")
                val value: String
            )
        }
    }
}