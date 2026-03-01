package swubook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class AladinApi {

    private static final String TTB_KEY = "ttblhhlove20051615001";

    public static void main(String[] args) {

        int insertedCount = 0; // 새로 저장된 책 수
        int skippedCount = 0;  // 중복으로 스킵된 책 수

        try {
            // 🔹 사람들이 실제로 많이 읽는 키워드 위주
            String[] keywords = {
                "소설", "에세이", "자기계발", "인문", "심리", "힐링"
            };

            for (String keyword : keywords) {

                // 🔹 페이지 수 늘림 (키워드당 60권)
                for (int page = 1; page <= 6; page++) {

                    String apiUrl =
                        "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx"
                      + "?ttbkey=" + TTB_KEY
                      + "&Query=" + keyword
                      + "&QueryType=Keyword"
                      + "&MaxResults=10"
                      + "&start=" + page
                      + "&SearchTarget=Book"
                      + "&output=js"
                      + "&Version=20131101";

                    URL url = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8")
                    );

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    JSONObject json = new JSONObject(sb.toString());
                    JSONArray items = json.getJSONArray("item");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject b = items.getJSONObject(i);

                        Book book = new Book(
                            b.optString("title"),
                            b.optString("author"),
                            b.optString("publisher"),
                            b.optString("categoryName"),
                            b.optString("cover")
                        );

                        try {
                            BookDAO.insert(book);
                            insertedCount++;
                            System.out.println("✅ 저장됨 [" + keyword + "]: " + book.getTitle());
                        } catch (Exception e) {
                            // UNIQUE 제약조건에 걸린 경우 (중복)
                            if (e.getMessage() != null &&
                                e.getMessage().contains("Duplicate")) {

                                skippedCount++;
                                System.out.println("⏭ 중복 스킵: " + book.getTitle());
                            } else {
                                // 진짜 에러면 출력
                                e.printStackTrace();
                            }
                        }
                    }

                    // 🔥 API 호출 제한 방지
                    Thread.sleep(400);
                }
            }

            System.out.println("=================================");
            System.out.println("📚 알라딘 데이터 수집 완료");
            System.out.println("➕ 새로 저장된 책: " + insertedCount + "권");
            System.out.println("⏭ 중복으로 스킵된 책: " + skippedCount + "권");
            System.out.println("=================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
