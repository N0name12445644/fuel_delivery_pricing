# Fuel Delivery Pricing

A Java application that reads fuel delivery records from JSON, validates them, calculates delivery prices and taxes, and exports the results to CSV files.

## Implemented Features

**Task 1 — Standard Pricing**
- Validates shipment records.
- Calculates fuel price with volume discounts.
- Calculates logistics costs, excise duty, and VAT.
- Exports results to `output/task1.csv`.

**Task 2 — Custom Pricing**
- Asks the user for a custom VAT percentage for PRIVATE clients.
- Asks the user for a custom gasoline excise rate.
- Rejects invalid or negative inputs and asks again.
- Recalculates delivery prices using the custom rates.
- Exports results to `output/task2.csv`.

**Validation**
- Detects missing fields, unknown fuel types, unknown client types, invalid dates, invalid numeric values, and duplicate IDs.
- Excludes invalid shipments from pricing calculations.
- Exports validation errors to `output/errors.csv`, even if there are no errors.

Bonus 1 and Bonus 2 are not implemented.

## Requirements

- Java JDK 27
- Internet access on the first Maven Wrapper run to download required dependencies

## How to Run

Place `shipments.json` in the project root directory, alongside `pom.xml`.

Open a terminal in the project root.

**Windows (PowerShell):**

```powershell
.\mvnw.cmd compile exec:java
```

**Linux / macOS:**

```bash
./mvnw compile exec:java
```

The program asks for two values in this order:

1. Custom VAT rate for PRIVATE clients, as a percentage (for example, `25`).
2. Custom gasoline excise rate per litre (for example, `0.52`).

The program then creates the following files in the `output` directory:

- `task1.csv`
- `task2.csv`
- `errors.csv`

## Date Handling

Both `YYYY-MM-DD` and `DD/MM/YYYY` formats are supported.

Dates are validated using Java `LocalDate` and strict date parsing to reject invalid calendar dates.

PRIVATE clients pay 21% VAT for deliveries before January 1, 2024, and 23% from that date onward in Task 1. EXEMPT clients do not pay VAT.

## Calculations

All financial calculations use Java `BigDecimal` with `HALF_UP` rounding to two decimal places at each required calculation step.

Discounts are 5% for volumes greater than 5,000 litres and 10% for volumes greater than 20,000 litres.

Logistics costs are calculated at 1.80 per kilometre, with a 150.00 surcharge for volumes below 1,000 litres.

## AI Usage

ChatGPT was used as a learning and development assistant, discuss program architecture, help implement validation and pricing logic, and support debugging and testing.
