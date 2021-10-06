package sample;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BigQuery {

    private static com.google.cloud.bigquery.BigQuery getBigQueryCredential() throws IOException {
        return BigQueryOptions.newBuilder().setProjectId("ingsw2021").setCredentials(ServiceAccountCredentials.fromStream(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("ingsw2021-bf069d24a538.json")))).build().getService();
    }

    private static String dateToBigQueryFormat(long date){
        return new SimpleDateFormat("yyyyMMdd").format(new Date(date));
    }

    private static String dateToBigQueryFormat(Date date){
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    private static TableResult getTableResult(TableResult result, com.google.cloud.bigquery.BigQuery bigquery, QueryJobConfiguration queryConfig) {
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

    public static TableResult getDeviceList() throws InterruptedException, IOException {
        TableResult result = null;
        com.google.cloud.bigquery.BigQuery bigquery = null;
        try {
            bigquery = getBigQueryCredential();
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                                """
                                        SELECT device.mobile_marketing_name as modello , COUNT(device.mobile_marketing_name) as conta  FROM `ingsw2021.analytics_260600984.events_*`
                                        where device.mobile_marketing_name is not null\s
                                        group by device.mobile_marketing_name order by conta desc;""")
                        .setUseLegacySql(false).build();
        return getTableResult(result, bigquery, queryConfig);
    }

    public static TableResult getRegions() throws InterruptedException, IOException {
        com.google.cloud.bigquery.BigQuery bigquery = null;
        TableResult result = null;
        try {
            bigquery = getBigQueryCredential();
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                                "SELECT geo.country,geo.region FROM `ingsw2021.analytics_260600984.events_*` group by geo.region,geo.country order by count(geo.region) desc")
                        .setUseLegacySql(false).build();
        return getTableResult(result, bigquery, queryConfig);
    }

    public static TableResult getCrashReport() throws InterruptedException, IOException {
        com.google.cloud.bigquery.BigQuery bigquery = null;
        TableResult result = null;
        try {
            bigquery = getBigQueryCredential();
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                                "SELECT  issue_id,issue_title, issue_subtitle,event_timestamp, operating_system.display_version, application.display_version FROM `ingsw2021.firebase_crashlytics.*`")
                        .setUseLegacySql(false).build();
        return getTableResult(result, bigquery, queryConfig);
    }

}
