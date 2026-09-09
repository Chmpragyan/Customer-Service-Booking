# API Contract - Customer Service Booking (v1)

This document defines the REST API contract for the Customer Service Booking application.

## Base URL
`https://api.example.com/api/v1`

---

## 1. List/Search Services
Retrieve a list of available services, optionally filtered by a search query.

- **Method**: `GET`
- **Endpoint**: `/services`
- **Query Parameters**:
  - `query` (optional): String to filter services by name or category.
- **Success Response**: `200 OK`
  ```json
  [
    {
      "id": "1",
      "name": "AC Repair",
      "category": "Maintenance",
      "provider": "CoolAir Solutions",
      "price": 500.0,
      "currency": "NPR",
      "durationMinutes": 60,
      "rating": 4.5,
      "description": "Professional AC maintenance",
      "availableDates": ["2026-05-20"]
    }
  ]
  ```

## 2. Get Service Details
Retrieve full details for a specific service.

- **Method**: `GET`
- **Endpoint**: `/services/{service_id}`
- **Success Response**: `200 OK`
  ```json
  {
    "id": "1",
    "name": "AC Repair",
    "category": "Maintenance",
    "provider": "CoolAir Solutions",
    "price": 500.0,
    "currency": "NPR",
    "durationMinutes": 60,
    "rating": 4.5,
    "description": "Professional AC maintenance",
    "availableDates": ["2026-05-20"]
  }
  ```
- **Error Response**: `404 Not Found` (Service not found)

## 3. Get Availability
Retrieve available time slots for a specific service on a given date.

- **Method**: `GET`
- **Endpoint**: `/services/{service_id}/availability`
- **Query Parameters**:
  - `date` (required): Date in `yyyy-MM-dd` format.
- **Success Response**: `200 OK`
  ```json
  [
    {
      "id": "s1",
      "date": "2024-05-20",
      "startTime": "09:00",
      "endTime": "10:00",
      "isAvailable": true
    }
  ]
  ```

## 4. Create Booking
Submit a new booking request.

- **Method**: `POST`
- **Endpoint**: `/bookings`
- **Request Body**:
  ```json
  {
    "serviceId": "1",
    "slotId": "s1",
    "date": "2024-05-20",
    "startTime": "09:00",
    "customerName": "John Doe",
    "customerContact": "9876543210",
    "customerAddress": "Kathmandu, Nepal"
  }
  ```
- **Success Response**: `201 Created`
  ```json
  {
    "bookingId": "uuid-string",
    "bookingNumber": "BK123456",
    "serviceName": "AC Repair",
    "status": "PENDING",
    "createdAt": "2024-05-19 10:00:00"
  }
  ```
- **Error Responses**:
  - `400 Bad Request`: Validation error (missing required fields).
  - `409 Conflict`: Slot conflict (already booked).
  - `500 Internal Server Error`: Unexpected failure.

## 5. List Customer Bookings
Retrieve all bookings made by the current user.

- **Method**: `GET`
- **Endpoint**: `/bookings`
- **Success Response**: `200 OK`
  ```json
  [
    {
      "bookingId": "uuid-string",
      "bookingNumber": "BK123456",
      "serviceName": "AC Repair",
      "scheduledDate": "2024-05-20",
      "status": "PENDING"
    }
  ]
  ```

---

## Standard Error Structure

```json
{
  "error": {
    "code": 409,
    "message": "This time slot has just been booked by someone else."
  }
}
```

## Behavior Requirements
- **Loading**: UI should show a progress indicator while waiting for response.
- **Empty State**: UI should display a friendly message if a list returns 0 items.
- **Error Handling**: 
  - Network errors should show a "Retry" option.
  - Conflict errors (409) should prompt the user to select a different slot.
  - Validation errors should highlight the specific field.
