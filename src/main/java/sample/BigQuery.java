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

    private static TableResult result;

    public static void testBigQuery() throws InterruptedException, FileNotFoundException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(dateToBigQueryFormat(new Date(System.currentTimeMillis())));
                System.out.println(dateToBigQueryFormat(System.currentTimeMillis()));
                String projectId = "ingsw2021";
                java.io.File credentialsPath = new java.io.File(Objects.requireNonNull(getClass().getResource("../ingsw2021-bf069d24a538.json")).getPath());
                System.out.println(credentialsPath.getAbsolutePath());
                GoogleCredentials credentials = null;
                try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
                    credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                com.google.cloud.bigquery.BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials)
                        .setProjectId(projectId).build().getService();
                System.out.println("Datasets:");
                for (Dataset dataset : bigquery.listDatasets().iterateAll()) {
                    System.out.printf("%s%n", dataset.getDatasetId().getDataset());
                }
            }
        }).start();

    }

    public static void test2BigQuery() throws InterruptedException, IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.google.cloud.bigquery.BigQuery bigquery = null;
                try {
                    bigquery = BigQueryOptions.newBuilder().setProjectId("ingsw2021")
                            .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(Objects.requireNonNull(getClass().getResource("../ingsw2021-bf069d24a538.json")).getPath()))).build().getService();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                QueryJobConfiguration queryConfig =
                        QueryJobConfiguration.newBuilder(
                                        "SELECT user_id, event_name,(SELECT value.string_value FROM UNNEST(event_params)" +
                                                "WHERE key = 'firebase_screen_class' and value.string_value=\"ToolBarActivity\") AS class," +
                                                "geo.city as city " +
                                                "FROM `ingsw2021.analytics_260600984.events_"+dateToBigQueryFormat(System.currentTimeMillis()-(2*24 * 60 * 60 * 1000))+"` WHERE event_name=\"user_engagement\" and geo.city is not null " +
                                                "order by event_timestamp desc;")
                                .setUseLegacySql(false).build();
                System.out.println(queryConfig.getQuery());
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
                TableResult result = null;
                try {
                    result = queryJob.getQueryResults();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert result != null;
                for (FieldValueList row : result.iterateAll()) {
                    for (FieldValue fieldValue : row) {
                        System.out.println(fieldValue.getValue());
                    }
                }
            }
        }).start();

    }

    public static TableResult getDeviceList() throws InterruptedException, IOException {
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

}
