# Storage

The Storage component is a shared directory structure used by both the Server and the Evaluator to manage test cases and user submissions.

## Structure

The storage is organized as follows:

- `/{problemId}/input/`: Contains input test cases (e.g., `input00.txt`, `input01.txt`).
- `/{problemId}/output/`: Contains expected output for the corresponding input test cases (e.g., `output00.txt`, `output01.txt`).
- `/submissions/`: Stores the source code of user submissions, named by their submission ID or a unique identifier.

## Usage

### Server
The Server writes user submissions to the `/submissions/` directory when a new solution is posted. It also reads from the problem directories when managing test cases.

### Evaluator
The Evaluator reads the submission code from `/submissions/` and the test cases from the respective problem's `input/` and `output/` directories to perform the evaluation.

