RHMS
Hospital Record Management System (HRMS)
A desktop-based Hospital Record Management System developed in Java using Swing. This project was created to simplify the management of hospital operations by providing an organized way to handle patient records, doctor information, appointments, and medical history through an interactive graphical user interface.

Project Overview
Managing hospital records manually can be time-consuming and prone to errors. This application provides a digital solution that allows administrators, doctors, and patients to efficiently manage healthcare-related information in one place.

The system follows object-oriented programming principles and demonstrates concepts such as encapsulation, inheritance, polymorphism, exception handling, file handling, serialization, and GUI development.

Features
👨‍⚕️ Role-based login system (Admin, Doctor & Patient)
🩺 Manage patient records
📅 Book, update, and cancel appointments
⚠️ Detect appointment scheduling conflicts
📋 Store and view medical history
💾 Save and load data using Java Serialization
🖥️ Interactive Java Swing GUI
🚫 Custom exception handling for invalid operations
📂 Persistent local data storage
Technologies Used
Java
Java Swing
Object-Oriented Programming (OOP)
Java Collections Framework
File Handling
Serialization
Project Structure
HospitalSystem/
│
├── src/
│   ├── Main.java              # Application entry point
│   ├── model/                 # Data model classes
│   ├── gui/                   # Swing GUI screens
│   ├── service/               # Business logic
│   ├── storage/               # Data persistence
│   └── exception/             # Custom exception classes
│
├── data/                      # Serialized data files
├── bin/                       # Compiled classes
└── README.md
Getting Started
Prerequisites
Java JDK 8 or later
Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans, or VS Code)
Run the Project
Compile the source files:

javac -d bin src/**/*.java
Run the application:

java -cp bin Main
👥 User Roles
Administrator
Manage doctors and patients
Monitor hospital records
Maintain system data
Doctor
View appointments
Access patient medical records
Update patient information
Patient
Book appointments
View appointment details
Access personal medical history
💡 Object-Oriented Concepts Implemented
Encapsulation
Inheritance
Polymorphism
Abstraction
Exception Handling
File Handling
Serialization
Java Collections
🚀 Future Improvements
Some features that can be added in future versions include:

Database integration (MySQL)
User authentication with passwords
Email or SMS appointment reminders
Prescription generation
Billing and payment module
Medical report uploads
Search and filtering options
📸 Screenshots
You can add screenshots of the application here.

screenshots/
├── Login.png
├── Dashboard.png
├── PatientManagement.png
├── AppointmentBooking.png
└── MedicalRecords.png
📄 License
This project was developed for educational purposes as part of an Object-Oriented Programming course.

👤 Author
Aymen Faisal

If you found this project helpful or interesting, feel free to ⭐ the repository.
