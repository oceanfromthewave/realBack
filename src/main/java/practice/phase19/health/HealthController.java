package practice.phase19.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController
{

	private final DataSource dataSource;

	public HealthController(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@GetMapping("/health")
	public ResponseEntity<Map<String, Object>> health()
	{
		boolean dbUp;
		try(Connection conn = dataSource.getConnection())
		{
			dbUp = conn.isValid(2);
		}
		catch(SQLException e)
		{
			dbUp = false;
		}

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", dbUp ? "UP" : "DOWN");
		body.put("db", dbUp ? "UP" : "DOWN");

		return dbUp ? ResponseEntity.ok(body) : ResponseEntity.status(503).body(body);
	}
}
