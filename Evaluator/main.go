package main

import (
	"log"
	"time"

	"github.com/moby/moby/client"
)

// Submission matches the Java model in the Server project
type Submission struct {
	SubmissionID int64     `json:"submissionId"`
	ProblemID    int64     `json:"problemId"`
	Username     string    `json:"username"`
	Language     string    `json:"language"`
	Submitted    time.Time `json:"submitted"`
	Verdict      string    `json:"verdict"`
}

func main() {
	// Initialize Database
	if err := InitDB(); err != nil {
		log.Fatalf("Failed to initialize database: %v", err)
	}
	log.Println("Connected to database")

	// Initialize Docker Client
	cli, err := client.New(client.FromEnv)
	if err != nil {
		log.Fatalf("Failed to create Docker client: %v", err)
	}
	defer cli.Close()

	// Start Queue Listener (blocks)
	StartQueueListener(cli)
}
