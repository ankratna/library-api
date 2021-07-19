**Library Api for Books management :** This project allows users to manage books in library.Following are the funtionality:
1. Add single book to system.
2. Upload multiple books via csv file.
3. Update already existing book.
4. Delete already existing book.
5. Search: search via any field available in book (isbn,author,title,List of tags), it will ne AND of all conditions
6. Search if any of the tag is available in book given by user.
7. Search if all the tags available given by user.
8. Get list of all books available.
9. Search book by isbn
10.Search book by author
11.Search book by title

**Technologies used**
1. SpringBoot framework
2. H2- in memory database
3. Junit and Mockito for unit testing
4. Swagger for REST Api testing
5. Java8 for development
6. SOLID Design principle
7. Design pattern : Strategy and Dependency Injection 

**How to install and run**
1. Install Java8 ans maven.
2. Clone git repository by clicking here : https://github.com/ankratna/library-api
  (or run : git clone https://github.com/ankratna/library-api.git)   
3. run: mvn clean install
4. cd library-api and run: mvn spring-boot:run
5. open swagger-ui : http://localhost:8002/swagger-ui/index.html#/
6. open h2-database console here : http://localhost:8002/h2-console
7. use swagger to test application.

**Instruction to upload csv :** first line of csv file should br HEADER, and csv should be in the order(Isbn,Author,Title,Tags(tag list can be any length with comma separated))







