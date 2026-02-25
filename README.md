## ParkingLot

Simple in–memory parking lot ticketing system implemented in Java using a layered architecture (controller → service → repositories/strategies → models).

### Tech stack

- **Language**: Java 21  
- **Build tool**: Maven (`pom.xml`)  
- **Libraries**: Lombok (for getters/setters/builders)

### Project structure

- **`src/main/java/org/example/Main.java`**: Application entry point that wires repositories, strategy, service, and controller, then issues a sample ticket.
- **`controller`**
  - **`TicketController`**: Accepts an `IssueTicketRequest`, calls `TicketService`, and builds an `IssueTicketResponse`.
- **`services`**
  - **`TicketService`**: Core domain logic for issuing a ticket:
    - Validates and fetches the entry `Gate`.
    - Finds or creates a `Vehicle`.
    - Asks a `SpotAssignmentStrategy` for an available `ParkingSpot`.
    - Marks the spot occupied and saves the `Ticket`.
- **`dtos`**
  - **`IssueTicketRequest`**: `vehicleNumber`, `gateId`, `vehicleType`.
  - **`IssueTicketResponse`**: Details of the issued ticket (ticket id, floor/spot number, gate number, timestamps, vehicle number).
- **`model`**
  - Core domain entities: `ParkingLot`, `ParkingFloor`, `ParkingSpot`, `Ticket`, `Gate`, `Operator`, `Vehicle`, enums like `VehicleType`, `ParkingSpotStatus`, `GateType`, `PaymentType`, etc.
- **`repositories`**
  - In–memory repositories backed by `Map`/`HashMap`:
    - `GateRepository`, `ParkingLotRepository`, `VehicleRepository`, `TicketRepository`.
- **`strategies`**
  - **`SpotAssignmentStrategy`**: Interface for parking spot selection strategies.
  - **`RandomSpotAssignmentStrategy`**: Iterates over floors/spots of a `ParkingLot` and returns the first available spot that matches the requested `VehicleType`.
- **`exceptions`**
  - **`GateNotFoundException`**: Thrown when a gate id is invalid.

### How the flow works

1. **Client builds a request** (`IssueTicketRequest`) with:
   - Vehicle number
   - Gate id
   - Vehicle type
2. **`TicketController.issueTicket`** receives the request and delegates to `TicketService.issueTicket`.
3. **`TicketService`**:
   - Uses `GateRepository` to fetch the `Gate` (or throws `GateNotFoundException`).
   - Uses `VehicleRepository` to find or create a `Vehicle`.
   - Calls `SpotAssignmentStrategy.getSpot` to select an available `ParkingSpot`.
   - Marks the spot as `OCCUPIED`, associates the vehicle, and creates a `Ticket` with:
     - Unique number
     - Entry time
     - Gate and operator
     - Assigned parking spot
   - Saves the ticket in `TicketRepository`.
4. **`TicketController`** converts the `Ticket` into an `IssueTicketResponse` that exposes only the required fields.

### Prerequisites

- Java 21 (or a compatible JDK)
- Maven 3.x
- Lombok support enabled in your IDE (for IntelliJ IDEA, install the Lombok plugin and enable annotation processing).

### Build and run

#### Using IntelliJ IDEA

- **Open the project** as a Maven project.
- Ensure annotation processing is enabled so Lombok compiles correctly.
- Locate `Main` (`org.example.Main`) and **Run** it from the IDE.
- The `main` method currently builds sample repositories, strategy, service, and controller, then calls `issueTicket` with a hard–coded request. You can customize this to wire real data or integrate with a UI/API.

#### Using Maven from the command line

From the project root (`ParkingLot`):

```bash
mvn clean compile
```

This compiles the project and resolves dependencies. To package it:

```bash
mvn clean package
```

You can then run the `Main` class using your IDE or by configuring an executable JAR/`exec-maven-plugin` as needed.

### Notes and limitations

- **In–memory data only**: All repositories are simple in–memory maps; data is lost when the application stops.
- **Bootstrapping data**: By default, the repositories are empty. For the sample flow to work end–to–end (especially spot assignment), you will typically need to pre–populate:
  - `GateRepository` with at least one `Gate` and `Operator`.
  - `ParkingLotRepository` with a `ParkingLot` that has `ParkingFloor` and `ParkingSpot` instances, including spot type (`VehicleType`) and `ParkingSpotStatus.AVAILABLE`.
- **Error handling**: Invalid gate IDs result in `GateNotFoundException` inside `TicketService`; `TicketController` currently wraps this with a generic `RuntimeException("INVALID GATE")`. You can evolve this into a richer error model (HTTP responses, error codes, etc.) if you expose this via REST.

### Possible extensions

- Add persistence with a real database (JPA/Hibernate or JDBC).
- Expose REST APIs using Spring Boot for issuing tickets, exiting vehicles, and calculating invoices.
- Implement additional spot assignment strategies (e.g., nearest-to-gate, price-based, floor-priority).
- Add billing/invoice generation, payment handling, and reporting.

