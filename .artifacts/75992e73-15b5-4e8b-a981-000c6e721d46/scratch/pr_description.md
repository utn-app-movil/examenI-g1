# Pull Request Description

## Title
Jordan-ProductionOrders

## Description of Implementation
In this Pull Request, I implemented the complete CRUD feature for the **Production Orders Control** scenario (`prod_`) in accordance with the exam specifications and architectural constraints.

### Key Components Added/Modified:
1. **Entity Class (`ProdOrder.kt`)**:
   - Created the `ProdOrder` entity extending the abstract class `Identifier` (implementing `id` and `fullName`).
   - Stores production order attributes: order number, selected products list, quantity, target company, start date, and delivery date.

2. **ViewModel Class (`ProdViewModel.kt`)**:
   - Implemented `ProdViewModel` utilizing the project's `MemoryDataManager` (`MemoryManager`) repository to manage in-memory CRUD operations (`getAll`, `getById`, `save`, `update`, `delete`).

3. **CRUD Activity (`ProdCrudActivity.kt`) & List Activity (`ProdListActivity.kt`)**:
   - Developed the user interface logic for creating, reading, updating, and deleting production orders.
   - Integrated multi-selection options (CheckBoxes) for products, a Spinner for target companies, and date/time formatting utilities.

## Validations & Business Logic
- **Quantity Validation**: Ensured that the quantity entered is strictly greater than zero (`quantity > 0`).
- **Date Validation**: Verified that the delivery date/time is strictly after the start date/time (`deliveryDateTime.isAfter(startDate)`).
- **Duplicate Prevention**: Checked for existing records by ID/order number prior to insertion to prevent duplicate data (`TextDuplicateData`).
- **Data Integrity**: Ensured proper object-oriented programming principles, inheritance, and clean data flow between activities and the ViewModel.

## Architectural Constraints & Best Practices
- **Strict Adherence to Project Rules**: 
  - Did NOT modify `AndroidManifest.xml`, `MainActivity`, `strings.xml`, menus, or `util.kt`.
  - Did NOT modify protected base classes (`Person`, `Identifier`, `IRepository`, `MemoryManager`).
- **Naming Conventions & Language**: All newly created code, classes, methods, and variables are written in English. Reused existing string resources (`strings.xml`) without adding new strings.

## Evidence & Debugging
- **Build Status**: Successfully compiled and installed on the emulator/device (`app:assembleDebug` completed with status `success=true`).
- **Screenshots / Debugging Evidence**:
  *(Attach screenshots here showing:*
   1. *Main screen with Production Orders button)*
   2. *List view (`ProdListActivity`)*
   3. *CRUD form (`ProdCrudActivity` with validations for quantity and dates)*
   4. *Successful save / delete toasts and debug logs)*
