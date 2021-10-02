package com.example.clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import reactor.core.publisher.Mono;

import com.example.clock.service.TimezoneDBService;


@SpringBootApplication
@RestController
public class ClockApplication {

	Logger logger = LoggerFactory.getLogger(getClass().getName());
	@Value("${timezonedb.apikey}")
	private String apikey;
	@Value("${timezone.local}")
	private String localTimezone;
	@Value("${ratelimit.message}")
	private String limitmessage;

	@Autowired
	private TimezoneDBService timezoneService;


	public static void main(String[] args) {
		SpringApplication.run(ClockApplication.class, args);
	}

	@GetMapping("/api/v1/time/server")
	public Map<String, String>	 getServerTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String[] datetime = dtf.format(now).split(" ", 2);
		TimeZone timezone = TimeZone.getDefault();

		HashMap<String, String> map = new HashMap<>();
		map.put("date", datetime[0]);
		map.put("time", datetime[1]);
		map.put("timezone", timezone.getID());
		return map;
	}

	@GetMapping("/api/v1/time/manila")
	public ResponseEntity<Object> getManilaTime() {
		TimeZone timezone = TimeZone.getDefault();
		String fromTimezone = timezone.getID();
		long unixTime = Instant.now().getEpochSecond();
		HttpStatus status = HttpStatus.OK;

		String response = timezoneService
			.getManilaTime(apikey, fromTimezone, localTimezone, String.valueOf(unixTime))
			.block();

		if (response == limitmessage) {
			status = HttpStatus.TOO_MANY_REQUESTS;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			long timestamp = Long.parseLong(response) * 1000;
			Date date = new Date(timestamp);
			response = sdf.format(date);
		}

		return new ResponseEntity<Object>(Collections.singletonMap("time", response), status);
	}

}