package com.aws.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class S3Operation implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
    private final String bucketName = "secondbucketsure"; // replace with your bucket name
    private final String objectKey = "mijay.txt"; // replace with your object key

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        switch (event.getHttpMethod().toLowerCase()) {
            case "get":
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(getObject());
            case "put":
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(putObject());
            case "delete":
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(deleteObject());
            default:
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Invalid operation");
        }
    }

    private String getObject() {
        S3Object s3Object = s3.getObject(new GetObjectRequest(bucketName, objectKey));
        try (InputStream objectData = s3Object.getObjectContent()) {
            return IOUtils.toString(objectData);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error getting object: " + e.getMessage();
        }
    }

    private String putObject() 
    {
        String objectContent = "This is my object content";
        byte[] contentBytes = objectContent.getBytes(StandardCharsets.UTF_8);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentBytes.length);

        InputStream inputStream = new ByteArrayInputStream(contentBytes);
        s3.putObject(new PutObjectRequest(bucketName, objectKey, inputStream, metadata));

        return "Object added to S3 bucket: " + bucketName + "/" + objectKey;
    }
    
    
    

    private String deleteObject() {
        s3.deleteObject(bucketName, objectKey);
        return "Object deleted from S3 bucket: " + bucketName + "/" + objectKey;
    }
}
