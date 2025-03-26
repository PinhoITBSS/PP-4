import java.io.*;
import java.net.*;
import java.util.*;

public class Mavenproject1 {
    public static void main(String[] args) {
        String url = "https://drive.google.com/uc?export=download&id=14DWF2kG0hGD3hYJjAcsexOCGedVfrv3r";

        List<Product> productList = new ArrayList<>();
        Set<String> uniqueCountries = new HashSet<>();
        Map<String, Integer> productSalesCount = new HashMap<>();
        Map<String, Double> revenuePerCountry = new HashMap<>();
        Map<String, Product> productByStockCode = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { // Skip header
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                try {
                    String stockCode = values[1].trim();
                    String description = values[2].trim();
                    double unitPrice = Double.parseDouble(values[5].trim());  // Harus angka
                    int quantity = Integer.parseInt(values[3].trim());       // Harus angka
                    String country = values[7].trim();

                    Product product = new Product(stockCode, description, unitPrice, quantity, country);
                    productList.add(product);

                    // Add to unique country set
                    uniqueCountries.add(country);

                    // Update sales count per StockCode
                    productSalesCount.put(stockCode, productSalesCount.getOrDefault(stockCode, 0) + quantity);

                    // Update revenue per country
                    revenuePerCountry.put(country, revenuePerCountry.getOrDefault(country, 0.0) + product.getTotalRevenue());

                    // Store product by StockCode (last occurrence kept)
                    productByStockCode.put(stockCode, product);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Skipping invalid row: " + line);
                }
            }

            System.out.println("============================================================");
            System.out.println("Dataset berhasil dibaca! Jumlah produk: " + productList.size());
            System.out.println("============================================================");
            System.out.println("Daftar Negara Unik (5 Pertama):");
            uniqueCountries.stream().limit(5).forEach(System.out::println);
            System.out.println("============================================================");
            System.out.println("Total Produk Terjual Berdasarkan StockCode (5 Pertama):");
            productSalesCount.entrySet().stream().limit(5).forEach(entry -> 
                System.out.println("StockCode: " + entry.getKey() + " | Total Terjual: " + entry.getValue()));
            System.out.println("============================================================");
            System.out.println("Total Pendapatan Per Negara (5 Pertama):");
            revenuePerCountry.entrySet().stream().limit(5).forEach(entry -> 
                System.out.println("Negara: " + entry.getKey() + " | Pendapatan: " + entry.getValue()));
            System.out.println("============================================================");
            System.out.println("Produk Berdasarkan StockCode (5 Pertama):");
            productByStockCode.entrySet().stream().limit(5).forEach(entry -> 
                System.out.println("StockCode: " + entry.getKey() + " | Produk: " + entry.getValue()));
            System.out.println("============================================================");
            System.out.println("Contoh 5 Produk Dari Daftar:");
            productList.stream().limit(5).forEach(System.out::println);
            System.out.println("============================================================");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

class Product {
    private String stockCode;
    private String description;
    private double unitPrice;
    private int quantity;
    private String country;

    public Product(String stockCode, String description, double unitPrice, int quantity, String country) {
        this.stockCode = stockCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.country = country;
    }

    public String getStockCode() {
        return stockCode;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCountry() {
        return country;
    }

    public double getTotalRevenue() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return "StockCode: " + stockCode + ", Description: " + description + ", UnitPrice: " + unitPrice + ", Quantity: " + quantity + ", Country: " + country;
    }
}