# Restaurant

Igor Melnikov && Alena Vasileva, BSE218

# Раз уж на то пошло, то поставьте звёздочку на курсач плиз https://github.com/objectionary/speco 

## How it works

The program has one instance of agents `ManagerAgent` and `StoreAgent` for each.

`VisitorAgent`, `ChefAgent` and `EquipmentAgent` are created by the `ManagerAgent` based on the input JSONs.

`ManagerAgent` creates an `OrderAgent` for each order.

`OrderAgent` creates an `ProcessAgent` for each dish in order.

`ProcessAgent` creates an `OperationAgent` for each operation from `dish_card`.

### Concurrency

Quote from the documentation: *In JADE there is a single Java thread per agent.*

Thus, orders, processes and operations are already being executed concurrently.
For example, you can run the program several times and look at the order of logs in the console.

### Order awaiting

TBA

### Input and Output

TBA

### Patterns

TBA

## Usage

To run project (with existing `restaurant.jar`):
```bash
make run
```

After the launch, the GUI with the created agents will be available,
but it is better to look at the logs in the console.

To rebuild project:
```bash
make build
```

To check format (btw this is checked in CI):
```bash
make lint
```
