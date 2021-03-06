/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bigquerydatatransfer;

// [START bigquerydatatransfer_create_amazons3_transfer]
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.bigquery.datatransfer.v1.CreateTransferConfigRequest;
import com.google.cloud.bigquery.datatransfer.v1.DataTransferServiceClient;
import com.google.cloud.bigquery.datatransfer.v1.ProjectName;
import com.google.cloud.bigquery.datatransfer.v1.TransferConfig;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Sample to create amazon s3 transfer config.
public class CreateAmazonS3Transfer {

  public static void main(String[] args) throws IOException {
    // TODO(developer): Replace these variables before running the sample.
    final String projectId = "MY_PROJECT_ID";
    String datasetId = "MY_DATASET_ID";
    String tableId = "MY_TABLE_ID";
    // Amazon S3 Bucket Uri with read role permission
    String sourceUri = "s3://your-bucket-name/*";
    String awsAccessKeyId = "MY_AWS_ACCESS_KEY_ID";
    String awsSecretAccessId = "AWS_SECRET_ACCESS_ID";
    String sourceFormat = "CSV";
    String fieldDelimiter = ",";
    String skipLeadingRows = "1";
    Map<String, Value> params = new HashMap<>();
    params.put(
        "destination_table_name_template", Value.newBuilder().setStringValue(tableId).build());
    params.put("data_path", Value.newBuilder().setStringValue(sourceUri).build());
    params.put("access_key_id", Value.newBuilder().setStringValue(awsAccessKeyId).build());
    params.put("secret_access_key", Value.newBuilder().setStringValue(awsSecretAccessId).build());
    params.put("source_format", Value.newBuilder().setStringValue(sourceFormat).build());
    params.put("field_delimiter", Value.newBuilder().setStringValue(fieldDelimiter).build());
    params.put("skip_leading_rows", Value.newBuilder().setStringValue(skipLeadingRows).build());
    TransferConfig transferConfig =
        TransferConfig.newBuilder()
            .setDestinationDatasetId(datasetId)
            .setDisplayName("Your Aws S3 Config Name")
            .setDataSourceId("amazon_s3")
            .setParams(Struct.newBuilder().putAllFields(params).build())
            .setSchedule("every 24 hours")
            .build();
    createAmazonS3Transfer(projectId, transferConfig);
  }

  public static void createAmazonS3Transfer(String projectId, TransferConfig transferConfig)
      throws IOException {
    try (DataTransferServiceClient client = DataTransferServiceClient.create()) {
      ProjectName parent = ProjectName.of(projectId);
      CreateTransferConfigRequest request =
          CreateTransferConfigRequest.newBuilder()
              .setParent(parent.toString())
              .setTransferConfig(transferConfig)
              .build();
      TransferConfig config = client.createTransferConfig(request);
      System.out.println("Amazon s3 transfer created successfully :" + config.getName());
    } catch (ApiException ex) {
      System.out.print("Amazon s3 transfer was not created." + ex.toString());
    }
  }
}
// [END bigquerydatatransfer_create_amazons3_transfer]
