package cn.blogss.kotlin

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author: Little Bei
 * @Date: 2022/5/18
 */
class ExtensionsKtTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addEnthusiasm() {
        println("Madrigal has left the building".addEnthusiasm())
    }

    @Test
    fun easyPrint() {
        "Madrigal has left the building".addEnthusiasm().easyPrint()
        42.easyPrint()
    }

    @Test
    fun easPrintLink() {
        "Madrigal has left the building".easyPrintLink().addEnthusiasm(3).easyPrintLink()
        42.easyPrintLink()
    }

    @Test
    fun getNumVowels() {
        "How many vowels ?".numVowels.easyPrint()
    }
}