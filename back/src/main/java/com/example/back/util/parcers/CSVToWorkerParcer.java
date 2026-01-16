package com.example.back.util.parcers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.example.back.model.dto.Address;
import com.example.back.model.dto.Color;
import com.example.back.model.dto.Coordinates;
import com.example.back.model.dto.Country;
import com.example.back.model.dto.Location;
import com.example.back.model.dto.Organization;
import com.example.back.model.dto.OrganizationType;
import com.example.back.model.dto.Person;
import com.example.back.model.dto.Position;
import com.example.back.model.dto.Status;
import com.example.back.model.dto.Worker;

public class CSVToWorkerParcer implements Parcer<InputStream, List<Worker>> {

    @Override
    public List<Worker> parse(InputStream toParce) {
        List<Worker> workers = new ArrayList<>();

        try (Reader reader = new BufferedReader(
                new InputStreamReader(toParce, StandardCharsets.UTF_8))) {

            CSVParser csvParser = CSVFormat.RFC4180
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .build()
                    .parse(reader);

            System.out.println("CSV Headers: " + csvParser.getHeaderNames());

            for (CSVRecord record : csvParser) {
                System.out.println("Processing record: " + record.toString());

                if (record.size() == 0) {
                    continue;
                }

                Worker worker = new Worker();

                try {
                    worker.setName(record.get("name"));
                    worker.setCoordinates(
                            new Coordinates(Double.parseDouble(record.get("x")), Integer.parseInt(record.get("y"))));
                    worker.setStartDate(LocalDate.parse(record.get("start_date")));
                    worker.setPosition(Position.stringToPosition(record.get("position")));
                    worker.setStatus(Status.stringToStatus(record.get("status")));
                    worker.setSalary(Double.parseDouble(record.get("salary")));
                    worker.setRating(Double.parseDouble(record.get("rating")));
                    worker.setOrganization(
                            new Organization(
                                    new Address(record.get("org_street"), record.get("org_zip_code"),
                                            new Location(
                                                    Double.parseDouble(record.get("org_x")),
                                                    Integer.parseInt(record.get("org_y")),
                                                    Integer.parseInt(record.get("org_z")),
                                                    record.get("org_name"))),
                                    Long.parseLong(record.get("annual_turnover")),
                                    Long.parseLong(record.get("employees_count")),
                                    Double.parseDouble(record.get("org_rating")),
                                    OrganizationType.stringToOrganizationType(record.get("org_type"))));
                    worker.setPerson(new Person(Color.stringToColor(record.get("eye_color")),
                            Color.stringToColor(record.get("hair_color")),
                            new Location(
                                    Double.parseDouble(record.get("person_location_x")),
                                    Integer.parseInt(record.get("person_location_y")),
                                    Integer.parseInt(record.get("person_location_z")),
                                    record.get("person_location_name")),
                            record.get("passport_id"), Country.stringToCountry(record.get("nationality"))));

                    workers.add(worker);
                } catch (Exception e) {
                    System.err.println("Error processing record: " + record);
                    e.printStackTrace();
                }
            }

            System.out.println("Total workers parsed: " + workers.size());

        } catch (Exception e) {
            System.err.println("Error parsing CSV:");
            e.printStackTrace();
        }

        return workers;
    }
}