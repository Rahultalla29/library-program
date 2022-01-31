import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Member {

    private String name;
    private String memberNumber;
    private List<Book> rentingbooks = new ArrayList<Book>();
    private List<Book> rentalHistory = new ArrayList<Book>();

    public Member (String name, String memberNumber) {
        this.name = name;
        this.memberNumber = memberNumber;
    }

	public String getName() {return this.name;}
    public String getMemberNumber() {return this.memberNumber;}

    public boolean rent(Book book) {

        if (book == null){
            return false;
        }

        if (!book.isRented()) {      
            book.rent(this);
            rentingbooks.add(book);
            return true;
        }

        else if (book.currentRenter.equals(this)) {
            return false;
        }
        
        return false;     
    }

    public boolean relinquish(Book book) {

        if (book == null) { 
            return false;
        }

        if (book.currentRenter != this) {
            return false;
        }
        
        else {
            book.relinquish(this); 
            rentalHistory.add(book); // Update renting data after returning book
            rentingbooks.remove(book);
            return true;
        }
    }

    public void relinquishAll () {

        List<Book> booksToReturn = new ArrayList<Book>();

        for (Book elem : rentingbooks) {       
            rentalHistory.add(elem);
            booksToReturn.add(elem);          
            elem.relinquish(this);
        }

        for (Book book : booksToReturn) {
            rentingbooks.remove(book);
        }       
    }

    public List<Book> history() {
        return rentalHistory;
    }

    public List<Book> renting() {
        return rentingbooks;
    }

    public static List<Book> commonBooks(Member[] members) {
        if ((members == null) || (members.length == 0)) {
            return null;
        }

        else{

            for (Member elem : members ){
                if (elem == null){
                    return null;
                }
            }
        }

        List<Book> intersection = members[0].history(); // Set first member as comparable for remaining members in list 

        // Iterates members list and compares rental history with comparable. Sets boolean flag when found to update intersection
        
        for (int i = 1 ; i < members.length ; i++) {
            for (int j = 0 ; j < intersection.size(); j++) {
                boolean Same = false;
                for (int k = 0 ; k < members[i].rentalHistory.size() ; k++) {
                    if (intersection.get(j).equals(members[i].rentalHistory.get(k))) {
                        Same = true;

                    }
                }
                if (Same == false) {
                    intersection.remove(j);
                }
            }
        }
        
        //Converts to serial number to sort and update by serial number

        List<String> sortedCommonBookSerial = new ArrayList<String>();

        for (Book elem : intersection) {
            sortedCommonBookSerial.add(elem.getSerialNumber()); 
        }
               
        Collections.sort(sortedCommonBookSerial);
        
        List<Book> sortedCommonBooks = new ArrayList<Book>();

        for (String serialNo : sortedCommonBookSerial) {
            for (Book elem : intersection) {
                if (elem.getSerialNumber().equals(serialNo)){
                    sortedCommonBooks.add(elem);
                }
            }           
        }
        return sortedCommonBooks;
    }
}