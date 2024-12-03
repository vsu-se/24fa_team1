package application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenceUtil {

    private static final String SAVE_FILE_PATH = "system_state.txt";

    // Save the state to a text file
    public static void saveState(List<Category> categories, List<Item> items, double buyerPremium, double sellerCommission) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE_PATH))) {
            // Save categories
            writer.println("CATEGORIES");
            for (Category category : categories) {
                writer.println(category.getName());
            }

            // Save items
            writer.println("ITEMS");
            for (Item item : items) {
                writer.printf("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%f|%f|%b%n",
                        item.getTitle(), item.getWeight(), item.getDescription(), item.getCategory().getName(),
                        item.getCondition(), item.getTag1(), item.getTag2(), item.getTag3(),
                        item.getStartDate(), item.getEndDate(), item.getBuyItNowPrice(),
                        item.getCurrentBid(), item.isActive());
            }

            // Save buyer premium and seller commission
            writer.println("BUYER_PREMIUM");
            writer.println(buyerPremium);
            writer.println("SELLER_COMMISSION");
            writer.println(sellerCommission);

            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save system state.");
        }
    }

    // Load the state from a text file
    public static SystemState loadState() {
        List<Category> categories = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        double buyerPremium = 0.0;
        double sellerCommission = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE_PATH))) {
            String line;
            String currentSection = "";

            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case "CATEGORIES":
                        currentSection = "CATEGORIES";
                        break;
                    case "ITEMS":
                        currentSection = "ITEMS";
                        break;
                    case "BUYER_PREMIUM":
                        currentSection = "BUYER_PREMIUM";
                        break;
                    case "SELLER_COMMISSION":
                        currentSection = "SELLER_COMMISSION";
                        break;
                    default:
                        switch (currentSection) {
                            case "CATEGORIES":
                                categories.add(new Category(line));
                                break;
                            case "ITEMS":
                                String[] itemData = line.split("\\|");
                                if (itemData.length == 13) {
                                    Category category = categories.stream()
                                            .filter(c -> c.getName().equals(itemData[3]))
                                            .findFirst()
                                            .orElse(new Category(itemData[3]));
                                    Item item = new Item(itemData[0], itemData[1], itemData[2], category,
                                            itemData[4], itemData[5], itemData[6], itemData[7],
                                            LocalDateTime.parse(itemData[8]), LocalDateTime.parse(itemData[9]),
                                            Double.parseDouble(itemData[10]), Double.parseDouble(itemData[11]));
                                    item.setHasBidder(Boolean.parseBoolean(itemData[12]));
                                    items.add(item);
                                }
                                break;
                            case "BUYER_PREMIUM":
                                buyerPremium = Double.parseDouble(line);
                                break;
                            case "SELLER_COMMISSION":
                                sellerCommission = Double.parseDouble(line);
                                break;
                        }
                }
            }

            System.out.println("System state loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load system state.");
        }

        return new SystemState(categories, items, buyerPremium, sellerCommission);
    }

    // Nested static class to represent the state of the system
    public static class SystemState {
        private List<Category> categories;
        private List<Item> items;
        private double buyerPremium;
        private double sellerCommission;

        public SystemState(List<Category> categories, List<Item> items, double buyerPremium, double sellerCommission) {
            this.categories = categories;
            this.items = items;
            this.buyerPremium = buyerPremium;
            this.sellerCommission = sellerCommission;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public List<Item> getItems() {
            return items;
        }

        public double getBuyerPremium() {
            return buyerPremium;
        }

        public double getSellerCommission() {
            return sellerCommission;
        }
    }
}
