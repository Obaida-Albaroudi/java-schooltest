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
public class CourseControllerTest
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

        Instructor Sally = new Instructor("'Sally'");
        Sally.setInstructid(1);
        Instructor Lucy = new Instructor("Lucy");
        Lucy.setInstructid(2);
        Instructor Charlie = new Instructor("Charlie");
        Charlie.setInstructid(3);


        // Restaurant String name, String address, String city, String state, String telephone

        Course DataScience = new Course("Data Science", Sally);
        DataScience.setCourseid(1);
        courseList.add(DataScience);
        Course JavaScript = new Course("JavaScript", Sally);
        JavaScript.setCourseid(2);
        courseList.add(JavaScript);
        Course NodeJs = new Course("Node.js", Sally);
        NodeJs.setCourseid(3);
        courseList.add(NodeJs);
        Course JavaBackEnd = new Course("Java Back End", Lucy);
        JavaBackEnd.setCourseid(4);
        courseList.add(JavaBackEnd);
        Course MobileIos = new Course("Mobile IOS", Lucy);
        MobileIos.setCourseid(5);
        courseList.add(MobileIos);
        Course MobileAndroid = new Course("Mobile Android", Charlie);
        MobileAndroid.setCourseid(6);
        courseList.add(MobileAndroid);

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
        String apiUrl = "/courses/courses/1";

        Mockito.when(courseService.findCourseById(1)).thenReturn(courseList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn(); // this could throw an exception
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(courseList.get(0));

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
        String apiUrl = "/courses/course/add";

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