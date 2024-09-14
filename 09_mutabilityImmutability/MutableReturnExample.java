import java.util.ArrayList;
import java.util.List;

public class MutableReturnExample {
    private static List<String> names = new ArrayList<>();

    public static void main(String[] args) {
        // Add some initial names
        addName("Alice");
        addName("Bob");

        // Get the list of names and modify it - this really should only
        // add to a temporary copy of names but we end up modify the class variable
        // directly
        List<String> currentNames = getNames();
        currentNames.add("Charlie");

        // Print the names
        System.out.println("Names: " + getNames());

        // Try to add another name using the original method
        addName("David");

        // Print the names again
        System.out.println("Names after adding David: " + getNames());
    }

    public static void addName(String name) {
        names.add(name);
    }

    // Bad practice: returning mutable object
    public static List<String> getNames() {
        return names;
    }

    // // correct implementation: return a copy
    // public static List<String> getNames() {
    // return new ArrayList<>(names); // Returns a copy
    // }

}