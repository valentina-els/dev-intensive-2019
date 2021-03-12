package ru.skillbranch.devintensive.extensions

fun String.truncate(value:Int = 16) : String{
    var result = this

    if(result.length > value){
        result = result.substring(0, value)

        if(result.get(result.length - 1) == ' '){
            result = result.substring(0, result.length-1)
        }
        result += "..."
    }

    return result
}

fun String.stripHtml() : String{
    var result = this

    result = Regex("[&\'\"]").replace(result, "")
    result = Regex("</?.+?>").replace(result, "")
    result = Regex("[\\s]+").replace(result, " ")

    return result
}
