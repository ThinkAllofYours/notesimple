package com.example.notesimple

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class NotesimpleApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun testHelloWorldOutput() {
		val outputStream = ByteArrayOutputStream()
		val originalOut = System.out
		System.setOut(PrintStream(outputStream))

		main(arrayOf())

		System.setOut(originalOut)
		val output = outputStream.toString().trim()
		assertTrue(output.contains("hello world"))
	}

}
