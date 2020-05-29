package com.fabric.sim;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;

import java.util.List;
import java.util.function.Consumer;

public class SQSMessagesFetcher {

    private final AmazonSQS sqs;
    private final String queueUrl;
    private final IncomingMessageReader incomingMessageReader;

    public SQSMessagesFetcher(String accessId, String secretKey, String queueName) {
        final AWSStaticCredentialsProvider credentialsProvider = createCredentialsProvider(accessId, secretKey);
        incomingMessageReader = new IncomingMessageReader(credentialsProvider);
        sqs = AmazonSQSClientBuilder.standard()
                .withRegion("eu-central-1")
                .withCredentials(credentialsProvider).build();
        this.queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
    }

    private AWSStaticCredentialsProvider createCredentialsProvider(String accessId, String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessId, secretKey));
    }

    public void processMessagesWith(Consumer<String> processor) {
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
        for(Message m : messages) {
            processor.accept(incomingMessageReader.readFrom(m.getBody()));
            sqs.deleteMessage(queueUrl, m.getReceiptHandle());
        }
    }
}
