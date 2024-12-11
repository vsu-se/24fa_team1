import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;

public class AuctionSystemTest {

    private AuctionSystem auctionSystem;

    @BeforeEach
    public void setUp() {
        auctionSystem = new AuctionSystem();
    }

    @Test
    public void testGetCategories() {
        ObservableList<String> categories = auctionSystem.getCategories();
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testGetTime() {
        LocalDateTime time = auctionSystem.getTime();
        assertNotNull(time);
    }

    @Test
    public void testGetClock() {
        SystemClock clock = auctionSystem.getClock();
        assertNotNull(clock);
    }

    @Test
    public void testGetAuctions() {
        ObservableList<Auction> auctions = auctionSystem.getAuctions();
        assertNotNull(auctions);
        assertTrue(auctions.isEmpty());
    }

    @Test
    public void testGetAndSetBuyersPremium() {
        auctionSystem.setBuyersPremium(10.0);
        assertEquals(10.0, auctionSystem.getBuyersPremium());
    }

    @Test
    public void testGetAndSetSellerCommission() {
        auctionSystem.setSellerCommission(5.0);
        assertEquals(5.0, auctionSystem.getSellerCommission());
    }

    @Test
    public void testGetConcludedCategories() {
        ObservableList<String> concludedCategories = auctionSystem.getConcludedCategories();
        assertNotNull(concludedCategories);
        assertTrue(concludedCategories.isEmpty());
    }

    @Test
    public void testAddAuction() {
        Auction auction = new Auction(new Item("Item1", "1.0", "Description", "New"), "Category1", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 0.0, auctionSystem.getClock());
        auctionSystem.addAuction(auction);
        assertTrue(auctionSystem.getAuctions().contains(auction));
    }
}