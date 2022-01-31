import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Library {

    public List<Book> booksInSystem = new ArrayList<Book>();
    public List<Book> AvailableBooksInSystem = new ArrayList<Book>();
    public List<Book> booksRentedInSystem = new ArrayList<Book>();
    public List<Member> membersInSystem = new ArrayList<Member>();
    private  int memberNumberCount = 100000;

    public static final String HELP_STRING = String.join("\n" ,
                                                        "EXIT ends the library process",
                                                        "COMMANDS outputs this help string\n",
                                                        "LIST ALL [LONG] outputs either the short or long string for all books",
                                                        "LIST AVAILABLE [LONG] outputs either the short of long string for all available books",
                                                        "NUMBER COPIES outputs the number of copies of each book",
                                                        "LIST GENRES outputs the name of every genre in the system",
                                                        "LIST AUTHORS outputs the name of every author in the system\n",
                                                        "GENRE <genre> outputs the short string of every book with the specified genre",
                                                        "AUTHOR <author> outputs the short string of every book by the specified author\n",
                                                        "BOOK <serialNumber> [LONG] outputs either the short or long string for the specified book",
                                                        "BOOK HISTORY <serialNumber> outputs the rental history of the specified book\n",
                                                        "MEMBER <memberNumber> outputs the information of the specified member",
                                                        "MEMBER BOOKS <memberNumber> outputs the books currently rented by the specified member",
                                                        "MEMBER HISTORY <memberNumber> outputs the rental history of the specified member\n",
                                                        "RENT <memberNumber> <serialNumber> loans out the specified book to the given member",
                                                        "RELINQUISH <memberNumber> <serialNumber> returns the specified book from the member",
                                                        "RELINQUISH ALL <memberNumber> returns all books rented by the specified member\n",
                                                        "ADD MEMBER <name> adds a member to the system",
                                                        "ADD BOOK <filename> <serialNumber> adds a book to the system\n",
                                                        "ADD COLLECTION <filename> adds a collection of books to the system",
                                                        "SAVE COLLECTION <filename> saves the system to a csv file\n",
                                                        "COMMON <memberNumber1> <memberNumber2> ... outputs the common books in members\' history");


    public Library () {}
     public static void main(String[] args) {

        Library test1 = new Library();        
        test1.run();        
    }

    public void run() {
        
        boolean wantToStay = true;
        Scanner commandline = new Scanner(System.in);
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
        
        while (wantToStay) {

            System.out.print("user: ");

            try {
            

                String command = reader.readLine(); 
                //Command: COMMANDS
                if (command.toUpperCase().equals("COMMANDS")) {
                    System.out.println(Library.HELP_STRING + "\n");
                }

                String[] nestedInput = command.split(" "); // Converts input line to sectioned elements E.G. {ADD,COLLECTION,FILENAME}
                
                //Command: ADD MEMBER
                if (nestedInput[0].toUpperCase().equals("ADD") && nestedInput[1].toUpperCase().equals("MEMBER")) {
                    String parts[] = command.split(" ", 3);
                    addMember(parts[2]);
                }

                //Command: ADD BOOK
                if (nestedInput[0].toUpperCase().equals("ADD") && nestedInput[1].toUpperCase().equals("BOOK")) {
                    File f = new File(nestedInput[2]);
                    
                    if(!f.exists()) { // Checks for file
                        System.out.println("No such file.\n");
                    }

                    else {addBook(nestedInput[2],nestedInput[3]); }                                                       
                }

                //Command: BOOK HISTORY
                if (nestedInput[0].toUpperCase().equals("BOOK") && nestedInput[1].toUpperCase().equals("HISTORY")) {
                    bookHistory(nestedInput[2]);
                }

                //Command: BOOK [LONG]
                if (nestedInput[0].toUpperCase().equals("BOOK") && command.toUpperCase().contains("LONG")) {
                    getBook(nestedInput[1], true);
                }

                else if ((nestedInput[0].toUpperCase().equals("BOOK")) && (nestedInput.length == 2)) {
                    getBook(nestedInput[1], false);
                }

                //Command: LIST AVAILABLE [LONG]
                if (nestedInput[0].toUpperCase().equals("LIST") && command.toUpperCase().contains("AVAILABLE")) {
                    if (command.toUpperCase().contains("LONG")) {
                        getAvailableBooks(true);
                    }

                    else if (!command.toUpperCase().contains("LONG")) {
                        getAvailableBooks(false);
                    }
                }
                
                //Command: LIST AUTHORS
                if (nestedInput[0].toUpperCase().equals("LIST") && command.toUpperCase().contains("AUTHORS")) {
                    getAuthors();
                }

                else if (command.toUpperCase().contains("AUTHOR")) {
                    String parts[] = command.split(" ", 2);
                    getBooksByAuthor(parts[1]);
                }

                //Command: LIST GENRES
                if (nestedInput[0].toUpperCase().equals("LIST") && command.toUpperCase().contains("GENRES")) { 
                    getGenres();

                }
                else if (command.toUpperCase().contains("GENRE")) {
                    String parts[] = command.split(" ", 2);
                    getBooksByGenre(parts[1]);

                }

                //Command: LIST ALL [LONG]
                if (nestedInput[0].toUpperCase().equals("LIST") && command.toUpperCase().contains("ALL")) {
                    if (command.toUpperCase().contains("LONG")) {
                        getAllBooks(true);
                    }
                    else if (!command.toUpperCase().contains("LONG")) {
                        getAllBooks(false);
                    }
                }

                //Command: NUMBER COPIES
                if (nestedInput[0].toUpperCase().equals("NUMBER") && nestedInput[1].toUpperCase().equals("COPIES")) {
                    getCopies();
                }

                //Command: MEMBER HISTORY
                if (nestedInput[0].toUpperCase().equals("MEMBER") && command.toUpperCase().contains("HISTORY")) {
                    if ((nestedInput.length < 4) && (membersInSystem.size() == 0)) {
                        System.out.println("No members in system.\n");

                    }
                    else {
                        memberRentalHistory(nestedInput[2]);
                    }
                    
                }

                //Command: MEMBER BOOKS
                else if (nestedInput[0].toUpperCase().equals("MEMBER") && command.toUpperCase().contains("BOOKS")) {
                    getMemberBooks(nestedInput[2]);
                }

                else if (nestedInput[0].toUpperCase().equals("MEMBER")) {
                    
                    if ((nestedInput.length < 3) && (membersInSystem.size() == 0)) {
                        System.out.println("No members in system.\n");

                    }
                    else {
                        getMember(nestedInput[1]);
                    }
                }

                //Command: RENT
                if (nestedInput[0].toUpperCase().equals("RENT")) {
                    if (membersInSystem.size() == 0) {
                        System.out.println("No members in system.\n");
                    }
                    else if (booksInSystem.size() == 0) {
                        System.out.println("No books in system.\n");
                    }

                    else {
                        rentBook(nestedInput[1], nestedInput[2]);
                    }
                }

                //Command: RELINQUISH ALL
                if (nestedInput[0].toUpperCase().equals("RELINQUISH") && nestedInput[1].toUpperCase().contains("ALL")) {
                    if (membersInSystem.size() == 0) {
                        System.out.println("No members in system.\n");
                    }
                    else {relinquishAll(nestedInput[2]);}
                }

                else if (nestedInput[0].toUpperCase().equals("RELINQUISH")) {
                    relinquishBook(nestedInput[1], nestedInput[2]);
                }

                //Command: ADD COLLECTION
                if (nestedInput[0].toUpperCase().equals("ADD") && nestedInput[1].toUpperCase().equals("COLLECTION")) {
                    File f = new File(nestedInput[2]);
                    
                    if(!f.exists()) { 
                        System.out.println("No such collection.\n");
                    }

                    else{
                        addCollection(nestedInput[2]);
                    }
                }

                //Command: SAVE COLLECTION
                if (nestedInput[0].toUpperCase().equals("SAVE") && nestedInput[1].toUpperCase().equals("COLLECTION")) {
                    saveCollection(nestedInput[2]);
                }

                //Command: COMMON
                if (command.toUpperCase().contains("COMMON")) {
                    
                    String parts[] = command.split(" ", 2);
                    List<String> memberNums = new ArrayList<String>(Arrays.asList(parts[1].split(" ")));
                    String[] memberNumsArray = memberNums.toArray(new String[0]);
                    common(memberNumsArray);                   
                }

                //Command: EXIT
                if (command.toUpperCase().equals("EXIT")) {
                    System.out.println("Ending Library process.");
                    wantToStay = false;
                    
                    

                }

            }catch (IOException e) {
                System.out.println("Nothing to read");
            }
        }
    }      

    public void getAllBooks(boolean fullString) {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        if (fullString == true) {
            for (Book elem : booksInSystem) {
                System.out.println(elem.longString() + "\n");
            }
        }

        else if (fullString == false) {
            for (Book elem : booksInSystem) {
                System.out.println(elem.shortString());
            }
            System.out.println();
        }
    }

    public int numberOfAvailableBooks(List<Book> booksList) {

        int count = 0;
        for (Book elem : booksList) {
            if(!elem.isRented()) {
                count +=1;
            }
        }
        return count;
    }

    public void UpdateAvailableBooks() {

        AvailableBooksInSystem.clear(); // Empties lists before updating
        booksRentedInSystem.clear();

        for (Book elem : booksInSystem) {
            if(!elem.isRented()) {
                AvailableBooksInSystem.add(elem);              
            }

            if(elem.isRented()) {
                booksRentedInSystem.add(elem);   
            }
        }
    }

    public void getAvailableBooks(boolean fullString) {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        if ( numberOfAvailableBooks(booksInSystem) == 0) {
            System.out.println("No books available.\n");
            return;
        }

        UpdateAvailableBooks();

        for (Book book : AvailableBooksInSystem) {
            if (fullString == true) {
                System.out.println(book.longString() + "\n");
            }
            else if (fullString == false) {
                System.out.println(book.shortString());
            }
        }

        if (fullString == false) {
            System.out.println();
        }
    }


    public void getCopies(){

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        List<String> bookShortStrings = new ArrayList<String>();
        
        for (Book elem : booksInSystem) {
            bookShortStrings.add(elem.shortString());
        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (String elem : bookShortStrings) {
            Integer count = map.get(elem);
            map.put(elem, (count == null) ? 1 : count + 1);
        }

        Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);

        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + ": "
                + entry.getValue());
        }
        System.out.println();

    }

    public void getGenres() {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        List<String> genres = new ArrayList<String>();

        for (Book elem : booksInSystem) {
            genres.add(elem.getGenre());
        }

        Set<String> set = new LinkedHashSet<>(); 
        set.addAll(genres); 
        genres.clear(); 
        genres.addAll(set); 
        genres.sort( Comparator.comparing( String::toString ) ); 

        for (String elem : genres) {
            System.out.println(elem);;
        } 
        System.out.println();
    }

    public void getAuthors() {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        List<String> authors = new ArrayList<String>();

        for (Book elem : booksInSystem) {
            authors.add(elem.getAuthor());
        }

        Set<String> set = new LinkedHashSet<>(); 
        set.addAll(authors); 
        authors.clear(); 
        authors.addAll(set); 
        authors.sort( Comparator.comparing( String::toString ) ); 

        for (String elem : authors) {
            System.out.println(elem);;
        } 
        System.out.println();
    }
    
    public void getBooksByGenre(String genre) {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        List<Book> booksByGenre = Book.filterGenre(booksInSystem, genre);
        if (booksByGenre.size() == 0) {
            System.out.println("No books with genre " + genre +".\n");
            return;

        }

        else {
            
            for (Book elem : booksByGenre) {
                System.out.println(elem.shortString());
            }
            System.out.println();
        }
    }


    public void getBooksByAuthor(String author) {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        List<Book> booksByAuthor = Book.filterAuthor(booksInSystem, author);

        if (booksByAuthor.size() == 0) {
            System.out.println("No books by " + author +".\n");
            return;

        }

        else {
            
            for (Book elem : booksByAuthor) {
                System.out.println(elem.shortString());
            }
            System.out.println();
        }
    }



    public void getBook(String serialNumber, boolean fullstring) {

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        for (Book elem : booksInSystem) {
            if (elem.getSerialNumber().equals(serialNumber) && (fullstring == true)) {
                System.out.println(elem.longString());
                System.out.println();
                return;
            }

            else if (elem.getSerialNumber().equals(serialNumber) && (fullstring == false)) {
                System.out.println(elem.shortString());
                System.out.println();
                return;
            }

        }
        System.out.println("No such book in system.\n");
    }

    public void bookHistory(String serialNumber) {

        boolean bookExist = false;
        for (Book elem : booksInSystem) {
            if (elem.getSerialNumber().equals(serialNumber)) {
                if ((elem.renterHistory().size() == 0 ) || (elem.renterHistory() == null) ) {
                    System.out.println("No rental history.\n");
                    return;
                }
                bookExist = true;
                for (Member member : elem.renterHistory()) {
                    System.out.println(member.getMemberNumber());
                }
                System.out.println();
            }
        }

        if (!bookExist) {
            System.out.println("No such book in system.\n");
        }
    }

    public void addBook(String bookFile, String serialNumber) {

        for (Book elem : booksInSystem) {
            if(elem.getSerialNumber().equals(serialNumber)) {
                System.out.println("Book already exists in system.\n");
                return;
            }
        }

        Book bookToAdd = Book.readBook(bookFile, serialNumber);

        if (bookToAdd == null) {
            System.out.println("No such book in file.\n");
            return;
        }

        booksInSystem.add(bookToAdd);
        System.out.println("Successfully added: " + bookToAdd.shortString() + ".\n");
    }

    public void rentBook(String memberNumber, String serialNumber) {

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
            return;
        }

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        else {
            boolean memberExists = false;
            Member matchingMember = null;
            // Member exists check
            for (int i = 0; i < membersInSystem.size(); i++) {
                if (membersInSystem.get(i).getMemberNumber().equals(memberNumber)) {
                    memberExists = true;
                    matchingMember =  membersInSystem.get(i);
                    break;
                }
            }

            if (!memberExists) {
                System.out.println("No such member in system.\n");
                return;
            }
            // Book exists check
            boolean bookExists = false;
            for (int i = 0; i < booksInSystem.size(); i++) {
                if (booksInSystem.get(i).getSerialNumber().equals(serialNumber)) {
                    bookExists = true;
                    break;
                }
            }
            if (!bookExists) {
                System.out.println("No such book in system.\n");
                return;
            }
            
            for (Book elem  : booksInSystem) {
                if (elem.getSerialNumber().equals(serialNumber)) {

                    if (!matchingMember.rent(elem)) {
                        System.out.println("Book is currently unavailable.\n"); 
                        return;
                    }

                    matchingMember.rent(elem);
                    System.out.println("Success.\n"); 
                    UpdateAvailableBooks();
                    return;
                }   
            }
              
        }
    }

    public void relinquishBook(String memberNumber, String serialNumber) {

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
            return;
        }
        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
        }

        else {
            boolean memberExists = false;
            Member matchingMember = null;
            // Member exists check
            for (int i = 0; i < membersInSystem.size(); i++) {
                if (membersInSystem.get(i).getMemberNumber().equals(memberNumber)) {
                    matchingMember =  membersInSystem.get(i);
                    memberExists = true;
                    break;
                }
            }
            if (memberExists == false) {
                System.out.println("No such member in system.\n");
                return;
            }
            // Book exists check
            boolean bookExists = false;
            for (int i = 0; i < booksInSystem.size(); i++) {
                if (booksInSystem.get(i).getSerialNumber().equals(serialNumber)) {
                    bookExists = true;
                    break;
                }
            }
            if (!bookExists) {
                System.out.println("No such book in system.\n");
                return;
            }
         
            for (Book elem  : booksInSystem) {

                if(elem.currentRenter != null) {

                    if (elem.currentRenter.equals(matchingMember)) {
                        matchingMember.relinquish(elem);
                        System.out.println("Success.\n");
                        UpdateAvailableBooks();  
                        return;
                    }
                }
            } 
            System.out.println("Unable to return book.\n");  
        }
    }

    public void relinquishAll(String memberNumber) {

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
        }
        else {
            boolean memberExists = false;

            for (int i = 0; i < membersInSystem.size(); i++) {

                if (membersInSystem.get(i).getMemberNumber().equals(memberNumber)) {
                    memberExists = true;
                    membersInSystem.get(i).relinquishAll();
                    System.out.println("Success.\n");
                    break;
                }
            }
            if (!memberExists) {
                System.out.println("No such member in system.\n");
            }
        }
    }

    public void getMember(String memberNumber) {

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
        }

        else {
            boolean memberExists = false;
            for (int i = 0; i < membersInSystem.size(); i++) {

                if (membersInSystem.get(i).getMemberNumber().equals(memberNumber)) {
                    memberExists = true;
                    System.out.println(memberNumber+ ": " + membersInSystem.get(i).getName());
                    System.out.println();
                    break;
                }
            }
            if (!memberExists) {
                System.out.println("No such member in system.\n");
            }
        }
    }

    public void getMemberBooks(String memberNumber) {

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
        }

        else {
            boolean memberExists = false;

            for (int i = 0; i < membersInSystem.size(); i++) {

                if (membersInSystem.get(i).getMemberNumber().equals(memberNumber)) {
                    memberExists = true;

                    if ((membersInSystem.get(i).renting().size() == 0) || (membersInSystem.get(i).renting() == null)) {
                        System.out.println("Member not currently renting.\n");
                        return;
                    }

                    else {
                        for (Book elem : membersInSystem.get(i).renting()) {
                            System.out.println(elem.shortString());
                        }
                        System.out.println();
                    }
                }
            }

            if (!memberExists) {
                System.out.println("No such member in system.\n");

            }     
        }
    }

    public void memberRentalHistory(String memberNumber) { 

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
        }

        else {
            boolean memberExists = false;
            for (int i = 0; i < membersInSystem.size(); i++) {

                if (membersInSystem.get(i).getMemberNumber().equals(memberNumber)) {
                    memberExists = true;
                    if ((membersInSystem.get(i).history().size() == 0) || (membersInSystem.get(i).history() == null)) {
                        System.out.println("No rental history for member.\n");
                        return;
                    }

                    else {

                        for (Book elem : membersInSystem.get(i).history()) {
                            System.out.println(elem.shortString());
                        }
                        System.out.println();
                    }
                }
            }

            if (!memberExists) {
                System.out.println("No such member in system.\n");

            }
        }
    }

    public void addMember(String name) { 

        if (memberNumberCount == 100000) {
            Member addmember = new Member(name, Integer.toString(memberNumberCount));
            memberNumberCount += 1;
            membersInSystem.add(addmember);
            System.out.println("Success.\n");

        }

        else {

            Member addmember = new Member(name, Integer.toString(memberNumberCount));
            memberNumberCount += 1;
            membersInSystem.add(addmember);
            System.out.println("Success.\n");
        }
    }


    public void saveCollection(String filename) {

        if ((booksInSystem.size() == 0) || (booksInSystem == null)) {
            System.out.println("No books in system.\n");
            return;
        }

        try {       
            File f = new File(filename);
            boolean fileExists = f.createNewFile();
            if ((booksInSystem.size() > 0) && fileExists) {
                Collection<Book> s = booksInSystem;
                Book.saveBookCollection(filename, s);
                System.out.println("Success.\n");
            }

        }catch (IOException e) {
            System.out.println("File does not exist, could not create file");
        }
    }

    public void addCollection(String filename) {

        List<Book> templs = Book.readBookCollection(filename);
        int booksAdded = 0;

        for (int i = 0; i < templs.size(); i++) {
            boolean Same = false;

            for (int j = 0; j < booksInSystem.size(); j++) {
                if (templs.get(i).getSerialNumber().equals(booksInSystem.get(j).getSerialNumber())){
                    Same = true;
                }
            }

            if (Same == false) {
                booksInSystem.add(templs.get(i));
                booksAdded += 1;
            }
        }

        if (booksAdded >= 1) {
            System.out.println(booksAdded + " books successfully added.\n");
        }

        else if (booksAdded == 0) {
            System.out.println("No books have been added to the system.\n");
        }
    }

    public void common(String[] memberNumbers) {

        if ((membersInSystem.size() == 0) || (membersInSystem == null) ) {
            System.out.println("No members in system.\n");
            return;

        }

        if ((booksInSystem.size() == 0) || (booksInSystem == null) ) {
            System.out.println("No books in system.\n");
            return;
            
        }

        List<Member> membersToCheck = new ArrayList<Member>();

        // Checks if the given members exist
        for (int j = 0 ; j < memberNumbers.length; j++) {
            boolean Same = false;
            for (int i = 0 ; i < membersInSystem.size() ; i++) {
                if (memberNumbers[j].equals(membersInSystem.get(i).getMemberNumber())) {
                    membersToCheck.add(membersInSystem.get(i));
                    Same = true;

                }
            }
            if (Same == false) {
                System.out.println("No such member in system." + "\n");
                return;
            }
        }
        
        Set<String> set = new LinkedHashSet<String>();

        for (String elem : memberNumbers) {
        
            if (set.contains(elem)) {
                System.out.println("Duplicate members provided.\n");
                return;
            }
            set.add(elem);
        }

        Member[] membersToCheckArray = new Member[membersToCheck.size()];
        membersToCheck.toArray(membersToCheckArray); // Converts list to array
        List<Book> commonBooks = Member.commonBooks(membersToCheckArray);

        if (commonBooks.size() == 0) {
            System.out.println("No common books.\n");
        }

        else{

            for (Book elem : commonBooks) {
                System.out.println(elem.shortString());
            }
            System.out.println();
        }   
    }
}