package swubook;

import java.io.*;
import java.net.*;
import org.json.*;

public class AladinBestseller {

    private static final String KEY = "ttblhhlove20051615001";

    public static void main(String[] args) {

        try {
            for (int page = 1; page <= 4; page++) {

                String apiUrl =
                    "https://www.aladin.co.kr/ttb/api/ItemList.aspx"
                  + "?ttbkey=" + KEY
                  + "&QueryType=Bestseller"
                  + "&MaxResults=100"
                  + "&start=" + page
                  + "&SearchTarget=Book"
                  + "&output=js"
                  + "&Version=20131101";

                URL url = new URL(apiUrl);
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(url.openStream(), "UTF-8")
                );

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONArray items = new JSONObject(sb.toString()).getJSONArray("item");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject b = items.getJSONObject(i);

                    Book book = new Book(
                        b.optString("title"),
                        b.optString("author"),
                        b.optString("publisher"),
                        b.optString("categoryName"),
                        b.optString("cover")
                    );

                    BookDAO.insert(book);
                    System.out.println("베스트셀러 저장: " + book.getTitle());
                }

                Thread.sleep(300);
            }

            System.out.println("베스트셀러 수집 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

