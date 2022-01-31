import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Book {

    private String title;
    private String author;
    private String genre;
    private String serialNumber;
    public Member currentRenter;
    private List<Member> BookRentedBy = new ArrayList<Member>();

    public Book (String title, String author, String genre, String serialNumber ) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.serialNumber = serialNumber;
    }

    public String getTitle() {return this.title;}
    public String getAuthor() {return this.author;}
    public String getGenre() {return this.genre;}
    public String getSerialNumber() {return this.serialNumber;}
    public void setTitle(String title) { this.title = title;}
    public void setAuthor(String author) {this.author = author;}
    public void setGenre(String genre) {this.genre = genre;}
    public void setSerialNumber(String serial) {this.serialNumber = serial;}

    public  String longString() {

        if (currentRenter == null) {
            return String.format("%s: %s (%s, %s)\nCurrently available.",this.serialNumber,this.title,this.author,this.genre);
        }

        return String.format("%s: %s (%s, %s)\nRented by: %s.",this.serialNumber,this.title,this.author,this.genre,currentRenter.getMemberNumber());        
    }

    public String shortString() {

        return String.format("%s (%s)", this.title,this.author);
        
    }

    public List<Member> renterHistory() {
        
        return BookRentedBy;
    }

    public boolean isRented() {

        if(currentRenter == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean rent(Member member) {

        if (member == null){
            return false;
        }

        if (currentRenter == null) {
            currentRenter = member;       
            return true;
        }

        return false;
    }

    public boolean relinquish(Member member) {

        if ((member == null) || (currentRenter == null)) {
            return false;
        }

        else if (!currentRenter.equals(member)) {
            return false;

        }

        if (currentRenter.equals(member)) { 
            BookRentedBy.add(member);
            currentRenter = null;
            return true;
        }
        
        return false;
    }

    public static Book readBook(String filename,String serialNumber) {
        
        String title = null;
        String author = null;
        String genre = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            
            String line;
 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[0].equals(serialNumber)){
                    title = values[1];
                    author = values[2];
                    genre = values[3];                 
                }                          
            }
               
        } catch (FileNotFoundException e){
            return null;

        } catch (IOException e){
            return null;
            
        } catch (NullPointerException nullPointer){
            return null;
        }

        if (title == null){ // Title value hasn't been changed- No book exists
            return null;
        }
        else {

            Book newBook = new Book(title, author, genre, serialNumber);
            return newBook;
        }       
    }

    public static List<Book> readBookCollection(String filename) {

        List<Book> listOfBooks = new ArrayList<Book>();
                
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            
            String line;
 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].equals("serialNumber")) { // Ignores headers
                    Book tempbook = new Book(values[1],values[2],values[3],values[0]);
                    listOfBooks.add(tempbook);

                }                                                     
            }
               
        } catch (FileNotFoundException e){
            return null;

        } catch (IOException e){
            return null;

        }catch (NullPointerException nullPointer){
            return null;
        }

        return listOfBooks;       
    }
    
    public static void saveBookCollection(String filename, Collection<Book> books){
                          
        try (PrintWriter writer = new PrintWriter(new File(filename))) {

            File tmpDir = new File(filename); 
            boolean exists = tmpDir.exists();
    
            if(books == null || (exists == false)){ // Check if file exists
                return;
            }

            StringBuilder sb = new StringBuilder();

            sb.append("serialNumber,");  // Add header columns
            sb.append("title,");
            sb.append("author,");     
            sb.append("genre");
            sb.append('\n');
                
            for (Iterator<Book> iterator = books.iterator(); iterator.hasNext();) {
                Book tempBook = iterator.next();
                sb.append(tempBook.getSerialNumber()+",");
                sb.append(tempBook.getTitle()+",");
                sb.append(tempBook.getAuthor()+",");
                sb.append(tempBook.getGenre()+'\n');
            }
      
            writer.write(sb.toString());

        }catch (FileNotFoundException e) {
            return;

        }catch (NullPointerException nullPointer){
            return;
        }      
    }

    public static List<Book> filterAuthor(List<Book> books, String author) {
    
        if (books == null || books.isEmpty()){
            return null;
        }

        if (author == null){
            return null;
        }

        for(Book elem : books) {
            if(elem == null){
                return null;
            }
        }

        List<Book> booksByAuthor = new ArrayList<Book>();

        for (Book elem : books) {  // Add matching members
            if (elem.getAuthor().equals(author)){
                booksByAuthor.add(elem);
                
            }
        }

        List<String> sortedBookSerialNoByAuthor = new ArrayList<String>();

        for (Book elem : booksByAuthor) { // Add matching authors with their associated books
            sortedBookSerialNoByAuthor.add(elem.serialNumber);
        }
                
        Collections.sort(sortedBookSerialNoByAuthor);

        List<Book> sortedBooksByAuthor = new ArrayList<Book>();

        for (String serialNo : sortedBookSerialNoByAuthor) { // Sort associated books by serial number and add back to list
            for (Book elem : booksByAuthor) {
                if (elem.getSerialNumber().equals(serialNo)){
                    sortedBooksByAuthor.add(elem);
                }
            }            
        }

        return sortedBooksByAuthor;
    }

    public static List<Book> filterGenre(List<Book> books, String genre) {

        if (books == null || books.isEmpty()){
            return null;
        }
        if (genre == null){
            return null;
        }

        for(Book elem : books) {
            if(elem == null){
                return null;
            }
        }

        List<Book> booksByGenre = new ArrayList<Book>();

        for (Book elem : books) { // Add matching genre with their associated books
            if (elem.getGenre().equals(genre)){
                booksByGenre.add(elem);
                
            }
        }

        List<String> sortedBookSerialNoByGenre = new ArrayList<String>();

        for (Book elem : booksByGenre) {
            sortedBookSerialNoByGenre.add(elem.serialNumber);
        }
                
        Collections.sort(sortedBookSerialNoByGenre);

        List<Book> sortedBooksByGenre = new ArrayList<Book>();

        for (String serialNo : sortedBookSerialNoByGenre) { // Sort associated books by serial number and add back to list
            for (Book elem : booksByGenre) {
                if (elem.getSerialNumber().equals(serialNo)){
                    sortedBooksByGenre.add(elem);

                }
            }           
        }
        return sortedBooksByGenre;
    }
}
