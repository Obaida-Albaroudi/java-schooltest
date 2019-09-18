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
    private Instructor instructor;

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
        assertEquals("JavaScript", courseService.findCourseById(2).getCoursename());
    }


    @Test
    public void update()
    {
        Instructor thisInstructor = new Instructor();
        Course r1 = new Course(null, thisInstructor);
        r1.setCourseid(10);

        Course updatedR1 = courseService.update(r1, 10);

        assertEquals("ZZ", updatedR1.getClass());
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
        Instructor thisInstructor = new Instructor("Aws");
        String rest3Name = "Java 10000";
        Course r5 = new Course( rest3Name,
                thisInstructor);
        r5.getStudents().add(new Student("John"));

        Course addCourse = courseService.save(r5);

        assertNotNull(addCourse);

        Course foundCourse = courseService.findCourseById(addCourse.getCourseid());
        assertEquals(addCourse.getCoursename(), foundCourse.getCoursename());
    }
}