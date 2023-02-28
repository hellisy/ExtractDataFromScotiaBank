import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ScotiaBankScraper {
    public static void main(String[] args) throws IOException {
        // Connect to the online banking website
        String url = "https://www.scotiabank.com/ca/en/personal.html";
        Document doc = Jsoup.connect(url).get();

        // Extract the data you want from the website
        ArrayList<String> accountNames = new ArrayList<>();
        Elements accountNameElements = doc.select("div.account-name");
        for (Element accountNameElement : accountNameElements) {
            String accountName = accountNameElement.text();
            accountNames.add(accountName);
        }

        ArrayList<Double> accountBalances = new ArrayList<>();
        Elements accountBalanceElements = doc.select("div.account-balance");
        for (Element accountBalanceElement : accountBalanceElements) {
            String accountBalanceString = accountBalanceElement.text();
            double accountBalance = Double.parseDouble(accountBalanceString.replaceAll("[^\\d.]+", ""));
            accountBalances.add(accountBalance);
        }

        // Write the data to an Excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Account Data");

        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Account Name");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Current Balance");

        for (int i = 0; i < accountNames.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell1 = row.createCell(0);
            cell1.setCellValue(accountNames.get(i));
            cell2 = row.createCell(1);
            cell2.setCellValue(accountBalances.get(i));
        }

        // Save the Excel file
        FileOutputStream outputStream = new FileOutputStream(new File("ScotiabankData.xlsx"));
        workbook.write(outputStream);
        workbook.close();
    }
}
