# Troubleshooting: Fix "Mani Babu" Username Issue

## Problem Summary
When running the Spring Profiles application with the "dev" profile active, the username prints as "Mani Babu" instead of the expected "devuser" from `application-dev.properties`.

---

## Root Cause
The Windows environment variable `USERNAME` (which is set to "Mani Babu") is being picked up by Spring Boot's environment variable binding feature and overriding the property file value.

---

## Verification (Debug Output Added)

The `Profiles.java` file has been updated with debug information. When you run the application, you'll now see:

```
=== DEBUG INFO ===
Active Profiles: [dev]
OS USERNAME env var: Mani Babu
username property from environment: Mani Babu
==================
```

This confirms the source is the OS environment variable.

---

## Fix Option A: Most Recommended - Rename Property Name

### Step 1: Update `Profiles.java`
Change from:
```java
@Value("${username}")
private String userName;
```

To:
```java
@Value("${app.username}")
private String userName;
```

### Step 2: Update all property files

**application.properties:**
```properties
app.username=defaultuser
app.password=defaultpassword
```

**application-dev.properties:**
```properties
app.username=devuser
app.password=devpassword
```

**application-local.properties:**
```properties
app.username=localuser
app.password=localpassword
```

**application-prod.properties:**
```properties
app.username=produser
app.password=prodpassword
```

**application-qa.properties:**
```properties
app.username=qauser
app.password=qapassword
```

**Benefit:** Avoids conflicts with system environment variables and makes your code more explicit.

---

## Fix Option B: JVM System Property Override

When running the application from IDE, add JVM arguments:

**In IntelliJ IDEA:**
1. Run → Edit Configurations
2. For "ProfilesApplication" configuration
3. Add to "VM options":
   ```
   -Dusername=devuser -Dpassword=devpassword
   ```

**Command line (if using java command):**
```bash
java -Dusername=devuser -Dpassword=devpassword -jar your-app.jar
```

**Benefit:** Simple override without code changes (temporary solution).

---

## Fix Option C: Disable Environment Variable Binding

Add to `application.properties`:
```properties
spring.config.use-legacy-processing=true
```

**Note:** This is not recommended as it disables useful features added in Spring Boot 2.4+.

---

## Fix Option D: Use Environment-Specific Prefix

### Step 1: Update Property Names
Use a more specific prefix like `app.db.username` instead of just `username`.

**Benefits:** 
- Eliminates conflicts with OS environment variables
- Makes properties self-documenting
- Better for complex applications with many properties

---

## Testing the Fix

After implementing a fix, run the application and verify:

```
username: devuser          ✓ (should show profile value, not "Mani Babu")
password: devpassword      ✓
```

---

## Recommended Approach

**Use Option A (Rename Properties) because:**
1. ✓ Eliminates conflicts with OS environment variables permanently
2. ✓ Makes code more maintainable and explicit
3. ✓ Follows Spring Boot best practices
4. ✓ No performance impact
5. ✓ Future-proof solution

---

## Spring Boot Environment Variable Mapping Rules

For reference, Spring Boot maps environment variables to properties using these rules:

| Environment Variable | Spring Property |
|---|---|
| `USERNAME` | `username` |
| `DB_HOST` | `db.host` |
| `APP_NAME_VALUE` | `app.name.value` |
| `SPRING_PROFILES_ACTIVE` | `spring.profiles.active` |

This is why `USERNAME` was picked up as `username` property.

---

## Additional Resources

- Spring Boot Configuration Documentation: https://spring.io/projects/spring-boot
- Environment Variable Binding (Spring Boot 2.4+): https://docs.spring.io/spring-boot/docs/3.5.3/reference/html/
- Property Sources in Spring: https://spring.io/blog/2011/02/15/spring-framework-caching-tutorial

