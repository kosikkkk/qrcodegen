import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class qrcode {
    // Klasa do przechowywania informacji o kodach QR
    static class QRRecord {
        private String userName;
        private String url;
        private String filePath;
        public QRRecord(String userName, String url, String filePath) {
            this.userName = userName;
            this.url = url;
            this.filePath = filePath;
        }
        @Override
        public String toString() {
            return "Użytkownik: " + userName + ", URL: " + url + ", Plik: " + filePath;
        }
    }
    private static List<QRRecord> qrHistory = new ArrayList<>();
    // Funkcja do wprowadzania imienia użytkownika
    private static String getUserName() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Wpisz imię: ");
        return scan.nextLine();
    }
    // Funkcja do generowania losowego URL
    private static String generateRandomURL() {
        List<String> domains = Arrays.asList("youtube.com", "random.org", "san.edu.pl",
                "chatgpt.com",
                "www.pinterest.com/pin/140948663331042615",
                "www.pinterest.com/pin/617133955223540456",
                "www.youtube.com/watch?v=gsZfueGslWI&ab_channel=VladislavBrabus",
                "youtu.be/ioxEuSpUMkQ?si=4b-T25z6Nvi9qvdZ",
                "youtu.be/XutKt6GrvhU?si=rqMkDR1Yz9b69iv9",
                "www.pinterest.com/pin/1125477763132909238");
        return "https://" + domains.get(new Random().nextInt(domains.size()));
    }
    // Funkcja do wprowadzania URL przez użytkownika
    private static String getURL() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Wpisz URL: ");
        return scan.nextLine();
    }
    // Sprawdzanie poprawności URL
    private static boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    // Funkcja do generowania kodu QR
    private static void generateQRCode(String userName, String URL, String filePath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(URL, BarcodeFormat.QR_CODE,
                    500, 500);
            File outputFile = new File(filePath);
            // Sprawdzanie poprawności ścieżki
            File parentDir = outputFile.getParentFile();
            if (parentDir == null || !parentDir.exists()) {
                throw new IOException("Ścieżka zapisu kodu QR jest nieprawidłowa lub nie istnieje");
            }
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", outputFile.toPath());
            System.out.println("Kod QR zapisano jako " + filePath);
            qrHistory.add(new QRRecord(userName, URL, filePath));
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu kodu QR: " + e.getMessage());
        } catch (WriterException e) {
            System.out.println("Błąd podczas generowania kodu QR: " + e.getMessage());
        }
    }
    // Funkcja do wyświetlania historii kodów QR
    private static void displayQRHistory() {
        if (qrHistory.isEmpty()) {
            System.out.println("Historia kodów QR jest pusta.");
        } else {
            System.out.println("Historia kodów QR:");
            for (QRRecord record : qrHistory) {
                System.out.println(record);
            }
        }
    }
    // Główna logika programu
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String userName = getUserName(); // Pobranie imienia użytkownika
        while (true) {
            System.out.println("\nMenu:\n1. Wygeneruj kod QR dla podanego URL\n2. Wygeneruj kod QR dla losowego URL\n3. Wyświetl historię\n4. Wyjście");
            System.out.print("Wybierz opcję: ");
            String choice = scan.nextLine();
            switch (choice) {
                case "1":
                    String userURL = getURL();
                    if (!isValidURL(userURL)) {
                        System.out.println("Nieprawidłowy format URL. Spróbuj ponownie.");
                        break;
                    }
                    System.out.print("Podaj ścieżkę do zapisu kodu QR (np. C:/Users/User/Desktop/qr_code.png): ");
                    String filePath1 = scan.nextLine();
                    generateQRCode(userName, userURL, filePath1);
                    break;
                case "2":
                    String randomURL = generateRandomURL();
                    System.out.println("Wygenerowano losowy URL: " + randomURL);
                    System.out.print("Podaj ścieżkę do zapisu kodu QR (np. C:/Users/User/Desktop/qr_code.png): ");
                    String filePath2 = scan.nextLine();
                    generateQRCode(userName, randomURL, filePath2);
                    break;
                case "3":
                    displayQRHistory();
                    break;
                case "4":
                    System.out.println("Do widzenia!");
                    scan.close();
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }
}
