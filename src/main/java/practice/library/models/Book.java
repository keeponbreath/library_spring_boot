package practice.library.models;

//import jakarta.persistence.*;
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.Size;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    @NotEmpty
    @Size(max = 50, message = "Название книги не может содержать более 50 символов")
    private String title;

    @Column(name = "author")
    @NotEmpty
    @Size(max = 50, message = "Имя автора не может содержать более 50 символов")
    private String author;

    @Column(name = "year_of_prod")
    @Min(1699)
    @Max(2023)
    private int yearOfProd;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    public Book() {
    }

    public Book(String title, String author, int yearOfProd) {
        this.title = title;
        this.author = author;
        this.yearOfProd = yearOfProd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfProd() {
        return yearOfProd;
    }

    public void setYearOfProd(int yearOfProd) {
        this.yearOfProd = yearOfProd;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearOfProd=" + yearOfProd +
                ", isOverdue=" + isOverdue +
                ", owner=" + owner +
                '}';
    }
}
