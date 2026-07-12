# EXACT SOURCE: Where "Mani Babu" is Coming From

## The Answer

**"Mani Babu" is NOT in your code or configuration files.**

It comes from: **Windows System Environment Variable `USERNAME`**

---

## Proof Chain

### 1. Terminal Verification
```powershell
PS> echo $env:USERNAME
Mani Babu
```
✓ Confirmed: Windows `USERNAME` env var = "Mani Babu"

### 2. Configuration Files Check
- ✗ `application.properties` → `username=defaultuser` (NOT "Mani Babu")
- ✗ `application-dev.properties` → `username=devuser` (NOT "Mani Babu")
- ✗ All other profile files → NO "Mani Babu" found
- ✗ Java source code → NO hardcoded "Mani Babu"
- ✗ IDE configurations → NO environment variables set

### 3. Code that Prints It
**File:** `spring-profiles/src/main/java/com/mani/learning/Profiles.java`

```java
@Component
public class Profiles {
    
    @Value("${username}")              // ← Looks for "username" property
    private String userName;
    
    @PostConstruct
    public void init(){
        System.out.println("username: " + userName);  // ← Prints "Mani Babu"
    }
}
```

### 4. How Spring Resolves "${username}"

```
Spring Boot 3.5.3 Property Resolution Order:
    ↓
Command-line args → Not passed
    ↓
JVM System Properties → Not set
    ↓
Environment Variables → FOUND: USERNAME=Mani Babu ✓
    ↓
application-dev.properties → username=devuser (not checked, already resolved)
    ↓
```

**Result:** Spring finds OS environment variable `USERNAME` (normalized to lowercase `username`) and uses "Mani Babu"

---

## Visual Flow

```
┌─────────────────────────────────────────────┐
│  @Value("${username}")                      │
│  private String userName;                   │
└────────────────┬────────────────────────────┘
                 │
                 ▼
      ┌──────────────────────┐
      │ Resolve "username"   │
      │ Property from Spring │
      └──────────┬───────────┘
                 │
    ┌────────────┴───────────────────┐
    │                                │
    ▼                                ▼
 Check                          Check
 application.properties          Windows
 & -dev files                    Environment
 username=devuser                Variables
 [LOWER PRIORITY]                USERNAME=Mani Babu
                                 [HIGHER PRIORITY]
    │                                │
    └────────────────┬───────────────┘
                     │
                     ▼
              ┌─────────────┐
              │ Use Value:  │
              │ Mani Babu ✓ │
              └─────────────┘
```

---

## Why This is Happening

**Spring Boot 3.5.3** (your version) automatically binds OS environment variables to Spring properties with this priority:

1. **High Priority:** Environment Variables (OS-level)
2. **Lower Priority:** Properties files (application.properties, application-dev.properties, etc.)

The environment variable `USERNAME` has a different scope/priority than config files.

---

## Exact Location Chain

```
Windows System
    ↓
Environment Variable: USERNAME = "Mani Babu"
    ↓
Spring Boot 3.5.3 Environment Binding
    ↓
Property Placeholder: ${username}
    ↓
Profiles.java @Value annotation
    ↓
System.out.println("username: " + userName)
    ↓
CONSOLE OUTPUT: "username: Mani Babu" ✓
```

---

## Summary Table

| Level | Source | Value | Used? |
|-------|--------|-------|-------|
| 1 | Windows Environment Variable | `USERNAME=Mani Babu` | ✓ YES |
| 2 | JVM Argument (-D) | (not set) | ✗ NO |
| 3 | application-dev.properties | `username=devuser` | ✗ NO |
| 4 | application.properties | `username=defaultuser` | ✗ NO |

**Conclusion:** Level 1 (OS Environment) wins and provides the value "Mani Babu"

---

## Files Created for Reference

1. **USERNAME_SOURCE_ANALYSIS.md** - Detailed investigation with all findings
2. **FIX_USERNAME_ISSUE.md** - 4 different solutions with step-by-step instructions
3. **Profiles.java** - Updated with debug output to verify this behavior

---

## Next Steps

Choose one of the solutions in `FIX_USERNAME_ISSUE.md`:
- **Option A (Recommended):** Rename property to `app.username` to avoid OS env var conflicts
- **Option B:** Override via JVM arguments
- **Option C:** Set custom environment variable
- **Option D:** Use legacy property processing

