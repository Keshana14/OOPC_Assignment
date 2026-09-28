/*
 * DO NOT MODIFY THIS CLASS EXCEPT AS INSTRUCTED IN THE ASSIGNMENT DOCUMENTATION.
 */
public class Simulation 
{
    public static void main(String[] args) 
    {
        Shop shop = new Shop("group8");
        Profile profile = new Profile("group8");

        try{
            System.out.println("ID: " + profile.getFullProfileString());
            shop.simulate();
        }
        catch( OutOfBoundIndexNumber e)
        {
            System.out.println("You exceeded the number of mechanics or vehicles when using the DatabaseVM class.");
        }
    }
}
