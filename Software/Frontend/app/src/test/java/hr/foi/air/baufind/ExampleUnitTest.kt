package hr.foi.air.baufind

import hr.foi.air.baufind.ws.model.Skill
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
     fun getAllSkills (){
        val skill: MutableList<String> = mutableListOf()
        var skills = listOf(1,2,3)
        var _skills = listOf(
            Skill(1, "Vodoinstalater"),
            Skill(2, "Električar"),
            Skill(3, "Moler"),
            Skill(4, "Stolar"),
            Skill(5, "Keramičar")
        )
        if (_skills != null) {
            for (i in _skills){
                if(skills.contains(i.id)){
                    skill +=i.title
                }
            }
        }
        val result = skill.contains("Vodoinstalater") && skill.contains("Električar") && skill.contains("Moler")
        Assert.assertTrue(result)
    }
}