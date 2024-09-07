# Enum Types

An enum type is a special data type that enables for a variable to be a set of predefined constants. The variable must be equal to one of the values that have been predefined for it.

```java
enum Season {
    SPRING, SUMMER, AUTUMN, WINTER;

    public String getDescription() {
        switch (this) {
            case SPRING: return "Flowers bloom";
            case SUMMER: return "It's hot";
            case AUTUMN: return "Leaves fall";
            case WINTER: return "It's cold";
            default: return "Unknown season";
        }
    }
}

class EnumDemo {
    public static void main(String[] args) {
        Season current = Season.SUMMER;
        System.out.println("Current season: " + current);
        System.out.println("Description: " + current.getDescription());

        System.out.println("\nAll seasons:");
        for (Season s : Season.values()) {
            System.out.println(s + ": " + s.getDescription());
        }
    }
}
```
The enum declaration defines a class (called an *enum type*). The enum class body can include methods and other fields.

> One special method is the `values()` method, which returns an array containing all of the values of the enum in the order they are declared. This is commonly used with for-each to iterate over values in an enum type.
