import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Shop
{
	private Mechanic mechanics[];
	private Vehicle vehicles[];
	private Display output;
	private Menu menu;
	
	// Tracks which vehicle each mechanic is repairing.
	private int assignedVehicle[];

	// Stores repair information for each mechanic.
	private int assignedDays[];
	private double assignedCost[];

	// Vehicle status: 0 = waiting, 1 = under repair, 2 = completed.
	private int vehicleStatus[];

	private ArrayList<Integer> waitingList;

	private int completedVehicles;
	private int iteration;
	private int nextVehicleIndex;

	private double totalRevenue;
	private boolean jobsCreated;

	//private boolean simulationStarted;

	public Shop()
	{
		this("group8");
	}

	public Shop(String prompt)
	{
		mechanics = new Mechanic[DatabaseConstants.TOTAL_MECHANICS];
		vehicles = new Vehicle[DatabaseConstants.TOTAL_VEHICLES];

		output = new Display();
		menu = new Menu(prompt);
		
		assignedVehicle = new int[DatabaseConstants.TOTAL_MECHANICS];
		assignedDays = new int[DatabaseConstants.TOTAL_MECHANICS];
		assignedCost = new double[DatabaseConstants.TOTAL_MECHANICS];

		vehicleStatus = new int[DatabaseConstants.TOTAL_VEHICLES];

		waitingList = new ArrayList<Integer>();

		completedVehicles = 0;
		iteration = 0;
		totalRevenue = 0;
		jobsCreated = false;
		//simulationStarted = false;

		for(int i = 0; i < mechanics.length; i++)
		{
			mechanics[i] = new Mechanic();
			assignedVehicle[i] = -1;
		}

		for(int i = 0; i < vehicles.length; i++)
		{
			vehicles[i] = new Vehicle();
		}
	}

	public void simulate() throws OutOfBoundIndexNumber
	{
		// Run the simulation.
		setup();
		run();
	}

	private void setup() throws OutOfBoundIndexNumber
	{
		DatabaseVM data = new DatabaseVM();

		for(int i = 0; i < mechanics.length; i++)
		{
			MechanicData md = data.mechanic(i); 

			mechanics[i] = new Mechanic(
				md.name(), 
				md.experience(), 
				md.id());
		}

		for(int i = 0; i < vehicles.length; i++)
		{
			VehicleData vd = data.vehicle(i);

			vehicles[i] = new Vehicle(
				vd.company(), 
				vd.model(), 
				vd.type(), 
				vd.damage());
		}
	}

	private void run()
	{
		// This is the starting point of the simulation.
		menu.displayMenu();
		while(true)
		{
			int action = menu.getMenuSelection();
			
			if(performAction(action))
		{
				output.outputSummary(
					completedVehicles, 
					iteration, 
					(int)Math.round(totalRevenue));

					break;
		}

		if(jobsCreated)
		{
			output.outputStatusTable();
		}
		else
		{
			System.out.println("No jobs currently recorded.");
		}

		menu.displayMenu();
	}
}
	private boolean performAction(int action)
	{	
		switch(action)
		{
			case Menu.MENU_OPTION_AVAILABLE_MECHANICS:
				displayAvailableMechanics();
				break;
				
			case Menu.MENU_OPTION_VEHICLES_UNDER_REPAIR:
				displayVehiclesUnderRepair();
				break;
				
			case Menu.MENU_OPTION_TOTAL_VEHICLES_LEFT_TO_REPAIR: 
				System.out.println("Vehicles left to repair: " + (vehicles.length - completedVehicles));
				break;
				
			case Menu.MENU_OPTION_TOTAL_REVENUE: 
				System.out.printf("Total Revenue: $%.2f%n", totalRevenue);
				break;
				
			case Menu.MENU_OPTION_JOB_LIST:
				// The status table is displayed by run()
				break;
				
			case Menu.MENU_OPTION_RUN_SIMULATION:
				runCompleteSimulation();
				break;
				
			case Menu.MENU_OPTION_FIXED_SIMULATION_RUN:
				runFixedSimulation();
				break;
				
			case Menu.MENU_OPTION_EXIT:
				System.out.println("Exiting Simulation.");
					return true;
				
			default:
				System.out.println("Invalid menu selection was made.\n");
		}
		
		return false;
	}

	private void displayAvailableMechanics()
	{
		boolean found = false;

		System.out.println("\nAvailable Mechanics:");

		for(int i = 0; i < mechanics.length; i++)
		{
			if(mechanics[i].isAvailable() && assignedVehicle[i] == -1)
			{
				System.out.println(mechanics[i].name() + " (ID: " + mechanics[i].id() + ")" + " Experience: " + mechanics[i].experience());

				found = true;
			}
		}

		if(!found)
		{
			System.out.println("No mechanics are currently available");
		}
	}

	private void displayVehiclesUnderRepair()
	{
		boolean found = false;

		System.out.println("\nVehicles Under Repair:");

		for(int i = 0; i < mechanics.length; i++)
		{
			if(assignedVehicle[i] != -1)
			{
				Vehicle v = vehicles[assignedVehicle[i]];

				System.out.println(v.toString() + " - Mechanic: " + mechanics[i].name() + " - Days remaining: " + mechanics[i].daysRemaining());

				found = true;
			}
		}

		if(!found)
		{
			System.out.println("No vehicles are currently under repair.");
		}

		System.out.println("Vehicles waiting: " + waitingList.size());
	}

	// Returns the damage repair cost based on the damage code
	private double damageCost(int damage)
	{
		switch(damage)
		{
			case 100: return 100; // Small Pothole
			case 101: return 300; // Medium Pothole
			case 102: return 600;	// Large Pothole

			case 103: return 150;	// Back Bumper
			case 104: return 100; //Front Bumper
			case 105: return 500; //Roof
			case 106: return 300; //Side Door
			case 107: return 400; // Windshield
			case 108: return 50;	// Side Mirror
			
			case 109: return 400; // Wind
			case 110: return 700; // Flood
			case 111: return 800; //Fire 

			default: return 0;
		}
	}

	// Returns the additional repair time multiplier for weather damage
	private double weatherMultiplier(int damage)
	{
		switch(damage)
		{
			case 109: return 0.2;
			case 110: return 0.4;
			case 111: return 0.5;
			
			default: return 0.0;
		}
	}

	// Calculate the total repair time
	private int calculateRepairDays(Mechanic mechanic, Vehicle vehicle)
		{
			int baseDays = Math.max(
				mechanic.minimumRepairDays(),
				vehicle.minimumRepairDays());

			double multiplier = weatherMultiplier(vehicle.damage());

			return (int)Math.ceil(baseDays * (1.0 + multiplier));
		}

	// Calculate the total repair cost
  private double calculateRepairCost(Mechanic mechanic, Vehicle vehicle, int days)
	{
		return damageCost(vehicle.damage()) + mechanic.repairCost(days) + vehicle.repairCost() + vehicle.partsCost();
	}

	private int findAvailableMechanic()
	{
		for(int i = 0; i < mechanics.length; i++)
		{
			if(mechanics[i].isAvailable() && assignedVehicle[i] == -1)
			{
				return i;
			}
		}

		return -1;
	}

	// Assigns waiting vehicles to the first available mechanics
	// private void assignWaitingVehicles()
	// {
	// 	for(int i = 0; i < mechanics.length; i++)
	// 	{
	// 		if(waitingList.isEmpty())
	// 		{
	// 			break;
	// 		}

	// 		if(mechanics[i].isAvailable())
	// 		{
	// 			int vehicleIndex = waitingList.remove(0);

	// 			assignJob(i, vehicleIndex);
	// 		}
	// 	}
	// }

	// Assigns one vehicle to one mechanic
	private void assignJob(int mechanicIndex, int vehicleIndex)
	{
		Mechanic mechanic = mechanics[mechanicIndex];
		Vehicle vehicle = vehicles[vehicleIndex];

		int days = calculateRepairDays(mechanic, vehicle);

		double cost = calculateRepairCost(mechanic, vehicle, days);

		mechanic.assignJob(days);

		assignedVehicle[mechanicIndex] = vehicleIndex;
		assignedDays[mechanicIndex] = days;
		assignedCost[mechanicIndex] = cost;

		vehicleStatus[vehicleIndex] = 1;

		jobsCreated = true;

		output.addJob(
			vehicle.toString(), 
			mechanic.name(), 
			days, 
			cost);

		output.outputAssignedToMechanic(
			vehicle.toString(),
			mechanic.name(),
			Integer.toString(days),
			String.format("$%.2f", cost));
	}

	// Advances the simulation by one day
	private void advanceOneDay()
	{
		iteration++;

		output.outputIterationNumber(iteration);

		// Take only one new vehicle from the list each day
		if(nextVehicleIndex < vehicles.length)
		{
			int vehicleIndex = nextVehicleIndex++;

			int mechanicIndex = findAvailableMechanic();

			if(mechanicIndex == -1)
			{
				waitingList.add(vehicleIndex);

				output.outputAddedToWaitingList(
					vehicles[vehicleIndex].type(),
					vehicles[vehicleIndex].company(), 
					vehicles[vehicleIndex].model());
			}
			else
			{
				assignJob(mechanicIndex, vehicleIndex);
			}
		}

		// Advance the repairs already assigned to mechanics
		for(int i = 0; i < mechanics.length; i++)
		{
			if(assignedVehicle[i] != -1)
			{
				mechanics[i].advanceDay();

				if(mechanics[i].daysRemaining() == 0)
				{
					int vehicleIndex = assignedVehicle[i];

					Vehicle vehicle = vehicles[vehicleIndex];

					output.outputRepairedByMechanic(
						mechanics[i].name(),
						vehicle.type(),
						vehicle.company(), 
						vehicle.model());

					vehicleStatus[vehicleIndex] = 2;

					completedVehicles++;

					totalRevenue += assignedCost[i];

					assignedVehicle[i] = -1;
					assignedDays[i] = 0;
					assignedCost[i] = 0;

					// Give newly available mechanics vehicles from waiting list
					if(!waitingList.isEmpty())
					{
						int waitingVehicle = waitingList.remove(0);

						assignJob(i, vehicleIndex);
					}
				}
			}
		}

		System.out.println();
	}

	// Option 6: Continue until every vehicle is repaired
	private void runCompleteSimulation()
	{
		System.out.println("\nRunning Complete Simulation\n");

		while(completedVehicles < vehicles.length)
		{
			advanceOneDay();
		}

		System.out.println("All vehicles have been repaired.");
	}

	// Option 7: Run for a specified number of iterations
	private void runFixedSimulation()
	{
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(System.in));

		try
		{
			System.out.println("Enter the number of iterations: ");

			int numberOfIterations = Integer.parseInt(
				reader.readLine());

			if(numberOfIterations <= 0)
			{
				System.out.println("Please enter a number greate than 0.");
				return;
			}

			System.out.println("\nRunning Fixed Simulation\n");

			int daysRun = 0;

			while(daysRun < numberOfIterations && completedVehicles < vehicles.length)
			{
				advanceOneDay();
				daysRun++;
			}

			if(completedVehicles == vehicles.length)
			{
				System.out.println("All vehicles have been repaired.");
			}
			else 
			{
				System.out.println("Fixed simulation finished after " + daysRun + " iterations.");
			}
		}

		catch(NumberFormatException e)
		{
			System.out.println("Please enter a valid integer.");
		}

		catch(IOException e)
		{
			System.out.println("Unable to read the number of iterations.");
		}
	}
}