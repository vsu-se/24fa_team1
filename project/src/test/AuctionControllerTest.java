
package test;

import application.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionControllerTest {
    private AuctionController auctionController;

    @BeforeEach
    public void setUp() {
        auctionController = new AuctionController();
    }

    @Test
    public void testAddAuction() {
        Auction auction = new Auction("Electronics Auction", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        auctionController.addAuction(auction);
        List<Auction> auctions = auctionController.getAllAuctions();
        assertTrue(auctions.contains(auction), "Auction should be added to the controller.");
    }

    @Test
    public void testFindAuctionByName() {
        Auction auction = new Auction("Furniture Auction", LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        auctionController.addAuction(auction);
        Auction foundAuction = auctionController.findAuctionByName("Furniture Auction");
        assertNotNull(foundAuction, "Should find the auction by name.");
        assertEquals("Furniture Auction", foundAuction.getName(), "Auction name should match.");
    }

    @Test
    public void testAddItemToAuction() {
        Auction auction = new Auction("Books Auction", LocalDateTime.now(), LocalDateTime.now().plusDays(3));
        auctionController.addAuction(auction);
        Item item = new Item("Book Title", "1kg", "Interesting book", new Category("Books"),
                "New", "Tag1", "Tag2", "Tag3", null, null, 20.0, 0.0, true, false);
        boolean success = auctionController.addItemToAuction("Books Auction", item);
        assertTrue(success, "Item should be added to the auction.");
        Auction updatedAuction = auctionController.findAuctionByName("Books Auction");
        assertTrue(updatedAuction.getItems().contains(item), "Auction should contain the added item.");
    }
}
