package BankApplication.service.bankfeedservice;

import java.util.List;
import java.util.Map;

/**
 * Created by Kir Kolesnikov on 19.01.2015.
 */
public interface IBankFeedService {
     Map<String, String> parseFeed(String feed);
     List<String[]> loadFeeds(String folder);
}
