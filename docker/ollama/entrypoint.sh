#!/bin/bash
set -e

ollama serve &
OLLAMA_PID=$!

until ollama list > /dev/null 2>&1; do
    echo "Waiting for Ollama server to start..."
    sleep 1
done

if ! ollama list | grep -q "llama3.2"; then
    echo "Pulling llama3.2..."
    ollama pull llama3.2
fi

if ! ollama list | grep -q "nomic-embed-text"; then
    echo "Pulling nomic-embed-text..."
    ollama pull nomic-embed-text
fi

echo "Ollama ready with required models."

wait $OLLAMA_PID