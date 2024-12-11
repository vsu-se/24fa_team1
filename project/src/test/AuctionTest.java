package test;
import application.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {

    private Auction auction;
    private Item item;
    private SystemClock clock;

    @BeforeEach
    void setUp() {
    	AuctionStatePersistence.canWrite = false;
        item = new Item("Sample Item", "23", "long", "new"); // Updated constructor for Item
        clock = new SystemClock(); // Custom TestClock for controlling time in tests
        clock.setTime(LocalDateTime.now());

        auction = new Auction(
                item,
                "Electronics",
                "Tag1",
                "Tag2",
                "Tag3",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                500.0,
                100.0,
                clock
        );
    }

    @Test
    void testIsActive_TrueWhenWithinAuctionPeriod() {
        assertTrue(auction.isActive());
    }

    @Test
    void testIsActive_FalseWhenPastEndDate() {
        clock.setTime(LocalDateTime.now().plusDays(2));
        auction.checkAndSetInactive();
        assertFalse(auction.isActive());
    }

    @Test
    void testCheckAndSetInactive_DeactivatesWhenPastEndDate() {
        clock.setTime(LocalDateTime.now().plusDays(2));
        auction.checkAndSetInactive();
        assertFalse(auction.isActive());
    }

    @Test
    void testPlaceBid_SuccessfulWhenHigherBid() {
        boolean result = auction.placeBid(150.0);
        assertTrue(result);
        assertEquals(150.0, auction.getCurrentBid());
    }

    @Test
    void testPlaceBid_UnsuccessfulWhenLowerOrEqualBid() {
        boolean result = auction.placeBid(100.0);
        assertFalse(result);
        assertEquals(100.0, auction.getCurrentBid());

        result = auction.placeBid(50.0);
        assertFalse(result);
        assertEquals(100.0, auction.getCurrentBid());
    }

    @Test
    void testGetSellersCommission() {
        double commission = auction.getSellersCommission(10.0);
        assertEquals(10.0, commission);
    }

    @Test
    void testGetBuyersPremium() {
        double premium = auction.getBuyersPremium(15.0);
        assertEquals(15.0, premium);
    }

    @Test
    void testSetHasBidder() {
        auction.setHasBidder(true);
        assertTrue(auction.hasBidder());

        auction.setHasBidder(false);
        assertFalse(auction.hasBidder());
    }

    @Test
    void testGetShippingCost() {
        assertEquals(10.0, auction.getShippingCost());
    }

}