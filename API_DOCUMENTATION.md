# API Documentation — Support Ticket AI Assistant

Base URL (local): `http://localhost:8080`

---

## 1. Ticket Status (Tool-calling)

**`GET /api/tickets/status`**

Answers ticket status questions using LLM tool-calling. The LLM calls the `getTicketStatus` tool against mock ticket data and returns the result — no RAG/document context is used here.

**Query Parameters**

| Name | Type | Required | Description |
|---|---|---|---|
| `query` | string | Yes | The user's ticket-related question |

**Example Request**
```bash
curl "http://localhost:8080/api/tickets/status?query=what is the ticket status of 1001"
```

**Example Response**
```
The current status of ticket #1001 is "Open".
```

**Notes**
- If the ticket ID is not found in the mock data, the tool returns a "No ticket found" message.
- Backed by `TicketService` and `TicketStatusTools`.

---

## 2. Ask (RAG Pipeline)

**`GET /api/ask`**

Answers general questions using Retrieval-Augmented Generation. Relevant document chunks are retrieved from the vector store and the LLM is restricted to answering only from that context.

**Query Parameters**

| Name | Type | Required | Description |
|---|---|---|---|
| `query` | string | Yes | The user's question |

**Example Request**
```bash
curl "http://localhost:8080/api/ask?query=how do I reset my password"
```

**Example Response (context found)**
```
To reset your password, go to the login page and click "Forgot Password"...
```

**Example Response (no relevant context found)**
```
Sorry, unable to find feasible answer. Contact a Human Agent.
```

**Behavior**
- Answers are restricted to the retrieved document context only (no general LLM knowledge is used).
- If no relevant context is found, a fixed fallback message is returned instead of a generated answer.
- Does not use tool-calling — see `/api/tickets/status` for ticket queries.

---

## 3. Retrieve Similar Chunks

**`GET /api/retrieve`**

Runs a similarity search against the vector store and returns the raw matching document chunks, without any LLM generation. Useful for debugging retrieval quality independent of the RAG answer pipeline.

**Query Parameters**

| Name | Type | Required | Description |
|---|---|---|---|
| `query` | string | Yes | The text to search for similar chunks |

**Example Request**
```bash
curl "http://localhost:8080/api/retrieve?query=how do I reset my password"
```

**Example Response**
```json
[
  {
    "id": "a1b2c3d4-...",
    "text": "How to Reset Your Password\n\nIf you forgot your password...",
    "metadata": {
      "source": "password_reset.txt"
    }
  }
]
```

**Notes**
- Returns up to `TOP_K` (default: 4) chunks with similarity above the configured threshold (default: 0.5).
- Returns an empty array if no chunks meet the similarity threshold.

---

## Known Limitations

- Chunk overlap is not currently implemented in the ingestion pipeline (Spring AI's `TokenTextSplitter` limitation).
- RAG (`/api/ask`) and tool-calling (`/api/tickets/status`) are intentionally kept as separate endpoints/services — combining context-restriction rules and tool-calling in a single prompt was unreliable with the local model (`llama3.2` via Ollama).

---

## Authentication

None currently — all endpoints are open. Not intended for production use without adding authentication (e.g. JWT/Okta) before deployment.
