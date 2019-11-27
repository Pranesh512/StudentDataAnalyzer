package com.pranze.studentdataanalyzer;

import com.pranze.studentdataanalyzer.dto.StudentDTO;
import com.pranze.studentdataanalyzer.query.Order;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StudentDataAnalyzerTest {

    private static StudentDataAnalyzer studentDataAnalyzer;

    @BeforeClass
    public static void setUp() throws Exception {
        ClassLoader classLoader = StudentDataAnalyzerTest.class.getClassLoader();
        studentDataAnalyzer = new StudentDataAnalyzer();
        studentDataAnalyzer.loadData(
                new File(classLoader.getResource("stud_data.csv").getFile()),
                new File(classLoader.getResource("score_data.csv").getFile())
        );
    }

    @Test
    public void queryTopScorerByTotalInCS2016() {
        HashMap<String, String> filter = new HashMap<>();
        filter.put("course", "CS");
        filter.put("batch", "2016");
        Order order = Order.of("totalScore", true);
        List<StudentDTO> result = studentDataAnalyzer.query(filter, order, 5);
        assertEquals(5, result.size());
        System.out.println("queryTopScorerByTotalInCS2016");
        print(result);
    }

    @Test
    public void queryTopScorerByAvgInAllIT() {
        HashMap<String, String> filter = new HashMap<>();
        filter.put("course", "IT");
        Order order = Order.of("avgScore", true);
        List<StudentDTO> result = studentDataAnalyzer.query(filter, order, 10);
        assertEquals(10, result.size());
        System.out.println("queryTopScorerByAvgInAllCS");
        print(result);
    }

    @Test
    public void queryLowestScorersInSubInAllIT() {
        HashMap<String, String> filter = new HashMap<>();
        filter.put("course", "IT");
        filter.put("subject", "subject1");
        Order order = Order.of("score", false);
        List<StudentDTO> result = studentDataAnalyzer.query(filter, order, 5);
        assertEquals(5, result.size());
        System.out.println("queryLowestScorersInSubInAllIT");
        print(result);
    }

    @Test
    public void queryAll() {
        List<StudentDTO> result = studentDataAnalyzer.query(null, null, 0);
        System.out.println("queryAll");
        print(result);
    }

    public void print(Collection<StudentDTO> data) {
        PrintStream out = System.out;
        data.forEach(d -> {
            out.append(d.getRegNo()).append(',').append(d.getCourse()).append(',')
                    .append(d.getBatch()).append(',').append(d.getSection());
            if (d.getScore() != null) {
                out.append(',').append(d.getScore().toString());
            }
            out.println();
        });
    }
}