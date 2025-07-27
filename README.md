[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=marcelldechant_bistro)](https://sonarcloud.io/summary/new_code?id=marcelldechant_bistro)

---

## How to run

### Using the Scripts

Requirements:

- Docker is installed on your machine.
- Maven is installed on your machine (for building the project).
- OpenJDK 21 or higher installed on your machine.
- Folder `csv` on your Desktop with the following file:
    - `products.csv` (this file is provided in the project repository in the folder `example`).

On Windows, you can use PowerShell to run the scripts. On Linux or macOS, you can use the terminal.

- If you are on Windows, you can run the `build-and-run-docker.ps1` script.
- If you are on Linux or macOS, you can run the `build-and-run-docker.sh` script.
    - The script was only tested on Windows Subsystem for Linux (WSL), but it should work on any Linux distribution and
      macOS.

> 💡 The scripts will build the Project with Maven, build the Docker image, and run the Docker container.

### Using Maven

Requirements:

- Maven is installed on your machine.
- OpenJDK 21 or higher installed on your machine.
- Create a folder `data/input` in the root directory of the project.
- Copy the `products.csv` file into the `data/input` folder.

Build the project with Maven:

```bash
mvn clean package
```

Run it by executing the following command:

```bash
java -jar target/bistro.jar
```

### Using IntelliJ IDEA

Requirements:

- IntelliJ IDEA is installed on your machine.
- OpenJDK 21 or higher installed on your machine.
- Create a folder `data/input` in the root directory of the project.
- Copy the `products.csv` file into the `data/input` folder.

When you are all set up, you can run the Bistro application directly from IntelliJ IDEA:

1. Open the project in IntelliJ IDEA.
2. Make sure the JDK is set to OpenJDK 21 or higher.
3. Open the `BistroApplication` class in the `src/main/java/com/github/marcelldechant/bistro` directory.
4. Right-click on the class and select `Run 'BistroApplication.main()'`.
5. The application will start, and you can see the output in the console.

> 💡 Sometimes IntelliJ IDEA does not recognize that this is a Maven project. In this case, you can right-click on the
`pom.xml` file and select `Add as Maven Project`. This will enable Maven support for the project.

## API Documentation

Once you have started the application, you can access the API documentation at the following URL:

```
http://localhost:8080/swagger-ui/index.html
```

> 💡 You can also find a Postman collection in the `postman` folder of the project repository. This collection contains
> all the endpoints of the Bistro API and can be imported into Postman for testing.

## Technologies Used

- Java 21 (OpenJDK 21)
- Spring Boot 3.5.4
- H2 Database
- Lombok
- Maven
- Docker
- [SonarCloud](https://sonarcloud.io/summary/overall?id=marcelldechant_bistro&branch=main)
- JUnit 5
- Mockito
- Postman