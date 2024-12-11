package application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuctionStatePersistence {

    private static final String SAVE_FILE_PATH = "system_state.txt";
    public static boolean canWrite = true;

    // Save the state to a text file
    public static void saveState(ObservableList<String> categories, ObservableList<Auction> auctions, double buyersPremium, double sellerCommission) {
    	if(!canWrite) {
    		return;
    	}
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE_PATH))) {

            // Save categories
            writer.println("CATEGORIES");
            for (String category : categories) {
                writer.println(category);
            }

            // Save items
            writer.println("ITEMS");
            for (Auction auction : auctions) {
                writer.printf("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%f|%f|%b%n",
                		auction.getItem().getTitle(), auction.getItem().getWeight(), auction.getItem().getDescription(), auction.getCategory(),
                		auction.getItem().getCondition(), auction.getTag1(), auction.getTag2(), auction.getTag3(),
                		auction.getStartDate(), auction.getEndDate(), auction.getBuyItNowPrice(),
                		auction.getCurrentBid(), auction.isActive());

                // Save bid history
                for (Bid bid : auction.getBidHistory()) {
                    writer.printf("BID|%f|%s|%b%n", bid.getAmount(), bid.getTime(), bid.getIsBIN());
                }
                writer.println("END_BID_HISTORY");
            }

            // Save buyer premium and seller commission
            writer.println("BUYERS_PREMIUM");
            writer.println(buyersPremium);
            writer.println("SELLER_COMMISSION");
            writer.println(sellerCommission);

            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save system state.");
        }
    }

    // Load the state from a text file
    public static AuctionSystem loadState() {
        ObservableList<String> categories = FXCollections.observableArrayList();;
        ObservableList<Auction> auctions = FXCollections.observableArrayList();;
        double buyersPremium = 0.0;
        double sellerCommission = 0.0;
        LocalDateTime currentTime = LocalDateTime.now();
        SystemClock clock = new SystemClock();

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
                    case "BUYERS_PREMIUM":
                        currentSection = "BUYERS_PREMIUM";
                        break;
                    case "SELLER_COMMISSION":
                        currentSection = "SELLER_COMMISSION";
                        break;
                    default:
                        switch (currentSection) {
                            case "CATEGORIES":
                                categories.add(line);
                                break;
                            case "ITEMS":
                                if (line.startsWith("BID")) {
                                    String[] bidData = line.split("\\|");
                                    Auction lastAuction = auctions.get(auctions.size() - 1);
                                    lastAuction.addBid(new Bid(Double.parseDouble(bidData[1]), LocalDateTime.parse(bidData[2]), Boolean.parseBoolean(bidData[3])));
                                    lastAuction.setHasBidder(true);
                                } else if (line.startsWith("END_BID_HISTORY")) {
                                    break;
                                } else {
                                    String[] auctionData = line.split("\\|");
                                    String category = categories.stream()
                                            .filter(c -> c.equals(auctionData[3]))
                                            .findFirst()
                                            .orElse(auctionData[3]);
                                    Item item = new Item(auctionData[0], auctionData[1], auctionData[2], auctionData[4]);
									Auction auction = new Auction(item, category, auctionData[5], auctionData[6], auctionData[7],
                                            LocalDateTime.parse(auctionData[8]), LocalDateTime.parse(auctionData[9]),
                                            Double.parseDouble(auctionData[10]), Double.parseDouble(auctionData[11]), clock);
                                    auctions.add(auction);
                                }
                                break;
                            case "BUYERS_PREMIUM":
                                buyersPremium = Double.parseDouble(line);
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
		return new AuctionSystem(clock, buyersPremium, sellerCommission, categories, auctions);
    }
}