package com.example.notesimple

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotesimpleApplication

fun main(args: Array<String>) {
	println("hello world")
	runApplication<NotesimpleApplication>(*args)
}
