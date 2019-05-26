// IStudentManger.aidl
package cn.bsd.learn.binder.demo;
import cn.bsd.learn.binder.demo.Student;
// Declare any non-default types here with import statements

interface IStudentManger {
    List<Student> getStudentList();
    void addStudent(in Student student);
}
