public class Vehicle 
{
    private String model;
    private String type;
    private String company;
	private int damage;

    public Vehicle()
    {
        this.company = "";
        this.model = "";
        this.type = "";
		this.damage = 0;
    }

    public Vehicle(String company, String model, String type, int damage)
    {
        this.company = company;
        this.model = model;
        this.type = type;
		this.damage = damage;
    }

    public String company()
    {
        return company;
    }

    public String type()
    {
        return type;
    }

    public String model()
    {
        return model;
    }
	
	public int damage()
	{
		return damage;
	}

    public double repairCost()
    {
        switch(model.toLowerCase())
        {
            case "sedan": return 150;
            case "electric":
                if(type.equalsIgnoreCase("Car")) return 300;
                if(type.equalsIgnoreCase("Van")) return 700;
                break;
            case "hybrid": return 200;
            case "hatchback": return 100;
            case "diesel": return 500;
            case "gas": return 600;
            case "flatbed": return 1000;
            case "dump": return 2000;
            case "container": return 4000;
        }

        return 0;
    }

    public double partsCost()
    {
        switch(model.toLowerCase())
        {
            case "sedan": return 200;
            case "electric":
                if(type.equalsIgnoreCase("Car")) return 400;
                if(type.equalsIgnoreCase("Van")) return 750;
                break;
            case "hybrid": return 300;
            case "hatchback": return 100;
            case "diesel": return 550;
            case "gas": return 600;
            case "flatbed": return 4000;
            case "dump": return 6000;
            case "container": return 9000;
        }

        return 0;
    }

    public int minimumRepairDays()
    {
        switch(model.toLowerCase())
        {
            case "sedan": return 5;
            case "electric":
                if(type.equalsIgnoreCase("Car")) return 10;
                if(type.equalsIgnoreCase("Van")) return 20;
                break;
            case "hybrid": return 7;
            case "hatchback": return 5;
            case "diesel": return 15;
            case "gas": return 10;
            case "flatbed": return 25;
            case "dump": return 30;
            case "container": return 40;
        }

        return 0;
    }

    public String toString()
    {
        return company + " " + model + " " + type;
    }
}