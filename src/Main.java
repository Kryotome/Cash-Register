import java.util.Scanner;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Cash Register
 * A simple text-based program to manage items and sales.
 *
 * This program allows users to:
 * 1. Add items to the inventory.
 * 2. Remove items from the inventory.
 * 3. Display the inventory.
 * 4. Register sales transactions.
 * 5. View sales history.
 * 6. Sort and display sales history.
 *
 * @author Tauhid Mahmud
 */
public class Main {

    // Constants for the item array
    public static final int ITEM_ID = 0;
    public static final int ITEM_COUNT = 1;
    public static final int ITEM_PRICE = 2;
    public static final int ITEM_COLUMN_SIZE = 3;
    public static final int INITIAL_ITEM_SIZE = 10;

    // Constants for the sales array
    public static final int SALE_ITEM_ID = 0;
    public static final int SALE_ITEM_COUNT = 1;
    public static final int SALE_ITEM_PRICE = 2;
    public static final int SALE_COLUMN_SIZE = 3;
    public static final int MAX_SALES = 1000;

    // Other constants
    public static final int MENU_ITEM_1 = 1;
    public static final int MENU_ITEM_2 = 2;
    public static final int MENU_ITEM_3 = 3;
    public static final int MENU_ITEM_4 = 4;
    public static final int MENU_ITEM_5 = 5;
    public static final int MENU_ITEM_6 = 6;
    public static final int MENU_ITEM_Q = -1;

    public static final int INITIAL_ITEM_NUMBER = 1000;

    public static final int MIN_ITEM_COUNT = 1;
    public static final int MAX_ITEM_COUNT = 10;
    public static final int MIN_ITEM_PRICE = 100;
    public static final int MAX_ITEM_PRICE = 1000;

    private static Scanner userInputScanner = new Scanner(System.in);

    /**
     * Injects an alternative Scanner for testing purposes.
     *
     * @param inputScanner the Scanner to use for input
     */
    public static void injectInput(final Scanner inputScanner) {
        userInputScanner = inputScanner;
    }

    /**
     * Displays the menu and returns the user's selection.
     *
     * Pseudocode:
     * - Print all menu options to the user.
     * - Wait for user input.
     * - If the user inputs "q", return the quit constant (-1).
     * - Parse the input as an integer and return the selection.
     * - If the input is invalid, display an error message and re-prompt.
     *
     * @return the selected menu option, or -1 if invalid input is encountered
     */

    public static int menu() {
        System.out.println("1. Insert items");
        System.out.println("2. Remove an item");
        System.out.println("3. Display a list of items");
        System.out.println("4. Register a sale");
        System.out.println("5. Display sales history");
        System.out.println("6. Sort and display sales history table");
        System.out.println("q. Quit");
        System.out.print("Your Selection: ");
        try {
            while (true) {
                try {
                    String userInput = userInputScanner.nextLine().trim();
                    if (userInput.equalsIgnoreCase("q")) {
                        return MENU_ITEM_Q;
                    }
                    return Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                } catch (NoSuchElementException e) {
                    System.out.println("No input found. Exiting input prompt.");
                    return -1;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Invalid input. Please try again.");
            return -1;
        }
    }

    /**
     * Reads user input and parses the first valid integer.
     *
     * Pseudocode:
     * - Check if the input stream has data.
     * - Read the next line of input and split it into parts.
     * - Parse each part to find the first valid integer.
     * - If no valid integer is found, display an error message.
     * - Return the integer or -1 for invalid input.
     *
     * @return the parsed integer, or -1 if invalid input is encountered
     */
    public static int input() {
        try {
            if (!userInputScanner.hasNextLine()) {
                System.out.println("No input found. Exiting input prompt.");
                return -1; // Indicate error or end of input
            }
            String userInput = userInputScanner.nextLine().trim();

        // Split the input by whitespace and check for the first valid integer
            String[] parts = userInput.split("\\s+");
            for (String part : parts) {
                try {
                    return Integer.parseInt(part); // Return the first valid integer
                } catch (NumberFormatException ignored) {
                // Ignore non-numeric parts and continue
                }
            }

            System.out.println("Invalid input. Please enter a valid number.");
            return -1; // Return error if no valid integer is found
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return input(); // Re-prompt for valid input
        }
    }

    /**
     * Checks if the items array has enough space to add new items.
     *
     * Pseudocode:
     * - Iterate through the items array and count free spaces (where ITEM_ID is 0).
     * - If free spaces are less than the number of items to add, return true (array is full).
     * - Otherwise, return false.
     *
     * @param items the array of items
     * @param noOfItems the number of items to add
     * @return true if there is insufficient space, false otherwise
     */
    public static boolean checkFull(final int[][] items, final int noOfItems) {
        int freeSpaces = 0;
        for (int[] item : items) {
            if (item[ITEM_ID] == 0) {
                freeSpaces++;
            }
        }
        return freeSpaces < noOfItems;
    }

    /**
     * Extends the items array to accommodate additional items.
     *
     * Pseudocode:
     * - Create a new array with size equal to the current array plus the number of new items.
     * - Copy all elements from the original array to the new array.
     * - Return the new array.
     *
     * @param items the original items array
     * @param noOfItems the number of items to add
     * @return the extended items array
     */
    public static int[][] extendArray(final int[][] items, final int noOfItems) {
        int newSize = items.length + noOfItems;
        int[][] extendedItems = new int[newSize][ITEM_COLUMN_SIZE];
        System.arraycopy(items, 0, extendedItems, 0, items.length);
        return extendedItems;
    }

    /**
     * Inserts new items into the inventory.
     *
     * Pseudocode:
     * - Check if there is enough space in the array.
     * - If not, extend the array.
     * - Generate random item details (ID, count, price).
     * - Insert items into the inventory.
     *
     * @param items the items array
     * @param lastItemId the ID of the last item
     * @param noOfItems the number of items to add
     * @return the updated items array
     */
    public static int[][] insertItems(final int[][] items, final int lastItemId, final int noOfItems) {
        int[][] updatedItems = items;

    // Extend the array if necessary
        if (checkFull(items, noOfItems)) {
            updatedItems = extendArray(items, noOfItems);
        }

        int currentId = lastItemId + 1;
        int remainingItems = noOfItems; // Track the number of items to add

        for (int i = 0; i < updatedItems.length && remainingItems > 0; i++) {
        // Check for empty slot to add an item
            if (updatedItems[i][ITEM_ID] == 0) {
                updatedItems[i][ITEM_ID] = currentId++;
                updatedItems[i][ITEM_COUNT] = (int) (Math.random() * (MAX_ITEM_COUNT - MIN_ITEM_COUNT + 1)) + MIN_ITEM_COUNT;
                updatedItems[i][ITEM_PRICE] = (int) (Math.random() * (MAX_ITEM_PRICE - MIN_ITEM_PRICE)) + MIN_ITEM_PRICE;
                remainingItems--; // Decrement after successfully adding an item
            }
        }

        return updatedItems;
    }

    /**
     * Removes an item from the inventory by its ID.
     *
     * Pseudocode:
     * - Iterate through the items array.
     * - If the specified item ID is found, reset its details (set ITEM_ID to 0).
     * - Return 0 to indicate success, or -1 if the item is not found.
     *
     * @param items the items array
     * @param itemId the ID of the item to remove
     * @return 0 if the item was removed successfully, -1 otherwise
     */
    public static int removeItem(final int[][] items, final int itemId) {
        for (int[] item : items) {
            if (item[ITEM_ID] == itemId) {
                item[ITEM_ID] = 0;
                item[ITEM_COUNT] = 0;
                item[ITEM_PRICE] = 0;
                return 0;
            }
        }
        return -1;
    }

    /**
     * Displays the current inventory of items.
     *
     * Pseudocode:
     * - Print a header for the table.
     * - Iterate through the items array.
     * - For each item with a non-zero ID, print its details (ID, count, price).
     *
     * @param items the array of items
     */
    public static void printItems(final int[][] items) {
        System.out.println("Item ID | Count | Price");
        System.out.println("-----------------------");
        for (int[] item : items) {
            if (item[ITEM_ID] != 0) {
                System.out.printf("%7d | %5d | %5d%n", item[ITEM_ID], item[ITEM_COUNT], item[ITEM_PRICE]);
            }
        }
    }

    /**
     * Registers a sale by reducing the item's count and adding a record to the sales history.
     *
     * Pseudocode:
     * - Iterate through the items array to find the specified item ID.
     * - Check if the item's count is sufficient for the sale.
     * - If sufficient:
     *   - Deduct the sold amount from the item's count.
     *   - Add the sale details (item ID, sold amount, total price) to the sales array.
     *   - Record the sale date in the salesDate array.
     *   - Return 0 to indicate a successful sale.
     * - If insufficient stock, return the current count to indicate how much is available.
     * - If the item is not found, return -1.
     *
     * @param sales the sales history array
     * @param salesDate the array of sale dates
     * @param items the inventory array
     * @param itemIdToSell the ID of the item to sell
     * @param amountToSell the quantity of the item to sell
     * @return 0 if the sale is successful, the current count if insufficient stock, or -1 if the item is not found
     */
    public static int sellItem(final int[][] sales, final Date[] salesDate, final int[][] items, final int itemIdToSell, final int amountToSell) {
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == itemIdToSell) {
                if (items[i][ITEM_COUNT] >= amountToSell) {
                    items[i][ITEM_COUNT] -= amountToSell;
                    for (int j = 0; j < sales.length; j++) {
                        if (sales[j][SALE_ITEM_ID] == 0) {
                            sales[j][SALE_ITEM_ID] = itemIdToSell;
                            sales[j][SALE_ITEM_COUNT] = amountToSell;
                            sales[j][SALE_ITEM_PRICE] = amountToSell * items[i][ITEM_PRICE];
                            salesDate[j] = new Date();
                            return 0;
                        }
                    }
                }
                return items[i][ITEM_COUNT];
            }
        }
        return -1;
    }

    /**
     * Displays the sales history.
     *
     * Pseudocode:
     * - Print a header for the sales table.
     * - Iterate through the sales array.
     * - For each sale with a non-zero ID, print its details (item ID, count, total price, date).
     *
     * @param sales the sales history array
     * @param salesDate the array of sale dates
     */
    public static void printSales(final int[][] sales, final Date[] salesDate) {
        System.out.println("Item ID | Count | Total Price | Date");
        System.out.println("-----------------------------------------");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i][SALE_ITEM_ID] != 0) {
                System.out.printf("%7d | %5d | %11d | %s%n", sales[i][SALE_ITEM_ID], sales[i][SALE_ITEM_COUNT], sales[i][SALE_ITEM_PRICE], salesDate[i]);
            }
        }
    }

    /**
     * Sorts the sales history table by item ID and displays it.
     *
     * Pseudocode:
     * - Use a bubble sort algorithm to sort the sales array based on the item ID.
     * - Ensure the salesDate array is sorted alongside the sales array.
     * - After sorting, call the printSales method to display the sorted sales history.
     *
     * @param sales the sales history array
     * @param salesDate the array of sale dates
     */
    public static void sortedTable(final int[][] sales, final Date[] salesDate) {
        for (int i = 0; i < sales.length; i++) {
            for (int j = i + 1; j < sales.length; j++) {
                if (sales[j][SALE_ITEM_ID] != 0 && sales[i][SALE_ITEM_ID] > sales[j][SALE_ITEM_ID]) {
                    int[] temp = sales[i];
                    sales[i] = sales[j];
                    sales[j] = temp;

                    Date tempDate = salesDate[i];
                    salesDate[i] = salesDate[j];
                    salesDate[j] = tempDate;
                }
            }
        }
        printSales(sales, salesDate);
    }

    /**
     * Returns the available stock for a given item ID.
     *
     * @param items The inventory array.
     * @param itemId The item ID to check.
     * @return The number of items in stock, or -1 if the item is not found.
     */
    public static int getAvailableStock(final int[][] items, final int itemId) {
        for (int[] item : items) {
            if (item[ITEM_ID] == itemId) {
                return item[ITEM_COUNT];  // Return available stock
            }
        }
        return -1;  // Item not found
    }

    public static void main(final String[] args) {
        int[][] items = new int[INITIAL_ITEM_SIZE][ITEM_COLUMN_SIZE];
        int[][] sales = new int[MAX_SALES][SALE_COLUMN_SIZE];
        Date[] saleDates = new Date[MAX_SALES];
        int lastItemNumber = INITIAL_ITEM_NUMBER;

        int selection;
        do {
            selection = menu();
            switch (selection) {
                case MENU_ITEM_1:
                    System.out.print("How many items to add? ");
                    int noOfItems = input();
                    items = insertItems(items, lastItemNumber, noOfItems);
                    lastItemNumber += noOfItems;
                    break;
                case MENU_ITEM_2:
                    System.out.print("Enter item ID to remove: ");
                    int itemIdToRemove = input();
                    if (removeItem(items, itemIdToRemove) == 0) {
                        System.out.println("Item removed successfully.");
                    } else {
                        System.out.println("Could not find");
                    }
                    break;
                case MENU_ITEM_3:
                    printItems(items);
                    break;
                case MENU_ITEM_4:
                    System.out.print("Enter item ID to sell: ");
                    int itemIdToSell = 0;
                    try {
                        itemIdToSell = input();
                    } catch (NoSuchElementException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        break;
                    }

                    boolean itemExists = false;
                    for (int[] item : items) {
                        if (item[ITEM_ID] == itemIdToSell) {
                            itemExists = true;
                            break;
                        }
                    }

                    if (!itemExists) {
                        System.out.println("Could not find");
                        break;
                    }
                    System.out.print("Enter amount to sell: ");
                    int amountToSell = 0;
                    try {
                        amountToSell = input();
                    } catch (NoSuchElementException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        break;
                    }

                    int availableStock = getAvailableStock(items, itemIdToSell);

                    if (availableStock == -1) {
                        System.out.println("Failed to sell specified amount");
                        break;
                    } else if (availableStock == 0) {
                        System.out.println("Failed to sell specified amount");
                        break;
                    } else if (availableStock < amountToSell) {
                        System.out.println("Failed to sell specified amount");
                        break;
                    }

                    int result = sellItem(sales, saleDates, items, itemIdToSell, amountToSell);

                    if ((result == 0) && (availableStock > amountToSell) && (amountToSell > 0)) {
                        System.out.println("Sale registered successfully.");
                    } else {
                        System.out.println("Failed to sell specified amount");
                    }
                    break;
                case MENU_ITEM_5:
                    printSales(sales, saleDates);
                    break;
                case MENU_ITEM_6:
                    sortedTable(sales, saleDates);
                    break;
                case MENU_ITEM_Q:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        } while (selection != MENU_ITEM_Q);
    }
}

