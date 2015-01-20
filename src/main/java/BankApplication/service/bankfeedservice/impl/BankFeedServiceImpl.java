package BankApplication.service.bankfeedservice.impl;

import BankApplication.service.bankfeedservice.BankFeedService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Kir Kolesnikov on 19.01.2015.
 */
public class BankFeedServiceImpl implements BankFeedService {
    @Override
    public Map<String, String> parseFeed(String feed) {
        Map<String, String> result = new HashMap<>();
        String[] splittedStrings = feed.split(";");
        for (int i = 0; i < splittedStrings.length - 1; i++) {
            String[] tempPair = splittedStrings[i].split("=");
            result.put(tempPair[0], tempPair[1]);
            //magic numbers (((
        }
        return result;
    }

    @Override
    public List<String[]> loadFeeds(String folder) {
        List<String[]> result = new ArrayList<>();
        File folderToRead = new File(folder);
        if (folderToRead.isDirectory()) {
            File[] filesInFolder = folderToRead.listFiles();
            for (int i = 0; i < filesInFolder.length; i++) {
                File tempFile = filesInFolder[i];
                result.add(readFileLineByLine(tempFile));
            }
        }
        return result;
    }

    private String[] readFileLineByLine(File incomingFile) {
        List<String> listResult = new ArrayList<>();
        if (incomingFile.isFile()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(incomingFile))) {
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    listResult.add(currentLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] result = new String[listResult.size()];
        return listResult.toArray(result);
    }


}
