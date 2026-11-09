public class Mechanic 
{
    private String name;
    private int id;
    private int experience;
    private int daysRemaining;

    public Mechanic()
    {
        this.name = "";
        this.id = 0;
        this.experience = 0;
        this.daysRemaining = 0;
    }

    public Mechanic(String name, int experience, int id)
    {
        this.name = name;
        this.id = id;
        this.experience = experience;
        daysRemaining = 0;
    }

    public String name()
    {
        return name;
    }

    public int id()
    {
        return id;
    }

    public int experience()
    {
        return experience;
    }

    public int hourlyRate()
    {
        switch(experience)
        {
            case 1: return 20;
            case 2: return 60;
            case 3: return 100;
            default: return 0;
        }
    }

    public int minimumRepairDays()
    {
        switch(experience)
        {
            case 1: return 20;
            case 2: return 15;
            case 3: return 10;
            default: return 0;
        }
    }

    public double repairCost(int days)
    {
        return hourlyRate() * 8.0 * days;
    }

    public boolean isAvailable()
    {
        return daysRemaining == 0;
    }

    public void assignJob(int days)
    {
        daysRemaining = days;
    }

    public void advanceDay()
    {
        if(daysRemaining > 0)
        {
            daysRemaining--;
        }
    }

    public int daysRemaining()
    {
        return daysRemaining;
    }

    public String toString()
    {
        return name + " (ID: " + id + ")";
    }
}
