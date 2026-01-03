package main

import (
	"database/sql"

	_ "github.com/lib/pq"
)

var db *sql.DB

func InitDB() error {
	var err error
	db, err = sql.Open("postgres", "host=localhost port=5432 user=postgres password=postgres dbname=programming_judge sslmode=disable")
	if err != nil {
		return err
	}
	return db.Ping()
}

func UpdateVerdict(submissionID int64, verdict string) error {
	_, err := db.Exec("UPDATE submissions SET verdict = $1 WHERE submission_id = $2", verdict, submissionID)
	return err
}
