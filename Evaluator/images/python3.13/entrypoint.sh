#!/bin/sh
# solution.py is expected to be in /app
# test cases are mounted at /testcases

for input_file in /testcases/input/input*.txt; do
    # Extract ID from inputID.txt
    filename=$(basename "$input_file")
    id=${filename#input}
    id=${id%.txt}
    expected_file="/testcases/output/output$id.txt"
    
    if [ ! -f "$expected_file" ]; then
        echo "TEST_CASE $id: MISSING_EXPECTED_OUTPUT"
        exit 1
    fi

    timeout --signal=SIGTERM "${TIME_LIMIT}s" python3 solution.py < "$input_file" > "actual_$id.txt" 2> "stderr_$id.txt"
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 124 ]; then
        echo "Time Limit Exceeded"
        exit 0
    fi

    if [ $EXIT_CODE -ne 0 ]; then
        echo "Runtime error on test $id"
        exit 0
    fi
    # Compare output (ignoring trailing whitespace/newlines)
    diff -Z "actual_$id.txt" "$expected_file" > /dev/null
    if [ $? -eq 0 ]; then
        continue
    else
        echo "Wrong answer on test $id"
        exit 0
    fi
done

echo "Accepted"
