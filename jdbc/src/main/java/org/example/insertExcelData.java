package org.example;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dao.Course;
import org.example.dao.Student;

public class insertExcelData {

    private static final String URL = "jdbc:postgresql://192.168.1.112:5432/jdbc_db";
    private static final String USER = "xzw";
    private static final String PASSWORD = "Xzw@010816";
    public static List<Student> students;
    public static List<Course> courses;

    public static Connection conn = null;

    public static void readData() throws SQLException {
        String studentFile = "/Users/xuzhongwei/Downloads/students-20.xlsx";
        String courseFile = "/Users/xuzhongwei/Downloads/courses-20.xlsx";
        try{conn = DriverManager.getConnection(URL, USER, PASSWORD);}catch(SQLException e){e.printStackTrace();}
        // 读取学生信息
        students = readStudentData(studentFile);

        // 插入学生信息到数据库
        //insertStudentData();
//        fixStudent();
        //updateStudenData();

        //deleteStudentData(students);
        // 读取课程信息
        //fixCourse();
        courses = readCourseData(courseFile);
  //      fixCourse();
        //updateCourseData();
        //selectCourseData();
        //selectStudentData();
        // 插入课程信息到数据库
//        insertCourseData(courses);
        //deleteCourseData();
    }

    public static List<Student> readStudentData(String fileName) {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // skip header row
                    continue;
                }

                Long id = Long.parseLong(row.getCell(0).getStringCellValue());
                String name = row.getCell(1).getStringCellValue();
                String sex = row.getCell(2).getStringCellValue();
                String bdate = row.getCell(3).getStringCellValue();
                double height = row.getCell(4).getNumericCellValue();
                String dorm = row.getCell(5).getStringCellValue();

                Student student = new Student(id, name, sex, bdate, height, dorm);
                students.add(student);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static List<Course> readCourseData(String fileName) {
        List<Course> courses = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // skip header row
                    continue;
                }
                String id = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                int period = (int) row.getCell(2).getNumericCellValue();
                double credit = (double)row.getCell(3).getNumericCellValue();
                String teacher = row.getCell(4).getStringCellValue();

                Course course = new Course(id, name, period, credit, teacher);
                courses.add(course);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void insertStudentData() {
        try  {
            String sql = "INSERT INTO student (\"S#\", sname, sex, bdate, height, dorm) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            Random random = new Random(); // 创建随机数生成器对象
            int size = students.size(); // 获取学生对象列表的长度
            int index = 0;
            if(size != 0) {
                index = random.nextInt(size); // 随机选择一个下标
            }
            Student student = students.get(index); // 获取选中的学生对象
            String querySql = "SELECT 1 FROM student WHERE \"S#\" = ?";
            PreparedStatement queryStmt = conn.prepareStatement(querySql);
            queryStmt.setLong(1, student.getId());
            ResultSet rs = queryStmt.executeQuery();
            if (!rs.next()) {
                stmt.setLong(1, student.getId());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getSex());
                stmt.setString(4, student.getBdate());
                stmt.setDouble(5, student.getHeight());
                stmt.setString(6, student.getDorm());
                stmt.executeUpdate();
                students.remove(index); // 删除已选中的学生对象
                System.out.println("Inserted student data successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public static void insertCourseData() {

        try {
            String sql = "INSERT INTO course (\"C#\", cname, period, credit, teacher) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            Random random = new Random(); // 创建随机数生成器对象
            int size = courses.size(); // 获取课程对象列表的长度
            int index = random.nextInt(size); // 随机选择一个下标
            Course course = courses.get(index); // 获取选中的课程对象
            String querySql = "SELECT 1 FROM course WHERE \"C#\" = ?";
            PreparedStatement queryStmt = conn.prepareStatement(querySql);
            queryStmt.setString(1, course.getId());
            ResultSet rs = queryStmt.executeQuery();
            if (!rs.next()) {
                stmt.setString(1, course.getId());
                stmt.setString(2, course.getName());
                stmt.setInt(3, course.getPeriod());
                stmt.setDouble(4, course.getCredit());
                stmt.setString(5, course.getTeacher());
                stmt.executeUpdate();
                courses.remove(index); // 删除已选中的课程对象
                System.out.println("Inserted course data successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deleteStudentData(){
        try  {
            String countSql = "SELECT COUNT(*) FROM student"; // 获取表中记录总数的 SQL 语句
            String deleteSql = "DELETE FROM student WHERE \"S#\" = ?"; // 删除记录的 SQL 语句
            Statement countStmt = conn.createStatement();
            ResultSet countRs = countStmt.executeQuery(countSql);
            countRs.next();
            int count = countRs.getInt(1); // 获取表中记录总数
            if(count > 0){
//                Random random = new Random();
//                //int index = random.nextInt()+1;
//                int index = 3;
                Random random = new Random();
                int index = random.nextInt(students.size()); // 随机选择一个学生的下标
                Student s = students.get(index); // 获取随机选择的学生
                long sId = s.getId(); // 获取该学生的"S#"值
                System.out.println("The delete student is "+students.remove(index));
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setLong(1, sId);
                int rows = deleteStmt.executeUpdate();
                if(rows >0){
                    System.out.println("Delete successfully.");

                }else{
                    System.out.println("No record found with index="+ index);
                }
            }else{
                System.out.println("No record found");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void deleteCourseData() {
        try {
            String countSql = "SELECT COUNT(*) FROM course"; // 获取表中记录总数的 SQL 语句
            String deleteSql = "DELETE FROM course WHERE \"C#\" = ?"; // 删除记录的 SQL 语句
            Statement countStmt = conn.createStatement();
            ResultSet countRs = countStmt.executeQuery(countSql);
            countRs.next();
            int count = countRs.getInt(1); // 获取表中记录总数
            if(count > 0){
//                Random random = new Random();
//                //int index = random.nextInt()+1;
//                int index = 3;
                Random random = new Random();
                int index = random.nextInt(courses.size()); // 随机选择一个学生的下标
                Course c = courses.get(index); // 获取随机选择的学生
                String cId = c.getId(); // 获取该学生的"S#"值
                System.out.println("The delete student is "+courses.remove(index));
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setString(1, cId);
                int rows = deleteStmt.executeUpdate();
                if(rows >0){
                    System.out.println("Delete successfully.");

                }else{
                    System.out.println("No record found with index="+ index);
                }
            }else{
                System.out.println("No record found");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateStudenData(){
        try{
            Random random = new Random();
            int index = random.nextInt(students.size());
            Student student = students.get(index);
            Long sId = student.getId();
            int num = random.nextInt(2);
            String updateSql;
            if(num < 1) {
                updateSql = "UPDATE student SET height = height+0.1 where \"S#\" = ?";
            }
            else{
                updateSql = "UPDATE student SET dorm = \'教务处\' where \"S#\" = ? ";
            }

            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setLong(1,sId);
            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("Update successfully.");
                System.out.println("The updatesd student is "+student);
            }else{
                System.out.println("No such student!");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateCourseData(){
        try{
            Random random = new Random();
            int index = random.nextInt(courses.size());
            Course course = courses.get(index);
            int con = random.nextInt(2);
            String updateSql;
            if(con < 1){
                updateSql = "UPDATE course SET period = 0 where \"C#\" = ?";
            }else{
                updateSql = "UPDATE course SET teacher = \'TEST老师\' where \"C#\" = ?";
            }
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, course.getId());
            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("Update successfully.");
                System.out.println("The updated course is "+course);
            }else{
                System.out.println("No such course!");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void selectStudentData(){
        try{
            Random random = new Random();
            int index = random.nextInt(students.size());
            Student student = students.get(index);
            long sId = student.getId();
            String selectSql = "SELECT * FROM student WHERE \"S#\" = ?";
            PreparedStatement stmt = conn.prepareStatement(selectSql);
            stmt.setLong(1, sId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // 循环遍历查询结果集
                long id = rs.getLong("S#");
                String name = rs.getString("sname");
                String sex = rs.getString("sex");
                String bdate = rs.getString("bdate");
                double height = rs.getDouble("height");
                String dorm = rs.getString("dorm");
                Student queriedStudent = new Student(id, name, sex, bdate, height, dorm);
                System.out.println("Queried student: " + queriedStudent);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void selectCourseData(){
        try {
            Random random = new Random();
            int index = random.nextInt(courses.size());
            Course course = courses.get(index);
            String cId = course.getId();
            String selectSql = "SELECT * FROM course WHERE \"C#\" = ?";
            PreparedStatement stmt = conn.prepareStatement(selectSql);
            stmt.setString(1, cId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // 循环遍历查询结果集
                String id = rs.getString("C#");
                String name = rs.getString("cname");
                int period = rs.getInt("period");
                double credit = rs.getDouble("credit");
                String teacher = rs.getString("teacher");
                Course queriedCourse = new Course(id, name, period, credit, teacher);
                System.out.println("Queried course: " + queriedCourse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void fixCourse(){
        try {
            String sql = "INSERT INTO course (\"C#\", cname, period, credit, teacher) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (Course course : courses) {
                String querySql = "SELECT 1 FROM course WHERE \"C#\" = ?";
                PreparedStatement queryStmt = conn.prepareStatement(querySql);
                queryStmt.setString(1, course.getId());
                ResultSet rs = queryStmt.executeQuery();
                if (!rs.next()) { // 如果课程不在数据库中，插入该课程
                    stmt.setString(1, course.getId());
                    stmt.setString(2, course.getName());
                    stmt.setInt(3, course.getPeriod());
                    stmt.setDouble(4, course.getCredit());
                    stmt.setString(5, course.getTeacher());
                    stmt.executeUpdate();
                    System.out.println("Inserted course " + course.getId() + " successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void fixStudent(){
        try {
            String sql = "INSERT INTO student (\"S#\", sname, sex, bdate, height, dorm) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (Student student : students) {
                String querySql = "SELECT 1 FROM student WHERE \"S#\" = ?";
                PreparedStatement queryStmt = conn.prepareStatement(querySql);
                queryStmt.setLong(1, student.getId());
                ResultSet rs = queryStmt.executeQuery();
                if (!rs.next()) { // 如果学生不在数据库中，插入该学生
                    stmt.setLong(1, student.getId());
                    stmt.setString(2, student.getName());
                    stmt.setString(3, student.getSex());
                    stmt.setString(4, student.getBdate());
                    stmt.setDouble(5, student.getHeight());
                    stmt.setString(6, student.getDorm());
                    stmt.executeUpdate();
                    System.out.println("Inserted student " + student.getId() + " successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}