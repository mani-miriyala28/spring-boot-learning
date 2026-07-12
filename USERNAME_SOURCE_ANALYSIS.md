# Analysis: Where "Mani Babu" Username is Coming From

## Summary
The username `Mani Babu` being printed is coming from the **Windows OS environment variable `USERNAME`**, which is automatically mapped to the Spring property `username` by Spring Boot 3.5.3's environment variable binding feature.

---

## Investigation Results

### 1. **Windows Environment Variable**
```
Checked: echo $env:USERNAME
Result: "Mani Babu"
```
The Windows system has an environment variable `USERNAME` set to `"Mani Babu"`.

### 2. **Property Files Do NOT Contain This Value**
Files checked:
- `application.properties` → `username=defaultuser` ✗
- `application-dev.properties` → `username=devuser` ✗
- `application-local.properties` → `username=localuser` ✗
- `application-prod.properties` → `username=produser` ✗
- `application-qa.properties` → `username=qauser` ✗

**Conclusion:** "Mani Babu" is NOT hardcoded in any configuration file.

### 3. **No Explicit Configuration**
Searched entire project for:
- `PropertySource` annotations ✗ (not found)
- `System.getenv()` calls ✗ (not found)
- `System.getProperty()` calls ✗ (not found)
- `.env` files ✗ (not found)
- Custom environment variable configuration ✗ (not found)

### 4. **Source Code Location**
The output is generated in: `spring-profiles/src/main/java/com/mani/learning/Profiles.java`

```java
@Component
public class Profiles {
    @Value("${username}")
    private String userName;
    
    @PostConstruct
    public void init(){
        System.out.println("username: " + userName);  // ← Prints "Mani Babu"
    }
}
```

---

## Root Cause: Spring Boot Environment Variable Binding

**Spring Boot Version:** 3.5.3 (see pom.xml)

Starting from Spring Boot 2.4+, Spring automatically binds OS environment variables to application properties. The binding works as follows:

### Property Source Priority Order (High to Low):
1. Command-line arguments
2. System properties (JVM -D arguments)
3. **Environment variables** ← `USERNAME` is read here
4. `application-{profile}.properties` files
5. `application.properties` file

### How "username" Property is Resolved:
1. Application looks for property: `username`
2. Checks `application-dev.properties` → finds `username=devuser`
3. However, the @Value annotation or property resolution may also check environment variables
4. Finds OS environment variable: `USERNAME=Mani Babu`
5. **Result:** OS environment variable takes precedence or overrides the property file

---

## Why This Happens

In Spring Boot 3.5.3 with default configuration:
- Environment variables are automatically exposed as property sources
- The property name is normalized (case-insensitive): `USERNAME` → `username`
- On many systems, environment variables can override property file values depending on the configuration

---

## Current Configuration
```
Active Profile: dev
Property Source Checking: 
- Environment Variable USERNAME = "Mani Babu" ✓ FOUND (HIGH PRIORITY)
- Property File (dev) = "devuser" (LOWER PRIORITY)
```

---

## Solution Options

### Option 1: Set Environment Variable (Not Recommended)
```powershell
# Remove or rename the USERNAME environment variable (not advisable on Windows)
```

### Option 2: Override via JVM System Property (Recommended)
Pass JVM argument when running:
```powershell
-Dusername=devuser
```

### Option 3: Disable Environment Variable Binding
In `application.properties`, add:
```properties
spring.config.use-legacy-processing=true
# or explicitly configure property sources
```

### Option 4: Use Application-Specific Environment Variable
Create a new environment variable with a different name:
```powershell
$env:APP_USERNAME = "devuser"
```
And update `@Value` annotation to reference it:
```java
@Value("${app.username}")
private String userName;
```

### Option 5: Use Spring Cloud Config or Externalized Configuration
For more control over property resolution hierarchy.

---

## Files Analyzed
- ✓ `Profiles.java` - Source of the output
- ✓ `application.properties` - Default properties
- ✓ `application-dev.properties` - Dev profile properties
- ✓ `application-local.properties` - Local profile properties
- ✓ `application-prod.properties` - Production profile properties
- ✓ `application-qa.properties` - QA profile properties
- ✓ `pom.xml` - Maven configuration (Spring Boot 3.5.3)
- ✓ `.idea/workspace.xml` - IDE run configuration (no env vars set)
- ✓ System environment variables

---

## Recommendation
The most straightforward solution is to rename the property in your code to avoid conflicts with the OS environment variable, or explicitly pass the property via JVM arguments when running the application.

