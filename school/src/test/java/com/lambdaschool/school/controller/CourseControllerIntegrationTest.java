package com.lambdaschool.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.school.model.Instructor;
import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.Student;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.number.OrderingComparison.lessThan;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerIntegrationTest
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void initialiseRestAssuredMockMvcWebApplicationContext()
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    //GET /courses/courses
    @Test
    public void whenMeasuredReponseTime()
    {
        given().when().get("/courses/courses").then().time(lessThan(5000L));
    }


    //    POST /courses/course/add
    @Test
    public void givenPostACourse() throws Exception
    {
        Instructor thisInstructor = new Instructor();
        String rest3Name = "Java 101";
        Course r5 = new Course(rest3Name, thisInstructor);
        r5.getStudents().add(new Student("Obaida"));

        ObjectMapper mapper = new ObjectMapper();
        String stringR3 = mapper.writeValueAsString(r5);

        given().contentType("application/json").body(stringR3).when().post("/courses/course/add").then().statusCode(201);
    }


    //    GET /restaurants/restaurant/{restaurantId}
    @Test
    public void givenFoundCourseId() throws Exception
    {
        long aCourse = 1;

        given().when().get("/courses/courses/" + aCourse).then().statusCode(200).and().body(containsString("Java 101"));
    }




}
