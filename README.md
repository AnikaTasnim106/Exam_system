# 🎓 EduTrack - Online Exam Management System

A comprehensive JavaFX-based desktop application for managing online examinations with separate interfaces for teachers and students.

![JavaFX](https://img.shields.io/badge/JavaFX-21-orange)
![Java](https://img.shields.io/badge/Java-17+-blue)
![License](https://img.shields.io/badge/License-MIT-green)

---

## 📋 Table of Contents

- [Features](#-features)
- [System Architecture](#-system-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Running the Application](#-running-the-application)
- [Usage Guide](#-usage-guide)
- [Project Structure](#-project-structure)
- [Technologies Used](#-technologies-used)
- [Default Credentials](#-default-credentials)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

---

## ✨ Features

### 👨‍🏫 For Teachers

- **Question Bank Management**
  - Create, view, edit, and delete questions
  - Filter questions by subject and difficulty level
  - Support for multiple-choice questions (A, B, C, D)
  - Add explanations for correct answers
  - Color-coded difficulty levels (Easy, Medium, Hard)

- **Exam Creation**
  - Create customized exams with selected questions
  - Set exam duration (5-180 minutes)
  - Filter and select questions from the question bank
  - Add exam descriptions and metadata

- **Student Management**
  - View all registered students
  - Track student performance metrics
  - View individual student exam history
  - Search and filter students
  - Display statistics: total students, active students, average performance

- **Results Management**
  - View results by exam or by student
  - Detailed exam analytics
  - Export-ready result displays
  - Grade distribution visualization

### 👨‍🎓 For Students

- **Exam Taking**
  - Browse available exams by subject
  - Real-time countdown timer with color indicators
  - Question navigator showing answered/unanswered status
  - Previous/Next navigation between questions
  - Auto-submit when time expires
  - Immediate result display after submission

- **Results Viewing**
  - View all exam results with scores and grades
  - Detailed answer review (correct/incorrect)
  - Performance statistics and trends
  - Color-coded grade display

- **Practice Mode**
  - Review wrong answers from previous exams
  - Practice mode for self-improvement
  - Track progress over time

### 🔒 Authentication & Security

- User registration with role selection (Student/Teacher)
- Secure login system
- Session management
- Role-based access control

---

## 🏗️ System Architecture

EduTrack follows the **MVC (Model-View-Controller)** pattern with an additional **Service Layer**:

```
┌─────────────────────────────────────────────────────┐
│                    USER INTERFACE                    │
│              (FXML Files - JavaFX Views)             │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                   CONTROLLERS                        │
│    (Handle user actions, update UI, call services)  │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                    SERVICES                          │
│       (Business logic, data operations, I/O)         │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                     MODELS                           │
│         (Data structures - User, Exam, etc.)         │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│              FILE STORAGE (.dat files)               │
│      (Persistent storage using serialization)        │
└─────────────────────────────────────────────────────┘
```

### Networking Architecture (Optional)

EduTrack supports **client-server architecture** with multi-threading:

```
┌──────────────┐         ┌─────────────────────┐
│  Client 1    │────────▶│                     │
├──────────────┤         │      SERVER         │
│  Client 2    │────────▶│    (Port 5000)      │
├──────────────┤         │   Multi-threaded    │
│  Client 3    │────────▶│                     │
└──────────────┘         └─────────────────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │   Data Files    │
                         └─────────────────┘
```

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required

- **Java Development Kit (JDK) 17 or higher**
  - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
  - Verify installation: `java -version`

- **JavaFX SDK 21 or higher**
  - Download: [JavaFX Downloads](https://gluonhq.com/products/javafx/)
  - Or use Maven/Gradle to manage dependencies

### Recommended

- **IDE**: IntelliJ IDEA, Eclipse, or NetBeans
- **Build Tool**: Maven or Gradle (optional but recommended)
- **Scene Builder** (for FXML editing): [Download Scene Builder](https://gluonhq.com/products/scene-builder/)

---

## 🚀 Installation

### Method 1: Using Maven (Recommended)

#### Step 1: Clone the Repository

```bash
git clone https://github.com/AnikaTasnim106/Exam_system.git
cd Exam_system
```

#### Step 2: Verify Maven Installation

```bash
mvn -version
```

If Maven is not installed, download it from [Maven Downloads](https://maven.apache.org/download.cgi).

#### Step 3: Install Dependencies

```bash
mvn clean install
```

This will:
- Download all required dependencies (JavaFX, etc.)
- Compile the source code
- Run any tests
- Package the application

#### Step 4: Verify Project Structure

Ensure your `pom.xml` includes JavaFX dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>0.0.8</version>
            <configuration>
                <mainClass>com.buet.edutrack.Main</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

### Method 2: Manual Setup (Without Maven)

#### Step 1: Clone the Repository

```bash
git clone https://github.com/AnikaTasnim106/Exam_system.git
cd Exam_system
```

#### Step 2: Download JavaFX SDK

1. Download JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/)
2. Extract to a location (e.g., `C:\javafx-sdk-21` on Windows or `/usr/local/javafx-sdk-21` on Linux/Mac)

#### Step 3: Configure IDE

**For IntelliJ IDEA:**

1. Open the project in IntelliJ
2. Go to **File → Project Structure → Libraries**
3. Click **+** → **Java** → Select the `lib` folder from JavaFX SDK
4. Go to **Run → Edit Configurations**
5. Add VM options:
   ```
   --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
   ```
6. Set Main class: `com.buet.edutrack.Main`

**For Eclipse:**

1. Right-click project → **Build Path → Configure Build Path**
2. **Libraries** → **Add External JARs** → Add all JARs from JavaFX SDK `lib` folder
3. **Run Configurations** → **Arguments** → **VM arguments**:
   ```
   --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
   ```

#### Step 4: Compile the Project

```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d bin src/main/java/com/buet/edutrack/*.java src/main/java/com/buet/edutrack/**/*.java
```

---

### Method 3: Using Gradle

#### Step 1: Clone the Repository

```bash
git clone https://github.com/AnikaTasnim106/Exam_system.git
cd Exam_system
```

#### Step 2: Verify/Create `build.gradle`

```gradle
plugins {
    id 'application'
    id 'org.openjfx.javafxplugin' version '0.0.13'
}

group 'com.buet.edutrack'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules = ['javafx.controls', 'javafx.fxml']
}

application {
    mainClass = 'com.buet.edutrack.Main'
}

dependencies {
    // Add any additional dependencies here
}
```

#### Step 3: Build and Run

```bash
./gradlew build
./gradlew run
```

---

## ▶️ Running the Application

### Using Maven

```bash
mvn javafx:run
```

### Using Gradle

```bash
./gradlew run
```

### Using IDE

1. Locate `Main.java` in `src/main/java/com/buet/edutrack/`
2. Right-click → **Run 'Main.main()'**

### Using Command Line (Manual)

```bash
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp bin com.buet.edutrack.Main
```

### Running the Server (For Networking Mode)

If you want to use the client-server architecture:

1. **Start the Server:**
   ```bash
   java -cp bin com.buet.edutrack.Server
   ```
   
2. **Start the Client(s):**
   ```bash
   java --module-path /path/to/javafx-sdk/lib \
        --add-modules javafx.controls,javafx.fxml \
        -cp bin com.buet.edutrack.Main
   ```

---

## 📖 Usage Guide

### First Time Setup

When you run the application for the first time:

1. **Default accounts are automatically created:**
   - Student account: `student` / `123`
   - Teacher account: `teacher` / `123`

2. **Login Screen appears:**
   - Select your role (Student/Teacher)
   - Enter credentials
   - Click "Login"

3. **Or create a new account:**
   - Click "Sign Up"
   - Fill in the registration form
   - Select your role
   - Click "Register"

### Teacher Workflow

#### 1. Create Questions

```
Dashboard → Question Bank → Add New Question
```

- Fill in question text
- Add four options (A, B, C, D)
- Select correct answer
- Choose difficulty level
- Add explanation
- Click "Save"

#### 2. Create Exam

```
Dashboard → Create Exam
```

- Enter exam title and subject
- Set duration (5-180 minutes)
- Add description (optional)
- Filter and select questions
- Check the questions you want to include
- Click "Create Exam"

#### 3. View Student Performance

```
Dashboard → Manage Students
```

- View all registered students
- See performance metrics
- Search for specific students
- Click "View Details" for individual history

#### 4. View Exam Results

```
Dashboard → View Results
```

- Select an exam
- View all students who took it
- See average scores
- View detailed results

### Student Workflow

#### 1. Take an Exam

```
Dashboard → Attend Exam → Select Exam → Start
```

- Review exam details
- Confirm start
- Answer questions using radio buttons
- Navigate with Previous/Next buttons
- Use question navigator to jump to specific questions
- Monitor time remaining
- Click "Finish" when done

#### 2. View Results

```
Dashboard → View Results
```

- See all your exam attempts
- View scores and grades
- Click "View Details" to see:
  - Which questions you got right/wrong
  - Correct answers
  - Time taken

#### 3. Practice Mode

```
Dashboard → Practice Mode
```

- Review exams you've taken
- Practice wrong answers
- Improve your understanding

---

## 📁 Project Structure

```
EduTrack/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/buet/edutrack/
│   │   │       ├── Main.java                    # Entry point
│   │   │       ├── Server.java                  # Network server (optional)
│   │   │       ├── NetworkClient.java           # Network client (optional)
│   │   │       │
│   │   │       ├── controllers/                 # UI Controllers
│   │   │       │   ├── LoginController.java
│   │   │       │   ├── SignupController.java
│   │   │       │   ├── StudentDashboardController.java
│   │   │       │   ├── TeacherDashboardController.java
│   │   │       │   ├── AddQuestionController.java
│   │   │       │   ├── QuestionBankController.java
│   │   │       │   ├── CreateExamController.java
│   │   │       │   ├── ManageExamsController.java
│   │   │       │   ├── ManageStudentsController.java
│   │   │       │   ├── ExamListController.java
│   │   │       │   ├── TakeExamController.java
│   │   │       │   ├── ViewResultsController.java
│   │   │       │   ├── PracticeController.java
│   │   │       │   └── PracticeSessionController.java
│   │   │       │
│   │   │       ├── models/                      # Data Models
│   │   │       │   ├── User.java
│   │   │       │   ├── Question.java
│   │   │       │   ├── Exam.java
│   │   │       │   └── Result.java
│   │   │       │
│   │   │       ├── services/                    # Business Logic
│   │   │       │   ├── UserService.java
│   │   │       │   ├── QuestionService.java
│   │   │       │   ├── ExamService.java
│   │   │       │   ├── ResultService.java
│   │   │       │   └── ForumService.java
│   │   │       │
│   │   │       └── utils/                       # Utilities
│   │   │           ├── SceneManager.java
│   │   │           └── SessionManager.java
│   │   │
│   │   └── resources/
│   │       ├── views/                           # FXML Files
│   │       │   ├── login.fxml
│   │       │   ├── signup.fxml
│   │       │   ├── student-dashboard.fxml
│   │       │   ├── teacher-dashboard.fxml
│   │       │   ├── add-question.fxml
│   │       │   ├── question-bank.fxml
│   │       │   ├── create-exam.fxml
│   │       │   ├── manage-exams.fxml
│   │       │   ├── manage-students.fxml
│   │       │   ├── exam-list.fxml
│   │       │   ├── take-exam.fxml
│   │       │   ├── view-results.fxml
│   │       │   ├── practice.fxml
│   │       │   └── practice-session.fxml
│   │       │
│   │       └── css/
│   │           └── dark-theme.css               # Styling
│   │
│   └── test/                                    # Unit Tests (if any)
│
├── users.txt                                    # User data (auto-generated)
├── questions.dat                                # Questions (auto-generated)
├── exams.dat                                    # Exams (auto-generated)
├── results.dat                                  # Results (auto-generated)
├── forum_data.ser                               # Forum data (auto-generated)
│
├── pom.xml                                      # Maven configuration
├── build.gradle                                 # Gradle configuration (alternative)
├── README.md                                    # This file
└── .gitignore
```

---

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17+ | Core programming language |
| JavaFX | 21 | GUI framework |
| Maven/Gradle | Latest | Build automation |
| Java Serialization | Built-in | Data persistence |
| Java Sockets | Built-in | Networking (optional) |
| CSS | 3 | UI styling |

### Key JavaFX Components Used

- **Controls:** Button, Label, TextField, TextArea, TableView, ComboBox, RadioButton, Spinner
- **Layouts:** VBox, HBox, BorderPane, GridPane, StackPane, FlowPane
- **Animation:** Timeline (for exam timer)
- **Dialogs:** Alert (for confirmations and messages)

---

## 🔑 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Student | `student` | `123` |
| Teacher | `teacher` | `123` |

**Note:** These default accounts are created automatically on first run if `users.txt` doesn't exist.

---

## 🐛 Troubleshooting

### Issue: "Error: JavaFX runtime components are missing"

**Solution:**
```bash
# Add JavaFX modules to VM options
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

### Issue: "Could not load FXML file"

**Solution:**
- Verify FXML files are in `src/main/resources/views/`
- Check that paths in controllers use `/views/filename.fxml`
- Rebuild the project

### Issue: "CSS not found"

**Solution:**
- Ensure `dark-theme.css` is in `src/main/resources/css/`
- Check resource loading in `Main.java` and `SceneManager.java`

### Issue: "Scene cannot be loaded"

**Solution:**
- Missing imports in FXML (e.g., `<?import javafx.scene.control.ScrollPane?>`)
- Check FXML for syntax errors in Scene Builder
- Verify controller class name matches exactly (case-sensitive)

### Issue: Data files not saving

**Solution:**
- Check file permissions in the application directory
- Ensure services are calling `save()` methods
- Look for console error messages

### Issue: Server won't start (Networking mode)

**Solution:**
```bash
# Port 5000 might be in use
netstat -an | grep 5000  # Check if port is occupied
# Change SERVER_PORT in Server.java if needed
```

### Issue: Build fails with Maven

**Solution:**
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if they're failing
mvn clean install -DskipTests
```

---

## 🎨 Customization

### Changing the Theme

Edit `src/main/resources/css/dark-theme.css`:

```css
/* Change primary color */
.button {
    -fx-background-color: #YOUR_COLOR;
}

/* Change background */
.root {
    -fx-background-color: #YOUR_BG_COLOR;
}
```

### Adding New Subjects

Edit the subject list in relevant controllers:

```java
subjectCombo.getItems().addAll(
    "Mathematics", "Physics", "Chemistry", "Biology",
    "Computer Science", "English", "History", "Geography",
    "Your New Subject"  // Add here
);
```

### Changing Grade Thresholds

Edit `Result.java`:

```java
public String getGrade() {
    if (percentage >= 90) return "A+";
    if (percentage >= 80) return "A";
    // Modify these thresholds
}
```

---

## 🔐 Data Storage

EduTrack uses **Java Serialization** for data persistence:

- **users.txt** - User accounts (serialized `List<User>`)
- **questions.dat** - Question bank (serialized `List<Question>`)
- **exams.dat** - Exam definitions (serialized `List<Exam>`)
- **results.dat** - Exam results (serialized `List<Result>`)
- **forum_data.ser** - Forum posts (serialized `List<ForumPost>`)

### Data Location

Files are created in the **application's working directory** (where you run the app from).

### Backup Your Data

```bash
# Create backups regularly
cp users.txt users_backup.txt
cp questions.dat questions_backup.dat
cp exams.dat exams_backup.dat
cp results.dat results_backup.dat
```

---

## 🚀 Performance Considerations

- **Single-user mode:** All data stored in memory, fast access
- **Networking mode:** Multi-threaded server handles concurrent clients
- **File I/O:** Serialization is fast for small-medium datasets
- **Scalability:** For >1000 users/exams, consider migrating to a database

---

## 🔄 Future Enhancements

Potential improvements for the project:

- [ ] Database integration (MySQL/PostgreSQL)
- [ ] Question import/export (CSV, JSON)
- [ ] Image support in questions
- [ ] Randomized question order
- [ ] Exam scheduling
- [ ] Email notifications
- [ ] PDF report generation
- [ ] Mobile app version
- [ ] Web-based interface
- [ ] Analytics dashboard
- [ ] Question categories/tags
- [ ] Difficulty adjustment algorithm

---

## 📝 License

This project is licensed under the MIT License.

```
MIT License

Copyright (c) 2026 EduTrack

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 👥 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java naming conventions
- Add JavaDoc comments to public methods
- Keep methods under 50 lines when possible
- Use meaningful variable names
- Add comments for complex logic

---

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Review closed issues on GitHub
3. Open a new issue with:
   - Error message/screenshot
   - Steps to reproduce
   - Your environment (OS, Java version, etc.)

---

## 🙏 Acknowledgments

- JavaFX team for the excellent GUI framework
- Gluon for Scene Builder and JavaFX distributions
- All contributors and testers

---

## 📊 Project Statistics

- **Lines of Code:** ~5,000+
- **Classes:** 25+
- **FXML Files:** 15+
- **Features:** 20+
- **Supported Roles:** 2 (Student, Teacher)

---

## 🎯 Quick Start Summary

```bash
# 1. Clone the repository
git clone https://github.com/AnikaTasnim106/Exam_system.git

# 2. Navigate to project directory
cd Exam_system

# 3. Build with Maven
mvn clean install

# 4. Run the application
mvn javafx:run

# 5. Login with default credentials
# Student: student / 123
# Teacher: teacher / 123
```

---

**Made with ❤️ for education**

**Happy Examining! 🎓**
