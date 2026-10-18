``` mermaid

classDiagram
	class Simulation{
	}

	class Shop{
		simulate()
	}

	class Mechanic{
		name()
		id()
		experience()
	}

	class Vehicle{
		company()
		type()
		model()
		damage()
	}

	class Display{
		addJob()
		outputStatusTable()
		outputSummary()
		outputIterationNumber()
		outputAddedToWaitingList()
		outputAssignedToMechanic()
		outputRepairedByMechanic()
	}

	class Menu{
		displayMenu()
		getMenuSelection()
	}

	class Job{
		calculateCost()
		calculateTime()
		generateRecord()
		status()
	}

	class WaitingList{
		addVehicle()
		removeVehicle()
		nextVehicle()
		count()
	}

	class Damage{
		repairCost()
		repairTime()
	}

	class PotholeDamage{
		repairCost()
		repairTime()
	}

	class CollisionDamage{
		repairCost()
		repairTime()
	}

	class WeatherDamage{
		repairCost()
		repairTime()
	}

	Simulation "1" --> "1" Shop : simulates
	Shop "1" --> "*" Mechanic : employs
	Shop "0" --> "*" Vehicle : repairs
	Shop "1" --> "1" Display : uses
	Shop "1" --> "1" Menu : displays
	Job "1" --> "1" Vehicle : repairs
	Job "1" --> "1" Mechanic : assigned
	WaitingList "0" --> "*" Vehicle : contains
```
