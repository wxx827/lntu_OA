# Backend Development - Quick Start Guide

## Prerequisites
1. **Java JDK 8+** installed
2. **Maven 3.x** installed
3. **Oracle Database** running (localhost:1521)
4. **Tomcat 9.x** server

## Database Setup

### Step 1: Create Oracle User
```sql
-- Connect as SYSDBA
CREATE USER oa_system IDENTIFIED BY oa123456;
GRANT CONNECT, RESOURCE, DBA TO oa_system;
```

### Step 2: Run Schema Script
```bash
sqlplus oa_system/oa123456@localhost:1521/orcl
@src/main/resources/schema.sql
```

## Build & Deploy

### Step 1: Build Project
```bash
cd oa-backend
mvn clean package
```

### Step 2: Deploy to Tomcat
```bash
# Copy WAR file to Tomcat webapps
cp target/oa-backend.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME/bin/startup.bat # Windows
```

### Step 3: Verify Deployment
Open browser: `http://localhost:8080/oa-backend/`

## API Testing

### Test Car Management APIs

#### 1. Get All Cars
```bash
curl http://localhost:8080/oa-backend/api/car/list
```

#### 2. Add New Car
```bash
curl -X POST http://localhost:8080/oa-backend/api/car/add \
  -H "Content-Type: application/json" \
  -d '{
    "licensePlate": "京D88888",
    "brand": "BMW",
    "model": "5 Series",
    "seats": 5,
    "status": "AVAILABLE"
  }'
```

#### 3. Get Statistics
```bash
curl http://localhost:8080/oa-backend/api/car/stats
```

### Test Schedule API

```bash
curl "http://localhost:8080/oa-backend/api/schedule/my-events?userId=1&startDate=2026-01-01&endDate=2026-01-31"
```

## Configuration

### Update Database Connection
Edit `src/main/resources/jdbc.properties`:
```properties
jdbc.url=jdbc:oracle:thin:@YOUR_HOST:1521:YOUR_SID
jdbc.username=YOUR_USERNAME
jdbc.password=YOUR_PASSWORD
```

### Update CORS Settings
Edit `src/main/resources/spring-mvc.xml`:
```xml
<mvc:mapping path="/**"
             allowed-origins="http://localhost:5173"  <!-- Your frontend URL -->
```

## Troubleshooting

### Issue: Connection refused
- Verify Oracle is running: `lsnrctl status`
- Check firewall settings

### Issue: 404 Not Found
- Verify Tomcat is running
- Check deployment path: `$TOMCAT_HOME/webapps/oa-backend/`

### Issue: 500 Internal Server Error
- Check Tomcat logs: `$TOMCAT_HOME/logs/catalina.out`
- Verify database tables exist

## Next Steps
1. Implement JWT authentication
2. Add more business logic
3. Write unit tests
4. Integrate with frontend
