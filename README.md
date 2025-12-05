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

## Project Structure
smiCS/
├── src/
│   ├── application/
│   │   ├── Main.java                    # Application entry point
│   │   ├── Login.java                   # Login screen and authentication
│   │   ├── CreateAccount.java           # Account creation interface
│   │   ├── Smi_Dashboard.java           # Main dashboard controller
│   │   ├── AddCourse.java               # Course adding logic & validation
│   │   ├── Fonts.java                   # Font loader utility
│   │   ├── Notifier.java                # Alert/notification popups
│   │   └── application.css              # Application styling
│   │
│   ├── bases/
│   │   ├── Account.java                 # User account model
│   │   ├── Course.java                  # Course information model
│   │   ├── Offering.java                # Course offering model
│   │   └── YearSet.java                 # Academic year offerings container
│   │
│   ├── fileHandlers/
│   │   ├── AccountLoader.java           # Load accounts from CSV
│   │   ├── BasketHandler.java           # Save/load user schedules
│   │   ├── CourseLoader.java            # Load course catalog
│   │   └── OfferingLoader.java          # Load course offerings
│   │
│   ├── planning/
│   │   ├── Planner_Sched.java           # Schedule grid visualization
│   │   ├── Planner_BasketView.java      # Course basket display
│   │   └── Planner_SearchAdd.java       # Course search and add interface
│   │
│   └── screens/
│       ├── InfoPage.java                # Dashboard/info screen
│       ├── CoursePage.java              # Course catalog browser
│       ├── PlannerPage.java             # Planner main screen
│       ├── ProfilePage.java             # User profile screen
│       └── TutorialPage.java            # Video tutorial screen
│
├── data/
│   ├── course_offerings.csv             # Available course offerings
│   ├── ics_cmsc_courses.csv             # BSCS course catalog
│   ├── ics_mscs_courses.csv             # MSCS course catalog
│   ├── ics_phd_courses.csv              # PhD CS course catalog
│   └── ics_mit_courses.csv              # MIT course catalog
│
├── accounts_data/
│   ├── accounts.csv                     # User accounts database
│   └── [username]_basket.csv            # Individual user schedules
│
├── assets/
│   ├── icon.png                         # App icon
│   ├── brand.png                        # Brand logo
│   ├── menubutton.png                   # Menu button icon
│   ├── LandingPage.gif                  # Landing page animation
│   ├── Hang.gif / Hng2.gif              # Hanging character animations
│   ├── backend.gif / Backendreveal.m4v  # Backend team animations
│   ├── frontend.gif / Frontendreveal.m4v # Frontend team animations
│   ├── uiux.gif / Uiuxreveal.m4v        # UI/UX team animations
│   ├── PlannerTutorial.gif              # Planner tutorial
│   ├── ViewTutorial.gif                 # View tutorial
│   ├── tutorial.m4v                     # Full tutorial video
│   ├── lay.png / yay.png                # Notification characters
│   └── Unhovered_sign.png / Hovered_sign.png  # Button states
│
└── fonts/
    ├── dotemp-8bit.otf                  # Retro display font
    └── CrimsonPro-VariableFont_wght.ttf # Body text font

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

---

## Problems
For issues;
feel free to contact any of te following:
jbcalleja@up.edu.ph
mpgevana@up.edu.ph

For questions or issues, please contact the development team through your course instructor.
