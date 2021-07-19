package com.jpmc.util;

import com.jpmc.dto.BookDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"Isbn", "Author", "Title", "Tags"};

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<BookDTO> csvToBooks(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<BookDTO> books = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                int numberOfRecords = csvRecord.size();

                BookDTO book = new BookDTO();
                book.setIsbn(Long.parseLong(csvRecord.get(0)));
                book.setAuthor(csvRecord.get(1));
                book.setTitle(csvRecord.get(2));
                int currentRecordNumber = 3;
                HashSet<String> tags = new HashSet<>();
                while (currentRecordNumber < numberOfRecords) {
                    tags.add(csvRecord.get(currentRecordNumber));
                    currentRecordNumber++;
                }
                book.setTags(tags);
                books.add(book);
            }

            return books;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}