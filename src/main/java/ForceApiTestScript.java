import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class ForceApiTestScript {
    public static void main(String[] args) {
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
                { Cell cell = cellIterator.next();
                    String offerId=cell.getStringCellValue();
                    conn=getHttpConnection("http://gatekeeper-sync.prod.gatekeeper.catdev.prod.walmart.com/gatekeeper-sync/services/biz/publish/offer/force/"+offerId,"DELETE");
                    try( DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
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

    public static HttpURLConnection getHttpConnection(String url, String type){
        URL uri = null;
        HttpURLConnection con = null;
        try{
            uri = new URL(url);
            con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod(type); //type: POST, PUT, DELETE, GET
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(60000); //60 secs
            con.setReadTimeout(60000); //60 secs
            con.setRequestProperty("Accept-Encoding", "Your Encoding");
            con.setRequestProperty("Content-Type", "Your Encoding");
        }catch(Exception e){
            System.out.println( "connection i/o failed" );
        }
        return con;
    }
}
