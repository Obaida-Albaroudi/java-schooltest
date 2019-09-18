package com.lambdaschool.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.Instructor;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.CourseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// mocking service to test controller

@RunWith(SpringRunner.class)
@WebMvcTest(value = CourseController.class, secure = false)
public class RestaurantControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private List<Course> courseList;

    @Before
    public void setUp() throws Exception
    {
        courseList = new ArrayList<>();

        Instructor InstType1 = new Instructor("John");
        InstType1.setInstructid(1);
        Instructor InstType2 = new Instructor("Jack");
        InstType2.setInstructid(2);
        Instructor InstType3 = new Instructor("Joe");
        InstType3.setInstructid(3);


        // Restaurant String name, String address, String city, String state, String telephone
        String rest1Name = "Java 101";
        Course r1 = new Course(rest1Name);
        r1.setCourseid(1);

        r1.setInstructor(InstType1);
        r1.getStudents().add(new Student("Obaida"));
        r1.getStudents().get(0).setStudid(20);
        r1.getStudents().add(new Student("Brandon"));
        r1.getStudents().get(1).setStudid(21);
        r1.getStudents().add(new Student("Isaiah"));
        r1.getStudents().get(2).setStudid(22);
        r1.getStudents().add(new Student("Adetunji"));
        r1.getStudents().get(3).setStudid(23);
        r1.getStudents().add(new Student("Timoth"));
        r1.getStudents().get(4).setStudid(24);

        courseList.add(r1);

        String rest2Name = "Java 102";
        Course r2 = new Course(rest2Name);
        r2.setCourseid(2);

        r2.setInstructor(InstType2);
        r2.getStudents().add(new Student("Samantha"));
        r2.getStudents().get(5).setStudid(25);
        r2.getStudents().add(new Student("Jennifer"));
        r2.getStudents().get(6).setStudid(26);

        courseList.add(r2);

        String rest3Name = "Java 103";
        Course r3 = new Course(rest3Name);
        r3.setCourseid(3);

        r3.setInstructor(InstType3);
        r3.getStudents().add(new Student("Haley"));
        r3.getStudents().get(7).setStudid(27);
        r3.getStudents().add(new Student("Bailey"));
        r3.getStudents().get(8).setStudid(28);
        r3.getStudents().add(new Student("Kaley"));
        r3.getStudents().get(9).setStudid(29);

        courseList.add(r3);

    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listAllCourses() throws Exception
    {
        String apiUrl = "/courses/courses";

        Mockito.when(courseService.findAll()).thenReturn((ArrayList<Course>) courseList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        // the following actually performs a real controller call
        MvcResult r = mockMvc.perform(rb).andReturn(); // this could throw an exception
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(courseList);

        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void getCourseByIdFound() throws Exception
    {
        String apiUrl = "/courses/courses/3";

        Mockito.when(courseService.findCourseById(3)).thenReturn(courseList.get(3));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn(); // this could throw an exception
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(courseList.get(2));

        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void getRestaurantByIdNotFound() throws Exception
    {
        String apiUrl = "/courses/courses/100";

        Mockito.when(courseService.findCourseById(100)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn(); // this could throw an exception
        String tr = r.getResponse().getContentAsString();

        String er = "";

        assertEquals("Rest API Returns List", er, tr);
    }



    @Test
    public void addNewCourse() throws Exception
    {
        String apiUrl = "//courses/course/add";

        // build a course
        Instructor thisInstructor = new Instructor("Sally");
        String rest3Name = "Java 104";
        Course r4 = new Course(rest3Name,
                thisInstructor);
        r4.setCourseid(100);
        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(r4);

        Mockito.when(courseService.save(any(Course.class))).thenReturn(r4);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(restaurantString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void deleteCourseById() throws Exception
    {
        String apiUrl = "/courses/courses/{courseid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}