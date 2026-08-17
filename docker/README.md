# Docker Setup — Support Ticket AI Assistant

This directory runs the full stack (Spring Boot app + PostgreSQL/pgvector + Ollama) as three separate containers on a shared Docker network, similar to a real deployment.

## Directory Structure

```
docker/
├── docker-compose.yml       # Orchestrates all three services
├── .env.example             # Copy to .env and fill in values
├── app/
│   └── Dockerfile           # Spring Boot app (copies prebuilt jar)
├── postgres-pgvector/
│   ├── Dockerfile           # PostgreSQL 17 + pgvector built from source
│   └── 01-init-pgvector.sql # Auto-enables the vector extension on first start
└── ollama/
    ├── Dockerfile           # Ollama with custom entrypoint
    └── entrypoint.sh        # Starts server, pulls required models on first run
```

## Prerequisites

- Docker Desktop installed and running
- Repo cloned, with the Spring Boot project at `../staa` relative to this folder

## Steps to Run

**1. Build the application jar (from the `staa` project root)**
```bash
cd staa
mvn clean package -DskipTests
cd ..
```

**2. Set up environment variables**
```bash
cd docker
cp .env.example .env
```
Open `.env` and adjust `POSTGRES_PASSWORD` and other values as needed. `.env` should never be committed — it's already covered by `.gitignore`.

**3. Build and start all containers**
```bash
docker compose up --build
```

This will:
- Build a custom PostgreSQL image with pgvector compiled from source
- Build a custom Ollama image and, on first run, pull `llama3.2` and `nomic-embed-text`
- Build the Spring Boot app image from the prebuilt jar
- Start all three containers on a shared network, with the app waiting for PostgreSQL to be healthy before starting

**First run will take longer** — pgvector compilation and Ollama model downloads (~2.5GB total) both happen once. Subsequent runs reuse the persisted volumes and start much faster.

**4. Verify it's running**
```bash
curl "http://localhost:8080/api/ask?query=how do I reset my password"
```

**5. Stop everything**
```bash
docker compose down
```
Add `-v` to also remove volumes (Postgres data and Ollama models will be deleted, and re-downloaded/re-initialized next run):
```bash
docker compose down -v
```

## Notes

- Postgres data and Ollama models persist across restarts via named volumes (`postgres_data`, `ollama_models`), so `docker compose up` after the first run is fast.
- The app connects to Ollama and Postgres using their Docker Compose service names (`ollama`, `postgres`), not `localhost` — this only works inside the Docker network.
- To run the app locally outside Docker (e.g. from the IDE) against a local Ollama/Postgres install, the `application.properties` defaults (`localhost`) still apply — no code changes needed to switch between the two modes.
