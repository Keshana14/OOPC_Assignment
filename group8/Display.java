import java.util.LinkedList;
import java.util.ListIterator;

public class Display{
  private LinkedList<Record> history;

  public Display()
  {
    history = new LinkedList<Record>();
  }

  public void addJob(String vehicle, String mechanic, int time, double cost)
  {
    history.add(new Record(vehicle, mechanic, time, cost));
  }

  public void outputStatusTable()
  {
    System.out.println("\nVehicle\t\tMechanic\tTime\tCost");
    System.out.println("------------------------------------------");

    // Check whether the list contains records before reading them.
    if (history.isEmpty())
    {
      System.out.println("No jobs currently recorded.");
    }
    else
    {
      ListIterator<Record> iter = history.listIterator();

      while (iter.hasNext())
      {
        Record record = iter.next();
        System.out.println(record);
      }
    }

    System.out.println();
  }

  public void outputSummary(int completed, int waiting, int revenue)
  {
    System.out.println("Completed: " + completed);
    System.out.println("Waiting: " + waiting);
    System.out.println("Revenue: $" + revenue);
  }

  public void outputIterationNumber(int iteration)
  {
    System.out.println("Iteration: " + iteration);
  }

  public void outputAddedToWaitingList(String vehicle, String reason, String mechanic)
  {
    System.out.println(vehicle + " added to waiting list: " + reason + " " + mechanic);
  }

  public void outputAssignedToMechanic(String vehicle, String mechanic, String time, String cost)
  {
    System.out.println(vehicle + " assigned to " + mechanic + ". Time: " + time + ", Cost: " + cost);
  }

  public void outputRepairedByMechanic(String vehicle, String mechanic, String time, String cost)
  {
    System.out.println(vehicle + " repaired by " + mechanic + ". Time: " + time + ", Cost: " + cost);
  }
}