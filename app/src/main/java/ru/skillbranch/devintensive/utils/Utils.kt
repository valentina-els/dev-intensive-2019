package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName : String?) : Pair<String?, String?>{
        val parts : List<String>? = fullName?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return (if (firstName != "" && firstName != " ") firstName else null) to (if (lastName != "" && lastName != " ") lastName else null)
    }

    fun transliteration(payload:String, divider:String = " "): String {
        var result = ""
        for (i in payload.indices){
            when (payload.get(i)) {
                'а' -> result += ("a") //result.plus("a")
                'А' -> result += ("A")
                'б' -> result += ("b")
                'Б' -> result += ("B")
                'в' -> result += ("v")
                'В' -> result += ("V")
                'г' -> result += ("g")
                'Г' -> result += ("G")
                'д' -> result += ( "d")
                'Д' -> result += ( "D")
                'е', 'ё' -> result += ("e")
                'Е', 'Ё' -> result += ("E")
                'ж' -> result += ("zh")
                'Ж' -> result += ("Zh")
                'з' -> result += ( "z")
                'З' -> result += ( "Z")
                'и', 'й' -> result += ( "i")
                'И', 'Й' -> result += ( "I")
                'к' -> result += ( "k")
                'К' -> result += ( "K")
                'л' -> result += ( "l")
                'Л' -> result += ( "L")
                'м' -> result += ( "m")
                'М' -> result += ( "M")
                'н' -> result += ( "n")
                'Н' -> result += ( "N")
                'о' -> result += ( "o")
                'О' -> result += ( "O")
                'п' -> result += ( "p")
                'П' -> result += ( "P")
                'р' -> result += ("r")
                'Р' -> result += ("R")
                'с' -> result += ("s")
                'С' -> result += ("S")
                'т' -> result += ( "t")
                'Т' -> result += ( "T")
                'у' -> result += ( "u")
                'У' -> result += ( "U")
                'ф' -> result += ( "f")
                'Ф' -> result += ( "F")
                'х' -> result += ( "h")
                'Х' -> result += ( "H")
                'ц' -> result += ( "c")
                'Ц' -> result += ( "C")
                'ч' -> result += ( "ch")
                'Ч' -> result += ( "Ch")
                'ш', 'щ' -> result += ( "sh")
                'Ш', 'Щ' -> result += ( "Sh")
                'ъ', 'ь'-> result += ( "")
                'ы' -> result += ("i")
                'э'-> result += ( "e")
                'Э'-> result += ( "E")
                'ю' -> result += ( "yu")
                'Ю' -> result += ( "Yu")
                'я' -> result += ( "ya")
                'Я' -> result += ( "Ya")
                ' ' -> result += divider
                else -> result += (payload.get(i))
            }
        }
        println("result $result")
        return result
    }

    fun toInitials(firstName: String?, lastName: String?): String {
        return "${if (firstName != "" && firstName != " " && firstName != null) firstName.getOrNull(0)?.toUpperCase() else null}${if (lastName != "" && lastName != " " && lastName != null) lastName.getOrNull(0)?.toUpperCase() else ""}"
    }
}

