# smiCS - ICS Registration Planner

**smiCS** is a course registration planning application designed to help students at the University of the Philippines Los Baños (UPLB) organize their class schedules efficiently.

**Developed by:** Group JELly.ace  
**Course:** CMSC 22, University of the Philippines Los Baños  
**Academic Year:** 2025-2026, First Semester

---

## Features

- **User Account Management**: Create and manage personal accounts with degree program specification
- **Interactive Course Planner**: Add, edit, and remove courses with real-time schedule visualization
- **Visual Calendar**: View your weekly schedule with automatic conflict detection
- **Course Catalog**: Browse and search all ICS courses by program, code, or title
- **Smart Validation**: Prevents schedule conflicts and ensures courses match your degree program
- **Persistent Storage**: Save and load your planned schedules

---

## Getting Started

### First Time Users

1. **Launch the Application**  
   Upon launch, you'll see the login screen.

2. **Create an Account**  
   - Click **"Create an Account"**
   - Fill in your information:
     - Email address
     - First name, middle name (optional), and last name
     - Select your degree program (BSCS, MSCS, PhD CS, or MIT)
     - Create a password
   - Click **"Create"** to register

3. **Log In**  
   - Enter your email address and password
   - Click **"Log In"** to access the planner

---

## 📖 Using smiCS

### Dashboard Tab

The Dashboard is your home page, containing:
- Welcome message and user guide
- Information about how to navigate the portal
- Details about the creators and development team
- References and credits

### Planner Tab

The Planner is where you build your schedule:

- **Visual Calendar**: View your weekly schedule from 7:00 AM to 7:00 PM
- **Course Basket**: See all courses you've added to your plan
- **Add Classes Section**: Browse available course offerings and add them to your basket

**Adding Courses:**
1. Browse available courses in the "Add Classes" section
2. Use search fields to filter by course code or title
3. Click **"Add"** to add a course to your basket
4. Courses with labs must be added together (lecture + lab)

**Managing Your Schedule:**
- Click **"Delete"** in your basket to remove courses
- Click **"Save Schedule"** to save your plan
- Click **"Clear All"** to remove all courses and start over

> **Important:** The system will prevent you from adding courses that:
> - Conflict with your existing schedule
> - Are not available in your degree program
> - Are missing required lab/lecture pairs

### Courses Tab

Browse the complete ICS course catalog:
- **Filter by Program**: View courses for specific degree programs
- **Search by Code**: Find courses using their course code
- **Search by Name**: Look up courses by title
- **View Details**: See course descriptions, units, and program requirements

### Profile

Access your profile by clicking your name in the top-right corner:
- View your account information
- **Sign Out**: Return to the login page
- **Exit**: Close the application

---

## Saving Your Work

- Your schedule is automatically saved when you click **"Save Schedule"**
- Saved schedules are loaded automatically when you return to the Planner
- Each user's schedule is stored separately and securely

---

## Supported Degree Programs

- **BSCS** - Bachelor of Science in Computer Science
- **MSCS** - Master of Science in Computer Science
- **PhD CS** - Doctor of Philosophy in Computer Science
- **MIT** - Master of Information Technology

---

## Important Notes

- Courses are filtered based on your degree program
- The planner prevents scheduling conflicts automatically
- Courses with labs require both lecture and lab sections to be added together
- Save your schedule regularly to avoid losing your work

---

## Technical Information

**Built with:** Java, JavaFX  
**Data Storage:** CSV files  
**Fonts:** Dotemp 8bit, Crimson Pro

---

## License & Credits
Smiski Official Website: https://smiski.com/
Created by Group JELly.ace for CMSC 22 at the University of the Philippines Los Baños.

For questions or issues, please contact the development team through your course instructor.
