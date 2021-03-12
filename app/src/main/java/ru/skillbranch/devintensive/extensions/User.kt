package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

fun User.toUserView() : UserView {
    val nickname = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status = if(lastVisit == null) "Еще ни разу не был" else if (isOnline) "online" else "Последний раз был ${lastVisit.humanizeDiff(lastVisit)}"

        return UserView(
            id,
            fullName = "$firstName $lastName",
            avatar = avatar,
            nickName = nickname,
            initials = initials,
            status = status)
}

private fun Date?.humanizeDiff(date: Date? = Date()): String {
    var result = ""
        if (date != null) {
        val milliseconds = Date().time -  date.time
        result = if (milliseconds != 0.toLong())
            Date().difference(milliseconds)
            else "сейчас"
    }
    return result
}

