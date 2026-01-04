package main

import (
	"encoding/json"
	"log"

	"github.com/docker/docker/client"
	amqp "github.com/rabbitmq/amqp091-go"
)

func StartQueueListener(cli *client.Client) {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	if err != nil {
		log.Fatalf("Failed to connect to RabbitMQ: %v", err)
	}
	defer conn.Close()

	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("Failed to open a channel: %v", err)
	}
	defer ch.Close()

	queueName := "submissionQueue"
	exchangeName := "submissionExchange"
	routingKey := "submission"

	err = ch.ExchangeDeclare(exchangeName, "topic", true, false, false, false, nil)
	if err != nil {
		log.Fatalf("Failed to declare an exchange: %v", err)
	}

	q, err := ch.QueueDeclare(queueName, false, false, false, false, nil)
	if err != nil {
		log.Fatalf("Failed to declare a queue: %v", err)
	}

	err = ch.QueueBind(q.Name, routingKey, exchangeName, false, nil)
	if err != nil {
		log.Fatalf("Failed to bind a queue: %v", err)
	}

	msgs, err := ch.Consume(q.Name, "", true, false, false, false, nil)
	if err != nil {
		log.Fatalf("Failed to register a consumer: %v", err)
	}

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")

	for d := range msgs {
		var submission Submission
		err := json.Unmarshal(d.Body, &submission)
		if err != nil {
			log.Printf("Error decoding JSON: %v. Body: %s", err, string(d.Body))
			continue
		}
		log.Printf("Received a submission: ID=%d, ProblemID=%d, User=%s, Language=%s",
			submission.SubmissionID, submission.ProblemID, submission.Username, submission.Language)

		// Set verdict to "Running"
		if err := UpdateVerdict(submission.SubmissionID, "Running"); err != nil {
			log.Printf("Failed to update verdict to Running: %v", err)
		}

		result := EvaluateSubmission(cli, submission)
		log.Printf("Evaluation result for ID=%d: %s", submission.SubmissionID, result)

		if err := UpdateVerdict(submission.SubmissionID, result); err != nil {
			log.Printf("Failed to update final verdict: %v", err)
		}
	}
}
