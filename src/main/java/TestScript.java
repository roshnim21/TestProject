import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class TestScript {


    public static void main(String[] args) throws IOException {

        // Create a neat value object to hold the URL
        HttpURLConnection conn = null;

        // Now it's "open", we can set the request method, headers etc.
        try {
            FileInputStream file = new FileInputStream(new File("src/main/resources/test.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                //iterate columns
                while (cellIterator.hasNext())
                {
                    conn=getHttpURLConnection();
                    Cell cell = cellIterator.next();
                    String offerId=cell.getStringCellValue();
                    try( DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                        wr.write( offerId.getBytes(StandardCharsets.UTF_8) );
                    }
                    String line;
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }

                }
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://gatekeeper-sync.stg0.gatekeeper.catdev.prod.walmart.com/gatekeeper-sync/services/biz/publish/execute/offer");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setUseCaches(false);
        return conn;
    }

}

