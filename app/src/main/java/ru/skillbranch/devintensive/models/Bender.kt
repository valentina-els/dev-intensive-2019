package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.models.Bender.Question.*

class Bender(var status: Status = Status.NORMAL, var question: Question = NAME) {

    fun askQuestion(): String = when (question){
        NAME -> NAME.question
        PROFESSION -> PROFESSION.question
        MATERIAL -> MATERIAL.question
        BDAY -> BDAY.question
        SERIAL -> SERIAL.question
        IDLE -> IDLE.question

    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>>{
        return if(question != IDLE) {
            if (question.getRegExp().matches(answer)) {
                if (question.answer.contains(answer)) {
                    question = question.nextQuestion()
                    "Отлично - ты справился\n${question.question}" to status.color
                } else {
                    status = status.nextStatus()
                    if (status == Status.NORMAL) {
                        "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                    } else {
                        "Это неправильный ответ\n${question.question}" to status.color
                    }
                }
            } else {
                question.getText() + "\n${question.question}" to status.color
            }
        }else{
            question.question to status.color
        }
    }

    enum class Status(val color : Triple<Int, Int, Int>){
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus() : Status {
            return if (this.ordinal < values().lastIndex){
                values()[this.ordinal + 1]
            }else{
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>){
        NAME("Как меня зовут?", listOf("Бендер", "Bender")){
            override fun nextQuestion(): Question = PROFESSION
            override fun getRegExp(): Regex = Regex("^[A-ZА-Я].*\$") //[a-z]*?$
            override fun getText(): String = "Имя должно начинаться с заглавной буквы"
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик","bender")){
            override fun nextQuestion(): Question = MATERIAL
            override fun getRegExp(): Regex = Regex("^[а-яa-z]\\w*\$") // *?$
            override fun getText(): String = "Профессия должна начинаться со строчной буквы"
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")){
            override fun nextQuestion(): Question = BDAY
            override fun getRegExp(): Regex = Regex("^[A-ZА-Я]*[a-zа-я]*?\$")  //^[A-ZА-Я]*[a-zа-я]*?\$
            override fun getText(): String = "Материал не должен содержать цифр"
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun nextQuestion(): Question = SERIAL
            override fun getRegExp(): Regex = Regex("^[0-9]+?\$")
            override fun getText(): String = "Год моего рождения должен содержать только цифры"
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun nextQuestion(): Question = IDLE
            override fun getRegExp(): Regex = Regex("^[0-9]{7}\$")
            override fun getText(): String = "Серийный номер содержит только цифры, и их 7"
        },
        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun nextQuestion(): Question = IDLE
            override fun getRegExp(): Regex = Regex(".*")
            override fun getText(): String = ""
        };

        abstract fun nextQuestion() : Question

        abstract fun getRegExp() : Regex
        abstract fun getText(): String
    }

}