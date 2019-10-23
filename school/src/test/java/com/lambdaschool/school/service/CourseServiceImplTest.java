package com.lambdaschool.school.service;

import com.lambdaschool.school.SchoolApplication;
import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.Instructor;
import com.lambdaschool.school.model.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchoolApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourseServiceImplTest
{
    @Autowired
    private CourseService courseService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findAll()
    {
        assertEquals(5, courseService.findAll().size());
    }


    @Test
    public void findCourseById()
    {
        assertEquals("Data Science", courseService.findCourseById(1).getCoursename());
    }


    @Test
    public void update()
    {
        Instructor Charlie = new Instructor("Charlie");
        Charlie.setInstructid(3);
        Course MobileAndroid = new Course("Android", Charlie);
        MobileAndroid.setCourseid(6);

        Course updatedMobileAndroid = courseService.update(MobileAndroid, 6);

        assertEquals("Android", updatedMobileAndroid.getCoursename());
    }

    @Test (expected = EntityNotFoundException.class)
    public void deleteNotFound()
    {
        courseService.delete(3);
        assertEquals(2, courseService.findAll().size());
    }


    @Test
    public void deleteFound()
    {
        courseService.delete(3);
        assertEquals(5, courseService.findAll().size());
    }


    @Test
    public void save()
    {
        Instructor Lucy = new Instructor("Lucy");
        Lucy.setInstructid(2);
        Course JavaBackEnd = new Course("Java Back End", Lucy);
        JavaBackEnd.setCourseid(4);


        Course addCourse = courseService.save(JavaBackEnd);

        assertNotNull(addCourse);

        Course foundCourse = courseService.findCourseById(addCourse.getCourseid());
        assertEquals(addCourse.getCoursename(), foundCourse.getCoursename());
    }
}