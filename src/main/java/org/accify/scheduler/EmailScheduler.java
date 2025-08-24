package org.accify.scheduler;

/*
import jakarta.mail.*;
import jakarta.mail.search.*;
import org.accify.entity.Freight;
import org.accify.repo.FreightRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


@Component
public class EmailScheduler {

    @Autowired
    private FreightRepository freightRepository;

    private static final String TARGET_SUBJECT = "LORRY FREIGHT";

    //@Scheduled(cron = "0 0 18 * * ?") // Every day at 6:00 PM
    //@Scheduled(cron = "0 * * * * *") // Every minute
    public void checkEmail() {
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imap.host", "imap.gmail.com");
            props.put("mail.imap.port", "993");
            props.put("mail.imap.ssl.enable", "true");
            Session session = Session.getInstance(props);
            Store store = session.getStore();
            //store.connect("imap.gmail.com", "iamworldchanger25@gmail.com", "gmaw tyeg vmgb vuoz");
            store.connect("imap.gmail.com", "emailId@gmail.com", "password");
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // Set today at 00:00 AM
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            // Search terms
            SubjectTerm subjectTerm = new SubjectTerm(TARGET_SUBJECT);
            ReceivedDateTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GE, today); // Greater or equal to today
            SearchTerm combinedSearch = new AndTerm(new SearchTerm[]{subjectTerm, dateTerm});
            Message[] messages = inbox.search(combinedSearch);

            System.out.println("📨 Matching unread messages from today: " + messages.length);
            for (Message msg : messages) {
                System.out.println("✅ Found email: " + msg.getSubject());
                if (msg.getContent() instanceof Multipart multipart) {
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && part.getFileName().toLowerCase().endsWith(".xlsx")) {
                            System.out.println("📎 Found Excel attachment: " + part.getFileName());
                            InputStream is = part.getInputStream();
                            processExcelFile(is);
                            msg.setFlag(Flags.Flag.SEEN, true); // Mark as read
                        }
                    }
                }
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processExcelFile(InputStream is) throws IOException {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        int currentSerial = (int) freightRepository.count(); // Assuming `id` is SERIAL
        int serial = currentSerial + 1;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Freight entry = new Freight();

            // Nullable date
            Cell dateCell = row.getCell(0);
            if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                entry.setDate(dateCell.getLocalDateTimeCellValue().toLocalDate());
            } else {
                entry.setDate(null);
            }

            // Strings (nullable)
            entry.setConsignor(getStringCell(row.getCell(1)));
            entry.setConsignee(getStringCell(row.getCell(2)));
            entry.setFromLocation(getStringCell(row.getCell(3)));
            entry.setToLocation(getStringCell(row.getCell(4)));
            entry.setVehicleNo(getStringCell(row.getCell(5)));

            // Numerics (nullable)
            entry.setWeight(getNumericCell(row.getCell(6)));
            entry.setAmount(getNumericCell(row.getCell(7)));
            entry.setGst(getNumericCell(row.getCell(8)));
            entry.setTotal(getNumericCell(row.getCell(9)));

            freightRepository.save(entry);
        }

        workbook.close();
    }

    // Safe string extraction
    private String getStringCell(Cell cell) {
        return (cell != null) ? cell.toString().trim() : null;
    }

    // Safe numeric extraction
    private Double getNumericCell(Cell cell) {
        try {
            return (cell != null && cell.getCellType() == CellType.NUMERIC) ? cell.getNumericCellValue() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
 */
