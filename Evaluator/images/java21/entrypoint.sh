#!/bin/sh
# Solution.java is expected to be in /app
# test cases are mounted at /testcases

# Compile the java code once
javac Solution.java 2> stderr_compile.txt
if [ $? -ne 0 ]; then
    echo "Compilation Error"
    exit 0
fi

for input_file in /testcases/input/input*.txt; do
    # Extract ID from inputID.txt
    filename=$(basename "$input_file")
    id=${filename#input}
    id=${id%.txt}
    expected_file="/testcases/output/output$id.txt"
    
    if [ ! -f "$expected_file" ]; then
        exit 1
    fi

    java Solution < "$input_file" > "actual_$id.txt" 2> "stderr_$id.txt"
    EXIT_CODE=$?
    
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
