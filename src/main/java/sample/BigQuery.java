package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BigQuery {

    private static String dateToBigQueryFormat(long date){
        return new SimpleDateFormat("yyyyMMdd").format(new Date(date));
    }

    private static String dateToBigQueryFormat(Date date){
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }




    public static TableResult getDeviceList() throws InterruptedException, IOException {
        TableResult result = null;
        com.google.cloud.bigquery.BigQuery bigquery = null;
        try {
            bigquery = BigQueryOptions.newBuilder().setProjectId("ingsw2021")
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(Objects.requireNonNull(BigQuery.class.getResource("../ingsw2021-bf069d24a538.json")).getPath()))).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                                "SELECT device.mobile_marketing_name as modello , COUNT(device.mobile_marketing_name) as conta  FROM `ingsw2021.analytics_260600984.events_*`\n" +
                                        "where device.mobile_marketing_name is not null \n" +
                                        "group by device.mobile_marketing_name order by conta desc;")
                        .setUseLegacySql(false).build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
        try {
            queryJob = queryJob.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        try {
            result = queryJob.getQueryResults();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert result != null;
        return result;
    }

    public static TableResult getRegions() throws InterruptedException, IOException {
        com.google.cloud.bigquery.BigQuery bigquery = null;
        TableResult result = null;

        try {
            bigquery = BigQueryOptions.newBuilder().setProjectId("ingsw2021")
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(Objects.requireNonNull(BigQuery.class.getResource("../ingsw2021-bf069d24a538.json")).getPath()))).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                                "SELECT geo.country,geo.region FROM `ingsw2021.analytics_260600984.events_*` group by geo.region,geo.country order by count(geo.region) desc")
                        .setUseLegacySql(false).build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
        try {
            queryJob = queryJob.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        try {
            result = queryJob.getQueryResults();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert result != null;
        return result;
    }

    public static TableResult getCrashReport() throws InterruptedException, IOException {
        com.google.cloud.bigquery.BigQuery bigquery = null;
        TableResult result = null;

        try {
            bigquery = BigQueryOptions.newBuilder().setProjectId("ingsw2021")
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(Objects.requireNonNull(BigQuery.class.getResource("../ingsw2021-bf069d24a538.json")).getPath()))).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                                "SELECT  issue_id,issue_title, issue_subtitle,event_timestamp, operating_system.display_version, application.display_version FROM `ingsw2021.firebase_crashlytics.*`")
                        .setUseLegacySql(false).build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
        try {
            queryJob = queryJob.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        try {
            result = queryJob.getQueryResults();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert result != null;
        return result;
    }

}
