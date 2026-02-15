public class Student {
    private String studentId;
    private String name;
    private int age;
    private String course;
    private double gpa;
    private String pin;

    // Constructor
    public Student(String studentId, String name, int age, String course, double gpa, String pin) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.course = course;
        this.gpa = gpa;
        this.pin = pin;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCourse() { return course; }
    public double getGpa() { return gpa; }
    public String getPin() { return pin; }

    // Display student details
    public void displayDetails() {
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Course: " + course);
        System.out.println("GPA: " + gpa);
    }
}
