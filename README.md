# Douglas - Inventory Module

## 📦 Inventory Management System
Implementation of a complete CRUD system for inventory management as part of the Exam I project. This module allows users to register, view, update, and delete product entries with comprehensive data validation.

## Features Implemented

### Person Information Management
- Person ID (required)
- Person Name (required)
- Email address with format validation

### Product Information Management
- Product Code with duplicate detection
- Product Name
- Quantity with numeric validation

## Screenshots

### Main Menu
![Main Menu](https://github.com/user-attachments/assets/86fa95e8-25a1-49bc-8a31-5b7abf91f7ce)

### Inventory List View
![Inventory List View](https://github.com/user-attachments/assets/cfb4b450-79c6-445e-9844-ff0e3c2426b6)

### Entry Form
![Entry Form](https://github.com/user-attachments/assets/8e75c6e1-4623-4e2c-866b-6376d21919e8)

### Entry Details
![Entry Details](https://github.com/user-attachments/assets/fb61d731-7b1f-47e1-8e4d-7d9128e30390)

## CRUD Operations
- Create: Users can add new inventory entries through the FloatingActionButton
- Read: All inventory entries are displayed in a scrollable RecyclerView list
- Update: Users can tap any list item to open the edit form with pre-populated data
- Delete: Delete option available in the menu with confirmation dialog
  
## Modified Files

### New Files Created
- InventoryController.kt
- InventoryEntry.kt
- InventoryAdapter.kt
- InventoryListActivity.kt
- InventoryDetailActivity.kt
- activity_inv_list.xml
- activity_inv_detail.xml
- item_inv_list.xml
- strings-inventory.xml
