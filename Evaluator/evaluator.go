package main

import (
	"archive/tar"
	"bytes"
	"context"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"

	"github.com/moby/moby/api/types/container"
	"github.com/moby/moby/client"
)

func EvaluateSubmission(cli *client.Client, sub Submission) string {
	ctx := context.Background()
	imageName := ""
	fileName := ""

	switch sub.Language {
	case "PYTHON_3_13":
		imageName = "judge-python:3.13"
		fileName = "solution.py"
	case "JAVA_21":
		imageName = "judge-java:21"
		fileName = "Solution.java"
	default:
		return fmt.Sprintf("Unsupported language: %s", sub.Language)
	}

	// Path to test cases
	storagePath, err := filepath.Abs(filepath.Join("..", "Storage", fmt.Sprintf("%d", sub.ProblemID)))
	if err != nil {
		return fmt.Sprintf("Failed to get absolute path for storage: %v", err)
	}

	// Create container with bind mount
	resp, err := cli.ContainerCreate(ctx, client.ContainerCreateOptions{
		Config: &container.Config{
			Image: imageName,
			Tty:   false,
		},
		HostConfig: &container.HostConfig{
			Binds: []string{
				fmt.Sprintf("%s:/testcases:ro", storagePath),
			},
		},
	})
	if err != nil {
		return fmt.Sprintf("Failed to create container: %v", err)
	}
	defer cli.ContainerRemove(ctx, resp.ID, client.ContainerRemoveOptions{Force: true})

	// Read code from storage
	submissionFilePath := filepath.Join("..", "Storage", "submissions", fmt.Sprintf("%d", sub.SubmissionID))
	codeBytes, err := os.ReadFile(submissionFilePath)
	if err != nil {
		return fmt.Sprintf("Failed to read submission file: %v", err)
	}

	// Prepare code file to copy
	filesToCopy := map[string]string{
		fileName: string(codeBytes),
	}

	if err := copyFilesToContainer(ctx, cli, resp.ID, filesToCopy); err != nil {
		return fmt.Sprintf("Failed to copy code: %v", err)
	}

	// Start container
	if _, err := cli.ContainerStart(ctx, resp.ID, client.ContainerStartOptions{}); err != nil {
		return fmt.Sprintf("Failed to start container: %v", err)
	}

	// Wait for container to finish
	waitRes := cli.ContainerWait(ctx, resp.ID, client.ContainerWaitOptions{
		Condition: container.WaitConditionNotRunning,
	})
	select {
	case err := <-waitRes.Error:
		if err != nil {
			return fmt.Sprintf("Error waiting for container: %v", err)
		}
	case <-waitRes.Result:
	}

	// Get logs to see the result from entrypoint script
	out, err := cli.ContainerLogs(ctx, resp.ID, client.ContainerLogsOptions{ShowStdout: true, ShowStderr: true})
	if err != nil {
		return fmt.Sprintf("Failed to get logs: %v", err)
	}
	defer out.Close()

	var stdout bytes.Buffer
	_, err = io.Copy(&stdout, out)
	if err != nil {
		return fmt.Sprintf("Failed to read logs: %v", err)
	}

	return strings.TrimSpace(cleanDockerLogs(stdout.Bytes()))
}

func copyFilesToContainer(ctx context.Context, cli *client.Client, containerID string, files map[string]string) error {
	var buf bytes.Buffer
	tw := tar.NewWriter(&buf)

	for name, content := range files {
		err := tw.WriteHeader(&tar.Header{
			Name: name,
			Mode: 0644,
			Size: int64(len(content)),
		})
		if err != nil {
			return err
		}
		tw.Write([]byte(content))
	}
	tw.Close()

	_, err := cli.CopyToContainer(ctx, containerID, client.CopyToContainerOptions{
		DestinationPath: "/app",
		Content:         &buf,
	})
	return err
}

func cleanDockerLogs(logs []byte) string {
	var result strings.Builder
	for i := 0; i < len(logs); {
		if i+8 > len(logs) {
			break
		}
		size := int(logs[i+4])<<24 | int(logs[i+5])<<16 | int(logs[i+6])<<8 | int(logs[i+7])
		i += 8
		if i+size > len(logs) {
			result.WriteString(string(logs[i:]))
			break
		}
		result.WriteString(string(logs[i : i+size]))
		i += size
	}
	return result.String()
}
