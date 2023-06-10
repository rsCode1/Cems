package logic;

import java.io.Serializable;

//class for question ,it helps to create a question and save it in the database
//it is used on writeQuestionController
public class Question implements Serializable {
    private String questionDescription;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer;
    private String course;
    private String subject;
    private String author;

    public Question(String questionDescription, String answer1, String answer2, String answer3, String answer4, int correctAnswer, String course, String subject, String author) {
        this.questionDescription = questionDescription;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
        this.course = course;
        this.subject = subject;
        this.author = author;
    }

    //getters for all fields
    public String getQuestionDescription() {
        return questionDescription;
    }
    public String getAnswer1() {
        return answer1;
    }
    public String getAnswer2() {
        return answer2;
    }
    public String getAnswer3() {
        return answer3;
    }
    public String getAnswer4() {
        return answer4;
    }
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    public String getCourse() {
        return course;
    }
    public String getSubject() {
        return subject;
    }
    public String getAuthor() {
        return author;
    }


}
