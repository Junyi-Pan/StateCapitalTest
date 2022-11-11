package edu.uga.cs.statecapitalquiz;

/**
 * This class (a POJO) represents a single question, including the id, state,
 * capital, and two other cities.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Question {

    // Instance variables for columns in table
    private long id;
    private String state;
    private String capital;
    private String city2;
    private String city3;

    //Constructor
    public Question(){
        this.id = -1;
        this.state = null;
        this.capital = null;
        this.city2 = null;
        this.city3 = null;
    }

    public Question(String state, String capital, String city2, String city3) {
        this.id = -1;
        this.state = state;
        this.capital = capital;
        this.city2 = city2;
        this.city3 = city3;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getCity3() {
        return city3;
    }

    public void setCity3(String city3) {
        this.city3 = city3;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", capital='" + capital + '\'' +
                ", city2='" + city2 + '\'' +
                ", city3='" + city3 + '\'' +
                '}';
    }
}
